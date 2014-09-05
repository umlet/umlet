package com.baselet.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.TokenMap;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.baselet.control.Constants;
import com.baselet.control.MenuConstants;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.swing.Converter;
import com.baselet.element.GridElement;

public class OwnSyntaxPane {

	private static final String SEPARATOR = "    ";

	private final DefaultCompletionProvider provider = new DefaultCompletionProvider() {
		@Override
		protected boolean isValidChar(char ch) {
			return ch != ' '; // every character except space can be part of an autocompletion
		}
	};

	List<AutocompletionText> words = new ArrayList<AutocompletionText>();

	JPanel panel;
	RSyntaxTextArea textArea;
	RTextScrollPane scrollPane;

	public OwnSyntaxPane() {

		panel = new JPanel(new FlowLayout());
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		textArea = new RSyntaxTextArea() {
			private static final long serialVersionUID = 7431070002967577129L;

			@Override
			public void undoLastAction() {
				MenuFactorySwing.getInstance().doAction(MenuConstants.UNDO, null);
			}

			@Override
			public void redoLastAction() {
				MenuFactorySwing.getInstance().doAction(MenuConstants.REDO, null);
			}
		};

		// Setup highlighting
		createHightLightMap();
		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
		atmf.putMapping(OwnTokenMaker.ID, OwnTokenMaker.class.getName());
		textArea.setSyntaxEditingStyle(OwnTokenMaker.ID);

		textArea.getSyntaxScheme().getStyle(TokenTypes.RESERVED_WORD).foreground = Converter.convert(ColorOwn.SYNTAX_HIGHLIGHTING);

		// Setup autocompletion
		createAutocompletionCompletionProvider();
		AutoCompletion ac = new AutoCompletion(provider);
		// ac.setShowDescWindow(true);
		ac.install(textArea);

		JLabel propertyLabel = new JLabel(" Properties");
		propertyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		propertyLabel.setFont(Constants.getPanelHeaderFont());
		panel.add(propertyLabel);

		textArea.setAntiAliasingEnabled(true);
		textArea.setFont(Constants.getPanelContentFont());
		scrollPane = new RTextScrollPane(textArea, false);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel.add(scrollPane);

		textArea.getDocument().putProperty(PlainDocument.tabSizeAttribute, 3); // Reduce tab size
	}

	/**
	 * create one per class
	 * @param strings 
	 */
	private void createAutocompletionCompletionProvider() {
		provider.clear();
		for (AutocompletionText word : words) {
			provider.addCompletion(new BasicCompletion(provider, word.getText(), word.getInfo()) {
				@Override
				public String toString() {
					if (getShortDescription() == null) {
						return getInputText();
					}
					return getInputText() + SEPARATOR + getShortDescription();
				}
			});
		}

	}

	private void createHightLightMap() {
		TokenMap myWordsToHighlight = new TokenMap();
		for (AutocompletionText word : words) {
			myWordsToHighlight.put(word.getText(), TokenTypes.RESERVED_WORD);
		}
		// use ugly static setter because OwnTokenMaker is unfortunately not instantiated by us
		OwnTokenMaker.setMyWordsToHighlight(myWordsToHighlight);
		// switch syntaxstyle to null and back to OwnTokenMaker to make sure the wordsToHighlight are reset!
		textArea.setSyntaxEditingStyle(null);
		textArea.setSyntaxEditingStyle(OwnTokenMaker.ID);
	}

	public String getText() {
		return textArea.getText();
	}

	public JPanel getPanel() {
		return panel;
	}

	public void revalidate() {
		if (scrollPane != null) {
			scrollPane.revalidate();
		}
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
		textArea.setText(text); // Always set text even if they are equal to trigger correct syntax highlighting (if words to highlight have changed but text not)
		textArea.setCaretPosition(0);
		createHightLightMap();
		createAutocompletionCompletionProvider();
	}

}
