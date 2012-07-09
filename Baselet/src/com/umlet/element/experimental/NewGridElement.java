package com.umlet.element.experimental;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.element.GridComponent;
import com.baselet.element.GridElement;
import com.baselet.element.Group;
import com.baselet.element.StickingPolygon;
import com.umlet.element.Relation;

public abstract class NewGridElement implements GridElement {

	protected final static Logger log = Logger.getLogger(Utils.getClassName());

	private boolean stickingBorderActive;

	private boolean allowResize = true;

	protected BaseDrawHandler drawer;
	private DiagramHandler handler;

	protected boolean isSelected = false;

	private Group group = null;
	private boolean autoresizeandmanualresizeenabled;

	protected String panelAttributes = "";
	protected String panelAttributesAdditional = "";

	private String fgColorString;
	private String bgColorString;

	private GridComponent component = new GridComponent() {
		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g) {
			drawer = new BaseDrawHandler(g, handler, Constants.DEFAULT_FOREGROUND_COLOR, Constants.DEFAULT_BACKGROUND_COLOR, component.getSize(), isSelected);
			colorize();
			paintElement();
		}

		@Override
		public boolean contains(Point p) {
			Rectangle bounds = this.getVisibleRect();
			if (!bounds.contains(p)) return false;

			Vector<GridElement> entities = handler.getDrawPanel().getAllEntitiesNotInGroup();
			for (GridElement other : entities) {
				if (other instanceof Relation) { // a relation is always on top
					// move point to coordinate system of other entity
					Point other_p = new Point(p.x + this.getLocation().x - other.getLocation().x, p.y + this.getLocation().y - other.getLocation().y);
					if (other.getComponent().contains(other_p)) return false;
				}

				// If the this visibleRect is equal to the other VisibleRect, true will be returned. Otherwise we need to check further
				else if (!this.getVisibleRect().equals(other.getVisibleRect())) {
					Rectangle other_bounds = other.getVisibleRect();
					// move bounds to coordinate system of this component
					other_bounds.x += other.getLocation().x - this.getLocation().x;
					other_bounds.y += other.getLocation().y - this.getLocation().y;
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

		@Override
		public GridElement getGridElement() {
			return NewGridElement.this;
		}

	};

	protected abstract void paintElement();

	public void init(Rectangle bounds, String panelAttributes, String panelAttributesAdditional, DiagramHandler handler) {
		this.setBounds(bounds);
		this.panelAttributes = panelAttributes;
		this.panelAttributesAdditional = panelAttributesAdditional;
		setHandlerAndInitListeners(handler);
	}

	@Override
	public DiagramHandler getHandler() {
		return handler;
	}

	@Override
	public void setHandlerAndInitListeners(DiagramHandler handler) {
		if (this.getHandler() != null) {
			this.removeMouseListener(this.getHandler().getEntityListener(this));
			this.removeMouseMotionListener(this.getHandler().getEntityListener(this));
		}
		this.handler = handler;
		this.addMouseListener(this.getHandler().getEntityListener(this));
		this.addMouseMotionListener(this.getHandler().getEntityListener(this));
	}

	@Override
	public String getPanelAttributes() {
		return panelAttributes;
	}

	@Override
	public boolean isSelected() {
		return isSelected;
	}

	@Override
	public void setPanelAttributes(String panelAttributes) {
		this.panelAttributes = panelAttributes;
	}

	@Override
	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public GridElement CloneFromMe() {
		try {
			java.lang.Class<? extends GridElement> cx = this.getClass(); // get class of dynamic object
			GridElement c = cx.newInstance();
			c.setPanelAttributes(this.getPanelAttributes()); // copy states
			c.setBounds(this.getBounds());
			c.setHandlerAndInitListeners(this.getHandler());
			return c;
		} catch (Exception e) {
			log.error("Error at calling CloneFromMe() on entity", e);
		}
		return null;
	}

	@Override
	public void changeLocation(int diffx, int diffy) {
		this.setLocation(this.getLocation().x + diffx, this.getLocation().y + diffy);
	}

	@Override
	public void onDeselected() {
		isSelected = false;
		this.repaint();
	}

	@Override
	public void onSelected() {
		isSelected = true;
		this.repaint();
	}

	private void colorize() {
		Color bgColor = Constants.DEFAULT_BACKGROUND_COLOR;
		float bgAlpha = Constants.ALPHA_FULL_TRANSPARENCY;
		Color fgColor = Constants.DEFAULT_FOREGROUND_COLOR;
		for (String line : Utils.decomposeStringsWithComments(panelAttributes)) {
			if (line.startsWith("bg=")) {
				bgColorString = line.substring("bg=".length());
				bgColor = Utils.getColor(bgColorString);
				if (bgColor == null) bgColor = Constants.DEFAULT_BACKGROUND_COLOR; // if value is unknown, default background color is used (at 100% transparency)
				else bgAlpha = Constants.ALPHA_MIDDLE_TRANSPARENCY; // otherwise set 50% transparency to make sure the background color is visible
			}
			else if (line.startsWith("fg=")) {
				fgColorString = line.substring("fg=".length());
				fgColor = Utils.getColor(fgColorString);
				if (fgColor == null) fgColor = Constants.DEFAULT_FOREGROUND_COLOR; // if value is unknown, default foreground color is used
			}
		}

		drawer.setBackground(bgColor, bgAlpha);
		drawer.setForegroundColor(fgColor);
	}

	@Override
	public void updateProperty(String key, String newValue) {
		String newState = "";
		for (String line : Utils.decomposeStringsWithComments(this.getPanelAttributes())) {
			if (!line.startsWith(key)) newState += line + "\n";
		}
		newState = newState.substring(0, newState.length()-1); //remove last linebreak
		if (newValue != null) newState += "\n" + key + "=" + newValue; // null will not be added as a value
		this.setPanelAttributes(newState);
		this.getHandler().getDrawPanel().getSelector().updateSelectorInformation(); // update the property panel to display changed attributes
		this.repaint();
	}

	@Override
	public String getFGColorString() {
		return fgColorString;
	}

	@Override
	public String getBGColorString() {
		return bgColorString;
	}

	@Override
	public Group getGroup() {
		return this.group;
	}

	@Override
	public String getAdditionalAttributes() {
		return "";
	}

	@Override
	public void setAdditionalAttributes(String additional_attributes) { }

	@Override
	public boolean isPartOfGroup() {
		if (this.group != null) return true;
		return false;
	}

	@Override
	public boolean isInRange(Point upperLeft, Dimension size) {
		Rectangle2D rect1 = new Rectangle2D.Double(upperLeft.getX(), upperLeft.getY(), size.getWidth(), size.getHeight());
		Rectangle2D rect2 = new Rectangle2D.Double(getLocation().x, getLocation().y, getSize().width, getSize().height);
		return (rect1.contains(rect2));
	}

	@Override
	public int getResizeArea(int x, int y) {
		int ret = 0;
		if ((x <= 5) && (x >= 0)) ret = Constants.RESIZE_LEFT;
		else if ((x <= this.getSize().width) && (x >= this.getSize().width - 5)) ret = Constants.RESIZE_RIGHT;

		if ((y <= 5) && (y >= 0)) ret = ret | Constants.RESIZE_TOP;
		else if ((y <= this.getSize().height) && (y >= this.getSize().height - 5)) ret = ret | Constants.RESIZE_BOTTOM;
		return ret;
	}

	@Override
	public int getPossibleResizeDirections() {
		if (this.allowResize) return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
		return 0;
	}

	@Override
	public void setStickingBorderActive(boolean stickingBordersActive) {
		this.stickingBorderActive = stickingBordersActive;
	}

	@Override
	public boolean isStickingBorderActive() {
		return stickingBorderActive;
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon();
		p.addPoint(new Point(x, y));
		p.addPoint(new Point(x + width, y));
		p.addPoint(new Point(x + width, y + height));
		p.addPoint(new Point(x, y + height), true);
		return p;
	}

	@Override
	public void setManualResized() {
		if (autoresizeandmanualresizeenabled) {
			if (!this.isManResized()) {
				this.setPanelAttributes(this.getPanelAttributes() + Constants.NEWLINE + Constants.NOAUTORESIZE);
				if (this.equals(Main.getInstance().getEditedGridElement())) Main.getInstance().setPropertyPanelToGridElement(this);
			}
		}
	}

	private boolean isManResized() {
		Vector<String> lines = Utils.decomposeStringsWithComments(this.getPanelAttributes());
		for (String line : lines) {
			if (line.startsWith(Constants.NOAUTORESIZE)) return true;
		}
		return false;
	}

	@Override
	public void changeSize(int diffx, int diffy) {
		this.setSize(this.getSize().width + diffx, this.getSize().height + diffy);
	}

	@Override
	public Rectangle getVisibleRect() {
		return component.getVisibleRect();
	}

	@Override
	public void setBounds(Rectangle bounds) {
		component.setBounds(bounds);
	}

	@Override
	public void addMouseListener(MouseListener mouseListener) {
		component.addMouseListener(mouseListener);
	}

	@Override
	public void addMouseMotionListener(MouseMotionListener mouseMotionListener) {
		component.addMouseMotionListener(mouseMotionListener);
	}

	@Override
	public void removeMouseMotionListener(MouseMotionListener mouseMotionListener) {
		component.removeMouseMotionListener(mouseMotionListener);
	}

	@Override
	public void removeMouseListener(MouseListener mouseListener) {
		component.removeMouseListener(mouseListener);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		component.setBounds(x, y, width, height);
	}

	@Override
	public void setLocation(int x, int y) {
		component.setLocation(x, y);
	}

	@Override
	public void setSize(int width, int height) {
		component.setSize(width, height);
	}

	@Override
	public Point getLocation() {
		return component.getLocation();
	}

	@Override
	public Point getLocationOnScreen() {
		return component.getLocationOnScreen();
	}

	@Override
	public Rectangle getBounds() {
		return component.getBounds();
	}

	@Override
	public void repaint() {
		component.repaint();
	}

	@Override
	public Dimension getSize() {
		return component.getSize();
	}

	@Override
	public void paint(Graphics g) {
		component.paint(g);
	}

	@Override
	public Dimension getRealSize() {
		return new Dimension(getSize().width / handler.getGridSize() * Constants.DEFAULTGRIDSIZE, getSize().height / handler.getGridSize() * Constants.DEFAULTGRIDSIZE);
	}

	@Override
	public JComponent getComponent() {
		return component;
	}
	
	
}
