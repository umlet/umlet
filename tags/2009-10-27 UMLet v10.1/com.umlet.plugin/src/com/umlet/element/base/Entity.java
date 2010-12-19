// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.base;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import javax.swing.JComponent;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.StickingPolygon;

@SuppressWarnings("serial")
public abstract class Entity extends JComponent {

	private DiagramHandler handler;
	private boolean enabled;

	public Entity() {
		this.setSize(100, 100); // LME: initial size
		this.setVisible(true);
		this.enabled = true;
		this.autoresizeandmanualresizeenabled = false;
	}

	public DiagramHandler getHandler() {
		return handler;
	}

	protected void setHandler(DiagramHandler handler) {
		this.handler = handler;
	}

	public int[] getCoordinates() {
		int[] ret = new int[4];
		ret[0] = getX();
		ret[1] = getY();
		ret[2] = getWidth();
		ret[3] = getHeight();
		return ret;
	}

	public String getClassCode() {
		return this.getClass().getName().toUpperCase().substring(0, 4);
	}

	protected Color _selectedColor = Color.blue;
	protected Color _deselectedColor = Color.black;
	// A.Mueller start
	public float _zoomFactor = 1f;
	private Group group = null;
	private boolean autoresizeandmanualresizeenabled;

	public boolean isManualResized() {
		autoresizeandmanualresizeenabled = true;
		return this.isManResized();
	}

	private boolean isManResized() {
		Vector<String> lines = Constants.decomposeStringsWithComments(this.getState(), Constants.NEWLINE);
		for (String line : lines) {
			if (line.startsWith(Constants.NOAUTORESIZE)) return true;
		}
		return false;
	}

	protected boolean autoResizeandManualResizeEnabled() {
		return this.autoresizeandmanualresizeenabled;
	}

	public void setManualResized() {
		if (autoresizeandmanualresizeenabled) {
			if (!this.isManResized()) {
				this.setState(this.getState() + Constants.NEWLINE + Constants.NOAUTORESIZE);
				if (this.equals(Umlet.getInstance().getEditedEntity())) Umlet.getInstance().setPropertyPanelToEntity(this);
			}
		}
	}

	public String getAdditionalAttributes() {
		return "";
	}

	public void setAdditionalAttributes(String s) {

	}

	public Group getGroup() {
		return this.group;
	}

	protected void setGroup(Group group) {
		this.group = group;
	}

	public boolean isPartOfGroup() {
		if (this.group != null) return true;
		return false;
	}

	// returns true if the entity is part of group g
	// or if g = null and the entity is in no group
	// false otherwise
	public boolean isPartOfGroup(Group g) {
		if (g == null) {
			if (this.group == null) return true;
			else return false;
		}
		else if (g.equals(this.group)) return true;
		else return false;
	}

	public void removeGroup() {
		this.group = null;
	}

	public String getState() {
		return _state;
	}

