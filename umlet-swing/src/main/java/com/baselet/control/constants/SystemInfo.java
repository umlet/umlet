package com.baselet.control.constants;

import java.util.Locale;

import com.baselet.control.enums.Metakey;
import com.baselet.control.enums.Os;

public class SystemInfo {

	public static final Os OS;
	public static final String JAVA_VERSION = java.lang.System.getProperty("java.specification.version");
	public static final Metakey META_KEY;

	static {
		String os = java.lang.System.getProperty("os.name").toUpperCase(Locale.ENGLISH);
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

		if (SystemInfo.OS == Os.MAC) {
			META_KEY = Metakey.CMD;
		}
		else {
			META_KEY = Metakey.CTRL;
		}
	}
}