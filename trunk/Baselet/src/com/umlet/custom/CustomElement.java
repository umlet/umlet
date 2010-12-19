package com.umlet.custom;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
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
import java.util.Vector;

import com.baselet.control.Constants;
import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.AlignVertical;
import com.baselet.control.Constants.LineType;
import com.baselet.control.Utils;
import com.baselet.element.GridElement;


@SuppressWarnings("serial")
public abstract class CustomElement extends GridElement {

	private class Text {
		private String text;
		private int x, y;
		private AlignHorizontal align;
		private AlignVertical valign;
		private Integer fixedSize;

		private Text(String text, int x, int y, AlignHorizontal align, AlignVertical valign) {
			this.text = text;
			this.x = x;
			this.y = y;
			this.align = align;
			this.valign = valign;
		}

		private Text(String text, int x, int y, AlignHorizontal align, AlignVertical valign, Integer fixedSize) {
			this.text = text;
			this.x = x;
			this.y = y;
			this.align = align;
			this.valign = valign;
			this.fixedSize = fixedSize; // some texts should not be zoomed
		}
	}

	protected float zoom;
	// We need a seperate main behaviour for 10% and 20% zoom. Otherwise manual resized entities would have the following bugs:
	// 10%: entity grow without end, 20%: relations don't stick on the right or bottom end of entity
	private boolean bugfix;

	protected Graphics2D g2;
	protected float temp;
	protected int width, height;
	protected Composite[] composites;
	private String code;
	private boolean allowResize;

	private Vector<StyleShape> shapes;
	private Vector<Text> texts;

	// The temp-variables are needed to store styles with setLineType etc. methods temporarily so that draw-Methods know the actual set style
	private LineType tmpLineType;
	private int tmpLineThickness;
	private Color tmpFgColor;
	private Color tmpBgColor;
	private float tmpAlpha;

	boolean specialLine, specialFgColor, specialBgColor;
	int oldFontSize;

	public CustomElement() {

		this.allowResize = true;
		this.shapes = new Vector<StyleShape>();
		this.texts = new Vector<Text>();
//		 this.is_centered = false;
	}

	public abstract void paint();

	public final void setCode(String code) {
		this.code = code;
	}

	public final String getCode() {
		return this.code;
	}

