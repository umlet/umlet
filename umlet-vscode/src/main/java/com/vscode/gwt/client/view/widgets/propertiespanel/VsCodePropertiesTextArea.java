package com.vscode.gwt.client.view.widgets.propertiespanel;

import com.baselet.gwt.client.view.widgets.propertiespanel.PropertiesTextArea;

public class VsCodePropertiesTextArea extends PropertiesTextArea {
    public VsCodePropertiesTextArea() {
        super();
        addFocusChangeListeners();
    }

    private void addFocusChangeListeners() {
        getValueBox().addFocusHandler(event -> propagateFocus(true));
        getValueBox().addBlurHandler(event -> propagateFocus(false));
    }

    public native void propagateFocus(Boolean inFocus) /*-{
        $wnd.vscode.postMessage({
            command: 'propertiesFocus',
            text: inFocus
        });
    }-*/;
}
