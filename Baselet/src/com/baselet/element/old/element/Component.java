package com.baselet.element.old.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class Component extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);

		boolean normal = false; // G.M

		// symbol outline
		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, 0, getRectangle().width - 1, getRectangle().height - 1);
		g2.setComposite(composites[0]);
		if (HandlerElementMap.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) {
			g2.setColor(fgColor);
		}
		else {
			g2.setColor(fgColorBase);
		}

		g2.drawRect(0, 0, getRectangle().width - 1, getRectangle().height - 1);

		Vector<String> tmp = Utils.decomposeStrings(getPanelAttributes());
		int yPos = 0;
		int startY = getRectangle().height / 2 - tmp.size() * (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts()) / 2;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);

			if (s.startsWith("'") || normal) { // G.M
				startY = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
				s = s.substring(1, s.length());
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
				HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) (10 * zoom), startY + yPos, AlignHorizontal.LEFT);
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				normal = true;
			}
			else {
				if (s.startsWith("*")) {
					startY = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
					s = s.substring(1, s.length());
				}
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
				HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, getRectangle().width / 2.0, startY + yPos, AlignHorizontal.CENTER);
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
		}

		// small component symbol

		g2.drawLine(getRectangle().width - (int) (50 * zoom), (int) (10 * zoom), getRectangle().width - (int) (15 * zoom), (int) (10 * zoom));
		g2.drawLine(getRectangle().width - (int) (50 * zoom), (int) (35 * zoom), getRectangle().width - (int) (15 * zoom), (int) (35 * zoom));
		g2.drawLine(getRectangle().width - (int) (15 * zoom), (int) (10 * zoom), getRectangle().width - (int) (15 * zoom), (int) (35 * zoom));
		g2.drawLine(getRectangle().width - (int) (50 * zoom), (int) (10 * zoom), getRectangle().width - (int) (50 * zoom), (int) (15 * zoom));
		g2.drawLine(getRectangle().width - (int) (55 * zoom), (int) (15 * zoom), getRectangle().width - (int) (45 * zoom), (int) (15 * zoom));
		g2.drawLine(getRectangle().width - (int) (55 * zoom), (int) (20 * zoom), getRectangle().width - (int) (45 * zoom), (int) (20 * zoom));
		g2.drawLine(getRectangle().width - (int) (55 * zoom), (int) (15 * zoom), getRectangle().width - (int) (55 * zoom), (int) (20 * zoom));
		g2.drawLine(getRectangle().width - (int) (45 * zoom), (int) (15 * zoom), getRectangle().width - (int) (45 * zoom), (int) (20 * zoom));
		g2.drawLine(getRectangle().width - (int) (50 * zoom), (int) (20 * zoom), getRectangle().width - (int) (50 * zoom), (int) (25 * zoom));
		g2.drawLine(getRectangle().width - (int) (55 * zoom), (int) (25 * zoom), getRectangle().width - (int) (45 * zoom), (int) (25 * zoom));
		g2.drawLine(getRectangle().width - (int) (55 * zoom), (int) (30 * zoom), getRectangle().width - (int) (45 * zoom), (int) (30 * zoom));
		g2.drawLine(getRectangle().width - (int) (55 * zoom), (int) (25 * zoom), getRectangle().width - (int) (55 * zoom), (int) (30 * zoom));
		g2.drawLine(getRectangle().width - (int) (45 * zoom), (int) (25 * zoom), getRectangle().width - (int) (45 * zoom), (int) (30 * zoom));
		g2.drawLine(getRectangle().width - (int) (50 * zoom), (int) (30 * zoom), getRectangle().width - (int) (50 * zoom), (int) (35 * zoom));
	}

}
