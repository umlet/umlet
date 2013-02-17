package com.baselet.gui.eclipse;

import java.awt.BorderLayout;
import java.awt.Panel;

import javax.swing.JPanel;

import com.baselet.control.Constants;
import com.baselet.diagram.DiagramHandler;
import com.baselet.gui.BaseGUIBuilder;
import com.baselet.gui.listener.GUIListener;

public class EclipseGUIBuilder extends BaseGUIBuilder {

	public Panel initEclipseGui(DiagramHandler handler) {
		JPanel editor = new JPanel();
		editor.setLayout(new BorderLayout());
		editor.add(initBase(handler.getDrawPanel().getScrollPane(), Constants.main_split_position));

		getCustomHandler().getPanel().getTextPane().addFocusListener(new CustomCodePaneFocusListener());
		getPropertyTextPane().getTextComponent().addFocusListener(new TextPaneFocusListener());

		Panel embedded_panel = new Panel();
		embedded_panel.setLayout(new BorderLayout());
		embedded_panel.add(initBase(handler.getDrawPanel().getScrollPane(), Constants.main_split_position));
		embedded_panel.addKeyListener(new GUIListener());
		return embedded_panel;
	}
}
