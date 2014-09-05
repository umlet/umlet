package com.baselet.control;

import javax.swing.UIManager;

import com.baselet.control.Constants.Os;
import com.baselet.control.Constants.SystemInfo;
import com.baselet.control.SharedConstants.Program;
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

	public Config() {
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
}
