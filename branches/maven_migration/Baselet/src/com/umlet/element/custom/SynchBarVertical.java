package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.Set;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.command.Resize;
import com.baselet.element.OldGridElement;
import com.baselet.element.sticking.StickingPolygon;


@SuppressWarnings("serial")
public class SynchBarVertical extends OldGridElement {
	private static int textHeight = 0;

	@Override
	public void paintEntity(Graphics g) {

		float zoom = Main.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		int yPos = 0;
		textHeight = 0; // reset

		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		textHeight = tmp.size() * ((int) (Main.getHandlerForElement(this).getFontHandler().getFontSize() + Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts()));
		boolean ADAPT_SIZE = false;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
			TextLayout l = new TextLayout(s, Main.getHandlerForElement(this).getFontHandler().getFont(), g2.getFontRenderContext());
			Rectangle2D r2d = l.getBounds();
			int width = (int) r2d.getWidth();
			if ((this.getRectangle().width / 2 - width / 2) < 0) {
				ADAPT_SIZE = true;
				break;
			}
			Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, this.getRectangle().width / 2, yPos, AlignHorizontal.CENTER);
			yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}

		if (ADAPT_SIZE) {
			(new Resize(this, -Main.getHandlerForElement(this).getGridSize(), 0, 0, 0)).execute(Main.getHandlerForElement(this));
			(new Resize(this, 0, 0, Main.getHandlerForElement(this).getGridSize(), 0)).execute(Main.getHandlerForElement(this));
			return;
		}
		if (yPos > this.getRectangle().height) {
			(new Resize(this, 0, 0, 0, (int) (Main.getHandlerForElement(this).getFontHandler().getFontSize() + Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts()))).execute(Main.getHandlerForElement(this));
			return;
		}

		g2.fillRect(this.getRectangle().width / 2 - (int) (3 * zoom), textHeight + (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(), (int) (5 * zoom), this.getRectangle().height - textHeight - (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() * 2);
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
		StickingPolygon p = new StickingPolygon(0, 0);
		p.addPoint(x + width / 2, y + textHeight);
		p.addPoint(x + width / 2, y + height);
		return p;
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		Set<Direction> directions = super.getResizeArea(x, y);
		directions.remove(Direction.LEFT); // allow height changes only
		directions.remove(Direction.RIGHT);
		return directions;
	}
}
