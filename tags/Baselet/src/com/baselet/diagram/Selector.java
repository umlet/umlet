package com.baselet.diagram;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.element.GridElement;
import com.baselet.element.Group;
import com.umlet.custom.CustomElement;


public class Selector {

	private static Selector currentSelector;// to determin what selector is active right now (to set that element blue)

	private GridElement dominantEntity;
	private Vector<GridElement> selectedEntities = new Vector<GridElement>();
	private DrawPanel panel;
	private boolean _selectorframeactive;
	private SelectorFrame _selectorframe;

	public Vector<GridElement> getSelectedEntities() {
		return selectedEntities;
	}

	public Selector(DrawPanel panel) {
		this.panel = panel;
		this._selectorframeactive = false;
		this._selectorframe = new SelectorFrame();
	}

	// AB: usually this is the first selected entity
	// If a group has been selected this entity can be set explicitly
	public GridElement getDominantEntity() {
		if ((dominantEntity == null) && !selectedEntities.isEmpty()) { return selectedEntities.firstElement(); }
		return dominantEntity;
	}

	// AB: if you set this entity, make sure the selectedEntities Vector contains a group entity that contains the dominantEntity.
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

	public void deselectAll() {
		for (GridElement e : this.selectedEntities)
			e.onDeselected();

		dominantEntity = null;
		selectedEntities.clear();
		updateSelectorInformation();
	}

	// needed for custom element exchange
	public void singleSelectWithoutUpdatePropertyPanel(GridElement e) {
		deselectAllWithoutUpdatePropertyPanel();
		selectedEntities.add(e);
		e.onSelected();
		if (Main.getInstance().getGUI() != null) updateGUIInformation();
		Main.getInstance().setPropertyPanelToCustomElement(e);
	}

	public void deselectAllWithoutUpdatePropertyPanel() {
		for (GridElement e : this.selectedEntities)
			e.onDeselected();
		dominantEntity = null;
		selectedEntities.clear();
	}

	public void select(GridElement e) {
		Vector<GridElement> v = new Vector<GridElement>();
		v.add(e);
		handleSelect(v);
	}

	public void select(Vector<GridElement> entities) {
		handleSelect(entities);
	}

	public void selectAll() {
		handleSelect(panel.getAllEntitiesNotInGroup());
	}

	private void handleSelect(Vector<GridElement> entities) {
		for (GridElement e : entities) {
			if (selectedEntities.contains(e) || e.isPartOfGroup()) continue;
			e.onSelected();
			selectedEntities.add(e);
		}
		updateSelectorInformation();
	}

	public void deselect(GridElement e) {
		if (selectedEntities.contains(e)) {
			selectedEntities.removeElement(e);
			e.onDeselected();
			updateSelectorInformation();
		}
	}

	private void updateGUIInformation() {
		Main.getInstance().getGUI().elementsSelected(selectedEntities.size());
		if ((selectedEntities.size() == 1) && (selectedEntities.get(0) instanceof Group)) {
			Main.getInstance().getGUI().setUngroupEnabled(true);
		}
		else {
			Main.getInstance().getGUI().setUngroupEnabled(false);
		}

		if ((selectedEntities.size() == 1) && (selectedEntities.get(0) instanceof CustomElement)) Main.getInstance().getGUI().setCustomElementSelected(true);
		else Main.getInstance().getGUI().setCustomElementSelected(false);
	}

	// updates the GUI with the current selector information (that includes the propertypanel
	public void updateSelectorInformation() {
		// update the current blue selected elements
		if (currentSelector == null) currentSelector = this;
		else if (currentSelector != this) {
			currentSelector.setElementsSelected(false);
			currentSelector = this;
			this.setElementsSelected(true);
		}

		// every time something is selected - update the current diagram to this element
		Main.getInstance().setCurrentDiagramHandler(this.panel.getHandler());
		if (Main.getInstance().getGUI() != null) {
			updateGUIInformation();
			if (selectedEntities.size() == 1) Main.getInstance().setPropertyPanelToGridElement(selectedEntities.elementAt(0));
			else Main.getInstance().setPropertyPanelToGridElement(null);
		}
	}

	private void setElementsSelected(boolean selected) {
		if (selected) {
			for (GridElement e : this.selectedEntities)
				e.onSelected();
		}
		else {
			for (GridElement e : this.selectedEntities)
				e.onDeselected();
		}
	}

	public void singleSelect(GridElement e) {
		this.deselectAll();
		this.select(e);
	}

	public void multiSelect(Point upperLeft, Dimension size) {
		Vector<GridElement> entities = this.panel.getAllEntities();
		for (int i = 0; i < entities.size(); i++) {
			GridElement e = entities.get(i);
			if (e.isInRange(upperLeft, size)) select(e);
		}
	}
}
