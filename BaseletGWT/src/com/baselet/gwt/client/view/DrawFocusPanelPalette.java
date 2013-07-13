package com.baselet.gwt.client.view;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.NewGridElementConstants;
import com.baselet.diagram.commandnew.CanAddAndRemoveGridElement;
import com.baselet.element.GridElement;
import com.baselet.gwt.client.OwnXMLParser;
import com.baselet.gwt.client.view.widgets.PropertiesTextArea;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;

public class DrawFocusPanelPalette extends DrawFocusPanel {

	private static final List<List<GridElement>> PALETTELIST = Arrays.asList(
			OwnXMLParser.xmlToGridElements("<diagram program=\"umlet\" version=\"12.1\">&#10;  <zoom_level>8</zoom_level>&#10;  <element>&#10;    <id>UMLClass</id>&#10;    <coordinates>&#10;      <x>8</x>&#10;      <y>88</y>&#10;      <w>320</w>&#10;      <h>52</h>&#10;    </coordinates>&#10;    <panel_attributes>This class has the setting&#10;*elementstyle=autoresize*&#10;--&#10;Write text and watch how the class automatically grows/shrinks&#10;elementstyle=autoresize</panel_attributes>&#10;    <additional_attributes/>&#10;  </element>&#10;  <element>&#10;    <id>UMLUseCase</id>&#10;    <coordinates>&#10;      <x>8</x>&#10;      <y>304</y>&#10;      <w>168</w>&#10;      <h>96</h>&#10;    </coordinates>&#10;    <panel_attributes>This usecase has&#10;custom colors and linetype&#10;--&#10;*fg=#5c2b00*&#10;*bg=orange*&#10;*lt=.*&#10;fg=#5c2b00&#10;bg=orange&#10;lt=.</panel_attributes>&#10;    <additional_attributes/>&#10;  </element>&#10;  <element>&#10;    <id>UMLClass</id>&#10;    <coordinates>&#10;      <x>8</x>&#10;      <y>160</y>&#10;      <w>168</w>&#10;      <h>128</h>&#10;    </coordinates>&#10;    <panel_attributes>This class has the setting&#10;*elementstyle=wordwrap*&#10;--&#10;Write text and watch how the linebreak is added automatically at the expected position to fill the whole class.&#10;&#10;You can also resize the class and see that the text will always fit the border&#10;elementstyle=wordwrap</panel_attributes>&#10;    <additional_attributes/>&#10;  </element>&#10;  <element>&#10;    <id>UMLClass</id>&#10;    <coordinates>&#10;      <x>8</x>&#10;      <y>8</y>&#10;      <w>320</w>&#10;      <h>64</h>&#10;    </coordinates>&#10;    <panel_attributes>This palette contains the new grid elements UMLet offers since v12 (at the moment Class and UseCase).&#10;&#10;Press Ctrl+Space to open the possible settings to customize your elements as you can see below.&#10;elementstyle=wordwrap</panel_attributes>&#10;    <additional_attributes/>&#10;  </element>&#10;  <element>&#10;    <id>UMLUseCase</id>&#10;    <coordinates>&#10;      <x>184</x>&#10;      <y>304</y>&#10;      <w>144</w>&#10;      <h>96</h>&#10;    </coordinates>&#10;    <panel_attributes>this usecase has&#10;*halign=left*&#10;--&#10;As you can see the&#10;text is always within the&#10;usecase circle&#10;&#10;halign=LEFT</panel_attributes>&#10;    <additional_attributes/>&#10;  </element>&#10;  <element>&#10;    <id>UMLClass</id>&#10;    <coordinates>&#10;      <x>184</x>&#10;      <y>160</y>&#10;      <w>144</w>&#10;      <h>128</h>&#10;    </coordinates>&#10;    <panel_attributes>This class has&#10;the settings&#10;*valign=center*&#10;*halign=center*&#10;*fontsize=18*&#10;*lth=2.5*&#10;valign=center&#10;halign=center&#10;fontsize=18&#10;lth=2.5</panel_attributes>&#10;    <additional_attributes/>&#10;  </element>&#10;  <element>&#10;    <id>UMLInterface</id>&#10;    <coordinates>&#10;      <x>16</x>&#10;      <y>416</y>&#10;      <w>64</w>&#10;      <h>72</h>&#10;    </coordinates>&#10;    <panel_attributes>Interface&#10;--&#10;Operation1&#10;Operation2</panel_attributes>&#10;    <additional_attributes/>&#10;  </element>&#10;  <element>&#10;    <id>UMLActor</id>&#10;    <coordinates>&#10;      <x>88</x>&#10;      <y>416</y>&#10;      <w>56</w>&#10;      <h>128</h>&#10;    </coordinates>&#10;    <panel_attributes>Large&#10;Actor&#10;lt=..&#10;fg=gray&#10;fontsize=20</panel_attributes>&#10;    <additional_attributes/>&#10;  </element>&#10;  <element>&#10;    <id>UMLActor</id>&#10;    <coordinates>&#10;      <x>152</x>&#10;      <y>416</y>&#10;      <w>40</w>&#10;      <h>80</h>&#10;    </coordinates>&#10;    <panel_attributes>Actor2&#10;bg=red</panel_attributes>&#10;    <additional_attributes/>&#10;  </element>&#10;<element><id>Relation</id><coordinates><x>190</x><y>380</y><w>180</w><h>120</h></coordinates><panel_attributes>start=&gt;&#10;end=&lt;</panel_attributes><additional_attributes>20;60;80;60;120;20</additional_attributes></element><element><id>Text</id><coordinates><x>210</x><y>480</y><w>150</w><h>30</h></coordinates><panel_attributes>this is a text element</panel_attributes></element></diagram>"),
			OwnXMLParser.xmlToGridElements("<diagram program=\"umlet_web\" version=\"12.2\"><zoom_level>10</zoom_level><element><id>Relation</id><coordinates><x>10</x><y>10</y><w>80</w><h>20</h></coordinates><panel_attributes>start=&gt;&#10;end=&lt;</panel_attributes><additional_attributes>10.0;10.0;70.0;10.0</additional_attributes></element><element><id>Relation</id><coordinates><x>10</x><y>40</y><w>80</w><h>20</h></coordinates><panel_attributes>start=&lt;&#10;end=&gt;&#10;starttext=text&#10;endtext=text&#10;</panel_attributes><additional_attributes>10.0;10.0;70.0;10.0</additional_attributes></element><element><id>Relation</id><coordinates><x>10</x><y>70</y><w>80</w><h>20</h></coordinates><panel_attributes>start=&gt;&gt;&#10;end=&gt;&gt;</panel_attributes><additional_attributes>10.0;10.0;70.0;10.0</additional_attributes></element><element><id>Relation</id><coordinates><x>10</x><y>90</y><w>80</w><h>20</h></coordinates><panel_attributes>start=&gt;&gt;&gt;&#10;end=&gt;&gt;&gt;</panel_attributes><additional_attributes>10.0;10.0;70.0;10.0</additional_attributes></element></diagram>")
			);

	private CanAddAndRemoveGridElement doubleClickTarget;

	public DrawFocusPanelPalette(MainView mainView, PropertiesTextArea propertiesPanel, final ListBox paletteChooser, CanAddAndRemoveGridElement doubleClickTarget) {
		super(mainView, propertiesPanel);
		this.doubleClickTarget = doubleClickTarget;
		this.setGridElements(PALETTELIST.get(0));

		paletteChooser.addItem("Default");
		paletteChooser.addItem("Arrows");
		paletteChooser.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				setGridElements(PALETTELIST.get(paletteChooser.getSelectedIndex()));
				selector.deselectAll();
			}
		});
	}

	@Override
	void doDoubleClickAction(GridElement ge) {
		GridElement e = ge.CloneFromMe();
		e.setLocation(NewGridElementConstants.DEFAULT_GRID_SIZE, NewGridElementConstants.DEFAULT_GRID_SIZE);
		commandInvoker.addElements(doubleClickTarget, e);
	}

}
