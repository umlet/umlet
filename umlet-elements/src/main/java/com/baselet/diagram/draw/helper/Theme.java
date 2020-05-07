package com.baselet.diagram.draw.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Theme {
    private static final Logger log = LoggerFactory.getLogger(Theme.class);

    public enum THEMES {
        LIGHT, DARK
    }

    private static ColorOwnBase colorOwn;

    private static THEMES currentTheme = THEMES.LIGHT;

    public static void init(THEMES theme) {
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
    }

    public static ColorOwnBase getCurrentThemeColor() {
        if(colorOwn == null) {
            init(THEMES.DARK);
        }
        return colorOwn;
    }
}
