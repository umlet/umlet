// Class by A.Mueller Oct.05

package com.baselet.diagram.command;

import java.util.Vector;

import com.baselet.diagram.DiagramHandler;
import com.baselet.element.GridElement;
import com.baselet.element.Group;


public class CreateGroup extends Command {
	private Group _group;
	private Vector<GridElement> _entities;

	public CreateGroup() {
		_group = new Group();
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		if (this._entities == null) {
			this._entities = new Vector<GridElement>();
			this._entities.addAll(handler.getDrawPanel().getSelector().getSelectedEntities());
		}
		_group.setHandlerAndInitListeners(handler);
		_group.group(this._entities);
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		_group.ungroup();
	}
}
