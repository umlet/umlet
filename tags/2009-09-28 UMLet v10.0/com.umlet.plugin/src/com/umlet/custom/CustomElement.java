package com.umlet.custom;

import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public abstract class CustomElement extends Entity {

	private float zoom;

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
	private boolean is_centered;

	private Vector<Shape> fillshapes;
	private Vector<Shape> drawshapes;
	private Vector<Text> texts;

	public CustomElement() {

		this.supportsColors = true;
		this.allowResize = true;
		this.fillshapes = new Vector<Shape>();
		this.drawshapes = new Vector<Shape>();
		this.texts = new Vector<Text>();
		this.is_centered = false;
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

		this.g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);
		composites = this.colorize(g2);

		// width and height must be zoomed back to 100% before any custom code is applied
		temp = this.getWidth() - 1;
		width = (int) (temp / zoom);
		temp = this.getHeight() - 1;
		height = (int) (temp / zoom);

		// secure this thread before executing the code!
		String key = "R" + Math.random();
		CustomElementSecurityManager.addThread(Thread.currentThread(), key);
		this.paint(); // calls the paint method of the specific custom element
		CustomElementSecurityManager.remThread(Thread.currentThread(), key);

		// After the custom code we zoom the width and height back
		width *= zoom;
		height *= zoom;

		this.drawShapes();

		// if (this.is_centered && (!this.allowResize || (this.autoResizeandManualResizeEnabled() && !this.isManualResized()))) {
		// int change = (this.width + 1 - this.getWidth()) / 2;
		// Point p = this.getLocation();
		// this.setLocation(p.x - change, p.y);
		// }
		// Resize elements if manual resize is not set
		if (!this.allowResize || (this.autoResizeandManualResizeEnabled() && !this.isManualResized())) {
			this.setSize(this.width + 1, this.height + 1);
		}
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

	protected final Dimension zoomedTextsize(String text) {
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
		this.texts.add(new Text(text, ((int) (width * zoom) - this.zoomedTextsize(text).width) / 2, (int) (y * zoom), LEFT, BOTTOM));
	}

	@CustomFunction(param_defaults = "text")
	protected final int width(String text) {
		return (int) (this.getHandler().getRealTextSize(g2, text).width + this.getHandler().getRealDistTextToLine());
	}

	@CustomFunction(param_defaults = "")
	protected final int textheight() {
		return (int) ((int) this.getHandler().getRealFontsize() + this.getHandler().getRealDistTextToLine());
	}

	@CustomFunction(param_defaults = "text")
	protected final int textwidth(String text) {
		return (int) (this.getHandler().getRealTextWidth(this.g2, text) + this.getHandler().getRealDistTextToLine());
	}

	@CustomFunction(param_defaults = "x,y,width,height")
	protected final void drawRect(int x, int y, int width, int height) {
		this.fillshapes.add(new Rectangle((int) (x * zoom), (int) (y * zoom), (int) (width * zoom), (int) (height * zoom)));
	}

	@CustomFunction(param_defaults = "x1,y1,x2,y2")
	protected final void drawLine(int x1, int y1, int x2, int y2) {
		this.drawshapes.add(new Line2D.Float((int) (x1 * zoom), (int) (y1 * zoom), (int) (x2 * zoom), (int) (y2 * zoom)));
	}

	@CustomFunction(param_defaults = "y")
	protected final void drawLineHorizontal(int y) {
		this.drawshapes.add(new Line2D.Float((int) (0 * zoom), (int) (y * zoom), (int) (width * zoom), (int) (y * zoom)));
	}

	@CustomFunction(param_defaults = "x")
	protected final void drawLineVertical(int x) {
		this.drawshapes.add(new Line2D.Float((int) (x * zoom), (int) (0 * zoom), (int) (x * zoom), (int) (height * zoom)));
	}

	@CustomFunction(param_defaults = "x,y,radius")
	protected final void drawCircle(int x, int y, int radius) {
		this.fillshapes.add(new Ellipse2D.Float((int) ((x - radius) * zoom), (int) ((y - radius) * zoom), (int) (radius * 2 * zoom), (int) (radius * 2 * zoom)));
	}

	@CustomFunction(param_defaults = "x,y,radiusX,radiusY")
	protected final void drawEllipse(int x, int y, int radiusX, int radiusY) {
		this.fillshapes.add(new Ellipse2D.Float((int) ((x - radiusX) * zoom), (int) ((y - radiusY) * zoom), (int) (radiusX * 2 * zoom), (int) (radiusY * 2 * zoom)));
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

	@CustomFunction(param_defaults = "shape")
	protected final void drawShape(Shape shape) {
		this.fillshapes.add(shape);
	}

	@CustomFunction(param_defaults = "value1,value2")
	protected final int min(int value1, int value2) {
		return Math.min(value1, value2);
	}

	@CustomFunction(param_defaults = "value1,value2")
	protected final int max(int value1, int value2) {
		return Math.max(value1, value2);
	}

	@Override
	@CustomFunction(param_defaults = "")
	public final boolean isManualResized() {
		return super.isManualResized();
	}

	@CustomFunction(param_defaults = "allow")
	protected final void allowResize(boolean allow) {
		this.allowResize = allow;
	}

	/*
	 * @CustomFunction(param_defaults="x,y")
	 * protected final void addStickingPoint(int x, int y)
	 * {
	 * if(this.first_paint)
	 * {
	 * Point p = new Point(x,y);
	 * this.stickingpolygon.addLine(this.laststickingpoint, p);
	 * this.laststickingpoint = p;
	 * }
	 * }
	 */

	@CustomFunction(param_defaults = "")
	protected final void setElementCentered() {
		this.is_centered = true;
	}
}
