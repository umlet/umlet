package com.umlet.custom;

import java.util.List;

import com.baselet.control.Main;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.swing.BaseDrawHandlerSwing;
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
		BaseDrawHandlerSwing drawer = new BaseDrawHandlerSwing(g2, Main.getHandlerForElement(this), ColorOwn.RED, ColorOwn.DEFAULT_BACKGROUND);
		drawer.drawRectangle(0, 0, this.getRealSize().width, this.getRealSize().height);
		if (errors != null) {
			double y = textHeight();
			double x = Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(false);
			drawer.print("Custom Element With Errors:", x, (int) y, AlignHorizontal.LEFT);
			y += textHeight();
			for (CompileError error : errors) {
				drawer.print(error.getLineNr() + ": " + error.getError(), x, (int) y, AlignHorizontal.LEFT);
				y += textHeight();
			}
		}
		drawer.drawAll(Main.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this));
	}

	@Override
	public final GridElement CloneFromMe() {
		CustomElementWithErrors e = (CustomElementWithErrors) super.CloneFromMe();
		e.setCode(this.getCode());
		e.errors = this.errors;
		return e;
	}
}
