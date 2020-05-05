package com.baselet.element.facet.customdrawings;

import com.baselet.control.StringStyle;
import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.constants.FacetConstants;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.LineType;
import com.baselet.diagram.draw.helper.ColorOwnBase;
import com.baselet.diagram.draw.helper.ColorOwnLight;
import com.baselet.diagram.draw.helper.Theme;

public class DummyDrawHandler extends com.baselet.diagram.draw.DrawHandler {

	public static final ColorOwnLight defaultBg = Theme.getCurrentThemeColor().getStyleColorMap().get(ColorOwnBase.ColorStyle.DEFAULT_BACKGROUND);
	public static final ColorOwnLight defaultFg = Theme.getCurrentThemeColor().getStyleColorMap().get(ColorOwnBase.ColorStyle.DEFAULT_FOREGROUND);
	public static final LineType defaultLineType = LineType.SOLID;
	public static final double defaultLineWidth = FacetConstants.LINE_WIDTH_DEFAULT;

	private String lastDrawCall = null;

	public DummyDrawHandler() {
		setBackgroundColor(defaultBg);
		setForegroundColor(defaultFg);
		setLineType(defaultLineType);
		setLineWidth(defaultLineWidth);
	}

	@Override
	protected DimensionDouble textDimensionHelper(StringStyle sinlgeLine) {
		return null;
	}

	@Override
	protected DimensionDouble textDimension(StringStyle sinlgeLine) {
		return super.textDimension(sinlgeLine);
	}

	@Override
	protected double getDefaultFontSize() {
		return 0;
	}

	@Override
	public void drawArc(double x, double y, double width, double height, double start, double extent, boolean open) {
		lastDrawCall = drawArcToString(x, y, width, height, start, extent, open, getForegroundColor(), getBackgroundColor(), getLineType(), getLineWidth());
	}

	@Override
	public void drawCircle(double x, double y, double radius) {
		lastDrawCall = drawCircleToString(x, y, radius, getForegroundColor(), getBackgroundColor(), getLineType(), getLineWidth());

	}

	@Override
	public void drawEllipse(double x, double y, double width, double height) {
		lastDrawCall = drawEllipseToString(x, y, width, height, getForegroundColor(), getBackgroundColor(), getLineType(), getLineWidth());
	}

	@Override
	public void drawLines(PointDouble... points) {

	}

	@Override
	public void drawRectangle(double x, double y, double width, double height) {
		lastDrawCall = drawRectangleToString(x, y, width, height, getForegroundColor(), getBackgroundColor(), getLineType(), getLineWidth());
	}

	@Override
	public void drawRectangleRound(double x, double y, double width, double height, double radius) {
		lastDrawCall = drawRectangleRoundToString(x, y, width, height, radius, getForegroundColor(), getBackgroundColor(), getLineType(), getLineWidth());
	}

	@Override
	public void printHelper(StringStyle[] lines, PointDouble point, AlignHorizontal align) {}

	@Override
	public void print(String text, double x, double y, AlignHorizontal align) {
		lastDrawCall = drawTextToString(text, x, y, align, getForegroundColor());
	}

	@Override
	public void drawLine(double x1, double y1, double x2, double y2) {
		lastDrawCall = drawLineToString(x1, y1, x2, y2, getForegroundColor(), getLineType(), getLineWidth());
	}

