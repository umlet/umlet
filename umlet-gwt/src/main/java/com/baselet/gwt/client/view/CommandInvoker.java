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
import com.baselet.gwt.client.clipboard.VsCodeClipboardManager;
import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.element.WebStorage;
import com.baselet.gwt.client.element.ElementFactoryGwt;
import com.baselet.gwt.client.view.commands.AddGridElementCommandNoUpdate;
import com.baselet.gwt.client.view.commands.RemoveGridElementCommandNoUpdate;
import com.google.gwt.core.client.GWT;

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
		if (VersionChecker.GetVersion() == VersionChecker.Version.VSCODE)
		{
			if (target instanceof DrawPanel)
			{
				VsCodeClipboardManager.copyDiagramToClipboard((DrawPanel)target);
			}
		} else {
			WebStorage.setClipboard(copyElementsInList(target.getSelector().getSelectedElements(), target.getDiagram())); // must be copied here to ensure location etc. will not be changed
		}

	}

	public void cutSelectedElements(CommandTarget target) {
		copySelectedElements(target);
		removeSelectedElements(target);
	}

	void pasteElements(CommandTarget target) {
		if (VersionChecker.GetVersion() == VersionChecker.Version.VSCODE)
		{
			//save context menu position so vscode knows where to put the pasted element when returning
			if (target instanceof DrawPanel)
				VsCodeClipboardManager.setNextPastePosition(((DrawPanel) target).getLastContextMenuPosition());
			//request of paste
			VsCodeClipboardManager.requestVsCodePaste();
		} else
		{
			List<GridElement> copyOfElements = copyElementsInList(WebStorage.getClipboard(), target.getDiagram());
			executePaste(target, copyOfElements);
		}

	}

	/*
	 targetPosition is relative to the CommandTarget target, so (0,0) is the top left of target
	 */
	private void executePaste(CommandTarget target, List<GridElement> copyOfElements) {
		Selector.replaceGroupsWithNewGroups(copyOfElements, target.getSelector());
		//if there is a context menu currently opened, place it at the cursor position, otherwise at the top left
		realignElementsToVisibleRect(target, copyOfElements);
		if (target instanceof  DrawPanel)
		{
			DrawPanel targetDrawPanel = (DrawPanel)target;
			Point targetPosition = null;
			if (VersionChecker.GetVersion() == VersionChecker.Version.VSCODE){
				targetPosition = VsCodeClipboardManager.popNextPastePosition();
			} else {
				targetPosition = targetDrawPanel.getLastContextMenuPosition();
			}
			if (targetPosition != null)
			{
				DrawPanel.snapElementsToPointPosition(copyOfElements, targetDrawPanel, targetPosition);
			} else {

				DrawPanel.snapElementsToVisibleTopLeft(copyOfElements, targetDrawPanel);
			}
		}


		addElements(target, copyOfElements); // copy here to make sure it can be pasted multiple times
	}

	//used when paste is called via vs code command
	public void pasteElementsVsCode(CommandTarget target, String content) {
		List<GridElement> copyOfElements = copyElementsInList(DiagramXmlParser.xmlToGridElements(content), target.getDiagram());
		executePaste(target, copyOfElements);
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
		target.updatePropertiesPanelWithSelectedElement();
	}
}
