package com.vscode.gwt.client.view.utils;

import com.baselet.gwt.client.view.utils.StartupDiagramLoader;

public class VSCodeStartupDiagramLoader implements StartupDiagramLoader {
    @Override
    public String loadDiagram() {
        //Retrieve the Diagram
        return getVsCodePredefinedFile();
    }

    private static native String getVsCodePredefinedFile() /*-{
        if (typeof window.parent.vsCodeInitialDiagramData !== 'undefined') {
            return decodeURIComponent(window.parent.vsCodeInitialDiagramData.toString());
        }
        return null;
    }-*/;
}
