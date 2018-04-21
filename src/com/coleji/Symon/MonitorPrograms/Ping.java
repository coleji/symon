package com.coleji.Symon.MonitorPrograms;

import com.coleji.Shell.CommandWrapper;
import com.coleji.Shell.ShellManager;
import com.coleji.Util.PropertiesWrapper;

public class Ping extends com.coleji.Symon.MonitorProgram {
	public Ping(PropertiesWrapper props, String url) {
		this.setProperties(props);
		this.argString = url;
		try {
			String command = "curl " + url;
			CommandWrapper cw = ShellManager.getInstance().execute(command, null, 20000, null, null);
			String result = cw.getMainOutputAsString().trim();
			int exitStatus = cw.getExitValue();
			if (exitStatus == 0 && result.equals("pong")) {
				this.setResultNormal();
			} else {
				this.setResultBad("Api server at " + url + " is down");
			}
		} catch (Exception e) {
			this.setResultFailed(e);
		}
	}
}
