package com.baselet.gwt.client.clipboard;

import com.baselet.element.interfaces.Diagram;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.element.ElementFactoryGwt;
import com.baselet.gwt.client.view.CommandInvoker;
import com.baselet.gwt.client.view.DrawPanel;
import com.baselet.gwt.client.view.DrawPanelDiagram;
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
        $wnd.PasteClipboardToDiagram =
            $entry(@com.baselet.gwt.client.clipboard.VsCodeClipboardManager::PasteClipboardToDiagram(Ljava/lang/String;));
        var newClipboardManager = {
            copy: function () {
                console.log("hit copy in gwt");
                $wnd.CopyDiagramToClipboard();
            },
            paste: function (content) {
                console.log("hit paste in gwt, content is: " + content);
                $wnd.PasteClipboardToDiagram(content);
            }
        };
        console.log("Sending clipboarmanager to gwt");
        window.parent.vsCodeClipboardManager = newClipboardManager;
    }-*/;

    public static native void debugLogInVsCodeWebview(String content) /*-{
        console.log("GWT DEBUG LOG: " + content);
    }-*/;

    public static void SetStorage(EventHandlingUtils.DragCache storage) {
        VsCodeClipboardManager.storage = storage;
    }

    //Without arguments default to whatever panel is active in storage
    public static void CopyDiagramToClipboard() {
        if (VsCodeClipboardManager.storage.getActivePanel() instanceof DrawPanel) {
            DrawPanel activeDrawPanel = ((DrawPanel) VsCodeClipboardManager.storage.getActivePanel());
            CopyDiagramToClipboard(activeDrawPanel);
        }
    }


    public static void CopyDiagramToClipboard(DrawPanel target) {
        List<GridElement> tempList = copyElementsInList(target.getSelector().getSelectedElements(), target.getDiagram());
        String dataForClipboard = (DiagramXmlParser.gridElementsToXml(tempList));
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

    public native static void requestVsCodePaste()
        /*-{
            window.parent.vscode.postMessage({
                command: 'requestPasteClipboard'
            })
        }-*/;



    private static List<GridElement> copyElementsInList(Collection<GridElement> sourceElements, Diagram targetDiagram) {
        List<GridElement> targetElements = new ArrayList<GridElement>();
        for (GridElement ge : sourceElements) {
            GridElement e = ElementFactoryGwt.create(ge, targetDiagram);
            targetElements.add(e);
        }
        return targetElements;
    }

    public static void PasteClipboardToDiagram(String content) {
        if (VsCodeClipboardManager.storage.getActivePanel() instanceof DrawPanelDiagram) {
            DrawPanelDiagram activeDrawPanel = ((DrawPanelDiagram) VsCodeClipboardManager.storage.getActivePanel());
            PasteClipboardToDiagram(activeDrawPanel, content);
        }
    }

    public static void PasteClipboardToDiagram(DrawPanelDiagram target, String content) {
        commandInvoker.pasteElementsVsCode(target, content);
    }
}
