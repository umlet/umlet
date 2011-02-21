package com.baselet.gui.listener;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Constants.SystemInfo;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.SelectorFrame;
import com.baselet.diagram.command.AddElement;
import com.baselet.diagram.command.Command;
import com.baselet.diagram.command.Macro;
import com.baselet.diagram.command.Move;
import com.baselet.diagram.command.MoveLinePoint;
import com.baselet.diagram.command.Resize;
import com.baselet.element.GridElement;
import com.baselet.element.StickingPolygon;
import com.umlet.element.Relation;
import com.umlet.element.relation.RelationLinePoint;

public class GridElementListener extends UniversalListener {

	private static HashMap<DiagramHandler, GridElementListener> entitylistener = new HashMap<DiagramHandler, GridElementListener>();
	private final static Logger log = Logger.getLogger(Utils.getClassName());

	public static GridElementListener getInstance(DiagramHandler handler) {
		if (!entitylistener.containsKey(handler)) entitylistener.put(handler, new GridElementListener(handler));
		return entitylistener.get(handler);
	}

	protected boolean IS_DRAGGING = false;
	protected boolean IS_DRAGGING_DIAGRAM = false;
	protected boolean IS_RESIZING = false;
	protected boolean IS_DRAGGED_FROM_PALETTE = false;
	protected boolean IS_FIRST_MOVE = false;
	protected boolean IS_FIRST_DRAGGING_OVER = false;
	private int RESIZE_DIRECTION = 0;
	private Resize FIRST_RESIZE = null;
	private Vector<Command> ALL_MOVE_COMMANDS = null;
	protected boolean DESELECT_MULTISEL = false;
	private boolean LASSO_ACTIVE = false;

	private Rectangle lassoToleranceRectangle;
	private final int lassoTolerance = 2;

	private Point mousePressedPoint;
	private int resizeDirection;

	protected GridElementListener(DiagramHandler handler) {
		super(handler);
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		super.mouseDragged(me);
		log.debug("Entity dragged");

		GridElement e = (GridElement) me.getComponent();

		// Lasso selection is only activated if mouse is moved more than lasso_tolerance to avoid accidential activation instead of selecting the entity
		if (LASSO_ACTIVE && (lassoToleranceRectangle != null) && !lassoToleranceRectangle.contains(new Point(getOffset(me).x, getOffset(me).y))) {
			dragLasso(me, e);
			return;
		}

		if (this.doReturn()) return;

		if (IS_DRAGGING_DIAGRAM) dragDiagram();
		if (IS_DRAGGING) dragEntity(e);
		if (IS_RESIZING) resizeEntity(e, me);
	}

	@Override
	protected Point getOffset(MouseEvent me) {
		return new Point(me.getX() + me.getComponent().getX(), me.getY() + me.getComponent().getY());
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		super.mouseMoved(me);
		log.debug("Entity moved");
		if (this.IS_DRAGGED_FROM_PALETTE) {
			log.debug("mouseMoved with dragged");
			GridElement e = (GridElement) me.getComponent();
			e.setLocation(me.getX() - 100, me.getY() - 20);
		}
		GridElement e = (GridElement) me.getComponent();
		resizeDirection = e.getResizeArea(me.getX(), me.getY());
		resizeDirection = resizeDirection & e.getPossibleResizeDirections(); // LME
		if (resizeDirection == 0) Main.getInstance().getGUI().setCursor(Constants.HAND_CURSOR);
		if ((resizeDirection == Constants.RESIZE_TOP) | (resizeDirection == Constants.RESIZE_BOTTOM)) Main.getInstance().getGUI().setCursor(Constants.TB_CURSOR);
		if ((resizeDirection == Constants.RESIZE_RIGHT) | (resizeDirection == Constants.RESIZE_LEFT)) Main.getInstance().getGUI().setCursor(Constants.LR_CURSOR);
		if ((resizeDirection == Constants.RESIZE_TOP_RIGHT) | (resizeDirection == Constants.RESIZE_BOTTOM_LEFT)) Main.getInstance().getGUI().setCursor(Constants.NE_CURSOR);
		if ((resizeDirection == Constants.RESIZE_BOTTOM_RIGHT) | (resizeDirection == Constants.RESIZE_TOP_LEFT)) Main.getInstance().getGUI().setCursor(Constants.NW_CURSOR);
	}

