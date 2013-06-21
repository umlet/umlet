package com.baselet.diagram.commandnew;

import com.baselet.element.GridElement;
import com.baselet.element.Selector;

public class AddGridElementCommand extends Command {

	private CanAddAndRemoveGridElement target;
	private Selector selector;
	private GridElement[] elements;

	public AddGridElementCommand(CanAddAndRemoveGridElement target, Selector selector, GridElement ... elements) {
		this.target = target;
		this.selector = selector;
		this.elements = elements;
	}

	@Override
	public void execute() {
		target.addGridElements(elements);
		selector.select(elements);
	}

	@Override
	public void undo() {
		target.removeGridElements(elements);
	}
	
}
