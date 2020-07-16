package com.baselet.gwt.client.clipboard;

public class VsCodeClipboardManager {

    public static native void hookUpClipboardManagerToVsCode() /*-{
        $wnd.HandleExportDiagramRequest =
            $entry(@com.baselet.gwt.client.clipboard.VsCodeClipboardManager::handleExportDiagramRequest(Ljava/lang/String;));
        var newClipboardManager = {
            requestExport: function (content) {
                console.log("requesting export in gwt");
                $wnd.HandleExportDiagramRequest(content);
            }
        };
        console.log("Sending clipboarmanager to gwt");
        window.parent.vsCodeClipboardManager = newClipboardManager;
    }-*/;

    public static native void debugLogInVsCodeWebview(String content) /*-{
        console.log("GWT DEBUG LOG: " + content);
    }-*/;

    //drawPanelDiagram must be set before this function is called
    //size must be a string which can be converted to a double
    public static void handleExportDiagramRequest(String size) {
        /*double scalingValue = Double.parseDouble(size);
        String scaledPngUrl = CanvasUtils.createPngCanvasDataUrl(drawPanelDiagram.getDiagram(), scalingValue);
        exportPngVSCode(scaledPngUrl);*/
    }
}
