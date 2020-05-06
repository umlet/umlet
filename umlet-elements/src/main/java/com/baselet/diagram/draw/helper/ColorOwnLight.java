package com.baselet.diagram.draw.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;

public class ColorOwnLight extends ColorOwnBase {

    private static final Logger log = LoggerFactory.getLogger(ColorOwnLight.class);

    private final ColorOwnBase RED = new ColorOwnBase(255, 0, 0, Transparency.FOREGROUND);
    private final ColorOwnBase GREEN = new ColorOwnBase(0, 255, 0, Transparency.FOREGROUND);
    private final ColorOwnBase BLUE = new ColorOwnBase(0, 0, 255, Transparency.FOREGROUND);
    private final ColorOwnBase YELLOW = new ColorOwnBase(255, 255, 0, Transparency.FOREGROUND);
    private final ColorOwnBase MAGENTA = new ColorOwnBase(255, 0, 255, Transparency.FOREGROUND);
    private final ColorOwnBase WHITE = new ColorOwnBase(255, 255, 255, Transparency.FOREGROUND);
    private final ColorOwnBase BLACK = new ColorOwnBase(0, 0, 0, Transparency.FOREGROUND);
    private final ColorOwnBase ORANGE = new ColorOwnBase(255, 165, 0, Transparency.FOREGROUND);
    private final ColorOwnBase CYAN = new ColorOwnBase(0, 255, 255, Transparency.FOREGROUND);
    private final ColorOwnBase DARK_GRAY = new ColorOwnBase(70, 70, 70, Transparency.FOREGROUND);
    private final ColorOwnBase GRAY = new ColorOwnBase(120, 120, 120, Transparency.FOREGROUND);
    private final ColorOwnBase LIGHT_GRAY = new ColorOwnBase(200, 200, 200, Transparency.FOREGROUND);
    private final ColorOwnBase PINK = new ColorOwnBase(255, 175, 175, Transparency.FOREGROUND);

    private final ColorOwnBase TRANSPARENT = WHITE.transparency(Transparency.FULL_TRANSPARENT); // color white is important because EPS export doesn't support transparency, therefore background will be white
    private final ColorOwnBase SELECTION_FG = BLUE;
    private final ColorOwnBase SELECTION_BG = new ColorOwnBase(0, 0, 255, Transparency.SELECTION_BACKGROUND);
    private final ColorOwnBase STICKING_POLYGON = new ColorOwnBase(100, 180, 255, Transparency.FOREGROUND);
    private final ColorOwnBase SYNTAX_HIGHLIGHTING = new ColorOwnBase(0, 100, 255, Transparency.FOREGROUND);
    private final ColorOwnBase DEFAULT_FOREGROUND = BLACK;
    private final ColorOwnBase DEFAULT_BACKGROUND = TRANSPARENT;

    public ColorOwnLight() {
        generateColorMaps();
    }

    private void generateColorMaps() {
        HashMap<PredefinedColors, ColorOwnBase> colorMap = new HashMap<PredefinedColors, ColorOwnBase>();
        colorMap.put(PredefinedColors.BLACK, BLACK);
        colorMap.put(PredefinedColors.BLUE, BLUE);
        colorMap.put(PredefinedColors.CYAN, CYAN);
        colorMap.put(PredefinedColors.DARK_GRAY, DARK_GRAY);
        colorMap.put(PredefinedColors.GRAY, GRAY);
        colorMap.put(PredefinedColors.GREEN, GREEN);
        colorMap.put(PredefinedColors.LIGHT_GRAY, LIGHT_GRAY);
        colorMap.put(PredefinedColors.MAGENTA, MAGENTA);
        colorMap.put(PredefinedColors.ORANGE, ORANGE);
        colorMap.put(PredefinedColors.PINK, PINK);
        colorMap.put(PredefinedColors.RED, RED);
        colorMap.put(PredefinedColors.WHITE, WHITE);
        colorMap.put(PredefinedColors.YELLOW, YELLOW);
        colorMap.put(PredefinedColors.TRANSPARENT, TRANSPARENT);
        colorMap.put(PredefinedColors.NONE, TRANSPARENT);
        this.colorMap = Collections.unmodifiableMap(colorMap);

        HashMap<ColorStyle, ColorOwnBase> styleColorMap = new HashMap<ColorStyle, ColorOwnBase>();
        styleColorMap.put(ColorStyle.SELECTION_FG, SELECTION_FG);
        styleColorMap.put(ColorStyle.SELECTION_BG, SELECTION_BG);
        styleColorMap.put(ColorStyle.STICKING_POLYGON, STICKING_POLYGON);
        styleColorMap.put(ColorStyle.SYNTAX_HIGHLIGHTING, SYNTAX_HIGHLIGHTING);
        styleColorMap.put(ColorStyle.DEFAULT_FOREGROUND, DEFAULT_FOREGROUND);
        styleColorMap.put(ColorStyle.DEFAULT_BACKGROUND, DEFAULT_BACKGROUND);
        this.styleColorMap = Collections.unmodifiableMap(styleColorMap);
    }

    /*public ColorOwnLight transparency(Transparency transparency) {
        return transparency(transparency.getAlpha());
    }*/

    /*public ColorOwnLight transparency(int alpha) {
        return new ColorOwnLight(getRed(), getGreen(), getBlue(), alpha);
    }*/

    /*public ColorOwnLight darken(int factor) {
        return new ColorOwnLight(Math.max(0, getRed() - factor), Math.max(0, getGreen() - factor), Math.max(0, getBlue() - factor), getAlpha());
    }*/

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + alpha;
        result = prime * result + blue;
        result = prime * result + green;
        result = prime * result + red;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ColorOwnLight other = (ColorOwnLight) obj;
        if (alpha != other.alpha) {
            return false;
        }
        if (blue != other.blue) {
            return false;
        }
        if (green != other.green) {
            return false;
        }
        if (red != other.red) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ColorOwn [red=" + red + ", green=" + green + ", blue=" + blue + ", alpha=" + alpha + "]";
    }

}
