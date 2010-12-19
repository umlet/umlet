// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.command;

import com.umlet.control.diagram.DiagramHandler;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public abstract class Command {

  public void execute(DiagramHandler handler) {

  }
  public void undo(DiagramHandler handler) {

  }
  public boolean isMergeableTo(Command c) { return false; }
  public Command mergeTo(Command c) { return null; }
}