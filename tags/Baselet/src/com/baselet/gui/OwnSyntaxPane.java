package com.baselet.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.text.PlainDocument;

import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.util.Configuration;

import org.apache.log4j.Logger;

import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.ProgramName;
import com.baselet.control.Utils;
import com.plotlet.gui.PlotletSyntaxKit;
import com.umlet.gui.UmletSyntaxKit;


@SuppressWarnings("serial")
public class OwnSyntaxPane extends JEditorPane {

	private final static Logger log = Logger.getLogger(Utils.getClassName());
	private JPanel panel;

	public OwnSyntaxPane(JPanel panel) {
		this.panel = panel;
		this.setBackground(Color.WHITE);
		
		//AB: Reduce tab size (works only if document type is plain)
		this.getDocument().putProperty(PlainDocument.tabSizeAttribute, 3);
	}

	@Override
	public void setSize(Dimension d) {
		if (d.width < getParent().getSize().width) d.width = getParent().getSize().width;

		super.setSize(d);
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public JPanel getPanel() {
		return this.panel;
	}

	public void initJSyntaxPane() {
		DefaultSyntaxKit.initKit();
		Configuration conf = DefaultSyntaxKit.getConfig(DefaultSyntaxKit.class);
		
		//remove built-in undo/redo
		conf.remove("Action.undo");
		conf.remove("Action.redo");
		
		if (Program.PROGRAM_NAME == ProgramName.PLOTLET) {
			log.info("Register PlotletSyntaxKit");
			
			try {
				// IMPORTANT: The config-key "Action.combo-completion.Items" only accepts a semikolon-separated string because we have changed the method:
				//            jsyntaxpane/actions/ComboCompletionAction.java#setItems(). Otherwise it would only accept a real list
				String autocompletionList = PlotletSyntaxKit.createAutocompletionList(";");
				DefaultSyntaxKit.getConfig(PlotletSyntaxKit.class).put("Action.combo-completion.ItemsAsString", autocompletionList);
			} catch (Exception e) {
				log.error("Error at creating the autocompletion");
			}
			DefaultSyntaxKit.registerContentType("text/propertypanel", PlotletSyntaxKit.class.getCanonicalName());
		} else {
			log.info("Register UmletSyntaxKit");
			
			//removes the line numbering			
			conf.remove("Components");
			
			DefaultSyntaxKit.registerContentType("text/propertypanel", UmletSyntaxKit.class.getCanonicalName());
		}	
		
		this.setContentType("text/propertypanel");
		this.validate();
	}
}
