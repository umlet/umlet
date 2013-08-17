package com.umlet.element.custom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.element.OldGridElement;


@SuppressWarnings("serial")
public class SynchBar extends OldGridElement {
	@Override
	public void paintEntity(Graphics g) {

		float zoom = Main.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		g2.setColor(Color.red);
		

		int yPos = 0;
		yPos += (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
			Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, 0, yPos, AlignHorizontal.CENTER);
			yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}

		// g2.fillRect(0,7,this.getWidth(),this.getHeight()-15);
		g2.fillRect(0, (int) (7 * zoom), this.getRectangle().width, (int) (5 * zoom));
	}

	public int doesCoordinateAppearToBeConnectedToMe(Point p) {
		int ret = 0;
		int tmpX = p.x - this.getRectangle().x;
		int tmpY = p.y - this.getRectangle().y;

		if ((tmpX > -4) && (tmpX < this.getRectangle().width + 4)) {
			if ((tmpY > 0) && (tmpY < 8)) ret += 1;
			if ((tmpY > this.getRectangle().height - 16) && (tmpY < this.getRectangle().height + 0)) ret += 4;
		}
		// if (tmpY>-4 && tmpY<this.getHeight()+4) {
		// if (tmpX>0 && tmpX<8) ret+=8;
		// if (tmpX>this.getWidth()-4 && tmpX<this.getWidth()+4) ret+=2;
		// }
		return ret;
	}
}
