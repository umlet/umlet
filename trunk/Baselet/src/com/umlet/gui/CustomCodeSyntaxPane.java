package com.umlet.gui;

import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JToolTip;

import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.util.Configuration;

import com.baselet.gui.JMultiLineToolTip;
import com.baselet.gui.OwnSyntaxPane;

@SuppressWarnings("serial")
public class CustomCodeSyntaxPane extends OwnSyntaxPane {

	private JToolTip tooltip;
	private CustomCodePanelListener listener;

	public CustomCodeSyntaxPane(JPanel panel) {
		super(panel);
	}

	@Override
	public JToolTip createToolTip() {
		tooltip = new JMultiLineToolTip();
		return tooltip;
	}

	public JToolTip getToolTip() {
		return this.tooltip;
	}
	
	@Override
	public void initJSyntaxPane() {
		DefaultSyntaxKit.initKit();		
		
		//removes the line numbering
		Configuration conf = DefaultSyntaxKit.getConfig(DefaultSyntaxKit.class);
		conf.remove("Components");
		
		Font font = this.getFont();
		this.setContentType("text/java");
		this.setFont(font); //dont let jsyntaxpane overwrite the default font
		this.validate();
	}
	
	public void initCodePanelListener() {
		if (listener == null) {
			listener = new CustomCodePanelListener();
			this.getDocument().addUndoableEditListener(listener);
		}
	}
}
