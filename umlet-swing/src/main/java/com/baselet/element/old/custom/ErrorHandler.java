package com.baselet.element.old.custom;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.List;

import com.baselet.custom.CompileError;
import com.baselet.custom.CustomCodeSyntaxPane;

public class ErrorHandler implements MouseMotionListener {

	private CustomCodeSyntaxPane codepane;
	private HashMap<Integer, String> errors;

	public ErrorHandler(CustomCodeSyntaxPane codepane) {
		this.codepane = codepane;
		errors = new HashMap<Integer, String>();
	}

	/******************** ERROR HANDLING *************************/
	@Override
	public void mouseDragged(MouseEvent arg0) {

	}

	@Override
	public void mouseMoved(MouseEvent me) {
		int line = Math.round(me.getY() / (float) codepane.getTextComponent().getFontMetrics(codepane.getTextComponent().getFont()).getHeight());
		if (errors.get(line) != null) {
			codepane.getTextComponent().setToolTipText(errors.get(line));
		}
		else {
			codepane.getTextComponent().setToolTipText(null);
		}

	}

	protected void clearErrors() {
		errors.clear();
		// TODO CUSTOM ELEMENTS REFACTORING
		// this.codepane.getStyledDocument().setCharacterAttributes(0, this.codepane.getText().length(), this.codepane.getStyledDocument().getStyle("default"), true);
	}

	@SuppressWarnings("unused")
	protected void addError(Integer line, String error, int from, int length) {
		errors.put(line, error);
		// TODO CUSTOM ELEMENTS REFACTORING
		// this.codepane.getStyledDocument().setCharacterAttributes(from, length, this.codepane.getStyledDocument().getStyle("error"), true);
	}

	public void addErrors(List<CompileError> compileErrors) {
		for (CompileError error : compileErrors) {
			addError(error.getLineNr(), error.getError(), 0, 0);
		}
	}
}
