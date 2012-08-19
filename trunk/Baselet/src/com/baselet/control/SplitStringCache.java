package com.baselet.control;

import java.util.LinkedHashMap;

import com.baselet.diagram.draw.BaseDrawHandler;

/**
 * Cache for already known results of splitstrings
 */
public class SplitStringCache {
	private static LinkedHashMap<SplitStringCacheKey, String> splitStringCache = new LinkedHashMap<SplitStringCacheKey, String>(100);

	public static String getOutput(String input, float width, BaseDrawHandler drawer) {
		SplitStringCacheKey key = new SplitStringCacheKey(input, width);
		String result = splitStringCache.get(key);
		if (result != null) return result;

		String possibleOutput = input;
		while (drawer.textWidth(possibleOutput) > width) {
			possibleOutput = possibleOutput.substring(0, possibleOutput.length()-1);
		}
		splitStringCache.put(key, possibleOutput);
		return possibleOutput;
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