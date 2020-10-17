package com.baselet.gwt.client.jsinterop;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, name = "blobStream", namespace = JsPackage.GLOBAL)
public class BlobStream {

	public native void on(String eventType, EventListenerCallback fn);

	public native String toBlobURL(String type);

	public native Object toBlob(String type);
}
