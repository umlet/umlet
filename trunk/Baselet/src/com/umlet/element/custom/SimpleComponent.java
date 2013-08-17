package com.umlet.element.custom;

// Some import to have access to more Java features
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class SimpleComponent extends OldGridElement {

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
		

		// It's getting interesting here:
		// First, the strings you type in the element editor are read and
		// split into lines.
		// Then, by default, they are printed out on the element, aligned
		// to the left.
		// Change this to modify this default text printing and to react
		// to special strings
		// (like the "--" string in the UML class elements which draw a line).
		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);

			yPos += (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
			Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() * 3, yPos, AlignHorizontal.LEFT);
			// offset into box
			yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}

		// Finally, change other graphical attributes using
		// drawLine, getWidth, getHeight..

		int fnt = (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();

		g2.drawLine((int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), 0, this.getRectangle().width - 1, 0);
		// top
		g2.drawLine((int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), getRectangle().height - 1, getRectangle().width - 1, getRectangle().height - 1); // bottom

		// left side
		g2.drawLine((int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), 0, (int) Main.getHandlerForElement(this).getFontHandler().getFontSize(), fnt); // top
		g2.drawLine(fnt, fnt * 2, fnt, fnt * 3); // middle
		g2.drawLine(fnt, fnt * 4, fnt, getRectangle().height - 1);
		// bottom

		g2.drawLine(getRectangle().width - 1, 0, getRectangle().width - 1, getRectangle().height - 1); // right side

		g2.drawLine(0, fnt, fnt * 2, fnt); // top box top line
		g2.drawLine(0, fnt * 2, fnt * 2, fnt * 2); // top box bottom line
		g2.drawLine(0, fnt, 0, fnt * 2); // top box left line
		g2.drawLine(fnt * 2, fnt, fnt * 2, fnt * 2); // top box right line

		g2.drawLine(0, fnt * 3, fnt * 2, fnt * 3); // bottom box top line
		g2.drawLine(0, fnt * 4, fnt * 2, fnt * 4); // bottom box bottom line
		g2.drawLine(0, fnt * 3, 0, fnt * 4); // bottom box left line
		g2.drawLine(fnt * 2, fnt * 3, fnt * 2, fnt * 4); // bottom box right line

	}

}
