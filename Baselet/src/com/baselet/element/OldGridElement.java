package com.baselet.element;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
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

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.control.Constants.LineType;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.umlet.element.Relation;

public abstract class OldGridElement extends GridComponent implements GridElement {

	private static final long serialVersionUID = 1L;

	protected final static Logger log = Logger.getLogger(Utils.getClassName());

	private DiagramHandler handler;
	private boolean enabled;
	private boolean stickingBorderActive;
	private boolean autoresizeandmanualresizeenabled;
	private Group group = null;
	protected String panelAttributes = "";
	protected boolean isSelected = false;

	// deselectedColor and fgColor must be stored separately because selection changes the actual fgColor but not the fgColorBase
	protected Color fgColorBase = Color.black;
	protected Color fgColor = fgColorBase;
	private String fgColorString = "";
	protected Color bgColor = Color.white;
	private String bgColorString = "";
	protected float alphaFactor;

	public OldGridElement() {
		this.setSize(100, 100);
		this.setVisible(true);
		this.enabled = true;
		this.autoresizeandmanualresizeenabled = false;
		this.stickingBorderActive = true;
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

	@Override
	public boolean isStickingBorderActive() {
		return stickingBorderActive;
	}

	@Override
	public void setStickingBorderActive(boolean stickingBordersActive) {
		this.stickingBorderActive = stickingBordersActive;
	}

	public boolean isManualResized() {
		autoresizeandmanualresizeenabled = true;
		return this.isManResized();
	}

	private boolean isManResized() {
		Vector<String> lines = Utils.decomposeStringsWithComments(this.getPanelAttributes());
		for (String line : lines) {
			if (line.startsWith(Constants.NOAUTORESIZE)) return true;
		}
		return false;
	}

	protected boolean isAutoResizeandManualResizeEnabled() {
		return autoresizeandmanualresizeenabled;
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

	@Override
	public Group getGroup() {
		return this.group;
	}

	@Override
	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public boolean isPartOfGroup() {
		if (this.group != null) return true;
		return false;
	}

	// Some GridElements need additionalAttributes to be displayed correctly (eg: Relations need exact positions for edges)
	@Override
	public String getAdditionalAttributes() {
		return "";
	}

	@Override
	public void setAdditionalAttributes(String s) { }

	@Override
	public String getPanelAttributes() {
		return panelAttributes;
	}

	@Override
	public void setPanelAttributes(String panelAttributes) {
		this.panelAttributes = panelAttributes;
	}

	@Override
	public boolean isSelected() {
		return isSelected;
	}

	@Override
	public void onSelected() {
		isSelected = true;
		fgColor = Constants.DEFAULT_SELECTED_COLOR;
		this.repaint();
	}

	@Override
	public void onDeselected() {
		isSelected = false;
		fgColor = fgColorBase;
		this.setStickingBorderActive(true);
		this.repaint();
	}

	public Color getFgColor() {
		return fgColor;
	}

	@Override
	public String getFGColorString() {
		return fgColorString;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public String getBGColorString() {
		return bgColorString;
	}

	@Override
	public void setColor(String colorString, boolean isForegroundColor) {
		String new_state = "";
		Vector<String> textlines = Utils.decomposeStringsWithComments(this.getPanelAttributes());
		String prefix = (isForegroundColor ? "fg=" : "bg=");
		for (int i = 0; i < textlines.size(); i++) {
			if (!textlines.get(i).startsWith(prefix)) new_state += textlines.get(i) + "\n";
		}
		if (!colorString.equals("default")) new_state += prefix + colorString;
		this.setPanelAttributes(new_state);
		this.getHandler().getDrawPanel().getSelector().updateSelectorInformation(); // update the property panel to display changed attributes
		this.repaint();
	}

	public Composite[] colorize(Graphics2D g2) {
		bgColorString = "";
		fgColorString = "";
		bgColor = Constants.DEFAULT_BACKGROUND_COLOR;
		fgColorBase = Constants.DEFAULT_FOREGROUND_COLOR;
		Vector<String> v = Utils.decomposeStringsWithComments(panelAttributes);
		for (int i = 0; i < v.size(); i++) {
			String line = v.get(i);
			if (line.indexOf("bg=") >= 0) {
				bgColorString = line.substring("bg=".length());
				bgColor = Utils.getColor(bgColorString);
				if (bgColor == null) bgColor = Constants.DEFAULT_BACKGROUND_COLOR;
			}
			else if (line.indexOf("fg=") >= 0) {
				fgColorString = line.substring("fg=".length());
				fgColorBase = Utils.getColor(fgColorString);
				if (fgColorBase == null) fgColorBase = Constants.DEFAULT_FOREGROUND_COLOR;
				if (!isSelected) fgColor = fgColorBase;
			}
		}

		alphaFactor = Constants.ALPHA_MIDDLE_TRANSPARENCY;
		if (bgColorString.equals("") || bgColorString.equals("default")) alphaFactor = Constants.ALPHA_FULL_TRANSPARENCY;

		Composite old = g2.getComposite();
		AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaFactor);
		Composite composites[] = { old, alpha };
		return composites;
	}

	public int getRealWidth() {
		return getSize().width / handler.getGridSize() * Constants.DEFAULTGRIDSIZE;
	}

	public int getRealHeight() {
		return getSize().height / handler.getGridSize() * Constants.DEFAULTGRIDSIZE;
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
		return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
	}

	@Override
	public void changeSize(int diffx, int diffy) {
		this.setSize(this.getSize().width + diffx, this.getSize().height + diffy);
	}

	@Override
	public void changeLocation(int diffx, int diffy) {
		this.setLocation(this.getLocation().x + diffx, this.getLocation().y + diffy);
	}


	/**
	 * Must be overwritten because Swing uses this method to tell if 2 elements are overlapping
	 * It's also used to determine which element gets selected if there are overlapping elements (the smallest one)
	 */
	@Override
	public boolean contains(Point p) {
		Rectangle bounds = this.getVisibleRect();
		if (!bounds.contains(p)) return false;

		Vector<GridElement> entities = this.getHandler().getDrawPanel().getAllEntitiesNotInGroup();
		for (GridElement other : entities) {
			if (other instanceof Relation) { // a relation is always on top
				// move point to coordinate system of other entity
				Point other_p = new Point(p.x + this.getLocation().x - other.getLocation().x, p.y + this.getLocation().y - other.getLocation().y);
				if (other.contains(other_p)) return false;
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

	/**
	 * Must be overwritten because Swing uses this method to tell if 2 elements are overlapping
	 * It's also used to determine which element gets selected if there are overlapping elements (the smallest one)
	 */
	@Override
	public boolean contains(int x, int y) {
		return this.contains(new Point(x, y));
	}

	@Override
	public boolean isInRange(Point upperLeft, Dimension size) {
		Rectangle2D rect1 = new Rectangle2D.Double(upperLeft.getX(), upperLeft.getY(), size.getWidth(), size.getHeight());
		Rectangle2D rect2 = new Rectangle2D.Double(getLocation().x, getLocation().y, getSize().width, getSize().height);
		return (rect1.contains(rect2));
	}

	public void setInProgress(Graphics g, boolean flag) {
		if (flag) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setFont(this.getHandler().getFontHandler().getFont());
			g2.setColor(Color.red);
			this.getHandler().getFontHandler().writeText(g2, "in progress...", this.getSize().width / 2 - 40, this.getSize().height / 2 + (int) this.getHandler().getFontHandler().getFontSize() / 2, AlignHorizontal.LEFT);
		}
		else {
			repaint();
		}
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

	/**
	 * Define and return the polygon on which relations stick on
	 */
	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon();
		p.addPoint(new Point(x, y));
		p.addPoint(new Point(x + width, y));
		p.addPoint(new Point(x + width, y + height));
		p.addPoint(new Point(x, y + height), true);
		return p;
	}

	public final void drawStickingPolygon(Graphics2D g2) {
		StickingPolygon poly;
		// The Java Implementations in the displaceDrawingByOnePixel list start at (1,1) to draw while any others start at (0,0)
		if (Utils.displaceDrawingByOnePixel()) poly = this.generateStickingBorder(1, 1, this.getSize().width - 1, this.getSize().height - 1);
		else poly = this.generateStickingBorder(0, 0, this.getSize().width - 1, this.getSize().height - 1);
		if (poly != null) {
			Color c = g2.getColor();
			Stroke s = g2.getStroke();
			g2.setColor(Constants.DEFAULT_SELECTED_COLOR);
			g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
			poly.draw(g2);
			g2.setColor(c);
			g2.setStroke(s);
		}
	}

	@Override
	public final void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;

		if (this.isSelected && Constants.show_stickingpolygon && !this.isPartOfGroup()) this.drawStickingPolygon(g2);

		// Zoom per transformation for certain elements. unused!
		// if (this instanceof SequenceDiagram) {
		// AffineTransform t = g2.getTransform();
		// float scale = ((float) this.getHandler().getGridSize()) / 10;
		// t.scale(scale, scale);
		// g2.setTransform(t);
		// }

		this.paintEntity(g2);
	}

	public abstract void paintEntity(Graphics g);

	protected final int textHeight() {
		return (int) ((int) this.getHandler().getFontHandler().getFontSize(false) + this.getHandler().getFontHandler().getDistanceBetweenTexts(false));
	}

	protected final int textWidth(String text, boolean applyZoom) {
		return getHandler().getFontHandler().getTextSize(text, applyZoom).width + (int) getHandler().getFontHandler().getDistanceBetweenTexts(applyZoom);
	}

	@Override
	public JComponent getComponent() {
		return this;
	}

	@Override
	public GridElement getGridElement() {
		return this;
	}
}
