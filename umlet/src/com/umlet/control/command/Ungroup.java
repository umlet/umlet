// The UMLet source code is distributed under the terms of the GPL; see license.txt
// Class by A.Mueller Oct.05

package com.umlet.control.command;

import java.util.Vector;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.Selector;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Group;

public class Ungroup extends Command {
	Group _group;
	Vector<Entity> members;

	public Ungroup(Group group) {
		_group = group;
		members = new Vector<Entity>();
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
		_group.assignToDiagram(handler);
		_group.group(this.members);
	}
}
