package com.baselet.gui.standalone;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComboBox;

import com.baselet.control.Main;

public class ZoomListener implements ActionListener, MouseWheelListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		handleEvent(e);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		handleEvent(e);
	}

	private void handleEvent(AWTEvent e) {
		if ((Main.getInstance().getGUI() instanceof StandaloneGUI) && (Main.getInstance().getDiagramHandler() != null)) {
			// The offset is -1 (wheel up) or +1 (wheel down) or 0 (no wheel used)
			int offset = 0;
			if (e instanceof MouseWheelEvent) offset = ((MouseWheelEvent) e).getWheelRotation();

			String zoomFactor = ((JComboBox) e.getSource()).getSelectedItem().toString();
			zoomFactor = zoomFactor.substring(0, zoomFactor.length() - 2); // Cut the zoomvalue eg: "120%" to "12"
			int newZoomFactor = Integer.parseInt(zoomFactor) + offset;

			if ((newZoomFactor > 0) && (newZoomFactor < 21)) Main.getInstance().getDiagramHandler().setGridAndZoom(newZoomFactor);
		}
	}
}
