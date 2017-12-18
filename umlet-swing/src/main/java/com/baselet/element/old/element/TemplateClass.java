package com.baselet.element.old.element;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class TemplateClass extends OldGridElement {

	public TemplateClass() {
		super();
	}

	private Vector<String> getStringVector() {
		return Utils.decomposeStrings(getPanelAttributes());
	}

	@Override
	public void paintEntity(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		g2.setColor(fgColor);

		// Constants.getFRC(g2);

		Vector<String> tmp = getStringVector();

		int yPos = 0;
		yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();

		boolean CENTER = true;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				CENTER = false;
				g2.drawLine(0, yPos, getRectangle().width, yPos);
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
			else {
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
				if (CENTER) {
					HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, getRectangle().width / 2.0, yPos, AlignHorizontal.CENTER);
				}
				else {
					HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2), yPos, AlignHorizontal.LEFT);
				}
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
		}

		Rectangle r = getRectangle();
		g.drawRect(0, 0, r.getWidth() - 1, r.getHeight() - 1);
		/* if (_selected) { g.drawRect(1,1,(int)r.getWidth()-3,(int)r.getHeight()-3); } */
	}

}
