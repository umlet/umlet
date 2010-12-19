package com.umlet.gui.base.listeners;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.command.AddEntity;
import com.umlet.control.command.Command;
import com.umlet.control.command.Macro;
import com.umlet.control.command.Move;
import com.umlet.control.command.MoveLinePoint;
import com.umlet.control.command.Resize;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.StickingPolygon;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Relation;
import com.umlet.element.base.relation.RelationLinePoint;

public class EntityListener extends UniversalListener {
	
	private static HashMap<DiagramHandler, EntityListener> entitylistener = new HashMap<DiagramHandler, EntityListener>();

	public static EntityListener getInstance(DiagramHandler handler) {
		if (!entitylistener.containsKey(handler)) entitylistener.put(handler, new EntityListener(handler));
		return entitylistener.get(handler);
	}

	protected boolean IS_DRAGGING = false;
	protected boolean IS_RESIZING = false;
	private int RESIZE_DIRECTION = 0;
	private boolean IS_FIRST_DRAGGING_OVER = false;
	private Resize FIRST_RESIZE = null;
	private Vector<Command> ALL_MOVE_COMMANDS = null;
	private boolean DESELECT_MULTISEL = false;
	private boolean IS_SELECTING_BY_LASSO = false;

	private Point clickStart;
	private Rectangle clickRect;
	private int lasso_tolerance = 2;

