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
public class SynchBarHorizontal extends OldGridElement {
	private static int textWidth = 0;

	@Override
	public void paintEntity(Graphics g) {

		float zoom = Main.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);
		

		textWidth = 0; // reset
		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = this.getRectangle().height / 2 - tmp.size() * ((int) (Main.getHandlerForElement(this).getFontHandler().getFontSize() + Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts())) / 2;
		boolean ADAPT_SIZE_X = false;
		int textHeight = tmp.size() * ((int) (Main.getHandlerForElement(this).getFontHandler().getFontSize() + Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts()));

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);

			TextLayout l = new TextLayout(s, Main.getHandlerForElement(this).getFontHandler().getFont(), g2.getFontRenderContext());
			Rectangle2D r2d = l.getBounds();
			textWidth = ((int) r2d.getWidth() > textWidth) ? ((int) r2d.getWidth()) : (textWidth);

			if ((this.getRectangle().width - textWidth) < 0) {
				ADAPT_SIZE_X = true;
				break;
			}
			yPos += (int) Main.getHandlerForElement(this).getFontHandler().getFontSize();
			Main.getHandlerForElement(this).getFontHandler().writeText(g2, s, 0, yPos, AlignHorizontal.LEFT);
			yPos += Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}

		if (ADAPT_SIZE_X) {
			(new Resize(this, -Main.getHandlerForElement(this).getGridSize(), 0, 0, 0)).execute(Main.getHandlerForElement(this));
			(new Resize(this, 0, 0, Main.getHandlerForElement(this).getGridSize(), 0)).execute(Main.getHandlerForElement(this));
			return;
		}

		if (textHeight > this.getRectangle().height) {
			(new Resize(this, 0, 0, 0, 20)).execute(Main.getHandlerForElement(this));
			return;
		}

		g2.fillRect(textWidth + (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(), getRectangle().height / 2 - (int) (3 * zoom), this.getRectangle().width - textWidth - (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() * 2, (int) (5 * zoom));
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
		StickingPolygon p = new StickingPolygon(0, 0);
		p.addPoint(x + textWidth, y + height / 2);
		p.addPoint(x + width, y + height / 2);
		return p;
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		Set<Direction> returnSet = super.getResizeArea(x, y);
		returnSet.remove(Direction.UP);
		returnSet.remove(Direction.DOWN);
		return returnSet;
	}
}
