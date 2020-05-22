package com.baselet.element.old.custom;

import java.util.List;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.custom.CompileError;
import com.baselet.diagram.draw.helper.Theme;
import com.baselet.diagram.draw.helper.ThemeFactory;
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
		drawer.setHandler(HandlerElementMap.getHandlerForElement(this));
		drawer.setForegroundColor(ThemeFactory.getCurrentTheme().getColor(Theme.PredefinedColors.RED));
		drawer.drawRectangle(0, 0, getRealSize().width, getRealSize().height);
		if (errors != null) {
			double y = textHeight();
			double x = HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(false);
			drawer.print("Custom Element With Errors:", x, (int) y, AlignHorizontal.LEFT);
			y += textHeight();
			for (CompileError error : errors) {
				drawer.print(error.getLineNr() + ": " + error.getError(), x, (int) y, AlignHorizontal.LEFT);
				y += textHeight();
			}
		}
		drawer.drawAll(HandlerElementMap.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this));
	}

	@Override
	public final GridElement cloneFromMe() {
		CustomElementWithErrors e = (CustomElementWithErrors) super.cloneFromMe();
		e.setCode(getCode());
		e.errors = errors;
		return e;
	}
}
