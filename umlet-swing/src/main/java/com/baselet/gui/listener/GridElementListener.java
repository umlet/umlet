package com.baselet.gui.listener;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.Main;
import com.baselet.control.basics.Converter;
import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.SharedConfig;
import com.baselet.control.constants.SystemInfo;
import com.baselet.control.enums.Direction;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.PaletteHandler;
import com.baselet.diagram.SelectorFrame;
import com.baselet.element.ElementFactorySwing;
import com.baselet.element.facet.common.GroupFacet;
import com.baselet.element.interfaces.CursorOwn;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.old.element.Relation;
import com.baselet.element.sticking.StickableMap;
import com.baselet.element.sticking.Stickables;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.gui.CurrentGui;
import com.baselet.gui.command.AddElement;
import com.baselet.gui.command.Command;
import com.baselet.gui.command.Macro;
import com.baselet.gui.command.Move;
import com.baselet.gui.command.MoveEnd;
import com.baselet.gui.command.OldMoveLinePoint;
import com.baselet.gui.command.OldRelationLinePoint;
import com.baselet.gui.command.OldResize;

public class GridElementListener extends UniversalListener {

	private static final Logger log = LoggerFactory.getLogger(GridElementListener.class);

	protected boolean IS_DRAGGING = false;
	protected boolean IS_DRAGGING_DIAGRAM = false;
	protected boolean IS_DRAGGED_FROM_PALETTE = false;
	private boolean FIRST_DRAG = true;
	private Vector<Command> FIRST_MOVE_COMMANDS = null;
	private Point POINT_BEFORE_MOVE = null;
	protected boolean DESELECT_MULTISEL = false;
	private boolean LASSO_ACTIVE = false;

	private Rectangle lassoToleranceRectangle;
	private final int lassoTolerance = 2;

	private Point mousePressedPoint;
	private Set<Direction> resizeDirections;

	public GridElementListener(DiagramHandler handler) {
		super(handler);
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		super.mouseDragged(me);
		log.debug("Entity dragged");

		GridElement e = getGridElement(me);

		// Lasso selection is only activated if mouse is moved more than lasso_tolerance to avoid accidential activation instead of selecting the entity
		if (LASSO_ACTIVE && lassoToleranceRectangle != null && !lassoToleranceRectangle.contains(getOffset(me))) {
			dragLasso(me, e);
			return;
		}

		if (disableElementMovement()) {
			return;
		}

		if (IS_DRAGGING_DIAGRAM) {
			dragDiagram();
		}
		if (IS_DRAGGING) {
			dragEntity(me.isShiftDown(), e);
		}
	}

	private GridElement getGridElement(MouseEvent me) {
		return handler.getDrawPanel().getElementToComponent(me.getComponent());
	}

	@Override
	protected Point getOffset(MouseEvent me) {
		return new Point(me.getX() + me.getComponent().getX(), me.getY() + me.getComponent().getY());
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		super.mouseMoved(me);
		GridElement e = getGridElement(me);
		if (IS_DRAGGED_FROM_PALETTE) {
			log.debug("mouseMoved with dragged");
			e.setLocation(me.getX() - 100, me.getY() - 20);
		}
		resizeDirections = e.getResizeArea(me.getX(), me.getY());
		Point point = new Point(me.getX() + e.getRectangle().getX(), me.getY() + e.getRectangle().getY());

		CursorOwn cursor = e.getCursor(point, resizeDirections);
		if (cursor != null) {
			CurrentGui.getInstance().getGui().setCursor(Converter.convert(cursor));
		}
	}

	private void showContextMenu(GridElement ge, int x, int y) {

		if (!selector.getSelectedElements().contains(ge)) {
			selector.selectOnly(ge);
		}

		selector.setDominantEntity(ge);

		JPopupMenu contextMenu = CurrentGui.getInstance().getGui().getContextMenu(ge);
		if (contextMenu != null) {
			contextMenu.show((Component) ge.getComponent(), x, y);
		}
	}

	@Override
	public void mousePressed(MouseEvent me) {
		super.mousePressed(me);
		GridElement e = getGridElement(me);
		mousePressedPoint = getOffset(me);

		// deselect elements of all other drawpanels
		for (DiagramHandler h : Main.getInstance().getDiagramsAndPalettes()) {
			if (!h.equals(handler)) {
				h.getDrawPanel().getSelector().deselectAllWithoutUpdatePropertyPanel();
			}
		}

		if (me.getButton() == MouseEvent.BUTTON3) {
			showContextMenu(e, me.getX(), me.getY());
		}
		else if (me.getButton() == MouseEvent.BUTTON2) {
			IS_DRAGGING_DIAGRAM = true;
		}
		else if (me.getButton() == MouseEvent.BUTTON1) {
			if (me.getClickCount() == 1) {
				pressedLeftButton(me);
			}
			if (me.getClickCount() == 2) {
				mouseDoubleClicked(e);
			}
		}
	}