	private void drawShapes() {

		g2.setColor(bgColor);
		g2.setComposite(this.composites[1]);

		for (StyleShape s : this.shapes) {
			specialBgColor = ((s.getBgColor() != bgColor) || (s.getAlpha() != Constants.ALPHA_NO_TRANSPARENCY));
			if (specialBgColor) {
				g2.setColor(s.getBgColor());
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, s.getAlpha()));
			}
			this.g2.fill(s.getShape());
			if (specialBgColor) {
				g2.setColor(bgColor);
				g2.setComposite(this.composites[1]);
			}
		}

		g2.setComposite(this.composites[0]);
		g2.setColor(fgColor);

		for (StyleShape s : this.shapes) {
			specialLine = ((s.getLineType() != LineType.SOLID) || (s.getLineThickness() != Constants.DEFAULT_LINE_THICKNESS));
			specialFgColor = (s.getFgColor() != Constants.DEFAULT_FOREGROUND_COLOR);

			if (specialLine) g2.setStroke(Utils.getStroke(s.getLineType(), s.getLineThickness()));
			if (specialFgColor) g2.setColor(s.getFgColor());
			this.g2.draw(s.getShape());
			if (specialLine) g2.setStroke(Utils.getStroke(LineType.SOLID, Constants.DEFAULT_LINE_THICKNESS));
			if (specialFgColor) g2.setColor(fgColor);
		}

		for (Text t : this.texts) {
			if (t.fixedSize != null) {
				oldFontSize = (int) getHandler().getFontHandler().getFontSize();
				getHandler().getFontHandler().setFontSize(t.fixedSize);
				g2.setFont(this.getHandler().getFontHandler().getFont(false));
				t.y += textHeight();
			}
			this.getHandler().getFontHandler().writeText(this.g2, t.text, t.x, t.y, t.align, t.valign);
			if (t.fixedSize != null) {
				getHandler().getFontHandler().setFontSize(oldFontSize);
				g2.setFont(this.getHandler().getFontHandler().getFont());
			}
		}

		this.texts.clear();
		this.shapes.clear();

	}

	@Override
	public final void paintEntity(Graphics g) {

		zoom = getHandler().getZoomFactor();
		if (zoom < 0.25) bugfix = true;
		else bugfix = false;

		this.g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		g2.setColor(fgColor);
		composites = this.colorize(g2);

		// width and height must be zoomed back to 100% before any custom code is applied
		temp = this.getWidth();
		width = Math.round(temp / zoom); // use Math.round cause (int) would round down from 239.99998 to 240
		temp = this.getHeight();
		height = Math.round(temp / zoom);
		// Set width and height on grid (used for manually resized custom elements mainly
		width = onGrid(width);
		height = onGrid(height);
		resetAll(); // Reset all tempstyle variables before painting
		paint(); // calls the paint method of the specific custom element
		width = onGrid(width);
		height = onGrid(height);

		// After the custom code we zoom the width and height back
		width *= zoom;
		height *= zoom;

		this.drawShapes();

		// Resize elements if manual resize is not set
		// if (!this.allowResize || (this.autoResizeandManualResizeEnabled() && !this.isManualResized())) {
		// CHANGED: Resize every custom object by +1px to get consistent height and width
		if (!bugfix) this.setSize(this.width + 1, this.height + 1);
		// }
	}

	@Override
	public final GridElement CloneFromMe() {
		CustomElement e = (CustomElement) super.CloneFromMe();
		e.code = this.code;
		return e;
	}

	@Override
	public final int getPossibleResizeDirections() {
		if (this.allowResize) return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
		return 0;
	}

	private final Dimension zoomedTextsize(String text) {
		Dimension d = this.getHandler().getFontHandler().getTextSize(text);
		d.height = (int) (this.getHandler().getFontHandler().getDistanceBetweenTexts() + this.getHandler().getFontHandler().getFontSize());
		d.width += this.getHandler().getFontHandler().getDistanceBetweenTexts();
		return d;
	}

	@CustomFunction(param_defaults = "text,x,y")
	protected final void print(String text, int x, int y) {
		this.texts.add(new Text(text, (int) (x * zoom), (int) (y * zoom), AlignHorizontal.LEFT, AlignVertical.BOTTOM));
	}

	@CustomFunction(param_defaults = "text,y")
	protected final void printLeft(String text, int y) {
		this.texts.add(new Text(text, (int) this.getHandler().getFontHandler().getDistanceBetweenTexts(), (int) (y * zoom), AlignHorizontal.LEFT, AlignVertical.BOTTOM));
	}

	@CustomFunction(param_defaults = "text,y")
	protected final void printRight(String text, int y) {
		this.texts.add(new Text(text, (int) (width * zoom) - this.zoomedTextsize(text).width, (int) (y * zoom), AlignHorizontal.LEFT, AlignVertical.BOTTOM));
	}

	@CustomFunction(param_defaults = "text,y")
	protected final void printCenter(String text, int y) {
		this.texts.add(new Text(text, (onGrid((int) (width * zoom)) - this.zoomedTextsize(text).width) / 2, (int) (y * zoom), AlignHorizontal.LEFT, AlignVertical.BOTTOM));
	}

	@CustomFunction(param_defaults = "text,x,y,fixedFontSize")
	protected final void printFixedSize(String text, int x, int y, int fixedFontSize) {
		this.texts.add(new Text(text, (int) (x * zoom), (int) (y * zoom), AlignHorizontal.LEFT, AlignVertical.BOTTOM, fixedFontSize));
	}

