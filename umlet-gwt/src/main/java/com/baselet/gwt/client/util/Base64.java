package com.baselet.gwt.client.util;

import elemental2.core.ArrayBuffer;
import elemental2.core.Uint8Array;
import elemental2.dom.DomGlobal;

public class Base64 {
	public static Uint8Array decode(String base64) {
		String decodedBase64 = DomGlobal.atob(base64);
		Uint8Array uint8Array = new Uint8Array(new ArrayBuffer(decodedBase64.length()));
		for (int i = 0; i < decodedBase64.length(); i++) {
			uint8Array.setAt(i, (double) decodedBase64.charAt(i));
		}
		return uint8Array;
	}
}
