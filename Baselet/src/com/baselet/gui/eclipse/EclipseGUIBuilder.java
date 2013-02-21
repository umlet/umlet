package com.baselet.gui.eclipse;

import java.awt.BorderLayout;
import java.awt.Panel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.baselet.control.Constants;
import com.baselet.gui.BaseGUIBuilder;
import com.baselet.gui.listener.GUIListener;

public class EclipseGUIBuilder extends BaseGUIBuilder {

	private JPanel contentPlaceHolder = new JPanel(new BorderLayout());

	public Panel initEclipseGui() {
		Panel embedded_panel = new Panel();
		embedded_panel.setLayout(new BorderLayout());
		embedded_panel.add(initBase(contentPlaceHolder, Constants.main_split_position));
		embedded_panel.addKeyListener(new GUIListener());

		getCustomHandler().getPanel().getTextPane().addFocusListener(new CustomCodePaneFocusListener());
		getPropertyTextPane().getTextComponent().addFocusListener(new TextPaneFocusListener());
		
		return embedded_panel;
	}
	
	public void setContent(JScrollPane scrollPane) {
		contentPlaceHolder.removeAll();
		contentPlaceHolder.add(scrollPane);
	}
	
}
