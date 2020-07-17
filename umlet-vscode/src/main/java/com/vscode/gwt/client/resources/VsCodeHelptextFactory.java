package com.vscode.gwt.client.resources;

import com.baselet.gwt.client.resources.HelptextFactory;
import com.baselet.gwt.client.resources.HelptextResources;
import com.google.gwt.core.client.GWT;

public class VsCodeHelptextFactory extends HelptextFactory {
    @Override
    public HelptextResources getInstance() {
        return GWT.create(VsCodeHelptextResources.class);
    }
}