	@Override
	public boolean contains(Point p) {
		Rectangle bounds = this.getVisibleRect();
		if (!bounds.contains(p)) return false;

		Vector<Entity> entities = this.getHandler().getDrawPanel().getNotInGroupEntitiesOnPanel();
		for (Entity other : entities) {
			if (other instanceof Relation) { // a relation is always on top
				// move point to coordinate system of other entity
				Point other_p = new Point(p.x + this.getX() - other.getX(), p.y + this.getY() - other.getY());
				if (other.contains(other_p)) return false;
			}

			// If the this visibleRect is equal to the other VisibleRect, true will be returned. Otherwise we need to check further
			else if (!this.getVisibleRect().equals(other.getVisibleRect())) {
				Rectangle other_bounds = other.getVisibleRect();
				// move bounds to coordinate system of this component
				other_bounds.x += other.getX() - this.getX();
				other_bounds.y += other.getY() - this.getY();
				if (other_bounds.contains(p)) { // the selected element has to be in front except if the other element is totally contained!
					if (bounds.contains(other_bounds) || (other.isSelected() && !other_bounds.contains(bounds))) return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean contains(int x, int y) {
		return this.contains(new Point(x, y));
	}

	// A.Mueller End
	// begin B. Buckl
	protected Color _fillColor = Color.white;
	// end B. Buckl

	protected Color _activeColor = _deselectedColor;

	public Entity CloneFromMe() {
		try { // LME
			java.lang.Class<? extends Entity> cx = this.getClass(); // get class of dynamic object
			Entity c = cx.newInstance();
			c.setState(this.getPanelAttributes()); // copy states
			c.setBounds(this.getBounds());
			return c;
		} catch (InstantiationException e) {
			System.err.println("UMLet -> Entity/" + this.getClass().toString() + ": " + e);
		} catch (IllegalAccessException e) {
			System.err.println("UMLet -> Entity/" + this.getClass().toString() + ": " + e);
		}
		return null;
	}

	protected boolean _selected = false;

	public boolean isSelected() {
		return _selected;
	}

	protected String _state = "";

	public String getPanelAttributes() {
		return _state;
	}

	public void setState(String s) {
		_state = s;
	}

	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) { // LME: define the polygon on which relations stick on
		StickingPolygon p = new StickingPolygon();
		p.addPoint(new Point(x, y));
		p.addPoint(new Point(x + width, y));
		p.addPoint(new Point(x + width, y + height));
		p.addPoint(new Point(x, y + height), true);
		return p;
	}

	public void assignToDiagram(DiagramHandler handler) {
		if (this.getHandler() != null) {
			this.removeMouseListener(this.getHandler().getEntityListener(this));
			this.removeMouseMotionListener(this.getHandler().getEntityListener(this));
		}
		this.setHandler(handler);
		this.addMouseListener(this.getHandler().getEntityListener(this));
		this.addMouseMotionListener(this.getHandler().getEntityListener(this));
	}

	@Override
	public void setEnabled(boolean en) {
		super.setEnabled(en);
		if (!en && enabled) {
			this.removeMouseListener(this.getHandler().getEntityListener(this));
			this.removeMouseMotionListener(this.getHandler().getEntityListener(this));
			enabled = false;
		}
		else if (en && !enabled) {
			if (!this.isPartOfGroup()) {
				this.addMouseListener(this.getHandler().getEntityListener(this));
				this.addMouseMotionListener(this.getHandler().getEntityListener(this));
			}
			enabled = true;
		}
	}

	public void changeLocation(int diffx, int diffy) {
		this.setLocation(this.getX() + diffx, this.getY() + diffy);
	}

	public void changeSize(int diffx, int diffy) {
		this.setSize(this.getWidth() + diffx, this.getHeight() + diffy);
	}

	public int getResizeArea(int x, int y) {
		int ret = 0;
		if ((x <= 5) && (x >= 0)) ret = Constants.RESIZE_LEFT;
		else if ((x <= this.getWidth()) && (x >= this.getWidth() - 5)) ret = Constants.RESIZE_RIGHT;

		if ((y <= 5) && (y >= 0)) ret = ret | Constants.RESIZE_TOP;
		else if ((y <= this.getHeight()) && (y >= this.getHeight() - 5)) ret = ret | Constants.RESIZE_BOTTOM;
		return ret;
	}

	public int getPossibleResizeDirections() { // LME
		return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
	}

	public void onSelected() {
		_selected = true;
		_activeColor = _selectedColor;
		this.repaint();
	}

	public void onDeselected() {
		_selected = false;
		_activeColor = _deselectedColor;
		this.repaint();
	}

	public final void drawStickingPolygon(Graphics2D g2) { // LME: draw the sticking polygon
		StickingPolygon poly;
		// The Java Implementations in the displaceDrawingByOnePixel list start at (1,1) to draw while any others start at (0,0)
		if (Constants.displaceDrawingByOnePixel()) poly = this.generateStickingBorder(1, 1, this.getWidth() - 1, this.getHeight() - 1);
		else poly = this.generateStickingBorder(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		if (poly != null) {
			Color c = g2.getColor();
			Stroke s = g2.getStroke();
			g2.setColor(this._selectedColor);
			g2.setStroke(Constants.getStroke(1, 1));
			poly.draw(g2);
			g2.setColor(c);
			g2.setStroke(s);
		}
	}

	public boolean isInRange(Point upperLeft, Dimension size) {
		Rectangle2D rect1 = new Rectangle2D.Double(upperLeft.getX(), upperLeft.getY(), size.getWidth(), size.getHeight());
		Rectangle2D rect2 = new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
		return (rect1.intersects(rect2));
	}

	public void setInProgress(Graphics g, boolean flag) {
		if (flag) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setFont(this.getHandler().getZoomedFont());
			g2.setColor(Color.red);
			this.getHandler().getFRC(g2);
			this.getHandler().writeText(g2, "in progress...", this.getWidth() / 2 - 40, this.getHeight() / 2 + (int) this.getHandler().getZoomedFontsize() / 2, false);
		}
		else {
			repaint();
		}
	}

	private String bgColor = "", fgColor = "";

	public String getFGColorString() {
		return fgColor;
	}

	public String getBGColorString() {
		return bgColor;
	}

	public boolean supportsColors = false;

	public Composite[] colorize(Graphics2D g2) {
		supportsColors = true;
		bgColor = "";
		fgColor = "";
		Vector<String> v = Constants.decomposeStringsWithComments(_state, Constants.NEWLINE);
		for (int i = 0; i < v.size(); i++) {
			String line = v.get(i);
			if (line.indexOf("bg=") >= 0) {
				bgColor = line.substring("bg=".length());
			}
			else if (line.indexOf("fg=") >= 0) {
				fgColor = line.substring("fg=".length());
			}
		}
		if (fgColor.equals("red")) _deselectedColor = Color.RED;
		else if (fgColor.equals("green")) _deselectedColor = Color.GREEN;
		else if (fgColor.equals("blue")) _deselectedColor = Color.BLUE;
		else if (fgColor.equals("yellow")) _deselectedColor = Color.YELLOW;
		else if (fgColor.equals("white")) _deselectedColor = Color.WHITE;
		else if (fgColor.equals("black")) _deselectedColor = Color.BLACK;
		else if (fgColor.equals("gray")) _deselectedColor = Color.GRAY;
		else if (fgColor.equals("orange")) _deselectedColor = Color.ORANGE;
		else if (fgColor.equals("magenta")) _deselectedColor = Color.MAGENTA;
		else if (fgColor.equals("pink")) _deselectedColor = Color.PINK;
		else {
			try {
				_deselectedColor = Color.decode(fgColor);
			} catch (NumberFormatException nfe) {
				_deselectedColor = Color.BLACK;
			}
		}

		if (bgColor.equals("red")) _fillColor = Color.RED;
		else if (bgColor.equals("green")) _fillColor = Color.GREEN;
		else if (bgColor.equals("blue")) _fillColor = Color.BLUE;
		else if (bgColor.equals("yellow")) _fillColor = Color.YELLOW;
		else if (bgColor.equals("white")) _fillColor = Color.WHITE;
		else if (bgColor.equals("black")) _fillColor = Color.BLACK;
		else if (bgColor.equals("gray")) _fillColor = Color.GRAY;
		else if (bgColor.equals("orange")) _fillColor = Color.ORANGE;
		else if (bgColor.equals("magenta")) _fillColor = Color.MAGENTA;
		else if (bgColor.equals("pink")) _fillColor = Color.PINK;
		else {
			try {
				_fillColor = Color.decode(bgColor);
			} catch (NumberFormatException nfe) {
				_fillColor = Color.WHITE;
			}
		}

		float alphaFactor = 0.5f;
		if (bgColor.equals("") || bgColor.equals("default")) alphaFactor = 0.0f;

		Composite old = g2.getComposite();
		AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaFactor);
		Composite composites[] = { old, alpha };
		return composites;
	}

	public void setColor(String colorString, boolean isForegroundColor) {
		String new_state = "";
		Vector<String> textlines = Constants.decomposeStringsWithComments(this.getPanelAttributes(), "\n");
		String prefix = (isForegroundColor ? "fg=" : "bg=");
		for (int i = 0; i < textlines.size(); i++) {
			if (!textlines.get(i).startsWith(prefix)) new_state += textlines.get(i) + "\n";
		}
		if (!colorString.equals("default")) new_state += prefix + colorString;
		this.setState(new_state);
		this.getHandler().getDrawPanel().getSelector().updateSelectorInformation(); // update the property panel to display changed attributes
		this.repaint();
	}

	@Override
	public final void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (this._selected && Constants.show_stickingpolygon && !this.isPartOfGroup()) this.drawStickingPolygon(g2);

		// Zoom per transform for certain elements. unused!
		// if (this instanceof SequenceDiagram) {
		// AffineTransform t = g2.getTransform();
		// float scale = ((float) this.getHandler().getGridSize()) / 10;
		// t.scale(scale, scale);
		// g2.setTransform(t);
		// }

		this.paintEntity(g2);
	}

	public abstract void paintEntity(Graphics g);
}
