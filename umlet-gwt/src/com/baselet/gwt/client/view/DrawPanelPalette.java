package com.baselet.gwt.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baselet.control.SharedUtils;
import com.baselet.control.basics.geom.Point;
import com.baselet.element.Selector;
import com.baselet.element.facet.common.GroupFacet;
import com.baselet.element.interfaces.Diagram;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.sticking.StickableMap;
import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.element.ElementFactoryGwt;
import com.baselet.gwt.client.view.palettes.Resources;
import com.baselet.gwt.client.view.widgets.propertiespanel.PropertiesTextArea;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.ListBox;

public class DrawPanelPalette extends DrawPanel {

	private static final List<TextResource> PALETTELIST = Arrays.asList(
			Resources.INSTANCE.UML_Common_Elements(),
			Resources.INSTANCE.Custom_Drawings(),
			Resources.INSTANCE.Generic_Colors(),
			Resources.INSTANCE.Generic_Layers(),
			Resources.INSTANCE.Generic_Text_and_Alignment(),
			Resources.INSTANCE.UML_Activity(),
			Resources.INSTANCE.UML_Class(),
			Resources.INSTANCE.UML_Composite_Structure(),
			Resources.INSTANCE.UML_Package(),
			Resources.INSTANCE.UML_Sequence(),
			Resources.INSTANCE.UML_Sequence_All_in_one(),
			Resources.INSTANCE.UML_State_Machine(),
			Resources.INSTANCE.UML_Structure_and_Deployment(),
			Resources.INSTANCE.UML_Use_Case(),
			Resources.INSTANCE.Plots());
	private final Map<TextResource, Diagram> paletteCache = new HashMap<TextResource, Diagram>();

	private final ListBox paletteChooser;

	public DrawPanelPalette(MainView mainView, PropertiesTextArea propertiesPanel, final ListBox paletteChooser) {
		super(mainView, propertiesPanel);
		setDiagram(parsePalette(PALETTELIST.get(0)));
		this.paletteChooser = paletteChooser;
		for (TextResource r : PALETTELIST) {
			paletteChooser.addItem(r.getName().replaceAll("_", " "));
		}
		paletteChooser.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				setDiagram(parsePalette(PALETTELIST.get(paletteChooser.getSelectedIndex())));
				selector.deselectAll();
			}
		});
		paletteChooser.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.stopPropagation(); // avoid propagation of mouseclick to palette which can be under the opened listbox
			}
		});
	}

	private Diagram parsePalette(TextResource res) {
		Diagram diagram = paletteCache.get(res);
		if (diagram == null) {
			diagram = DiagramXmlParser.xmlToDiagram(res.getText());
			paletteCache.put(res, diagram);
		}
		return diagram;
	}

	@Override
	public void onDoubleClick(GridElement ge) {
		if (ge != null && !propertiesPanel.getPaletteShouldIgnoreMouseClicks()) {
			otherDrawFocusPanel.setFocus(true);
			GridElement e = ElementFactoryGwt.create(ge, otherDrawFocusPanel.getDiagram());
			e.setProperty(GroupFacet.KEY, null);
			commandInvoker.realignElementsToVisibleRect(otherDrawFocusPanel, Arrays.asList(e));
			commandInvoker.addElements(otherDrawFocusPanel, Arrays.asList(e));
		}
	}

	private final List<GridElement> draggedElements = new ArrayList<GridElement>();

	@Override
	void onMouseDown(GridElement element, boolean isControlKeyDown) {
		super.onMouseDown(element, isControlKeyDown);
		for (GridElement original : selector.getSelectedElements()) {
			draggedElements.add(ElementFactoryGwt.create(original, getDiagram()));
		}
	}

	@Override
	public void onMouseDragEnd(GridElement gridElement, Point lastPoint) {
		if (lastPoint.getX() < 0) { // mouse moved from palette to diagram -> insert elements to diagram
			List<GridElement> elementsToMove = new ArrayList<GridElement>();
			for (GridElement original : selector.getSelectedElements()) {
				GridElement copy = ElementFactoryGwt.create(original, otherDrawFocusPanel.getDiagram());
				int verticalScrollbarDiff = otherDrawFocusPanel.scrollPanel.getVerticalScrollPosition() - scrollPanel.getVerticalScrollPosition();
				int horizontalScrollbarDiff = otherDrawFocusPanel.scrollPanel.getHorizontalScrollPosition() - scrollPanel.getHorizontalScrollPosition();
				copy.setLocationDifference(otherDrawFocusPanel.getVisibleBounds().width + horizontalScrollbarDiff, paletteChooser.getOffsetHeight() + verticalScrollbarDiff);

				copy.setRectangle(SharedUtils.realignToGrid(copy.getRectangle(), false)); // realign location to grid (width and height should not be changed)
				elementsToMove.add(copy);
			}
			Selector.replaceGroupsWithNewGroups(elementsToMove, otherDrawFocusPanel.getSelector());
			commandInvoker.removeSelectedElements(this);
			commandInvoker.addElements(this, draggedElements);
			selector.deselectAll();
			commandInvoker.addElements(otherDrawFocusPanel, elementsToMove);
		}
		draggedElements.clear();
		super.onMouseDragEnd(gridElement, lastPoint);
	}

	@Override
	protected StickableMap getStickablesToMoveWhenElementsMove(GridElement draggedElement, List<GridElement> elements) {
		// Moves at the palette NEVER stick
		return StickableMap.EMPTY_MAP;
	}

}
