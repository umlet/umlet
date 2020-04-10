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
public class Artefact extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);

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
		int yPos = (int) (10 * zoom);
		int startY = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
			HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(), startY + yPos, AlignHorizontal.LEFT);
			yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}

		// small component symbol
		g2.drawLine(getRectangle().width - (int) (30 * zoom), (int) (10 * zoom), getRectangle().width - (int) (30 * zoom), (int) (40 * zoom));
		g2.drawLine(getRectangle().width - (int) (30 * zoom), (int) (40 * zoom), getRectangle().width - (int) (5 * zoom), (int) (40 * zoom));
		g2.drawLine(getRectangle().width - (int) (5 * zoom), (int) (40 * zoom), getRectangle().width - (int) (5 * zoom), (int) (20 * zoom));
		g2.drawLine(getRectangle().width - (int) (5 * zoom), (int) (20 * zoom), getRectangle().width - (int) (15 * zoom), (int) (10 * zoom));
		g2.drawLine(getRectangle().width - (int) (15 * zoom), (int) (10 * zoom), getRectangle().width - (int) (30 * zoom), (int) (10 * zoom));
		g2.drawLine(getRectangle().width - (int) (5 * zoom), (int) (20 * zoom), getRectangle().width - (int) (15 * zoom), (int) (20 * zoom));
		g2.drawLine(getRectangle().width - (int) (15 * zoom), (int) (20 * zoom), getRectangle().width - (int) (15 * zoom), (int) (10 * zoom));

	}
}
