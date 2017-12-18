package com.baselet.control.config;

import java.awt.Font;

public class DerivedConfig {

	public static Font getPanelHeaderFont() {
		return new Font(Font.SANS_SERIF, Font.BOLD, Config.getInstance().getPropertiesPanelFontsize());
	}

	public static Font getPanelContentFont() {
		return new Font(Font.MONOSPACED, Font.PLAIN, Config.getInstance().getPropertiesPanelFontsize());
	}

}
