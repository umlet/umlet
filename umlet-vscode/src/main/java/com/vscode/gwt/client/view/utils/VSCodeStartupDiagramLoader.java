package com.vscode.gwt.client.view.utils;

import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.logging.CustomLogger;
import com.baselet.gwt.client.logging.CustomLoggerFactory;
import com.baselet.gwt.client.view.DrawPanel;
import com.baselet.gwt.client.view.DrawPanelDiagram;
import com.baselet.gwt.client.view.MainView;
import com.baselet.gwt.client.view.utils.StartupDiagramLoader;

public class VSCodeStartupDiagramLoader implements StartupDiagramLoader {

    private CustomLogger log = CustomLoggerFactory.getLogger(VSCodeStartupDiagramLoader.class);

    @Override
    public void loadDiagram(MainView mainView) {
        //Retrieve the Diagram
        String uxf = getVsCodePredefinedFile();

        // In case a plain, newly created empty file was loaded, UMLet will create the default empty workspace
        // if its not empty, it will load it
        if (uxf != null && !uxf.equals("")) {
            try {
                mainView.setDiagram(DiagramXmlParser.xmlToDiagram(uxf));
            } catch (Exception e) {
                log.error("failed to load diagram passed from startup, loading defaults...");
            }
        } else {
            DrawPanel drawPanel = mainView.getDiagramPanel();
            log.info("current diag is at start:" + DiagramXmlParser.diagramToXml(drawPanel.getDiagram()));
            //if diagram is indeed empty, a new empty diagram file was opened
            //send the 'basic' empty umlet file data back to vscode
            if (mainView.getDiagramPanel() instanceof DrawPanelDiagram)
                ((DrawPanelDiagram) drawPanel).handleFileUpdate();
        }
    }

    private static native String getVsCodePredefinedFile() /*-{
        if (typeof window.parent.vsCodeInitialDiagramData !== 'undefined') {
            return decodeURIComponent(window.parent.vsCodeInitialDiagramData.toString());
        }
        return null;
    }-*/;
}