	private void dragDiagram() {
		if (this.doReturn()) return;
		log.debug("dragDiagram()");

		Point newp = this.getNewCoordinate();
		Point oldp = this.getOldCoordinate();

		int diffx = newp.x - oldp.x;
		int diffy = newp.y - oldp.y;

		Vector<GridElement> v = this.diagram.getAllEntities();
		Vector<Command> moveCommands = new Vector<Command>();
		for (int i = 0; i < v.size(); i++) {
			GridElement e = v.elementAt(i);
			if (e.isPartOfGroup()) continue;
			moveCommands.add(new Move(e, diffx, diffy));
		}

		this.controller.executeCommand(new Macro(moveCommands));
	}

	private void showContextMenu(GridElement me, int x, int y) {

		if (!this.selector.getSelectedEntities().contains(me)) this.selector.singleSelect(me);

		selector.setDominantEntity(me);

		JPopupMenu contextMenu = Main.getInstance().getGUI().getContextMenu(me);
		if (contextMenu != null) contextMenu.show(me, x, y);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		super.mousePressed(me);
		GridElement e = (GridElement) me.getComponent();
		mousePressedPoint = getOffset(me);

		if (!handler.getDrawPanel().equals(Main.getInstance().getPalettePanel())) Main.getInstance().getPalettePanel().getSelector().deselectAllWithoutUpdatePropertyPanel();

		if (me.getButton() == MouseEvent.BUTTON3) {
			this.showContextMenu(e, me.getX(), me.getY());
		}
		else if (me.getButton() == MouseEvent.BUTTON2) {
			IS_DRAGGING_DIAGRAM = true;
		}
		else if (me.getButton() == MouseEvent.BUTTON1) {
			if (me.getClickCount() == 1) {
				pressedLeftButton(me);
			}
			if (me.getClickCount() == 2) {
				this.mouseDoubleClicked(e);
			}
		}
	}

	private void pressedLeftButton(MouseEvent me) {
		GridElement e = (GridElement) me.getComponent();

		// Ctrl + Mouseclick initializes the lasso
		if ((me.getModifiers() & SystemInfo.META_KEY.getMask()) != 0) initializeLasso(me);

		int ra = e.getResizeArea(me.getX(), me.getY());
		ra = ra & e.getPossibleResizeDirections(); // LME

		if (ra != 0) {
			IS_RESIZING = true;
			RESIZE_DIRECTION = ra;
		}
		else {
			IS_DRAGGING = true;
			if ((me.getModifiers() & SystemInfo.META_KEY.getMask()) != 0) {
				if (e.isSelected()) DESELECT_MULTISEL = true;
				else this.selector.select(e);
			}
		}

		if (!this.selector.getSelectedEntities().contains(e)) this.selector.singleSelect(e);
		else this.selector.updateSelectorInformation();
	}

	public void mouseDoubleClicked(GridElement me) {
		GridElement e = me.CloneFromMe();
		GridElementListener eListener = handler.getEntityListener(e);
		Command cmd;
		int gridSize = Main.getInstance().getDiagramHandler().getGridSize();
		cmd = new AddElement(e, me.getX() + gridSize * 2, me.getY() + gridSize * 2);
		// this.IS_FIRST_DRAGGING_OVER=true;
		this.controller.executeCommand(cmd);
		this.selector.singleSelect(e);
		e.setStickingBorderActive(false);
		eListener.IS_FIRST_DRAGGING_OVER = false;
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		super.mouseReleased(me);
		// log.debug("Entity mouse released");
		if (this.IS_DRAGGED_FROM_PALETTE) this.IS_DRAGGED_FROM_PALETTE = false;

		GridElement e = (GridElement) me.getComponent();
		e.setStickingBorderActive(true);

		if ((me.getModifiers() & SystemInfo.META_KEY.getMask()) != 0) {
			if (e.isSelected() && DESELECT_MULTISEL) this.selector.deselect(e);
		}

		DESELECT_MULTISEL = false;
		IS_DRAGGING = false;
		IS_DRAGGING_DIAGRAM = false;
		IS_RESIZING = false;
		IS_FIRST_DRAGGING_OVER = false;
		FIRST_RESIZE = null;
		ALL_MOVE_COMMANDS = null;

		if (LASSO_ACTIVE) {
			LASSO_ACTIVE = false;
			((JComponent) me.getComponent()).remove(selector.getSelectorFrame());
		}
	}

