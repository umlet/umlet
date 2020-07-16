package com.baselet.gwt.client.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.baselet.command.*;
import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.constants.SharedConstants;
import com.baselet.element.GridElementUtils;
import com.baselet.element.Selector;
import com.baselet.element.interfaces.Diagram;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.element.WebStorage;
import com.baselet.gwt.client.element.ElementFactoryGwt;
import com.baselet.gwt.client.view.commands.AddGridElementCommandNoUpdate;
import com.baselet.gwt.client.view.commands.RemoveGridElementCommandNoUpdate;

public class CommandInvoker extends Controller {

	private static final CommandInvoker instance = new CommandInvoker();

	public static CommandInvoker getInstance() {
		return instance;
	}

	private CommandInvoker() {
		super();
	}

	void addElements(CommandTarget target, List<GridElement> elements) {
		executeCommand(new AddGridElementCommand(target, elements));
	}

	//adds elements but does not notify vs code that anything is changed
	void addElementsDontNotifyUpdate(CommandTarget target, List<GridElement> elements) {
		executeCommand(new AddGridElementCommandDontNotifyUpdate(target, elements));
	}

	private class AddGridElementCommandDontNotifyUpdate extends AddGridElementCommand{

		public AddGridElementCommandDontNotifyUpdate(CommandTarget target, List<GridElement> elements) {
			super(target, elements);
		}

		@Override
		public void execute() {
			if(target instanceof DrawPanelDiagram)
				((DrawPanelDiagram)target).addGridElementsDontNotifyUpdate(elements);
			else
				super.execute();
		}

		@Override
		public void undo() {
			if(target instanceof DrawPanelDiagram)
				((DrawPanelDiagram)target).removeGridElementsDontNotifyUpdate(elements);
			else
				super.undo();
		}
	}

	void removeElements(CommandTarget target, List<GridElement> elements) {
		executeCommand(new RemoveGridElementCommand(target, elements));
	}

	/*
	 used to add an element without triggering VSCode being notified about the diagram change
	 used to add preview Elements
	 */
	void addElementsNoUpdate(CommandTarget target, List<GridElement> elements) {
		executeCommand(new AddGridElementCommandNoUpdate(target, elements));
	}

	/*
	 used to remove an element without triggering VSCode being notified about the diagram change
	 used to remove preview Elements
	 */
	void removeElementsNoUpdate(CommandTarget target, List<GridElement> elements) {
		executeCommand(new RemoveGridElementCommandNoUpdate(target, elements));
	}

	void removeSelectedElements(CommandTarget target) {
		removeElements(target, target.getSelector().getSelectedElements());
	}


	public void copySelectedElements(CommandTarget target) {
		WebStorage.setClipboard(copyElementsInList(target.getSelector().getSelectedElements(), target.getDiagram())); // must be copied here to ensure location etc. will not be changed
	}

	public void cutSelectedElements(CommandTarget target) {
		copySelectedElements(target);
		removeSelectedElements(target);
	}

	public void pasteElements() {
		WebStorage.getClipboardAsync();
	}

	public void executePaste(CommandTarget target, String content, Point pasteTargetPosition) {
		List<GridElement> copyOfElements = copyElementsInList(DiagramXmlParser.xmlToGridElements(content), target.getDiagram());
		Selector.replaceGroupsWithNewGroups(copyOfElements, target.getSelector());
		//if there is a context menu currently opened, place it at the cursor position, otherwise at the top left
		realignElementsToVisibleRect(target, copyOfElements);
		DrawPanel targetDrawPanel = (DrawPanel)target;
		if (pasteTargetPosition != null) {
			DrawPanel.snapElementsToPointPosition(copyOfElements, targetDrawPanel, pasteTargetPosition);
		} else {
			DrawPanel.snapElementsToVisibleTopLeft(copyOfElements, targetDrawPanel);
		}

		addElements(target, copyOfElements); // copy here to make sure it can be pasted multiple times
	}

	private List<GridElement> copyElementsInList(Collection<GridElement> sourceElements, Diagram targetDiagram) {
		List<GridElement> targetElements = new ArrayList<GridElement>();
		for (GridElement ge : sourceElements) {
			GridElement e = ElementFactoryGwt.create(ge, targetDiagram);
			targetElements.add(e);
		}
		return targetElements;
	}

	void realignElementsToVisibleRect(CommandTarget target, List<GridElement> gridElements) {
		Rectangle rect = GridElementUtils.getGridElementsRectangle(gridElements);
		Rectangle visible = target.getVisibleBounds();
		for (GridElement ge : gridElements) {
			ge.getRectangle().move(visible.getX() - rect.getX() + SharedConstants.DEFAULT_GRID_SIZE, visible.getY() - rect.getY() + SharedConstants.DEFAULT_GRID_SIZE);
		}
	}



	public void updateSelectedElementsProperty(CommandTarget target, String key, Object value) {
		for (GridElement e : target.getSelector().getSelectedElements()) {
			e.setProperty(key, value);
		}
		//Notify updates to vscode
		if (target instanceof DrawPanelDiagram)
			((DrawPanelDiagram)target).handleFileUpdate();
		target.updatePropertiesPanelWithSelectedElement();
	}
}
