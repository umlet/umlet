package com.umlet.gui.base;

import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JComponent;

import com.umlet.control.Umlet;
import com.umlet.control.command.Command;
import com.umlet.control.command.RemoveElement;
import com.umlet.control.diagram.Controller;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.element.base.Entity;

public class NotificationRemoveTask extends TimerTask {

	private JComponent entityToRemove;
	private DrawPanel diagram;
	
	public NotificationRemoveTask(JComponent entityToRemove, DrawPanel diagram){
		this.entityToRemove = entityToRemove;
		this.diagram = diagram;
	}
	
	@Override
	public void run() {
		Controller controller = diagram.getHandler().getController();
//		Vector<Entity> toDelete = new Vector<Entity>();
//		toDelete.add(entityToRemove);
//		Command removeNotificationEntity = new RemoveElement(toDelete);
//		Vector<Entity> selectedEntities = (Vector<Entity>) Umlet.getInstance().getDiagramHandler().getDrawPanel().getSelector().getSelectedEntities().clone();		
//		controller.executeCommand(removeNotificationEntity);
//		for(Entity e : selectedEntities){
//			diagram.getHandler().getDrawPanel().getSelector().select(e);
//		}		
		diagram.getHandler().getDrawPanel().remove(entityToRemove);
		diagram.getHandler().getDrawPanel().repaint();
	}

}
