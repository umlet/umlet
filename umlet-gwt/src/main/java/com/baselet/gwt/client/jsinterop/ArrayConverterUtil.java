package com.baselet.gwt.client.jsinterop;

import com.google.gwt.core.client.JavaScriptObject;

public class ArrayConverterUtil {
public static native JavaScriptObject toUint8Array(int[] data) /*-{
        return Uint8Array.from(data);
    }-*/;
}
