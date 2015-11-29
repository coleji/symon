package com.coleji.Symon.MonitorPrograms;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.coleji.Shell.CommandWrapper;
import com.coleji.Shell.ShellManager;
import com.coleji.Util.PropertiesWrapper;

public class BackupRan extends com.coleji.Symon.MonitorProgram {
	
	public BackupRan(PropertiesWrapper props, String directory) {
		this.setProperties(props);
		this.argString = "";
		SimpleDateFormat sdf = new SimpleDateFormat("Y-M-d");
		String dateString = sdf.format(new Date());
		try {
			String command = "test -e " + directory + "/" + dateString + ".tar.gz";
			CommandWrapper cw = ShellManager.getInstance().execute(command);
			int exitStatus = cw.getExitValue();
			if (exitStatus == 0) {
				this.setResultNormal();
			} else {
				this.setResultBad("DB Backup does not appear to have run");
			}
		} catch (Exception e) {
			this.setResultFailed(e);
		}
	}
}
