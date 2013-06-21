package com.baselet.gwt.client.view;

import com.baselet.diagram.commandnew.AddGridElementCommand;
import com.baselet.diagram.commandnew.Controller;
import com.baselet.element.GridElement;

public class CommandInvoker extends Controller {
	
	private DrawPanelCanvas canvas;

	public CommandInvoker(DrawPanelCanvas canvas) {
		super();
		this.canvas = canvas;
	}
	
	void addElement(GridElement element) {
		this.executeCommand(new AddGridElementCommand(canvas, element));
	}
}
