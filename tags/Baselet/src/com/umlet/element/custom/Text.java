package com.umlet.element.custom;

// Some import to have access to more Java features
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Utils;


@SuppressWarnings("serial")
public class Text extends com.baselet.element.GridElement {

	// Change this method if you want to edit the graphical
	// representation of your custom element.
	@Override
	public void paintEntity(Graphics g) {

		// Some unimportant initialization stuff; setting color, font
		// quality, etc. You should not have to change this.
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
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
		int yPos = (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getFontHandler().getFontSize();
			this.getHandler().getFontHandler().writeText(g2, s, (int) this.getHandler().getFontHandler().getFontSize() / 2, yPos, false);
			yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
		}

		// Finally, change other graphical attributes using
		// drawLine, getWidth, getHeight..
		/*
		 * int fnt = this.getHandler().getFontHandler().getFontsize()/3;
		 * int w = getWidth();
		 * int h = getHeight();
		 * g2.drawLine(1,1,fnt,1);
		 * g2.drawLine(1,1,1,fnt);
		 * g2.drawLine(w-1,h,w-1,h-fnt);
		 * g2.drawLine(w-1,h-1,w-fnt,h-1);
		 */

	}

	/*
	 * Uncommented because this method makes the Textarea not-resizable and removes the stickingpolygon
	 * // Advanced: change this method to modify the area where relations
	 * // stick to your custom element.
	 * @Override
	 * public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
	 * // By default, the element returns its outer borders. Change it,
	 * // if your element needs to stick to relations differently.
	 * // See, for example, the source code of the UML interface element.
	 * return null;
	 * }
	 */
}
