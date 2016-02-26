package com.baselet.diagram.draw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.StringStyle;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.control.enums.FormatLabels;
import com.baselet.diagram.draw.helper.Style;
import com.baselet.util.LRUCache;

/**
 * Based on the old TextSplitter, but offers additional features.
 * e.g. splitting a whole line into the parts
 * calculate the height
 * calculate minimum width of an text so it can be fully drawn
 *
 * If not stated otherwise all Strings supplied to the TextSplitter are interpreted and
 * the found markup is used to format the String (see {@link StringStyle}).
 * <br>
 * <i>Hint: The old TextSplitter can be found in the git history</i>
 * <pre>e.g. hash: 1320ae5858446795bccda8d2bb12d18664278886</pre>
 */
@SuppressWarnings("unused")
public class TextSplitter {

	private static final Logger log = LoggerFactory.getLogger(TextSplitter.class);

	// since the 2nd and 3rd cache use the value of the 1st as a partial key, the size shouldn't be too different
	// especially for the 2nd, the 3rd is bigger because there will be many different width value because of resize operations
	private static final int WORD_CACHE_SIZE = 180;
	private static final int MIN_WIDTH_CACHE_SIZE = 190;
	private static final int WORDWRAP_CACHE_SIZE = 400;

	private static final String SPLIT_CHARS = " \t";

	// 3 Caches are used
	// String line -> WordRegion[] words
	// WordRegion[] words + Style style + FormatLabels -> Double minWidth
	// WordRegion[] words + Style style + FormatLabels + Double width -> String[] wrappedLines + double height

	private static LinkedHashMap<String, WordRegion[]> wordCache = new LRUCache<String, WordRegion[]>(WORD_CACHE_SIZE);
	private static LinkedHashMap<MinWidthCacheKey, Double> minWidthCache = new LRUCache<MinWidthCacheKey, Double>(MIN_WIDTH_CACHE_SIZE);
	private static LinkedHashMap<WordwrapCacheKey, WordwrapCacheValue> wordwrapCache = new LRUCache<WordwrapCacheKey, WordwrapCacheValue>(WORDWRAP_CACHE_SIZE);

	/**
	 *
	 * @param drawer
	 * @param textLines each element is a line, must fit into width,height Rectangle
	 * @param topLeftX
	 * @param topLeftY
	 * @param width
	 * @param height
	 * @param hAlignment
	 * @param vAlignment
	 */
	public static void drawText(DrawHandler drawer, String[] textLines, double topLeftX, double topLeftY, double width, double height, AlignHorizontal hAlignment, AlignVertical vAlignment) {
		double textHeight = getSplitStringHeight(textLines, width, drawer);
		if (textHeight > height) {
			throw new IllegalArgumentException("The text needs more height then specified in the parameter");
		}
		switch (vAlignment) {
			case TOP:
				break;
			case CENTER:
				topLeftY += (height - textHeight) / 2.0;
				break;
			case BOTTOM:
				topLeftY += height - textHeight;
				break;
			default:
				log.error("Encountered unhandled enumeration value '" + vAlignment + "'.");
				break;
		}
		topLeftY += drawer.textHeightMax();
		switch (hAlignment) {
			case LEFT:
				break;
			case CENTER:
				topLeftX += width / 2.0;
				break;
			case RIGHT:
				topLeftX += width;
				break;
			default:
				log.error("Encountered unhandled enumeration value '" + hAlignment + "'.");
				break;
		}
		for (String l : textLines) {
			for (StringStyle wl : splitStringAlgorithm(l, width, drawer)) {
				drawer.print(wl, topLeftX, topLeftY, hAlignment);
				topLeftY += drawer.textHeightMaxWithSpace();
			}
		}

	}