	private void pressedLeftButton(MouseEvent me) {
		GridElement e = getGridElement(me);

		// Ctrl + Mouseclick initializes the lasso
		if ((me.getModifiers() & SystemInfo.META_KEY.getMask()) != 0) {
			initializeLasso();
		}

		IS_DRAGGING = true;
		if ((me.getModifiers() & SystemInfo.META_KEY.getMask()) != 0) {
			if (selector.isSelected(e)) {
				DESELECT_MULTISEL = true;
			}
			else {
				selector.select(e);
			}
		}

		if (!selector.getSelectedElements().contains(e)) {
			selector.selectOnly(e);
		}
		else {
			selector.updateSelectorInformation(e);
		}
	}

	public void mouseDoubleClicked(GridElement me) {
		GridElement e = ElementFactorySwing.createCopy(me);
		e.setProperty(GroupFacet.KEY, null);
		GridElementListener eListener = handler.getEntityListener(e);
		Command cmd;
		int gridSize = CurrentDiagram.getInstance().getDiagramHandler().getGridSize();
		cmd = new AddElement(e, me.getRectangle().x + gridSize * 2, me.getRectangle().y + gridSize * 2);
		controller.executeCommand(cmd);
		selector.selectOnly(e);
		eListener.FIRST_DRAG = true;
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		super.mouseReleased(me);
		// log.debug("Entity mouse released");
		if (IS_DRAGGED_FROM_PALETTE) {
			IS_DRAGGED_FROM_PALETTE = false;
		}

		GridElement e = getGridElement(me);

		if ((me.getModifiers() & SystemInfo.META_KEY.getMask()) != 0) {
			if (selector.isSelected(e) && DESELECT_MULTISEL) {
				selector.deselect(e);
			}
		}
		if (IS_DRAGGING && !FIRST_DRAG) { // if mouse is dragged and element really has been dragged around execute moveend
			controller.executeCommand(new MoveEnd(e));
		}

		DESELECT_MULTISEL = false;
		IS_DRAGGING = false;
		IS_DRAGGING_DIAGRAM = false;
		FIRST_DRAG = true;
		FIRST_MOVE_COMMANDS = null;
		POINT_BEFORE_MOVE = null;

		if (LASSO_ACTIVE) {
			LASSO_ACTIVE = false;
			((JComponent) me.getComponent()).remove(selector.getSelectorFrame());
		}
	}

	private void initializeLasso() {
		lassoToleranceRectangle = new Rectangle(mousePressedPoint.x - lassoTolerance, mousePressedPoint.y - lassoTolerance, lassoTolerance * 2, lassoTolerance * 2);
		LASSO_ACTIVE = true;
		SelectorFrame selframe = selector.getSelectorFrame();
		selframe.setLocation(Converter.convert(mousePressedPoint));
		selframe.setSize(1, 1);
		CurrentDiagram.getInstance().getDiagramHandler().getDrawPanel().add(selframe, 0);
		CurrentGui.getInstance().getGui().setCursor(Converter.convert(CursorOwn.DEFAULT));
	}

	private void dragLasso(MouseEvent me, GridElement e) {
		selector.setSelectorFrameActive(true);

		selector.getSelectorFrame().setDisplacement(e.getRectangle().x, e.getRectangle().y);
		selector.getSelectorFrame().resizeTo(me.getX(), me.getY()); // Subtract difference between entityx/entityy and the position of the mouse cursor

		selector.deselectAll(); // If lasso is active the clicked and therefore automatically selected entity gets unselected
	}

	/**
	 * Dragging entities must be a Macro, because undo should undo the full move (and not only a small part which would
	 * happen with many short Move actions) and it must consider sticking relations at the dragging-start and later
	 * @param mainElement
	 * @param directions
	 * @param b
	 */
	private void dragEntity(boolean isShiftKeyDown, GridElement mainElement) {

		DESELECT_MULTISEL = false;

		Point newp = getNewCoordinate();
		Point oldp = getOldCoordinate();
		int diffx = newp.x - oldp.x;
		int diffy = newp.y - oldp.y;

		List<GridElement> elementsToMove = selector.getSelectedElements();
		if (!resizeDirections.isEmpty()) {
			elementsToMove = Arrays.asList(mainElement);
		}
		if (FIRST_MOVE_COMMANDS == null) {
			POINT_BEFORE_MOVE = mousePressedPoint; // issue #358: use position of mouse click BEFORE starting to drag; must be exact coordinates eg for Relation which calculates distances from lines (to possibly drag new points out of it)
			FIRST_MOVE_COMMANDS = calculateFirstMoveCommands(diffx, diffy, POINT_BEFORE_MOVE, elementsToMove, isShiftKeyDown, false, handler, resizeDirections);
		}
		else if (diffx != 0 || diffy != 0) {
			Vector<Command> commands = continueDragging(diffx, diffy, POINT_BEFORE_MOVE, elementsToMove);
			POINT_BEFORE_MOVE = new Point(POINT_BEFORE_MOVE.getX() + diffx, POINT_BEFORE_MOVE.getY() + diffy);
			controller.executeCommand(new Macro(commands));
			FIRST_DRAG = false;
		}
	}

