package com.coleji.Symon.MonitorPrograms;

import java.io.BufferedReader;

import com.coleji.Shell.CommandWrapper;
import com.coleji.Shell.ShellManager;
import com.coleji.Util.PropertiesWrapper;

public class DFCheck extends com.coleji.Symon.MonitorProgram {
	private static final int ARR_POSITION_DEVICE = 0;
	@SuppressWarnings("unused")
	private static final int ARR_POSITION_TOTAL = 1;
	@SuppressWarnings("unused")
	private static final int ARR_POSITION_USED = 2;
	@SuppressWarnings("unused")
	private static final int ARR_POSITION_AVAIL = 3;
	private static final int ARR_POSITION_USAGEPCT = 4;
	private static final int ARR_POSITION_MOUNTPT = 5;
	
	
	public DFCheck(PropertiesWrapper props, String device, String mountPoint, Integer alarmAtUsagePct) {
		this.setProperties(props);
		this.argString = device + this.argStringSeparator + mountPoint + this.argStringSeparator + alarmAtUsagePct;

		try {
			if (alarmAtUsagePct < 0 || alarmAtUsagePct > 100) {
				throw new Exception();
			}
			CommandWrapper cw = ShellManager.getInstance().execute("df");
			BufferedReader br = cw.getMainOutputAsBufferedReader();
			String line;
			
			int element = 0;
			while ((line = br.readLine()) != null) {
				String[] StringArr = line.split(" ");
				String[] StringArrTrimmed = new String[6];
				for (String s : StringArr) {
					if (s.equals("")) continue;
					StringArrTrimmed[element++] = s;
				}
				// StringArrTrimmed should look like e.g.  (device, total 1K, used, available, use%, mountpoint)
				// /dev/sda1
				// 240972
				// 168719
				// 59812
				// 74%
				// /boot

				
				if (StringArrTrimmed[ARR_POSITION_DEVICE] != device || StringArrTrimmed[ARR_POSITION_MOUNTPT] != mountPoint) {
					continue;
				}  // else, found it!
				
				// trim % off the end
				Integer usage = new Integer(StringArrTrimmed[ARR_POSITION_USAGEPCT].substring(0, StringArrTrimmed[ARR_POSITION_USAGEPCT].length() - 1));
				
				if (usage < alarmAtUsagePct) {
					this.setResultNormal();
				} else {
					this.setResultBad(usage + "% usage for " + StringArrTrimmed[ARR_POSITION_MOUNTPT] + ", alarm was set to " + alarmAtUsagePct + "%");
				}
				return;
			}
			this.setResultBad("Unable to df, no device " + device + " at mountpoint " + mountPoint);
		} catch (Exception e) {
			this.setResultFailed(e);
		}
	}
}