	public String getLastDrawCall() {
		return lastDrawCall;
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param start
	 * @param extent
	 * @param open
	 * @param fg if null, default value is taken
	 * @param bg if null, default value is taken
	 * @param lt if null, default value is taken
	 * @param lw if null, default value is taken
	 * @return the string representation of this DrawHandler call.
	 */
	public static String drawArcToString(double x, double y, double width, double height, double start, double extent, boolean open, ColorOwnBase fg, ColorOwnBase bg, LineType lt, Double lw) {
		if (fg == null) {
			fg = defaultFg;
		}
		if (bg == null) {
			bg = defaultBg;
		}
		if (lt == null) {
			lt = defaultLineType;
		}
		if (lw == null) {
			lw = defaultLineWidth;
		}
		return String.format("drawArc(%.3f, %.3f, %.3f, %.3f, %.3f, %.3f, %b)", x, y, width, height, start, extent, open) +
				String.format(" fg='%s' bg='%s' lt=%s lw=%s ", fg, bg, lt, lw);
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param radius
	 * @param fg if null, default value is taken
	 * @param bg if null, default value is taken
	 * @param lt if null, default value is taken
	 * @param lw if null, default value is taken
	 * @return the string representation of this DrawHandler call.
	 */
	public static String drawCircleToString(double x, double y, double radius, ColorOwnBase fg, ColorOwnBase bg, LineType lt, Double lw) {
		if (fg == null) {
			fg = defaultFg;
		}
		if (bg == null) {
			bg = defaultBg;
		}
		if (lt == null) {
			lt = defaultLineType;
		}
		if (lw == null) {
			lw = defaultLineWidth;
		}
		return String.format("drawCircle(%.3f, %.3f, %.3f)", x, y, radius) +
				String.format(" fg='%s' bg='%s' lt=%s lw=%s ", fg, bg, lt, lw);
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param fg if null, default value is taken
	 * @param bg if null, default value is taken
	 * @param lt if null, default value is taken
	 * @param lw if null, default value is taken
	 * @return the string representation of this DrawHandler call.
	 */
	public static String drawEllipseToString(double x, double y, double width, double height, ColorOwnBase fg, ColorOwnBase bg, LineType lt, Double lw) {
		if (fg == null) {
			fg = defaultFg;
		}
		if (bg == null) {
			bg = defaultBg;
		}
		if (lt == null) {
			lt = defaultLineType;
		}
		if (lw == null) {
			lw = defaultLineWidth;
		}
		return String.format("drawEllipse(%.3f, %.3f, %.3f, %.3f)", x, y, width, height) +
				String.format(" fg='%s' bg='%s' lt=%s lw=%s ", fg, bg, lt, lw);
	}

	/**
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param fg if null, default value is taken
	 * @param lt if null, default value is taken
	 * @param lw if null, default value is taken
	 * @return the string representation of this DrawHandler call.
	 */
	public static String drawLineToString(double x1, double y1, double x2, double y2, ColorOwnBase fg, LineType lt, Double lw) {
		if (fg == null) {
			fg = defaultFg;
		}
		if (lt == null) {
			lt = defaultLineType;
		}
		if (lw == null) {
			lw = defaultLineWidth;
		}
		return String.format("drawLine(%.3f, %.3f, %.3f, %.3f)", x1, y1, x2, y2) +
				String.format(" fg='%s' lt=%s lw=%s ", fg, lt, lw);
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param fg if null, default value is taken
	 * @param bg if null, default value is taken
	 * @param lt if null, default value is taken
	 * @param lw if null, default value is taken
	 * @return the string representation of this DrawHandler call.
	 */
	public static String drawRectangleToString(double x, double y, double width, double height, ColorOwnBase fg, ColorOwnBase bg, LineType lt, Double lw) {
		if (fg == null) {
			fg = defaultFg;
		}
		if (bg == null) {
			bg = defaultBg;
		}
		if (lt == null) {
			lt = defaultLineType;
		}
		if (lw == null) {
			lw = defaultLineWidth;
		}
		return String.format("drawRectangle(%.3f, %.3f, %.3f, %.3f)", x, y, width, height) +
				String.format(" fg='%s' bg='%s' lt=%s lw=%s ", fg, bg, lt, lw);
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param radius
	 * @param fg if null, default value is taken
	 * @param bg if null, default value is taken
	 * @param lt if null, default value is taken
	 * @param lw if null, default value is taken
	 * @return the string representation of this DrawHandler call.
	 */
	public static String drawRectangleRoundToString(double x, double y, double width, double height, double radius, ColorOwnBase fg, ColorOwnBase bg, LineType lt, Double lw) {
		if (fg == null) {
			fg = defaultFg;
		}
		if (bg == null) {
			bg = defaultBg;
		}
		if (lt == null) {
			lt = defaultLineType;
		}
		if (lw == null) {
			lw = defaultLineWidth;
		}
		return String.format("drawRectangleRound(%.3f, %.3f, %.3f, %.3f, %.3f)", x, y, width, height, radius) +
				String.format(" fg='%s' bg='%s' lt=%s lw=%s ", fg, bg, lt, lw);
	}

	/**
	 * @param text
	 * @param x
	 * @param y
	 * @param align
	 * @param fg if null, default value is taken
	 * @return the string representation of this DrawHandler call.
	 */
	public static String drawTextToString(String text, double x, double y, AlignHorizontal align, ColorOwnBase fg) {
		if (fg == null) {
			fg = defaultFg;
		}
		return String.format("drawText('%s', %.3f, %.3f, %s)", text, x, y, align) +
				String.format(" fg='%s' ", fg);
	}
}
