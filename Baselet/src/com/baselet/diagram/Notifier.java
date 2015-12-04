package com.baselet.diagram;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import com.baselet.control.basics.Converter;

public class Notifier {

	private static final Notifier instance = new Notifier();

	public static synchronized Notifier getInstance() {
		return instance;
	}

	public void showInfo(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				showNotificationHelper(message, 4000, Color.BLACK, Color.BLUE);
			}
		});
	}

	public void showError(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				showNotificationHelper("ERROR: " + message, 10000, Color.RED, Color.RED);
			}
		});
	}

	private void showNotificationHelper(String message, int duration, Color textColor, Color backgroundColor) {
		final DrawPanel notifierPanel = CurrentDiagram.getInstance().getDiagramHandler().getDrawPanel();

		Rectangle viewRect = notifierPanel.getScrollPane().getViewport().getViewRect();
		final DiagramNotification notification = new DiagramNotification(Converter.convert(viewRect), message, textColor, backgroundColor);
		new Timer("Notificationtimer", true).schedule(new TimerTask() {
			@Override
			public void run() {
				notifierPanel.remove(notification);
				notifierPanel.repaint();
			}
		}, duration);

		notifierPanel.setNotification(notification);
	}
}
