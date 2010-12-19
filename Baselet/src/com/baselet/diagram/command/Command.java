package com.baselet.diagram.command;

import com.baselet.diagram.DiagramHandler;

public abstract class Command {

	public void execute(DiagramHandler handler) {

	}

	public void undo(DiagramHandler handler) {

	}

	public boolean isMergeableTo(Command c) {
		return false;
	}

	public Command mergeTo(Command c) {
		return null;
	}
}
