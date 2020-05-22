package com.baselet.diagram.draw.helper.theme;

import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.StyleException;

import java.util.Map;

public abstract class Theme {
    public static final String EXAMPLE_TEXT = "color string (green,...) or code (#3c7a00,...)";

    public enum PredefinedColors {
        RED, GREEN, BLUE, YELLOW, MAGENTA, WHITE, BLACK, ORANGE, CYAN, DARK_GRAY, GRAY, LIGHT_GRAY, PINK, TRANSPARENT, NONE
    }

    public enum ColorStyle {
        SELECTION_FG, SELECTION_BG, STICKING_POLYGON, SYNTAX_HIGHLIGHTING, DEFAULT_FOREGROUND, DEFAULT_BACKGROUND, DEFAULT_SPLITTER_COLOR
    }

    protected Map<PredefinedColors, ColorOwn> colorMap;
    protected Map<ColorStyle, ColorOwn> styleColorMap;

    /**
     * Converts colorString into a Color which is available in the colorMap or if not tries to decode the colorString
     *
     * @param colorString String which describes the color
     * @return Color which is related to the String or null if it is no valid colorString
     */
    public ColorOwn forStringOrNull(String colorString, ColorOwn.Transparency transparency) {
        try {
            return forString(colorString, transparency);
        } catch (StyleException e) {
            return null;
        }
    }

    public ColorOwn forString(String colorString, ColorOwn.Transparency transparency) {
        return forString(colorString, transparency.getAlpha());
    }

    /**
     * Converts colorString into a Color which is available in the colorMap or if not tries to decode the colorString
     *
     * @param colorString String which describes the color
     * @return Color which is related to the String or null if it is no valid colorString
     */
    public ColorOwn forString(String colorString, int transparency) {
        boolean error = false;
        ColorOwn returnColor = null;
        if (colorString == null) {
            error = true;
        } else {
            for (Map.Entry<PredefinedColors, ColorOwn> c : colorMap.entrySet()) {
                if (colorString.equalsIgnoreCase(c.getKey().toString())) {
                    returnColor = c.getValue();
                    break;
                }
            }
            if (returnColor == null) {
                try {
                    returnColor = new ColorOwn(colorString);
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

    public ColorOwn getColor(PredefinedColors color) {
        return colorMap.get(color);
    }

    public ColorOwn getColor(ColorStyle colorStyle) {
        return styleColorMap.get(colorStyle);
    }

    public Map<PredefinedColors, ColorOwn> getColorMap() {
        return colorMap;
    }
}
