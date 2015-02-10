package com.baselet.element.old.custom;

import java.util.List;

import com.baselet.control.Main;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.custom.CompileError;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.swing.DrawHandlerSwing;
import com.baselet.element.interfaces.GridElement;

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
		DrawHandlerSwing drawer = new DrawHandlerSwing(this);
		drawer.setGraphics(g2);
		drawer.setHandler(Main.getHandlerForElement(this));
		drawer.setForegroundColor(ColorOwn.RED);
		drawer.drawRectangle(0, 0, getRealSize().width, getRealSize().height);
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
	public final GridElement cloneFromMe() {
		CustomElementWithErrors e = (CustomElementWithErrors) super.cloneFromMe();
		e.setCode(getCode());
		e.errors = errors;
		return e;
	}
}
