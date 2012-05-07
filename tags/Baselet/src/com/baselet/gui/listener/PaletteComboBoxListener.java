package com.baselet.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComboBox;

import com.baselet.control.Main;


public class PaletteComboBoxListener implements ActionListener, MouseWheelListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JComboBox) {
			String paletteName = ((JComboBox) e.getSource()).getSelectedItem().toString();
			Main.getInstance().getGUI().selectPalette(paletteName);
			setZoom();
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getSource() instanceof JComboBox) {
			JComboBox comboBox = ((JComboBox) e.getSource());
			int newIndex = comboBox.getSelectedIndex() + e.getWheelRotation(); // wheelrotation is -1 (up) or +1 (down)
			if (comboBox.getItemAt(newIndex) != null) {
				String newSelectedItem = comboBox.getItemAt(newIndex).toString();
				Main.getInstance().getGUI().selectPalette(newSelectedItem);
				comboBox.setSelectedIndex(newIndex);
				setZoom();
			}
		}
	}

	private void setZoom() {
		if ((Main.getInstance().getPalette() != null)) {
			int factor = Main.getInstance().getPalette().getGridSize();
			Main.getInstance().getGUI().setValueOfZoomDisplay(factor);
		}
	}

}
