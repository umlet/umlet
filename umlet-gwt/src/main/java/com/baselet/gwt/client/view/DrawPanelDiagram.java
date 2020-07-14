package com.baselet.gwt.client.view;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.basics.geom.Point;
import com.baselet.control.constants.SharedConstants;
import com.baselet.element.facet.common.GroupFacet;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.element.ElementFactoryGwt;
import com.baselet.gwt.client.keyboard.Shortcut;
import com.baselet.gwt.client.view.widgets.propertiespanel.PropertiesTextArea;
import com.google.gwt.event.dom.client.KeyDownEvent;

public class DrawPanelDiagram extends DrawPanel {
    private List<GridElement> currentPreviewElementsInstantiated;
    private List<GridElement> currentPreviewElements; //previewed elements that will be displayed while dragging from palette into actual canvas

    private String lastDiagramXMLState;

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

    public void InitializeDisplayingPreviewElements(List<GridElement> previewElements) {
        if (currentPreviewElements == null) {
            if (previewElements != null)
                commandInvoker.addElements(this, previewElements);
            this.currentPreviewElements = previewElements;
        }
        this.redraw(false);
    }

    /*
    displays a preview object by newly creating a copy.
    destroys and overrides old copy preview object if one was available.
    a preview will stay visible until this method is called with (null) as argument or RemoveOldPreview() is called
    should not be used for multiple objects, due to performance
    */
    public void setDisplayingPreviewElementInstantiated(List<GridElement> previewElements) {
        RemoveOldPreview();
        if (previewElements != null)
            this.addGridElementsDontNotifyUpdate(previewElements);
        this.currentPreviewElementsInstantiated = previewElements;
    }

    //for multiple
    public void UpdateDisplayingPreviewElements(int diffX, int diffY, boolean firstDrag) {
        if (currentPreviewElements != null) {
            moveElements(diffX, diffY, firstDrag, currentPreviewElements);
        }
        this.redraw(false);
    }




    @Override
    void onMouseDown(GridElement element, boolean isControlKeyDown) {
        super.onMouseDown(element, isControlKeyDown);
        //CancelDragOfPalette(); //Should not be needed anymore since selecting elements only works with left click now
        if (!isControlKeyDown || selector.getSelectedElements().size() > 0)
            propertiesPanel.setEnabled(true);
        RemoveOldPreview();

    }

    public void CancelDragOfPalette() {
        if (otherDrawFocusPanel instanceof DrawPanelPalette)
            ((DrawPanelPalette) otherDrawFocusPanel).CancelDragNoDuplicate();
    }

    @Override
    public void onShowMenu(Point point) {
        CancelDragOfPalette();
        super.onShowMenu(point);
    }

    /*
    takes the current state of the diagram and forwards it (to vscode)
     */
    public void handleFileUpdate() {
        String newDiagramXMLState = DiagramXmlParser.diagramToXml(getDiagram());
        if (lastDiagramXMLState != null && !lastDiagramXMLState.equals(newDiagramXMLState)) {

            fileChangeNotifier.notifyFileChange(newDiagramXMLState);
        }

        lastDiagramXMLState = newDiagramXMLState;
    }

    /*
         removes old previews for both instantiated and regular preview variants
         */
    public void RemoveOldPreview() {
        //regular
        if (currentPreviewElements != null)
            commandInvoker.removeElementsNoUpdate(this, this.currentPreviewElements);
        currentPreviewElements = null;

        //instantiated
        if (currentPreviewElementsInstantiated != null)
            commandInvoker.removeElementsNoUpdate(this, this.currentPreviewElementsInstantiated);
        currentPreviewElementsInstantiated = null;
    }

    public boolean currentlyDisplayingPreview() {
        if (currentPreviewElements == null)
            return false;
        else
            return true;
    }

    //Whenever elements are updated (added, moved/deformed, removed, edited in the properties panel), visual studio code must be informed and update with the newest file
    @Override
    public void addGridElements(List<GridElement> elements) {
        super.addGridElements(elements);
        handleFileUpdate();
    }

    public void addGridElementsDontNotifyUpdate(List<GridElement> elements) {
        super.addGridElements(elements);
    }



    @Override
    public void removeGridElements(List<GridElement> elements) {
        super.removeGridElements(elements);
        handleFileUpdate();
    }

    public void removeGridElementsDontNotifyUpdate(List<GridElement> elements) {
        super.removeGridElements(elements);
    }

    @Override
    public void onMouseDragEnd(GridElement gridElement, Point lastPoint) {
        super.onMouseDragEnd(gridElement, lastPoint);
        if (cursorWasMovedDuringDrag)
            handleFileUpdate();
    }

    @Override
    public void handleKeyDown(KeyDownEvent event) {
        super.handleKeyDown(event);
        if (Shortcut.MOVE_UP.matches(event) || Shortcut.MOVE_DOWN.matches(event) || Shortcut.MOVE_LEFT.matches(event) || Shortcut.MOVE_RIGHT.matches(event)) {
            handleFileUpdate();
        }
    }

}
