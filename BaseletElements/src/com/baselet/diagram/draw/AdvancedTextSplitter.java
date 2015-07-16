package com.baselet.diagram.draw;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import com.baselet.diagram.draw.helper.Style;

/**
 * Based on the TextSplitter, but offers additional features.
 * e.g. splitting a whole line into the parts
 * calculate the height
 * calculate minimum width of an text so it can be fully drawn
 *
 * @see TextSplitter
 */
@SuppressWarnings("unused")
public class AdvancedTextSplitter {

	// since the 2nd and 3rd cache use the value of the 1st as a partial key, the size shouldn't be too different
	private static final int WORD_CACHE_SIZE = 180;
	private static final int MIN_WIDTH_CACHE_SIZE = 190;
	private static final int WORDWRAP_CACHE_SIZE = 200;

	private static final String SPLIT_CHARS = " \t";

	// 3 Caches are used
	// String line -> WordRegion[] words
	// WordRegion[] words + Style style -> Double minWidth
	// WordRegion[] words + Style style + Double width -> String[] wrappedLines + double height

	private static LinkedHashMap<String, WordRegion[]> wordCache = new LRUCache<String, WordRegion[]>(WORD_CACHE_SIZE);
	private static LinkedHashMap<MinWidthCacheKey, Double> minWidthCache = new LRUCache<MinWidthCacheKey, Double>(MIN_WIDTH_CACHE_SIZE);
	private static LinkedHashMap<WordwrapCacheKey, WordwrapCacheValue> wordwrapCache = new LRUCache<WordwrapCacheKey, WordwrapCacheValue>(WORDWRAP_CACHE_SIZE);

	/**
	 * checks if the whole string would fit into the width
	 * @param text
	 * @param width
	 * @param drawer
	 * @return true if the whole string would fit into the width
	 */
	public static boolean checkifStringFitsNoWordwrap(String text, double width, DrawHandler drawer) {
		WordRegion[] words = getCachedWords(text); // only check cache because we don't need the words
		if (words == null) {
			return drawer.textWidth(text) + endBuffer(drawer) + 0.01 < width;
		}
		else
		{
			WordwrapCacheValue wwValue = getCachedWordwrap(words, width, drawer.getStyleClone());
			if (wwValue == null) {
				return drawer.textWidth(text) + endBuffer(drawer) + 0.01 < width;
			}
			else {
				return wwValue.getWrappedLines().length < 2; // if only 1 line was generated then it fits
			}
		}
	}

	/**
	 * checks if the minimum width exceeds the given width
	 * @param text
	 * @param width
	 * @param drawer
	 * @return true if the minimum width does not exceed the given width
	 */
	public static boolean checkifStringFitsWithWordwrap(String text, double width, DrawHandler drawer) {
		return getTextMinWidth(text, drawer) < width; // generate the words and min width (or take them from cache)
	}

	/**
	 * Splits the text so it can be drawn with the given width and then the height is calculated.
	 * @param text a single line (no \r \n)
	 * @param width
	 * @param drawer
	 * @return the split text, each line as an element
	 *
	 * @see #splitStringAndHeightAlgorithm(String, double, DrawHandler, boolean)
	 */
	public static double getSplitStringHeight(String text, double width, DrawHandler drawer) {
		return splitStringAndHeightAlgorithm(text, width, drawer, false).getHeight();
	}

	/**
	 * Splits the text so it can be drawn with the given width, if a single word would exceed the width it is truncated.
	 * @param text a single line (no \r \n)
	 * @param width
	 * @param drawer
	 * @return the split text, each line as an element
	 *
	 * @see #splitStringAlgorithm(String, double, DrawHandler, boolean)
	 */
	public static String[] splitStringAlgorithm(String text, double width, DrawHandler drawer) {
		return splitStringAlgorithm(text, width, drawer, false);
	}

