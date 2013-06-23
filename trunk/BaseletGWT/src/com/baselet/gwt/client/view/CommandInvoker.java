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
import com.baselet.element.Selector;
import com.baselet.gwt.client.BrowserStorage;

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
	void removeSelectedElements() {
		List<GridElement> elements = canvas.getSelector().getSelectedElements();
		GridElement[] elementsArray = elements.toArray(new GridElement[elements.size()]);
		this.executeCommand(new RemoveGridElementCommand(canvas, canvas.getSelector(), elementsArray));
	}
	
	
	
	
	
	//TODO implement copy & paste as commands

	void copySelectedElements() {
		BrowserStorage.setClipboard(copyElementsInList(canvas.getSelector().getSelectedElements())); // must be copied here to ensure location etc. will not be changed
	}

	void cutSelectedElements() {
		copySelectedElements();
		removeSelectedElements();
	}
	
	void pasteElements() {
		addElements(copyElementsInList(BrowserStorage.getClipboard(canvas.getSelector()))); // copy here to make sure it can be pasted multiple times
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
