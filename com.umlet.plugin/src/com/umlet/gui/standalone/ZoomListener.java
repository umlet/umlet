package com.umlet.gui.standalone;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.umlet.control.Umlet;

public class ZoomListener implements ActionListener {

	public void actionPerformed(ActionEvent arg0) {
		if ((Umlet.getInstance().getGUI() instanceof StandaloneGUI) && (Umlet.getInstance().getDiagramHandler() != null)) {
			String zoomFactor = ((StandaloneGUI) Umlet.getInstance().getGUI()).getValueOfZoomDisplay();
			zoomFactor = zoomFactor.substring(0, zoomFactor.length() - 2); // Cut the zoomvalue eg: "120%" to "12"

			Umlet.getInstance().getDiagramHandler().setGridAndZoom(Integer.parseInt(zoomFactor));
		}
	}

}
