package com.umlet.control.command;

import java.util.Vector;
import java.util.regex.*;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.DiagramHandler;



public class HelpPanelChanged extends Command{
	  private String changed_to;
	  private String changed_from;

	  
	  public HelpPanelChanged(String text) {
		  this.changed_to = text;
	  }

	  private HelpPanelChanged(String changed_from, String changed_to) {
		  this.changed_from = changed_from;
		  this.changed_to = changed_to;
	  }
	  
	  public static Integer getFontsize(String text) {
		  if(text == null)
			  return null;
		  Pattern p = Pattern.compile("(\\s)*fontsize\\=([0-9]+)(\\s)*");
		  Vector<String> txt = Constants.decomposeStrings(text, "\n");
		  for(String t : txt) {
			  Matcher m = p.matcher(t);
			  if(m.matches())
				  return Integer.parseInt(m.group(2));
		  }
		  return null;
	  }
	  
	  public void execute(DiagramHandler handler) {
		super.execute(handler);
		this.changed_from = handler.getHelpText();
	    handler.updateHelpText(changed_to);
	    Integer fontsize = getFontsize(changed_to);
	    if(fontsize != null)
	    	handler.setFontsize(Math.max(Math.min(fontsize, 20),9));
	    else
	    	handler.setFontsize(null);
	    handler.getDrawPanel().repaint();
	  }
	  
	  public void undo(DiagramHandler handler) {
		super.undo(handler);
		handler.updateHelpText(changed_from);
	    handler.setFontsize(getFontsize(changed_from));
	    handler.getDrawPanel().repaint();
	  }
	  
	  public boolean isMergeableTo(Command c) { 
	  	if ((c instanceof HelpPanelChanged)) 
	  		return true;
	  	return false;
	  }
	  
	  public Command mergeTo(Command c) { 
		  HelpPanelChanged tmp=(HelpPanelChanged)c;
		  HelpPanelChanged ret=new HelpPanelChanged(tmp.changed_from,this.changed_to);
	    return ret;
	  } 
}
