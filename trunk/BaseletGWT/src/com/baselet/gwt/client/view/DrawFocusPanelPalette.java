package com.baselet.gwt.client.view;

import java.util.Arrays;
import java.util.List;

import com.baselet.element.GridElement;
import com.baselet.gwt.client.OwnXMLParser;
import com.baselet.gwt.client.element.Diagram;
import com.baselet.gwt.client.view.widgets.PropertiesTextArea;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;

public class DrawFocusPanelPalette extends DrawFocusPanel {

	private static final List<Diagram> PALETTELIST = Arrays.asList(
			OwnXMLParser.xmlToDiagram("<diagram program=\"umlet\" version=\"12.1\">&#10;  <zoom_level>8</zoom_level>&#10;  <element><id>UMLClass</id>&#10;    <coordinates><x>10</x><y>90</y><w>320</w><h>50</h></coordinates>&#10;    <panel_attributes>This class has the setting&#10;*elementstyle=autoresize*&#10;--&#10;Write text and watch how the class automatically grows/shrinks&#10;elementstyle=autoresize</panel_attributes><additional_attributes/>&#10;  </element>&#10;  <element><id>UMLUseCase</id>&#10;    <coordinates><x>10</x><y>310</y><w>170</w><h>100</h></coordinates>&#10;    <panel_attributes>This usecase has&#10;custom colors and linetype&#10;--&#10;*fg=#5c2b00*&#10;*bg=orange*&#10;*lt=.*&#10;fg=#5c2b00&#10;bg=orange&#10;lt=.</panel_attributes><additional_attributes/>&#10;  </element>&#10;  <element><id>UMLClass</id>&#10;    <coordinates><x>10</x><y>160</y><w>170</w><h>130</h></coordinates>&#10;    <panel_attributes>This class has the setting&#10;*elementstyle=wordwrap*&#10;--&#10;Write text and watch how the linebreak is added automatically at the expected position to fill the whole class.&#10;&#10;You can also resize the class and see that the text will always fit the border&#10;elementstyle=wordwrap</panel_attributes><additional_attributes/>&#10;  </element>&#10;  <element><id>UMLClass</id>&#10;    <coordinates><x>10</x><y>10</y><w>320</w><h>70</h></coordinates>&#10;    <panel_attributes>This palette contains the new grid elements UMLet offers since v12 (at the moment Class and UseCase).&#10;&#10;Press Ctrl+Space to open the possible settings to customize your elements as you can see below.&#10;elementstyle=wordwrap</panel_attributes><additional_attributes/>&#10;  </element>&#10;  <element><id>UMLUseCase</id>&#10;    <coordinates><x>190</x><y>310</y><w>150</w><h>100</h></coordinates>&#10;    <panel_attributes>this usecase has&#10;*halign=left*&#10;--&#10;As you can see the&#10;text is always within the&#10;usecase circle&#10;&#10;halign=LEFT</panel_attributes><additional_attributes/>&#10;  </element>&#10;  <element><id>UMLClass</id>&#10;    <coordinates><x>190</x><y>160</y><w>150</w><h>130</h></coordinates>&#10;    <panel_attributes>This class has&#10;the settings&#10;*valign=center*&#10;*halign=center*&#10;*fontsize=18*&#10;*lth=2.5*&#10;valign=center&#10;halign=center&#10;fontsize=18&#10;lth=2.5</panel_attributes><additional_attributes/>&#10;  </element>&#10;  <element><id>UMLInterface</id>&#10;    <coordinates><x>20</x><y>420</y><w>70</w><h>70</h></coordinates>&#10;    <panel_attributes>Interface&#10;--&#10;Operation1&#10;Operation2</panel_attributes><additional_attributes/>&#10;  </element>&#10;  <element><id>UMLActor</id>&#10;    <coordinates><x>90</x><y>420</y><w>60</w><h>130</h></coordinates>&#10;    <panel_attributes>Large&#10;Actor&#10;lt=..&#10;fg=gray&#10;fontsize=20</panel_attributes><additional_attributes/>&#10;  </element>&#10;  <element><id>UMLActor</id>&#10;    <coordinates><x>150</x><y>420</y><w>40</w><h>80</h></coordinates>&#10;    <panel_attributes>Actor2&#10;bg=red</panel_attributes><additional_attributes/>&#10;  </element>&#10;<element><id>Relation</id><coordinates><x>190</x><y>380</y><w>180</w><h>120</h></coordinates><panel_attributes>l=&gt;&#10;r=&lt;</panel_attributes><additional_attributes>20;60;80;60;120;20</additional_attributes></element><element><id>Text</id><coordinates><x>210</x><y>480</y><w>150</w><h>30</h></coordinates><panel_attributes>this is a text element</panel_attributes></element></diagram>"),
			OwnXMLParser.xmlToDiagram("<diagram program=\"umlet_web\" version=\"12.2\"><zoom_level>10</zoom_level><element><id>Relation</id><coordinates><x>10</x><y>10</y><w>80</w><h>20</h></coordinates><panel_attributes>l=&gt;&#10;r=&lt;</panel_attributes><additional_attributes>10.0;10.0;70.0;10.0</additional_attributes></element><element><id>Relation</id><coordinates><x>10</x><y>40</y><w>80</w><h>20</h></coordinates><panel_attributes>l=&lt;&#10;r=&gt;&#10;starttext=text&#10;endtext=text&#10;</panel_attributes><additional_attributes>10.0;10.0;70.0;10.0</additional_attributes></element><element><id>Relation</id><coordinates><x>10</x><y>70</y><w>80</w><h>20</h></coordinates><panel_attributes>l=&gt;&gt;&#10;r=&gt;&gt;</panel_attributes><additional_attributes>10.0;10.0;70.0;10.0</additional_attributes></element><element><id>Relation</id><coordinates><x>10</x><y>90</y><w>80</w><h>20</h></coordinates><panel_attributes>l=&gt;&gt;&gt;&#10;r=&gt;&gt;&gt;</panel_attributes><additional_attributes>10.0;10.0;70.0;10.0</additional_attributes></element></diagram>")
			);

	public DrawFocusPanelPalette(MainView mainView, PropertiesTextArea propertiesPanel, final ListBox paletteChooser) {
		super(mainView, propertiesPanel);
		this.setDiagram(PALETTELIST.get(0));

		paletteChooser.addItem("Default");
		paletteChooser.addItem("Arrows");
		paletteChooser.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				setDiagram(PALETTELIST.get(paletteChooser.getSelectedIndex()));
				selector.deselectAll();
			}
		});
	}

	@Override
	void doDoubleClickAction(GridElement ge) {
		otherDrawFocusPanel.setFocus(true);
		GridElement e = ge.CloneFromMe();
		commandInvoker.realignElementsToVisibleRect(otherDrawFocusPanel, Arrays.asList(e));
		commandInvoker.addElements(otherDrawFocusPanel, e);
	}

}
