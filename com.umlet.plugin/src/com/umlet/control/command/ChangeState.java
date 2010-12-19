// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.command;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ChangeState extends Command {
  private Entity _entity;
  public Entity getEntity() {
    return _entity;
  }
  private String _newState;
  private String _oldState;
  public String getNewState() { return _newState; }
  public String getOldState() { return _oldState; }


  public ChangeState(Entity e, String oldState, String newState) {
    _entity=e;
    _newState=newState;
    _oldState=oldState;
  }

  public void execute(DiagramHandler handler) {
	super.execute(handler);
    _entity.setState(_newState);
    _entity.repaint();
  }
  public void undo(DiagramHandler handler) {
	super.undo(handler);
    _entity.setState(_oldState);
    _entity.repaint();
  }
  
  public boolean isMergeableTo(Command c) { 
  	if (!(c instanceof ChangeState)) return false;
  	ChangeState cs=(ChangeState)c;
  	if (this.getEntity()!=cs.getEntity()) return false;
  	return true;
  }
  public Command mergeTo(Command c) { 
    ChangeState tmp=(ChangeState)c;
    ChangeState ret=new ChangeState(this.getEntity(), tmp.getOldState(), this.getNewState());
    return ret;
  }    
}