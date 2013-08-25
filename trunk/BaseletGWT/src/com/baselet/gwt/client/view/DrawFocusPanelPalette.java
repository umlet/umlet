package com.baselet.gwt.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.geom.Point;
import com.baselet.element.GridElement;
import com.baselet.gwt.client.OwnXMLParser;
import com.baselet.gwt.client.element.Diagram;
import com.baselet.gwt.client.element.ElementFactory;
import com.baselet.gwt.client.view.widgets.PropertiesTextArea;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;

public class DrawFocusPanelPalette extends DrawFocusPanel {

	private static final List<Diagram> PALETTELIST = Arrays.asList(
			OwnXMLParser.xmlToDiagram(true, "%3Cdiagram%20program=%22umlet_web%22%20version=%2212.2%22%3E%3Czoom_level%3E10%3C/zoom_level%3E%3Celement%3E%3Cid%3EUMLClass%3C/id%3E%3Ccoordinates%3E%3Cx%3E10%3C/x%3E%3Cy%3E160%3C/y%3E%3Cw%3E170%3C/w%3E%3Ch%3E130%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3EThis%20class%20has%20the%20setting%0A*elementstyle=wordwrap*%0A--%0AWrite%20text%20and%20watch%20how%20the%20linebreak%20is%20added%20automatically%20at%20the%20expected%20position%20to%20fill%20the%20whole%20class.%0A%0AYou%20can%20also%20resize%20the%20class%20and%20see%20that%20the%20text%20will%20always%20fit%20the%20border%0Aelementstyle=wordwrap%3C/panel_attributes%3E%3Cadditional_attributes%3E%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3EUMLClass%3C/id%3E%3Ccoordinates%3E%3Cx%3E10%3C/x%3E%3Cy%3E90%3C/y%3E%3Cw%3E350%3C/w%3E%3Ch%3E60%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3EThis%20class%20has%20the%20setting%0A*elementstyle=autoresize*%0A--%0AWrite%20text%20and%20watch%20how%20the%20class%20automatically%20grows/shrinks%0Aelementstyle=autoresize%3C/panel_attributes%3E%3Cadditional_attributes%3E%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3EUMLUseCase%3C/id%3E%3Ccoordinates%3E%3Cx%3E10%3C/x%3E%3Cy%3E310%3C/y%3E%3Cw%3E170%3C/w%3E%3Ch%3E100%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3EThis%20usecase%20has%0Acustom%20colors%20and%20linetype%0A--%0A*fg=%235c2b00*%0A*bg=orange*%0A*lt=.*%0Afg=%235c2b00%0Abg=orange%0Alt=.%3C/panel_attributes%3E%3Cadditional_attributes%3E%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3EUMLClass%3C/id%3E%3Ccoordinates%3E%3Cx%3E10%3C/x%3E%3Cy%3E10%3C/y%3E%3Cw%3E320%3C/w%3E%3Ch%3E70%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3EThis%20palette%20contains%20the%20new%20grid%20elements%20UMLet%20offers%20since%20v12%20(at%20the%20moment%20Class%20and%20UseCase).%0A%0APress%20Ctrl+Space%20to%20open%20the%20possible%20settings%20to%20customize%20your%20elements%20as%20you%20can%20see%20below.%0Aelementstyle=wordwrap%3C/panel_attributes%3E%3Cadditional_attributes%3E%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3EUMLUseCase%3C/id%3E%3Ccoordinates%3E%3Cx%3E190%3C/x%3E%3Cy%3E310%3C/y%3E%3Cw%3E150%3C/w%3E%3Ch%3E100%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3Ethis%20usecase%20has%0A*halign=left*%0A--%0AAs%20you%20can%20see%20the%0Atext%20is%20always%20within%20the%0Ausecase%20circle%0A%0Ahalign=LEFT%3C/panel_attributes%3E%3Cadditional_attributes%3E%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3EUMLClass%3C/id%3E%3Ccoordinates%3E%3Cx%3E190%3C/x%3E%3Cy%3E160%3C/y%3E%3Cw%3E150%3C/w%3E%3Ch%3E130%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3EThis%20class%20has%0Athe%20settings%0A*valign=center*%0A*halign=center*%0A*fontsize=18*%0A*lth=2.5*%0Avalign=center%0Ahalign=center%0Afontsize=18%0Alth=2.5%3C/panel_attributes%3E%3Cadditional_attributes%3E%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3EUMLActor%3C/id%3E%3Ccoordinates%3E%3Cx%3E90%3C/x%3E%3Cy%3E420%3C/y%3E%3Cw%3E70%3C/w%3E%3Ch%3E170%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3ELarge%0AActor%0Alt=..%0Afg=gray%0Afontsize=20%3C/panel_attributes%3E%3Cadditional_attributes%3E%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3EUMLActor%3C/id%3E%3Ccoordinates%3E%3Cx%3E150%3C/x%3E%3Cy%3E420%3C/y%3E%3Cw%3E50%3C/w%3E%3Ch%3E90%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3EActor2%0Abg=red%3C/panel_attributes%3E%3Cadditional_attributes%3E%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3ERelation%3C/id%3E%3Ccoordinates%3E%3Cx%3E190%3C/x%3E%3Cy%3E400%3C/y%3E%3Cw%3E180%3C/w%3E%3Ch%3E120%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3Elt=&amp;lt;&amp;gt;%3C/panel_attributes%3E%3Cadditional_attributes%3E20.0;60.0;80.0;60.0;120.0;20.0%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3EText%3C/id%3E%3Ccoordinates%3E%3Cx%3E210%3C/x%3E%3Cy%3E490%3C/y%3E%3Cw%3E150%3C/w%3E%3Ch%3E30%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3Ethis%20is%20a%20text%20element%3C/panel_attributes%3E%3Cadditional_attributes%3E%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3EUMLInterface%3C/id%3E%3Ccoordinates%3E%3Cx%3E10%3C/x%3E%3Cy%3E420%3C/y%3E%3Cw%3E80%3C/w%3E%3Ch%3E90%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3EInterface%0A--%0AOperation1%0AOperation2%3C/panel_attributes%3E%3Cadditional_attributes%3E%3C/additional_attributes%3E%3C/element%3E%3C/diagram%3E"),
			OwnXMLParser.xmlToDiagram(true, "%3Cdiagram%20program=%22umlet_web%22%20version=%2212.2%22%3E%3Czoom_level%3E10%3C/zoom_level%3E%3Celement%3E%3Cid%3ERelation%3C/id%3E%3Ccoordinates%3E%3Cx%3E10%3C/x%3E%3Cy%3E10%3C/y%3E%3Cw%3E160%3C/w%3E%3Ch%3E20%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3Elt=&amp;lt;-&amp;gt;%3C/panel_attributes%3E%3Cadditional_attributes%3E10.0;10.0;150.0;10.0%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3ERelation%3C/id%3E%3Ccoordinates%3E%3Cx%3E10%3C/x%3E%3Cy%3E40%3C/y%3E%3Cw%3E160%3C/w%3E%3Ch%3E22%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3Elt=&amp;lt;..&amp;gt;&amp;gt;%0Am1=text1%0Am2=text2%0Amm=middle%3C/panel_attributes%3E%3Cadditional_attributes%3E10.0;10.0;150.0;10.0%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3ERelation%3C/id%3E%3Ccoordinates%3E%3Cx%3E10%3C/x%3E%3Cy%3E70%3C/y%3E%3Cw%3E160%3C/w%3E%3Ch%3E20%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3Elt=&amp;lt;&amp;lt;.&amp;gt;&amp;gt;%3C/panel_attributes%3E%3Cadditional_attributes%3E10.0;10.0;150.0;10.0%3C/additional_attributes%3E%3C/element%3E%3C/diagram%3E")
			);

