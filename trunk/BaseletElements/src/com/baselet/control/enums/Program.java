package com.baselet.control.enums;

/**
 * PROGRAM, PLATTFORM AND JAVA SPECIFIC SETTINGS
 **/
public class Program {

	private static final Program instance = new Program();

	public static Program getInstance() {
		return instance;
	}

	// Basically the RUNTIME_TYPE is STANDALONE until it gets overwritten after program startup
	private RuntimeType runtimeType = RuntimeType.STANDALONE;
	private String configName;
	private String programName;
	private String extension;
	private String website;
	private String version;

	public void init(String version) {
		programName = "UMLet";
		extension = "uxf";
		website = "http://www." + getProgramName().toLowerCase() + ".com";

		if (Program.getInstance().getRuntimeType() == RuntimeType.STANDALONE) {
			configName = getProgramName().toLowerCase() + ".cfg";
		}
		else {
			configName = getProgramName().toLowerCase() + "plugin.cfg";
		}

		this.version = version;
	}

	public RuntimeType getRuntimeType() {
		return runtimeType;
	}

	public void setRuntimeType(RuntimeType runtimeType) {
		this.runtimeType = runtimeType;
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