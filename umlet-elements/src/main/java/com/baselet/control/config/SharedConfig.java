package com.baselet.control.config;

public class SharedConfig {
	private static final SharedConfig instance = new SharedConfig();

	public static SharedConfig getInstance() {
		return instance;
	}

	private boolean show_stickingpolygon = true;
	private boolean stickingEnabled = true;
	private boolean dev_mode = false; // TODO should be moved to a shared config class

	private boolean dark_mode = false;

	private SharedConfig() {}

	public boolean isShow_stickingpolygon() {
		return show_stickingpolygon;
	}

	public void setShow_stickingpolygon(boolean show_stickingpolygon) {
		this.show_stickingpolygon = show_stickingpolygon;
	}

	public boolean isStickingEnabled() {
		return stickingEnabled;
	}

	public void setStickingEnabled(boolean stickingEnabled) {
		this.stickingEnabled = stickingEnabled;
	}

	public boolean isDev_mode() {
		return dev_mode;
	}

	public void setDev_mode(boolean dev_mode) {
		this.dev_mode = dev_mode;
	}

	public boolean isDark_mode() {
		return dark_mode;
	}

	public void setDark_mode(boolean dark_mode) {
		this.dark_mode = dark_mode;
	}
}
