package com.baselet.gwt.client.view;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.constants.SharedConstants;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.diagram.draw.helper.Theme;
import com.baselet.diagram.draw.helper.ThemeFactory;
import com.baselet.element.GridElementUtils;
import com.baselet.element.interfaces.Diagram;
import com.baselet.gwt.client.base.Converter;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;

public class CanvasUtils {

    private static final int EXPORT_BORDER = 10;

    public static String createPngCanvasDataUrl(Diagram diagram) {
        ThemeFactory.changeTheme(ThemeFactory.THEMES.LIGHT);
        DrawCanvas pngCanvas = new DrawCanvas();
        // Calculate and set canvas width
        Rectangle geRect = GridElementUtils.getGridElementsRectangle(diagram.getGridElements());
        geRect.addBorder(EXPORT_BORDER);
        pngCanvas.clearAndSetSize(geRect.getWidth(), geRect.getHeight());
        // Fill Canvas white
        pngCanvas.getContext2d().setFillStyle(Converter.convert(ThemeFactory.getCurrentTheme().getColorMap().get(Theme.PredefinedColors.WHITE)));
        pngCanvas.getContext2d().fillRect(0, 0, pngCanvas.getWidth(), pngCanvas.getHeight());
        // Draw Elements on Canvas and translate their position
        pngCanvas.getContext2d().translate(-geRect.getX(), -geRect.getY());
        pngCanvas.draw(false, diagram.getGridElementsByLayerLowestToHighest(), new SelectorNew(diagram)); // use a new selector which has nothing selected
        ThemeFactory.changeTheme(ThemeFactory.THEMES.DARK);
        return pngCanvas.toDataUrl("image/png");
    }

    private static Canvas gridCanvas;

    public static void drawGridOn(Context2d context2d) {
        if (gridCanvas == null) {
            gridCanvas = Canvas.createIfSupported();
            gridCanvas.setCoordinateSpaceWidth(3000);
            gridCanvas.setCoordinateSpaceHeight(2000);
            int width = gridCanvas.getCoordinateSpaceWidth();
            int height = gridCanvas.getCoordinateSpaceHeight();
            Context2d backgroundContext = gridCanvas.getContext2d();
            backgroundContext.setStrokeStyle(Converter.convert(ThemeFactory.getCurrentTheme().getColorMap().get(Theme.PredefinedColors.BLACK).transparency(Transparency.SELECTION_BACKGROUND)));
            for (int i = 0; i < width; i += SharedConstants.DEFAULT_GRID_SIZE) {
                drawLine(backgroundContext, i, 0, i, height);
            }
            for (int i = 0; i < height; i += SharedConstants.DEFAULT_GRID_SIZE) {
                drawLine(backgroundContext, 0, i, width, i);
            }
        }
        context2d.drawImage(gridCanvas.getCanvasElement(), 0, 0);
    }

    private static void drawLine(Context2d context, int x, int y, int x2, int y2) {
        context.beginPath();
        context.moveTo(x + 0.5, y + 0.5); // +0.5 because a line of thickness 1.0 spans 50% left and 50% right (therefore it would not be on the 1 pixel - see https://developer.mozilla.org/en-US/docs/HTML/Canvas/Tutorial/Applying_styles_and_colors)
        context.lineTo(x2 + 0.5, y2 + 0.5);
        context.stroke();
    }
}
