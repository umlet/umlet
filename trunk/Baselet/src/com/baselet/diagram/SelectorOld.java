package com.baselet.diagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.baselet.element.Selector;
import com.umlet.custom.CustomElement;

public class SelectorOld extends Selector {

	private static SelectorOld currentSelector;// to determin what selector is active right now (to set that element blue)

	private GridElement dominantEntity;
	private Vector<GridElement> selectedElements = new Vector<GridElement>();
	private DrawPanel panel;
	private boolean _selectorframeactive;
	private SelectorFrame _selectorframe;

	public SelectorOld(DrawPanel panel) {
		this.panel = panel;
		this._selectorframeactive = false;
		this._selectorframe = new SelectorFrame();
	}

	public GridElement getDominantEntity() {
		if ((dominantEntity == null) && !selectedElements.isEmpty()) { return selectedElements.firstElement(); }
		return dominantEntity;
	}

	public void setDominantEntity(GridElement dominantEntity) {
		this.dominantEntity = dominantEntity;
	}

	public SelectorFrame getSelectorFrame() {
		return this._selectorframe;
	}

	public void setSelectorFrameActive(boolean active) {
		this._selectorframeactive = active;
		if (!active) this._selectorframe.reset();
	}

	public boolean isSelectorFrameActive() {
		return this._selectorframeactive;
	}

	// needed for custom element exchange
	public void singleSelectWithoutUpdatePropertyPanel(GridElement e) {
		deselectAllWithoutUpdatePropertyPanel();
		selectedElements.add(e);
		if (Main.getInstance().getGUI() != null) updateGUIInformation();
		Main.getInstance().setPropertyPanelToCustomElement(e);
	}

	public void deselectAllWithoutUpdatePropertyPanel() {
		// copy selected entities, clear list (to let GridElement.isSelected() calls return the correct result) and iterate over list and update selection status of GridElements
		List<GridElement> listCopy = new ArrayList<GridElement>(selectedElements);
		selectedElements.clear();
		for (GridElement e : listCopy) {
			e.repaint(); // repaint to make sure now unselected entities are not drawn as selected anymore
		}
		dominantEntity = null;
	}

	public void selectAll() {
		select(panel.getGridElements());
	}

	@Override
	public void doAfterSelectionChanged() {
		updateSelectorInformation();
	}
	
	private void updateGUIInformation() {
		Main.getInstance().getGUI().elementsSelected(selectedElements);
		boolean customElementSelected = (selectedElements.size() == 1) && (selectedElements.get(0) instanceof CustomElement);
		Main.getInstance().getGUI().setCustomElementSelected(customElementSelected);
	}

	// updates the GUI with the current selector information (that includes the propertypanel)
	public void updateSelectorInformation() {
		// update the current blue selected elements
		if (currentSelector == null) currentSelector = this;
		else if (currentSelector != this) {
			currentSelector = this;
		}

		// every time something is selected - update the current diagram to this element
		Main.getInstance().setCurrentDiagramHandler(this.panel.getHandler());
		if (Main.getInstance().getGUI() != null) {
			updateGUIInformation();
			if (!selectedElements.isEmpty()) Main.getInstance().setPropertyPanelToGridElement(selectedElements.elementAt(0));
			else Main.getInstance().setPropertyPanelToGridElement(null);
		}
	}

	public void multiSelect(Rectangle rect) {
		for (GridElement e : panel.getGridElements()) {
			if (e.isInRange(rect)) select(e);
		}
	}

	@Override
	public boolean isSelected(GridElement ge) {
		boolean isSelected = super.isSelected(ge);
		return isSelected;
	}
	
	@Override
	public List<GridElement> getSelectedElements() {
		return selectedElements;
	}

	@Override
	public List<GridElement> getAllElements() {
		if (Main.getInstance().getDiagramHandler() == null) {
			return Collections.<GridElement>emptyList();
		}
		return Main.getInstance().getDiagramHandler().getDrawPanel().getGridElements();
	}
	
	@Override
	public void doAfterSelect(GridElement e) {
		super.doAfterSelect(e);
		e.repaint(); // element must be repainted if selection state has changed (for selectioncolor)
	}
	
	@Override
	public void doAfterDeselect(GridElement e) {
		super.doAfterDeselect(e);
		e.repaint(); // element must be repainted if selection state has changed (for selectioncolor)
	}
}
