package com.baselet.diagram.draw;

import java.awt.Color;
import java.awt.Graphics;

import com.baselet.control.Constants;
import com.baselet.control.DimensionFloat;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.FontHandler.FormatLabels;
import com.baselet.element.Dimension;

public abstract class BaseDrawHandler {

	protected Color bgColor;
	protected Color fgColor;

	protected DiagramHandler handler;

	protected DrawableCache drawables = new DrawableCache();

	protected Style style;
	
	protected float width;
	protected float height;
	
	public void setHandler(DiagramHandler handler) {
		this.handler = handler;
		this.style = new Style();
		resetStyle();
	}

	public void setSize(Dimension size) {
		this.width = size.width;
		this.height = size.height;
	}
	
	protected void addDrawable(Drawable drawable) {
		drawables.add(drawable);
	}

	public void drawAll(boolean isSelected) {
		if (isSelected) {
			drawables.setForegroundOverlay(Constants.DEFAULT_SELECTED_COLOR);
		} else {
			drawables.removeForegroundOverlay();
		}
		drawAll();
	}

	public void clearCache() {
		drawables.clear();
	}

	protected void addText(Text t) {
		addDrawable(new Drawable(style.cloneFromMe(), t));
	}

	public final void print(String text, float x, float y, AlignHorizontal align) {
		addText(new Text(text, x * getZoom(), y * getZoom(), align));
	}

	public final float textHeightWithSpace() {
		return textHeight() + 2;
	}

	public final float textHeight() {
		return textDimension("dummy").getHeight();
	}

	public final float textWidth(String text) {
		return textDimension(text).getWidth();
	}

	public final void setForeground(String color, float alpha) {
		setForegroundColor(color);
		setForegroundAlpha(alpha);
	}

	public final void setForeground(Color color, float alpha) {
		setForegroundColor(color);
		setForegroundAlpha(alpha);
	}

	public final void setBackground(String color, float alpha) {
		setBackgroundColor(color);
		setBackgroundAlpha(alpha);
	}

	public final void setBackground(Color color, float alpha) {
		setBackgroundColor(color);
		setBackgroundAlpha(alpha);
	}

	public final void setForegroundColor(String color) {
		if (color.equals("fg")) setForegroundColor(fgColor);
		else setForegroundColor(Utils.getColor(color)); // if fgColor is not a valid string null will be set
	}

	public final void setForegroundColor(Color color) {
		if (color == null) style.setFgColor(Constants.DEFAULT_FOREGROUND_COLOR);
		else style.setFgColor(color);
	}

	public final void setBackgroundColor(String color) {
		if (color.equals("bg")) setBackgroundColor(bgColor);
		else setBackgroundColor(Utils.getColor(color));
	}

	public final void setBackgroundColor(Color color) {
		if (color == null) style.setBgColor(Constants.DEFAULT_BACKGROUND_COLOR);
		else style.setBgColor(color);
	}

	public final void setForegroundAlpha(float alpha) {
		style.setFgAlpha(alpha);
	}

	public final void setBackgroundAlpha(float alpha) {
		style.setBgAlpha(alpha);
	}

	public void resetColorSettings() {
		setForeground("fg", Constants.ALPHA_NO_TRANSPARENCY);
		setBackground("bg", Constants.ALPHA_FULL_TRANSPARENCY);
	}

	public final void setFontSize(float fontSize) {
		style.setFontSize(fontSize);
	}

	public final void setFontSize(String fontSize) {
		if (fontSize != null) {
			try {
				setFontSize(Float.valueOf(fontSize));
			} catch (NumberFormatException e) {/*do nothing*/}
		}
	}
	
	public final void setLineType(LineType type) {
		style.setLineType(type);
	}

	public final void setLineType(String type) {
		for (LineType lt : LineType.values()) {
			if (lt.getValue().equals(type)) {
				style.setLineType(lt);
			}
		}

		if (FormatLabels.BOLD.equals(type)) style.setLineThickness(2.0f);
	}
	
	public final void setLineThickness(float lineThickness) {
		style.setLineThickness(lineThickness);
	}
	
	public void resetStyle() {
		resetColorSettings();
		style.setFontSize(getDefaultFontSize());
		style.setLineType(LineType.SOLID);
		style.setLineThickness(1.0f);
	}

	public Style getCurrentStyle() {
		return style.cloneFromMe();
	}
	
	public void setCurrentStyle(Style style) {
		this.style = style;
	}

	/**
	 * exists to apply all facet operations without real drawing (eg: necessary to calculate space which is needed for autoresize)
	 */
	public PseudoDrawHandler getPseudoDrawHandler() {
		PseudoDrawHandler counter = new PseudoDrawHandler();
		counter.setHandler(handler);
		counter.width = this.width;
		counter.height = this.height;
		return counter;
	}

	/*
	 * HELPER METHODS
	 */
	
	public abstract void setGraphics(Graphics g);
	public abstract void drawAll();
	abstract float getZoom();
	abstract DimensionFloat textDimension(String string);
	public abstract float getDistanceBetweenTexts();
	abstract float getDefaultFontSize();

	/*
	 * DRAW METHODS
	 */
	public abstract void drawArcOpen(float x, float y, float width, float height, float start, float extent);
	public abstract void drawArcChord(float x, float y, float width, float height, float start, float extent);
	public abstract void drawArcPie(float x, float y, float width, float height, float start, float extent);
	public abstract void drawCircle(float x, float y, float radius);
	public abstract void drawCurveCubic(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2);
	public abstract void drawCurveQuad(float x1, float y1, float ctrlx, float ctrly, float x2, float y2);
	public abstract void drawEllipse(float x, float y, float radiusX, float radiusY);
	public abstract void drawLine(float x1, float y1, float x2, float y2);
	public abstract void drawLineHorizontal(float y);
	public abstract void drawLineVertical(float x);
	public abstract void drawRectangle(float x, float y, float width, float height);
	public abstract void drawRectangleRound(float x, float y, float width, float height, float arcw, float arch);

}
