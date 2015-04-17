package com.baselet.gui.command;

import java.util.Vector;

import com.baselet.diagram.DiagramHandler;
import com.baselet.element.ElementFactorySwing;
import com.baselet.element.interfaces.GridElement;

public class Copy extends Command {

	private Vector<GridElement> entities;

	public Copy() {

	}

	@Override
	public void execute(DiagramHandler handler) {

		// dont execute super.execute() because no change of diagram is required.
		if (entities == null) {
			entities = new Vector<GridElement>();
			for (GridElement e : handler.getDrawPanel().getSelector().getSelectedElements()) {
				entities.add(ElementFactorySwing.createCopy(e));
			}
		}

		// if (entities.isEmpty()) return; UNCOMMENTED TO ALLOW COPY FULL DIAGRAM TO CLIPBOARD WITHOUT SELECTING ANYTHING
		ClipBoard.getInstance().copyAndZoomToDefaultLevel(entities, handler);
	}

	@Override
	public boolean isChangingDiagram() {
		return false;
	}

}
