package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class Systemborder extends OldGridElement {

	@Override
	public void paintEntity(Graphics g) {

		float zoom = Main.getElementHandlerMapping().get(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getElementHandlerMapping().get(this).getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillRect(0, 0, getZoomedSize().width - 1, getZoomedSize().height - 1);
		g2.setComposite(composites[0]);
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);
		g2.drawRect(0, 0, getZoomedSize().width - 1, getZoomedSize().height - 1);

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = (int) Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();
		boolean center = false;
		boolean downleft = false;
		boolean upcenter = false;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.startsWith("center:") && !s.equals("center:")) {
				if (tmp.size() == 1) {
					yPos = ((getZoomedSize().height - 1) / 2) - (int) (10 * zoom);
				}
				else {
					yPos = ((getZoomedSize().height - 1) / 2) - (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize() * (tmp.size() / 2) - (int) (10 * zoom);
				}
				center = true;
				s = s.replace("center:", "");
			}
			else if (s.startsWith("bottomleft:") && !s.equals("bottomleft:")) {
				downleft = true;
				s = s.replace("bottomleft:", "");
				yPos = (getZoomedSize().height - 1) - (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize();
			}
			else if (s.startsWith("topcenter:") && !s.equals("topcenter:")) {
				upcenter = true;
				s = s.replace("topcenter:", "");
				// yPos+=this.getHandler().getFontHandler().getFontsize();
			}
			if (center) {
				yPos += (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize();
				Main.getElementHandlerMapping().get(this).getFontHandler().writeText(g2, s, (getZoomedSize().width - 1) / 2, yPos, AlignHorizontal.CENTER);
				yPos += 2 * Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();
			}
			else if (downleft) {
				Main.getElementHandlerMapping().get(this).getFontHandler().writeText(g2, s, (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize() / 2, yPos, AlignHorizontal.LEFT);
			}
			else if (upcenter) {
				yPos += (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize();
				Main.getElementHandlerMapping().get(this).getFontHandler().writeText(g2, s, (getZoomedSize().width - 1) / 2, yPos, AlignHorizontal.CENTER);
				yPos += Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();
			}
			else {
				yPos += (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize();
				Main.getElementHandlerMapping().get(this).getFontHandler().writeText(g2, s, (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize() / 2, yPos, AlignHorizontal.LEFT);
				yPos += Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();
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
