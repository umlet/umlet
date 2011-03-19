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
public class SynchBarHorizontal extends GridElement {
	private static int textWidth = 0;

	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		textWidth = 0; // reset
		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = this.getHeight() / 2 - tmp.size() * ((int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts())) / 2;
		boolean ADAPT_SIZE_X = false;
		int textHeight = tmp.size() * ((int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts()));

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);

			TextLayout l = new TextLayout(s, this.getHandler().getFontHandler().getFont(), Constants.FRC);
			Rectangle2D r2d = l.getBounds();
			textWidth = ((int) r2d.getWidth() > textWidth) ? ((int) r2d.getWidth()) : (textWidth);

			if ((this.getWidth() - textWidth) < 0) {
				ADAPT_SIZE_X = true;
				break;
			}
			yPos += (int) this.getHandler().getFontHandler().getFontSize();
			this.getHandler().getFontHandler().writeText(g2, s, 0, yPos, false);
			yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
		}

		if (ADAPT_SIZE_X) {
			(new Resize(this, -this.getHandler().getGridSize(), 0, 0, 0)).execute(this.getHandler());
			(new Resize(this, 0, 0, this.getHandler().getGridSize(), 0)).execute(this.getHandler());
			return;
		}

		if (textHeight > this.getHeight()) {
			(new Resize(this, 0, 0, 0, 20)).execute(this.getHandler());
			return;
		}

		g2.fillRect(textWidth + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts(), getHeight() / 2 - (int) (3 * zoom), this.getWidth() - textWidth - (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() * 2, (int) (5 * zoom));
	}

	/*
	 * public int doesCoordinateAppearToBeConnectedToMe(Point p) {
	 * int ret=0;
	 * int tmpX=p.x-this.getX();
	 * int tmpY=p.y-this.getY();
	 * if (tmpX>(textWidth+4) && tmpX<this.getWidth()+4) {
	 * //if (tmpY>0 && tmpY<8) ret+=1;
	 * if (tmpY>this.getHeight()/2-8 && tmpY<this.getHeight()/2+8) ret+=4;
	 * }
	 * return ret;
	 * }
	 */
	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon();
		p.addLine(new Point(x + textWidth, y + height / 2), new Point(x + width, y + height / 2));
		return p;
	}

	@Override
	public int getPossibleResizeDirections() { // allow width changes only
		return Constants.RESIZE_LEFT | Constants.RESIZE_RIGHT;
	}
}
