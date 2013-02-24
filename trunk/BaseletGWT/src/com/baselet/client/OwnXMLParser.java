package com.baselet.client;

import java.util.ArrayList;
import java.util.List;

import com.baselet.client.element.CanvasWrapperGWT;
import com.baselet.client.element.GridElement;
import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class OwnXMLParser {

	public static List<GridElement> parse(String xml) {
		List<GridElement> returnList = new ArrayList<GridElement>();
		  try {
			    // parse the XML document into a DOM
			    Document messageDom = XMLParser.parse(xml);

			    NodeList elements = messageDom.getElementsByTagName("element");
			    for (int i = 0; i < elements.getLength(); i++) {
					Element element = (Element) elements.item(i);
					String id = element.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
					Element coordinates = (Element) element.getElementsByTagName("coordinates").item(0);
					int x = Integer.valueOf(coordinates.getElementsByTagName("x").item(0).getFirstChild().getNodeValue());
					int y = Integer.valueOf(coordinates.getElementsByTagName("y").item(0).getFirstChild().getNodeValue());
					int w = Integer.valueOf(coordinates.getElementsByTagName("w").item(0).getFirstChild().getNodeValue());
					int h = Integer.valueOf(coordinates.getElementsByTagName("h").item(0).getFirstChild().getNodeValue());
					System.out.println("ID " + id);
					returnList.add(new GridElement(new Rectangle(x, y, w, h), DrawPanelCanvas.BLUE, new CanvasWrapperGWT()));
				}
			  } catch (DOMException e) {
			    Window.alert("Could not parse XML document.");
			  }
		  return returnList;
	}
	
//	<?xml version="1.0" encoding="UTF-8" standalone="no"?>
//	<diagram program="umlet" version="12.1">
//	  <zoom_level>10</zoom_level>
//	  <element>
//	    <id>UMLClass</id>
//	    <coordinates>
//	      <x>240</x>
//	      <y>180</y>
//	      <w>180</w>
//	      <h>90</h>
//	    </coordinates>
//	    <panel_attributes>This palette contains the new grid elements UMLet offers since v12 (at the moment Class and UseCase).
//
//	Press Ctrl+Space to open the possible settings to customize your elements as you can see below.
//	elementstyle=wordwrap</panel_attributes>
//	    <additional_attributes/>
//	  </element>
}
