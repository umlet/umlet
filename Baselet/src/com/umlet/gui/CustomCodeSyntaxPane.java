package com.umlet.gui;

import com.baselet.gui.JMultiLineToolTip;
import com.baselet.gui.OwnSyntaxPane;

@SuppressWarnings("serial")
public class CustomCodeSyntaxPane extends OwnSyntaxPane {

	//TODO refactor to a usable Java version of the syntaxpane
	
	public JMultiLineToolTip getToolTip() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setToolTipText(String text) {
		// TODO Auto-generated method stub
		
	}

//	private JToolTip tooltip;
//	private CustomCodePanelListener listener;
//
//	public CustomCodeSyntaxPane(JPanel panel) {
//		super(panel);
//	}
//
//	@Override
//	public JToolTip createToolTip() {
//		tooltip = new JMultiLineToolTip();
//		return tooltip;
//	}
//
//	public JToolTip getToolTip() {
//		return this.tooltip;
//	}
//	
//	@Override
//	public void initJSyntaxPane() {
//		DefaultSyntaxKit.initKit();		
//		
//		//removes the line numbering
//		Configuration conf = DefaultSyntaxKit.getConfig(DefaultSyntaxKit.class);
//		conf.remove("Components");
//		
//		this.setContentType("text/java");
//		this.setFont(Constants.PANEL_FONT); //Set font to make sure UTF-8 characters work
//		this.validate();
//	}
//	
//	public void initCodePanelListener() {
//		if (listener == null) {
//			listener = new CustomCodePanelListener();
//			this.getDocument().addUndoableEditListener(listener);
//		}
//	}
}
