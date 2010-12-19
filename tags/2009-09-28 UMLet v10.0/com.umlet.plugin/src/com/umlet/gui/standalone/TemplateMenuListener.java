package com.umlet.gui.standalone;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.umlet.control.Umlet;

public class TemplateMenuListener implements ActionListener {

	public TemplateMenuListener() {

	}

	public void actionPerformed(ActionEvent e) {
		JMenuItem b = (JMenuItem) e.getSource();
		if (Umlet.getInstance().getGUI().getCurrentCustomHandler().closeEntity()) {
			Umlet.getInstance().getGUI().setCustomPanelEnabled(true);
			Umlet.getInstance().getGUI().getCurrentCustomHandler().getPanel().setCustomElementIsNew(true);
			Umlet.getInstance().getGUI().getCurrentCustomHandler().newEntity(b.getText());
		}
	}

}