	private ListBox paletteChooser;

	public DrawFocusPanelPalette(MainView mainView, PropertiesTextArea propertiesPanel, final ListBox paletteChooser) {
		super(mainView, propertiesPanel);
		this.setDiagram(PALETTELIST.get(0));
		this.paletteChooser = paletteChooser;
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
	public void onDoubleClick(GridElement ge) {
		if (ge != null) {
			otherDrawFocusPanel.setFocus(true);
			GridElement e = ElementFactory.create(ge, otherDrawFocusPanel.getDiagram());
			commandInvoker.realignElementsToVisibleRect(otherDrawFocusPanel, Arrays.asList(e));
			commandInvoker.addElements(otherDrawFocusPanel, e);
		}
	}

	private List<GridElement> draggedElements = new ArrayList<GridElement>();
	public void onMouseDown(GridElement element, boolean isControlKeyDown) {
		super.onMouseDown(element, isControlKeyDown);
		for (GridElement original : selector.getSelectedElements()) {
			draggedElements.add(ElementFactory.create(original, getDiagram()));
		}
	}
	
	@Override
	public void onMouseDragEnd(GridElement gridElement, Point lastPoint) {
		if (lastPoint.getX() < 0) {
			System.out.println(scrollPanel.getVerticalScrollPosition());
			List<GridElement> elementsToMove = new ArrayList<GridElement>();
			for (GridElement original : selector.getSelectedElements()) {
				GridElement copy = ElementFactory.create(original, otherDrawFocusPanel.getDiagram());
				int verticalScrollbarDiff = otherDrawFocusPanel.scrollPanel.getVerticalScrollPosition() - scrollPanel.getVerticalScrollPosition();
				int horizontalScrollbarDiff = otherDrawFocusPanel.scrollPanel.getHorizontalScrollPosition() - scrollPanel.getHorizontalScrollPosition();
				copy.setLocationDifference(otherDrawFocusPanel.getVisibleBounds().width + horizontalScrollbarDiff, paletteChooser.getOffsetHeight() + verticalScrollbarDiff);
				elementsToMove.add(copy);
			}
			commandInvoker.removeSelectedElements(this);
			commandInvoker.addElements(this, draggedElements);
			selector.deselectAll();
			commandInvoker.addElements(otherDrawFocusPanel, elementsToMove);
		}
		draggedElements.clear();
		super.onMouseDragEnd(gridElement, lastPoint);
	}

}
