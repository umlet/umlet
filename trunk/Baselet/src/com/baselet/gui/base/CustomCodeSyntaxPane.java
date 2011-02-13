package com.baselet.gui.base;

import javax.swing.JPanel;
import javax.swing.JToolTip;

import com.umlet.gui.CustomCodePanelListener;

import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.util.Configuration;

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
		
		this.setContentType("text/java");
		this.validate();
	}
	
	public void initCodePanelListener() {
		if (listener == null) {
			listener = new CustomCodePanelListener();
			this.getDocument().addUndoableEditListener(listener);
		}
	}
}
