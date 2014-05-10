package com.baselet.diagram.commandnew;

import java.util.List;

import com.baselet.element.GridElement;

public class AddGridElementCommand extends Command {

	private CanAddAndRemoveGridElement target;
	private List<GridElement> elements;

	public AddGridElementCommand(CanAddAndRemoveGridElement target, List<GridElement> elements) {
		this.target = target;
		this.elements = elements;
	}

	@Override
	public void execute() {
		target.addGridElements(elements);
	}

	@Override
	public void undo() {
		target.removeGridElements(elements);
	}

}
