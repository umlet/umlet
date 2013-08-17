package com.baselet.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.reflect.Constructor;

import com.baselet.control.Main;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.swing.Converter;

@SuppressWarnings("serial")
public class ErrorOccurred extends OldGridElement {

	private String errorMessage;

	public ErrorOccurred() {
		errorMessage = "This Element has an Error!";
	}

	public ErrorOccurred(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	@Override
	public void paintEntity(Graphics g) {

		DiagramHandler handlerForElement = Main.getHandlerForElement(this);
		float zoom = handlerForElement.getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.drawRect(0, 0, this.getRectangle().width - 1, this.getRectangle().height - 1);
		if (handlerForElement.getDrawPanel().getSelector().isSelected(this)) g2.setColor(Converter.convert(ColorOwn.SELECTION_FG));
		else g2.setColor(Color.red);
		g2.setFont(handlerForElement.getFontHandler().getFont());
		handlerForElement.getFontHandler().writeText(g2, errorMessage, this.getRectangle().width / 2, this.getRectangle().height / 2 - (int) (10 * zoom), AlignHorizontal.CENTER);
		g2.setColor(fgColor);
	}

	@Override
	public GridElement CloneFromMe() {
		try {
			Constructor<ErrorOccurred> c = ErrorOccurred.class.getConstructor(new Class[]{String.class});
			GridElement ge = c.newInstance(new Object[]{errorMessage});
			ge.setPanelAttributes(this.getPanelAttributes()); // copy states
			ge.setRectangle(this.getRectangle());
			Main.getHandlerForElement(this).setHandlerAndInitListeners(ge);
			return ge;
		} catch (Exception e) {
			log.error("Error at calling CloneFromMe() on entity", e);
		}
		return null;
	}

}
