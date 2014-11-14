package com.baselet.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Controller {
	private List<Command> commands = new ArrayList<Command>();
	private int _cursor;

	public Controller() {
		_cursor = -1;
	}

	protected void executeCommand(Command newCommand) {
		// Remove future commands
		for (int i = commands.size() - 1; i > _cursor; i--) {
			commands.remove(i);
		}
		commands.add(newCommand);
		newCommand.execute();

		if (commands.size() >= 2) {
			Command c_n, c_nMinus1;
			c_n = commands.get(commands.size() - 1);
			c_nMinus1 = commands.get(commands.size() - 2);

			if (c_n.isMergeableTo(c_nMinus1)) {
				commands.remove(c_n);
				commands.remove(c_nMinus1);
				Command c = c_n.mergeTo(c_nMinus1);
				commands.add(c);
			}
		}
		_cursor = commands.size() - 1;
	}

	public void undo() {
		if (isUndoable()) {
			Command c = commands.get(_cursor);
			c.undo();
			_cursor--;
		}
	}

	public void redo() {
		if (isRedoable()) {
			Command c = commands.get(_cursor + 1);
			c.execute();
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

	public long getCommandCount() {
		return commands.size();
	}

	public void clear() {
		commands = new Vector<Command>();
		_cursor = -1;
	}

}
