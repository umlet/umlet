package com.vscode.gwt.client.view;

import com.baselet.gwt.client.view.MainView;

public class VsCodeMainView extends MainView {
    public VsCodeMainView() {
        super();
        diagramPaletteSplitter.setWidgetHidden(diagramPaletteSplitter.getWidget(0), true);
    }
}
