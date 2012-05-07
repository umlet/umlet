package com.baselet.diagram.command;

import java.util.Vector;

import com.baselet.diagram.DiagramHandler;


public class Macro extends Command {
	private Vector<Command> _commands;

	public Vector<Command> getCommands() {
		return _commands;
	}

	public Macro(Vector<Command> v) {
		_commands = v;
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		for (int i = 0; i < _commands.size(); i++) {
			Command c = _commands.elementAt(i);
			c.execute(handler);
		}
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		for (int i = 0; i < _commands.size(); i++) {
			Command c = _commands.elementAt(i);
			c.undo(handler);
		}
	}

	@Override
	public boolean isMergeableTo(Command c) {
		if (!(c instanceof Macro)) return false;
		Macro m = (Macro) c;
		Vector<Command> v = m.getCommands();
		if (this.getCommands().size() != v.size()) return false;
		for (int i = 0; i < this.getCommands().size(); i++) {
			Command c1 = this.getCommands().elementAt(i);
			Command c2 = v.elementAt(i);
			if (!(c1.isMergeableTo(c2))) return false;
		}
		return true;
	}

	@Override
	public Command mergeTo(Command c) {
		Macro m = (Macro) c;
		Vector<Command> v = m.getCommands();

		Vector<Command> vectorOfCommands = new Vector<Command>();
		Command ret = new Macro(vectorOfCommands);

		for (int i = 0; i < this.getCommands().size(); i++) {
			Command c1 = this.getCommands().elementAt(i);
			Command c2 = v.elementAt(i);
			Command c3 = c1.mergeTo(c2);
			vectorOfCommands.add(c3);
		}
		return ret;
	}
}
