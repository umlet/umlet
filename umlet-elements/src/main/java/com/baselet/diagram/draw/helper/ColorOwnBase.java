package com.baselet.diagram.draw.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ColorOwnBase {
    private static final Logger log = LoggerFactory.getLogger(ColorOwnBase.class);
    public static final String EXAMPLE_TEXT = "color string (green,...) or code (#3c7a00,...)";

    public static enum Transparency {
        FOREGROUND(255), FULL_TRANSPARENT(0), DEPRECATED_WARNING(175), BACKGROUND(125), SELECTION_BACKGROUND(20);

        private int alpha;

        private Transparency(int alpha) {
            this.alpha = alpha;
        }

        public int getAlpha() {
            return alpha;
        }
    }

    public enum PredefinedColors {
        RED, GREEN, BLUE, YELLOW, MAGENTA, WHITE, BLACK, ORANGE, CYAN, DARK_GRAY, GRAY, LIGHT_GRAY, PINK, TRANSPARENT, NONE
    }

    public enum ColorStyle {
        SELECTION_FG, SELECTION_BG, STICKING_POLYGON, SYNTAX_HIGHLIGHTING, DEFAULT_FOREGROUND, DEFAULT_BACKGROUND
    }

    /* fields should be final to avoid changing parts of existing color object (otherwise unexpected visible changes can happen) */
    protected final int red;
    protected final int green;
    protected final int blue;
    protected final int alpha;

    protected Map<PredefinedColors, ColorOwnBase> colorMap;
    protected Map<ColorStyle, ColorOwnBase> styleColorMap;

    public ColorOwnBase(int red, int green, int blue, Transparency transparency) {
        this(red, green, blue, transparency.getAlpha());
    }

    public ColorOwnBase(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public ColorOwnBase(String hex) {
        int i = Integer.decode(hex);
        red = i >> 16 & 0xFF;
        green = i >> 8 & 0xFF;
        blue = i & 0xFF;
        alpha = ColorOwnLight.Transparency.FOREGROUND.getAlpha();
    }

    public ColorOwnBase() {
        this(0, 0, 0, 0);
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public ColorOwnBase transparency(ColorOwnLight.Transparency transparency) {
        return transparency(transparency.getAlpha());
    }

    public ColorOwnBase transparency(int alpha) {
        return new ColorOwnBase(getRed(), getGreen(), getBlue(), alpha);
    }

    public ColorOwnBase darken(int factor) {
        return new ColorOwnBase(Math.max(0, getRed() - factor), Math.max(0, getGreen() - factor), Math.max(0, getBlue() - factor), getAlpha());
    }

    /**
     * Converts colorString into a Color which is available in the colorMap or if not tries to decode the colorString
     *
     * @param colorString String which describes the color
     * @return Color which is related to the String or null if it is no valid colorString
     */
    public ColorOwnBase forStringOrNull(String colorString, Transparency transparency) {
        try {
            return forString(colorString, transparency);
        } catch (StyleException e) {
            return null;
        }
    }

    public ColorOwnBase forString(String colorString, Transparency transparency) {
        return forString(colorString, transparency.getAlpha());
    }

    /**
     * Converts colorString into a Color which is available in the colorMap or if not tries to decode the colorString
     *
     * @param colorString String which describes the color
     * @return Color which is related to the String or null if it is no valid colorString
     */
    public ColorOwnBase forString(String colorString, int transparency) {
        boolean error = false;
        ColorOwnBase returnColor = null;
        if (colorString == null) {
            error = true;
        } else {
            for (Map.Entry<PredefinedColors, ColorOwnBase> c : colorMap.entrySet()) {
                if (colorString.equalsIgnoreCase(c.getKey().toString())) {
                    returnColor = c.getValue();
                    break;
                }
            }
            if (returnColor == null) {
                try {
                    returnColor = new ColorOwnBase(colorString);
                } catch (NumberFormatException e) {
                    error = true;
                }
            }
            if (returnColor != null) {
                returnColor = returnColor.transparency(transparency);
            }
        }
        if (error) {
            throw new StyleException("value must be a " + EXAMPLE_TEXT);
        }
        return returnColor;
    }

    public Map<PredefinedColors, ColorOwnBase> getColorMap() {
        return colorMap;
    }

    public Map<ColorStyle, ColorOwnBase> getStyleColorMap() {
        return styleColorMap;
    }
}
