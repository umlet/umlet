package com.baselet.control;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import com.baselet.diagram.DrawPanel;

public class Notifier {

	DrawPanel notifierPanel;

	private DiagramNotification notification;
	private TimerTask notificationRemoveTask;
	
	private static final Notifier instance = new Notifier();
	
	public synchronized static Notifier getInstance() {
		return instance;
	}


	public void setNotificationPanel(DrawPanel notifierPanel) {
		this.notifierPanel = notifierPanel;
	}


	public void showNotification(final String message) {
	SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run() {
			showNotificationHelper(message);
		}
	});}


	private void showNotificationHelper(String message) {
		if (notification != null) {
			notifierPanel.remove(notification);
				if (notificationRemoveTask != null) notificationRemoveTask.cancel();
		}

		notification = new DiagramNotification(notifierPanel.getScrollPane().getViewport().getViewRect(), message);
		notificationRemoveTask = new TimerTask() {
			@Override
			public void run() {
				if (notification != null) {
					notifierPanel.remove(notification);
					notifierPanel.repaint();
				}
			}
		};
		new Timer().schedule(notificationRemoveTask, Constants.NOTIFICATION_SHOW_TIME);
		
		notifierPanel.add(notification);
		notifierPanel.repaint();
	}
}
