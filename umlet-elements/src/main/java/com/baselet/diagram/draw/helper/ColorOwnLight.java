package com.baselet.diagram.draw.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;

public class ColorOwnLight extends ColorOwn {
    private static final Logger log = LoggerFactory.getLogger(ColorOwnLight.class);

    private final ColorOwn RED = new ColorOwn(255, 0, 0, Transparency.FOREGROUND);
    private final ColorOwn GREEN = new ColorOwn(0, 255, 0, Transparency.FOREGROUND);
    private final ColorOwn BLUE = new ColorOwn(0, 0, 255, Transparency.FOREGROUND);
    private final ColorOwn YELLOW = new ColorOwn(255, 255, 0, Transparency.FOREGROUND);
    private final ColorOwn MAGENTA = new ColorOwn(255, 0, 255, Transparency.FOREGROUND);
    private final ColorOwn WHITE = new ColorOwn(255, 255, 255, Transparency.FOREGROUND);
    private final ColorOwn BLACK = new ColorOwn(0, 0, 0, Transparency.FOREGROUND);
    private final ColorOwn ORANGE = new ColorOwn(255, 165, 0, Transparency.FOREGROUND);
    private final ColorOwn CYAN = new ColorOwn(0, 255, 255, Transparency.FOREGROUND);
    private final ColorOwn DARK_GRAY = new ColorOwn(70, 70, 70, Transparency.FOREGROUND);
    private final ColorOwn GRAY = new ColorOwn(120, 120, 120, Transparency.FOREGROUND);
    private final ColorOwn LIGHT_GRAY = new ColorOwn(200, 200, 200, Transparency.FOREGROUND);
    private final ColorOwn PINK = new ColorOwn(255, 175, 175, Transparency.FOREGROUND);

    private final ColorOwn TRANSPARENT = WHITE.transparency(Transparency.FULL_TRANSPARENT); // color white is important because EPS export doesn't support transparency, therefore background will be white
    private final ColorOwn SELECTION_FG = BLUE;
    private final ColorOwn SELECTION_BG = new ColorOwn(0, 0, 255, Transparency.SELECTION_BACKGROUND);
    private final ColorOwn STICKING_POLYGON = new ColorOwn(100, 180, 255, Transparency.FOREGROUND);
    private final ColorOwn SYNTAX_HIGHLIGHTING = new ColorOwn(0, 100, 255, Transparency.FOREGROUND);
    private final ColorOwn DEFAULT_FOREGROUND = BLACK;
    private final ColorOwn DEFAULT_BACKGROUND = TRANSPARENT;
    private final ColorOwn DEFAULT_SPLITTER_COLOR = GRAY;

    public ColorOwnLight() {
        generateColorMaps();
    }

    private void generateColorMaps() {
        HashMap<PredefinedColors, ColorOwn> colorMap = new HashMap<PredefinedColors, ColorOwn>();
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

        HashMap<ColorStyle, ColorOwn> styleColorMap = new HashMap<ColorStyle, ColorOwn>();
        styleColorMap.put(ColorStyle.SELECTION_FG, SELECTION_FG);
        styleColorMap.put(ColorStyle.SELECTION_BG, SELECTION_BG);
        styleColorMap.put(ColorStyle.STICKING_POLYGON, STICKING_POLYGON);
        styleColorMap.put(ColorStyle.SYNTAX_HIGHLIGHTING, SYNTAX_HIGHLIGHTING);
        styleColorMap.put(ColorStyle.DEFAULT_FOREGROUND, DEFAULT_FOREGROUND);
        styleColorMap.put(ColorStyle.DEFAULT_BACKGROUND, DEFAULT_BACKGROUND);
        styleColorMap.put(ColorStyle.DEFAULT_SPLITTER_COLOR, DEFAULT_SPLITTER_COLOR);
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
