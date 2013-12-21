package com.baselet.diagram.commandnew;

import com.baselet.element.GridElement;

public class AddGridElementCommand extends Command {

	private CanAddAndRemoveGridElement target;
	private GridElement[] elements;

	public AddGridElementCommand(CanAddAndRemoveGridElement target, GridElement ... elements) {
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
