package com.baselet.diagram.commandnew;


public abstract class Command {

	public abstract void execute();

	public abstract void undo();

	public boolean isMergeableTo(Command c) {
		return false;
	}

	public Command mergeTo(Command c) {
		return null;
	}
	public boolean isChangingDiagram() {
		return true;
	}
}
