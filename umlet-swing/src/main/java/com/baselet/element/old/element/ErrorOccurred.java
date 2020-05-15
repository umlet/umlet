package com.baselet.element.old.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.basics.Converter;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.helper.Theme;
import com.baselet.diagram.draw.helper.ThemeFactory;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class ErrorOccurred extends OldGridElement {

	private final String errorMessage;

	public ErrorOccurred() {
		errorMessage = "This Element has an Error!";
	}

	public ErrorOccurred(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	@Override
	public void paintEntity(Graphics g) {

		DiagramHandler handlerForElement = HandlerElementMap.getHandlerForElement(this);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawRect(0, 0, getRectangle().width - 1, getRectangle().height - 1);
		if (handlerForElement.getDrawPanel().getSelector().isSelected(this)) {
			g2.setColor(Converter.convert(ThemeFactory.getCurrentTheme().getStyleColorMap().get(Theme.ColorStyle.SELECTION_FG)));
		}
		else {
			g2.setColor(Color.red);
		}
		g2.setFont(handlerForElement.getFontHandler().getFont());
		handlerForElement.getFontHandler().writeText(g2, errorMessage, 0, 20, AlignHorizontal.LEFT);
		g2.setColor(fgColor);
	}

	@Override
	public GridElement cloneFromMe() {
		try {
			GridElement ge = new ErrorOccurred(errorMessage);
			ge.setPanelAttributes(getPanelAttributes()); // copy states
			ge.setRectangle(getRectangle());
			HandlerElementMap.getHandlerForElement(this).setHandlerAndInitListeners(ge);
			return ge;
		} catch (Exception e) {
			log.error("Error at calling CloneFromMe() on entity", e);
		}
		return null;
	}

}
