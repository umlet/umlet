package com.umlet.element;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Vector;

import com.baselet.control.Constants.LineType;
import com.baselet.control.Utils;
import com.baselet.element.GridElement;
import com.baselet.element.StickingPolygon;


@SuppressWarnings("serial")
public class Class extends GridElement {

	// A.Mueller start
	private Vector<Class> innerClasses;
	private boolean _isTemplate = false;
	private boolean _isInnerClass = false;
	private String _panelString = "";
	private int _templateHeight = 0;
	private int _templateWidth = 0;

	// A.Mueller end

	public Class() {
		super();
		innerClasses = new Vector<Class>(); // A.Mueller
	}

	private Vector<String> getStringVector() {
		if (this.isInnerClass()) return Utils.decomposeStrings(_panelString); // A.Mueller
		Vector<String> ret = Utils.decomposeStrings(this.getPanelAttributes());
		return ret;
	}

	// A.Mueller start
	public Vector<Class> getInnerClasses() {
		return this.innerClasses;
	}

	public boolean isInnerClass() {
		return _isInnerClass;
	}

	public void setIsInnerClass(boolean i) {
		this._isInnerClass = i;
	}

	public void setPanelString(String p) {
		this._panelString = p;
	}

	// A.Mueller end

	@Override
	public void paintEntity(Graphics g) {

		float zoom = getHandler().getZoomFactor();
		
//		setAutoresize(50, 50);

		int innerSoFar = 0; // A.Mueller
		_isTemplate = false;
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		
		Vector<String> tmp = getStringVector();
		int yPos = 0;

		yPos += (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();

		Composite[] composites = colorize(g2); // LME: enable colors
		g2.setColor(fgColor);

		Rectangle r = this.getBounds();
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
			else if (s.startsWith("template") && (i == 0)) {
				String[] template = s.split("=");
				if (template.length != 2) _isTemplate = false;
				else _isTemplate = true;
			}
		}

		if (!_isTemplate) {
			g2.setComposite(composites[1]); // set aplha composite for drawing the background color
			g2.setColor(bgColor);
			g2.fillRect(0, 0, (int) r.getWidth() - 1, (int) r.getHeight() - 1);
			g2.setComposite(composites[0]); // reset composite settings
			if (isSelected) g2.setColor(fgColor);
			else g2.setColor(fgColorBase);
			g2.setStroke(Utils.getStroke(lineType, thickness));
			g2.drawRect(0, 0, (int) r.getWidth() - 1, (int) r.getHeight() - 1);
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
				if (_isTemplate) g2.drawLine(0, yPos, this.getWidth() - 1 - this.getWidth() / 10, yPos);
				else g2.drawLine(0, yPos, this.getWidth() - 1, yPos);
				// A.Mueller end

				yPos += (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();

				// A.Mueller start
			}
			else if (s.equals("{active}") && (i == 0)) {
				g2.drawLine((int) this.getHandler().getFontHandler().getFontSize() / 2, 0, (int) this.getHandler().getFontHandler().getFontSize() / 2, this.getHeight() - 1);
				g2.drawLine(this.getWidth() - (int) this.getHandler().getFontHandler().getFontSize() / 2, 0, this.getWidth() - (int) this.getHandler().getFontHandler().getFontSize() / 2, this.getHeight() - 1);
				yPos = this.getHeight() / 2 - (tmp.size() - 1) * ((int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts())) / 2;
			}
			else if (s.startsWith("template") && (i == 0)) {
				String[] template = s.split("=");
				if (template.length == 2) {

					_templateWidth = this.getHandler().getFontHandler().getTextWidth(template[1]) + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
					_templateHeight = (int) (this.getHandler().getFontHandler().getFontSize() + this.getHandler().getFontHandler().getDistanceBetweenTexts()) + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();

					Polygon border = new Polygon();
					border.addPoint(0, _templateHeight / 2);
					border.addPoint(getWidth() - _templateWidth, _templateHeight / 2);
					border.addPoint(0, _templateHeight / 2);
					border.addPoint(0, this.getHeight());
					border.addPoint(0, this.getHeight() - 1);
					border.addPoint(this.getWidth() - this.getWidth() / 10, this.getHeight() - 1);
					border.addPoint(this.getWidth() - this.getWidth() / 10, this.getHeight() - 1);
					border.addPoint(this.getWidth() - this.getWidth() / 10, _templateHeight + 1);
					border.addPoint(getWidth() - _templateWidth, _templateHeight + 1);
					border.addPoint(getWidth() - _templateWidth, _templateHeight / 2);

					g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
					g2.setComposite(composites[1]); // set alpha composite
					g2.setColor(bgColor);
					g2.fillRect(getWidth() - _templateWidth, 0, _templateWidth, _templateHeight + 1);
					g2.fillPolygon(border);
					g2.setComposite(composites[0]); // reset composite
					if (isSelected) g2.setColor(fgColor);
					else g2.setColor(fgColorBase);

					// draw border lines of template box
					g2.drawRect(getWidth() - _templateWidth, 0, _templateWidth - 1, _templateHeight); // template box
					this.getHandler().getFontHandler().writeText(g2, template[1], getWidth() - _templateWidth + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts(), (int) this.getHandler().getFontHandler().getFontSize() + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts(), false);
					g2.setStroke(Utils.getStroke(LineType.SOLID, 1));

					// draw border lines of class
					g2.drawLine(0, _templateHeight / 2, getWidth() - _templateWidth, _templateHeight / 2);
					g2.drawLine(0, _templateHeight / 2, 0, this.getHeight());
					g2.drawLine(0, this.getHeight() - 1, this.getWidth() - this.getWidth() / 10, this.getHeight() - 1);
					g2.drawLine(this.getWidth() - this.getWidth() / 10, this.getHeight() - 1, this.getWidth() - this.getWidth() / 10, _templateHeight);

					yPos = yPos + _templateHeight + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();
				}
				else _isTemplate = false;
			}
			else if (s.equals("{innerclass")) {
				String state = "";
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
						if (innerCount > 0) innerCount--;
						else break;
					}
					else innerLines++;
					state = state + "\n" + tmp.elementAt(i);
				}

				Class temp;
				try {
					temp = innerClasses.get(innerSoFar);
					temp.setPanelString(state);
				} catch (ArrayIndexOutOfBoundsException e) {
					temp = new Class();
					innerClasses.add(innerSoFar, temp);
					temp.setHandler(this.getHandler());
					temp.setIsInnerClass(true);
					temp.setPanelString(state);
					innerSoFar++;
				}

				int height = innerLines * (int) this.getHandler().getFontHandler().getFontSize() + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts()
						+ (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() * --innerLines;

				if (this.isSelected()) temp.onSelected();
				else temp.onDeselected();

				temp.setLocation(5, yPos);

				if (_isTemplate) temp.setSize((int) (this.getWidth() - this.getWidth() / 10 - 10 * zoom), height);
				else temp.setSize((int) (this.getWidth() - 10 * zoom), height);

				temp.paintEntity(g.create((int) (5 * zoom), yPos, (int) (this.getWidth() - 5 * zoom), temp.getHeight()));
				yPos = yPos + temp.getHeight() + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts();

				// A.Mueller end

			}
			else {
				if (isSelected) g2.setColor(fgColor);
				else g2.setColor(fgColorBase);
				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				if (CENTER) {
					// A.Mueller
					if (_isTemplate) this.getHandler().getFontHandler().writeText(g2, s, (this.getWidth() - this.getWidth() / 10) / 2, yPos, true);
					else this.getHandler().getFontHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
				}
				else {
					this.getHandler().getFontHandler().writeText(g2, s, (int) this.getHandler().getFontHandler().getFontSize() / 2, yPos, false);
				}
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
			}
		}
		// g2.setStroke(Constants.getStroke(0, 1));
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		// LME: define the polygon on which relations stick on
		if (!_isTemplate) return super.generateStickingBorder(x, y, width, height);
		StickingPolygon p = new StickingPolygon();
		p.addPoint(new Point(x, y + _templateHeight / 2));
		p.addPoint(new Point(x + (9 * width) / 10 + 1, y + _templateHeight / 2));
		p.addPoint(new Point(x + (9 * width) / 10 + 1, y + height));
		p.addPoint(new Point(x, y + height), true);
		return p;
	}

}
