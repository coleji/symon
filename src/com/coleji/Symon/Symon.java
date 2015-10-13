package com.coleji.Symon;

import java.io.IOException;

import com.coleji.Symon.MonitorPrograms.MdadmCheck;
import com.coleji.Symon.MonitorPrograms.MountCheck;
import com.coleji.Util.PropertiesWrapper;

public class Symon {
	private static final int MONITOR_PROGRAM_MDADM_CHECK = 1;
	private static final int MONITOR_PROGRAM_MOUNT_CHECK = 2;

	public static void main(String[] args) {		
		if (args.length < 2) {
			System.out.println("Need location of config file, plus which program to run");
			System.exit(1);
		}
		try {
			String[] requiredProperties = {"email_to", "notify_proc_url", "salt"};
			PropertiesWrapper props = new PropertiesWrapper(args[0], requiredProperties);
			switch (new Integer (args[1])) {
			case Symon.MONITOR_PROGRAM_MDADM_CHECK:
				// device, e.g. "/dev/md0"
				new MdadmCheck(props, args[2]);
				break;
			case Symon.MONITOR_PROGRAM_MOUNT_CHECK:
				// devicename, mountpoint
				new MountCheck(props, args[2], args[3]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}