package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import com.baselet.element.Point;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.command.Resize;
import com.baselet.element.OldGridElement;
import com.baselet.element.StickingPolygon;


@SuppressWarnings("serial")
public class SynchBarVertical extends OldGridElement {
	private static int textHeight = 0;

	@Override
	public void paintEntity(Graphics g) {

		float zoom = Main.getElementHandlerMapping().get(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getElementHandlerMapping().get(this).getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		int yPos = 0;
		textHeight = 0; // reset

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		textHeight = tmp.size() * ((int) (Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize() + Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts()));
		boolean ADAPT_SIZE = false;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize();
			TextLayout l = new TextLayout(s, Main.getElementHandlerMapping().get(this).getFontHandler().getFont(), g2.getFontRenderContext());
			Rectangle2D r2d = l.getBounds();
			int width = (int) r2d.getWidth();
			if ((this.getZoomedSize().width / 2 - width / 2) < 0) {
				ADAPT_SIZE = true;
				break;
			}
			Main.getElementHandlerMapping().get(this).getFontHandler().writeText(g2, s, this.getZoomedSize().width / 2, yPos, AlignHorizontal.CENTER);
			yPos += Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts();
		}

		if (ADAPT_SIZE) {
			(new Resize(this, -Main.getElementHandlerMapping().get(this).getGridSize(), 0, 0, 0)).execute(Main.getElementHandlerMapping().get(this));
			(new Resize(this, 0, 0, Main.getElementHandlerMapping().get(this).getGridSize(), 0)).execute(Main.getElementHandlerMapping().get(this));
			return;
		}
		if (yPos > this.getZoomedSize().height) {
			(new Resize(this, 0, 0, 0, (int) (Main.getElementHandlerMapping().get(this).getFontHandler().getFontSize() + Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts()))).execute(Main.getElementHandlerMapping().get(this));
			return;
		}

		g2.fillRect(this.getZoomedSize().width / 2 - (int) (3 * zoom), textHeight + (int) Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts(), (int) (5 * zoom), this.getZoomedSize().height - textHeight - (int) Main.getElementHandlerMapping().get(this).getFontHandler().getDistanceBetweenTexts() * 2);
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
