package com.umlet.gui.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.JEditorPane;

import org.apache.log4j.Logger;

import com.umlet.control.Umlet;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.element.base.Entity;
import com.umlet.gui.base.listeners.HyperLinkActiveListener;

@SuppressWarnings("serial")
public class StartUpHelpText extends JEditorPane implements ContainerListener, ComponentListener {

	private static Logger log = Logger.getLogger(StartUpHelpText.class.getName());

	private DrawPanel panel;
	
	public StartUpHelpText(DrawPanel panel) {
		super();
		this.panel = panel;

		// If the GUI is null (e.g.: if UMLet is used in batch mode) the startup help text is not required
		if (Umlet.getInstance().getGUI() == null) return;

		panel.add(this);
		panel.addContainerListener(this);
		panel.addComponentListener(this);
		this.addMouseListener(new DelegatingMouseListener());
		showHTML(this);
		setVisible();
	}

	private void setVisible() {
		if (panel != null) {
			this.setVisible(true);
			this.panel.repaint();
		}
	}

	private void setInvisible() {
		this.setVisible(false);
		if (this.panel != null) this.panel.repaint();
	}

	private String getStartUpFileName() {
		return Umlet.getInstance().getHomePath() + "html/startuphelp.html";
	}

	private void showHTML(JEditorPane edit) {
		try {
			edit.setPage(new URL("file:///" + getStartUpFileName()));
			edit.addHyperlinkListener(new HyperLinkActiveListener());
			edit.setEditable(false);
			edit.setBackground(Color.WHITE);
			edit.setSelectionColor(edit.getBackground());
			edit.setSelectedTextColor(edit.getForeground());
		} catch (Exception e) {
			log.error(null, e);
		}
	}

	@Override
	public void componentAdded(ContainerEvent e) {
		if (!this.equals(e.getChild()) && (e.getChild() instanceof Entity)) this.setInvisible();
	}

	@Override
	public void componentRemoved(ContainerEvent e) {
		if ((e.getContainer().getComponentCount() <= 1) && !this.equals(e.getChild())) this.setVisible();
	}
	
	@Override
	public void componentResized(ComponentEvent arg0) {
		Dimension size = this.panel.getSize();
		Dimension labelSize = this.getPreferredSize();
		this.setSize(labelSize);
		this.setLocation(size.width / 2 - (labelSize.width / 2),
				Math.max(25, size.height / 2 - labelSize.height));
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {}

	@Override
	public void componentMoved(ComponentEvent arg0) {}

	@Override
	public void componentShown(ComponentEvent arg0) {}

	/**
	 * The MouseListener of this JEditorPane just delegates the
	 * MouseEvents up to the DiagramListener of the Handler
	 */
	private class DelegatingMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			panel.getHandler().getListener().mouseClicked(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			panel.getHandler().getListener().mouseEntered(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			panel.getHandler().getListener().mouseExited(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			panel.getHandler().getListener().mousePressed(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			panel.getHandler().getListener().mouseReleased(e);
		}
	}
}
