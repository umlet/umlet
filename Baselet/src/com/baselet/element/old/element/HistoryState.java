package com.baselet.element.old.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.Direction;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class HistoryState extends OldGridElement {

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);

		g2.fillOval(0, 0, getRectangle().width, getRectangle().height);
		// Measure the font and the message
		Rectangle2D bounds = HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont().getStringBounds("H", g2.getFontRenderContext());
		LineMetrics metrics = HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont().getLineMetrics("H", g2.getFontRenderContext());
		float width = (float) bounds.getWidth(); // The width of our text
		float lineheight = metrics.getHeight(); // Total line height
		float ascent = metrics.getAscent(); // Top of text to baseline

		// Now display the message centered horizontally and vertically in this
		float x0 = (getRectangle().width - width) / 2;
		float y0 = (getRectangle().height - lineheight) / 2 + ascent;
		g2.setColor(Color.WHITE);
		g2.drawString("H", x0, y0);
		g2.setColor(fgColor);

	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		return new HashSet<Direction>(); // deny size changes
	}

}
