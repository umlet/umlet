package com.baselet.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.reflect.Constructor;

@SuppressWarnings("serial")
public class ErrorOccurred extends GridElement {

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

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g2.setColor(Color.red);
		g2.setFont(this.getHandler().getFontHandler().getFont());
		this.getHandler().getFontHandler().writeText(g2, errorMessage, this.getWidth() / 2, this.getHeight() / 2 - (int) (10 * zoom), true);
		g2.setColor(fgColor);
	}

	@Override
	public GridElement CloneFromMe() {
		try {
			Constructor<ErrorOccurred> c = ErrorOccurred.class.getConstructor(new Class[]{String.class});
			GridElement ge = c.newInstance(new Object[]{errorMessage});
			ge.setPanelAttributes(this.getPanelAttributes()); // copy states
			ge.setBounds(this.getBounds());
			ge.setHandler(this.getHandler());
			return ge;
		} catch (Exception e) {
			log.error("Error at calling CloneFromMe() on entity", e);
		}
		return null;
	}

}
