package com.baselet.diagram;

import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.diagram.command.Command;

public class Controller {
	private Vector<Command> commands;
	private DiagramHandler handler;
	private int _cursor;

	public Controller(DiagramHandler handler) {
		commands = new Vector<Command>();
		_cursor = -1;
		this.handler = handler;
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

		if (newCommand.isChangingDiagram()) {
			this.handler.setChanged(true);
		}

		Main.getInstance().getGUI().updateGrayedOutMenuItems(this.handler);
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
		return commands.isEmpty();
	}

	public boolean isUndoable() {
		return _cursor >= 0;
	}

	public boolean isRedoable() {
		return _cursor < commands.size() - 1;
	}

	public void clear() {
		commands = new Vector<Command>();
		_cursor = -1;
	}

}
