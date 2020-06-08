package com.baselet.gwt.client.clipboard;

import com.baselet.element.interfaces.Diagram;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.element.ElementFactoryGwt;
import com.baselet.gwt.client.view.CommandInvoker;
import com.baselet.gwt.client.view.DrawPanel;
import com.baselet.gwt.client.view.EventHandlingUtils;
import com.google.gwt.core.client.GWT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VsCodeClipboardManager {
    private static EventHandlingUtils.DragCache storage; //needed to get active diagram
    private static CommandInvoker commandInvoker = CommandInvoker.getInstance();

    public static native void hookUpClipboardManagerToVsCode() /*-{
        $wnd.CopyDiagramToClipboard =
            $entry(@com.baselet.gwt.client.clipboard.VsCodeClipboardManager::CopyDiagramToClipboard());
        var newClipboardManager = {
            copy: function () {
                console.log("hit copy in gwt");
                $wnd.CopyDiagramToClipboard();
            },
            paste: function () {
                console.log("hit paste in gwt");
            }
        };
        console.log("Sending clipboarmanager to gwt");
        window.parent.vsCodeClipboardManager = newClipboardManager;
    }-*/;

    public static void SetStorage(EventHandlingUtils.DragCache storage) {
        VsCodeClipboardManager.storage = storage;
    }

    //Without arguments default to whatever panel is active in storage
    public static void CopyDiagramToClipboard() {
        GWT.log("COPIED0");
        if (VsCodeClipboardManager.storage.getActivePanel() instanceof DrawPanel) {
            DrawPanel activeDrawPanel = ((DrawPanel) VsCodeClipboardManager.storage.getActivePanel());
            GWT.log("COPIED1 FROM: " + activeDrawPanel.getDiagram().getPanelAttributes());
            CopyDiagramToClipboard(activeDrawPanel);
        }
    }


    public static void CopyDiagramToClipboard(DrawPanel target) {
        List<GridElement> tempList = copyElementsInList(target.getSelector().getSelectedElements(), target.getDiagram());
        String dataForClipboard = (DiagramXmlParser.gridElementsToXml(tempList));
        GWT.log("COPIED2 FROM: " + dataForClipboard);
        setVsCodeClipboard(dataForClipboard);
    }

    private native static void setVsCodeClipboard(String content)
        /*-{
            window.parent.vscode.postMessage({
                command: 'setClipboard',
                text: content
            })
            console.log("COPIED3, wrote text to clip " + content);
        }-*/;

    private native static String getVsCodeClipboard()
        /*-{
            return await (window.parent.vscode.env.clipboard.readText(content));
        }-*/;

    private static List<GridElement> copyElementsInList(Collection<GridElement> sourceElements, Diagram targetDiagram) {
        List<GridElement> targetElements = new ArrayList<GridElement>();
        for (GridElement ge : sourceElements) {
            GridElement e = ElementFactoryGwt.create(ge, targetDiagram);
            targetElements.add(e);
        }
        return targetElements;
    }

    public static void PasteClipboardToDiagram() {
        if (VsCodeClipboardManager.storage.getActivePanel() instanceof DrawPanel) {
            DrawPanel activeDrawPanel = ((DrawPanel) VsCodeClipboardManager.storage.getActivePanel());
            commandInvoker.copySelectedElements(activeDrawPanel);
        }
    }
}
