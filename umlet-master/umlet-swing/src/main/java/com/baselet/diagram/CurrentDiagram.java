package com.baselet.diagram;

import com.baselet.gui.BaseGUI;
import com.baselet.gui.CurrentGui;

public class CurrentDiagram {

	private final static CurrentDiagram instance = new CurrentDiagram();

	public static CurrentDiagram getInstance() {
		return instance;
	}

	private DiagramHandler currentDiagramHandler;

	// sets the current diagram the user works with - that may be a palette too
	public void setCurrentDiagramHandler(DiagramHandler handler) {
		currentDiagramHandler = handler;
		BaseGUI gui = CurrentGui.getInstance().getGui();
		if (gui != null) {
			gui.diagramSelected(handler);
		}
	}

	public void setCurrentDiagramHandlerAndZoom(DiagramHandler handler) {
		setCurrentDiagramHandler(handler);
		BaseGUI gui = CurrentGui.getInstance().getGui();
		if (handler != null && gui != null) {
			gui.setValueOfZoomDisplay(handler.getGridSize());
		}
	}

	// returns the current diagramhandler the user works with - may be a diagramhandler of a palette too
	public DiagramHandler getDiagramHandler() {
		return currentDiagramHandler;
	}

}
