package com.umlet.gui.standalone;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComboBox;

import org.apache.log4j.Logger;

import com.umlet.control.Umlet;

public class ZoomListener implements ActionListener, MouseWheelListener {

	private final static Logger log = Logger.getLogger(ZoomListener.class);

	public void actionPerformed(ActionEvent e) {
		handleEvent(e);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		handleEvent(e);
	}

	private void handleEvent(AWTEvent e) {
		if ((Umlet.getInstance().getGUI() instanceof StandaloneGUI) && (Umlet.getInstance().getDiagramHandler() != null)) {
			// The offset is -1 (wheel up) or +1 (wheel down) or 0 (no wheel used)
			int offset = 0;
			if (e instanceof MouseWheelEvent) offset = ((MouseWheelEvent) e).getWheelRotation();

			String zoomFactor = ((JComboBox) e.getSource()).getSelectedItem().toString();
			zoomFactor = zoomFactor.substring(0, zoomFactor.length() - 2); // Cut the zoomvalue eg: "120%" to "12"
			int newZoomFactor = Integer.parseInt(zoomFactor) + offset;

			if ((newZoomFactor > 0) && (newZoomFactor < 21)) Umlet.getInstance().getDiagramHandler().setGridAndZoom(newZoomFactor);
		}
	}
}
