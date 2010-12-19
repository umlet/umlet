package com.umlet.gui.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.io.File;
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
	private boolean visible;

	public StartUpHelpText(DrawPanel panel) {
		super();
		this.panel = panel;

		// Only show the startuphelp.html if the file exists (to make batch usage of the umlet.jar file alone possible)
		if (new File(getStartUpFileName()).exists()) {
			panel.add(this);
			panel.addContainerListener(this);
			panel.addComponentListener(this);
			showHTML(this);
			setVisible();
		}
	}

	public void setVisible() {
		if (panel != null) {
			this.setVisible(true);
			this.panel.repaint();
		}
	}

	@Override
	public void setEnabled(boolean en) {
		super.setEnabled(en);
		if (en && this.visible) {
			if (this.panel.getAllEntities().size() == 0) this.setVisible();
			else this.visible = false;
		}
		else {
			this.visible = this.isVisible();
			this.setInvisible();
		}
	}

	public void setInvisible() {
		this.setVisible(false);
		if (this.panel != null) this.panel.repaint();
	}

	public void componentAdded(ContainerEvent e) {
		if (!this.equals(e.getChild()) && e.getChild() instanceof Entity) this.setInvisible();
	}

	public void componentRemoved(ContainerEvent e) {
		if ((e.getContainer().getComponentCount() <= 1) && !this.equals(e.getChild())) this.setVisible();
	}

	protected String getStartUpFileName() {
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

	public void componentResized(ComponentEvent arg0) {
		Dimension size = this.panel.getSize();
		Dimension labelSize = this.getPreferredSize();
		this.setSize(labelSize);
		this.setLocation(size.width / 2 - (labelSize.width / 2),
				Math.max(25, size.height / 2 - labelSize.height));
	}

	public void componentHidden(ComponentEvent arg0) {}

	public void componentMoved(ComponentEvent arg0) {}

	public void componentShown(ComponentEvent arg0) {}

}
