/*
 * Based on WindowsBatchTokenMaker.java - Scanner for Windows batch files.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package com.baselet.gui;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;
import org.fife.ui.rsyntaxtextarea.TokenTypes;

/**
 * see http://fifesoft.com/forum/viewtopic.php?f=10&t=268&hilit=tokenmaker for more infos about own syntax
 */
public class OwnTokenMaker extends AbstractTokenMaker {

	private static TokenMap wordsToHighlight = new TokenMap();
	
	public static void setMyWordsToHighlight(TokenMap myWordsToHighlight) {
		OwnTokenMaker.wordsToHighlight = myWordsToHighlight;
	}
	
	public static final String ID = "OwnTokenMaker";

	@Override
	public void addToken(char[] array, int start, int end, int tokenType, int startOffset) {
		int value = wordsToHighlight.get(array, start, end);
		// only highlight if keyword is on its own line
		boolean isOnOwnLine = (start == 0 || array[start - 1] == '\n');
		if (value != -1 && isOnOwnLine) {
			tokenType = value;
		}
		else tokenType = TokenTypes.IDENTIFIER; // default type is IDENTIFIER (which is just black)
		super.addToken(array, start, end, tokenType, startOffset);
	}

	@Override
	public String[] getLineCommentStartAndEnd() {
		return new String[] { "// ", null }; // comments start with //
	}

	@Override
	public boolean getMarkOccurrencesOfTokenType(int type) {
		return false; // we don't mark occurrences of any token type
	}

	@Override
	public TokenMap getWordsToHighlight() {
		return wordsToHighlight;
	}

	/** THE CODE FROM HERE IS BASED ON WindowsBatchTokenMaker.java TO MAKE getTokenList() WORK **/ 
	
	private int currentTokenStart;
	private int currentTokenType;
	private boolean bracketVariable;