	/**
	 *
	 * @param text  a single line (no \r \n)
	 * @param width in which the text should be fitted, need to be &gt; the width of the 'n' character
	 * @param drawer
	 * @param runtimeException if true then a runtime exception is thrown if a single word is to big for the given width
	 * @return
	 */
	public static String[] splitStringAlgorithm(String text, double width, DrawHandler drawer, boolean runtimeException) {
		return splitStringAndHeightAlgorithm(text, width, drawer, runtimeException).getWrappedLines();
	}

	/**
	 *
	 * @param text  a single line (no \r \n)
	 * @param width in which the text should be fitted, need to be &gt; the width of the 'n' character
	 * @param drawer
	 * @param runtimeException if true then a runtime exception is thrown if a single word is to big for the given width
	 * @return
	 */
	private static WordwrapCacheValue splitStringAndHeightAlgorithm(String text, double width, DrawHandler drawer, boolean runtimeException) {
		WordRegion[] words = splitIntoWords(text);
		WordwrapCacheKey key = new WordwrapCacheKey(words, width, drawer.getStyleClone());
		if (getCachedWordwrap(key) != null) {
			return getCachedWordwrap(key);
		}
		else {
			List<String> wrappedText = new LinkedList<String>();
			if (words.length > 0) {
				width -= endBuffer(drawer); // subtract a buffer to make sure no character is hidden at the end (borrowed from TextSplitter)
				if (width <= 0) {
					throw new IllegalArgumentException("The width needs to be bigger then the size of a 'n' character.");
				}

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
			}
			else {
				wrappedText.add("");
			}
			double height = wrappedText.size() * drawer.textHeightMaxWithSpace();
			WordwrapCacheValue wordwrapValue = new WordwrapCacheValue(wrappedText.toArray(new String[0]), height);
			setCachedWordwrap(key, wordwrapValue);
			return wordwrapValue;
		}
	}

	/**
	 *
	 * @param text  a single line (no \r \n)
	 * @param drawer
	 * @return the minimum width, which is needed to draw the text. This is based on the biggest word.
	 */
	public static double getTextMinWidth(String text, DrawHandler drawer) {
		MinWidthCacheKey key = new MinWidthCacheKey(splitIntoWords(text), drawer.getStyleClone());
		if (getCachedMinWidth(key) != null) {
			return getCachedMinWidth(key);
		}
		else {
			double minWidth = 0;
			if (text.trim().length() > 0) {
				for (WordRegion wr : key.getWords())
				{
					for (int i = 0; i < text.length(); i++) {
						minWidth = Math.max(minWidth, getTextMinWidth(text.substring(wr.getBegin(), wr.getEnd()), drawer));
					}
				}
			}
			// add the Buffer and small number, so the text can be drawn with the returned width (see splitStringAlgorithm)
			minWidth += endBuffer(drawer) + 0.01;
			setCachedMinWidth(key, minWidth);
			return minWidth;
		}
	}

