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
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.TokenMap;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.baselet.control.Constants;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.diagram.draw.swing.Converter;
import com.baselet.element.GridElement;

public class OwnSyntaxPane {

	private static final int SPECIFIC_SETTING = 1;
	private static final int GLOBAL_SETTING = 2;

	private DefaultCompletionProvider provider = new DefaultCompletionProvider();

	List<AutocompletionText> words = new ArrayList<AutocompletionText>();

	JPanel panel;
	RSyntaxTextArea textArea;
	RTextScrollPane scrollPane;

	public OwnSyntaxPane() {

		panel = new JPanel(new FlowLayout());
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		textArea = new RSyntaxTextArea();

		// Setup highlighting
		createHightLightMap();
		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
		atmf.putMapping(OwnTokenMaker.ID, OwnTokenMaker.class.getName());
		textArea.setSyntaxEditingStyle(OwnTokenMaker.ID);

		SyntaxScheme scheme = textArea.getSyntaxScheme();
		scheme.getStyle(SPECIFIC_SETTING).foreground = Converter.convert(ColorOwn.forString("#e10100", Transparency.FOREGROUND));
		scheme.getStyle(GLOBAL_SETTING).foreground = Converter.convert(ColorOwn.BLUE);

		// Setup autocompletion
		createAutocompletionCompletionProvider();
		AutoCompletion ac = new AutoCompletion(provider);
		// ac.setShowDescWindow(true);
		ac.install(textArea);

		JLabel propertyLabel = new JLabel(" Properties");
		propertyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		propertyLabel.setFont(Constants.PANEL_HEADER_FONT);
		panel.add(propertyLabel);

		textArea.setAntiAliasingEnabled(true);
		textArea.setFont(Constants.PANEL_CONTENT_FONT);
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
			provider.addCompletion(new BasicCompletion(provider, word.getText(), word.getInfo()));
		}

	}

	private void createHightLightMap() {
		TokenMap myWordsToHighlight = new TokenMap();
		for (AutocompletionText word : words) {
			myWordsToHighlight.put(word.getText(), word.isGlobal() ? GLOBAL_SETTING : SPECIFIC_SETTING);
		}
		// use ugly static setter because OwnTokenMaker is unfortunately not instantiated by us
		OwnTokenMaker.setMyWordsToHighlight(myWordsToHighlight);
	}

	public String getText() {
		return textArea.getText();
	}

	public JPanel getPanel() {
		return this.panel;
	}

	public void revalidate() {
		if (scrollPane != null) scrollPane.revalidate();
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
