package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class TemplateClass extends OldGridElement {

	public TemplateClass() {
		super();
	}

	private Vector<String> getStringVector() {
		return Utils.decomposeStrings(this.getPanelAttributes());
	}

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		g2.setColor(fgColor);

		// Constants.getFRC(g2);

		Vector<String> tmp = getStringVector();

		int yPos = 0;
		yPos += (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();

		boolean CENTER = true;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				CENTER = false;
				g2.drawLine(0, yPos, this.getRectangle().width, yPos);
				yPos += (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
			else {
				yPos += (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
				if (CENTER) {
					Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, this.getRectangle().width / 2, yPos, AlignHorizontal.CENTER);
				}
				else {
					Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() / 2, yPos, AlignHorizontal.LEFT);
				}
				yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
		}

		Rectangle r = this.getRectangle();
		g.drawRect(0, 0, (int) r.getWidth() - 1, (int) r.getHeight() - 1);
		/*
		 * if (_selected) {
		 * g.drawRect(1,1,(int)r.getWidth()-3,(int)r.getHeight()-3);
		 * }
		 */
	}

}
