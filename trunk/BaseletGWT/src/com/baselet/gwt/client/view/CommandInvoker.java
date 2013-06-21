package com.baselet.gwt.client.view;

import java.util.Collection;

import com.baselet.diagram.commandnew.AddGridElementCommand;
import com.baselet.diagram.commandnew.Controller;
import com.baselet.diagram.commandnew.RemoveGridElementCommand;
import com.baselet.element.GridElement;

public class CommandInvoker extends Controller {
	
	private DrawPanelCanvas canvas;

	public CommandInvoker(DrawPanelCanvas canvas) {
		super();
		this.canvas = canvas;
	}
	
	void addElements(GridElement ... elements) {
		this.executeCommand(new AddGridElementCommand(canvas, canvas.getSelector(), elements));
	}
	
	void removeElements(GridElement ... elements) {
		this.executeCommand(new RemoveGridElementCommand(canvas, canvas.getSelector(), elements));
	}

	void removeElements(Collection<GridElement> elements) {
		removeElements(elements.toArray(new GridElement[elements.size()]));
	}
}
