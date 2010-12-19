package com.umlet.element.custom;

// Some import to have access to more Java features
import java.awt.Graphics;
import java.awt.Graphics2D;

@SuppressWarnings("serial")
public class Socket extends com.umlet.element.base.Entity {

	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getZoomedFont());
		g2.setColor(_activeColor);
		this.getHandler().getFRC(g2);

		int fnt = (int) this.getHandler().getZoomedFontsize() + ((int) this.getHandler().getZoomedFontsize() / 2);
		this.setSize(fnt + 2, fnt + 2);
		int h = this.getHeight();

		g2.drawArc(0, 0, h - 1, h - 1, 90, 180);
		g2.drawOval((int) (4 * zoom), (int) (4 * zoom), h - (int) (9 * zoom), h - (int) (9 * zoom));

	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	}

}
