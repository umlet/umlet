package com.baselet.diagram.command;

import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.Selector;

public abstract class Command {

	public void execute(DiagramHandler handler) {

	}

	public void undo(DiagramHandler handler) {
		if (handler != null) {
			Selector selector = handler.getDrawPanel().getSelector();
			if (selector != null) {
				selector.deselectAll();
			}
		}
	}

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
