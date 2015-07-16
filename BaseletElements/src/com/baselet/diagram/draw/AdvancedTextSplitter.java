package com.baselet.diagram.draw;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import com.baselet.diagram.draw.helper.Style;

/**
 * Based on the TextSplitter, but offers additional features.
 * e.g. splitting on white spaces,
 * calculate minimum width of an text so it can be fully drawn
 *
 * @see TextSplitter
 */
public class AdvancedTextSplitter {

	private static final int CACHE_SIZE = 100;
	private static final float CACHE_LOAD_SIZE = 0.8f;

	private static final String SPLIT_CHARS = " \t\r\n";
	/**
	 * Cache for already known results of splitstrings
	 */
	private static LinkedHashMap<SplitStringCacheKey, String[]> splitStringCache = new LinkedHashMap<SplitStringCacheKey, String[]>(CACHE_SIZE, CACHE_LOAD_SIZE, true) {
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(java.util.Map.Entry<SplitStringCacheKey, String[]> eldest) {
			// if size >= capacity * loadfactor then rehashing occurs (see implementation of LinkedHashMap.addEntry)
			// this method is called before the size check, therefore remove the eldest entry if the threshold is reached
			return size() >= CACHE_SIZE * CACHE_LOAD_SIZE;
		};
	};

	public static boolean checkifStringFits(String text, double width, DrawHandler drawer) {
		return splitString(text, width, drawer).equals(text);
	}

	public static String[] splitString(String text, double width, DrawHandler drawer) {
		SplitStringCacheKey key = new SplitStringCacheKey(text, width, drawer.getStyleClone());
		String[] result = splitStringCache.get(key);
		if (result != null) {
			return result;
		}

		result = splitStringAlgorithm(text, width, drawer);

		splitStringCache.put(key, result);
		return result;
	}

	/**
	 * Splits the text so it can be drawn with the given width, if a single word would exceed the width it is truncated.
	 * @param text
	 * @param width
	 * @param drawer
	 * @return the split text, each line as an element
	 *
	 * @see #splitStringAlgorithm(String, double, DrawHandler, boolean)
	 */
	private static String[] splitStringAlgorithm(String text, double width, DrawHandler drawer) {
		return splitStringAlgorithm(text, width, drawer, false);
	}

	/**
	 *
	 * @param text
	 * @param width in which the text should be fitted, need to be &gt; the width of the 'n' character
	 * @param drawer
	 * @param runtimeException if true then a runtime exception is thrown if a single word is to big for the given width
	 * @return
	 */
	private static String[] splitStringAlgorithm(String text, double width, DrawHandler drawer, boolean runtimeException) {
		WordRegion[] words = null; // TODO check cache
		if (words == null) {
			words = splitIntoWords(text);
		}
		if (words.length > 0) {
			width -= endBuffer(drawer); // subtract a buffer to make sure no character is hidden at the end (borrowed from TextSplitter)
			if (width <= 0) {
				throw new IllegalArgumentException("The width needs to be bigger then the size of a 'n' character.");
			}

			List<String> wrappedText = new LinkedList<String>();

			int lineStart = 0;
			for (int i = 0; i < words.length; i++) {
				if (drawer.textWidth(text.substring(words[lineStart].getBegin(), words[i].getEnd())) > width) {
					if (lineStart == i) {
						// the single word doesn't fit into the width!
						if (runtimeException) {
							throw new RuntimeException("At least one word is to big for the specified width!");
						}
						else
						{
							int endIndex = words[lineStart].getEnd() - 1;
							while (drawer.textWidth(text.substring(words[lineStart].getBegin(), endIndex)) > width) {
								endIndex--;
							}
							wrappedText.add(text.substring(words[lineStart].getBegin(), endIndex));
							lineStart = i;
						}
					}
					else {
						wrappedText.add(text.substring(words[lineStart].getBegin(), words[i - 1].getEnd()));
						lineStart = i;
					}
				}
			}
			wrappedText.add(text.substring(words[lineStart].getBegin(), words[words.length - 1].getEnd()));

			return wrappedText.toArray(new String[0]);
		}
		else {
			return new String[] { "" };
		}
	}

	public static double getTextMinWidth(String text, DrawHandler drawer) {
		double minWidth = 0;
		if (text.trim().length() > 0) {
			// TODO caching: from text to minWidth and/or words
			for (WordRegion wr : splitIntoWords(text))
			{
				for (int i = 0; i < text.length(); i++) {
					minWidth = Math.max(minWidth, getTextMinWidth(text.substring(wr.getBegin(), wr.getEnd()), drawer));
				}
			}
		}
		// add the Buffer and small number, so the text can be drawn with the returned width (see splitStringAlgorithm)
		return minWidth + endBuffer(drawer) + 0.01;
	}

	// string array is treated as if every string would be passed indivual into the method, cache single line!
	public static double getTextMinWidth(String[] textLines, DrawHandler drawer) {
		double minWidth = 0;
		for (String line : textLines) {
			minWidth = Math.max(minWidth, getTextMinWidth(line, drawer));
		}
		return minWidth;
	}

	/**
	 *
	 * @param text
	 * @return all the words which are separated by whitespaces (first word contains all leading whitespaces)
	 */
	private static WordRegion[] splitIntoWords(String text) {
		// TODO caching
		WordRegion[] word = new WordRegion[0];
		if (text.trim().length() > 0) {
			int wordStart = 0;
			int current = 0;
			// add the leading white spaces to the first word to keep indentation
			while (isWhitespace(text.charAt(current))) {
				current++;
			}
			current++;
			boolean inWord = true;
			for (; current < text.length(); current++) {
				if (inWord) {
					if (isWhitespace(text.charAt(current))) {
						word = Arrays.copyOf(word, word.length + 1);
						word[word.length - 1] = new WordRegion(wordStart, current);
						inWord = false;
					}
				}
				else {
					if (!isWhitespace(text.charAt(current))) {
						wordStart = current;
						inWord = true;
					}
				}
			}
			// if the last word isn't followed by a whitespace it won't get added in the loop
			if (inWord) {
				word = Arrays.copyOf(word, word.length + 1);
				word[word.length - 1] = new WordRegion(wordStart, current);
			}

		}
		return word;
	}

	private static boolean isWhitespace(char c) {
		for (int i = 0; i < SPLIT_CHARS.length(); i++) {
			if (SPLIT_CHARS.charAt(i) == c) {
				return true;
			}
		}
		return false;
	}

	private static double endBuffer(DrawHandler drawer) {
		// used to subtract a buffer to make sure no character is hidden at the end (borrowed from TextSplitter)
		return drawer.textWidth("n");
	}

	/**
	 * Contains the start and end of a word, can be directly used with substring
	 */
	private static class WordRegion {
		private final int begin;
		private final int end; // last character at end - 1. Thus the length is end-begin.

		public WordRegion(int begin, int end) {
			super();
			this.begin = begin;
			this.end = end;
		}

		public int getBegin() {
			return begin;
		}

		public int getEnd() {
			return end;
		}
	}

	private static class SplitStringCacheKey {
		private final String input;
		private final double width;
		private final Style style; // must be part of key, because text width also depends on styling like fontsize

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

	private static class SplitStringCacheValue {
		// final String[] splitted;
		// final double minWidth;

	}
}