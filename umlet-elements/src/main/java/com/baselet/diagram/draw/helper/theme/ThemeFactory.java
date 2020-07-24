package com.baselet.diagram.draw.helper.theme;

import java.util.ArrayList;
import java.util.List;

public class ThemeFactory {
    public enum THEMES {
        LIGHT, DARK
    }

    private static Theme theme;

    private static THEMES activeThemeEnum;

    private static final List<ThemeChangeListener> listeners;

    static {
        listeners = new ArrayList<ThemeChangeListener>();
    }

    public static void changeTheme(THEMES chosenTheme) {
        if (chosenTheme.equals(activeThemeEnum)) {
            return;
        }
        switch (chosenTheme) {
            case DARK:
                activeThemeEnum = chosenTheme;
                theme = new ThemeDark();
                break;
            case LIGHT:
                activeThemeEnum = chosenTheme;
                theme = new ThemeLight();
                break;
            default:
                activeThemeEnum = THEMES.LIGHT;
                theme = new ThemeLight();
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
}
