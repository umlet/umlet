package com.baselet.element.old.element;

// Some import to have access to more Java features
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class EER_Rel_Diamond extends OldGridElement {

	// Change this method if you want to edit the graphical
	// representation of your custom element.
	@Override
	public void paintEntity(Graphics g) {

		// Some unimportant initialization stuff; setting color, font
		// quality, etc. You should not have to change this.
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);

		// Finally, change other graphical attributes using
		// drawLine, getWidth, getHeight..

		// Define the elements outline using a polygon, rectangle, oval, etc.
		Polygon poly = new Polygon();
		poly.addPoint(getRectangle().width / 2, 0);
		poly.addPoint(getRectangle().width - 1, getRectangle().height / 2);
		poly.addPoint(getRectangle().width / 2, getRectangle().height - 1);
		poly.addPoint(0, getRectangle().height / 2);

		g2.setComposite(composites[1]); // set aplha composite for drawing the background color
		g2.setColor(bgColor);
		g2.fillPolygon(poly); // fill the background
		g2.setComposite(composites[0]); // reset composite settings
		if (HandlerElementMap.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) {
			g2.setColor(fgColor);
		}
		else {
			g2.setColor(fgColorBase);
		}

		// It's getting interesting here:
		// First, the strings you type in the element editor are read and
		// split into lines. Then, by default, they are printed out on the
		// element, aligned to the left. Change this to modify this default
		// text printing and to react to special strings (like the "--" string
		// in the UML class elements which draw a line).
		Vector<String> tmp = Utils.decomposeStrings(getPanelAttributes());
		int yPos = getRectangle().height / 2 - ((int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize()) * tmp.size() / 2;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
			HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, getRectangle().width / 2.0, yPos, AlignHorizontal.CENTER);
			yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}

		// Draw the elements outline
		g2.drawPolygon(poly);
	}

}
