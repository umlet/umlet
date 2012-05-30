package com.umlet.element.custom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Utils;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class SynchBar extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		g2.setColor(Color.red);
		

		int yPos = 0;
		yPos += (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getFontHandler().getFontSize();
			this.getHandler().getFontHandler().writeText(g2, s, 0, yPos, AlignHorizontal.CENTER);
			yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
		}

		// g2.fillRect(0,7,this.getWidth(),this.getHeight()-15);
		g2.fillRect(0, (int) (7 * zoom), this.getSize().width, (int) (5 * zoom));
	}

	public int doesCoordinateAppearToBeConnectedToMe(Point p) {
		int ret = 0;
		int tmpX = p.x - this.getLocation().x;
		int tmpY = p.y - this.getLocation().y;

		if ((tmpX > -4) && (tmpX < this.getSize().width + 4)) {
			if ((tmpY > 0) && (tmpY < 8)) ret += 1;
			if ((tmpY > this.getSize().height - 16) && (tmpY < this.getSize().height + 0)) ret += 4;
		}
		// if (tmpY>-4 && tmpY<this.getHeight()+4) {
		// if (tmpX>0 && tmpX<8) ret+=8;
		// if (tmpX>this.getWidth()-4 && tmpX<this.getWidth()+4) ret+=2;
		// }
		return ret;
	}
}
