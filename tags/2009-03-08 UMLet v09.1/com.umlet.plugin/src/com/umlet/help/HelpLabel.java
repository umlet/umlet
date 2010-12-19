package com.umlet.help;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.JLabel;

import com.umlet.control.diagram.DrawPanel;


//abscract Label for beeing able to insert html labels that are read from a html file.
//the component is its own listener for mouselisting purposes!
@SuppressWarnings("serial")
public abstract class HelpLabel extends JLabel implements MouseListener, MouseMotionListener{
	
	protected String helptext;
	protected DrawPanel diagram;
	
	protected abstract String getStartUpFileName();
	
	private String readAboutTextFromFile() 
	{
		this.helptext = "";
		try
		{
			File f = new File(this.getStartUpFileName());
			BufferedReader r = new BufferedReader(new FileReader(f));
			for(String line = r.readLine(); line != null; line = r.readLine())
				this.helptext += line;

		} catch(Exception ex) { ex.printStackTrace(); }
		return this.helptext;
	}
	
	protected HelpLabel(DrawPanel diagram) {
		super();
	    this.setText(this.readAboutTextFromFile());   
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.diagram = diagram;
	}
	
	public abstract void setVisible();
	public abstract void setInvisible();

	public void mouseClicked(MouseEvent arg0) {

	}

	public void mouseEntered(MouseEvent arg0) {
		
	}

	public void mouseExited(MouseEvent arg0) {
	
	}

	public void mousePressed(MouseEvent arg0) {
		
	}

	public void mouseReleased(MouseEvent arg0) {
		
	}

	public void mouseDragged(MouseEvent arg0) {
		
	}

	public void mouseMoved(MouseEvent arg0) {
		
	}

}
