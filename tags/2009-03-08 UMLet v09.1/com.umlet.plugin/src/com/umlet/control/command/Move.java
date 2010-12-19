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

public class Move extends Command {
  //private Vector _entitiesToBeMoved;
  private Entity _entity;
  public Entity getEntity() {
    return _entity;
  }
  private int _x, _y;
  public int getX() {
    return _x;
  }
  public int getY() {
    return _y;
  }
  public Move(Entity e, int x, int y) {
    _entity=e;
    _x=x;
    _y=y;
  }

  @Override
  public void execute(DiagramHandler handler) {
	super.execute(handler);
    this.getEntity().changeLocation(_x,_y);
  }
  
  @Override
  public void undo(DiagramHandler handler) {
	super.undo(handler);
    this.getEntity().changeLocation(-_x,-_y);
  }

  @Override
  public boolean isMergeableTo(Command c) {
  	if (!(c instanceof Move)) return false;
  	Move m=(Move)c;
  	return this.getEntity()==m.getEntity();
  }
  
  @Override
  public Command mergeTo(Command c) {
  	//System.out.println(Controller.getInstance().commands.size()+", ");
  	Move m=(Move)c;
  	Move ret=new Move(this.getEntity(), this.getX()+m.getX(), this.getY()+m.getY());
  	return ret;
  }
}