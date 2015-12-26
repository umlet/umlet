package com.baselet.gui.command;

import java.util.List;
import java.util.Vector;

import com.baselet.diagram.DiagramHandler;

public class Macro extends Command {
	private final List<Command> _commands;

	public List<Command> getCommands() {
		return _commands;
	}

	public Macro(List<Command> v) {
		_commands = v;
	}

	@Override
	public void execute(DiagramHandler handler) {
		for (int i = 0; i < _commands.size(); i++) {
			Command c = _commands.get(i);
			c.execute(handler);
		}
	}

	@Override
	public void redo(DiagramHandler handler) {
		for (int i = 0; i < _commands.size(); i++) {
			Command c = _commands.get(i);
			c.redo(handler);
		}
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		for (int i = 0; i < _commands.size(); i++) {
			Command c = _commands.get(i);
			c.undo(handler);
		}
	}

	@Override
	public boolean isMergeableTo(Command c) {
		if (!(c instanceof Macro)) {
			return false;
		}
		Macro m = (Macro) c;
		List<Command> v = m.getCommands();
		if (getCommands().size() != v.size()) {
			return false;
		}
		for (int i = 0; i < getCommands().size(); i++) {
			Command c1 = getCommands().get(i);
			Command c2 = v.get(i);
			if (!c1.isMergeableTo(c2)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Command mergeTo(Command c) {
		Macro m = (Macro) c;
		List<Command> v = m.getCommands();

		List<Command> vectorOfCommands = new Vector<Command>();
		Command ret = new Macro(vectorOfCommands);

		for (int i = 0; i < getCommands().size(); i++) {
			Command c1 = getCommands().get(i);
			Command c2 = v.get(i);
			Command c3 = c1.mergeTo(c2);
			vectorOfCommands.add(c3);
		}
		return ret;
	}

	@Override
	public boolean isChangingDiagram() {
		for (Command c : _commands) {
			if (c.isChangingDiagram()) {
				return true;
			}
		}
		return false;
	}
}
