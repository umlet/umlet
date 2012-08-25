package com.baselet.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;

import com.baselet.diagram.draw.BaseDrawHandler;

/**
 * Cache for already known results of splitstrings
 */
public class TextManipulator {
	private static LinkedHashMap<SplitStringCacheKey, String> splitStringCache = new LinkedHashMap<SplitStringCacheKey, String>(100);

	public static boolean checkifStringFits(String text, float width, BaseDrawHandler drawer) {
		return splitString(text, width, drawer).equals(text);
	}
	
	public static String splitString(String text, float width, BaseDrawHandler drawer) {
		SplitStringCacheKey key = new SplitStringCacheKey(text, width);
		String result = splitStringCache.get(key);
		if (result != null) return result;

		result = splitStringAlgorithm(text, width, drawer);
		
		splitStringCache.put(key, result);
		return result;
	}
	
	private static String splitStringAlgorithm(String text, float width, BaseDrawHandler drawer) {
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
				if (!line.isEmpty()) line += splitChar; // if this is not the first line, start with a splitcharacter
				line += nextEl;
				inputIter.remove();
			}
			if (inputIter.hasNext() && line.isEmpty()) { // if the line has no space and would be to wide for one line
				String nextEl = inputIter.next();
				String possibleLine = nextEl;
				while (!possibleLine.isEmpty() && drawer.textWidth(possibleLine) > width) {
						possibleLine = possibleLine.substring(0, possibleLine.length()-1);
				}
				line = possibleLine;
			}
		return line;
	}

	private static class SplitStringCacheKey {
		private String input;
		private float width;

		public SplitStringCacheKey(String input, float width) {
			super();
			this.input = input;
			this.width = width;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((input == null) ? 0 : input.hashCode());
			result = prime * result + Float.floatToIntBits(width);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SplitStringCacheKey other = (SplitStringCacheKey) obj;
			if (input == null) {
				if (other.input != null)
					return false;
			} else if (!input.equals(other.input))
				return false;
			if (Float.floatToIntBits(width) != Float
					.floatToIntBits(other.width))
				return false;
			return true;
		}

	}

}