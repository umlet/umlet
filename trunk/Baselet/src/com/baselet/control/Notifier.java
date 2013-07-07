package com.baselet.control;

import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.draw.swing.Converter;

public class Notifier {

	private static final Notifier instance = new Notifier();

	public static synchronized Notifier getInstance() {
		return instance;
	}

	public void showNotification(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				showNotificationHelper(message);
			}
		});}


	private void showNotificationHelper(String message) {
		final DrawPanel notifierPanel = Main.getInstance().getDiagramHandler().getDrawPanel();

		Rectangle viewRect = notifierPanel.getScrollPane().getViewport().getViewRect();
		final DiagramNotification notification = new DiagramNotification(Converter.convert(viewRect), message);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				notifierPanel.remove(notification);
				notifierPanel.repaint();
			}
		}, Constants.NOTIFICATION_SHOW_TIME);

		notifierPanel.setNotification(notification);
	}
}
