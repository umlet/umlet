package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class Database extends OldGridElement {

	// Change this method if you want to edit the graphical
	// representation of your custom element.
	@Override
	public void paintEntity(Graphics g) {

		// Some unimportant initialization stuff; setting color, font
		// quality, etc. You should not have to change this.
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		int inset = (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();

		// It's getting interesting here:
		// First, the strings you type in the element editor are read and
		// split into lines.
		// Then, by default, they are printed out on the element, aligned
		// to the left.
		// Change this to modify this default text printing and to react
		// to special strings
		// (like the "--" string in the UML class elements which draw a line).
		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = inset + (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
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

		// Finally, change other graphical attributes using
		// drawLine, getWidth, getHeight..
		g2.drawLine(0, this.getRectangle().height - 1 - inset / 2, 0, inset / 2);
		g2.drawOval(0, 0, this.getRectangle().width, inset);
		g2.drawArc(0, this.getRectangle().height - 1 - inset, this.getRectangle().width, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), 180, 180);
		g2.drawLine(this.getRectangle().width - 1, inset / 2, this.getRectangle().width - 1, this.getRectangle().height - 1 - inset / 2);
	}

}
