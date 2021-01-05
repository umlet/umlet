package com.baselet.command;

import java.util.List;

import com.baselet.element.interfaces.GridElement;

public class AddGridElementCommand extends Command {

	protected CommandTarget target;
	protected List<GridElement> elements;
	protected int oldZoomLevel;

	public AddGridElementCommand(CommandTarget target, List<GridElement> elements, int oldZoomLevel) {
		this.target = target;
		this.elements = elements;
		this.oldZoomLevel = oldZoomLevel;
	}

	@Override
	public void execute() {
		target.addGridElements(elements, oldZoomLevel);
	}

	@Override
	public void undo() {
		target.removeGridElements(elements);
	}

}
