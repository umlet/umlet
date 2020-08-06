package com.vscode.gwt.client.view.theme;

import com.baselet.diagram.draw.helper.theme.ThemeFactory;

public class ThemeFactoryVsCode extends ThemeFactory {
	private static boolean initialized = false;

	public ThemeFactoryVsCode() {
		// We need to initialize theme retrieval on first initialisation of this class
		if (!ThemeFactoryVsCode.initialized) {
			exportChangeTheme();
			String theme = getTheme();
			String backgroundColor = getBackgroundColor();
			if (theme != null) {
				changeTheme(theme, backgroundColor);
			}
			setInitialized();
		}
	}

	private static void setInitialized() {
		ThemeFactoryVsCode.initialized = true;
	}

	private static void changeTheme(String themeString, String backgroundColor) {
		changeTheme(ThemeFactory.THEMES.valueOf(themeString), backgroundColor, true);
	}

	private static native String getTheme() /*-{
		return $wnd.theme;
	}-*/;

	private static native String getBackgroundColor() /*-{
		return $wnd.backgroundColor;
	}-*/;

	public static native void exportChangeTheme() /*-{
		$wnd.changeTheme =
			$entry(@com.vscode.gwt.client.view.theme.ThemeFactoryVsCode::changeTheme(Ljava/lang/String;Ljava/lang/String;));
	}-*/;
}
