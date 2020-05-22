package com.baselet.diagram.draw.helper.theme;

import com.baselet.diagram.draw.helper.ColorOwn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;

import static com.baselet.diagram.draw.helper.ColorOwn.Transparency;

public class ThemeDark extends Theme {
    private static final Logger log = LoggerFactory.getLogger(ThemeDark.class);

    private final ColorOwn RED = new ColorOwn(220, 0, 0, Transparency.FOREGROUND);
    private final ColorOwn GREEN = new ColorOwn(0, 220, 0, Transparency.FOREGROUND);
    private final ColorOwn BLUE = new ColorOwn(0, 0, 220, Transparency.FOREGROUND);
    private final ColorOwn YELLOW = new ColorOwn(100, 100, 0, Transparency.FOREGROUND);
    private final ColorOwn MAGENTA = new ColorOwn(100, 0, 100, ColorOwn.Transparency.FOREGROUND);
    private final ColorOwn WHITE = new ColorOwn(255, 255, 255, ColorOwn.Transparency.FOREGROUND);
    private final ColorOwn BLACK = new ColorOwn(40, 40, 40, Transparency.FOREGROUND);
    private final ColorOwn ORANGE = new ColorOwn(175, 117, 0, Transparency.FOREGROUND);
    private final ColorOwn CYAN = new ColorOwn(0, 100, 100, Transparency.FOREGROUND);
    private final ColorOwn DARK_GRAY = new ColorOwn(70, 70, 70, Transparency.FOREGROUND);
    private final ColorOwn GRAY = new ColorOwn(120, 120, 120, Transparency.FOREGROUND);
    private final ColorOwn LIGHT_GRAY = new ColorOwn(200, 200, 200, Transparency.FOREGROUND);
    private final ColorOwn PINK = new ColorOwn(205, 120, 120, Transparency.FOREGROUND);

    private final ColorOwn TRANSPARENT = BLACK.transparency(Transparency.FULL_TRANSPARENT); // color white is important because EPS export doesn't support transparency, therefore background will be white
    private final ColorOwn SELECTION_FG = new ColorOwn(150, 150, 255, Transparency.FOREGROUND);
    private final ColorOwn SELECTION_BG = new ColorOwn(0, 0, 255, Transparency.SELECTION_BACKGROUND);
    private final ColorOwn STICKING_POLYGON = new ColorOwn(100, 180, 255, Transparency.FOREGROUND);
    private final ColorOwn SYNTAX_HIGHLIGHTING = new ColorOwn(0, 100, 255, Transparency.FOREGROUND);
    private final ColorOwn DEFAULT_FOREGROUND = WHITE;
    private final ColorOwn DEFAULT_BACKGROUND = BLACK;
    private final ColorOwn DEFAULT_SPLITTER_COLOR = GRAY;

    public ThemeDark() {
        generateColorMaps();
    }

    private void generateColorMaps() {
        HashMap<Theme.PredefinedColors, ColorOwn> colorMap = new HashMap<PredefinedColors, ColorOwn>();
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
}
