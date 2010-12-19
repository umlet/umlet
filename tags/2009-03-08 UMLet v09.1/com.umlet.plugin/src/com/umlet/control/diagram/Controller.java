// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.diagram;

import java.util.*;
/*import Move;
import Frame;*/

import com.umlet.control.command.Command;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Controller {
  private Vector<Command> commands; // auf private zurücksetzen
  private DiagramHandler handler;
  private int _cursor;

  public Controller(DiagramHandler handler) {
	  commands=new Vector<Command>();
	  _cursor=-1;
	  this.handler = handler;
  }
  
  public void executeCommand(Command newCommand) {
    // Remove future commands
    for (int i=commands.size()-1;i>_cursor;i--) {
      commands.removeElementAt(i);
    }
    commands.add(newCommand);
    newCommand.execute(this.handler);

    if (commands.size()>=2) {
      Command c_n, c_nMinus1;
      c_n=commands.elementAt(commands.size()-1);
      c_nMinus1=commands.elementAt(commands.size()-2);

      if (c_n.isMergeableTo(c_nMinus1)) {
        commands.removeElement(c_n);
        commands.removeElement(c_nMinus1);
        Command c=c_n.mergeTo(c_nMinus1);
	    commands.add(c);
      }
    }
    _cursor=commands.size()-1;
    this.handler.setChanged(true);
  }

  public void undo() {
    if (_cursor>=0) {
      Command c=commands.elementAt(_cursor);
      c.undo(this.handler);
      _cursor--;
      this.handler.setChanged(true);
    }
  }

 public void redo() {
    if (_cursor<commands.size()-1) {
      Command c=commands.elementAt(_cursor+1);
      c.execute(this.handler);
      _cursor++;
    }
  }

}