package com.umlet.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.LineType;
import com.baselet.control.Utils;
import com.baselet.element.OldGridElement;
import com.baselet.element.StickingPolygon;


@SuppressWarnings("serial")
public class UseCase extends OldGridElement {

	public UseCase() {
		super();
	}

	private Vector<String> getStringVector() {
		Vector<String> ret = Utils.decomposeStrings(this.getPanelAttributes());
		return ret;
	}

	@Override
	public void paintEntity(Graphics g) {
		int a = Math.max(1, (getSize().width - 1) / 2);
		int b = (getSize().height - 1) / 2;
		boolean found = false;
		int x = ((getSize().width - 1) / 9 * 4);
		int y = (int) Math.round((Math.sqrt(((a * a * b * b) - (b * b * x * x)) / (a * a))));
		int yPos = 0;
		int yPos1 = b;
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(bgColor);

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillOval(0, 0, 2 * a, 2 * b);
		g2.setComposite(composites[0]);
		if (isSelected()) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);

		Vector<String> tmp = new Vector<String>(getStringVector());
		if (tmp.contains("lt=.")) {
			tmp.remove("lt=.");
			g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
		}
		g2.drawOval(0, 0, 2 * a, 2 * b);

		if (tmp.contains("--")) {
			yPos = ((b - y) / 2);
			g2.drawLine(a - x, b - y, a + x, b - y);
			found = true;
		}
		else {
			yPos = this.getSize().height / 2 - tmp.size() * ((int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts())) / 2;
		}

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if ((s.equals("--")) && found) {
				yPos = yPos1;
			}
			else if (found) {
				this.getHandler().getFontHandler().writeText(g2, s, a, yPos + 5, AlignHorizontal.CENTER);
				yPos += 5 * this.getHandler().getFontHandler().getDistanceBetweenTexts();

			}
			else {
				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				this.getHandler().getFontHandler().writeText(g2, s, this.getSize().width / 2, yPos, AlignHorizontal.CENTER);
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
			}
		}
		g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
	}

	 @Override
	 public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
	 StickingPolygon p = new StickingPolygon();
	
	 //First point is the top left then the points are added clockwise
	 p.addPoint(new Point(x + width / 4, y));
	 p.addPoint(new Point(x + width * 3 / 4, y));
				
	 p.addPoint(new Point(x + width, y + height / 4));
	 p.addPoint(new Point(x + width, y + height * 3 / 4));
				
	 p.addPoint(new Point(x + width * 3 / 4, y + height));
	 p.addPoint(new Point(x + width / 4, y + height));
				
	 p.addPoint(new Point(x, y + height * 3 / 4));
	 p.addPoint(new Point(x, y + height / 4), true);
				
	 return p;
	 }

}
