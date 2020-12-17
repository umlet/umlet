package com.web.gwt.client.view.theme;

import com.baselet.diagram.draw.helper.theme.ThemeFactory;
import com.baselet.gwt.client.logging.CustomLogger;
import com.baselet.gwt.client.logging.CustomLoggerFactory;

public class ThemeFactoryWeb extends ThemeFactory {
	private static final CustomLogger log = CustomLoggerFactory.getLogger(ThemeFactoryWeb.class);

	private static boolean initialized = false;

	public ThemeFactoryWeb() {
		// We need to initialize theme retrieval on first initialisation of this class
		if (!ThemeFactoryWeb.initialized) {
			exportChangeTheme();
			String theme = getTheme();
			if (theme != null) {
				changeTheme(theme);
			}
			setInitialized();
		}
	}

	private static void setInitialized() {
		ThemeFactoryWeb.initialized = true;
	}

	private static void changeTheme(String themeString) {
		for (THEMES theme : THEMES.values()) {
			if (theme.toString().equals(themeString.toUpperCase())) {
				changeTheme(theme, null, true);
				return;
			}
		}
		log.error("The provided theme parameter is not valid! Expected: <light|dark>, Actual: " + themeString);
		changeTheme(THEMES.LIGHT, null, true);
	}

	private static native String getTheme() /*-{
        return $wnd.theme;
    }-*/;

	public static native void exportChangeTheme() /*-{
        $wnd.changeTheme =
            $entry(@com.web.gwt.client.view.theme.ThemeFactoryWeb::changeTheme(Ljava/lang/String;));
    }-*/;
}