	private void initializeLasso(MouseEvent me) {
		lassoToleranceRectangle = new Rectangle(mousePressedPoint.x - lassoTolerance, mousePressedPoint.y - lassoTolerance, lassoTolerance * 2, lassoTolerance * 2);
		LASSO_ACTIVE = true;
		SelectorFrame selframe = selector.getSelectorFrame();
		selframe.setLocation(mousePressedPoint);
		selframe.setSize(1, 1);
		Main.getInstance().getDiagramHandler().getDrawPanel().add(selframe, 0);
		Main.getInstance().getGUI().setCursor(Constants.DEFAULT_CURSOR);
	}

	private void dragLasso(MouseEvent me, GridElement e) {
		selector.setSelectorFrameActive(true);

		selector.getSelectorFrame().setDisplacement(e.getX(), e.getY());
		selector.getSelectorFrame().resizeTo(me.getX(), me.getY()); // Subtract difference between entityx/entityy and the position of the mouse cursor

		selector.deselectAll(); // If lasso is active the clicked and therefore automatically selected entity gets unselected
	}

	/**
	 * Dragging entities must be a Macro, because undo should undo the full move (and not only a small part which would
	 * happen with many short Move actions) and it must consider sticking relations at the dragging-start and later
	 */
	private void dragEntity(GridElement e) {

		DESELECT_MULTISEL = false;

		Point newp = this.getNewCoordinate();
		Point oldp = this.getOldCoordinate();
		int diffx = newp.x - oldp.x;
		int diffy = newp.y - oldp.y;
		if (!IS_FIRST_DRAGGING_OVER) {
			firstDragging(diffx, diffy);
		}
		else {
			continueDragging(diffx, diffy);
		}
		this.controller.executeCommand(new Macro(ALL_MOVE_COMMANDS));
	}

	/**
	 * At the beginning of dragging entities sticking relations must be calculated (and also moved from now on)
	 */
	private void firstDragging(int diffx, int diffy) {
		Vector<GridElement> entitiesToBeMoved = this.selector.getSelectedEntities();

		Vector<Move> moveCommands = new Vector<Move>();
		Vector<MoveLinePoint> linepointCommands = new Vector<MoveLinePoint>();
		for (GridElement e : entitiesToBeMoved) {
			moveCommands.add(new Move(e, diffx, diffy));
			if (e instanceof Relation) continue;
			StickingPolygon stick = null;
			if (e.isStickingBorderActive()) stick = e.generateStickingBorder(e.getX(), e.getY(), e.getWidth(), e.getHeight());
			if (stick != null) {
				Vector<RelationLinePoint> affectedRelationPoints = stick.getStickingRelationLinePoints(this.diagram);
				for (int j = 0; j < affectedRelationPoints.size(); j++) {
					RelationLinePoint tmpRlp = affectedRelationPoints.elementAt(j);
					if (entitiesToBeMoved.contains(tmpRlp.getRelation())) continue;
					linepointCommands.add(new MoveLinePoint(tmpRlp.getRelation(), tmpRlp.getLinePointId(), diffx, diffy));
				}
			}
		}
		Vector<Command> allCommands = new Vector<Command>();
		allCommands.addAll(moveCommands);
		allCommands.addAll(linepointCommands);

		ALL_MOVE_COMMANDS = allCommands;
		this.IS_FIRST_DRAGGING_OVER = true;
	}

	/**
	 * After the firstDragging is over, the vector of entities which should be dragged doesn't change (nothing starts sticking during dragging)
	 */
	private void continueDragging(int diffx, int diffy) {
		Vector<Command> tmpVector = new Vector<Command>();
		for (int i = 0; i < ALL_MOVE_COMMANDS.size(); i++) {
			Command tmpCommand = ALL_MOVE_COMMANDS.elementAt(i);
			if (tmpCommand instanceof Move) {
				Move m = (Move) tmpCommand;
				tmpVector.add(new Move(m.getEntity(), diffx, diffy));
			}
			else if (tmpCommand instanceof MoveLinePoint) {
				MoveLinePoint m = (MoveLinePoint) tmpCommand;
				tmpVector.add(new MoveLinePoint(m.getRelation(), m.getLinePointId(), diffx, diffy));
			}
		}
		ALL_MOVE_COMMANDS = tmpVector;
	}

