package com.baselet.gwt.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.control.SharedConstants;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.baselet.elementnew.ElementId;
import com.baselet.gwt.client.element.Diagram;
import com.baselet.gwt.client.element.ElementFactory;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class DiagramXmlParser {

	private static final String NUMBER_SIGN = "#"; // # is not automatically encoded by URL.encode() and URL.decode()
	private static final String NUMBER_SIGN_URL_ENCODED = "%23";
	private static final String GT = ">";
	private static final String GT_ENCODED = "&gt;"; // in some cases the xml parser doesn't convert automatically (especially together with URL.encoded strings) therefore replace manually
	private static final String LT = "<";
	private static final String LT_ENCODED = "&lt;";

	private static final Logger log = Logger.getLogger(DiagramXmlParser.class);

	private static final String DIAGRAM = "diagram";
	private static final String ELEMENT = "element";
	private static final String ZOOM_LEVEL = "zoom_level";
	private static final String HELP_TEXT = "help_text";
	private static final String ID = "id";
	private static final String COORDINATES = "coordinates";
	private static final String X = "x";
	private static final String Y = "y";
	private static final String W = "w";
	private static final String H = "h";
	private static final String PANEL_ATTRIBUTES = "panel_attributes";
	private static final String ADDITIONAL_ATTRIBUTES = "additional_attributes";
	private static final String ATTR_PROGRAM = "program";
	private static final String ATTR_VERSION = "version";

	public static Diagram xmlToDiagram(boolean decodeUrl, String xml) {
		if (decodeUrl) {
			xml = URL.decode(xml).replace(NUMBER_SIGN_URL_ENCODED, NUMBER_SIGN);
		}
		return xmlToDiagram(xml);
	}

	public static Diagram xmlToDiagram(String xml) {
		if (xml.startsWith(SharedConstants.UTF8_BOM)) {
			xml = xml.substring(1); // remove BOM if it is given
		}
		Diagram diagram = null;
		String helpText = null;
		try {
			// parse the XML document into a DOM
			Document messageDom = XMLParser.parse(xml);

			Node helpTextNode = messageDom.getElementsByTagName(HELP_TEXT).item(0);
			if (helpTextNode != null) {
				helpText = helpTextNode.getFirstChild().getNodeValue();
			}

			float zoomScale = 1.0f;
			Node zoomElement = messageDom.getElementsByTagName(ZOOM_LEVEL).item(0);
			if (zoomElement != null) {
				zoomScale = Float.valueOf(zoomElement.getFirstChild().getNodeValue()) / SharedConstants.DEFAULT_GRID_SIZE;
			}

			diagram = new Diagram(helpText, new ArrayList<GridElement>());
			NodeList elements = messageDom.getElementsByTagName(ELEMENT);
			for (int i = 0; i < elements.getLength(); i++) {
				Element element = (Element) elements.item(i);
				try {
					ElementId id = ElementId.valueOf(element.getElementsByTagName(ID).item(0).getFirstChild().getNodeValue());
					Element coord = (Element) element.getElementsByTagName(COORDINATES).item(0);
					Rectangle rect = new Rectangle(getInt(coord, X), getInt(coord, Y), getInt(coord, W), getInt(coord, H));

					String panelAttributes = "";
					Node panelAttrNode = element.getElementsByTagName(PANEL_ATTRIBUTES).item(0).getFirstChild();
					if (panelAttrNode != null) {
						panelAttributes = panelAttrNode.getNodeValue().replace(LT_ENCODED, LT).replace(GT_ENCODED, GT);
					}

					String additionalPanelAttributes = "";
					Node additionalAttrNode = element.getElementsByTagName(ADDITIONAL_ATTRIBUTES).item(0);
					if (additionalAttrNode != null && additionalAttrNode.getFirstChild() != null) {
						additionalPanelAttributes = additionalAttrNode.getFirstChild().getNodeValue();
					}
					if (zoomScale != 1.0f) {
						rect.setX((int) (rect.getX() / zoomScale));
						rect.setY((int) (rect.getY() / zoomScale));
						rect.setWidth((int) (rect.getWidth() / zoomScale));
						rect.setHeight((int) (rect.getHeight() / zoomScale));
					}
					GridElement gridElement = ElementFactory.create(id, rect, panelAttributes, additionalPanelAttributes, diagram);
					diagram.getGridElements().add(gridElement);
				} catch (Exception e) {
					log.error("Element has invalid XML structure: " + element, e);
					Notification.showFeatureNotSupported("Diagram has invalid element: " + element, true);
				}
			}
		} catch (DOMException e) {
			log.error("Parsing error", e);
			Window.alert("Could not parse XML document.");
		}
		return diagram;
	}

	private static Integer getInt(Element coordinates, String tag) {
		return Integer.valueOf(coordinates.getElementsByTagName(tag).item(0).getFirstChild().getNodeValue());
	}

	public static String diagramToXml(Diagram diagram) {
		Document doc = XMLParser.createDocument();

		Element diagramElement = doc.createElement(DIAGRAM);
		diagramElement.setAttribute(ATTR_PROGRAM, SharedConstants.program);
		diagramElement.setAttribute(ATTR_VERSION, SharedConstants.VERSION);
		diagramElement.appendChild(create(doc, ZOOM_LEVEL, doc.createTextNode("10")));
		String helpText = diagram.getPanelAttributes();
		if (helpText != null) {
			diagramElement.appendChild(create(doc, HELP_TEXT, doc.createTextNode(helpText)));
		}
		doc.appendChild(diagramElement);

		for (GridElement ge : diagram.getGridElements()) {
			diagramElement.appendChild(
					create(doc, ELEMENT,
							create(doc, ID, doc.createTextNode(ge.getId().toString())),
							create(doc, COORDINATES,
									create(doc, X, doc.createTextNode(ge.getRectangle().getX() + "")),
									create(doc, Y, doc.createTextNode(ge.getRectangle().getY() + "")),
									create(doc, W, doc.createTextNode(ge.getRectangle().getWidth() + "")),
									create(doc, H, doc.createTextNode(ge.getRectangle().getHeight() + ""))),
							create(doc, PANEL_ATTRIBUTES, doc.createTextNode(ge.getPanelAttributes().replace(LT, LT_ENCODED).replace(GT, GT_ENCODED))),
							create(doc, ADDITIONAL_ATTRIBUTES, doc.createTextNode(ge.getAdditionalAttributes()))
					));
		}
		return doc.toString();
	}

	public static String diagramToXml(boolean encodeUrl, Diagram diagram) {
		String xml = diagramToXml(diagram);
		if (encodeUrl) {
			xml = URL.encode(xml).replace(NUMBER_SIGN, NUMBER_SIGN_URL_ENCODED);
		}
		return xml;
	}

	private static Element create(Document doc, String name, Node... children) {
		Element element = doc.createElement(name);
		for (Node c : children) {
			element.appendChild(c);
		}
		return element;
	}

	public static String gridElementsToXml(List<GridElement> gridElements) {
		return diagramToXml(new Diagram(gridElements));
	}

	public static List<GridElement> xmlToGridElements(String string) {
		return xmlToDiagram(string).getGridElements();
	}

}
