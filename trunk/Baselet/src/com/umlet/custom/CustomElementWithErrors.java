package com.umlet.custom;

import java.awt.Color;
import java.util.List;

import com.baselet.control.Constants;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.element.GridElement;


public class CustomElementWithErrors extends CustomElement {

	private static final long serialVersionUID = 1L;
	
	private List<CompileError> errors;

	public CustomElementWithErrors() {
		super();
	}
	
	public CustomElementWithErrors(List<CompileError> errors) {
		super();
		this.errors = errors;
	}

	@Override
	public void paint() {
		BaseDrawHandler drawer = new BaseDrawHandler(g2, getHandler(), Color.RED, Constants.DEFAULT_BACKGROUND_COLOR, this.getRealSize(), isSelected());
		drawer.drawRectangle(0, 0, this.getRealSize().width, this.getRealSize().height);
		if (errors != null) {
			float pos = textHeight();
			drawer.printLeft("Custom Element With Errors:", (int) pos);
			pos += textHeight();
			for (CompileError error : errors) {
				drawer.printLeft(error.getLineNr() + ": " + error.getError(), (int) pos);
				pos += textHeight();
			}
		}
		drawer.drawAll();
	}

	@Override
	public final GridElement CloneFromMe() {
		CustomElementWithErrors e = (CustomElementWithErrors) super.CloneFromMe();
		e.setCode(this.getCode());
		e.errors = this.errors;
		return e;
	}
}
