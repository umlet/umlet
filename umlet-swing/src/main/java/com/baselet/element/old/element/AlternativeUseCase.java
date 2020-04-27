package com.baselet.element.old.element;

// Some import to have access to more Java features
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class AlternativeUseCase extends OldGridElement {

	// Change this method if you want to edit the graphical
	// representation of your custom element.
	@Override
	public void paintEntity(Graphics g) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);

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

		boolean center = false;

		Vector<String> tmp = Utils.decomposeStrings(getPanelAttributes());
		int yPos = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
			if (s.equals("--")) {
				yPos = (int) (35 * zoom);
				center = true;
			}
			else if (center) {
				HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, (getRectangle().width - 1) / 2.0, yPos, AlignHorizontal.CENTER);
				center = false;
			}
			else {
				HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2), yPos, AlignHorizontal.LEFT);
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
		}

		g2.drawLine(0, (int) (30 * zoom), getRectangle().width - 1, (int) (30 * zoom));
		g2.drawOval(getRectangle().width - (int) (59 * zoom), (int) (3 * zoom), (int) (55 * zoom), (int) (20 * zoom));
	}
}
