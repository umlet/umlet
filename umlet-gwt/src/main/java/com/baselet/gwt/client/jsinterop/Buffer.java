package com.baselet.gwt.client.jsinterop;

import com.google.gwt.core.client.JavaScriptObject;
import elemental2.core.Uint8Array;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, name = "buffer.Buffer", namespace = JsPackage.GLOBAL)
public class Buffer {
	public static native JavaScriptObject from(Uint8Array array);
}
