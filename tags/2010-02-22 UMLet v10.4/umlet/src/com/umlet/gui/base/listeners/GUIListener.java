package com.umlet.gui.base.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import com.umlet.control.Umlet;
import com.umlet.control.command.Command;
import com.umlet.control.command.Macro;
import com.umlet.control.command.Move;
import com.umlet.control.command.MoveLinePoint;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.StickingPolygon;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Relation;
import com.umlet.element.base.relation.RelationLinePoint;

public class GUIListener implements KeyListener {

	public void keyPressed(KeyEvent e) {

		DiagramHandler handler = Umlet.getInstance().getDiagramHandler();

		if (!e.isAltDown() && !e.isAltGraphDown() /* && !e.isControlDown() && !e.isMetaDown() */) {

			/**
			 * Enter: jumps directly into the diagram
			 */
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				Umlet.getInstance().getGUI().focusPropertyPane();
			}

			/**
			 * Ctrl +/-: Zoom diagram by 10%
			 */
			// KeyChar check doesn't check non-numpad + on some keyboards, therefore we also need KeyEvent.VK_PLUS
			else if (((e.getKeyChar() == '+') || (e.getKeyCode() == KeyEvent.VK_PLUS)) && (handler != null)) {
				int actualZoom = handler.getGridSize();
				handler.setGridAndZoom(actualZoom + 1);
			}
			// KeyChar check doesn't check non-numpad - on some keyboards, therefore we also need KeyEvent.VK_MINUS
			else if (((e.getKeyChar() == '-') || (e.getKeyCode() == KeyEvent.VK_MINUS)) && (handler != null)) {
				int actualZoom = handler.getGridSize();
				handler.setGridAndZoom(actualZoom - 1);
			}
			/**
			 * Cursors: Move diagram by a small distance
			 */
			else {
				int diffx = 0;
				int diffy = 0;

				if ((e.getKeyCode() == KeyEvent.VK_DOWN) || (e.getKeyCode() == KeyEvent.VK_KP_DOWN)) diffy = handler.getGridSize();
				if ((e.getKeyCode() == KeyEvent.VK_UP) || (e.getKeyCode() == KeyEvent.VK_KP_UP)) diffy = -handler.getGridSize();
				if ((e.getKeyCode() == KeyEvent.VK_LEFT) || (e.getKeyCode() == KeyEvent.VK_KP_LEFT)) diffx = -handler.getGridSize();
				if ((e.getKeyCode() == KeyEvent.VK_RIGHT) || (e.getKeyCode() == KeyEvent.VK_KP_RIGHT)) diffx = handler.getGridSize();

				if ((diffx != 0) || (diffy != 0)) {
					Vector<Entity> entitiesToBeMoved;
					// Move only selected entities or all if no entity is selected
					entitiesToBeMoved = handler.getDrawPanel().getSelector().getSelectedEntities();
					if (entitiesToBeMoved.isEmpty()) entitiesToBeMoved = handler.getDrawPanel().getAllEntities();

					// TODO The following code is very similar to EntityListener 96-144 and should be refactored
					Vector<Command> moveCommands = new Vector<Command>();
					for (int i = 0; i < entitiesToBeMoved.size(); i++) {
						Entity entity = entitiesToBeMoved.elementAt(i);
						if (entity.isPartOfGroup()) continue;
						entity.setStickBorders(true);
						moveCommands.add(new Move(entity, diffx, diffy));
					}
					Vector<Command> linepointCommands = new Vector<Command>();
					for (int i = 0; i < entitiesToBeMoved.size(); i++) {
						Entity tmpEntity = entitiesToBeMoved.elementAt(i);
						if (tmpEntity instanceof Relation) continue;
						StickingPolygon stick = null;
						if (tmpEntity.isStickBorders()) stick = tmpEntity.generateStickingBorder(tmpEntity.getX(), tmpEntity.getY(), tmpEntity.getWidth(), tmpEntity.getHeight());
						else tmpEntity.setStickBorders(true);
						if (stick != null) {
							Vector<RelationLinePoint> affectedRelationPoints = stick.getStickingRelationLinePoints(handler.getDrawPanel());
							for (int j = 0; j < affectedRelationPoints.size(); j++) {
								RelationLinePoint tmpRlp = affectedRelationPoints.elementAt(j);
								if (entitiesToBeMoved.contains(tmpRlp.getRelation())) continue;
								linepointCommands.add(new MoveLinePoint(tmpRlp.getRelation(), tmpRlp.getLinePointId(), diffx, diffy));
							}
						}
					}
					Vector<Command> ALL_MOVE_COMMANDS = new Vector<Command>();
					ALL_MOVE_COMMANDS.addAll(moveCommands);
					ALL_MOVE_COMMANDS.addAll(linepointCommands);

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
					handler.getController().executeCommand(new Macro(ALL_MOVE_COMMANDS));
					Umlet.getInstance().getDiagramHandler().getDrawPanel().updatePanelAndScrollbars();
				}
			}
		}

		// TODO implement ESC Overview
		// if ((e.getModifiersEx() == 0) && (e.getKeyCode() == KeyEvent.VK_ESCAPE)) {
		// if ((handler != null) && (handler.getDrawPanel() != null)) {
		// // As long as the horizontal or vertical scrollbar is visible we must zoom 1 step out
		// while (handler.getDrawPanel().getScrollPanel().getHorizontalScrollBar().isVisible() ||
		// handler.getDrawPanel().getScrollPanel().getVerticalScrollBar().isVisible()) {
		// int actualZoom = handler.getGridSize();
		// handler.setGridAndZoom(actualZoom - 1);
		// }
		// }
		// }

	}

	public void keyReleased(KeyEvent arg0) {}

	public void keyTyped(KeyEvent arg0) {}

}
