package com.baselet.gui.command;

import com.baselet.diagram.DiagramHandler;
import com.baselet.element.interfaces.GridElement;

public class MoveEnd extends Command {

	private GridElement e;

	public MoveEnd(GridElement e) {
		this.e = e;
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		e.dragEnd();
	}

	@Override
	public boolean isMergeableTo(Command c) {
		if (c instanceof Macro) {
			return true;
		}
		return false;
	}

	@Override
	public Command mergeTo(Command c) {
		((Macro) c).getCommands().add(this);
		return c;
	}
}