	static Vector<Command> calculateFirstMoveCommands(int diffx, int diffy, Point oldp, Collection<GridElement> entitiesToBeMoved, boolean isShiftKeyDown, boolean useSetLocation, DiagramHandler handler, Set<Direction> directions) {
		Vector<Move> moveCommands = new Vector<Move>();
		Vector<OldMoveLinePoint> linepointCommands = new Vector<OldMoveLinePoint>();
		List<com.baselet.element.relation.Relation> stickables = handler.getDrawPanel().getStickables(entitiesToBeMoved);
		for (GridElement ge : entitiesToBeMoved) {
			// reduce stickables to those which really stick at the element at move-start
			StickableMap stickingStickables = Stickables.getStickingPointsWhichAreConnectedToStickingPolygon(ge.generateStickingBorder(), stickables);
			moveCommands.add(new Move(directions, ge, diffx, diffy, oldp, isShiftKeyDown, true, useSetLocation, stickingStickables));

			handleStickingOfOldRelation(diffx, diffy, entitiesToBeMoved, handler, directions, linepointCommands, ge);
		}
		Vector<Command> allCommands = new Vector<Command>();
		allCommands.addAll(moveCommands);
		allCommands.addAll(linepointCommands);
		return allCommands;
	}

	// for elements which are not OldRelation themselves and if sticking is not disabled, handle the sticking-movement of old relations. SHOULD BE REMOVED AS SOON AS THE OLDRELATION CLASS IS REMOVED!
	@Deprecated
	private static void handleStickingOfOldRelation(int diffx, int diffy, Collection<GridElement> entitiesToBeMoved, DiagramHandler handler, Set<Direction> directions, Vector<OldMoveLinePoint> linepointCommands, GridElement ge) {
		boolean stickingDisabled = !SharedConfig.getInstance().isStickingEnabled() || handler instanceof PaletteHandler;
		if (!(ge instanceof Relation || stickingDisabled)) {
			StickingPolygon stick = ge.generateStickingBorder();
			if (stick != null && directions.isEmpty()) { // sticking on resizing is disabled for old relations
				Vector<OldRelationLinePoint> affectedRelationPoints = OldResize.getStickingRelationLinePoints(handler, stick);
				for (int j = 0; j < affectedRelationPoints.size(); j++) {
					OldRelationLinePoint tmpRlp = affectedRelationPoints.elementAt(j);
					if (entitiesToBeMoved.contains(tmpRlp.getRelation())) {
						continue;
					}
					linepointCommands.add(new OldMoveLinePoint(tmpRlp.getRelation(), tmpRlp.getLinePointId(), diffx, diffy));
				}
			}
		}
	}

	/**
	 * After the firstDragging is over, the vector of entities which should be dragged doesn't change (nothing starts sticking during dragging)
	 * @param oldp
	 * @param elementsToMove
	 * @param directions
	 * @return
	 */
	private Vector<Command> continueDragging(int diffx, int diffy, Point oldp, List<GridElement> elementsToMove) {
		boolean useSetLocation = elementsToMove.size() != 1; // if >1 elements are selected they will be moved
		Vector<Command> tmpVector = new Vector<Command>();
		for (Command command : FIRST_MOVE_COMMANDS) { // use first move commands to identify the necessary commands and moved entities
			if (command instanceof Move) {
				Move m = (Move) command;
				tmpVector.add(new Move(resizeDirections, m.getEntity(), diffx, diffy, oldp, m.isShiftKeyDown(), FIRST_DRAG, useSetLocation, m.getStickables()));
			}
			else if (command instanceof OldMoveLinePoint) {
				OldMoveLinePoint m = (OldMoveLinePoint) command;
				tmpVector.add(new OldMoveLinePoint(m.getRelation(), m.getLinePointId(), diffx, diffy));
			}
		}
		return tmpVector;
	}

}
