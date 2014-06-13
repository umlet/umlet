package com.baselet.gwt.client.view.palettes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface Resources extends ClientBundle {

	Resources INSTANCE = GWT.create(Resources.class);

	@Source("UML Common Elements.uxf")
	TextResource UML_Common_Elements();

	@Source("Generic Colors.uxf")
	TextResource Generic_Colors();

}
