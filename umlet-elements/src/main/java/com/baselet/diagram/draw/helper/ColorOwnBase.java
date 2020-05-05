package com.baselet.diagram.draw.helper;

import java.util.Map;

public abstract class ColorOwnBase {

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
        RED, GREEN, BLUE, YELLOW, MAGENTA, WHITE, BLACK, ORANGE, CYAN, DARK_GRAY, GRAY, LIGHT_GRAY, PINK, TRANSPARENT
    }

    public enum ColorStyle {
        SELECTION_FG, SELECTION_BG, STICKING_POLYGON, SYNTAX_HIGHLIGHTING, DEFAULT_FOREGROUND, DEFAULT_BACKGROUND
    }

    /* fields should be final to avoid changing parts of existing color object (otherwise unexpected visible changes can happen) */
    protected final int red;
    protected final int green;
    protected final int blue;
    protected final int alpha;

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

    public ColorOwnLight transparency(ColorOwnLight.Transparency transparency) {
        return transparency(transparency.getAlpha());
    }

    public ColorOwnLight transparency(int alpha) {
        return new ColorOwnLight(getRed(), getGreen(), getBlue(), alpha);
    }

    public ColorOwnLight darken(int factor) {
        return new ColorOwnLight(Math.max(0, getRed() - factor), Math.max(0, getGreen() - factor), Math.max(0, getBlue() - factor), getAlpha());
    }

    public abstract Map<PredefinedColors, ColorOwnLight> getColorMap();

    public abstract Map<ColorStyle, ColorOwnLight> getStyleColorMap();

    public abstract ColorOwnLight forString(String colorString, Transparency transparency);

    public abstract ColorOwnLight forString(String colorString, int transparency);

    public abstract ColorOwnLight forStringOrNull(String colorString, Transparency transparency);
}
