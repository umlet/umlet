package com.baselet.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.TokenMap;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.fife.ui.rsyntaxtextarea.modes.BBCodeTokenMaker;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.baselet.element.GridElement;


public class OwnSyntaxPane {
	
	private static final int INLINE_SETTING = 1;

	private static TokenMap myWordsToHighlight = new TokenMap();
	private DefaultCompletionProvider provider = new DefaultCompletionProvider();

	List<AutocompletionText> words = new ArrayList<AutocompletionText>();

	JPanel panel;
	RSyntaxTextArea textArea;

	public OwnSyntaxPane() {

		panel = new JPanel(new BorderLayout());
		textArea = new RSyntaxTextArea();

		//Setup highlighting
		createHightLightMap();
		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
		atmf.putMapping(OwnTokenMaker.ID, OwnTokenMaker.class.getName());
		textArea.setSyntaxEditingStyle(OwnTokenMaker.ID);
		
		SyntaxScheme scheme = textArea.getSyntaxScheme();
	      scheme.getStyle(INLINE_SETTING).foreground = Color.BLUE;

		//Setup autocompletion
		createAutocompletionCompletionProvider();
		AutoCompletion ac = new AutoCompletion(provider);
//		ac.setShowDescWindow(true);
		ac.install(textArea);


		textArea.setAntiAliasingEnabled(true);
		panel.add(new RTextScrollPane(textArea));

		textArea.getDocument().putProperty(PlainDocument.tabSizeAttribute, 3); //Reduce tab size

	}

	/**
	 * create one per class
	 * @param strings 
	 */
	private void createAutocompletionCompletionProvider() {
		provider.clear();
		for (AutocompletionText word : words) {
			provider.addCompletion(new BasicCompletion(provider, word.getText(), word.getInfo()));
		}
	
	}

	private void createHightLightMap() {
		myWordsToHighlight = new TokenMap();
		for (AutocompletionText word : words) {
			myWordsToHighlight.put(word.getText(), INLINE_SETTING);
		}
	}

	public static class OwnTokenMaker extends BBCodeTokenMaker {

		public static final String ID = "OwnTokenMaker";

		@Override
		public void addToken(char[] array, int start, int end, int tokenType, int startOffset) {
			int value = myWordsToHighlight.get(array, start, end);
			if (value != -1) {
				tokenType = value;
			}
			else tokenType = TokenTypes.IDENTIFIER; // default type is IDENTIFIER (which is just black)
			super.addToken(array, start, end, tokenType, startOffset);
		}

	}

	public String getText() {
		return textArea.getText();
	}

	public JPanel getPanel() {
		return this.panel;
	}

	public JTextComponent getTextComponent() {
		return textArea;
	}

	public void switchToElement(GridElement e) {
		words = e.getAutocompletionList();
		setText(e.getPanelAttributes());
	}

	public void switchToNonElement(String text) {
		words = new ArrayList<AutocompletionText>();
		setText(text);
		
	}

	private void setText(String text) {
		if (!textArea.getText().equals(text)) {
			textArea.setText(text);
			textArea.setCaretPosition(0);
		}
		createHightLightMap();
		createAutocompletionCompletionProvider();
	}

}
