package com.baselet.diagram.commandnew;

import com.baselet.element.GridElement;
import com.baselet.element.Selector;

public class RemoveGridElementCommand extends Command {

	private CanAddAndRemoveGridElement target;
	private Selector selector;
	private GridElement[] elements;

	public RemoveGridElementCommand(CanAddAndRemoveGridElement target, Selector selector, GridElement ... elements) {
		this.target = target;
		this.selector = selector;
		this.elements = elements;
	}

	@Override
	public void execute() {
		target.removeGridElements(elements);
		selector.deselect(elements);
	}

	@Override
	public void undo() {
		target.addGridElements(elements);
	}
	
}
