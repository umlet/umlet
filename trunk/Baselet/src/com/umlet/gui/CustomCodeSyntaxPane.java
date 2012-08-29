package com.umlet.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.baselet.gui.JMultiLineToolTip;

public class CustomCodeSyntaxPane {
	
	JPanel panel;
	RSyntaxTextArea textArea;

	public CustomCodeSyntaxPane() {

		panel = new JPanel(new BorderLayout());
		textArea = new RSyntaxTextArea();
	    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setAntiAliasingEnabled(true);
		textArea.setCodeFoldingEnabled(true);
	    
		RTextScrollPane sp = new RTextScrollPane(textArea);
	    sp.setFoldIndicatorEnabled(true);
	    panel.add(sp);
		textArea.getDocument().putProperty(PlainDocument.tabSizeAttribute, 3); //Reduce tab size
	}

	public String getText() {
		return textArea.getText();
	}

	public JTextComponent getTextComponent() {
		return textArea;
	}

	public JPanel getPanel() {
		return this.panel;
	}

	public void setCode(String text) {
		textArea.setText(text);
	}

	public JMultiLineToolTip getToolTip() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setToolTipText(String text) {
		// TODO Auto-generated method stub
		
	}

}
