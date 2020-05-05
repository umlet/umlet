package com.baselet.diagram.draw.helper;

public class Theme {
    public enum THEMES {
        LIGHT_THEME, DARK_THEME
    }

    private static ColorOwnBase colorOwn;

    private static THEMES currentTheme = THEMES.LIGHT_THEME;

    public Theme(THEMES theme) {
        switch (theme) {
            case DARK_THEME:
                currentTheme = theme;
                colorOwn = new ColorOwnLight();
                break;
            case LIGHT_THEME:
                break;
        }
    }

    public static ColorOwnBase getCurrentThemeColor() {
        return colorOwn;
    }
}
