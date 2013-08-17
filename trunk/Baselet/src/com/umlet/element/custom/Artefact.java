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
public class Artefact extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = Main.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		// symbol outline
		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, 0, this.getRectangle().width - 1, this.getRectangle().height - 1);
		g2.setComposite(composites[0]);
		if (Main.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);
		g2.drawRect(0, 0, this.getRectangle().width - 1, this.getRectangle().height - 1);

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = (int) (10 * zoom);
		int startY = (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
			Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(), startY + yPos, AlignHorizontal.LEFT);
			yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}

		// small component symbol
		g2.drawLine(this.getRectangle().width - (int) (30 * zoom), (int) (10 * zoom), this.getRectangle().width - (int) (30 * zoom), (int) (40 * zoom));
		g2.drawLine(this.getRectangle().width - (int) (30 * zoom), (int) (40 * zoom), this.getRectangle().width - (int) (5 * zoom), (int) (40 * zoom));
		g2.drawLine(this.getRectangle().width - (int) (5 * zoom), (int) (40 * zoom), this.getRectangle().width - (int) (5 * zoom), (int) (20 * zoom));
		g2.drawLine(this.getRectangle().width - (int) (5 * zoom), (int) (20 * zoom), this.getRectangle().width - (int) (15 * zoom), (int) (10 * zoom));
		g2.drawLine(this.getRectangle().width - (int) (15 * zoom), (int) (10 * zoom), this.getRectangle().width - (int) (30 * zoom), (int) (10 * zoom));
		g2.drawLine(this.getRectangle().width - (int) (5 * zoom), (int) (20 * zoom), this.getRectangle().width - (int) (15 * zoom), (int) (20 * zoom));
		g2.drawLine(this.getRectangle().width - (int) (15 * zoom), (int) (20 * zoom), this.getRectangle().width - (int) (15 * zoom), (int) (10 * zoom));

	}
}
