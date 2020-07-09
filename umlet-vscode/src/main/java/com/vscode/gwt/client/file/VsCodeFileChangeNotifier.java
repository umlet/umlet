package com.vscode.gwt.client.file;

import com.baselet.gwt.client.file.FileChangeNotifier;

public class VsCodeFileChangeNotifier implements FileChangeNotifier {

    @Override
    public void notifyFileChange(String diagramXMLState) {
        updateDiagram(diagramXMLState);
    }

    //sends the current diagram file to vscode, also called when properties change
    private native void updateDiagram(String msg) /*-{
        window.parent.vscode.postMessage({
            command: 'updateFiledataUxf',
            text: msg
        });
    }-*/;
}
