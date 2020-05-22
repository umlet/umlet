package com.baselet.element.old.custom;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.basics.Converter;
import com.baselet.control.constants.FacetConstants;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.Direction;
import com.baselet.control.enums.LineType;
import com.baselet.control.util.Utils;
import com.baselet.custom.CustomFunction;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.diagram.draw.helper.Theme;
import com.baselet.diagram.draw.helper.ThemeFactory;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public abstract class CustomElement extends OldGridElement {

	private static class Text {
		private final String text;
		private final int x, y;
		private final AlignHorizontal align;
		private Integer fixedSize;

		private Text(String text, int x, int y, AlignHorizontal align) {
			this.text = text;
			this.x = x;
			this.y = y;
			this.align = align;
		}

		private Text(String text, int x, int y, AlignHorizontal align, Integer fixedSize) {
			this.text = text;
			this.x = x;
			this.y = y;
			this.align = align;
			this.fixedSize = fixedSize; // some texts should not be zoomed
		}
	}

	protected float zoom;
	// We need a seperate program behaviour for 10% and 20% zoom. Otherwise manual resized entities would have the following bugs:
	// 10%: entity grow without end, 20%: relations don't stick on the right or bottom end of entity
	private boolean bugfix;

	protected Graphics2D g2;
	protected float temp;
	protected int width, height;
	protected Composite[] composites;
	private String code;

	private final Vector<StyleShape> shapes = new Vector<StyleShape>();
	private final Vector<Text> texts = new Vector<Text>();

	// The temp-variables are needed to store styles with setLineType etc. methods temporarily so that draw-Methods know the actual set style
	private LineType tmpLineType;
	private int tmpLineThickness;
	private Color tmpFgColor;
	private Color tmpBgColor;
	private float tmpAlpha;

	private boolean specialLine, specialFgColor, specialBgColor;
	private boolean wordWrap = false;
	private boolean allowResize = true;

	public abstract void paint();

	public final void setCode(String code) {
		this.code = code;
	}

	public final String getCode() {
		return code;
	}

	private void drawShapes() {

		g2.setColor(bgColor);
		g2.setComposite(composites[1]);

		for (StyleShape s : shapes) {
			specialBgColor = !s.getBgColor().equals(bgColor);
			if (specialBgColor) {
				g2.setColor(s.getBgColor());
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, s.getAlpha()));
			}
			g2.fill(s.getShape());
			if (specialBgColor) {
				g2.setColor(bgColor);
				g2.setComposite(composites[1]);
			}
		}

		g2.setComposite(composites[0]);
		g2.setColor(fgColor);

		Theme currentTheme = ThemeFactory.getCurrentTheme();

		for (StyleShape s : shapes) {
			specialLine = s.getLineType() != LineType.SOLID || s.getLineThickness() != FacetConstants.LINE_WIDTH_DEFAULT;
			specialFgColor = !s.getFgColor().equals(Converter.convert(currentTheme.getColor(Theme.ColorStyle.DEFAULT_FOREGROUND)));

			if (specialLine) {
				g2.setStroke(Utils.getStroke(s.getLineType(), s.getLineThickness()));
			}
			if (specialFgColor) {
				if (HandlerElementMap.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) {
					g2.setColor(Converter.convert(currentTheme.getColor(Theme.ColorStyle.SELECTION_FG)));
				}
				else {
					g2.setColor(s.getFgColor());
				}
			}
			g2.draw(s.getShape());
			if (specialLine) {
				g2.setStroke(Utils.getStroke(LineType.SOLID, (float) FacetConstants.LINE_WIDTH_DEFAULT));
			}
			if (specialFgColor) {
				g2.setColor(fgColor);
			}
		}

		for (Text t : texts) {
			boolean applyZoom = true;
			if (t.fixedSize != null) {
				HandlerElementMap.getHandlerForElement(this).getFontHandler().setFontSize((double) t.fixedSize);
				applyZoom = false;
			}
			HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, t.text, t.x, t.y, t.align, applyZoom);
			if (t.fixedSize != null) {
				HandlerElementMap.getHandlerForElement(this).getFontHandler().resetFontSize();
			}
		}

		texts.clear();
		shapes.clear();

	}

	@Override
	public final void paintEntity(Graphics g) {

		g2 = (Graphics2D) g;
		composites = colorize(g2);
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		g2.setColor(fgColor);

		zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();
		if (zoom < 0.25) {
			bugfix = true;
		}
		else {
			bugfix = false;
		}

		// width and height must be zoomed back to 100% before any custom code is applied
		temp = getRectangle().width;
		width = Math.round(temp / zoom); // use Math.round cause (int) would round down from 239.99998 to 240
		temp = getRectangle().height;
		height = Math.round(temp / zoom);

		// Set width and height on grid (used for manually resized custom elements mainly
		width = onGrid(width);
		height = onGrid(height);

		// secure this thread before executing the code!
		// String key = "R" + Math.random();
		// CustomElementSecurityManager.addThread(Thread.currentThread(), key);

		resetAll(); // Reset all tempstyle variables before painting
		paint(); // calls the paint method of the specific custom element
		// CustomElementSecurityManager.remThread(Thread.currentThread(), key);

		width = onGrid(width);
		height = onGrid(height);

		// After the custom code we zoom the width and height back
		width *= zoom;
		height *= zoom;

		drawShapes();

		changeSizeIfNoBugfix();
	}

	private void changeSizeIfNoBugfix() {
		// Resize elements if manual resize is not set
		// if (!this.allowResize || (this.autoResizeandManualResizeEnabled() && !this.isManualResized())) {
		// CHANGED: Resize every custom object by +1px to get consistent height and width
		if (!bugfix) {
			this.setSize(width + 1, height + 1);
			// }
		}
	}

	@Override
	public GridElement cloneFromMe() {
		CustomElement e = (CustomElement) super.cloneFromMe();
		e.code = code;
		return e;
	}

	@CustomFunction(param_defaults = "text,x,y")
	protected final int print(String text, int x, int inY) {
		int y = inY;
		List<String> list = wordWrap ? splitString(text, width, HandlerElementMap.getHandlerForElement(this)) : Arrays.asList(new String[] { text });
		for (String s : list) {
			texts.add(new Text(s, (int) (x * zoom), (int) (y * zoom), AlignHorizontal.LEFT));
			y += textHeight();
		}
		return y - inY;
	}

	@CustomFunction(param_defaults = "text,y")
	protected final int printLeft(String text, int inY) {
		int y = inY;
		List<String> list = wordWrap ? splitString(text, width, HandlerElementMap.getHandlerForElement(this)) : Arrays.asList(new String[] { text });
		for (String s : list) {
			texts.add(new Text(s, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(), (int) (y * zoom), AlignHorizontal.LEFT));
			y += textHeight();
		}
		return y - inY;
	}

	@CustomFunction(param_defaults = "text,y")
	protected final int printRight(String text, int inY) {
		int y = inY;
		List<String> list = wordWrap ? splitString(text, width, HandlerElementMap.getHandlerForElement(this)) : Arrays.asList(new String[] { text });
		for (String s : list) {
			texts.add(new Text(s, (int) (width * zoom - textWidth(s, true)), (int) (y * zoom), AlignHorizontal.LEFT));
			y += textHeight();
		}
		return y - inY;
	}

	@CustomFunction(param_defaults = "text,y")
	protected final int printCenter(String text, int inY) {
		int y = inY;
		List<String> list = wordWrap ? splitString(text, width, HandlerElementMap.getHandlerForElement(this)) : Arrays.asList(new String[] { text });
		for (String s : list) {
			texts.add(new Text(s, (int) ((onGrid(width) * zoom - textWidth(s, true)) / 2), (int) (y * zoom), AlignHorizontal.LEFT));
			y += textHeight();
		}
		return y - inY;
	}

	@CustomFunction(param_defaults = "text,x,y,fixedFontSize")
	protected final int printFixedSize(String text, int x, int inY, int fixedFontSize) {
		int y = inY;
		List<String> list = wordWrap ? splitString(text, width, HandlerElementMap.getHandlerForElement(this)) : Arrays.asList(new String[] { text });
		for (String s : list) {
			texts.add(new Text(s, x, y, AlignHorizontal.LEFT, fixedFontSize));
			y += textHeight();
		}
		return y - inY;
	}

	@CustomFunction(param_defaults = "value")
	protected final int onGrid(double value) {
		return onGrid(value, false);
	}

	@CustomFunction(param_defaults = "value, roundUp")
	protected final int onGrid(double value, boolean roundUp) {
		if (value % 10 != 0) {
			value -= value % 10;
			if (roundUp) {
				value += 10;
			}
		}
		// BUGFIX for 10% and 20% zoom: Otherwise a manual resized entity border wouldn't be visible because of the exclusion of line 146
		if (bugfix) {
			value--;
		}
		return (int) value;
	}

	@CustomFunction(param_defaults = "value1,value2")
	protected final int min(int value1, int value2) {
		return Math.min(value1, value2);
	}

	@CustomFunction(param_defaults = "value1,value2")
	protected final int max(int value1, int value2) {
		return Math.max(value1, value2);
	}

	@CustomFunction(param_defaults = "minWidth, minHeight, horizontalSpacing")
	protected final void setAutoresize(int minWidth, int minHeight, int horizontalSpacing) {
		if (!isManualResized()) {
			height = minHeight; // minimal height
			width = minWidth; // minimal width
			// calculates the width and height of the component
			for (String textline : Utils.decomposeStrings(getPanelAttributes())) {
				height = height + textHeight();
				width = Math.max(textWidth(textline, false) + 10 + horizontalSpacing, width);
			}
			if (height < minHeight) {
				height = minHeight;
			}
			if (width < minWidth) {
				width = minWidth;
			}
		}
	}

	@Override
	@CustomFunction(param_defaults = "")
	public final boolean isManualResized() {
		return super.isManualResized();
	}

	@CustomFunction(param_defaults = "wordWrap")
	public final void setWordWrap(boolean wordWrap) {
		this.wordWrap = wordWrap;
	}

	@CustomFunction(param_defaults = "")
	public final boolean isWordWrap() {
		return wordWrap;
	}

	@CustomFunction(param_defaults = "x, y, width, height, start, extent")
	protected final void drawArcOpen(float x, float y, float width, float height, float start, float extent) {
		shapes.add(new StyleShape(new Arc2D.Float(x * zoom, y * zoom, width * zoom, height * zoom, start, extent, Arc2D.OPEN), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x, y, width, height, start, extent")
	protected final void drawArcChord(float x, float y, float width, float height, float start, float extent) {
		shapes.add(new StyleShape(new Arc2D.Float(x * zoom, y * zoom, width * zoom, height * zoom, start, extent, Arc2D.CHORD), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x, y, width, height, start, extent")
	protected final void drawArcPie(float x, float y, float width, float height, float start, float extent) {
		shapes.add(new StyleShape(new Arc2D.Float(x * zoom, y * zoom, width * zoom, height * zoom, start, extent, Arc2D.PIE), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x, y, radius")
	protected final void drawCircle(int x, int y, int radius) {
		shapes.add(new StyleShape(new Ellipse2D.Float((int) ((x - radius) * zoom), (int) ((y - radius) * zoom), (int) (radius * 2 * zoom), (int) (radius * 2 * zoom)), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2")
	protected final void drawCurveCubic(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
		shapes.add(new StyleShape(new CubicCurve2D.Float(x1 * zoom, y1 * zoom, ctrlx1 * zoom, ctrly1 * zoom, ctrlx2 * zoom, ctrly2 * zoom, x2 * zoom, y2 * zoom), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x1, y1, ctrlx, ctrly, x2, y2")
	protected final void drawCurveQuad(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
		shapes.add(new StyleShape(new QuadCurve2D.Float(x1 * zoom, y1 * zoom, ctrlx * zoom, ctrly * zoom, x2 * zoom, y2 * zoom), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x, y, radiusX, radiusYs")
	protected final void drawEllipse(int x, int y, int radiusX, int radiusY) {
		shapes.add(new StyleShape(new Ellipse2D.Float((int) ((x - radiusX) * zoom), (int) ((y - radiusY) * zoom), (int) (radiusX * 2 * zoom), (int) (radiusY * 2 * zoom)), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x1, y1, x2, y2")
	protected final void drawLine(int x1, int y1, int x2, int y2) {
		shapes.add(new StyleShape(new Line2D.Float((int) (x1 * zoom), (int) (y1 * zoom), (int) (x2 * zoom), (int) (y2 * zoom)), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "y")
	protected final void drawLineHorizontal(int y) {
		shapes.add(new StyleShape(new Line2D.Float((int) (0 * zoom), (int) (y * zoom), HandlerElementMap.getHandlerForElement(this).realignToGrid(false, (int) (width * zoom), true), (int) (y * zoom)), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x")
	protected final void drawLineVertical(int x) {
		shapes.add(new StyleShape(new Line2D.Float((int) (x * zoom), (int) (0 * zoom), (int) (x * zoom), HandlerElementMap.getHandlerForElement(this).realignToGrid(false, (int) (height * zoom), true)), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "polygon")
	protected final void drawPolygon(Polygon polygon) {
		for (int i = 0; i < polygon.xpoints.length; i++) {
			polygon.xpoints[i] *= zoom;
		}
		for (int i = 0; i < polygon.ypoints.length; i++) {
			polygon.ypoints[i] *= zoom;
		}
		shapes.add(new StyleShape(polygon, tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x, y, width, height")
	protected final void drawRectangle(int x, int y, int width, int height) {
		shapes.add(new StyleShape(new Rectangle((int) (x * zoom), (int) (y * zoom), (int) (width * zoom), (int) (height * zoom)), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x, y, width, height, arcw, arch")
	protected final void drawRectangleRound(int x, int y, int width, int height, float arcw, float arch) {
		shapes.add(new StyleShape(new RoundRectangle2D.Float((int) (x * zoom), (int) (y * zoom), (int) (width * zoom), (int) (height * zoom), arcw * zoom, arch * zoom), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	// EXAMPLE: drawShape(new java.awt.geom.RoundRectangle2D.Float(10,10, 50, 40, 15,15));
	// WARNING: Shapes aren't zoomed automatically
	@CustomFunction(param_defaults = "shape")
	protected final void drawShape(Shape shape) {
		shapes.add(new StyleShape(shape, tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	/* STYLING METHODS */

	@CustomFunction(param_defaults = "lineType")
	protected final void setLineType(int lineType) {
		if (lineType == 0) {
			tmpLineType = LineType.SOLID;
		}
		else if (lineType == 1) {
			tmpLineType = LineType.DASHED;
		}
		else if (lineType == 2) {
			tmpLineType = LineType.DOTTED;
		}
		else if (lineType == 3) {
			tmpLineType = LineType.DOUBLE;
		}
		else if (lineType == 4) {
			tmpLineType = LineType.DOUBLE_DASHED;
		}
		else if (lineType == 5) {
			tmpLineType = LineType.DOUBLE_DOTTED;
		}
		else {
			tmpLineType = LineType.SOLID;
		}
	}

	@CustomFunction(param_defaults = "lineThickness")
	protected final void setLineThickness(int lineThickness) {
		tmpLineThickness = lineThickness;
	}

	@CustomFunction(param_defaults = "foregroundColor")
	protected final void setForegroundColor(String fgColorString) {
		tmpFgColor = Converter.convert(ThemeFactory.getCurrentTheme().forStringOrNull(fgColorString, Transparency.FOREGROUND));
		if (tmpFgColor == null) {
			tmpFgColor = fgColor; // unknown colors resolve to default color
		}
	}

	@CustomFunction(param_defaults = "backgroundColor")
	protected final void setBackgroundColor(String bgColorString) {
		// OldGridElements apply transparency for background explicitly, therefore don't apply it here
		tmpBgColor = Converter.convert(ThemeFactory.getCurrentTheme().forStringOrNull(bgColorString, Transparency.FOREGROUND));
		if (tmpBgColor == null) {
			tmpBgColor = bgColor; // unknown colors resolve to default color
		}
		// Transparency is 0% if none or 50% if anything else
		if (bgColorString.equals("none")) {
			tmpAlpha = OldGridElement.ALPHA_FULL_TRANSPARENCY;
		}
		else {
			tmpAlpha = OldGridElement.ALPHA_MIDDLE_TRANSPARENCY;
		}
	}

	@CustomFunction(param_defaults = "")
	protected final void resetAll() {
		tmpLineThickness = (int) FacetConstants.LINE_WIDTH_DEFAULT;
		tmpLineType = LineType.SOLID;
		tmpFgColor = fgColor;
		tmpBgColor = bgColor;
		tmpAlpha = alphaFactor;
	}

	protected final int textHeight() {
		return (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize(false) + HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(false));
	}

	protected final int textWidth(String text, boolean applyZoom) {
		return (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextSize(text, applyZoom).getWidth() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(applyZoom));
	}

	protected final int textWidth(String text) {
		return textWidth(text, false);
	}

	protected final void allowResize(boolean allow) {
		allowResize = allow;
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		if (allowResize) {
			return super.getResizeArea(x, y);
		}
		else {
			return new HashSet<Direction>();
		}
	}

	private static List<String> splitString(String text, float width, DiagramHandler handler) {
		StringBuilder stringBuilder = new StringBuilder(text);
		int lastEmptyChar = -1; // is -1 if there was no ' ' in this line
		int firstCharInLine = 0;

		for (int i = 0; i < text.length(); i++) {
			if (stringBuilder.charAt(i) == ' ') {
				lastEmptyChar = i;
			}
			else if (stringBuilder.charAt(i) == '\n') {
				lastEmptyChar = -1;
				firstCharInLine = i + 1;
			}
			if (handler.getFontHandler().getTextWidth(text.substring(firstCharInLine, i), false) + 15 > width) {
				if (lastEmptyChar != -1) {
					stringBuilder.setCharAt(lastEmptyChar, '\n');
					firstCharInLine = lastEmptyChar + 1;
					lastEmptyChar = -1;
				}
				else {
					stringBuilder.insert(i, '\n');
					firstCharInLine = i + 1;
				}
			}
		}
		return Arrays.asList(stringBuilder.toString().split("\\n"));
	}

	@Override
	public boolean isDeprecated() {
		return false;
	}

}
