package com.umlet.custom;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.List;

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
		int line = Math.round(me.getY() / (float) this.codepane.getTextComponent().getFontMetrics(this.codepane.getTextComponent().getFont()).getHeight());
		if (this.errors.get(line) != null) {
			this.codepane.getTextComponent().setToolTipText(this.errors.get(line));
		}
		else this.codepane.getTextComponent().setToolTipText(null);

	}

	protected void clearErrors() {
		this.errors.clear();
		//TODO CUSTOM ELEMENTS REFACTORING
//		this.codepane.getStyledDocument().setCharacterAttributes(0, this.codepane.getText().length(), this.codepane.getStyledDocument().getStyle("default"), true);
	}

	@SuppressWarnings("unused")
	protected void addError(Integer line, String error, int from, int length) {
		this.errors.put(line, error);
		//TODO CUSTOM ELEMENTS REFACTORING
//		this.codepane.getStyledDocument().setCharacterAttributes(from, length, this.codepane.getStyledDocument().getStyle("error"), true);
	}

	public void addErrors(List<CompileError> compileErrors) {
		for (CompileError error : compileErrors) {
			addError(error.getLineNr(), error.getError(), 0, 0);
		}
	}
}
