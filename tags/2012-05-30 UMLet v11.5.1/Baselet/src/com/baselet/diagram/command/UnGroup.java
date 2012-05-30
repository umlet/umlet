// Class by A.Mueller Oct.05

package com.baselet.diagram.command;

import java.util.Vector;

import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.Selector;
import com.baselet.element.GridElement;
import com.baselet.element.Group;


public class UnGroup extends Command {
	Group _group;
	Vector<GridElement> members;

	public UnGroup(Group group) {
		_group = group;
		members = new Vector<GridElement>();
		members.addAll(_group.getMembers());
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		_group.ungroup();
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		Selector s = handler.getDrawPanel().getSelector();
		if (s == null) return;
		s.deselectAll();
		_group.setHandlerAndInitListeners(handler);
		_group.group(this.members);
	}
}