//	@CustomFunction(param_defaults = "")
//	protected final int textHeight() {
//		return (int) ((int) this.getHandler().getFontHandler().getFontSize(false) + this.getHandler().getFontHandler().getDistanceBetweenTexts(false));
//	}
//
//	@CustomFunction(param_defaults = "text")
//	protected final int textWidth(String text) {
//		return getHandler().getFontHandler().getTextWidth(text, false) + (int) getHandler().getFontHandler().getDistanceBetweenTexts(false);
//	}

	@CustomFunction(param_defaults = "value")
	protected final int onGrid(int value) {
		return onGrid(value, false);
	}

	@CustomFunction(param_defaults = "value, roundUp")
	protected final int onGrid(int value, boolean roundUp) {
		if (value % 10 != 0) {
			value -= (value % 10);
			if (roundUp) value += 10;
		}
		// BUGFIX for 10% and 20% zoom: Otherwise a manual resized entity border wouldn't be visible because of the exclusion of line 146
		if (bugfix) value--;
		return value;
	}

//	@CustomFunction(param_defaults = "allow")
//	protected final void allowResize(boolean allow) {
//		this.allowResize = allow;
//	}

	@CustomFunction(param_defaults = "minWidth, minHeight, horizontalSpacing")
	protected final void setAutoresize(int minWidth, int minHeight, int horizontalSpacing) {
		if (!isManualResized()) {
			height = minHeight; // minimal height
			width = minWidth; // minimal width
			// calculates the width and height of the component
			for (String textline : Utils.decomposeStrings(this.getPanelAttributes())) {
				height = height + textHeight();
				width = Math.max(textWidth(textline) + 10 + horizontalSpacing, width);
			}
			if (height < minHeight) height = minHeight;
			if (width < minWidth) width = minWidth;
		}
	}

	@Override
	@CustomFunction(param_defaults = "")
	public final boolean isManualResized() {
		return super.isManualResized();
	}

	@CustomFunction(param_defaults = "x, y, width, height, start, extent")
	protected final void drawArcOpen(float x, float y, float width, float height, float start, float extent) {
		this.shapes.add(new StyleShape(new Arc2D.Float(x * zoom, y * zoom, width * zoom, height * zoom, start, extent, Arc2D.OPEN), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x, y, width, height, start, extent")
	protected final void drawArcChord(float x, float y, float width, float height, float start, float extent) {
		this.shapes.add(new StyleShape(new Arc2D.Float(x * zoom, y * zoom, width * zoom, height * zoom, start, extent, Arc2D.CHORD), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x, y, width, height, start, extent")
	protected final void drawArcPie(float x, float y, float width, float height, float start, float extent) {
		this.shapes.add(new StyleShape(new Arc2D.Float(x * zoom, y * zoom, width * zoom, height * zoom, start, extent, Arc2D.PIE), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x, y, radius")
	protected final void drawCircle(int x, int y, int radius) {
		this.shapes.add(new StyleShape(new Ellipse2D.Float((int) ((x - radius) * zoom), (int) ((y - radius) * zoom), (int) (radius * 2 * zoom), (int) (radius * 2 * zoom)), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2")
	protected final void drawCurveCubic(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
		this.shapes.add(new StyleShape(new CubicCurve2D.Float(x1 * zoom, y1 * zoom, ctrlx1 * zoom, ctrly1 * zoom, ctrlx2 * zoom, ctrly2 * zoom, x2 * zoom, y2 * zoom), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x1, y1, ctrlx, ctrly, x2, y2")
	protected final void drawCurveQuad(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
		this.shapes.add(new StyleShape(new QuadCurve2D.Float(x1 * zoom, y1 * zoom, ctrlx * zoom, ctrly * zoom, x2 * zoom, y2 * zoom), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x, y, radiusX, radiusYs")
	protected final void drawEllipse(int x, int y, int radiusX, int radiusY) {
		this.shapes.add(new StyleShape(new Ellipse2D.Float((int) ((x - radiusX) * zoom), (int) ((y - radiusY) * zoom), (int) (radiusX * 2 * zoom), (int) (radiusY * 2 * zoom)), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x1, y1, x2, y2")
	protected final void drawLine(int x1, int y1, int x2, int y2) {
		this.shapes.add(new StyleShape(new Line2D.Float((int) (x1 * zoom), (int) (y1 * zoom), (int) (x2 * zoom), (int) (y2 * zoom)), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "y")
	protected final void drawLineHorizontal(int y) {
		this.shapes.add(new StyleShape(new Line2D.Float((int) (0 * zoom), (int) (y * zoom), getHandler().realignToGrid(false, (int) (width * zoom), true), (int) (y * zoom)), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x")
	protected final void drawLineVertical(int x) {
		this.shapes.add(new StyleShape(new Line2D.Float((int) (x * zoom), (int) (0 * zoom), (int) (x * zoom), getHandler().realignToGrid(false, (int) (height * zoom), true)), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "polygon")
	protected final void drawPolygon(Polygon polygon) {
		for (int i = 0; i < polygon.xpoints.length; i++) {
			polygon.xpoints[i] *= zoom;
		}
		for (int i = 0; i < polygon.ypoints.length; i++) {
			polygon.ypoints[i] *= zoom;
		}
		this.shapes.add(new StyleShape(polygon, tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x, y, width, height")
	protected final void drawRectangle(int x, int y, int width, int height) {
		this.shapes.add(new StyleShape(new Rectangle((int) (x * zoom), (int) (y * zoom), (int) (width * zoom), (int) (height * zoom)), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	@CustomFunction(param_defaults = "x, y, width, height, arcw, arch")
	protected final void drawRectangleRound(int x, int y, int width, int height, float arcw, float arch) {
		this.shapes.add(new StyleShape(new RoundRectangle2D.Float((int) (x * zoom), (int) (y * zoom), (int) (width * zoom), (int) (height * zoom), arcw * zoom, arch * zoom), tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	// EXAMPLE: drawShape(new java.awt.geom.RoundRectangle2D.Float(10,10, 50, 40, 15,15));
	// WARNING: Shapes aren't zoomed automatically
	@CustomFunction(param_defaults = "shape")
	protected final void drawShape(Shape shape) {
		this.shapes.add(new StyleShape(shape, tmpLineType, tmpLineThickness, tmpFgColor, tmpBgColor, tmpAlpha));
	}

	/*
	 * STYLING METHODS
	 */

	@CustomFunction(param_defaults = "lineType")
	protected final void setLineType(LineType lineType) {
		this.tmpLineType = lineType;
	}

	@CustomFunction(param_defaults = "lineThickness")
	protected final void setLineThickness(int lineThickness) {
		this.tmpLineThickness = lineThickness;
	}

	@CustomFunction(param_defaults = "foreground color")
	protected final void setForegroundColor(String fgColorString) {
		tmpFgColor = Utils.getColor(fgColorString);
		if (tmpFgColor == null) {
			if (fgColorString.equals("fg")) tmpFgColor = fgColor;
			else tmpFgColor = Constants.DEFAULT_FOREGROUND_COLOR;
		}
	}

	@CustomFunction(param_defaults = "background color")
	protected final void setBackgroundColor(String bgColorString) {
		tmpBgColor = Utils.getColor(bgColorString);
		if (tmpBgColor == null) {
			if (bgColorString.equals("bg")) tmpBgColor = bgColor;
			else tmpBgColor = Constants.DEFAULT_BACKGROUND_COLOR;
		}
//		 Transparency is 0% if none or 50% if anything else
		if (bgColorString.equals("none")) tmpAlpha = Constants.ALPHA_MIDDLE_TRANSPARENCY;
		else tmpAlpha = Constants.ALPHA_NO_TRANSPARENCY;
	}

	@CustomFunction(param_defaults = "")
	protected final void resetAll() {
		tmpLineThickness = Constants.DEFAULT_LINE_THICKNESS;
		tmpLineType = LineType.SOLID;
		tmpFgColor = Constants.DEFAULT_FOREGROUND_COLOR;
		tmpBgColor = Constants.DEFAULT_BACKGROUND_COLOR;
	}
}
