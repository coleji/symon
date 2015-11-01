package com.coleji.Symon.MonitorPrograms;

import com.coleji.Shell.CommandWrapper;
import com.coleji.Shell.ShellManager;
import com.coleji.Util.PropertiesWrapper;

public class ProcessRunningCheck extends com.coleji.Symon.MonitorProgram {
	public ProcessRunningCheck(PropertiesWrapper props, String processName) {
		this.setProperties(props);
		this.argString = processName;
		try {
			CommandWrapper cw = ShellManager.getInstance().execute("pidof " + processName);
			String status = cw.getMainOutputLine(0);
			if (status == null) {
				this.setResultBad("Process " + processName + " is not running.");
			} else {
				this.argString += this.argStringSeparator + status;
				this.setResultNormal();
			}
		} catch (Exception e) {
			this.setResultFailed(e);
		}
	}
}
