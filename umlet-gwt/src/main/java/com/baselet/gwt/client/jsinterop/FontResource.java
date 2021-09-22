package com.baselet.gwt.client.jsinterop;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface FontResource extends ClientBundle {
	@Source("fontNormal.txt")
	TextResource fontNormal();

	@Source("fontBold.txt")
	TextResource fontBold();

	@Source("fontItalic.txt")
	TextResource fontItalic ();
}
