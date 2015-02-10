package com.baselet.diagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.element.Selector;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.old.custom.CustomElement;
import com.baselet.gui.CurrentGui;

public class SelectorOld extends Selector {

	private GridElement dominantEntity;
	private final Vector<GridElement> selectedElements = new Vector<GridElement>();
	private final DrawPanel panel;
	private boolean _selectorframeactive;
	private final SelectorFrame _selectorframe;

	public SelectorOld(DrawPanel panel) {
		this.panel = panel;
		_selectorframeactive = false;
		_selectorframe = new SelectorFrame();
	}

	public GridElement getDominantEntity() {
		if (dominantEntity == null && !selectedElements.isEmpty()) {
			return selectedElements.firstElement();
		}
		return dominantEntity;
	}

	public void setDominantEntity(GridElement dominantEntity) {
		this.dominantEntity = dominantEntity;
	}

	public SelectorFrame getSelectorFrame() {
		return _selectorframe;
	}

	public void setSelectorFrameActive(boolean active) {
		_selectorframeactive = active;
		if (!active) {
			_selectorframe.reset();
		}
	}

	public boolean isSelectorFrameActive() {
		return _selectorframeactive;
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
		CurrentGui.getInstance().getGui().elementsSelected(selectedElements);
		boolean customElementSelected = selectedElements.size() == 1 && selectedElements.get(0) instanceof CustomElement;
		CurrentGui.getInstance().getGui().setCustomElementSelected(customElementSelected);
	}

	public void updateSelectorInformation() {
		GridElement elementForPropPanel = null;
		if (!selectedElements.isEmpty()) {
			elementForPropPanel = selectedElements.elementAt(selectedElements.size() - 1);
		}
		updateSelectorInformation(elementForPropPanel);
	}

	// updates the GUI with the current selector information (that includes the propertypanel)
	public void updateSelectorInformation(GridElement elementForPropPanel) {
		// every time something is selected - update the current diagram to this element
		CurrentDiagram.getInstance().setCurrentDiagramHandler(panel.getHandler());
		if (CurrentGui.getInstance().getGui() != null) {
			updateGUIInformation();
			Main.getInstance().setPropertyPanelToGridElement(elementForPropPanel);
		}
	}

	public void multiSelect(Rectangle rect) {
		for (GridElement e : panel.getGridElements()) {
			if (e.isInRange(rect)) {
				select(e);
			}
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
		if (CurrentDiagram.getInstance().getDiagramHandler() == null) {
			return Collections.<GridElement> emptyList();
		}
		return CurrentDiagram.getInstance().getDiagramHandler().getDrawPanel().getGridElements();
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
