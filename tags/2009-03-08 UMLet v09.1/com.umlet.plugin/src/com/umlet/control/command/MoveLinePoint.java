// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.command;

import java.awt.*;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Relation;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class MoveLinePoint extends Command {
  private Relation _relation;
  private int _linePointId, _diffx, _diffy;
  public int getLinePointId() { return _linePointId; }
  public Relation getRelation() { return _relation; }
  public int getDiffX() { return _diffx; }
  public int getDiffY() { return _diffy; }

  public MoveLinePoint(Relation rel, int i, int diffx, int diffy) {
    _relation=rel;
    _linePointId=i;
    _diffx=diffx;
    _diffy=diffy;
  }


  @Override
  public void execute(DiagramHandler handler) {
	super.execute(handler);
    Point p=(Point)_relation.getLinePoints().elementAt(_linePointId);
    p.x=p.x+_diffx;
    p.y=p.y+_diffy;
    _relation.repaint();
  }
  
  @Override
  public void undo(DiagramHandler handler) {
	super.undo(handler);
    Point p=(Point)_relation.getLinePoints().elementAt(_linePointId);
    p.x=p.x-_diffx;
    p.y=p.y-_diffy;
    _relation.repaint();
  }
  
  @Override
  public boolean isMergeableTo(Command c) {
    if (!(c instanceof MoveLinePoint)) return false;
  	MoveLinePoint mlp=(MoveLinePoint)c;
  	if (this.getRelation()!=mlp.getRelation()) return false;
  	if (this.getLinePointId()!=mlp.getLinePointId()) return false;
  	return true;  
  	}
  
  @Override
  public Command mergeTo(Command c) { 
    MoveLinePoint tmp=(MoveLinePoint)c;
    MoveLinePoint ret=new MoveLinePoint(this.getRelation(), this.getLinePointId(), this.getDiffX()+tmp.getDiffX(), this.getDiffY()+tmp.getDiffY());
    return ret;
    }
}