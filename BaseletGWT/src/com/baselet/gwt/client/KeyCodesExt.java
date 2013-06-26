package com.baselet.gwt.client;

/**
 * helper class to detect keycodes
 * see http://www.javascripter.net/faq/keycodes.htm (not up to date because it doesnt list 171 as +)
 * and http://www.w3.org/2002/09/tests/keys.html
 */
public class KeyCodesExt {
	
	public static boolean isPlus(int code) {
		return code == 61 || code == 187|| code == 107 || code == 171;
	}
	public static boolean isMinus(int code) {
		return code == 109 || code == 173|| code == 189;
	}
	public static boolean isZero(int code) {
		return code == 48 || code == 96;
	}
	public static boolean isZoomKey(int code) {
		return isPlus(code) || isMinus(code) || isZero(code);
	}
	
	public static boolean isSwitchToFullscreen(int code) {
		return code == 122; // F11
	}
}
