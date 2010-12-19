package com.umlet.gui.eclipse;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import com.umlet.constants.Constants;
import com.umlet.constants.Constants.UmletType;
import com.umlet.control.Umlet;
import com.umlet.gui.eclipse.EclipseGUI.Pane;

public class TextPaneFocusListener implements FocusListener {

	public void focusGained(FocusEvent e) {
		if (Constants.UMLETTYPE == UmletType.ECLIPSE_PLUGIN) ((EclipseGUI) Umlet.getInstance().getGUI()).setPaneFocused(Pane.PROPERTY);
	}

	public void focusLost(FocusEvent e) {
		if (Constants.UMLETTYPE == UmletType.ECLIPSE_PLUGIN) ((EclipseGUI) Umlet.getInstance().getGUI()).setPaneFocused(Pane.DIAGRAM);
	}

}
