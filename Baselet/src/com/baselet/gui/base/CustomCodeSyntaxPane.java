package com.baselet.gui.base;

import javax.swing.JPanel;
import javax.swing.JToolTip;

import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.util.Configuration;

import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.ProgramName;
import com.plotlet.gui.PlotletSyntaxKit;
import com.umlet.gui.UmletSyntaxKit;


@SuppressWarnings("serial")
public class CustomCodeSyntaxPane extends OwnSyntaxPane {

	private JToolTip tooltip;

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
}
