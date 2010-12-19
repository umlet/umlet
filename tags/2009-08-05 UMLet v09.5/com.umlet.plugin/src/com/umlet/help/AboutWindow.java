package com.umlet.help;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.border.LineBorder;

import com.umlet.constants.Constants;
import com.umlet.control.BrowserLauncher;
import com.umlet.control.Umlet;

@SuppressWarnings("serial")
public class AboutWindow extends HelpLabel {
	
	@Override
	protected String getStartUpFileName() {
		return Umlet.getInstance().getHomePath() + "html/aboutumlet.html";
	}

	private static AboutWindow window;
	private Rectangle link1;
	private Rectangle link2;
	
	private AboutWindow() 
	{
		super(null);
		link1 = new Rectangle(29,130,80,25);
		link2 = new Rectangle(45,150,85,25);
		this.setBorder(new LineBorder(Color.black,1,true));	
	}
	
	public static AboutWindow getInstance() {
		if(window == null)
			window = new AboutWindow();
		return window;
	}
	
	@Override
	public void setInvisible() {
		
	}

	@Override
	public void setVisible() 
	{
		Umlet.getInstance().getGUI().openDialog("About UMLet", this);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(link1.contains(e.getX(), e.getY())) {
			BrowserLauncher.openURL("http://www.umlet.com");
		}
		else if(link2.contains(e.getX(), e.getY())) {
			BrowserLauncher.openURL("mailto:info@umlet.com");
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		super.mouseEntered(e);
		if(this.getTopLevelAncestor() != null)
			this.getTopLevelAncestor().setCursor(Constants.handCursor);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(this.getTopLevelAncestor() != null)
		{
			if(link1.contains(e.getX(), e.getY()) || link2.contains(e.getX(), e.getY()))
				this.getTopLevelAncestor().setCursor(Constants.handCursor);
			else
				this.getTopLevelAncestor().setCursor(Constants.defCursor);
		}
	}
}
