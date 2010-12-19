// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.command;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.control.diagram.Selector;
import com.umlet.element.base.Entity;

public class Search extends Command {
    
    static Color _failed = new Color(227,127,127);
    static Color _success = new Color(148,172,251);
    private String regex;
    private Pattern pattern;
    
    public Search(String regex){
        this.regex=".*(" + regex.toLowerCase() + ").*";
    }
    
    public void execute(DiagramHandler handler) {
        super.execute(handler);
	    Selector s = handler.getDrawPanel().getSelector();
        s.deselectAll();
        DrawPanel d = handler.getDrawPanel();
        Vector<Entity> entities = d.getAllEntities();
        pattern = Pattern.compile(regex);
        Matcher m;
        for (int i=0; i<entities.size(); i++){
        	Entity e = entities.get(i);
            m = pattern.matcher(e.getState().toLowerCase());
            if (m.find()) {
            	while(e.isPartOfGroup())
            		e = e.getGroup();
                s.select(e);
            }
        }
        
        if(s.getSelectedEntities().size() == 0)
        	return;
        
        Rectangle panelview = d.getVisibleRect();
        Point p = null;
        for(Entity e : s.getSelectedEntities()) {
        	if(panelview.contains(e.getBounds())) {
        		p = new Point(0,0);
        		break;
        	}
        	else if(p == null)
        		p = new Point(e.getX()-panelview.x-10, e.getY()-panelview.y-10);
        }

        d.incViewPosition(p.x, p.y);
      }
    
      public void undo(DiagramHandler handler) {
        super.undo(handler);
        handler.getDrawPanel().getSelector().deselectAll();
      }
    
    
}
