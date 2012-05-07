package com.baselet.diagram.io;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.command.HelpPanelChanged;
import com.baselet.element.ErrorOccurred;
import com.baselet.element.GridElement;
import com.baselet.element.Group;
import com.umlet.custom.CustomElementCompiler;

/**
 * Describes what should happen with parsed elements from the input file
 * eg: set DiagramHandler variables, create GridElements etc.
 */
public class InputHandler extends DefaultHandler {
	private DrawPanel _p = null;
	private GridElement e = null;
	private String elementtext;

	private int x;
	private int y;
	private int w;
	private int h;
	private String entityname;
	private String code;
	private String panel_attributes;
	private String additional_attributes;

	private Group currentGroup;
	private DiagramHandler handler;

	// to be backward compatible - add list of old elements that were removed so that they are ignored when loading old files
	private List<String> ignoreElements;

	public InputHandler(DiagramHandler handler) {
		this.handler = handler;
		_p = handler.getDrawPanel();
		ignoreElements = new ArrayList<String>();
		ignoreElements.add("main.control.Group");
		currentGroup = null;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		elementtext = "";
		if (qName.equals("element")) {
			this.panel_attributes = "";
			this.additional_attributes = "";
			this.code = null;
		}
		if (qName.equals("group")) {
			Group g = new Group();
			g.setHandler(this.handler);
			if (currentGroup != null) currentGroup.addMember(g);
			currentGroup = g;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		String elementname = qName; // [UB]: we are not name-space aware, so use the qualified name

		if (elementname.equals("help_text")) {
			handler.setHelpText(elementtext);
			handler.getFontHandler().setDiagramDefaultFontSize(HelpPanelChanged.getFontsize(elementtext));
		}
		else if (elementname.equals("zoom_level")) {
			if (handler != null) handler.setGridSize(Integer.parseInt(elementtext));
		}
		else if (elementname.equals("group")) {
			if (this.currentGroup != null) {
				this.currentGroup.adjustSize(false);
				_p.add(this.currentGroup);
				this.currentGroup = this.currentGroup.getGroup();
			}
		}
		else if (elementname.equals("element")) {
			if (!this.ignoreElements.contains(this.entityname)) { // load classes
				try {
					if (this.code == null) e =  Main.getInstance().getGridElementFromPath(this.entityname);
					else e = CustomElementCompiler.getInstance().genEntity(this.code);
				} catch (Exception ex) {
					e = new ErrorOccurred();
				}
				e.setBounds(x, y, w, h);
				e.setPanelAttributes(this.panel_attributes);
				e.setAdditionalAttributes(this.additional_attributes);
				e.setHandler(this.handler);

				if (this.currentGroup != null) this.currentGroup.addMember(e);
				_p.add(e);
			}
		}
		else if (elementname.equals("type")) {
			this.entityname = elementtext;
		}
		else if (elementname.equals("x")) {
			Integer i = new Integer(elementtext);
			x = i.intValue();
		}
		else if (elementname.equals("y")) {
			Integer i = new Integer(elementtext);
			y = i.intValue();
		}
		else if (elementname.equals("w")) {
			Integer i = new Integer(elementtext);
			w = i.intValue();
		}
		else if (elementname.equals("h")) {
			Integer i = new Integer(elementtext);
			h = i.intValue();
		}
		else if (elementname.equals("panel_attributes")) this.panel_attributes = elementtext;
		else if (elementname.equals("additional_attributes")) this.additional_attributes = elementtext;
		else if (elementname.equals("custom_code")) this.code = elementtext;
	}

	@Override
	public void characters(char[] ch, int start, int length) {
		elementtext += (new String(ch)).substring(start, start + length);
	}

}
