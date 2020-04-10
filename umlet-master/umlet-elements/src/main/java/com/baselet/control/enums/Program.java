package com.baselet.control.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PROGRAM, PLATTFORM AND JAVA SPECIFIC SETTINGS
 **/
public class Program {

	private static final Logger log = LoggerFactory.getLogger(Program.class);

	private static Program instance;

	public static Program getInstance() {
		if (!isInitialized()) {
			throw new RuntimeException("Program must be initialized before using it");
		}
		return instance;
	}

	public static void init(String version, RuntimeType runtimeType) {
		instance = new Program(version, runtimeType);
	}

	public static boolean isInitialized() {
		return instance != null;
	}

	private final RuntimeType runtimeType;
	private final String configName;
	private final String programName;
	private final String extension;
	private final String website;
	private final String version;

	private Program(String version, RuntimeType runtimeType) {
		log.info("Initializing Program: Version=" + version + ", Runtime=" + runtimeType);
		this.version = version;
		this.runtimeType = runtimeType;
		programName = "UMLet";
		extension = "uxf";
		website = "http://www." + getProgramName().toLowerCase() + ".com";

		if (runtimeType == RuntimeType.ECLIPSE_PLUGIN) {
			configName = getProgramName().toLowerCase() + "plugin.cfg";
		}
		else /* STANDALONE and BATCH */ {
			configName = getProgramName().toLowerCase() + ".cfg";
		}

	}

	public RuntimeType getRuntimeType() {
		return runtimeType;
	}

	public String getConfigName() {
		return configName;
	}

	public String getProgramName() {
		return programName;
	}

	public String getExtension() {
		return extension;
	}

	public String getWebsite() {
		return website;
	}

	public String getVersion() {
		return version;
	}

}