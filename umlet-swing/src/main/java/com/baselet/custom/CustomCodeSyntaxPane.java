package com.baselet.custom;

import java.awt.BorderLayout;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.baselet.control.config.DerivedConfig;
import com.baselet.element.old.custom.CustomElement;

public class CustomCodeSyntaxPane {

	private final JPanel panel;
	private final RTextScrollPane scrollPane;
	private final RSyntaxTextArea textArea;
	private final DefaultCompletionProvider provider = new DefaultCompletionProvider();

	public CustomCodeSyntaxPane() {

		panel = new JPanel(new BorderLayout());
		textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setAntiAliasingEnabled(true);
		textArea.setCodeFoldingEnabled(true);
		textArea.setFont(DerivedConfig.getPanelContentFont());

		// setup autocompletion
		for (String word : getAutocompletionStrings()) {
			provider.addCompletion(new BasicCompletion(provider, word));
		}
		new AutoCompletion(provider).install(textArea);

		scrollPane = new RTextScrollPane(textArea);
		scrollPane.setFoldIndicatorEnabled(true);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel.add(scrollPane);
		textArea.getDocument().putProperty(PlainDocument.tabSizeAttribute, 3); // Reduce tab size
	}

	public String getText() {
		return textArea.getText();
	}

	public JTextComponent getTextComponent() {
		return textArea;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setCode(String text) {
		textArea.setText(text);
	}

	private String getStringForCustomElementMethod(Method m) {
		CustomFunction cm = m.getAnnotation(CustomFunction.class);
		StringBuilder sb = new StringBuilder("");
		sb.append(m.getName()).append("(");
		String[] params = cm.param_defaults().split(",");
		Class<?>[] types = m.getParameterTypes();
		for (int i = 0; i < params.length && i < types.length; i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(types[i].getSimpleName()).append(" ").append(params[i].trim());
		}
		return sb.append(");").toString();
	}

	private ArrayList<String> getAutocompletionStrings() {
		ArrayList<String> descriptors = new ArrayList<String>();
		for (Method m : CustomElement.class.getDeclaredMethods()) {
			if (m.isAnnotationPresent(CustomFunction.class)) {
				descriptors.add(getStringForCustomElementMethod(m));
			}
		}
		return descriptors;
	}

	public void repaint() {
		if (scrollPane != null) {
			scrollPane.repaint();
		}
	}

}
