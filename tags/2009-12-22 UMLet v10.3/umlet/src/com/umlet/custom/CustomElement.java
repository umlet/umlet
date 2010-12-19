package com.umlet.custom;

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
import java.util.logging.Handler;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public abstract class CustomElement extends Entity {

	protected float zoom;
	// We need a seperate program behaviour for 10% and 20% zoom. Otherwise manual resized entities would have the following bugs:
	// 10%: entity grow without end, 20%: relations don't stick on the right or bottom end of entity
	private boolean bugfix;

	private class Text {
		private String text;
		private int x, y;
		private int align, valign;

		private Text(String text, int x, int y, int align, int valign) {
			this.text = text;
			this.x = x;
			this.y = y;
			this.align = align;
			this.valign = valign;
		}
	}

	protected static int LEFT = 0, RIGHT = 2, CENTER = 1, TOP = 0, BOTTOM = 2;

	protected Graphics2D g2;
	protected float temp;
	protected int width, height;
	protected Composite[] composites;
	private String code;
	// private Point laststickingpoint;
	private boolean allowResize;
//	private boolean is_centered;

	private Vector<Shape> fillshapes;
	private Vector<Shape> drawshapes;
	private Vector<Text> texts;

	public CustomElement() {

		this.supportsColors = true;
		this.allowResize = true;
		this.fillshapes = new Vector<Shape>();
		this.drawshapes = new Vector<Shape>();
		this.texts = new Vector<Text>();
//		this.is_centered = false;
	}

	public abstract void paint();

	public final void setCode(String code) {
		this.code = code;
	}

	public final String getCode() {
		return this.code;
	}

	private void drawShapes() {

		g2.setColor(_fillColor);
		g2.setComposite(this.composites[1]);

		for (Shape s : this.fillshapes)
			this.g2.fill(s);

		g2.setComposite(this.composites[0]);
		g2.setColor(_activeColor);

		for (Shape s : this.fillshapes)
			this.g2.draw(s);

		for (Shape s : this.drawshapes)
			this.g2.draw(s);

		for (Text t : this.texts)
			this.getHandler().writeText(this.g2, t.text, t.x, t.y, t.align, t.valign);

		this.texts.clear();
		this.fillshapes.clear();
		this.drawshapes.clear();

	}

	@Override
	public final void paintEntity(Graphics g) {

		zoom = getHandler().getZoomFactor();
		if (zoom < 0.25) bugfix = true;
		else bugfix = false;

		this.g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);
		composites = this.colorize(g2);

		// width and height must be zoomed back to 100% before any custom code is applied
		temp = this.getWidth();
		width = Math.round(temp / zoom); //use Math.round cause (int) would round down from 239.99998 to 240
		temp = this.getHeight();
		height = Math.round(temp / zoom);
		// secure this thread before executing the code!
		String key = "R" + Math.random();
		CustomElementSecurityManager.addThread(Thread.currentThread(), key);
		// Set width and height on grid (used for manually resized custom elements mainly
		width = onGrid(width);
		height = onGrid(height);
		this.paint(); // calls the paint method of the specific custom element
		width = onGrid(width);
		height = onGrid(height);
		CustomElementSecurityManager.remThread(Thread.currentThread(), key);

		// After the custom code we zoom the width and height back
		width *= zoom;
		height *= zoom;

		this.drawShapes();

		// Resize elements if manual resize is not set
//		 if (!this.allowResize || (this.autoResizeandManualResizeEnabled() && !this.isManualResized())) {
		// CHANGED: Resize every custom object by +1px to get consistent height and width
		if (!bugfix) this.setSize(this.width+1, this.height+1);
//		 }
	}

	@Override
	public final Entity CloneFromMe() {
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
		Dimension d = this.getHandler().getZoomedTextSize(g2, text);
		d.height = (int) (this.getHandler().getZoomedDistTextToLine() + this.getHandler().getZoomedFontsize());
		d.width += this.getHandler().getZoomedDistTextToLine();
		return d;
	}

	@CustomFunction(param_defaults = "text,x,y")
	protected final void print(String text, int x, int y) {
		this.texts.add(new Text(text, (int) (x * zoom), (int) (y * zoom), LEFT, BOTTOM));
	}

	@CustomFunction(param_defaults = "text,y")
	protected final void printLeft(String text, int y) {
		this.texts.add(new Text(text, (int) this.getHandler().getZoomedDistLineToText(), (int) (y * zoom), LEFT, BOTTOM));
	}

	@CustomFunction(param_defaults = "text,y")
	protected final void printRight(String text, int y) {
		this.texts.add(new Text(text, (int) (width * zoom) - this.zoomedTextsize(text).width, (int) (y * zoom), LEFT, BOTTOM));
	}

	@CustomFunction(param_defaults = "text,y")
	protected final void printCenter(String text, int y) {
		this.texts.add(new Text(text, (onGrid((int) (width * zoom)) - this.zoomedTextsize(text).width) / 2, (int) (y * zoom), LEFT, BOTTOM));
	}

	@CustomFunction(param_defaults = "")
	protected final int textHeight() {
		return (int) ((int) this.getHandler().getRealFontsize() + this.getHandler().getRealDistTextToLine());
	}

	@CustomFunction(param_defaults = "text")
	protected final int textWidth(String text) {
		return getHandler().getRealTextWidth(this.g2, text) + (int) getHandler().getRealDistTextToLine();
	}

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
		//BUGFIX for 10% and 20% zoom: Otherwise a manual resized entity border wouldn't be visible because of the exclusion of line 136
		if (bugfix) value--;
		return value;
	}

	@CustomFunction(param_defaults = "value1,value2")
	protected final int min(int value1, int value2) {
		return Math.min(value1, value2);
	}

	@CustomFunction(param_defaults = "value1,value2")
	protected final int max(int value1, int value2) {
		return Math.max(value1, value2);
	}

	@CustomFunction(param_defaults = "allow")
	protected final void allowResize(boolean allow) {
		this.allowResize = allow;
	}

	@CustomFunction(param_defaults = "minWidth, minHeight, horizontalSpacing")
	protected final void setAutoresize(int minWidth, int minHeight, int horizontalSpacing) {
		if (!isManualResized()) {
			height = minHeight; // minimal height
			width = minWidth; // minimal width
			// calculates the width and height of the component
			for (String textline : Constants.decomposeStrings(this.getState())) {
				height = height + textHeight();
				width = max(textWidth(textline) + 10 + horizontalSpacing, width);
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

//	@CustomFunction(param_defaults = "")
//	protected final void setElementCentered() {
//		this.is_centered = true;
//	}

	@CustomFunction(param_defaults = "x,y,width,height,start,extent")
	protected final void drawArcOpen(float x, float y, float width, float height, float start, float extent) {
		this.fillshapes.add(new Arc2D.Float(x * zoom, y * zoom, width * zoom, height * zoom, start, extent, Arc2D.OPEN));
	}

	@CustomFunction(param_defaults = "x,y,width,height,start,extent")
	protected final void drawArcChord(float x, float y, float width, float height, float start, float extent) {
		this.fillshapes.add(new Arc2D.Float(x * zoom, y * zoom, width * zoom, height * zoom, start, extent, Arc2D.CHORD));
	}

	@CustomFunction(param_defaults = "x,y,width,height,start,extent")
	protected final void drawArcPie(float x, float y, float width, float height, float start, float extent) {
		this.fillshapes.add(new Arc2D.Float(x * zoom, y * zoom, width * zoom, height * zoom, start, extent, Arc2D.PIE));
	}

	@CustomFunction(param_defaults = "x,y,radius")
	protected final void drawCircle(int x, int y, int radius) {
		this.fillshapes.add(new Ellipse2D.Float((int) ((x - radius) * zoom), (int) ((y - radius) * zoom), (int) (radius * 2 * zoom), (int) (radius * 2 * zoom)));
	}

	@CustomFunction(param_defaults = "x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2")
	protected final void drawCurveCubic(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
		this.fillshapes.add(new CubicCurve2D.Float(x1 * zoom, y1 * zoom, ctrlx1 * zoom, ctrly1 * zoom, ctrlx2 * zoom, ctrly2 * zoom, x2 * zoom, y2 * zoom));
	}

	@CustomFunction(param_defaults = "x1, y1, ctrlx, ctrly, x2, y2")
	protected final void drawCurveQuad(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
		this.fillshapes.add(new QuadCurve2D.Float(x1 * zoom, y1 * zoom, ctrlx * zoom, ctrly * zoom, x2 * zoom, y2 * zoom));
	}

	@CustomFunction(param_defaults = "x,y,radiusX,radiusY")
	protected final void drawEllipse(int x, int y, int radiusX, int radiusY) {
		this.fillshapes.add(new Ellipse2D.Float((int) ((x - radiusX) * zoom), (int) ((y - radiusY) * zoom), (int) (radiusX * 2 * zoom), (int) (radiusY * 2 * zoom)));
	}

	@CustomFunction(param_defaults = "x1,y1,x2,y2")
	protected final void drawLine(int x1, int y1, int x2, int y2) {
		this.drawshapes.add(new Line2D.Float((int) (x1 * zoom), (int) (y1 * zoom), (int) (x2 * zoom), (int) (y2 * zoom)));
	}

	@CustomFunction(param_defaults = "y")
	protected final void drawLineHorizontal(int y) {
		this.drawshapes.add(new Line2D.Float((int) (0 * zoom), (int) (y * zoom), getHandler().realignToGrid(false, (int) (width * zoom), true), (int) (y * zoom)));
	}

	@CustomFunction(param_defaults = "x")
	protected final void drawLineVertical(int x) {
		this.drawshapes.add(new Line2D.Float((int) (x * zoom), (int) (0 * zoom), (int) (x * zoom), getHandler().realignToGrid(false, (int) (height * zoom), true)));
	}

	@CustomFunction(param_defaults = "polygon")
	protected final void drawPolygon(Polygon polygon) {
		for (int i = 0; i < polygon.xpoints.length; i++) {
			polygon.xpoints[i] *= zoom;
		}
		for (int i = 0; i < polygon.ypoints.length; i++) {
			polygon.ypoints[i] *= zoom;
		}
		this.fillshapes.add(polygon);
	}

	@CustomFunction(param_defaults = "x,y,width,height")
	protected final void drawRectangle(int x, int y, int width, int height) {
		this.fillshapes.add(new Rectangle((int) (x * zoom), (int) (y * zoom), (int) (width * zoom), (int) (height * zoom)));
	}

	@CustomFunction(param_defaults = "x,y,width,height,arcw,arch")
	protected final void drawRectangleRound(int x, int y, int width, int height, float arcw, float arch) {
		this.fillshapes.add(new RoundRectangle2D.Float((int) (x * zoom), (int) (y * zoom), (int) (width * zoom), (int) (height * zoom), arcw * zoom, arch * zoom));
	}

	// EXAMPLE: drawShape(new java.awt.geom.RoundRectangle2D.Float(10,10, 50, 40, 15,15));
	// WARNING: Shapes aren't zoomed automatically
	@CustomFunction(param_defaults = "shape")
	protected final void drawShape(Shape shape) {
		this.fillshapes.add(shape);
	}
}
