package com.baselet.diagram.commandnew;

import com.baselet.element.GridElement;
import com.baselet.element.Selector;

public class RemoveGridElementCommand extends Command {

	private CanAddAndRemoveGridElement target;
	private Selector selector;
	private GridElement element;

	public RemoveGridElementCommand(CanAddAndRemoveGridElement target, Selector selector, GridElement element) {
		this.target = target;
		this.selector = selector;
		this.element = element;
	}

	@Override
	public void execute() {
		target.removeGridElement(element);
		selector.deselectAll();
	}

	@Override
	public void undo() {
		target.addGridElement(element);
	}
	
}
