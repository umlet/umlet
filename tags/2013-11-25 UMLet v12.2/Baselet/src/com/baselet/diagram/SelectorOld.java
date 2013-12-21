package com.baselet.diagram;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.baselet.element.Group;
import com.baselet.element.OldGridElement;
import com.baselet.element.Selector;
import com.umlet.custom.CustomElement;
import com.umlet.element.experimental.ComponentSwing;

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

	// AB: usually this is the first selected entity
	// If a group has been selected this entity can be set explicitly
	public GridElement getDominantEntity() {
		if ((dominantEntity == null) && !selectedElements.isEmpty()) { return selectedElements.firstElement(); }
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

	// needed for custom element exchange
	public void singleSelectWithoutUpdatePropertyPanel(GridElement e) {
		deselectAllWithoutUpdatePropertyPanel();
		selectedElements.add(e);
		setSelected(e, true);
		if (Main.getInstance().getGUI() != null) updateGUIInformation();
		Main.getInstance().setPropertyPanelToCustomElement(e);
	}

	private void setSelected(GridElement e, boolean value) {
		if (e instanceof OldGridElement) {
			((OldGridElement) e).setSelected(value);
		} else {
			((ComponentSwing)e.getComponent()).setSelected(value);
		}
		if (e instanceof Group) {
			for (GridElement eInGroup : ((Group) e).getMembers()) {
				setSelected(eInGroup, value);
			}
		}
	}

	public void deselectAllWithoutUpdatePropertyPanel() {
		// copy selected entities, clear list (to let GridElement.isSelected() calls return the correct result) and iterate over list and update selection status of GridElements
		List<GridElement> listCopy = new ArrayList<GridElement>(selectedElements);
		selectedElements.clear();
		for (GridElement e : listCopy) {
			setSelected(e, false);
			e.repaint(); // repaint to make sure now unselected entities are not drawn as selected anymore
		}
		dominantEntity = null;
	}

	public void selectAll() {
		select(panel.getAllEntitiesNotInGroup());
	}

	@Override
	public void doAfterSelectionChanged() {
		updateSelectorInformation();
	}
	
	@Override
	public void doAfterSelect(GridElement e) {
		super.doAfterSelect(e);
		setSelected(e, true);
	}
	
	@Override
	public void doAfterDeselect(GridElement e) {
		super.doAfterDeselect(e);
		setSelected(e, false);
	}
	
	private void updateGUIInformation() {
		Main.getInstance().getGUI().elementsSelected(selectedElements.size());
		boolean ungroupEnabled = (selectedElements.size() == 1) && (selectedElements.get(0) instanceof Group);
		Main.getInstance().getGUI().setUngroupEnabled(ungroupEnabled);

		boolean customElementSelected = (selectedElements.size() == 1) && (selectedElements.get(0) instanceof CustomElement);
		Main.getInstance().getGUI().setCustomElementSelected(customElementSelected);
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
			if (selectedElements.size() == 1) Main.getInstance().setPropertyPanelToGridElement(selectedElements.elementAt(0));
			else Main.getInstance().setPropertyPanelToGridElement(null);
		}
	}

	private void setElementsSelected(boolean selected) {
		for (GridElement e : this.selectedElements)
			setSelected(e, selected);
	}

	public void multiSelect(Rectangle rect) {
		for (GridElement e : panel.getAllEntities()) {
			if (e.isInRange(rect)) select(e);
		}
	}

	@Override
	public boolean isSelected(GridElement ge) {
		boolean isSelected = super.isSelected(ge);
		for (GridElement sge : getSelectedElements()) {
			if (sge instanceof Group) {
				isSelected = isSelected || ((Group) sge).getMembersRecursive().contains(ge);
			}
		}
		return isSelected;
	}
	
	@Override
	public List<GridElement> getSelectedElements() {
		return selectedElements;
	}
}
