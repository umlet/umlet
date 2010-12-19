package com.umlet.control.diagram;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RepaintManager;
import javax.swing.ScrollPaneConstants;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Relation;
import com.umlet.gui.base.listeners.ScrollbarListener;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel implements Printable {
	private JScrollPane _scr;
	private Selector selector;
	private DiagramHandler handler;

	public DrawPanel(DiagramHandler handler) {
		this.handler = handler;
		this.setLayout(null);
		this.setBackground(Color.white);
		this.selector = new Selector(this);
		JScrollPane p = new JScrollPane() {
			@Override
			public void setEnabled(boolean en) {
				super.setEnabled(en);
				this.getViewport().getView().setEnabled(en);
			}
		};
		p.getHorizontalScrollBar().setUnitIncrement(20);
		p.getHorizontalScrollBar().setSize(0, 15);
		p.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		p.getVerticalScrollBar().setUnitIncrement(20);
		p.getVerticalScrollBar().setSize(15, 0);
		p.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		ScrollbarListener sbL = new ScrollbarListener(this);
		p.getHorizontalScrollBar().addMouseListener(sbL);
		p.getVerticalScrollBar().addMouseListener(sbL);

		p.setBorder(null);
		this.setScrollPanel(p);

	}

	@Override
	public void setEnabled(boolean en) {
		super.setEnabled(en);
		this.handler.setEnabled(en);
		for (Component c : this.getComponents())
			c.setEnabled(en);
		if (en) this.setBackground(new Color(255, 255, 255));
		else this.setBackground(new Color(235, 235, 235));
	}

	public DiagramHandler getHandler() {
		return this.handler;
	}

	private void setScrollPanel(JScrollPane scr) {
		_scr = scr;
		scr.setViewportView(this);
	}

	public JScrollPane getScrollPanel() {
		return _scr;
	}

	/**
	 * Returns the smalles possible rectangle which contains all entities and a border space around it
	 */
	public Rectangle getContentBounds(int borderSpace) {

		int minx = Integer.MAX_VALUE;
		int miny = Integer.MAX_VALUE;
		int maxx = 0;
		int maxy = 0;

		Vector<Entity> v = getAllEntities();
		for (int i = 0; i < v.size(); i++) {
			Entity e = v.elementAt(i);
			minx = Math.min(minx, e.getX() - borderSpace);
			miny = Math.min(miny, e.getY() - borderSpace);
			maxx = Math.max(maxx, e.getX() + e.getWidth() + borderSpace);
			maxy = Math.max(maxy, e.getY() + e.getHeight() + borderSpace);
		}
		return new Rectangle(minx, miny, maxx - minx, maxy - miny);
	}

	public void paintEntitiesIntoGraphics2D(Graphics2D g2d) {
		Vector<Entity> allEntities = getAllEntities();
		for (Entity entity : allEntities) {
			int x = entity.getX();
			int y = entity.getY();
			g2d.translate(x, y);
			entity.paint(g2d);
			g2d.translate(-x, -y);
		}
	}

	public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
		if (pageIndex > 0) return (NO_SUCH_PAGE);
		else {
			Graphics2D g2d = (Graphics2D) g;
			RepaintManager currentManager = RepaintManager.currentManager(this);
			currentManager.setDoubleBufferingEnabled(false);
			Rectangle bounds = this.getContentBounds(Constants.PRINTPADDING);
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			AffineTransform t = g2d.getTransform();
			double scale = Math.min(pageFormat.getImageableWidth() / bounds.width,
					pageFormat.getImageableHeight() / bounds.height);
			if (scale < 1) {
				t.scale(scale, scale);
				g2d.setTransform(t);
			}
			g2d.translate(-bounds.x, -bounds.y);
			this.paint(g2d);
			currentManager = RepaintManager.currentManager(this);
			currentManager.setDoubleBufferingEnabled(true);
			return (PAGE_EXISTS);
		}
	}

	public Vector<Entity> getAllEntities() {
		Vector<Entity> v = new Vector<Entity>();
		for (int i = 0; i < this.getComponentCount(); i++) {
			Component c = this.getComponent(i);
			if (c instanceof Entity) v.add((Entity) c);
		}
		return v;
	}

	public Vector<Entity> getNotInGroupEntitiesOnPanel() {
		Vector<Entity> all = this.getAllEntities();
		Vector<Entity> entities = new Vector<Entity>();
		for (Entity e : all) {
			if (!e.isPartOfGroup()) entities.add(e);
		}
		return entities;
	}

	public Vector<Relation> getAllRelationsOnPanel() {
		Component[] tmp = this.getComponents();
		Vector<Relation> ret = new Vector<Relation>();
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i] instanceof Relation) ret.add((Relation) tmp[i]);
		}
		return ret;
	}

	public Selector getSelector() {
		return this.selector;
	}

	public void realignToGrid() { // LME
		Vector<Entity> v = this.getAllEntities();
		for (int i = 0; i < v.size(); i++) {
			Entity entity = v.elementAt(i);
			int x = entity.getX();
			int y = entity.getY();
			if (entity instanceof Relation) {
				Relation r = (Relation) entity;
				r.reLocate();
			}
			else {
				entity.setLocation(getHandler().ensureValueIsOnGrid(x), getHandler().ensureValueIsOnGrid(y));
			}
		}
	}

	/**
	 * If entities are out of the visible drawpanel border on the upper left
	 * corner this method enlarges the drawpanel and displays scrollbars
	 */
	public void insertUpperLeftWhitespaceIfNeeded() {

		Rectangle diaWithoutWhite = getContentBounds(0);
		// We must adjust the components and the view by a certain factor
		int adjustWidth = 0;
		if (diaWithoutWhite.getX() < 0) adjustWidth = (int) diaWithoutWhite.getX();

		int adjustHeight = 0;
		if (diaWithoutWhite.getY() < 0) adjustHeight = (int) diaWithoutWhite.getY();
		// If any adjustment is needed we move the components and increase the view position
		if ((adjustWidth != 0) || (adjustHeight != 0)) {
			for (int i = 0; i < this.getComponents().length; i++) {
				Component c = this.getComponent(i);
				c.setLocation(c.getX() - adjustWidth, c.getY() - adjustHeight);
			}
		}

		if (adjustWidth < 0) setHorizontalScrollbarVisibility(true);
		if (adjustHeight < 0) setVerticalScrollbarVisibility(true);

		int width = (int) (_scr.getHorizontalScrollBar().getValue() + getViewableDiagrampanelSize().getWidth() - adjustWidth);
		int height = (int) (_scr.getVerticalScrollBar().getValue() + getViewableDiagrampanelSize().getHeight() - adjustHeight);
		setPreferredSize(new Dimension(width, height));

		changeViewPosition(-adjustWidth, -adjustHeight);
	}

	/**
	 * Changes the viewposition of the drawpanel and recalculates the optimal drawpanelsize
	 */
	public void changeViewPosition(int incx, int incy) {
		Point viewp = _scr.getViewport().getViewPosition();
		_scr.getViewport().setViewSize(getPreferredSize());
		_scr.getViewport().setViewPosition(new Point(viewp.x + incx, viewp.y + incy));
	}

	/**
	 * If there is a scrollbar visible and a unnecessary whitespace on any border of the diagram
	 * which is not visible (but possibly scrollable by scrollbars) we remove this whitespace
	 */
	public void removeUnnecessaryWhitespaceAroundDiagram() {

		Rectangle diaWithoutWhite = getContentBounds(0);
		Dimension viewSize = getViewableDiagrampanelSize();
		int horSbPos = _scr.getHorizontalScrollBar().getValue();
		int verSbPos = _scr.getVerticalScrollBar().getValue();

		int newX = 0;
		if (_scr.getHorizontalScrollBar().isShowing()) {
			if (horSbPos > diaWithoutWhite.getX()) newX = (int) diaWithoutWhite.getX();
			else newX = handler.ensureValueIsOnGrid(horSbPos);
		}

		int newY = 0;
		if (_scr.getVerticalScrollBar().isShowing()) {
			if (verSbPos > diaWithoutWhite.getY()) newY = (int) diaWithoutWhite.getY();
			else newY = handler.ensureValueIsOnGrid(verSbPos);
		}

		int newWidth = (int) (horSbPos + viewSize.getWidth());
		// If the diagram exceeds the right viewable border the width must be adjusted
		if (diaWithoutWhite.getX() + diaWithoutWhite.getWidth() > horSbPos + viewSize.getWidth()) {
			newWidth = (int) (diaWithoutWhite.getX() + diaWithoutWhite.getWidth());
		}

		int newHeight = (int) (verSbPos + viewSize.getHeight());
		// If the diagram exceeds the lower viewable border the width must be adjusted
		if (diaWithoutWhite.getY() + diaWithoutWhite.getHeight() > verSbPos + viewSize.getHeight()) {
			newHeight = (int) (diaWithoutWhite.getY() + diaWithoutWhite.getHeight());
		}

		for (int i = 0; i < this.getComponents().length; i++) {
			Component c = this.getComponent(i);
			c.setLocation(c.getX() - newX, c.getY() - newY);
		}

		changeViewPosition(-newX, -newY);
		setPreferredSize(new Dimension(newWidth - newX, newHeight - newY));

		checkIfScrollbarsAreNecessary();
	}

	/**
	 * Returns the visible size of the drawpanel
	 */
	public Dimension getViewableDiagrampanelSize() {
		return _scr.getViewport().getSize();
	}

	private void checkIfScrollbarsAreNecessary() {
		/**
		 * Afterwards recheck if scrollbars are necessary
		 * This is needed to avoid appearing scrollbars if the diagramm is on the bottom right
		 */

		Rectangle diaWithoutWhite = getContentBounds(0);
		Dimension viewSize = getViewableDiagrampanelSize();

		// Only if the scrollbar is visible we must respect its size to calculate the visibility of the scrollbar
		int verSbWidth = 0;
		int horSbHeight = 0;
		if (_scr.getVerticalScrollBar().isShowing()) verSbWidth = _scr.getVerticalScrollBar().getWidth();
		if (_scr.getHorizontalScrollBar().isShowing()) horSbHeight = _scr.getHorizontalScrollBar().getHeight();

		boolean vertWasVisible = isVerticalScrollbarVisible();
		boolean horWasVisible = isHorizontalScrollbarVisible();

		// If the horizontal scrollbar is on the most left point && the the right end of the diagram without whitespace <= the viewable width incl. the width of the vertical scrollbar we hide the horizontal scrollbar
		if ((_scr.getHorizontalScrollBar().getValue() < handler.getGridSize()) && ((diaWithoutWhite.getX() + diaWithoutWhite.getWidth()) <= (viewSize.getWidth() + verSbWidth))) setHorizontalScrollbarVisibility(false);
		// This is needed to hide the scrollbar if it's only scrollable by a value lower than the gridsize. Otherwise this scrollbar would remain visible to avoid grid-jumps of the diagram (only on the lower right corner)
		// We also hide it if the horizontal scrollbar has lower than gridsize && the viewable diagrampanel width plus the horizontal scrollbar value is equal the right end of the diagram without whitespace
		else if ((_scr.getHorizontalScrollBar().getValue() < handler.getGridSize()) && (getViewableDiagrampanelSize().getWidth() + _scr.getHorizontalScrollBar().getValue() == diaWithoutWhite.getX() + diaWithoutWhite.getWidth())) setHorizontalScrollbarVisibility(false);
		else setHorizontalScrollbarVisibility(true);

		if ((_scr.getVerticalScrollBar().getValue() < handler.getGridSize()) && ((diaWithoutWhite.getY() + diaWithoutWhite.getHeight()) <= (viewSize.getHeight() + horSbHeight))) setVerticalScrollbarVisibility(false);
		else if ((_scr.getVerticalScrollBar().getValue() < handler.getGridSize()) && (getViewableDiagrampanelSize().getHeight() + _scr.getVerticalScrollBar().getValue() == diaWithoutWhite.getY() + diaWithoutWhite.getHeight())) setVerticalScrollbarVisibility(false);
		else setVerticalScrollbarVisibility(true);
		// adjust x and y to avoid jumping diagram if both scrollbars were visible and one of them disappears (only in the upper left corner)
		int adx = 0;
		int ady = 0;
		if ((_scr.getHorizontalScrollBar().getValue() != 0) && vertWasVisible && !isVerticalScrollbarVisible()) adx = handler.ensureValueIsOnGrid(horSbHeight);
		if ((_scr.getVerticalScrollBar().getValue() != 0) && horWasVisible && !isHorizontalScrollbarVisible()) ady = handler.ensureValueIsOnGrid(verSbWidth);

		if ((adx != 0) || (ady != 0)) {
			setPreferredSize(new Dimension((int) (getPreferredSize().getWidth() + adx), (int) getPreferredSize().getHeight() + ady));
			changeViewPosition(adx, ady);
		}

		revalidate();
	}

	private void setHorizontalScrollbarVisibility(boolean visible) {
		if (visible) _scr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		else _scr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	}

	private void setVerticalScrollbarVisibility(boolean visible) {
		if (visible) _scr.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		else _scr.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
	}

	private boolean isHorizontalScrollbarVisible() {
		if (_scr.getHorizontalScrollBarPolicy() == ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS) return true;
		else return false;
	}

	private boolean isVerticalScrollbarVisible() {
		if (_scr.getVerticalScrollBarPolicy() == ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS) return true;
		else return false;
	}

	private void drawGrid(Graphics2D g2d) {
		g2d.setColor(Constants.GRID_COLOR);

		int gridSize = handler.getGridSize();
		if (gridSize == 1) return; // Gridsize 1 would only make the whole screen grey

		int width = 2000 + (int) getPreferredSize().getWidth();
		int height = 1000 + (int) getPreferredSize().getHeight();
		for (int i = gridSize; i < width; i += gridSize) {
			g2d.drawLine(i, 0, i, height);
		}
		for (int i = gridSize; i < height; i += gridSize) {
			g2d.drawLine(0, i, width, i);
		}
	}

	@Override
	protected void paintChildren(Graphics g) {

		// Possible Zoom of the drawpanel (not a real zoom because it doesn't resize anything)
		// Graphics2D g2 = (Graphics2D)g;
		// AffineTransform t = g2.getTransform();
		// float scale = 0.5f; //this.getHandler().getGridSize() / 10
		// t.scale(scale, scale);
		// g2.setTransform(t);

		if (Constants.show_grid) drawGrid((Graphics2D) g);

		// Graphics2D g2d = (Graphics2D) g;
		// g2d.setStroke(Constants.getStroke(1, 1));
		//		
		// // g2d.setColor(Color.BLUE);
		// // int w = handler.getDrawPanel().getScrollPanel().getViewport().getViewPosition().x;
		// // int h = handler.getDrawPanel().getScrollPanel().getViewport().getViewPosition().y;
		// // System.out.println(w + "," + h);
		// // System.out.println(_scr.getHorizontalScrollBar().getValue() + "/ " + _scr.getVerticalScrollBar().getValue());
		// // g2d.drawRect(w, h, (int) w+2, h+2);
		// // g2d.setColor(Color.GRAY);
		// // Dimension dim = getViewableDiagrampanelSize();
		// // g2d.drawRect(0, 0, (int) dim.getWidth(), (int) dim.getHeight());
		// // g2d.setColor(Color.RED);
		// // dim = getPreferredSize();
		// // g2d.drawRect(0, 0, (int) dim.getWidth(), (int) dim.getHeight());
		//				
		// g2d.setStroke(Constants.getStroke(0, 1));
		// this.repaint();

		super.paintComponents(g);
	}

}
