package com.baselet.control.config;

import java.io.File;

import javax.swing.UIManager;

import com.baselet.control.Constants;
import com.baselet.control.Constants.Os;
import com.baselet.control.Constants.SystemInfo;
import com.baselet.control.Program;
import com.baselet.control.SharedConstants.RuntimeType;

/**
 * holds all configuration settings from umlet.config
 * TODO move missing configuration entries from Constants class to this class
 */
public class Config {

	private static Config instance;

	public synchronized static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}

	private String uiManager;
	private String openFileHome = Constants.DEFAULT_FILE_HOME;
	private String saveFileHome = Constants.DEFAULT_FILE_HOME;
	private String programVersion;

	public Config() {
		initUiManager();
	}

	private void initUiManager() {
		// The default MacOS theme looks ugly, therefore we set metal
		if (SystemInfo.OS == Os.MAC) {
			uiManager = "javax.swing.plaf.metal.MetalLookAndFeel";
		}
		else if (Program.RUNTIME_TYPE == RuntimeType.ECLIPSE_PLUGIN && UIManager.getSystemLookAndFeelClassName().equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
			uiManager = "javax.swing.plaf.metal.MetalLookAndFeel";
		}
		else {
			uiManager = UIManager.getSystemLookAndFeelClassName();
		}
	}

	public String getUiManager() {
		return uiManager;
	}

	public void setUiManager(String uiManager) {
		this.uiManager = uiManager;
	}

	public String getOpenFileHome() {
		if (new File(openFileHome).exists()) {
			return openFileHome;
		}
		else { // if stored location doesn't exist, return default value
			return Constants.DEFAULT_FILE_HOME;
		}
	}

	public void setOpenFileHome(String openFileHome) {
		this.openFileHome = openFileHome;
	}

	public String getSaveFileHome() {
		if (new File(saveFileHome).exists()) {
			return saveFileHome;
		}
		else { // if stored location doesn't exist, return default value
			return Constants.DEFAULT_FILE_HOME;
		}
	}

	public void setSaveFileHome(String saveFileHome) {
		this.saveFileHome = saveFileHome;
	}

	public void setProgramVersion(String cfgVersion) {
		programVersion = cfgVersion;
	}

	public String getProgramVersion() {
		return programVersion;
	}
}
