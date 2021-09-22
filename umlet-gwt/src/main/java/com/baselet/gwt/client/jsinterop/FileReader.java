package com.baselet.gwt.client.jsinterop;

import com.google.gwt.core.client.GWT;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class FileReader {
	public String result;

	public native String readAsDataURL(Object blob);

	public EventListenerCallback onloadend;
}
