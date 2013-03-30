package com.baselet.diagram.draw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Iterator;

import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;

public class DrawableCache implements Iterable<Drawable> {

	private ArrayList<Drawable> drawables = new ArrayList<Drawable>();
	
	private Style overlay = new Style();

	@Override
	public Iterator<Drawable> iterator() {
		return drawables.iterator();
	}

	public void add(Drawable drawable) {
		drawables.add(drawable);
	}

	public void clear() {
		drawables.clear();
	}
	
	public void setForegroundOverlay(Color color) {
		overlay.setFgColor(color);
	}

	public void removeForegroundOverlay() {
		overlay.setFgColor(null);
	}

	public void drawAll(Graphics2D g2, DiagramHandler handler) {
		for (Drawable d : drawables) {
			if (d.getShape() != null) {
				drawShape(d.getStyle(), d.getShape(), g2, handler);
			}
			if (d.getText() != null) {
				drawText(d.getStyle(), d.getText(), g2, handler);
			}
		}
	}

	private void drawShape(Style style, Shape s, Graphics2D g2, DiagramHandler handler) {
		// Shapes Background
		g2.setColor(style.getBgColor());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, style.getBgAlpha()));
		g2.fill(s);
		// Shapes Foreground
		g2.setColor(overlay.getFgColor() != null ? overlay.getFgColor() : style.getFgColor());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, style.getFgAlpha()));
		g2.setStroke(Utils.getStroke(style.getLineType(), style.getLineThickness()));
		g2.draw(s);
	}

	private void drawText(Style style, Text t, Graphics2D g2, DiagramHandler handler) {
		g2.setColor(overlay.getFgColor() != null ? overlay.getFgColor() : style.getFgColor());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, style.getFgAlpha()));
		handler.getFontHandler().setFontSize(style.getFontSize());
		g2.setFont(handler.getFontHandler().getFont());
		handler.getFontHandler().writeText(g2, t.getText(), t.getX(), t.getY(), t.getHorizontalAlignment());
		handler.getFontHandler().resetFontSize();

	}

}
