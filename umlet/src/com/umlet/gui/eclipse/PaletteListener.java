package com.umlet.gui.eclipse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import com.umlet.control.Umlet;

public class PaletteListener implements ActionListener {

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		String paletteName = "";
		if (source instanceof JComboBox) paletteName = ((JComboBox) source).getSelectedItem().toString();
		Umlet.getInstance().getGUI().selectPalette(paletteName);
	}

}
