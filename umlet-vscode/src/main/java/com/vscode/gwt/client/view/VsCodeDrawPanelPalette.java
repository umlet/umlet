package com.vscode.gwt.client.view;

import com.baselet.gwt.client.view.DrawPanel;
import com.baselet.gwt.client.view.DrawPanelPalette;

public class VsCodeDrawPanelPalette extends DrawPanelPalette {

    public VsCodeDrawPanelPalette() {
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
                    that.@com.vscode.gwt.client.view.VsCodeDrawPanelPalette::zoom(Ljava/lang/String;)('IN');
                    break;
                case 'zoomOut':
                    that.@com.vscode.gwt.client.view.VsCodeDrawPanelPalette::zoom(Ljava/lang/String;)('OUT');
                    break;
                case 'zoomReset':
                    that.@com.vscode.gwt.client.view.VsCodeDrawPanelPalette::zoom(Ljava/lang/String;)('RESET');
                    break;
            }
        });
    }-*/;
}
