package com.baselet.diagram.draw.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ThemeFactory {
    private static final Logger log = LoggerFactory.getLogger(ThemeFactory.class);

    public enum THEMES {
        LIGHT, DARK
    }

    private static Theme theme;

    private static THEMES activeThemeEnum;

    private static final List<ThemeChangeListener> listeners;

    static {
        exportChangeTheme();
        listeners = new ArrayList<ThemeChangeListener>();
        String theme = getTheme();
        if (theme != null) {
            changeTheme(getTheme());
        }
    }

    private static void changeTheme(String themeString) {
        changeTheme(THEMES.valueOf(themeString));
    }

    public static void changeTheme(THEMES chosenTheme) {
        if (chosenTheme.equals(activeThemeEnum)) {
            return;
        }
        switch (chosenTheme) {
            case DARK:
                activeThemeEnum = chosenTheme;
                theme = new ColorOwnDark();
                break;
            case LIGHT:
                activeThemeEnum = chosenTheme;
                theme = new ColorOwnLight();
                break;
            default:
                activeThemeEnum = THEMES.LIGHT;
                theme = new ColorOwnLight();
        }
        for (ThemeChangeListener listener : listeners) {
            listener.onThemeChange();
        }
    }

    public static Theme getCurrentTheme() {
        if (theme == null) {
            changeTheme(THEMES.LIGHT);
        }
        return theme;
    }

    public static THEMES getActiveThemeEnum() {
        return activeThemeEnum;
    }

    public static void addListener(ThemeChangeListener listener) {
        listeners.add(listener);
    }

    private static native String getTheme() /*-{
        return $wnd.theme;
    }-*/;

    public static native void exportChangeTheme() /*-{
        $wnd.changeTheme =
            $entry(@com.baselet.diagram.draw.helper.ThemeFactory::changeTheme(Ljava/lang/String;));
    }-*/;
}
