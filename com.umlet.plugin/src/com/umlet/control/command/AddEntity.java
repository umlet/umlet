// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.command;

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

public class AddEntity extends Command {
  Entity _entity;
  int _x;
  int _y;

  public AddEntity(Entity e, int x, int y) {
    _entity=e;
    _x=x;
    _y=y;
  }

  private void addentity(Entity e, DrawPanel panel, int x, int y) 
  {
	  e.assignToDiagram(panel.getHandler());
  	  panel.add(e);
	  if(e instanceof Group)
	  {
		  Group g = (Group)e;
		  for(Entity ent : g.getMembers())
			  this.addentity(ent, panel, ent.getX() - g.getX() + x, ent.getY() - g.getY() + y);
		  g.removeMemberListeners(); //remove listeners from submemmbers of this group
		  g.adjustSize();	  
	  }
	
  	  e.setLocation(x,y);
	  if(e instanceof Relation) ((Relation)e).reLocate();	//LME: workaround for misplaced relations
  }
  
  @Override
  public void execute(DiagramHandler handler) {
	super.execute(handler);
	this.addentity(this._entity, handler.getDrawPanel(), _x, _y);
	handler.getDrawPanel().repaint();
    handler.getDrawPanel().getSelector().singleSelect(_entity);
  }

  @Override
  public void undo(DiagramHandler handler) {
  	super.undo(handler);
    handler.getDrawPanel().remove(_entity);
    handler.getDrawPanel().repaint();
  }
}