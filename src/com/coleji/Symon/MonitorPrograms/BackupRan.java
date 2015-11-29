package com.coleji.Symon.MonitorPrograms;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.coleji.Shell.CommandWrapper;
import com.coleji.Shell.ShellManager;
import com.coleji.Util.PropertiesWrapper;

public class BackupRan extends com.coleji.Symon.MonitorProgram {
	public static final String dummyText = "yes";
	
	public BackupRan(PropertiesWrapper props, String directory) {
		this.setProperties(props);
		this.argString = "";
		SimpleDateFormat sdf = new SimpleDateFormat("Y-M-d");
		String dateString = sdf.format(new Date());
		try {
			String command = "if test -e " + directory + "/" + dateString + ".tar.gz ; then echo \"" + dummyText + "\"; fi";
			CommandWrapper cw = ShellManager.getInstance().execute(command);
			String status = cw.getMainOutputLine(0);
			if (status != null && status.trim().equals(dummyText)) {
				this.setResultNormal();
			} else {
				this.setResultBad("DB Backup does not appear to have run");
			}
		} catch (Exception e) {
			this.setResultFailed(e);
		}
	}
}
