package com.baselet.gwt.client.keyboard;

/**
 * helper class to detect keycodes
 * see http://www.javascripter.net/faq/keycodes.htm (not up to date because it doesnt list 171 as +)
 * and http://www.w3.org/2002/09/tests/keys.html
 */
class KeyCodesExt {

	static boolean isPlus(int code) {
		return code == 61 || code == 187 || code == 107 || code == 171;
	}

	static boolean isMinus(int code) {
		return code == 109 || code == 173 || code == 189;
	}

	static boolean isZero(int code) {
		return code == 48 || code == 96;
	}

	static boolean isSpace(int code) {
		return code == 32;
	}

	static boolean isSwitchToFullscreen(int code) {
		return code == 122; // F11
	}
}
