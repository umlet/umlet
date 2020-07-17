package com.baselet.gwt.client.resources;

import com.google.gwt.core.client.GWT;

public class HelptextFactory {
    public HelptextResources getInstance() {
        return GWT.create(HelptextResources.class);
    }
}
