package com.baselet.diagram.commandnew;

import com.baselet.element.GridElement;

public class AddGridElementCommand extends Command {

	public static interface AddGridElementTarget {
		void addGridElement(GridElement element);

		void removeGridElement(GridElement element);
	}

	private AddGridElementTarget target;
	private GridElement element;

	public AddGridElementCommand(AddGridElementTarget target, GridElement element) {
		this.target = target;
		this.element = element;
	}

	@Override
	public void execute() {
		target.addGridElement(element);
	}

	@Override
	public void undo() {
		target.removeGridElement(element);
	}
	
}
