package com.baselet.control.constants;

import com.baselet.control.enums.JavaImplementation;
import com.baselet.control.enums.Metakey;
import com.baselet.control.enums.Os;

public class SystemInfo {

	public static final Os OS;
	public static final JavaImplementation JAVA_IMPL;
	public static final String JAVA_VERSION = java.lang.System.getProperty("java.specification.version");
	public static final Metakey META_KEY;

	static {
		String os = java.lang.System.getProperty("os.name").toUpperCase();
		if (os.startsWith("WINDOWS")) {
			OS = Os.WINDOWS;
		}
		else if (os.startsWith("MAC")) {
			OS = Os.MAC;
		}
		else if (os.startsWith("LINUX")) {
			OS = Os.LINUX;
		}
		else if (os.contains("UNIX") || os.contains("BSD")) {
			OS = Os.UNIX;
		}
		else {
			OS = Os.UNKNOWN;
		}

		if (java.lang.System.getProperty("java.runtime.name").toUpperCase().contains("OPEN")) {
			JAVA_IMPL = JavaImplementation.OPEN;
		}
		else {
			JAVA_IMPL = JavaImplementation.SUN;
		}

		if (SystemInfo.OS == Os.MAC) {
			META_KEY = Metakey.CMD;
		}
		else {
			META_KEY = Metakey.CTRL;
		}
	}
}