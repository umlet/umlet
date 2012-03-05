package com.baselet.diagram.command;

import java.awt.Point;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.element.GridElement;
import com.baselet.element.Group;

public class AddElement extends Command {

	private final static Logger log = Logger.getLogger(Utils.getClassName());
	
	// AB: checked false after first execution
	private boolean firstCall = true;
	private Point origin;
	private GridElement _entity;
	private int _x;
	private int _y;
	private boolean _zoom;

	private int getX() {
		// AB: use default grid size since we zoom the whole entity on execution
		if (_zoom) return _x * Constants.DEFAULTGRIDSIZE;// _entity.getHandler().getGridSize();
		else return _x;
	}

	private int getY() {
		// AB: use default grid size since we zoom the whole entity on execution
		if (_zoom) return _y * Constants.DEFAULTGRIDSIZE;// _entity.getHandler().getGridSize();
		else return _y;
	}

	public AddElement(GridElement e, int x, int y) {
		this(e, x, y, true);
	}

	public AddElement(GridElement e, int x, int y, boolean zoom) {
		_entity = e;
		_zoom = zoom;
		if (_zoom) {
			_x = x / e.getHandler().getGridSize();
			_y = y / e.getHandler().getGridSize();
			DiagramHandler.zoomEntity(e.getHandler().getGridSize(), Constants.DEFAULTGRIDSIZE, e);
		}
		else {
			_x = x;
			_y = y;
		}
	}

	private void addentity(GridElement e, DrawPanel panel, int x, int y) {
		e.setHandlerAndInitListeners(panel.getHandler());
		panel.addElement(e);
		if (e instanceof Group) {
			Group g = (Group) e;
			for (GridElement ent : g.getMembers())
				addentity(ent, panel, ent.getLocation().x - g.getLocation().x + x, ent.getLocation().y - g.getLocation().y + y);
			g.removeMemberListeners(); // remove listeners from submembers of this group
			if (_zoom) g.adjustSize(false); // Adjust cause problems if _zoom == false because the wrong zoomlevel would be assumed
		}

		e.setLocation(x, y);
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);

		// AB: set origin for the first execution;
		// NOTE this cannot be done in constructor since we cannot rely that the handler in the constructor is the same as here
		if (origin == null) {
			origin = handler.getDrawPanel().getOriginAtDefaultZoom();
		}

		// AB: calculate offset so that we add it on the same position on redo
		// NOTE the first time the command is executed, the offset is 0
		int offsetX = origin.x - handler.getDrawPanel().getOriginAtDefaultZoom().x;
		int offsetY = origin.y - handler.getDrawPanel().getOriginAtDefaultZoom().y;

		log.debug("Add Entity at " + getX() + "/" + getY());
		addentity(_entity, handler.getDrawPanel(), getX() + offsetX, getY() + offsetY);

		if (_zoom) DiagramHandler.zoomEntity(Constants.DEFAULTGRIDSIZE, handler.getGridSize(), _entity);
		handler.getDrawPanel().getSelector().singleSelect(_entity);

		// AB: do this because updatePanelAndScrollbars messes up frequent calls of AddEntity in a loop
		if (!firstCall) {
			handler.getDrawPanel().updatePanelAndScrollbars();
		}

		firstCall = false;
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		if (_zoom) DiagramHandler.zoomEntity(handler.getGridSize(), Constants.DEFAULTGRIDSIZE, _entity);
		handler.getDrawPanel().removeElement(_entity);
		(new RemoveElement(_entity, false)).execute(handler); // zoom must be false otherwise groups don't work correctly
		handler.getDrawPanel().repaint();
		handler.getDrawPanel().updatePanelAndScrollbars();
	}
}
