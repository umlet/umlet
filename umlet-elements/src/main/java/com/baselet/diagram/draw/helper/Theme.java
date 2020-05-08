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

    private static ColorOwnBase colorOwn;

    private static THEMES currentTheme = THEMES.LIGHT;

    private static List<ThemeChangeListener> listeners = new ArrayList<ThemeChangeListener>();

    public static void changeTheme(THEMES theme) {
        switch (theme) {
            case DARK:
                currentTheme = theme;
                colorOwn = new ColorOwnDark();
                break;
            case LIGHT:
                currentTheme = theme;
                colorOwn = new ColorOwnLight();
                break;
        }
        for(ThemeChangeListener listener : listeners) {
            listener.onThemeChange();
        }
    }

    public static ColorOwnBase getCurrentThemeColor() {
        if(colorOwn == null) {
            changeTheme(THEMES.DARK);
        }
        return colorOwn;
    }

    public static void addListener(ThemeChangeListener listener) {
        listeners.add(listener);
    }
}
