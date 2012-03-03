package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import com.baselet.control.Constants;
import com.baselet.control.Utils;
import com.baselet.diagram.command.Resize;
import com.baselet.element.GridElement;
import com.baselet.element.StickingPolygon;


@SuppressWarnings("serial")
public class SynchBarVertical extends GridElement {
	private static int textHeight = 0;

	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		int yPos = 0;
		textHeight = 0; // reset

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		textHeight = tmp.size() * ((int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts()));
		boolean ADAPT_SIZE = false;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getFontHandler().getFontSize();
			TextLayout l = new TextLayout(s, this.getHandler().getFontHandler().getFont(), Constants.FRC);
			Rectangle2D r2d = l.getBounds();
			int width = (int) r2d.getWidth();
			if ((this.getWidth() / 2 - width / 2) < 0) {
				ADAPT_SIZE = true;
				break;
			}
			this.getHandler().getFontHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
			yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
		}

		if (ADAPT_SIZE) {
			(new Resize(this, -this.getHandler().getGridSize(), 0, 0, 0)).execute(this.getHandler());
			(new Resize(this, 0, 0, this.getHandler().getGridSize(), 0)).execute(this.getHandler());
			return;
		}
		if (yPos > this.getHeight()) {
			(new Resize(this, 0, 0, 0, (int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts()))).execute(this.getHandler());
			return;
		}

		g2.fillRect(this.getWidth() / 2 - (int) (3 * zoom), textHeight + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts(), (int) (5 * zoom), this.getHeight() - textHeight - (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() * 2);
	}

	/*
	 * public int doesCoordinateAppearToBeConnectedToMe(Point p) {
	 * int ret=0;
	 * int tmpX=p.x-this.getX();
	 * int tmpY=p.y-this.getY();
	 * if (tmpY>textHeight+4 && tmpY<this.getHeight()+4) {
	 * //if (tmpX>0 && tmpX<16) ret+=8;
	 * if (tmpX>this.getWidth()/2-4 && tmpX<this.getWidth()/2+4) ret+=2;
	 * }
	 * return ret;
	 * }
	 */

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon();
		p.addLine(new Point(x + width / 2, y + textHeight), new Point(x + width / 2, y + height));
		return p;
	}

	@Override
	public int getPossibleResizeDirections() { // allow height changes only
		return Constants.RESIZE_TOP | Constants.RESIZE_BOTTOM;
	}
}
