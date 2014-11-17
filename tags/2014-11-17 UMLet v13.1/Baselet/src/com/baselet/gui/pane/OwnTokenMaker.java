/* Based on WindowsBatchTokenMaker.java - Scanner for Windows batch files. This library is distributed under a modified BSD license. See the included RSyntaxTextArea.License.txt file for details. */
package com.baselet.gui.pane;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;
import org.fife.ui.rsyntaxtextarea.TokenTypes;

/**
 * see http://fifesoft.com/rsyntaxtextarea/doc/CustomSyntaxHighlighting.html for more infos about own syntax
 */
public class OwnTokenMaker extends AbstractTokenMaker {

	private static TokenMap wordsToHighlight = new TokenMap();

	public static void setMyWordsToHighlight(TokenMap myWordsToHighlight) {
		OwnTokenMaker.wordsToHighlight = myWordsToHighlight;
	}

	public static final String ID = "OwnTokenMaker";

	@Override
	public TokenMap getWordsToHighlight() {
		return wordsToHighlight;
	}

	@Override
	public void addToken(Segment segment, int start, int end, int tokenType, int startOffset) {
		if (tokenType == TokenTypes.IDENTIFIER) {
			int value = getWordsToHighlight().get(segment, start, end);
			if (value != -1) {
				tokenType = value;
			}
		}
		super.addToken(segment, start, end, tokenType, startOffset);
	}

	/**
	 * Returns a list of tokens representing the given text.
	 * Based on http://fifesoft.com/rsyntaxtextarea/doc/CustomSyntaxHighlighting.html but reduced to our highlighting
	 *
	 * @param text The text to break into tokens.
	 * @param startTokenType The token with which to start tokenizing.
	 * @param startOffset The offset at which the line of tokens begins.
	 * @return A linked list of tokens representing <code>text</code>.
	 */
	@Override
	public Token getTokenList(Segment text, int startTokenType, int startOffset) {
		resetTokenList();

		int offset = text.offset;
		int count = text.count;
		int end = offset + count;

		int newStartOffset = startOffset - offset;

		int currentTokenStart = offset;
		int currentTokenType = startTokenType;

		for (int i = offset; i < end; i++) {
			if (currentTokenType == TokenTypes.NULL) {
				currentTokenType = TokenTypes.IDENTIFIER;
			}

		}

		addToken(text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart);
		addNullToken();

		return firstToken;
	}
}