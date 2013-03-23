package com.baselet.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.reflect.Constructor;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.control.enumerations.AlignHorizontal;
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

		float zoom = Main.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.drawRect(0, 0, this.getZoomedSize().width - 1, this.getZoomedSize().height - 1);
		if (isSelected()) g2.setColor(Converter.convert(Constants.DEFAULT_SELECTED_COLOR));
		else g2.setColor(Color.red);
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		Main.getHandlerForElement(this).getFontHandler().writeText(g2, errorMessage, this.getZoomedSize().width / 2, this.getZoomedSize().height / 2 - (int) (10 * zoom), AlignHorizontal.CENTER);
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
