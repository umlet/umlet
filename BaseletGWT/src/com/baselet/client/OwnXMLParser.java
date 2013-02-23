package com.baselet.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class OwnXMLParser {

	public static void parse(String xml) {
		  try {
			    // parse the XML document into a DOM
			    Document messageDom = XMLParser.parse(xml);

			    NodeList elements = messageDom.getElementsByTagName("element");
			    for (int i = 0; i < elements.getLength(); i++) {
					Element element = (Element) elements.item(i);
					String id = element.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
					Element coordinates = (Element) element.getElementsByTagName("coordinates").item(0);
					String x = coordinates.getElementsByTagName("x").item(0).getFirstChild().getNodeValue();
					String y = coordinates.getElementsByTagName("y").item(0).getFirstChild().getNodeValue();
					String w = coordinates.getElementsByTagName("w").item(0).getFirstChild().getNodeValue();
					String h = coordinates.getElementsByTagName("h").item(0).getFirstChild().getNodeValue();
					
					System.out.println(id + " / " + x + " / " + y + " / " + w + " / " + h);
				}
			  } catch (DOMException e) {
			    Window.alert("Could not parse XML document.");
			  }

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
