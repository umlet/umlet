package com.baselet.element.old.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.LineType;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class State extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRoundRect(0, 0, getRectangle().width - 1, getRectangle().height - 1, (int) (30 * zoom), (int) (30 * zoom));
		g2.setComposite(composites[0]);
		if (HandlerElementMap.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) {
			g2.setColor(fgColor);
		}
		else {
			g2.setColor(fgColorBase);
		}

		g2.drawRoundRect(0, 0, getRectangle().width - 1, getRectangle().height - 1, (int) (30 * zoom), (int) (30 * zoom));

		Vector<String> tmp = Utils.decomposeStrings(getPanelAttributes());
		int yPos = 0;
		// A.Mueller start
		if (tmp.contains("--") || tmp.contains("-.")) {
			yPos = 2 * (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}
		else {
			// A.Mueller end
			yPos = getRectangle().height / 2 - tmp.size() * (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts()) / 2;
		}

		boolean CENTER = true;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			// A.Mueller start
			if (s.equals("--")) {
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				g2.drawLine(0, yPos, getRectangle().width, yPos);
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				CENTER = false;
			}
			else if (s.equals("-.")) {
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
				g2.drawLine(0, yPos, getRectangle().width, yPos);
				g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				CENTER = false;
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
			// A.Mueller end
		}

	}
}
