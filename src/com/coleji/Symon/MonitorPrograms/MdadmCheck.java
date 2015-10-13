package com.coleji.Symon.MonitorPrograms;

import com.coleji.Shell.CommandWrapper;
import com.coleji.Shell.ShellManager;
import com.coleji.Util.PropertiesWrapper;

public class MdadmCheck extends com.coleji.Symon.MonitorProgram {
	private final Integer STATE_LINE_NUMBER = 11;
	private final String STATE_CLEAN_OUTPUT = "State : clean";
	
	public MdadmCheck(PropertiesWrapper props, String device) {
		this.setProperties(props);
		this.argString = device;
		try {
			CommandWrapper cw = ShellManager.getInstance().execute("mdadm --detail " + device);
			String status = cw.getMainOutputLine(STATE_LINE_NUMBER).trim();
			if (status.equals(STATE_CLEAN_OUTPUT)) {
				this.setResultNormal();
			} else {
				this.setResultBad("MdadmCheck returned the following non-clean status: " + status);
			}
		} catch (Exception e) {
			this.setResultFailed(e);
		}
	}
}