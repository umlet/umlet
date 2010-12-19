package com.umlet.gui.base.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComboBox;

import com.umlet.control.Umlet;
import com.umlet.gui.standalone.StandaloneGUI;

public class PaletteComboBoxListener implements ActionListener, MouseWheelListener {

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JComboBox) {
			String paletteName = ((JComboBox) e.getSource()).getSelectedItem().toString();
			Umlet.getInstance().getGUI().selectPalette(paletteName);
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
				Umlet.getInstance().getGUI().selectPalette(newSelectedItem);
				comboBox.setSelectedIndex(newIndex);
				setZoom();
			}
		}
	}

	private void setZoom() {
		// after setting the palette the zoomvalue of the new palette must be set at the zoombox on top of the StandaloneGUI
		if ((Umlet.getInstance().getGUI() instanceof StandaloneGUI) && (Umlet.getInstance().getPalette() != null)) {
			int factor = Umlet.getInstance().getPalette().getGridSize();
			((StandaloneGUI) Umlet.getInstance().getGUI()).setValueOfZoomDisplay(factor);
		}
	}

}
