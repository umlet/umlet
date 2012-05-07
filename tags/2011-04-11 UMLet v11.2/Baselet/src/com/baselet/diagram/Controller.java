package com.baselet.diagram;

import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.diagram.command.Command;
import com.baselet.gui.standalone.StandaloneGUI;


public class Controller {
	private Vector<Command> commands;
	private DiagramHandler handler;
	private int _cursor;
	private long commandCount;

	public Controller(DiagramHandler handler) {
		commands = new Vector<Command>();
		_cursor = -1;
		this.handler = handler;
		this.commandCount = 0;
	}

	public void executeCommand(Command newCommand) {
		// Remove future commands
		for (int i = commands.size() - 1; i > _cursor; i--) {
			commands.removeElementAt(i);
		}
		commands.add(newCommand);
		newCommand.execute(this.handler);

		if (commands.size() >= 2) {
			Command c_n, c_nMinus1;
			c_n = commands.elementAt(commands.size() - 1);
			c_nMinus1 = commands.elementAt(commands.size() - 2);

			if (c_n.isMergeableTo(c_nMinus1)) {
				commands.removeElement(c_n);
				commands.removeElement(c_nMinus1);
				Command c = c_n.mergeTo(c_nMinus1);
				commands.add(c);
			}
		}
		_cursor = commands.size() - 1;
		this.handler.setChanged(true);

		//update undo/redo menu entries
		if (Main.getInstance().getGUI() instanceof StandaloneGUI) ((StandaloneGUI) Main.getInstance().getGUI()).updateGrayedOutMenuItems(this.handler);
		
		commandCount++;
	}

	public void undo() {
		if (isUndoable()) {
			Command c = commands.elementAt(_cursor);
			c.undo(this.handler);
			_cursor--;
			this.handler.setChanged(true);
		}
	}

	public void redo() {
		if (isRedoable()) {
			Command c = commands.elementAt(_cursor + 1);
			c.execute(this.handler);
			_cursor++;
		}
	}

	public boolean isEmpty() {
		if (commands.isEmpty()) return true;
		else return false;
	}

	public boolean isUndoable() {
		if (_cursor >= 0) return true;
		else return false;
	}

	public boolean isRedoable() {
		if (_cursor < commands.size() - 1) return true;
		else return false;
	}

	public long getCommandCount() {
		return this.commandCount;
	}
	
	public void clear()
	{
		commands = new Vector<Command>();
		_cursor = -1;
		this.commandCount = 0;		
	}

}
