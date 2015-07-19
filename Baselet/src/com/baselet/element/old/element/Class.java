package com.baselet.element.old.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Vector;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.LineType;
import com.baselet.control.util.Utils;
import com.baselet.element.old.OldGridElement;
import com.baselet.element.sticking.StickingPolygon;

@SuppressWarnings("serial")
public class Class extends OldGridElement {

	// A.Mueller start
	private final Vector<Class> innerClasses = new Vector<Class>();
	private boolean _isTemplate = false;
	private boolean _isInnerClass = false;
	private String _panelString = "";
	private int _templateHeight = 0;
	private int _templateWidth = 0;

	// A.Mueller end

	private Vector<String> getStringVector() {
		if (isInnerClass()) {
			return Utils.decomposeStrings(_panelString); // A.Mueller
		}
		Vector<String> ret = Utils.decomposeStrings(getPanelAttributes());
		return ret;
	}

	// A.Mueller start
	public Vector<Class> getInnerClasses() {
		return innerClasses;
	}

	public boolean isInnerClass() {
		return _isInnerClass;
	}

	public void setIsInnerClass(boolean i) {
		_isInnerClass = i;
	}

	public void setPanelString(String p) {
		_panelString = p;
	}

	// A.Mueller end

	@Override
	public void paintEntity(Graphics g) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		// setAutoresize(50, 50);

