package com.umlet.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.element.OldGridElement;
import com.baselet.element.sticking.StickingPolygon;


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
		int a = Math.max(1, (getRectangle().width - 1) / 2);
		int b = (getRectangle().height - 1) / 2;
		boolean found = false;
		int x = ((getRectangle().width - 1) / 9 * 4);
		int y = (int) Math.round((Math.sqrt(((a * a * b * b) - (b * b * x * x)) / (a * a))));
		int yPos = 0;
		int yPos1 = b;
		Graphics2D g2 = (Graphics2D) g;
		DiagramHandler handlerForElement = Main.getHandlerForElement(this);
		g2.setFont(handlerForElement.getFontHandler().getFont());
		Composite[] composites = colorize(g2); // enable colors
		g2.setColor(bgColor);

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillOval(0, 0, 2 * a, 2 * b);
		g2.setComposite(composites[0]);
		if (handlerForElement.getDrawPanel().getSelector().isSelected(this)) g2.setColor(fgColor);
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
			yPos = this.getRectangle().height / 2 - tmp.size() * ((int) (handlerForElement.getFontHandler().getFontSize() + handlerForElement.getFontHandler().getDistanceBetweenTexts())) / 2;
		}

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if ((s.equals("--")) && found) {
				yPos = yPos1;
			}
			else if (found) {
				handlerForElement.getFontHandler().writeText(g2, s, a, yPos + 5, AlignHorizontal.CENTER);
				yPos += 5 * handlerForElement.getFontHandler().getDistanceBetweenTexts();

			}
			else {
				yPos += (int) handlerForElement.getFontHandler().getFontSize();
				handlerForElement.getFontHandler().writeText(g2, s, this.getRectangle().width / 2, yPos, AlignHorizontal.CENTER);
				yPos += handlerForElement.getFontHandler().getDistanceBetweenTexts();
			}
		}
		g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
	}

	 @Override
	 public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
	 StickingPolygon p = new StickingPolygon(0, 0);
	
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
