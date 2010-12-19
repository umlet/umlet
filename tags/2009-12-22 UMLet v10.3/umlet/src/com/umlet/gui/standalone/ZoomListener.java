package com.umlet.gui.standalone;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;

import org.apache.log4j.Logger;

import com.umlet.control.Umlet;
import com.umlet.control.command.AddEntity;
import com.umlet.control.command.Command;
import com.umlet.control.diagram.Controller;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.element.base.DiagramNotification;
import com.umlet.gui.base.NotificationRemoveTask;

public class ZoomListener implements ActionListener {

	private final static Logger log = Logger.getLogger(ZoomListener.class);


	public void actionPerformed(ActionEvent arg0) {
		if ((Umlet.getInstance().getGUI() instanceof StandaloneGUI) && (Umlet.getInstance().getDiagramHandler() != null)) {
			String zoomFactor = ((StandaloneGUI) Umlet.getInstance().getGUI()).getValueOfZoomDisplay();
			zoomFactor = zoomFactor.substring(0, zoomFactor.length() - 2); // Cut the zoomvalue eg: "120%" to "12"

			Umlet.getInstance().getDiagramHandler().setGridAndZoom(Integer.parseInt(zoomFactor));
			Umlet.getInstance().getDiagramHandler().getDrawPanel().showNotification("Zoom set to " + ((StandaloneGUI) Umlet.getInstance().getGUI()).getValueOfZoomDisplay());
		}
	}
	
}
