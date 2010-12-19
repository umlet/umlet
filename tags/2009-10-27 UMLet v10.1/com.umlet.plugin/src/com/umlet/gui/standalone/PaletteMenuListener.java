package com.umlet.gui.standalone;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.umlet.control.Umlet;

public class PaletteMenuListener implements ActionListener {

	public PaletteMenuListener() {

	}

	public void actionPerformed(ActionEvent e) {

		Umlet.getInstance().getGUI().selectPalette(((JMenuItem) e.getSource()).getText());
	}

}
