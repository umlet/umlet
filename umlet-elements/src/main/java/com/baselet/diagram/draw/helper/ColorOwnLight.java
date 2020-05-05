package com.baselet.diagram.draw.helper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ColorOwnLight extends ColorOwnBase {

    public static final String EXAMPLE_TEXT = "color string (green,...) or code (#3c7a00,...)";

    private final ColorOwnLight RED = new ColorOwnLight(255, 0, 0, Transparency.FOREGROUND);
    private final ColorOwnLight GREEN = new ColorOwnLight(0, 255, 0, Transparency.FOREGROUND);
    private final ColorOwnLight BLUE = new ColorOwnLight(0, 0, 255, Transparency.FOREGROUND);
    private final ColorOwnLight YELLOW = new ColorOwnLight(255, 255, 0, Transparency.FOREGROUND);
    private final ColorOwnLight MAGENTA = new ColorOwnLight(255, 0, 255, Transparency.FOREGROUND);
    private final ColorOwnLight WHITE = new ColorOwnLight(255, 255, 255, Transparency.FOREGROUND);
    private final ColorOwnLight BLACK = new ColorOwnLight(0, 0, 0, Transparency.FOREGROUND);
    private final ColorOwnLight ORANGE = new ColorOwnLight(255, 165, 0, Transparency.FOREGROUND);
    private final ColorOwnLight CYAN = new ColorOwnLight(0, 255, 255, Transparency.FOREGROUND);
    private final ColorOwnLight DARK_GRAY = new ColorOwnLight(70, 70, 70, Transparency.FOREGROUND);
    private final ColorOwnLight GRAY = new ColorOwnLight(120, 120, 120, Transparency.FOREGROUND);
    private final ColorOwnLight LIGHT_GRAY = new ColorOwnLight(200, 200, 200, Transparency.FOREGROUND);
    private final ColorOwnLight PINK = new ColorOwnLight(255, 175, 175, Transparency.FOREGROUND);

    private final ColorOwnLight TRANSPARENT = WHITE.transparency(Transparency.FULL_TRANSPARENT); // color white is important because EPS export doesn't support transparency, therefore background will be white
    private final ColorOwnLight SELECTION_FG = BLUE;
    private final ColorOwnLight SELECTION_BG = new ColorOwnLight(0, 0, 255, Transparency.SELECTION_BACKGROUND);
    private final ColorOwnLight STICKING_POLYGON = new ColorOwnLight(100, 180, 255, Transparency.FOREGROUND);
    private final ColorOwnLight SYNTAX_HIGHLIGHTING = new ColorOwnLight(0, 100, 255, Transparency.FOREGROUND);
    private final ColorOwnLight DEFAULT_FOREGROUND = BLACK;
    private final ColorOwnLight DEFAULT_BACKGROUND = TRANSPARENT;

    private Map<PredefinedColors, ColorOwnLight> colorMap;
    private Map<ColorStyle, ColorOwnLight> styleColorMap;

    @Override
    public Map<PredefinedColors, ColorOwnLight> getColorMap() {
        return colorMap;
    }

    @Override
    public Map<ColorStyle, ColorOwnLight> getStyleColorMap() {
        return styleColorMap;
    }

    public ColorOwnLight(int red, int green, int blue, Transparency transparency) {
        this(red, green, blue, transparency.getAlpha());
    }

    public ColorOwnLight(int red, int green, int blue, int alpha) {
        super(red, green, blue, alpha);
        generateColorMaps();
    }

    public ColorOwnLight(String hex) {
        super(hex);
        generateColorMaps();
    }

    public ColorOwnLight() {
        super();
    }

    private void generateColorMaps() {
        HashMap<PredefinedColors, ColorOwnLight> colorMap = new HashMap<PredefinedColors, ColorOwnLight>();
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
        this.colorMap = Collections.unmodifiableMap(colorMap);

        HashMap<ColorStyle, ColorOwnLight> styleColorMap = new HashMap<ColorStyle, ColorOwnLight>();
        styleColorMap.put(ColorStyle.SELECTION_FG, SELECTION_FG);
        styleColorMap.put(ColorStyle.SELECTION_BG, SELECTION_BG);
        styleColorMap.put(ColorStyle.STICKING_POLYGON, STICKING_POLYGON);
        styleColorMap.put(ColorStyle.SYNTAX_HIGHLIGHTING, SYNTAX_HIGHLIGHTING);
        styleColorMap.put(ColorStyle.DEFAULT_FOREGROUND, DEFAULT_FOREGROUND);
        styleColorMap.put(ColorStyle.DEFAULT_BACKGROUND, DEFAULT_BACKGROUND);
        this.styleColorMap = Collections.unmodifiableMap(styleColorMap);
    }

    public ColorOwnLight transparency(Transparency transparency) {
        return transparency(transparency.getAlpha());
    }

    public ColorOwnLight transparency(int alpha) {
        return new ColorOwnLight(getRed(), getGreen(), getBlue(), alpha);
    }

    public ColorOwnLight darken(int factor) {
        return new ColorOwnLight(Math.max(0, getRed() - factor), Math.max(0, getGreen() - factor), Math.max(0, getBlue() - factor), getAlpha());
    }

    /**
     * Converts colorString into a Color which is available in the colorMap or if not tries to decode the colorString
     *
     * @param colorString String which describes the color
     * @return Color which is related to the String or null if it is no valid colorString
     */
    @Override
    public ColorOwnLight forStringOrNull(String colorString, Transparency transparency) {
        try {
            return forString(colorString, transparency);
        } catch (StyleException e) {
            return null;
        }
    }

    @Override
    public ColorOwnLight forString(String colorString, Transparency transparency) {
        return forString(colorString, transparency.getAlpha());
    }

    /**
     * Converts colorString into a Color which is available in the colorMap or if not tries to decode the colorString
     *
     * @param colorString String which describes the color
     * @return Color which is related to the String or null if it is no valid colorString
     */
    @Override
    public ColorOwnLight forString(String colorString, int transparency) {
        boolean error = false;
        ColorOwnLight returnColor = null;
        if (colorString == null) {
            error = true;
        } else {
            for (Entry<PredefinedColors, ColorOwnLight> c : colorMap.entrySet()) {
                if (colorString.equalsIgnoreCase(c.getKey().toString())) {
                    returnColor = c.getValue();
                    break;
                }
            }
            if (returnColor == null) {
                try {
                    returnColor = new ColorOwnLight(colorString);
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
