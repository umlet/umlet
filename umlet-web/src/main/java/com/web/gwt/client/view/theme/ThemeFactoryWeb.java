package com.web.gwt.client.view.theme;

import com.baselet.diagram.draw.helper.theme.ThemeFactory;

public class ThemeFactoryWeb extends ThemeFactory {
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
        changeTheme(ThemeFactory.THEMES.valueOf(themeString.toUpperCase()));
    }

    private static native String getTheme() /*-{
        return $wnd.theme;
    }-*/;

    public static native void exportChangeTheme() /*-{
        $wnd.changeTheme =
            $entry(@com.web.gwt.client.view.theme.ThemeFactoryWeb::changeTheme(Ljava/lang/String;))
    }-*/;
}
