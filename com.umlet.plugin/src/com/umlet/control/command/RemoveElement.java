// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.command;

import java.util.*;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.element.base.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class RemoveElement extends Command {
  Vector<Entity> _entities;

  public RemoveElement(Vector<Entity> v) {
    _entities = new Vector<Entity>();
    _entities.addAll(v);
    
	for (int i=0; i<_entities.size(); i++) {
		Entity e=(Entity)_entities.elementAt(i);
	    if(e instanceof Group) {
	    	Group g = (Group)e;
	  	  Vector<Entity> groupElements = g.getMembers();
	  	  _entities.addAll(groupElements);
	    }
	}
  }

  public void execute(DiagramHandler handler) {
	super.execute(handler);
	if(this._entities.size() == 0)
		return;
		
	DrawPanel p = handler.getDrawPanel();
	for (Entity e : this._entities)
		handler.getDrawPanel().remove(e);

    p.repaint();
    p.getSelector().deselectAll();
  }
  
  public void undo(DiagramHandler handler) {
	super.undo(handler);
    for ( Entity e :_entities)
    	handler.getDrawPanel().add(e);

    handler.getDrawPanel().repaint();
  }
}