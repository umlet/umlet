package com.baselet.gui.command;

import java.util.List;

import com.baselet.diagram.DiagramHandler;
import com.baselet.element.ElementFactorySwing;
import com.baselet.element.interfaces.GridElement;

public class Copy extends Command {

	@Override
	public void execute(DiagramHandler handler) {
		// dont execute super.execute() because no change of diagram is required.
		List<GridElement> entities = ElementFactorySwing.createCopy(handler.getDrawPanel().getSelector().getSelectedElements());

		// if no element is selected, the whole diagram is copied into the clipboard
		if (entities.isEmpty()) {
			entities = ElementFactorySwing.createCopy(handler.getDrawPanel().getGridElements());
		}

		ClipBoard.getInstance().copyAndZoomToDefaultLevel(entities, handler);
	}

	@Override
	public boolean isChangingDiagram() {
		return false;
	}

}
