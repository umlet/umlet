package com.umlet.help;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import com.umlet.control.Umlet;
import com.umlet.control.diagram.DrawPanel;

@SuppressWarnings("serial")
public class StartUpHelpLabel extends HelpLabel implements ContainerListener, ComponentListener{
	
	private boolean visible;
	
	public StartUpHelpLabel(DrawPanel panel) {
		super(panel);
		panel.add(this);
		panel.addContainerListener(this);
		panel.addComponentListener(this);
		this.visible = true;
	}
	
	public void setVisible()
	{
		if(!"".equals(this.helptext) && this.diagram != null) {	
	        this.setVisible(true);
	        this.diagram.repaint();
		}
	}
	
	@Override
	public void setEnabled(boolean en) {
		super.setEnabled(en);
		if(en && this.visible) {
			if(this.diagram.getAllEntities().size() == 0)
				this.setVisible();
			else
				this.visible = false;
		}
		else {
			this.visible = this.isVisible();
			this.setInvisible();
		}
	}

	public void setInvisible()
	{
		this.setVisible(false);
		if(this.diagram != null)
			this.diagram.repaint();
	}
	
	public void componentAdded(ContainerEvent e) {
		if(!this.equals(e.getChild()))
			this.setInvisible();	
	}

	public void componentRemoved(ContainerEvent e) {
		if(e.getContainer().getComponentCount() <= 1 && !this.equals(e.getChild()))
			this.setVisible();	
	}

	@Override
	protected String getStartUpFileName() {
		return Umlet.getInstance().getHomePath() + "html/startuphelp.html";
	}

	public void componentHidden(ComponentEvent arg0) {
		
	}

	public void componentMoved(ComponentEvent arg0) {
		
	}

	public void componentResized(ComponentEvent arg0) {
        Dimension size = this.diagram.getSize();
        Dimension labelSize = this.getPreferredSize();
        this.setSize(labelSize);
        this.setLocation(size.width/2 - (labelSize.width/2),
                Math.max(0,size.height/2 - labelSize.height*2));
	}

	public void componentShown(ComponentEvent arg0) {
		
	}
	
	
}