	private void resizeEntity(GridElement e, MouseEvent me) {
		int gridSize = Main.getInstance().getDiagramHandler().getGridSize();
		int delta_x = 0;
		int delta_y = 0;
		final Point newp = this.getNewCoordinate();
		final Point oldp = this.getOldCoordinate();

		// If Shift is pressed and the resize direction is any diagonal direction, both axis are resized proportional
		if (me.isShiftDown() && (resizeDirection == Constants.RESIZE_TOP_LEFT || resizeDirection == Constants.RESIZE_TOP_RIGHT ||
				resizeDirection == Constants.RESIZE_BOTTOM_LEFT || resizeDirection == Constants.RESIZE_BOTTOM_RIGHT)) {
			if (e.getWidth() > e.getHeight()) {
				float proportion = (float) newp.x / mousePressedPoint.x;
				newp.setLocation(newp.x, mousePressedPoint.y*proportion);
			}
			else {
				float proportion = (float) newp.y / mousePressedPoint.y;
				newp.setLocation(mousePressedPoint.x*proportion, newp.y);
			}
		}

		if ((RESIZE_DIRECTION & Constants.RESIZE_RIGHT) > 0) {
			delta_x = (e.getX() + e.getWidth()) % gridSize;
		}
		if ((RESIZE_DIRECTION & Constants.RESIZE_BOTTOM) > 0) {
			delta_y = (e.getY() + e.getHeight()) % gridSize;
		}

		int diffx = newp.x - oldp.x - delta_x;
		int diffy = newp.y - oldp.y - delta_y;
		int diffw = 0;
		int diffh = 0;
		e.setManualResized();

		// AB: get minsize; add 0.5 to round float at typecast; then add rest to stick on grid
		// AB: Would be better if this is defined in the Entity class
		int minSize = (int) (2 * this.handler.getFontHandler().getFontSize() + 0.5);
		minSize = minSize - minSize % gridSize;

		if ((RESIZE_DIRECTION & Constants.RESIZE_LEFT) > 0) {
			// AB: get diffx; add MAIN_UNIT because of a natural offset (possible bug in mouse pos calculation?)
			diffx = newp.x - e.getX() + gridSize;

			// AB: only shrink to minimum size
			if (e.getWidth() - diffx < minSize) {
				diffx = e.getWidth() - minSize;
			}

			if (RESIZE_DIRECTION == Constants.RESIZE_LEFT) {
				diffy = 0;
			}
		}

		if ((RESIZE_DIRECTION & Constants.RESIZE_RIGHT) > 0) {
			// AB: get diffx; add MAIN_UNIT because of a natural offset (possible bug in mouse pos calculation?)
			diffx = newp.x - (e.getX() + e.getWidth()) + gridSize;

			// AB: only shrink to minimum size
			if (e.getWidth() + diffx < minSize) {
				diffx = minSize - e.getWidth();
			}

			if (RESIZE_DIRECTION == Constants.RESIZE_RIGHT) {
				diffy = 0;
			}

			diffw = diffx;
			diffx = 0;
		}

		if ((RESIZE_DIRECTION & Constants.RESIZE_TOP) > 0) {
			// AB: get diffy; add MAIN_UNIT because of a natural offset (possible bug in mouse pos calculation?)
			diffy = newp.y - e.getY() + gridSize;

			// AB: only shrink to minimum size
			if (e.getHeight() - diffy < minSize) {
				diffy = e.getHeight() - minSize;
			}

			if (RESIZE_DIRECTION == Constants.RESIZE_TOP) {
				diffx = 0;
			}
		}

		if ((RESIZE_DIRECTION & Constants.RESIZE_BOTTOM) > 0) {
			// AB: get diffy; add MAIN_UNIT because of a natural offset (possible bug in mouse pos calculation?)
			diffy = newp.y - (e.getY() + e.getHeight()) + gridSize;

			// AB: only shrink to minimum size
			if (e.getHeight() + diffy < minSize) {
				diffy = minSize - e.getHeight();
			}

			if (RESIZE_DIRECTION == Constants.RESIZE_BOTTOM) {
				diffx = 0;
			}

			diffh = diffy;
			diffy = 0;
		}

		Resize resize;
		if (FIRST_RESIZE == null) {
			resize = new Resize(e, diffx, diffy, diffw, diffh);
			FIRST_RESIZE = resize;

		}
		else {
			resize = new Resize(e, diffx, diffy, diffw, diffh, FIRST_RESIZE);
		}

		this.controller.executeCommand(resize);
	}

}