	protected EntityListener(DiagramHandler handler) {
		super(handler);
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		super.mouseDragged(me);
		Entity e = (Entity) me.getComponent();
		int MAIN_UNIT = Umlet.getInstance().getDiagramHandler().getGridSize();

		// Lasso selection is only activated if mouse is moved more than lasso_tolerance to avoid accidential activation instead of selecting the entity
		if (IS_SELECTING_BY_LASSO && (clickRect != null) && !clickRect.contains(new Point(getOffset(me).x, getOffset(me).y))) {
			selector.setSelectorFrameActive(true);
			System.out.println(me.getPoint());

			int xNew = me.getX();
			int yNew = me.getY();
			selector.getSelectorFrame().setDisplacement(e.getX(), e.getY());
			selector.getSelectorFrame().resizeTo(xNew, yNew); // Differenz zwischen entityx bzw y und mauszeiger abziehen

			selector.deselectAll(); // If lasso is active the clicked and automatically selected entity gets unselected
			return;
		}

		if (this.doReturn()) return;

		// delta
		int delta_x = 0;
		int delta_y = 0;
		if (IS_RESIZING) {
			if ((RESIZE_DIRECTION & Constants.RESIZE_RIGHT) > 0) {
				delta_x = (e.getX() + e.getWidth()) % MAIN_UNIT;
			}
			if ((RESIZE_DIRECTION & Constants.RESIZE_BOTTOM) > 0) {
				delta_y = (e.getY() + e.getHeight()) % MAIN_UNIT;
			}
		}

		Point newp = this.getNewCoordinate();
		Point oldp = this.getOldCoordinate();

		int diffx = newp.x - oldp.x - delta_x;
		int diffy = newp.y - oldp.y - delta_y;

		if (IS_DRAGGING) {
			DESELECT_MULTISEL = false;
			if (IS_FIRST_DRAGGING_OVER == false) {
				Vector<Entity> entitiesToBeMoved = new Vector<Entity>(
						this.selector.getSelectedEntities());

				Vector<Move> moveCommands = new Vector<Move>();
				for (Entity eToBeMoved : entitiesToBeMoved)
					moveCommands.add(new Move(eToBeMoved, diffx, diffy));

				Vector<MoveLinePoint> linepointCommands = new Vector<MoveLinePoint>();
				for (int i = 0; i < entitiesToBeMoved.size(); i++) {
					Entity tmpEntity = entitiesToBeMoved.elementAt(i);
					if (tmpEntity instanceof Relation) continue;
					StickingPolygon stick = null;					
					if(e.isStickBorders())
						stick = tmpEntity.generateStickingBorder(tmpEntity.getX(),
								tmpEntity.getY(), tmpEntity.getWidth(), tmpEntity.getHeight());
					else
						e.setStickBorders(true);
					if (stick != null) {
						Vector<RelationLinePoint> affectedRelationPoints = stick.getStickingRelationLinePoints(this.diagram);
						for (int j = 0; j < affectedRelationPoints.size(); j++) {
							RelationLinePoint tmpRlp = affectedRelationPoints
									.elementAt(j);
							if (entitiesToBeMoved
									.contains(tmpRlp.getRelation())) continue;
							linepointCommands.add(new MoveLinePoint(tmpRlp
									.getRelation(), tmpRlp.getLinePointId(),
									diffx, diffy));
						}
					}
				}
				Vector<Command> allCommands = new Vector<Command>();
				allCommands.addAll(moveCommands);
				allCommands.addAll(linepointCommands);

				ALL_MOVE_COMMANDS = allCommands;
				IS_FIRST_DRAGGING_OVER = true;
			}
			else {
				Vector<Command> tmpVector = new Vector<Command>();
				for (int i = 0; i < ALL_MOVE_COMMANDS.size(); i++) {
					Command tmpCommand = ALL_MOVE_COMMANDS.elementAt(i);
					if (tmpCommand instanceof Move) {
						Move m = (Move) tmpCommand;
						tmpVector
								.add(new Move(m.getEntity(), diffx, diffy));
					}
					else if (tmpCommand instanceof MoveLinePoint) {
						MoveLinePoint m = (MoveLinePoint) tmpCommand;
						tmpVector.add(new MoveLinePoint(m.getRelation(), m
								.getLinePointId(), diffx, diffy));
					}
				}
				ALL_MOVE_COMMANDS = tmpVector;
			}

			this.controller.executeCommand(
					new Macro(ALL_MOVE_COMMANDS));

		}
		else if (IS_RESIZING) {
			int diffw = 0;
			int diffh = 0;
			e.setManualResized();

			// AB: get minsize; add 0.5 to round float at typecast; then add rest to stick on grid
			// AB: Would be better if this is defined in the Entity class
			int minSize = (int) (2 * this.handler.getZoomedFontsize() + 0.5);
			minSize = minSize - minSize % MAIN_UNIT;

			if ((RESIZE_DIRECTION & Constants.RESIZE_LEFT) > 0) {
				// AB: get diffx; add MAIN_UNIT because of a natural offset (possible bug in mouse pos calculation?)
				diffx = newp.x - e.getX() + MAIN_UNIT;

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
				diffx = newp.x - (e.getX() + e.getWidth()) + MAIN_UNIT;

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
				diffy = newp.y - e.getY() + MAIN_UNIT;

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
				diffy = newp.y - (e.getY() + e.getHeight()) + MAIN_UNIT;

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

	@Override
	protected Point getOffset(MouseEvent me) {
		return new Point(me.getX() + me.getComponent().getX(), me.getY() + me.getComponent().getY());
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		super.mouseMoved(me);
		Entity e = (Entity) me.getComponent();
		int ra = e.getResizeArea(me.getX(), me.getY());
		ra = ra & e.getPossibleResizeDirections(); // LME
		if (0 != ra) {
			if ((ra == 1) | (ra == 4)) Umlet.getInstance().getGUI().setCursor(Constants.TB_CURSOR);
			if ((ra == 2) | (ra == 8)) Umlet.getInstance().getGUI().setCursor(Constants.LR_CURSOR);
			if ((ra == 3) | (ra == 12)) Umlet.getInstance().getGUI().setCursor(Constants.NE_CURSOR);
			if ((ra == 6) | (ra == 9)) Umlet.getInstance().getGUI().setCursor(Constants.NW_CURSOR);
		}
		else Umlet.getInstance().getGUI().setCursor(Constants.HAND_CURSOR);
	}

	private void showContextMenu(Entity me, int x, int y) {
		JPopupMenu contextMenu = Umlet.getInstance().getGUI().getContextMenu(me);
		if (contextMenu != null) contextMenu.show(me, x, y);
		if (!this.selector.getSelectedEntities().contains(me)) this.selector.singleSelect(me);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		super.mousePressed(me);
		Entity e = (Entity) me.getComponent();

		if (me.getButton() == MouseEvent.BUTTON3) {
			this.showContextMenu(e, me.getX(), me.getY());
		}
		else if (me.getButton() == MouseEvent.BUTTON1) {
			if (me.getClickCount() == 2) this.mouseDoubleClicked(e);
			else if (me.getClickCount() == 1) {
				// TODO uncomment to activate lasso on entities
				// if ((me.getModifiers() & Constants.CTRLMETA_MASK) != 0) {
				// clickStart = new Point((int) getOffset(me).getX(), (int) getOffset(me).getY());
				// clickRect = new Rectangle((int) getOffset(me).getX() - lasso_tolerance, (int) getOffset(me).getY() - lasso_tolerance, lasso_tolerance*2, lasso_tolerance*2);
				// IS_SELECTING_BY_LASSO = true;
				// SelectorFrame selframe = this.selector.getSelectorFrame();
				// selframe.setLocation((int) me.getX(), (int) me.getY());
				// selframe.setSize(1, 1);
				// ((JComponent) me.getComponent()).add(selframe, 0);
				// Umlet.getInstance().getGUI().setCursor(Constants.DEFAULT_CURSOR);
				// }

				int ra = e.getResizeArea(me.getX(), me.getY());
				ra = ra & e.getPossibleResizeDirections(); // LME
				
				if (ra != 0) {
					IS_RESIZING = true;
					RESIZE_DIRECTION = ra;
				}
				else {
					IS_DRAGGING = true;
					if ((me.getModifiers() & Constants.CTRLMETA_MASK) != 0) {
						if (e.isSelected()) DESELECT_MULTISEL = true;
						else this.selector.select(e);
					}
				}

				if (!this.selector.getSelectedEntities().contains(e)) this.selector.singleSelect(e);
				else this.selector.updateSelectorInformation();
			}
		}
	}

	public void mouseDoubleClicked(Entity me) {
		Entity e = me.CloneFromMe();
		e.setStickBorders(false);
		Command cmd;
		int gridSize = Umlet.getInstance().getDiagramHandler().getGridSize();
		cmd = new AddEntity(e, me.getX() + gridSize * 2, me.getY() + gridSize * 2);
		this.controller.executeCommand(cmd);
		this.selector.singleSelect(e);
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		super.mouseReleased(me);

		Entity e = (Entity) me.getComponent();

		if ((me.getModifiers() & Constants.CTRLMETA_MASK) != 0) {
			if (e.isSelected() && DESELECT_MULTISEL) this.selector.deselect(e);
		}

		DESELECT_MULTISEL = false;
		IS_DRAGGING = false;
		IS_RESIZING = false;
		IS_FIRST_DRAGGING_OVER = false;
		FIRST_RESIZE = null;
		ALL_MOVE_COMMANDS = null;

		if (IS_SELECTING_BY_LASSO) {
			IS_SELECTING_BY_LASSO = false;
			((JComponent) me.getComponent()).remove(selector.getSelectorFrame());
		}
	}

}
