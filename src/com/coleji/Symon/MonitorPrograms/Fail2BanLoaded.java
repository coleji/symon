package com.coleji.Symon.MonitorPrograms;

import com.coleji.Shell.CommandWrapper;
import com.coleji.Shell.ShellManager;
import com.coleji.Util.PropertiesWrapper;

public class Fail2BanLoaded extends com.coleji.Symon.MonitorProgram {
	private String LOADED_OUTPUT = "Server replied: pong";
	
	public Fail2BanLoaded(PropertiesWrapper props) {
		this.setProperties(props);
		this.argString = "";
		try {
			CommandWrapper cw = ShellManager.getInstance().execute("fail2ban-client ping");
			String status = cw.getMainOutputLine(0);
			if (status != null && status.trim().equals(LOADED_OUTPUT)) {
				this.setResultNormal();
			} else {
				this.setResultBad("Fail2Ban does not appear to be loaded; ping returned: " + status);
			}
		} catch (Exception e) {
			this.setResultFailed(e);
		}
	}
}