	/**
	 * Returns the minimum width which is needed to draw the given lines
	 * @param textLines each element must be a single line (no \r \n)
	 * @param drawer
	 * @return the minimum width which is needed to draw the given lines
	 */
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
		WordRegion[] words = getCachedWords(text);
		if (words == null) {
			words = new WordRegion[0];
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
							words = Arrays.copyOf(words, words.length + 1);
							words[words.length - 1] = new WordRegion(wordStart, current);
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
					words = Arrays.copyOf(words, words.length + 1);
					words[words.length - 1] = new WordRegion(wordStart, current);
				}

			}
			setCachedWords(text, words);
		}
		return words;
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

	// cache functions, so that the implementation (structure) of the cache can be changed
	// all functions will return null if no element was found

	private static WordRegion[] getCachedWords(String lineKey) {
		return wordCache.get(lineKey);
	}

	private static void setCachedWords(String lineKey, WordRegion[] words) {
		wordCache.put(lineKey, words);
	}

	private static Double getCachedMinWidth(MinWidthCacheKey key) {
		return minWidthCache.get(key);
	}

	private static Double getCachedMinWidth(WordRegion[] words, Style style) {
		return getCachedMinWidth(new MinWidthCacheKey(words, style));
	}

	private static void setCachedMinWidth(MinWidthCacheKey key, Double value) {
		minWidthCache.put(key, value);
	}

	private static WordwrapCacheValue getCachedWordwrap(WordwrapCacheKey key) {
		return wordwrapCache.get(key);
	}

	private static WordwrapCacheValue getCachedWordwrap(WordRegion[] words, double width, Style style) {
		return getCachedWordwrap(new WordwrapCacheKey(words, width, style));
	}

	private static void setCachedWordwrap(WordwrapCacheKey key, WordwrapCacheValue value) {
		wordwrapCache.put(key, value);
	}

	private static void setCachedWordwrap(WordwrapCacheKey key, String[] wrappedLines, double height) {
		wordwrapCache.put(key, new WordwrapCacheValue(wrappedLines, height));
	}

	private static void setCachedWordwrap(WordRegion[] words, double width, Style style, String[] wrappedLines, double height) {
		wordwrapCache.put(new WordwrapCacheKey(words, width, style), new WordwrapCacheValue(wrappedLines, height));
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

	private static class MinWidthCacheKey {
		private final WordRegion[] words;
		private final Style style; // must be part of key, because text width also depends on styling like fontsize

		public MinWidthCacheKey(WordRegion[] words, Style style) {
			super();
			this.words = words;
			this.style = style;
		}

		public WordRegion[] getWords() {
			return words;
		}

		public Style getStyle() {
			return style;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (words == null ? 0 : words.hashCode());
			result = prime * result + (style == null ? 0 : style.hashCode());
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
			MinWidthCacheKey other = (MinWidthCacheKey) obj;
			if (words == null) {
				if (other.words != null) {
					return false;
				}
			}
			else if (!words.equals(other.words)) {
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
			return true;
		}

	}

	private static class WordwrapCacheKey {
		private final WordRegion[] words;
		private final double width;
		private final Style style; // must be part of key, because text width also depends on styling like fontsize

		public WordwrapCacheKey(WordRegion[] words, double width, Style style) {
			super();
			this.words = words;
			this.width = width;
			this.style = style;
		}

		public WordRegion[] getWords() {
			return words;
		}

		public double getWidth() {
			return width;
		}

		public Style getStyle() {
			return style;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (words == null ? 0 : words.hashCode());
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
			WordwrapCacheKey other = (WordwrapCacheKey) obj;
			if (words == null) {
				if (other.words != null) {
					return false;
				}
			}
			else if (!words.equals(other.words)) {
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

	private static class WordwrapCacheValue {
		private final String[] wrappedLines;
		private final double height;

		public WordwrapCacheValue(String[] wrappedLines, double height) {
			super();
			this.wrappedLines = wrappedLines;
			this.height = height;
		}

		public String[] getWrappedLines() {
			return wrappedLines;
		}

		public double getHeight() {
			return height;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			long temp;
			temp = Double.doubleToLongBits(height);
			result = prime * result + (int) (temp ^ temp >>> 32);
			result = prime * result + Arrays.hashCode(wrappedLines);
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
			WordwrapCacheValue other = (WordwrapCacheValue) obj;
			if (Double.doubleToLongBits(height) != Double.doubleToLongBits(other.height)) {
				return false;
			}
			if (!Arrays.equals(wrappedLines, other.wrappedLines)) {
				return false;
			}
			return true;
		}
	}

	private static class LRUCache<K, V> extends LinkedHashMap<K, V> {

		private static final long serialVersionUID = 1L;
		private static final float LOAD_FACTOR = 0.8f;

		private final int cacheSize;

		/**
		 *
		 * @param cacheSize how many elements should fit in the cache (the actual capacity of the map may be bigger)
		 */
		public LRUCache(int cacheSize) {
			super((int) ((cacheSize + 1) / LOAD_FACTOR) + 1, LOAD_FACTOR, true);
			this.cacheSize = cacheSize;
		}

		@Override
		protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
			// if size >= capacity * loadfactor then rehashing occurs (see implementation of LinkedHashMap.addEntry)
			// this method is called before the size check, therefore remove the eldest entry if the threshold is reached
			return size() >= this.cacheSize;
		};

	}
}