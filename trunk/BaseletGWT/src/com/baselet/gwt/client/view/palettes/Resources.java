package com.baselet.gwt.client.view.palettes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface Resources extends ClientBundle {

	Resources INSTANCE = GWT.create(Resources.class);

	@Source("Generic.uxf")
	TextResource bla();

	@Source("Generic Colors.uxf")
	TextResource blax();

}
