package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.LineType;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class State extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = Main.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRoundRect(0, 0, this.getRectangle().width - 1, this.getRectangle().height - 1, (int) (30 * zoom), (int) (30 * zoom));
		g2.setComposite(composites[0]);
		if (Main.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);

		g2.drawRoundRect(0, 0, this.getRectangle().width - 1, this.getRectangle().height - 1, (int) (30 * zoom), (int) (30 * zoom));

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = 0;
		// A.Mueller start
		if (tmp.contains("--") || tmp.contains("-.")) yPos = 2 * (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		else
		// A.Mueller end
		yPos = this.getRectangle().height / 2 - tmp.size() * ((int) (Main.getHandlerForElement(this).getFontHandler().getFontSize() + Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts())) / 2;

		boolean CENTER = true;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			// A.Mueller start
			if (s.equals("--")) {
				yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				g2.drawLine(0, yPos, getRectangle().width, yPos);
				yPos += (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				CENTER = false;
			}
			else if (s.equals("-.")) {
				yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
				g2.drawLine(0, yPos, getRectangle().width, yPos);
				g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
				yPos += (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				CENTER = false;
			}
			else {
				yPos += (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
				if (CENTER) Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, this.getRectangle().width / 2, yPos, AlignHorizontal.CENTER);
				else
					Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() / 2, yPos, AlignHorizontal.LEFT);
				yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
			// A.Mueller end
		}

	}
}
