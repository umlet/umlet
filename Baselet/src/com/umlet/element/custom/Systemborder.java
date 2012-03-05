package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Constants;
import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Utils;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class Systemborder extends OldGridElement {

	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, 0, getSize().width - 1, getSize().height - 1);
		g2.setComposite(composites[0]);
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);
		g2.drawRect(0, 0, getSize().width - 1, getSize().height - 1);

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
		boolean center = false;
		boolean downleft = false;
		boolean upcenter = false;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.startsWith("center:") && !s.equals("center:")) {
				if (tmp.size() == 1) {
					yPos = ((getSize().height - 1) / 2) - (int) (10 * zoom);
				}
				else {
					yPos = ((getSize().height - 1) / 2) - (int) this.getHandler().getFontHandler().getFontSize() * (tmp.size() / 2) - (int) (10 * zoom);
				}
				center = true;
				s = s.replace("center:", "");
			}
			else if (s.startsWith("bottomleft:") && !s.equals("bottomleft:")) {
				downleft = true;
				s = s.replace("bottomleft:", "");
				yPos = (getSize().height - 1) - (int) this.getHandler().getFontHandler().getFontSize();
			}
			else if (s.startsWith("topcenter:") && !s.equals("topcenter:")) {
				upcenter = true;
				s = s.replace("topcenter:", "");
				// yPos+=this.getHandler().getFontHandler().getFontsize();
			}
			if (center) {
				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				this.getHandler().getFontHandler().writeText(g2, s, (getSize().width - 1) / 2, yPos, AlignHorizontal.CENTER);
				yPos += 2 * this.getHandler().getFontHandler().getDistanceBetweenTexts();
			}
			else if (downleft) {
				this.getHandler().getFontHandler().writeText(g2, s, (int) this.getHandler().getFontHandler().getFontSize() / 2, yPos, AlignHorizontal.LEFT);
			}
			else if (upcenter) {
				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				this.getHandler().getFontHandler().writeText(g2, s, (getSize().width - 1) / 2, yPos, AlignHorizontal.CENTER);
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
			}
			else {
				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				this.getHandler().getFontHandler().writeText(g2, s, (int) this.getHandler().getFontHandler().getFontSize() / 2, yPos, AlignHorizontal.LEFT);
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
			}
		}

	}

	// Change this method if you want to set the resize-attributes of
	// your custom element
	@Override
	public int getPossibleResizeDirections() {
		// Remove from this list the borders you don't want to be resizeable.
		return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
	}
}
