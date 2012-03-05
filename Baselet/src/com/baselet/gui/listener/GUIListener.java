package com.baselet.gui.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.command.Command;
import com.baselet.diagram.command.Macro;
import com.baselet.diagram.command.Move;
import com.baselet.diagram.command.MoveLinePoint;
import com.baselet.element.GridElement;
import com.baselet.element.StickingPolygon;
import com.umlet.element.Relation;
import com.umlet.element.relation.RelationLinePoint;


public class GUIListener implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {

		DiagramHandler handler = Main.getInstance().getDiagramHandler();

		if ((handler != null) && !e.isAltDown() && !e.isAltGraphDown() /* && !e.isControlDown() && !e.isMetaDown() */) {

			/**
			 * Enter: jumps directly into the diagram
			 */
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				Main.getInstance().getGUI().focusPropertyPane();
			}

			/**
			 * Ctrl +/-: Zoom diagram by 10%
			 */
			// KeyChar check doesn't check non-numpad + on some keyboards, therefore we also need KeyEvent.VK_PLUS
			else if (((e.getKeyChar() == '+') || (e.getKeyCode() == KeyEvent.VK_PLUS))) {
				int actualZoom = handler.getGridSize();
				handler.setGridAndZoom(actualZoom + 1);
			}
			// KeyChar check doesn't check non-numpad - on some keyboards, therefore we also need KeyEvent.VK_MINUS
			else if (((e.getKeyChar() == '-') || (e.getKeyCode() == KeyEvent.VK_MINUS))) {
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
					Vector<GridElement> entitiesToBeMoved;
					// Move only selected entities or all if no entity is selected
					entitiesToBeMoved = handler.getDrawPanel().getSelector().getSelectedEntities();
					if (entitiesToBeMoved.isEmpty()) entitiesToBeMoved = handler.getDrawPanel().getAllEntities();

					// TODO The following code is very similar to EntityListener 96-144 and should be refactored
					Vector<Command> moveCommands = new Vector<Command>();
					for (int i = 0; i < entitiesToBeMoved.size(); i++) {
						GridElement entity = entitiesToBeMoved.elementAt(i);
						if (entity.isPartOfGroup()) continue;
						entity.setStickingBorderActive(true);
						moveCommands.add(new Move(entity, diffx, diffy));
					}
					Vector<Command> linepointCommands = new Vector<Command>();
					for (int i = 0; i < entitiesToBeMoved.size(); i++) {
						GridElement tmpEntity = entitiesToBeMoved.elementAt(i);
						if (tmpEntity instanceof Relation) continue;
						StickingPolygon stick = null;
						if (tmpEntity.isStickingBorderActive()) stick = tmpEntity.generateStickingBorder(tmpEntity.getLocation().x, tmpEntity.getLocation().y, tmpEntity.getSize().width, tmpEntity.getSize().height);
						else tmpEntity.setStickingBorderActive(true);
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
					Main.getInstance().getDiagramHandler().getDrawPanel().updatePanelAndScrollbars();
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

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

}