	/**
	 * Returns a list of tokens representing the given text.
	 *
	 * @param text The text to break into tokens.
	 * @param startTokenType The token with which to start tokenizing.
	 * @param startOffset The offset at which the line of tokens begins.
	 * @return A linked list of tokens representing <code>text</code>.
	 */
	@SuppressWarnings("static-access")
	@Override
	public Token getTokenList(Segment text, int startTokenType, final int startOffset) {

		resetTokenList();

		char[] array = text.array;
		int offset = text.offset;
		int count = text.count;
		int end = offset + count;

		// See, when we find a token, its starting position is always of the form:
		// 'startOffset + (currentTokenStart-offset)'; but since startOffset and
		// offset are constant, tokens' starting positions become:
		// 'newStartOffset+currentTokenStart' for one less subtraction operation.
		int newStartOffset = startOffset - offset;

		currentTokenStart = offset;
		currentTokenType = startTokenType;

		// beginning:
		for (int i = offset; i < end; i++) {

			char c = array[i];

			switch (currentTokenType) {

				case Token.NULL:

					currentTokenStart = i; // Starting a new token here.

					switch (c) {

						case ' ':
						case '\t':
							currentTokenType = Token.WHITESPACE;
							break;

						case '"':
							currentTokenType = Token.ERROR_STRING_DOUBLE;
							break;

						case '%':
							currentTokenType = Token.VARIABLE;
							break;

						// The "separators".
						case '(':
						case ')':
							addToken(text, currentTokenStart, i, Token.SEPARATOR, newStartOffset + currentTokenStart);
							currentTokenType = Token.NULL;
							break;

						// The "separators2".
						case ',':
						case ';':
							addToken(text, currentTokenStart, i, Token.IDENTIFIER, newStartOffset + currentTokenStart);
							currentTokenType = Token.NULL;
							break;

						// Newer version of EOL comments, or a label
						case ':':
							// If this will be the first token added, it is
							// a new-style comment or a label
							if (firstToken == null) {
								if (i < end - 1 && array[i + 1] == ':') { // new-style comment
									currentTokenType = Token.COMMENT_EOL;
								}
								else { // Label
									currentTokenType = Token.PREPROCESSOR;
								}
							}
							else { // Just a colon
								currentTokenType = Token.IDENTIFIER;
							}
							break;

						default:

							// Just to speed things up a tad, as this will usually be the case (if spaces above failed).
							if (RSyntaxUtilities.isLetterOrDigit(c) || c == '\\') {
								currentTokenType = Token.IDENTIFIER;
								break;
							}

							currentTokenType = Token.IDENTIFIER;
							break;

					} // End of switch (c).

					break;

				case Token.WHITESPACE:

					switch (c) {

						case ' ':
						case '\t':
							break; // Still whitespace.

						case '"':
							addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = Token.ERROR_STRING_DOUBLE;
							break;

						case '%':
							addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = Token.VARIABLE;
							break;

						// The "separators".
						case '(':
						case ')':
							addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
							addToken(text, i, i, Token.SEPARATOR, newStartOffset + i);
							currentTokenType = Token.NULL;
							break;

						// The "separators2".
						case ',':
						case ';':
							addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
							addToken(text, i, i, Token.IDENTIFIER, newStartOffset + i);
							currentTokenType = Token.NULL;
							break;

						// Newer version of EOL comments, or a label
						case ':':
							addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
							currentTokenStart = i;
							// If the previous (whitespace) token was the first token
							// added, this is a new-style comment or a label
							if (firstToken.getNextToken() == null) {
								if (i < end - 1 && array[i + 1] == ':') { // new-style comment
									currentTokenType = Token.COMMENT_EOL;
								}
								else { // Label
									currentTokenType = Token.PREPROCESSOR;
								}
							}
							else { // Just a colon
								currentTokenType = Token.IDENTIFIER;
							}
							break;

						default: // Add the whitespace token and start anew.

							addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
							currentTokenStart = i;

							// Just to speed things up a tad, as this will usually be the case (if spaces above failed).
							if (RSyntaxUtilities.isLetterOrDigit(c) || c == '\\') {
								currentTokenType = Token.IDENTIFIER;
								break;
							}

							currentTokenType = Token.IDENTIFIER;

					} // End of switch (c).

					break;

				default: // Should never happen
				case Token.IDENTIFIER:

					switch (c) {

						case ' ':
						case '\t':
							// Check for REM comments.
							if (i - currentTokenStart == 3 && (array[i - 3] == 'r' || array[i - 3] == 'R') && (array[i - 2] == 'e' || array[i - 2] == 'E') && (array[i - 1] == 'm' || array[i - 1] == 'M')) {
								currentTokenType = Token.COMMENT_EOL;
								break;
							}
							addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = Token.WHITESPACE;
							break;

						case '"':
							addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = Token.ERROR_STRING_DOUBLE;
							break;

						case '%':
							addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
							currentTokenStart = i;
							currentTokenType = Token.VARIABLE;
							break;

						// Should be part of identifiers, but not at end of "REM".
						case '\\':
							// Check for REM comments.
							if (i - currentTokenStart == 3 && (array[i - 3] == 'r' || array[i - 3] == 'R') && (array[i - 2] == 'e' || array[i - 2] == 'E') && (array[i - 1] == 'm' || array[i - 1] == 'M')) {
								currentTokenType = Token.COMMENT_EOL;
							}
							break;

						case '.':
						case '_':
							break; // Characters good for identifiers.

						// The "separators".
						case '(':
						case ')':
							addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
							addToken(text, i, i, Token.SEPARATOR, newStartOffset + i);
							currentTokenType = Token.NULL;
							break;

						// The "separators2".
						case ',':
						case ';':
							addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
							addToken(text, i, i, Token.IDENTIFIER, newStartOffset + i);
							currentTokenType = Token.NULL;
							break;

						default:

							// Just to speed things up a tad, as this will usually be the case.
							if (RSyntaxUtilities.isLetterOrDigit(c) || c == '\\') {
								break;
							}

							// Otherwise, fall through and assume we're still okay as an IDENTIFIER...

					} // End of switch (c).

					break;

				case Token.COMMENT_EOL:
					i = end - 1;
					addToken(text, currentTokenStart, i, Token.COMMENT_EOL, newStartOffset + currentTokenStart);
					// We need to set token type to null so at the bottom we don't add one more token.
					currentTokenType = Token.NULL;
					break;

				case Token.PREPROCESSOR: // Used for labels
					i = end - 1;
					addToken(text, currentTokenStart, i, Token.PREPROCESSOR, newStartOffset + currentTokenStart);
					// We need to set token type to null so at the bottom we don't add one more token.
					currentTokenType = Token.NULL;
					break;

				case Token.ERROR_STRING_DOUBLE:

					if (c == '"') {
						addToken(text, currentTokenStart, i, Token.LITERAL_STRING_DOUBLE_QUOTE, newStartOffset + currentTokenStart);
						currentTokenStart = i + 1;
						currentTokenType = Token.NULL;
					}
					// Otherwise, we're still an unclosed string...

					break;

				case Token.VARIABLE:

					if (i == currentTokenStart + 1) { // first character after '%'.
						bracketVariable = false;
						switch (c) {
							case '{':
								bracketVariable = true;
								break;
							default:
								if (RSyntaxUtilities.isLetter(c) || c == ' ') { // No tab, just space; spaces are okay in variable names.
									break;
								}
								else if (RSyntaxUtilities.isDigit(c)) { // Single-digit command-line argument ("%1").
									addToken(text, currentTokenStart, i, Token.VARIABLE, newStartOffset + currentTokenStart);
									currentTokenType = Token.NULL;
									break;
								}
								else { // Anything else, ???.
									addToken(text, currentTokenStart, i - 1, Token.VARIABLE, newStartOffset + currentTokenStart); // ???
									i--;
									currentTokenType = Token.NULL;
									break;
								}
						} // End of switch (c).
					}
					else { // Character other than first after the '%'.
						if (bracketVariable) {
							if (c == '}') {
								addToken(text, currentTokenStart, i, Token.VARIABLE, newStartOffset + currentTokenStart);
								currentTokenType = Token.NULL;
							}
						}
						else {
							if (c == '%') {
								addToken(text, currentTokenStart, i, Token.VARIABLE, newStartOffset + currentTokenStart);
								currentTokenType = Token.NULL;
							}
						}
						break;
					}
					break;

			} // End of switch (currentTokenType).

		} // End of for (int i=offset; i<end; i++).

		// Deal with the (possibly there) last token.
		if (currentTokenType != Token.NULL) {

			// Check for REM comments.
			if (end - currentTokenStart == 3 && (array[end - 3] == 'r' || array[end - 3] == 'R') && (array[end - 2] == 'e' || array[end - 2] == 'E') && (array[end - 1] == 'm' || array[end - 1] == 'M')) {
				currentTokenType = Token.COMMENT_EOL;
			}

			addToken(text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart);
		}

		addNullToken();

		// Return the first token in our linked list.
		return firstToken;

	}

}