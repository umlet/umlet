package com.baselet.gwt.client.view;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.constants.SharedConstants;
import com.baselet.element.facet.common.GroupFacet;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gwt.client.element.ElementFactoryGwt;
import com.baselet.gwt.client.view.widgets.propertiespanel.PropertiesTextArea;



public class DrawPanelDiagram extends DrawPanel {
	private List<GridElement> lastPreviewElements; //previewed elements that will be displayed while dragging from pallete into actual canvas
	public DrawPanelDiagram(MainView mainView, PropertiesTextArea propertiesPanel) {
		super(mainView, propertiesPanel);
	}

	@Override
	public void onDoubleClick(GridElement ge) {
		if (ge != null) {
			GridElement e = ElementFactoryGwt.create(ge, getDiagram());
			e.setProperty(GroupFacet.KEY, null);
			e.setLocationDifference(SharedConstants.DEFAULT_GRID_SIZE, SharedConstants.DEFAULT_GRID_SIZE);
			commandInvoker.addElements(this, Arrays.asList(e));
		}
	}

	public void UpdateDisplayingPreviewElements(List<GridElement> previewElements)
	{
		//remove old preview objects
		RemoveOldPreview();
		//view new ones
		if (previewElements != null)
			commandInvoker.addElements(this, previewElements);
		this.lastPreviewElements = previewElements;
	}

	public void RemoveOldPreview() {
		if (lastPreviewElements != null)
			commandInvoker.removeElements(this, this.lastPreviewElements);
	}

}
