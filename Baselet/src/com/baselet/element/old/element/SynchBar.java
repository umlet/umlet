package com.baselet.element.old.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;

@SuppressWarnings("serial")
public class SynchBar extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		g2.setColor(Color.red);

		int yPos = 0;
		yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();

		Vector<String> tmp = Utils.decomposeStrings(getPanelAttributes());

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
			HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, 0, yPos, AlignHorizontal.CENTER);
			yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}

		// g2.fillRect(0,7,this.getWidth(),this.getHeight()-15);
		g2.fillRect(0, (int) (7 * zoom), getRectangle().width, (int) (5 * zoom));
	}

	public int doesCoordinateAppearToBeConnectedToMe(Point p) {
		int ret = 0;
		int tmpX = p.x - getRectangle().x;
		int tmpY = p.y - getRectangle().y;

		if (tmpX > -4 && tmpX < getRectangle().width + 4) {
			if (tmpY > 0 && tmpY < 8) {
				ret += 1;
			}
			if (tmpY > getRectangle().height - 16 && tmpY < getRectangle().height + 0) {
				ret += 4;
			}
		}
		// if (tmpY>-4 && tmpY<this.getHeight()+4) {
		// if (tmpX>0 && tmpX<8) ret+=8;
		// if (tmpX>this.getWidth()-4 && tmpX<this.getWidth()+4) ret+=2;
		// }
		return ret;
	}
}
