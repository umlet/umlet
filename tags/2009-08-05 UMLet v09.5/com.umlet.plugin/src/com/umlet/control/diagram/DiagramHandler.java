package com.umlet.control.diagram;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.io.DiagramFileHandler;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Relation;
import com.umlet.help.StartUpHelpLabel;
import com.umlet.listeners.DiagramListener;
import com.umlet.listeners.EntityListener;
import com.umlet.listeners.RelationListener;

public class DiagramHandler {
	private boolean isChanged;
	private DiagramFileHandler fileHandler;
	private DrawPanel drawpanel;
	private Controller controller;
	protected DiagramListener listener;
	private String helptext;
	private Constants constants;
	private boolean enabled;
	
	public DiagramHandler(File diagram)	{
		this(diagram,false);
	}
	
	protected DiagramHandler(File diagram, boolean nolistener) {
		this.isChanged=false;
		this.enabled = true;
		this.constants = new Constants();
		this.drawpanel = new DrawPanel(this);
		this.controller = new Controller(this);		
		this.fileHandler = DiagramFileHandler.createInstance(this, diagram);
		if(!nolistener)
			this.setListener(new DiagramListener(this));
		this.drawpanel.setStartUpHelpLabel(new StartUpHelpLabel(this.drawpanel));
		if(diagram != null)
			this.fileHandler.doOpen();
		if(this.helptext == null)
			this.helptext = Constants.defaultHelpText;
	}
	
	public void setEnabled(boolean en) {
		if(!en && enabled)
		{
			this.drawpanel.removeMouseListener(this.listener);
			this.drawpanel.removeMouseMotionListener(this.listener);
			enabled = false;
		}
		else if(en && !enabled) {
	    	this.drawpanel.addMouseListener(this.listener);
	    	this.drawpanel.addMouseMotionListener(this.listener);
	    	enabled = true;
		}
	}
	
	protected void setListener(DiagramListener listener) {
		this.listener = listener;
    	this.drawpanel.addMouseListener(this.listener);
    	this.drawpanel.addMouseMotionListener(this.listener);
	}
	
	public float getZoom() {
		return constants.getZoom();
	}
	
	public int getFontsize() {
		return this.constants.getFontsize();
	}
	
	public void setFontsize(Integer fontsize) {
		this.constants.setFontsize(fontsize);
	}
	
	public Dimension getTextSize(Graphics2D g2, String s) {
		return this.constants.getTextSize(g2, s);
	}
	
	public Font getFont() {
		return this.constants.getFont();
	}
	
	public Font getFontItalic() {
		return this.constants.getFontItalic();
	}
	
	public Font getFontBold() {
		return this.constants.getFontBold();
	}
		
	public FontRenderContext getFRC(Graphics2D g2) {
		return this.constants.getFRC(g2);
	}
	
	public int getDistLineToText() {
		return this.constants.getDistLineToText();
	}
	
	public int getDistTextToText() {
		return this.constants.getDistTextToText();
	}
	
	public int getDistTextToLine() {
		return this.constants.getDistTextToLine();
	}
	
	public void writeText(Graphics2D g2, String s, int x, int y, boolean center) {
		if(center)
			this.writeText(g2, s, x, y, Constants.CENTER, Constants.BOTTOM);
		else
			this.writeText(g2, s, x, y, Constants.LEFT, Constants.BOTTOM);
	}
	
	public void writeText(Graphics2D g2, String s, int x, int y, int align, int valign) {
		this.constants.write(g2, s, x, y, align, valign);
	}
	
	public int getPixelWidth(Graphics2D g2, String s) {
		return this.constants.getPixelWidth(g2, s);
	}
	
	public DiagramListener getListener() {
		return this.listener;
	}
	
	public void setChanged(boolean changed) {
		if(this.isChanged != changed) {
			this.isChanged = changed;
			Umlet.getInstance().getGUI().setDiagramChanged(this,changed);
		}
	}
	
	public DrawPanel getDrawPanel() {
		return this.drawpanel;
	}
	
	public Vector<Entity> getSelectedEntities() {
		return this.drawpanel.getSelector().getSelectedEntities();
	}
	
	public DiagramFileHandler getFileHandler() {
		return this.fileHandler;
	}
	
	public Controller getController() {
		return this.controller;
	}
	
	public void doSave() {
		if(this.fileHandler.doSave()) {
			this.setChanged(false);
			this.reloadPalettes();
		}
	}
	
	public void doSaveAs(String extension) {
		if(this.fileHandler.doSaveAs(extension)) {
			this.setChanged(false);
			this.reloadPalettes();
		}
	}
	
	public void doPrint() 
	{
	    PrinterJob printJob = PrinterJob.getPrinterJob();
	    printJob.setPrintable(this.getDrawPanel());
	    if (printJob.printDialog())
	      try {
	        printJob.print();
	      } catch(PrinterException pe) {
	        Umlet.displayError("An error occured during printing.");
	      }
	}
	
	//reloads the diagram from file + updates gui
	public void reload() {
		this.drawpanel.removeAll();
		this.fileHandler.doOpen();
	}
	
	//reloads palettes if the palette has been changed.
	private void reloadPalettes() {
		for(DiagramHandler d : Umlet.getInstance().getPalettes().values())
		{
			if(d.getFileHandler().equals(this.getFileHandler()) && !d.equals(this))
				d.reload();
		}
	}
	
	public void doClose() {
		if(this.askSaveIfDirty()) {
			Umlet.getInstance().getDiagrams().remove(this);
			Umlet.getInstance().getGUI().close(this);
			this.drawpanel.getSelector().deselectAll();
			
			//update property panel to now selected diagram (or to empty if no diagram exists)
			DiagramHandler newhandler = Umlet.getInstance().getDiagramHandler(); // 
			if(newhandler != null)
				newhandler.getDrawPanel().getSelector().updateSelectorInformation();
			else
				Umlet.getInstance().setPropertyPanelToEntity(null);
		}
	}
	
	public String getName() {
		String name = this.fileHandler.getFileName();
		return name.substring(0, name.lastIndexOf("."));
	}
	
	public String getFullPathName() {
		return this.fileHandler.getFullPathName();
	}
	
	//function to be able to controll the entitylistener + diagramlistener from the handler
	public EntityListener getEntityListener(Entity e) {
		if(e instanceof Relation)
			return RelationListener.getInstance(this);
		return EntityListener.getInstance(this);
	}
	
	public boolean askSaveIfDirty() {
      if (this.isChanged) {
          int ch=JOptionPane.showOptionDialog(Umlet.getInstance().getGUI(),"Save changes?","UMLet - " + this.getName(),JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,null, null, null);
          if (ch==JOptionPane.YES_OPTION) {
              this.doSave();
              return true;
          } else if (ch==JOptionPane.NO_OPTION) {
              return true;
          }
          return false;
      }
      return true;
	}
	
	public void updateHelpText(String helptext) {
		this.helptext = helptext;
		if(!this.helptext.equals(Umlet.getInstance().getGUI().getPropertyPanelText()))
			Umlet.getInstance().getGUI().setPropertyPanelText(this.helptext);
	}
	
	public String getHelpText() {
		return this.helptext;
	}
	
	public boolean isChanged() {
		return this.isChanged;
	}
}
