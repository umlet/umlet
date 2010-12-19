// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.diagram;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Vector;

import com.umlet.control.Umlet;
import com.umlet.custom.CustomElement;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Group;

public class Selector {

	private static Selector currentSelector;// to determin what selector is active right now (to set that element blue)

	private Entity dominantEntity;
	private Vector<Entity> selectedEntities = new Vector<Entity>();
	private DrawPanel panel;
	private boolean _selectorframeactive;
	private SelectorFrame _selectorframe;

	public Vector<Entity> getSelectedEntities() {
		return selectedEntities;
	}

	public Selector(DrawPanel panel) {
		this.panel = panel;
		this._selectorframeactive = false;
		this._selectorframe = new SelectorFrame();
	}

	// AB: usually this is the first selected entity
	// If a group has been selected this entity can be set explicitly
	public Entity getDominantEntity() {
		if ((dominantEntity == null) && !selectedEntities.isEmpty()) { return selectedEntities.firstElement(); }
		return dominantEntity;
	}

	// AB: if you set this entity, make sure the selectedEntities Vector contains a group entity that contains the dominantEntity.
	public void setDominantEntity(Entity dominantEntity) {
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
		for (Entity e : this.selectedEntities)
			e.onDeselected();

		dominantEntity = null;
		selectedEntities.clear();
		updateSelectorInformation();
	}

	// needed for custom element exchange
	public void singleSelectWithoutUpdatePropertyPanel(Entity e) {
		deselectAllWithoutUpdatePropertyPanel();
		selectedEntities.add(e);
		e.onSelected();
		if (Umlet.getInstance().getGUI() != null) updateGUIInformation();
		Umlet.getInstance().setPropertyPanelToCustomEntity(e);
	}

	public void deselectAllWithoutUpdatePropertyPanel() {
		for (Entity e : this.selectedEntities)
			e.onDeselected();
		dominantEntity = null;
		selectedEntities.clear();
	}

	public void select(Entity e) {
		Vector<Entity> v = new Vector<Entity>();
		v.add(e);
		handleSelect(v);
	}
	
	public void select(Vector<Entity> entities) {
		handleSelect(entities);
	}

	public void selectAll() {
		handleSelect(panel.getAllEntitiesNotInGroup());
	}
	
	private void handleSelect(Vector<Entity> entities) {
		for (Entity e : entities) {
			if (selectedEntities.contains(e) || e.isPartOfGroup()) continue;
			e.onSelected();
			selectedEntities.add(e);
		}
		updateSelectorInformation();
	}

	public void deselect(Entity e) {
		if (selectedEntities.contains(e)) {
			selectedEntities.removeElement(e);
			e.onDeselected();
			updateSelectorInformation();
		}
	}

	private void updateGUIInformation() {
		Umlet.getInstance().getGUI().elementsSelected(selectedEntities.size());
		if ((selectedEntities.size() == 1) && (selectedEntities.get(0) instanceof Group)) {
			Umlet.getInstance().getGUI().setUngroupEnabled(true);
		}
		else {
			Umlet.getInstance().getGUI().setUngroupEnabled(false);
		}

		if ((selectedEntities.size() == 1) && (selectedEntities.get(0) instanceof CustomElement)) Umlet.getInstance().getGUI().setCustomElementSelected(true);
		else Umlet.getInstance().getGUI().setCustomElementSelected(false);
	}

	// updates the Umlet/GUI with the current selector information (that includes the propertypanel
	public void updateSelectorInformation() {
		// update the current blue selected elements
		if (currentSelector == null) currentSelector = this;
		else if (currentSelector != this) {
			currentSelector.setElementsSelected(false);
			currentSelector = this;
			this.setElementsSelected(true);
		}

		// every time something is selected - update the current diagram to this element
		Umlet.getInstance().setCurrentDiagram(this.panel.getHandler());
		if (Umlet.getInstance().getGUI() != null) {
			updateGUIInformation();
			if (selectedEntities.size() == 1) Umlet.getInstance().setPropertyPanelToEntity(selectedEntities.elementAt(0));
			else Umlet.getInstance().setPropertyPanelToEntity(null);
		}
	}

	private void setElementsSelected(boolean selected) {
		if (selected) {
			for (Entity e : this.selectedEntities)
				e.onSelected();
		}
		else {
			for (Entity e : this.selectedEntities)
				e.onDeselected();
		}
	}

	public void singleSelect(Entity e) {
		this.deselectAll();
		this.select(e);
	}

	public void multiSelect(Point upperLeft, Dimension size) {
		Vector<Entity> entities = this.panel.getAllEntities();
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e.isInRange(upperLeft, size)) select(e);
		}
	}
}
