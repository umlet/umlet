package com.umlet.gui;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import com.baselet.control.Main;
import com.baselet.diagram.command.CustomCodePanelChanged;
import com.baselet.element.GridElement;

public class CustomCodePanelListener implements UndoableEditListener {
	
	@Override
	public void undoableEditHappened(UndoableEditEvent e) {	
//		CustomElementHandler customHandler = Main.getInstance().getGUI().getCurrentCustomHandler();
		GridElement gridElement = Main.getInstance().getEditedGridElement();		
		
		if (gridElement != null) {
			//only create command if changes were made
			gridElement.getHandler().getController().executeCommand(new CustomCodePanelChanged(e.getEdit()));
		}		
	}
}
