package com.coleji.Symon.MonitorPrograms;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.coleji.Shell.CommandWrapper;
import com.coleji.Shell.ShellManager;
import com.coleji.Util.PropertiesWrapper;

public class MountCheck extends com.coleji.Symon.MonitorProgram{
	public MountCheck(PropertiesWrapper props, String device, String mountPoint) {
		this.setProperties(props);
		this.argString = device + this.argStringSeparator + mountPoint;
		try {
			CommandWrapper cw = ShellManager.getInstance().execute("df");
			String patternString = "^" + device + "\\s(.*)\\s" + mountPoint + "$";
			Pattern p = Pattern.compile(patternString);
			String line;
			BufferedReader br = cw.getMainOutputAsBufferedReader();
			while ((line = br.readLine()) != null) {
				Matcher m = p.matcher(line);
				if (m.find()) {
					this.setResultNormal();
					return;
				}
			}
			this.setResultBad("MountCheck could not find device \"" + device + "\" mounted to location \"" + mountPoint + "\"\n\n" + cw.getMainOutputAsString());
		} catch (Exception e) {
			this.setResultFailed(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