		int innerSoFar = 0; // A.Mueller
		_isTemplate = false;
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());

		Vector<String> tmp = getStringVector();
		int yPos = 0;

		yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();

		Composite[] composites = colorize(g2); // LME: enable colors
		g2.setColor(fgColor);

		Rectangle r = getRectangle();
		LineType lineType = LineType.SOLID;
		int thickness = 1;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("bt=.")) {
				lineType = LineType.DASHED;
			}
			if (s.equals("bt=*")) {
				thickness = 2;
			}
			else if (s.startsWith("template") && i == 0) {
				String[] template = s.split("=");
				if (template.length != 2) {
					_isTemplate = false;
				}
				else {
					_isTemplate = true;
				}
			}
		}

		if (!_isTemplate) {
			g2.setComposite(composites[1]); // set aplha composite for drawing the background color
			g2.setColor(bgColor);
			g2.fillRect(0, 0, r.getWidth() - 1, r.getHeight() - 1);
			g2.setComposite(composites[0]); // reset composite settings
			if (HandlerElementMap.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) {
				g2.setColor(fgColor);
			}
			else {
				g2.setColor(fgColorBase);
			}
			g2.setStroke(Utils.getStroke(lineType, thickness));
			g2.drawRect(0, 0, r.getWidth() - 1, r.getHeight() - 1);
			g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
		}

		boolean CENTER = true;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("bt=.") || s.equals("bt=*")) {
				/* don't draw these lines because they are only for markup */
			}
			else if (s.equals("--")) {
				CENTER = false;

				// A.Mueller start
				if (_isTemplate) {
					g2.drawLine(0, yPos, getRectangle().width - 1 - getRectangle().width / 10, yPos);
				}
				else {
					g2.drawLine(0, yPos, getRectangle().width - 1, yPos);
					// A.Mueller end
				}

				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();

				// A.Mueller start
			}
			else if (s.equals("{active}") && i == 0) {
				g2.drawLine((int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2, 0, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2, getRectangle().height - 1);
				g2.drawLine(getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2, 0, getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2, getRectangle().height - 1);
				yPos = getRectangle().height / 2 - (tmp.size() - 1) * (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts()) / 2;
			}
			else if (s.startsWith("template") && i == 0) {
				String[] template = s.split("=");
				if (template.length == 2) {

					_templateWidth = (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextWidth(template[1]) + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts());
					_templateHeight = (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts()) + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();

					Polygon border = new Polygon();
					border.addPoint(0, _templateHeight / 2);
					border.addPoint(getRectangle().width - _templateWidth, _templateHeight / 2);
					border.addPoint(0, _templateHeight / 2);
					border.addPoint(0, getRectangle().height);
					border.addPoint(0, getRectangle().height - 1);
					border.addPoint(getRectangle().width - getRectangle().width / 10, getRectangle().height - 1);
					border.addPoint(getRectangle().width - getRectangle().width / 10, getRectangle().height - 1);
					border.addPoint(getRectangle().width - getRectangle().width / 10, _templateHeight + 1);
					border.addPoint(getRectangle().width - _templateWidth, _templateHeight + 1);
					border.addPoint(getRectangle().width - _templateWidth, _templateHeight / 2);

					g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
					g2.setComposite(composites[1]); // set alpha composite
					g2.setColor(bgColor);
					g2.fillRect(getRectangle().width - _templateWidth, 0, _templateWidth, _templateHeight + 1);
					g2.fillPolygon(border);
					g2.setComposite(composites[0]); // reset composite
					if (HandlerElementMap.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) {
						g2.setColor(fgColor);
					}
					else {
						g2.setColor(fgColorBase);
					}

					// draw border lines of template box
					g2.drawRect(getRectangle().width - _templateWidth, 0, _templateWidth - 1, _templateHeight); // template box
					HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, template[1], getRectangle().width - _templateWidth + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(), (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(), AlignHorizontal.LEFT);
					g2.setStroke(Utils.getStroke(LineType.SOLID, 1));

					// draw border lines of class
					g2.drawLine(0, _templateHeight / 2, getRectangle().width - _templateWidth, _templateHeight / 2);
					g2.drawLine(0, _templateHeight / 2, 0, getRectangle().height);
					g2.drawLine(0, getRectangle().height - 1, getRectangle().width - getRectangle().width / 10, getRectangle().height - 1);
					g2.drawLine(getRectangle().width - getRectangle().width / 10, getRectangle().height - 1, getRectangle().width - getRectangle().width / 10, _templateHeight);

					yPos = yPos + _templateHeight + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
				}
				else {
					_isTemplate = false;
				}
			}
			else if (s.equals("{innerclass")) {
				StringBuilder sb = new StringBuilder("");
				// Counting the inner lines helps to determine the Height of the
				// resulting innerClass (lines times the fontsize gives us the height of
				// the contents) by adding also the INNER words we make sure to have enough
				// space at the bottom since the lines of the classes also need space
				int innerLines = 0;
				int innerCount = 0;
				for (i++; i < tmp.size(); i++) {
					if (tmp.elementAt(i).equals("{innerclass")) {
						innerCount++;
						innerLines++;
					}
					else if (tmp.elementAt(i).equals("innerclass}")) {
						if (innerCount > 0) {
							innerCount--;
						}
						else {
							break;
						}
					}
					else {
						innerLines++;
					}
					sb.append("\n").append(tmp.elementAt(i));
				}

				String state = sb.toString();
				Class temp;
				try {
					temp = innerClasses.get(innerSoFar);
					temp.setPanelString(state);
				} catch (ArrayIndexOutOfBoundsException e) {
					temp = new Class();
					innerClasses.add(innerSoFar, temp);
					HandlerElementMap.getHandlerForElement(this).setHandlerAndInitListeners(temp);
					temp.setIsInnerClass(true);
					temp.setPanelString(state);
					innerSoFar++;
				}

				int height = innerLines * (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() * --innerLines;

				temp.setLocation(5, yPos);

				if (_isTemplate) {
					temp.setSize((int) (getRectangle().width - getRectangle().width / 10 - 10 * zoom), height);
				}
				else {
					temp.setSize((int) (getRectangle().width - 10 * zoom), height);
				}

				temp.paintEntity(g.create((int) (5 * zoom), yPos, (int) (getRectangle().width - 5 * zoom), temp.getRectangle().height));
				yPos = yPos + temp.getRectangle().height + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();

				// A.Mueller end

			}
			else {
				if (HandlerElementMap.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) {
					g2.setColor(fgColor);
				}
				else {
					g2.setColor(fgColorBase);
				}
				yPos += (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();
				if (CENTER) {
					// A.Mueller
					if (_isTemplate) {
						HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, (getRectangle().width - getRectangle().width / 10.0) / 2.0, yPos, AlignHorizontal.CENTER);
					}
					else {
						HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, getRectangle().width / 2.0, yPos, AlignHorizontal.CENTER);
					}
				}
				else {
					HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(g2, s, (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() / 2.0, yPos, AlignHorizontal.LEFT);
				}
				yPos += HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts();
			}
		}
		// g2.setStroke(Constants.getStroke(0, 1));
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		// LME: define the polygon on which relations stick on
		if (!_isTemplate) {
			return super.generateStickingBorder(x, y, width, height);
		}
		StickingPolygon p = new StickingPolygon(0, 0);
		p.addPoint(x, y + _templateHeight / 2);
		p.addPoint(x + 9 * width / 10 + 1, y + _templateHeight / 2);
		p.addPoint(x + 9 * width / 10 + 1, y + height);
		p.addPoint(x, y + height, true);
		return p;
	}

}
