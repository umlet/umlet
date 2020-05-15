package com.baselet.diagram.draw.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Theme {
    private static final Logger log = LoggerFactory.getLogger(Theme.class);

    public enum THEMES {
        LIGHT, DARK
    }

    private static ColorOwn colorOwn;

    private static THEMES currentTheme;

    private static final List<ThemeChangeListener> listeners;

    static {
        exportChangeTheme();
        listeners = new ArrayList<ThemeChangeListener>();
        changeTheme(getTheme());
    }

    private static void changeTheme(String themeString) {
        changeTheme(THEMES.valueOf(themeString));
    }

    public static void changeTheme(THEMES theme) {
        if (theme.equals(currentTheme)) {
            return;
        }
        switch (theme) {
            case DARK:
                currentTheme = theme;
                colorOwn = new ColorOwnDark();
                break;
            case LIGHT:
                currentTheme = theme;
                colorOwn = new ColorOwnLight();
                break;
            default:
                currentTheme = THEMES.LIGHT;
                colorOwn = new ColorOwnLight();
        }
        for (ThemeChangeListener listener : listeners) {
            listener.onThemeChange();
        }
    }

    public static ColorOwn getCurrentThemeColor() {
        if (colorOwn == null) {
            changeTheme(THEMES.LIGHT);
        }
        return colorOwn;
    }

    public static THEMES getCurrentTheme() {
        return currentTheme;
    }

    public static void addListener(ThemeChangeListener listener) {
        listeners.add(listener);
    }

    private static native String getTheme() /*-{
        return $wnd.theme;
    }-*/;

    public static native void exportChangeTheme() /*-{
        $wnd.changeTheme =
            $entry(@com.baselet.diagram.draw.helper.Theme::changeTheme(Ljava/lang/String;));
    }-*/;
}
