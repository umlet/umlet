package com.baselet.gwt.client.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.baselet.control.NewGridElementConstants;
import com.baselet.diagram.commandnew.AddGridElementCommand;
import com.baselet.diagram.commandnew.Controller;
import com.baselet.diagram.commandnew.RemoveGridElementCommand;
import com.baselet.element.GridElement;

public class CommandInvoker extends Controller {
	
	private DrawFocusPanel canvas;

	public CommandInvoker(DrawFocusPanel canvas) {
		super();
		this.canvas = canvas;
	}
	
	void addElements(GridElement ... elements) {
		this.executeCommand(new AddGridElementCommand(canvas, canvas.getSelector(), elements));
	}

	void addElements(Collection<GridElement> elements) {
		addElements(elements.toArray(new GridElement[elements.size()]));
	}
	
	void removeElements(GridElement ... elements) {
		this.executeCommand(new RemoveGridElementCommand(canvas, canvas.getSelector(), elements));
	}

	void removeElements(Collection<GridElement> elements) {
		removeElements(elements.toArray(new GridElement[elements.size()]));
	}
	
	
	
	
	
	//TODO implement copy & paste as commands

	private List<GridElement> copyTemplates = new ArrayList<GridElement>();
	void copyElements(Collection<GridElement> elements) {
		copyTemplates = copyElementsInList(elements); // must be copied here to ensure location etc. will not be changed
	}
	
	void pasteElements() {
		addElements(copyElementsInList(copyTemplates)); // copy here to make sure it can be pasted multiple times
	}

	private List<GridElement> copyElementsInList(Collection<GridElement> sourceElements) {
		List<GridElement> targetElements = new ArrayList<GridElement>();
		for (GridElement ge : sourceElements) {
			GridElement e = ge.CloneFromMe();
			targetElements.add(e);
		}
		return targetElements;
	}
}
