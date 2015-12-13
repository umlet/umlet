package com.baselet.element.old.element;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.Set;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.Direction;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.gui.command.OldResize;

@SuppressWarnings("serial")
public class SynchBarHorizontal extends OldGridElement {
	private int textWidth = 0;

	@Override
	public void paintEntity(Graphics g) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		colorize(g2); // enable colors
		g2.setColor(fgColor);

		textWidth = 0; // reset
		Vector<String> tmp = Utils.decomposeStrings(getPanelAttributes());
		int yPos = getRectangle().height / 2 - tmp.size() * (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts()) / 2;
		boolean ADAPT_SIZE_X = false;
		int textHeight = tmp.size() * (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts());

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);

			TextLayout l = new TextLayout(s, HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont(), g2.getFontRenderContext());
			Rectangle2D r2d = l.getBounds();
			textWidth = (int) r2d.getWidth() > textWidth ? (int) r2d.getWidth() : textWidth;

			if (getRectangle().width - textWidth < 0) {
				ADAPT_SIZE_X = true;
				break;
			}
			yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
			HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, 0, yPos, AlignHorizontal.LEFT);
			yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
		}

		if (ADAPT_SIZE_X) {
			new OldResize(this, -HandlerElementMap.getHandlerForElement(this).getGridSize(), 0, 0, 0).execute(HandlerElementMap.getHandlerForElement(this));
			new OldResize(this, 0, 0, HandlerElementMap.getHandlerForElement(this).getGridSize(), 0).execute(HandlerElementMap.getHandlerForElement(this));
			return;
		}

		if (textHeight > getRectangle().height) {
			new OldResize(this, 0, 0, 0, 20).execute(HandlerElementMap.getHandlerForElement(this));
			return;
		}

		g2.fillRect(textWidth + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(), getRectangle().height / 2 - (int) (3 * zoom), getRectangle().width - textWidth - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() * 2, (int) (5 * zoom));
	}

	/* public int doesCoordinateAppearToBeConnectedToMe(Point p) { int ret=0; int tmpX=p.x-this.getX(); int tmpY=p.y-this.getY(); if (tmpX>(textWidth+4) && tmpX<this.getWidth()+4) { //if (tmpY>0 && tmpY<8) ret+=1; if (tmpY>this.getHeight()/2-8 && tmpY<this.getHeight()/2+8) ret+=4; } return ret; } */
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
