package com.baselet.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.ListIterator;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.Style;

public class TextSplitter {

	/**
	 * Cache for already known results of splitstrings
	 */
	private static LinkedHashMap<SplitStringCacheKey, String> splitStringCache = new LinkedHashMap<SplitStringCacheKey, String>(100);

	public static boolean checkifStringFits(String text, double width, DrawHandler drawer) {
		return splitString(text, width, drawer).equals(text);
	}

	public static String splitString(String text, double width, DrawHandler drawer) {
		SplitStringCacheKey key = new SplitStringCacheKey(text, width, drawer.getStyle());
		String result = splitStringCache.get(key);
		if (result != null) {
			return result;
		}

		result = splitStringAlgorithm(text, width, drawer);

		splitStringCache.put(key, result);
		return result;
	}

	private static String splitStringAlgorithm(String text, double width, DrawHandler drawer) {
		String splitChar = " ";
		width -= drawer.textWidth("n"); // subtract a buffer to make sure no character is hidden at the end
		ListIterator<String> inputIter = new ArrayList<String>(Arrays.asList(text.split(splitChar, -1))).listIterator(); // split limit is -1 to retain spaces at the end of the string
		String line = "";
		while (inputIter.hasNext()) {
			String nextEl = inputIter.next();
			if (drawer.textWidth(line + nextEl) > width) {
				inputIter.previous();
				break;
			}
			line += nextEl + splitChar;
			inputIter.remove();
		}
		if (!line.isEmpty()) { // cut the last splitChar
			line = line.substring(0, line.length() - 1);
		}
		if (inputIter.hasNext() && line.isEmpty()) { // if the line has no space and would be to wide for one line
			String nextEl = inputIter.next();
			String possibleLine = nextEl;
			while (!possibleLine.isEmpty() && drawer.textWidth(possibleLine) > width) {
				possibleLine = possibleLine.substring(0, possibleLine.length() - 1);
			}
			line = possibleLine;
		}
		return line;
	}

	private static class SplitStringCacheKey {
		private String input;
		private double width;
		private Style style; // must be part of key, because text width also depends on styling like fontsize

		public SplitStringCacheKey(String input, double width, Style style) {
			super();
			this.input = input;
			this.width = width;
			this.style = style;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (input == null ? 0 : input.hashCode());
			result = prime * result + (style == null ? 0 : style.hashCode());
			long temp;
			temp = Double.doubleToLongBits(width);
			result = prime * result + (int) (temp ^ temp >>> 32);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			SplitStringCacheKey other = (SplitStringCacheKey) obj;
			if (input == null) {
				if (other.input != null) {
					return false;
				}
			}
			else if (!input.equals(other.input)) {
				return false;
			}
			if (style == null) {
				if (other.style != null) {
					return false;
				}
			}
			else if (!style.equals(other.style)) {
				return false;
			}
			if (Double.doubleToLongBits(width) != Double.doubleToLongBits(other.width)) {
				return false;
			}
			return true;
		}

	}

}