package com.umlet.custom;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

import com.baselet.control.Constants;
import com.umlet.gui.CustomCodeSyntaxPane;


public class ErrorHandler implements MouseMotionListener {

	private CustomCodeSyntaxPane codepane;
	private HashMap<Integer, String> errors;

	public ErrorHandler(CustomCodeSyntaxPane codepane) {
		this.codepane = codepane;
		this.errors = new HashMap<Integer, String>();
	}

	/******************** ERROR HANDLING *************************/
	@Override
	public void mouseDragged(MouseEvent arg0) {

	}

	@Override
	public void mouseMoved(MouseEvent me) {
		int line = (me.getY() - 3) / this.codepane.getFontMetrics(this.codepane.getFont()).getHeight();
		if (this.errors.get(line) != null) {
			this.codepane.setToolTipText(this.errors.get(line));
		}
		else this.codepane.setToolTipText(null);

	}

	protected void clearErrors() {
		this.errors.clear();
		//TODO CUSTOM ELMENTS REFACTORING
//		this.codepane.getStyledDocument().setCharacterAttributes(0, this.codepane.getText().length(), this.codepane.getStyledDocument().getStyle("default"), true);
	}

	protected void addError(Integer line, String error, int from, int length) {
		if (this.errors.containsKey(line)) this.errors.put(line, this.errors.get(line) + Constants.NEWLINE + error);
		else this.errors.put(line, error);
		//TODO CUSTOM ELMENTS REFACTORING
//		this.codepane.getStyledDocument().setCharacterAttributes(from, length, this.codepane.getStyledDocument().getStyle("error"), true);
	}
}
