// The UMLet source code is distributed under the terms of the GPL; see license.txt
//Class by A.Mueller Oct.05

package com.umlet.control.command;

import java.util.Vector;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.*;


public class AddGroup extends Command {
	private Group _group;
	private Vector<Entity> _entities;

  public AddGroup() {
    _group = new Group();
  }

  public void execute(DiagramHandler handler) {
  	super.execute(handler);
  	if(this._entities == null) {
  		this._entities = new Vector<Entity>();
  		this._entities.addAll(handler.getSelectedEntities());
  	}
  	_group.assignToDiagram(handler);
  	_group.group(this._entities);
  }

  public void undo(DiagramHandler handler) {
  	super.undo(handler);
    _group.ungroup();
  }
}