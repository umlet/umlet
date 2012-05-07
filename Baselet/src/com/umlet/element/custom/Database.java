package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Constants;
import com.baselet.control.Utils;


@SuppressWarnings("serial")
public class Database extends com.baselet.element.GridElement {

	// Change this method if you want to edit the graphical
	// representation of your custom element.
	@Override
	public void paintEntity(Graphics g) {

		// Some unimportant initialization stuff; setting color, font
		// quality, etc. You should not have to change this.
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		int inset = (int) this.getHandler().getFontHandler().getFontSize();

		// It's getting interesting here:
		// First, the strings you type in the element editor are read and
		// split into lines.
		// Then, by default, they are printed out on the element, aligned
		// to the left.
		// Change this to modify this default text printing and to react
		// to special strings
		// (like the "--" string in the UML class elements which draw a line).
		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = inset + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
		boolean CENTER = true;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				CENTER = false;
				g2.drawLine(0, yPos, this.getWidth(), yPos);
				yPos += (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
			}
			else {
				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				if (CENTER) {
					this.getHandler().getFontHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
				}
				else {
					this.getHandler().getFontHandler().writeText(g2, s, (int) this.getHandler().getFontHandler().getFontSize() / 2, yPos, false);
				}
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
			}
		}

		// Finally, change other graphical attributes using
		// drawLine, getWidth, getHeight..
		g2.drawLine(0, this.getHeight() - 1 - inset / 2, 0, inset / 2);
		g2.drawOval(0, 0, this.getWidth(), inset);
		g2.drawArc(0, this.getHeight() - 1 - inset, this.getWidth(), (int) this.getHandler().getFontHandler().getFontSize(), 180, 180);
		g2.drawLine(this.getWidth() - 1, inset / 2, this.getWidth() - 1, this.getHeight() - 1 - inset / 2);
	}

	// Change this method if you want to set the resize-attributes of
	// your custom element
	@Override
	public int getPossibleResizeDirections() {
		// Remove from this list the borders you don't want to be resizeable.
		return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
	}
}
