package com.baselet.gwt.client.view.utils;

/*File: Math.uuid.js Version: 1.3 Change History: v1.0 - first release v1.1 - less code and 2x performance boost (by minimizing calls to Math.random()) v1.2 - Add support for generating non-standard uuids of arbitrary length v1.3 - Fixed IE7 bug (can't use []'s to access string chars. Thanks, Brian R.) v1.4 - Changed method to be "Math.uuid". Added support for radix argument. Use module pattern for better encapsulation. Latest version: http://www.broofa.com/Tools/Math.uuid.js Information: http://www.broofa.com/blog/?p=151 Contact: robert@broofa.com ---- Copyright (c) 2008, Robert Kieffer All rights reserved. Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. Neither the name of Robert Kieffer nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

public class UUID {
	private static final char[] CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	/**
	 * Generate a random uuid of the specified length. Example: uuid(15) returns
	 * "VcydxgltxrVZSTV"
	 *
	 * @param len
	 *            the desired number of characters
	 */
	public static String uuid(int len) {
		return uuid(len, CHARS.length);
	}

	/**
	 * Generate a random uuid of the specified length, and radix. Examples:
	 * <ul>
	 * <li>uuid(8, 2) returns "01001010" (8 character ID, base=2)
	 * <li>uuid(8, 10) returns "47473046" (8 character ID, base=10)
	 * <li>uuid(8, 16) returns "098F4D35" (8 character ID, base=16)
	 * </ul>
	 *
	 * @param len
	 *            the desired number of characters
	 * @param radix
	 *            the number of allowable values for each character (must be <=
	 *            62)
	 */
	public static String uuid(int len, int radix) {
		if (radix > CHARS.length) {
			throw new IllegalArgumentException();
		}
		char[] uuid = new char[len];
		// Compact form
		for (int i = 0; i < len; i++) {
			uuid[i] = CHARS[(int) (Math.random() * radix)];
		}
		return new String(uuid);
	}

	/**
	 * Generate a RFC4122, version 4 ID. Example:
	 * "92329D39-6F5C-4520-ABFC-AAB64544E172"
	 */
	public static String uuid() {
		char[] uuid = new char[36];
		int r;

		// rfc4122 requires these characters
		uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
		uuid[14] = '4';

		// Fill in random data. At i==19 set the high bits of clock sequence as
		// per rfc4122, sec. 4.1.5
		for (int i = 0; i < 36; i++) {
			if (uuid[i] == 0) {
				r = (int) (Math.random() * 16);
				uuid[i] = CHARS[i == 19 ? r & 0x3 | 0x8 : r & 0xf];
			}
		}
		return new String(uuid);
	}
}
