package com.baselet.gwt.client.jsinterop;

import com.google.gwt.core.client.JavaScriptObject;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, name = "buffer.Buffer", namespace = JsPackage.GLOBAL)
public class Buffer {
	public static native JavaScriptObject from(int[] array);
}