	/**
	 * checks if the whole string would fit into the width
	 * @param text
	 * @param width
	 * @param drawer
	 * @return true if the whole string would fit into the width
	 */
	public static boolean checkifStringFitsNoWordwrap(String text, double width, DrawHandler drawer) {
		StringStyle analyzedText = StringStyle.analyzeFormatLabels(StringStyle.replaceNotEscaped(text));
		WordRegion[] words = getCachedWords(analyzedText.getStringWithoutMarkup()); // only check cache because we don't need the words
		if (words == null) {
			return drawer.textWidth(analyzedText.getStringWithoutMarkup()) + endBuffer(drawer) + 0.01 < width;
		}
		else {
			WordwrapCacheValue wwValue = getCachedWordwrap(words, width, drawer.getStyleClone(), analyzedText.getFormat());
			if (wwValue == null) {
				return drawer.textWidth(analyzedText.getStringWithoutMarkup()) + endBuffer(drawer) + 0.01 < width;
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
	 * @see #splitStringAndHeightAlgorithm(String, double, DrawHandler)
	 */
	public static double getSplitStringHeight(String text, double width, DrawHandler drawer) {
		return splitStringAndHeightAlgorithm(text, width, drawer).getHeight();
	}

	/**
	 * Splits each line so it can be drawn with the given width and then the height is calculated.
	 * It only call getSplitStringHeight(String, double, DrawHanlder) for each string and adds up the height
	 * @param textLines each element is a single line (no \r \n)
	 * @param width
	 * @param drawer
	 * @return the split text, each line as an element
	 *
	 * @see #splitStringAndHeightAlgorithm(String, double, DrawHandler)
	 */
	public static double getSplitStringHeight(String[] textLines, double width, DrawHandler drawer) {
		double height = 0;
		for (String l : textLines) {
			height += getSplitStringHeight(l, width, drawer);
		}
		return height;
	}

	/**
	 *
	 * @param text  a single line (no \r \n)
	 * @param width in which the text should be fitted, need to be &gt; the width of the 'n' character
	 * @param drawer
	 * @return the wrapped lines
	 */
	public static StringStyle[] splitStringAlgorithm(String text, double width, DrawHandler drawer) {
		return splitStringAndHeightAlgorithm(text, width, drawer).getWrappedLines();
	}

	/**
	 *
	 * @param text1  a single line (no \r \n)
	 * @param maxWidth in which the text should be fitted, need to be &gt; the width of the 'n' character
	 * @param drawer
	 * @param runtimeException if true then a runtime exception is thrown if a single word is to big for the given width
	 * @return
	 */
	private static WordwrapCacheValue splitStringAndHeightAlgorithm(String text, double maxWidth, DrawHandler drawer) {
		StringStyle analyzedText = StringStyle.analyzeFormatLabels(StringStyle.replaceNotEscaped(text));
		String finalText = analyzedText.getStringWithoutMarkup();
		WordRegion[] words = splitIntoWords(finalText);
		WordwrapCacheKey key = new WordwrapCacheKey(words, maxWidth, drawer.getStyleClone(), analyzedText.getFormat());
		WordwrapCacheValue cachedWordwrap = getCachedWordwrap(key);
		if (cachedWordwrap != null) {
			log.trace("got value from cache " + cachedWordwrap);
			return cachedWordwrap;
		}
		else {
			maxWidth -= endBuffer(drawer); // subtract a buffer to make sure no character is hidden at the end
			List<StringStyle> wrappedText = new LinkedList<StringStyle>();
			List<WordRegion> wordsList = new ArrayList<TextSplitter.WordRegion>(Arrays.asList(words));
			for (ListIterator<WordRegion> iter = wordsList.listIterator(); iter.hasNext();) {
				WordRegion currentRegion = iter.next();
				String currentWord = finalText.substring(currentRegion.getBegin(), currentRegion.getEnd());
				log.trace("current word: " + currentWord);
				// Case1: current word is too long for available width space
				if (!wordFits(maxWidth, drawer, currentWord)) {
					// remove character by character from the word until it fits the line
					int endIndex = currentRegion.getEnd();
					String partialWord = finalText.substring(currentRegion.getBegin(), endIndex);
					while (endIndex > 0 && !wordFits(maxWidth, drawer, partialWord)) {
						partialWord = finalText.substring(currentRegion.getBegin(), --endIndex);
					}
					// if there is space for at least one character (ie: beginIdx != endIdx), add the portion of the current word which fits to the wrappedText and handle the rest of the word in the next iteration
					if (currentRegion.getBegin() != endIndex) {
						wrappedText.add(new StringStyle(analyzedText.getFormat(), partialWord));
						iter.set(new WordRegion(endIndex, currentRegion.getEnd())); // overwrite this Wordregion with the new partial wordregion and handle it again
						iter.previous();
					}
				}
				// Case2: word fits the line but possibly more words would fit the line, therefore merge current WordRegion with next region until it's too long or there is no next region
				else {
					String mergedWord = currentWord;
					while (iter.hasNext() && wordFits(maxWidth, drawer, mergedWord)) {
						mergedWord = finalText.substring(currentRegion.getBegin(), iter.next().getEnd());
					}
					// Case2.1: all remaining words are merged and it still fits the width, therefore add the mergedWord
					if (wordFits(maxWidth, drawer, mergedWord)) {
						wrappedText.add(new StringStyle(analyzedText.getFormat(), mergedWord));
					}
					// Case2.2: the merged word grew too long, therefore add the merged word without the last merge and handle the remaining words in the next iteration of the main loop
					else {
						iter.previous(); // afterwards iter is between the fitting WordRegion and the one which makes the mergedWord too long
						WordRegion lastFittingRegion = iter.previous(); // gives the fitting WordRegion but also moves the iter before the region
						String substring = finalText.substring(currentRegion.getBegin(), lastFittingRegion.getEnd());
						wrappedText.add(new StringStyle(analyzedText.getFormat(), substring));
						iter.next(); // call next() to move the iter back between the fitting WordRegion and the one which makes the mergedWord too long
					}
				}
			}
			double height = wrappedText.size() * drawer.textHeightMaxWithSpace();
			WordwrapCacheValue wordwrapValue = new WordwrapCacheValue(wrappedText.toArray(new StringStyle[0]), height);
			setCachedWordwrap(key, wordwrapValue);
			if (log.isTraceEnabled()) {
				log.trace("split result: " + Arrays.toString(wordwrapValue.getWrappedLines()));
			}
			return wordwrapValue;
		}
	}

	private static boolean wordFits(double maxWidth, DrawHandler drawer, String word) {
		double width = drawer.textWidth(word);
		log.trace("checking if \"" + word + "\" with width " + width + " fits available space of " + maxWidth);
		return width <= maxWidth;
	}

	/**
	 *
	 * @param text  a single line (no \r \n)
	 * @param drawer
	 * @return the minimum width, which is needed to draw the text. This is based on the biggest word.
	 */
	public static double getTextMinWidth(String text, DrawHandler drawer) {
		StringStyle analyzedText = StringStyle.analyzeFormatLabels(StringStyle.replaceNotEscaped(text));
		MinWidthCacheKey key = new MinWidthCacheKey(splitIntoWords(analyzedText.getStringWithoutMarkup()),
				drawer.getStyleClone(), analyzedText.getFormat());
		if (getCachedMinWidth(key) != null) {
			return getCachedMinWidth(key);
		}
		else {
			double minWidth = 0;
			if (analyzedText.getStringWithoutMarkup().trim().length() > 0) {
				for (WordRegion wr : key.getWords()) {
					minWidth = Math.max(minWidth, drawer.textWidth(
							analyzedText.getStringWithoutMarkup().substring(wr.getBegin(), wr.getEnd())));
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

	private static void setCachedMinWidth(MinWidthCacheKey key, Double value) {
		minWidthCache.put(key, value);
	}

	private static WordwrapCacheValue getCachedWordwrap(WordwrapCacheKey key) {
		return wordwrapCache.get(key);
	}

	private static WordwrapCacheValue getCachedWordwrap(WordRegion[] words, double width, Style style, Set<FormatLabels> format) {
		return getCachedWordwrap(new WordwrapCacheKey(words, width, style, format));
	}

	private static void setCachedWordwrap(WordwrapCacheKey key, WordwrapCacheValue value) {
		wordwrapCache.put(key, value);
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

		@Override
		public String toString() {
			return "WordRegion [begin=" + begin + ", end=" + end + "]";
		}

	}

	private static class MinWidthCacheKey {
		private final WordRegion[] words;
		private final Style style; // must be part of key, because text width also depends on styling like fontsize
		private final Set<FormatLabels> format;

		public MinWidthCacheKey(WordRegion[] words, Style style, Set<FormatLabels> format) {
			super();
			this.words = words;
			this.style = style;
			this.format = format;
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
			result = prime * result + (format == null ? 0 : format.hashCode());
			result = prime * result + (style == null ? 0 : style.hashCode());
			result = prime * result + Arrays.hashCode(words);
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
			if (format == null) {
				if (other.format != null) {
					return false;
				}
			}
			else if (!format.equals(other.format)) {
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
			if (!Arrays.equals(words, other.words)) {
				return false;
			}
			return true;
		}

	}

	private static class WordwrapCacheKey {
		private final WordRegion[] words;
		private final double width;
		private final Style style; // must be part of key, because text width also depends on styling like fontsize
		private final Set<FormatLabels> format;

		public WordwrapCacheKey(WordRegion[] words, double width, Style style, Set<FormatLabels> format) {
			super();
			this.words = words;
			this.width = width;
			this.style = style;
			this.format = format;
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

		public Set<FormatLabels> getFormat() {
			return format;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (format == null ? 0 : format.hashCode());
			result = prime * result + (style == null ? 0 : style.hashCode());
			long temp;
			temp = Double.doubleToLongBits(width);
			result = prime * result + (int) (temp ^ temp >>> 32);
			result = prime * result + Arrays.hashCode(words);
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
			if (format == null) {
				if (other.format != null) {
					return false;
				}
			}
			else if (!format.equals(other.format)) {
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
			if (!Arrays.equals(words, other.words)) {
				return false;
			}
			return true;
		}

	}

	private static class WordwrapCacheValue {
		// Style is included although the format is already included in the key, because nearly every time the line are retrieved the style is needed
		private final StringStyle[] wrappedLines;
		private final double height;

		public WordwrapCacheValue(StringStyle[] wrappedLines, double height) {
			super();
			this.wrappedLines = wrappedLines;
			this.height = height;
		}

		public WordwrapCacheValue(String[] wrappedLines, Set<FormatLabels> format, double height) {
			super();
			this.wrappedLines = new StringStyle[wrappedLines.length];
			for (int i = 0; i < wrappedLines.length; i++) {
				this.wrappedLines[i] = new StringStyle(format, wrappedLines[i]);
			}
			this.height = height;
		}

		public StringStyle[] getWrappedLines() {
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
}