package com.umlet.gui.standalone;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JMenuItem;

import com.umlet.control.Umlet;

public class PaletteMenuListener implements ActionListener {

	public PaletteMenuListener() {

	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		String paletteName = "";
		if (source instanceof JMenuItem) paletteName = ((JMenuItem) source).getText();
		if (source instanceof JComboBox) paletteName = ((JComboBox) source).getSelectedItem().toString();
		Umlet.getInstance().getGUI().selectPalette(paletteName);
	}

}
