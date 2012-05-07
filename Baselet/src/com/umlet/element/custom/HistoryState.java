package com.umlet.element.custom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

import com.baselet.control.Constants;
import com.baselet.element.GridElement;


@SuppressWarnings("serial")
public class HistoryState extends GridElement {

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		g2.fillOval(0, 0, this.getWidth(), this.getHeight());
		// Measure the font and the message
		Rectangle2D bounds = this.getHandler().getFontHandler().getFont().getStringBounds("H", Constants.FRC);
		LineMetrics metrics = this.getHandler().getFontHandler().getFont().getLineMetrics("H", Constants.FRC);
		float width = (float) bounds.getWidth(); // The width of our text
		float lineheight = metrics.getHeight(); // Total line height
		float ascent = metrics.getAscent(); // Top of text to baseline

		// Now display the message centered horizontally and vertically in this
		float x0 = ((this.getWidth() - width) / 2);
		float y0 = ((this.getHeight() - lineheight) / 2 + ascent);
		g2.setColor(Color.WHITE);
		g2.drawString("H", x0, y0);
		g2.setColor(fgColor);

	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes

}
