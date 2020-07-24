package com.web.gwt.client.view.theme;

import com.baselet.diagram.draw.helper.theme.ThemeFactory;

public class ThemeFactoryWeb extends ThemeFactory {
    private static boolean initialized = false;

    public ThemeFactoryWeb() {
        // We need to initialize theme retrieval on first initialisation of this class
        if (!ThemeFactoryWeb.initialized) {
            initialiseChangeListener();
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
        // If browser does not support this feature, choose light theme
        if ($wnd.matchMedia('(prefers-color-scheme)').media === 'not all') {
            return "LIGHT";
        }

        if ($wnd.matchMedia('(prefers-color-scheme: dark)').matches) {
            return "DARK";
        } else {
            return "LIGHT";
        }
    }-*/;

    private static native void initialiseChangeListener() /*-{
        var darkModeMediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
        var that = this;
        darkModeMediaQuery.addEventListener("change", function (e) {
            var darkMode = e.matches;
            if (darkMode) {
                @com.web.gwt.client.view.theme.ThemeFactoryWeb::changeTheme(Ljava/lang/String;)("DARK");
            } else {
                @com.web.gwt.client.view.theme.ThemeFactoryWeb::changeTheme(Ljava/lang/String;)("LIGHT");
            }
        })
    }-*/;
}
