package com.baselet.gui.listener;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;

import com.baselet.control.Main;
import com.baselet.control.Utils;

public class PaletteComboBoxListener implements SelectionListener, MouseWheelListener {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	@Override
	public void mouseScrolled(MouseEvent e) {
		log.debug("Event: mouseScrolled");
		
		if (e.getSource() instanceof Combo) {
			Combo comboBox = ((Combo) e.getSource());
			int newIndex = comboBox.getSelectionIndex(); 
			if (comboBox.getItem(newIndex) != null) {
				String newSelectedItem = comboBox.getItem(newIndex).toString();
				//Main.getInstance().getGUI().selectPalette(newSelectedItem);
				setZoom();
			}
		}
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		log.debug("Event: widgetSelected");
		
		if (e.getSource() instanceof Combo) {
			int index = ((Combo)e.getSource()).getSelectionIndex();
			String paletteName = ((Combo) e.getSource()).getItem(index);
			//Main.getInstance().getGUI().selectPalette(paletteName);
			setZoom();
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		log.debug("Event: widgetDefaultSelected");
		// TODO Auto-generated method stub	
	}
	
	
	private void setZoom() {
		if ((Main.getInstance().getPalette() != null)) {
			int factor = Main.getInstance().getPalette().getGridSize();
			Main.getInstance().getGUI().setValueOfZoomDisplay(factor);
		}
	}
}
