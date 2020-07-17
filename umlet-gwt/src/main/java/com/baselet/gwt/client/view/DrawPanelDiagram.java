package com.baselet.gwt.client.view;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.constants.SharedConstants;
import com.baselet.element.GridElementUtils;
import com.baselet.element.facet.common.GroupFacet;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.element.ElementFactoryGwt;
import com.baselet.gwt.client.keyboard.Shortcut;
import com.baselet.gwt.client.logging.CustomLogger;
import com.baselet.gwt.client.logging.CustomLoggerFactory;
import com.baselet.gwt.client.view.widgets.propertiespanel.PropertiesTextArea;
import com.google.gwt.event.dom.client.KeyDownEvent;

public class DrawPanelDiagram extends DrawPanel {
    private List<GridElement> currentPreviewElementsInstantiated;
    private List<GridElement> currentPreviewElements; //previewed elements that will be displayed while dragging from palette into actual canvas

    private String lastDiagramXMLState;
    private boolean tempInvalid;

    private static final CustomLogger log = CustomLoggerFactory.getLogger(DiagramXmlParser.class);

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
                commandInvoker.addElementsDontNotifyUpdate(this, previewElements);
            this.currentPreviewElements = previewElements;
        }
        this.redraw(false);
    }

    @Override
    void redraw(boolean recalcSize) {
        if (tempInvalid)
        {
            log.info("redrawing temp invalid");
            List<GridElement> gridElements = diagram.getGridElementsByLayerLowestToHighest();
            if (recalcSize) {
                if (scrollPanel == null) {
                    return;
                }

                Rectangle diagramRect = GridElementUtils.getGridElementsRectangle(gridElements);
                Rectangle visibleRect = getVisibleBounds();
                // realign top left corner of the diagram back to the canvas and remove invisible whitespace outside of the diagram
                final int xTranslate = Math.min(visibleRect.getX(), diagramRect.getX()); // can be positive (to cut upper left whitespace without diagram) or negative (to move diagram back to the visible canvas which starts at (0,0))
                final int yTranslate = Math.min(visibleRect.getY(), diagramRect.getY());
                if (xTranslate != 0 || yTranslate != 0) {
                    // temp increase of canvas size to make sure scrollbar can be moved
                    canvas.clearAndSetSize(canvas.getWidth() + Math.abs(xTranslate), canvas.getHeight() + Math.abs(yTranslate));
                    // move scrollbars
                    scrollPanel.moveHorizontalScrollbar(-xTranslate);
                    scrollPanel.moveVerticalScrollbar(-yTranslate);
                    // then move gridelements to correct position
                    for (GridElement ge : gridElements) {
                        ge.setLocationDifference(-xTranslate, -yTranslate);
                    }
                }
                // now realign bottom right corner to include the translate-factor and the changed visible and diagram rect
                int width = Math.max(visibleRect.getX2(), diagramRect.getX2()) - xTranslate;
                int height = Math.max(visibleRect.getY2(), diagramRect.getY2()) - yTranslate;
                canvas.clearAndSetSize(width, height);
            } else {
                canvas.clearAndSetSize(canvas.getWidth(), canvas.getHeight());
            }
            canvas.drawInvalidDiagramInfo();
        } else {
            log.info("redrawing super");
            super.redraw(recalcSize);
        }
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
        if (!tempInvalid)
            fileChangeNotifier.notifyFileChange(DiagramXmlParser.diagramToXml(getDiagram()));
    }


    /*
     if vs code injects an invalid uxf during use, then umletino will go into an tempInvalid state until a valid diagram is sent by vscode.
     no changes will be submitted to vs code while tempInvalid, and redraw() will result in and error message beeing drawn.
     */
    public void setTempInvalid(boolean tempInvalid)
    {
        this.tempInvalid = tempInvalid;
        //keep in mind a redraw is needed
        redraw();
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
