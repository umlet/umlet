package com.baselet.diagram.command;

import java.util.Vector;

import com.baselet.control.ClipBoard;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.GridElement;
import com.umlet.element.experimental.ElementFactory;


public class Copy extends Command {

	private Vector<GridElement> entities;

	public Copy() {

	}

	@Override
	public void execute(DiagramHandler handler) {

		// dont execute super.execute() because no change of diagram is required.
		if (this.entities == null) {
			this.entities = new Vector<GridElement>();
			for (GridElement e : handler.getDrawPanel().getSelector().getSelectedElements())
				this.entities.add(ElementFactory.createCopy(e));
		}

		// if (entities.isEmpty()) return; UNCOMMENTED TO ALLOW COPY FULL DIAGRAM TO CLIPBOARD WITHOUT SELECTING ANYTHING
		ClipBoard.getInstance().copy(this.entities, handler);
	}


	@Override
	public boolean isChangingDiagram() {
		return false;
	}
	
}
