package com.umlet.element.base;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import java.awt.Font;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.gui.base.NotificationRemoveTask;

public class DiagramNotification extends JComponent {

	private final static Logger log = Logger.getLogger(DiagramNotification.class);
	private DiagramHandler handler;
	private String message;
	
	public DiagramNotification(DiagramHandler handler, String message){
		this.handler = handler;
		this.message = message;
		Timer timer = new Timer();
		TimerTask removeTask = new NotificationRemoveTask(this,Umlet.getInstance().getGUI().getCurrentDiagram());
		timer.schedule(
				removeTask, 
				3000);
	}
	
	
	@Override
	public final void paint(Graphics g) {
				
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		Composite old = g2.getComposite(); // Store non-transparent composite

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f)); // 40% transparency
		g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
		
		Font font = new Font(Constants.FONT, Font.BOLD, 10);
		g2.setFont(font);
		TextLayout tl = new TextLayout(message, font, handler.getFRC(g2));
		log.debug("zoomFactor="+handler.getZoomFactor());
		log.debug("bounds.width="+tl.getBounds().getWidth());
		log.debug("width="+getWidth());
//		int textX = (int)((getWidth() / 2 - tl.getBounds().getWidth() / 2));
		int textX = 5;
		int textY= (int) (getHeight() / 2 + tl.getBounds().getHeight() / 2);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f)); // 70% transparency
		handler.writeText(g2, message, textX, textY, false);
		

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f)); // 5% transparency
		g2.setColor(java.awt.Color.blue);
		g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		
		// Reset old transparency factor and font
		g2.setComposite(old);
		g2.setFont(handler.getZoomedFont());
	}

}
