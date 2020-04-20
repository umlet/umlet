package com.baselet.command;

public abstract class Command {

	public abstract void execute();

	public abstract void undo();

	public boolean isMergeableTo(@SuppressWarnings("unused") Command c) {
		return false;
	}

	public Command mergeTo(@SuppressWarnings("unused") Command c) {
		return null;
	}

	public boolean isChangingDiagram() {
		return true;
	}
}
