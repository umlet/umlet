package com.vscode.gwt.client.file;

import com.baselet.element.interfaces.Diagram;
import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.file.FileChangeNotifier;

public class VsCodeFileChangeNotifier implements FileChangeNotifier {

    @Override
    public void notifyFileChange(Diagram diagram) {
        updateDiagram(DiagramXmlParser.diagramToXml(diagram));
    }

    //sends the current diagram file to vscode, also called when properties change
    private native void updateDiagram(String msg) /*-{
        window.parent.vscode.postMessage({
            command: 'updateFiledataUxf',
            text: msg
        });
    }-*/;
}
