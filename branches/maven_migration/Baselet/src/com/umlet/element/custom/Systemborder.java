package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class Systemborder extends OldGridElement {

	@Override
	public void paintEntity(Graphics g) {

		float zoom = Main.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, 0, getRectangle().width - 1, getRectangle().height - 1);
		g2.setComposite(composites[0]);
		if (Main.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);
		g2.drawRect(0, 0, getRectangle().width - 1, getRectangle().height - 1);

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		boolean center = false;
		boolean downleft = false;
		boolean upcenter = false;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.startsWith("center:") && !s.equals("center:")) {
				if (tmp.size() == 1) {
					yPos = ((getRectangle().height - 1) / 2) - (int) (10 * zoom);
				}
				else {
					yPos = ((getRectangle().height - 1) / 2) - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() * (tmp.size() / 2) - (int) (10 * zoom);
				}
				center = true;
				s = s.replace("center:", "");
			}
			else if (s.startsWith("bottomleft:") && !s.equals("bottomleft:")) {
				downleft = true;
				s = s.replace("bottomleft:", "");
				yPos = (getRectangle().height - 1) - (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
			}
			else if (s.startsWith("topcenter:") && !s.equals("topcenter:")) {
				upcenter = true;
				s = s.replace("topcenter:", "");
				// yPos+=this.getHandler().getFontHandler().getFontsize();
			}
			if (center) {
				yPos += (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
				Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, (getRectangle().width - 1) / 2, yPos, AlignHorizontal.CENTER);
				yPos += 2 * Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
			else if (downleft) {
				Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() / 2, yPos, AlignHorizontal.LEFT);
			}
			else if (upcenter) {
				yPos += (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
				Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, (getRectangle().width - 1) / 2, yPos, AlignHorizontal.CENTER);
				yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
			else {
				yPos += (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
				Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() / 2, yPos, AlignHorizontal.LEFT);
				yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
		}

	}
}
