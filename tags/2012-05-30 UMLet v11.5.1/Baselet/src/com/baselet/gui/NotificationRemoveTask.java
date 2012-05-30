package com.baselet.gui;

import java.util.TimerTask;

import javax.swing.JComponent;

import com.baselet.diagram.DrawPanel;


public class NotificationRemoveTask extends TimerTask {

	private JComponent entityToRemove;
	private DrawPanel diagram;

	public NotificationRemoveTask(JComponent entityToRemove, DrawPanel diagram) {
		this.entityToRemove = entityToRemove;
		this.diagram = diagram;
	}

	@Override
	public void run() {
		// Controller controller = diagram.getHandler().getController();
		// Vector<Entity> toDelete = new Vector<Entity>();
		// toDelete.add(entityToRemove);
		// Command removeNotificationEntity = new RemoveElement(toDelete);
		// Vector<Entity> selectedEntities = (Vector<Entity>) Main.getInstance().getDiagramHandler().getDrawPanel().getSelector().getSelectedEntities().clone();
		// controller.executeCommand(removeNotificationEntity);
		// for(Entity e : selectedEntities){
		// diagram.getHandler().getDrawPanel().getSelector().select(e);
		// }
		diagram.getHandler().getDrawPanel().remove(entityToRemove);
		diagram.getHandler().getDrawPanel().repaint();
	}

}
