package com.vscode.gwt.client.view;

import com.baselet.gwt.client.view.DrawPanel;
import com.baselet.gwt.client.view.DrawPanelDiagram;

public class VsCodeDrawPanelDiagram extends DrawPanelDiagram {

    public VsCodeDrawPanelDiagram() {
        initListener();
    }

    @Override
    protected void zoom(DrawPanel.Zoom zoom) {
    }


    private native void initListener() /*-{
        var that = this;
        $wnd.addEventListener('message', function (event) {
            var message = event.data;
            switch (message.command) {
                case 'zoomIn':
                    that.@com.vscode.gwt.client.view.VsCodeDrawPanelDiagram::zoom(Ljava/lang/String;)('IN');
                    break;
                case 'zoomOut':
                    that.@com.vscode.gwt.client.view.VsCodeDrawPanelDiagram::zoom(Ljava/lang/String;)('OUT');
                    break;
                case 'zoomReset':
                    that.@com.vscode.gwt.client.view.VsCodeDrawPanelDiagram::zoom(Ljava/lang/String;)('RESET');
                    break;
            }
        });
    }-*/;
}
