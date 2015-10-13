package com.coleji.Symon;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.coleji.Shell.ShellManager;
import com.coleji.Shell.SSMTP.SSMTP;
import com.coleji.Util.MACAddress;
import com.coleji.Util.MD5Wrapper;
import com.coleji.Util.PropertiesWrapper;

public abstract class MonitorProgram {
	protected final String argStringSeparator = "$";
	
	protected static final Integer CHECK_RESULT_NORMAL = 1;
	protected static final Integer CHECK_RESULT_BAD = 2;
	protected static final Integer CHECK_RESULT_FAILED = 3;	// i.e. failed to complete the check and determine status
	
	protected String emailTo;
	protected String notifyProcURL;
	protected String salt;
	
	private Integer checkResult;
	protected String argString;
	
	protected void setProperties(PropertiesWrapper props) {
		this.emailTo = props.getProperty("email_to");
		this.notifyProcURL = props.getProperty("notify_proc_url");
		this.salt = props.getProperty("salt");
	}
	
	
	protected void setResultNormal() {
		this.checkResult = MonitorProgram.CHECK_RESULT_NORMAL;
		notifyServer();
	}
	
	protected void setResultBad(String errorMessage) {
		this.checkResult = MonitorProgram.CHECK_RESULT_BAD;
		String subClassName = this.getClass().getSimpleName();
		String hostName = ShellManager.getInstance().execute("hostname").getMainOutputLine(0);
		SSMTP.send(
				this.emailTo,
				"SYMON",
				"Result BAD for program " + subClassName + ", host " + hostName,
				hostName + ": " + errorMessage
		);
		notifyServer();
	}
	
	protected void setResultFailed(Exception e) {
		this.checkResult = MonitorProgram.CHECK_RESULT_FAILED;
		String subClassName = this.getClass().getSimpleName();
		String hostName = ShellManager.getInstance().execute("hostname").getMainOutputLine(0);
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		
		SSMTP.send(
				this.emailTo,
				"SYMON",
				"Result FAILED for program " + subClassName + ", host " + hostName,
				hostName + ": " + sw.toString()
		);
		notifyServer();
	}
	
	private void notifyServer() {
		String hostName = ShellManager.getInstance().execute("hostname").getMainOutputLine(0);
		String macAddress = MACAddress.getMACAddress();
		String subClassName = this.getClass().getSimpleName();
		String hashOriginal = this.salt + hostName + "-" + subClassName + "-" + this.argString + "-" + this.checkResult + "-" + macAddress + "-" + MonitorProgram.getDateString() + this.salt;
		String hash = MD5Wrapper.getMD5Hash(hashOriginal);
		String command = "wget -qO- " + this.notifyProcURL + 
				"?P_HOST_NAME=" + hostName.trim() + 
				"&P_PROGRAM_NAME=" + subClassName.trim() +
				"&P_ARG_STRING=" + this.argString + 
				"&P_STATUS=" + this.checkResult + 
				"&P_MAC=" + macAddress + 
				"&P_HASH=" + hash + 
				" &> /dev/null";
		ShellManager.getInstance().execute(command);
	}
	
	private static String getDateString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public Integer getResult() { return this.checkResult; }
}
