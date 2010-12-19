package com.baselet.element;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.TextLayout;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.gui.base.NotificationRemoveTask;

public class DiagramNotification extends JComponent {

	private static final long serialVersionUID = 1L;
	private final static Logger log = Logger.getLogger(Utils.getClassName());
	private DiagramHandler handler;
	private String message;

	public DiagramNotification(DiagramHandler handler, String message) {
		this.handler = handler;
		this.message = message;
		Timer timer = new Timer();
		TimerTask removeTask = new NotificationRemoveTask(this, Main.getInstance().getGUI().getCurrentDiagram());
		timer.schedule(
				removeTask,
				3000);
		this.setSize(100, 20);
		this.adaptDimensions();
	}

	@Override
	public final void paint(Graphics g) {

		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		Composite old = g2.getComposite(); // Store non-transparent composite

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f)); // 40% transparency
		// g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

		Font font = new Font(Constants.FONT, Font.BOLD, 10);
		g2.setFont(font);
		adaptDimensions();
		TextLayout tl = new TextLayout(message, font, Constants.FRC);

		log.debug("zoomFactor=" + handler.getZoomFactor());
		log.debug("bounds.width=" + tl.getBounds().getWidth());
		log.debug("width=" + getWidth());
		// int textX = (int)((getWidth() / 2 - tl.getBounds().getWidth() / 2));
		int textX = 5;
		int textY = (int) (getHeight() / 2 + tl.getBounds().getHeight() / 2);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f)); // 70% transparency
		handler.getFontHandler().writeText(g2, message, textX, textY, false);

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f)); // 5% transparency
		g2.setColor(java.awt.Color.blue);
		// g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		// Reset old transparency factor and font
		g2.setComposite(old);
		g2.setFont(handler.getFontHandler().getFont());
	}

	private void adaptDimensions() {
		Rectangle currentView = this.handler.getDrawPanel().getScrollPane().getViewport().getViewRect();
		Graphics2D g2 = (Graphics2D) this.handler.getDrawPanel().getGraphics();
		int x = (int) (currentView.getMaxX() - g2.getFontMetrics().stringWidth(message));
		int y = (int) (currentView.getMinY() + 10);
		this.setLocation(x, y);
		int width = g2.getFontMetrics().stringWidth(message);
		int height = g2.getFontMetrics().getHeight() + 10;
		this.setSize(width, height);
	}
}
