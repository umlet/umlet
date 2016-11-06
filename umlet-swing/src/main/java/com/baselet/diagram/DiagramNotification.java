package com.baselet.diagram;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;

import javax.swing.JComponent;

import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.control.basics.geom.Rectangle;

public class DiagramNotification extends JComponent {

	private static final int BORDER_DISTANCE = 5;
	private static final double VBUFFER = 1;

	private static final long serialVersionUID = 1L;
	private final String message;
	private final Rectangle drawPanelSize;

	private static final Font notificationFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
	private static final FontRenderContext frc = new FontRenderContext(null, true, true);
	private final Color textColor;
	private final Color backgroundColor;

	public DiagramNotification(Rectangle drawPanelSize, String message, Color textColor, Color backgroundColor) {
		this.message = message;
		this.drawPanelSize = drawPanelSize;
		this.textColor = textColor;
		this.backgroundColor = backgroundColor;
		this.setSize(100, 20);
		adaptDimensions();
	}

	@Override
	public final void paint(Graphics g) {

		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		Composite old = g2.getComposite(); // Store non-transparent composite

		g2.setColor(textColor);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f)); // 40% transparency
		g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		Font drawFont = g2.getFont();
		g2.setFont(notificationFont);
		adaptDimensions();

		int textX = 5;
		double textY = BORDER_DISTANCE * 0.75;
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f)); // 70% transparency

		String[] line = safeSplit(message);
		for (String element : line) {
			textY += textSize(element).getHeight() + VBUFFER;
			g2.drawString(element, textX, (int) textY);
		}

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f)); // 5% transparency
		g2.setColor(backgroundColor);
		g2.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
		g2.setComposite(old);
		g2.setFont(drawFont);
	}

	private void adaptDimensions() {
		DimensionDouble fullTextSize = getTextblockDimensions(message);
		int x = (int) (drawPanelSize.getX2() - fullTextSize.getWidth() - 20);
		int y = drawPanelSize.getY() + 10;
		this.setLocation(x, y);
		this.setSize((int) fullTextSize.getWidth() + 10, (int) Math.round(fullTextSize.getHeight() + BORDER_DISTANCE * 2));
	}

	private DimensionDouble textSize(String line) {
		return new FormattedFont(line, notificationFont.getSize(), notificationFont, frc).getDimensions();
	}

	private String[] safeSplit(String message) {
		String[] line = message.split("\n");
		if (line.length == 0) {
			line = new String[] { message };
		}
		return line;
	}

	private DimensionDouble getTextblockDimensions(String message) {
		double maxWidth = 0;
		double heightAccumulator = 0;
		for (String line : safeSplit(message)) {
			DimensionDouble dim = textSize(line);
			maxWidth = Math.max(maxWidth, dim.getWidth());
			heightAccumulator += dim.getHeight() + VBUFFER;
		}
		return new DimensionDouble(maxWidth, heightAccumulator);
	}
}
