package com.vscode.gwt.client.view.utils;

import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.view.VersionChecker;
import com.baselet.gwt.client.view.utils.StartupDiagramLoader;
import com.google.gwt.core.client.GWT;

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
