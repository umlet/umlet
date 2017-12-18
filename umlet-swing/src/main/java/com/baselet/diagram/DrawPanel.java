package com.baselet.diagram;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.RepaintManager;
import javax.swing.ScrollPaneConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.Config;
import com.baselet.control.config.SharedConfig;
import com.baselet.control.constants.Constants;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.control.util.Utils;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.old.element.Relation;
import com.baselet.gui.filedrop.FileDrop;
import com.baselet.gui.filedrop.FileDropListener;
import com.baselet.gui.listener.ScrollbarListener;

@SuppressWarnings("serial")
public class DrawPanel extends JLayeredPane implements Printable {

	private static final Logger log = LoggerFactory.getLogger(DrawPanel.class);

	private final Point origin;
	private JScrollPane _scr;
	private final SelectorOld selector;
	private final DiagramHandler handler;

	private final List<GridElement> gridElements = new ArrayList<GridElement>();

	public DrawPanel(DiagramHandler handler, boolean initStartupTextAndFiledrop) {
		this.handler = handler;
		// AB: Origin is used to track diagram movement in Cut Command
		origin = new Point();
		setLayout(null);
		setBackground(Color.WHITE);
		setOpaque(true);
		selector = new SelectorOld(this);
		JScrollPane p = new JScrollPane() {
			@Override
			public void setEnabled(boolean en) {
				super.setEnabled(en);
				getViewport().getView().setEnabled(en);
			}
		};
		p.getHorizontalScrollBar().setUnitIncrement(50); // Using mousewheel on bar or click on arrow
		p.getHorizontalScrollBar().setSize(0, 15);
		p.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		p.getVerticalScrollBar().setUnitIncrement(50);
		p.getVerticalScrollBar().setSize(15, 0);
		p.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		ScrollbarListener sbL = new ScrollbarListener(this);
		p.getHorizontalScrollBar().addMouseListener(sbL);
		p.getVerticalScrollBar().addMouseListener(sbL);

		p.setBorder(null);
		setScrollPanel(p);

		if (Program.getInstance().getRuntimeType() != RuntimeType.BATCH) {
			// Wait until drawpanel is valid (eg: after loading a diagramm) and then update panel and scrollbars
			// Otherwise palettes which are larger than the viewable area would sometimes not have visible scrollbars until the first click into the palette
			new Timer("updatePanelAndScrollbars", true).schedule(new TimerTask() {
				@Override
				public void run() {
					if (isValid()) {
						updatePanelAndScrollbars();
						cancel();
					}
				}
			}, 25, 25);

			if (initStartupTextAndFiledrop) {
				StartUpHelpText startupHelpText = new StartUpHelpText(this);
				add(startupHelpText);
				@SuppressWarnings("unused")
				FileDrop fd = new FileDrop(startupHelpText, new FileDropListener()); // only init if this is not a BATCH call. Also fixes Issue 81
			}
		}

		this.repaint(); // repaint the drawpanel to be sure everything is visible (startuphelp etc)
	}

	@Override
	public void setEnabled(boolean en) {
		super.setEnabled(en);
		handler.setEnabled(en);
		for (Component c : getComponents()) {
			c.setEnabled(en);
		}
		if (en) {
			setBackground(new Color(255, 255, 255));
		}
		else {
			setBackground(new Color(235, 235, 235));
		}
	}

	public DiagramHandler getHandler() {
		return handler;
	}

	private void setScrollPanel(JScrollPane scr) {
		_scr = scr;
		scr.setViewportView(this);
	}

	public JScrollPane getScrollPane() {
		return _scr;
	}

	/**
	 * Returns the smalles possible rectangle which contains all entities and a border space around it
	 *
	 * @param borderSpace
	 *            the borderspace around the rectangle
	 * @param entities
	 *            the entities which should be included
	 * @return Rectangle which contains all entities with border space
	 */
	public static Rectangle getContentBounds(int borderSpace, Collection<GridElement> entities) {
		if (entities.size() == 0) {
			return new Rectangle(0, 0, 0, 0);
		}

		int minx = Integer.MAX_VALUE;
		int miny = Integer.MAX_VALUE;
		int maxx = 0;
		int maxy = 0;

		for (GridElement e : entities) {
			minx = Math.min(minx, e.getRectangle().x - borderSpace);
			miny = Math.min(miny, e.getRectangle().y - borderSpace);
			maxx = Math.max(maxx, e.getRectangle().x + e.getRectangle().width + borderSpace);
			maxy = Math.max(maxy, e.getRectangle().y + e.getRectangle().height + borderSpace);
		}
		return new Rectangle(minx, miny, maxx - minx, maxy - miny);
	}

	@Override
	public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
		if (pageIndex > 0) {
			return NO_SUCH_PAGE;
		}
		else {
			Graphics2D g2d = (Graphics2D) g;
			RepaintManager currentManager = RepaintManager.currentManager(this);
			currentManager.setDoubleBufferingEnabled(false);
			Rectangle bounds = getContentBounds(Config.getInstance().getPrintPadding(), getGridElements());
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			AffineTransform t = g2d.getTransform();
			double scale = Math.min(pageFormat.getImageableWidth() / bounds.width,
					pageFormat.getImageableHeight() / bounds.height);
			if (scale < 1) {
				t.scale(scale, scale);
				g2d.setTransform(t);
			}
			g2d.translate(-bounds.x, -bounds.y);
			paint(g2d);
			currentManager = RepaintManager.currentManager(this);
			currentManager.setDoubleBufferingEnabled(true);
			return PAGE_EXISTS;
		}
	}

	public List<GridElement> getGridElements() {
		return gridElements;
	}

	public List<Relation> getOldRelations() {
		return getHelper(Relation.class);
	}

	public List<com.baselet.element.relation.Relation> getStickables(Collection<GridElement> excludeList) {
		if (!SharedConfig.getInstance().isStickingEnabled() || handler instanceof PaletteHandler) {
			return Collections.<com.baselet.element.relation.Relation> emptyList();
		}
		List<com.baselet.element.relation.Relation> returnList = getHelper(com.baselet.element.relation.Relation.class);
		returnList.removeAll(excludeList);
		return returnList;
	}

	@SuppressWarnings("unchecked")
	private <T extends GridElement> List<T> getHelper(Class<T> filtered) {
		List<T> gridElementsToReturn = new ArrayList<T>();
		for (GridElement e : getGridElements()) {
			if (e.getClass().equals(filtered)) {
				gridElementsToReturn.add((T) e);
			}
		}
		return gridElementsToReturn;
	}

	public SelectorOld getSelector() {
		return selector;
	}

	/**
	 * This method must be called after every "significant change" on the drawpanel.
	 * This doesn't include every increment of dragging an grid element but it should be called after
	 * the grid elements new location is set (= after the mousebutton is released)
	 * It should be called only once after many grid elements have changed and not for each element!
	 * This makes it very hard to call this method by using listeners, therefore it's called explicitly in specific cases.
	 */
	public void updatePanelAndScrollbars() {
		insertUpperLeftWhitespaceIfNeeded();
		removeUnnecessaryWhitespaceAroundDiagram();
	}

	/**
	 * If entities are out of the visible drawpanel border on the upper left
	 * corner this method enlarges the drawpanel and displays scrollbars
	 */
	private void insertUpperLeftWhitespaceIfNeeded() {

		Rectangle diaWithoutWhite = getContentBounds(0, getGridElements());
		// We must adjust the components and the view by a certain factor
		int adjustWidth = 0;
		if (diaWithoutWhite.getX() < 0) {
			adjustWidth = diaWithoutWhite.getX();
		}

		int adjustHeight = 0;
		if (diaWithoutWhite.getY() < 0) {
			adjustHeight = diaWithoutWhite.getY();
		}

		moveOrigin(adjustWidth, adjustHeight);

		// If any adjustment is needed we move the components and increase the view position
		if (adjustWidth != 0 || adjustHeight != 0) {
			for (int i = 0; i < getComponents().length; i++) {
				Component c = getComponent(i);
				c.setLocation(handler.realignToGrid(false, c.getX() - adjustWidth), handler.realignToGrid(false, c.getY() - adjustHeight));
			}
		}

		if (adjustWidth < 0) {
			setHorizontalScrollbarVisibility(true);
		}
		if (adjustHeight < 0) {
			setVerticalScrollbarVisibility(true);
		}

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
	private void removeUnnecessaryWhitespaceAroundDiagram() {

		Rectangle diaWithoutWhite = getContentBounds(0, getGridElements());
		Dimension viewSize = getViewableDiagrampanelSize();
		int horSbPos = _scr.getHorizontalScrollBar().getValue();
		int verSbPos = _scr.getVerticalScrollBar().getValue();

		horSbPos = handler.realignToGrid(false, horSbPos);
		verSbPos = handler.realignToGrid(false, verSbPos);

		int newX = 0;
		if (_scr.getHorizontalScrollBar().isShowing()) {
			if (horSbPos > diaWithoutWhite.getX()) {
				newX = diaWithoutWhite.getX();
			}
			else {
				newX = horSbPos;
			}
		}

		int newY = 0;
		if (_scr.getVerticalScrollBar().isShowing()) {
			if (verSbPos > diaWithoutWhite.getY()) {
				newY = diaWithoutWhite.getY();
			}
			else {
				newY = verSbPos;
			}
		}

		int newWidth = (int) (horSbPos + viewSize.getWidth());
		// If the diagram exceeds the right viewable border the width must be adjusted
		if (diaWithoutWhite.getX() + diaWithoutWhite.getWidth() > horSbPos + viewSize.getWidth()) {
			newWidth = diaWithoutWhite.getX() + diaWithoutWhite.getWidth();
		}

		int newHeight = (int) (verSbPos + viewSize.getHeight());
		// If the diagram exceeds the lower viewable border the width must be adjusted
		if (diaWithoutWhite.getY() + diaWithoutWhite.getHeight() > verSbPos + viewSize.getHeight()) {
			newHeight = diaWithoutWhite.getY() + diaWithoutWhite.getHeight();
		}

		moveOrigin(newX, newY);

		for (GridElement ge : getGridElements()) {
			ge.setLocation(handler.realignToGrid(false, ge.getRectangle().x - newX), handler.realignToGrid(false, ge.getRectangle().y - newY));
		}

		changeViewPosition(-newX, -newY);
		setPreferredSize(new Dimension(newWidth - newX, newHeight - newY));

		checkIfScrollbarsAreNecessary();
	}

	/**
	 * Returns the visible size of the drawpanel
	 */
	public Dimension getViewableDiagrampanelSize() {
		return getVisibleRect().getSize();
	}

	private void checkIfScrollbarsAreNecessary() {
		/**
		 * Afterwards recheck if scrollbars are necessary
		 * This is needed to avoid appearing scrollbars if the diagramm is on the bottom right
		 */

		Rectangle diaWithoutWhite = getContentBounds(0, getGridElements());
		Dimension viewSize = getViewableDiagrampanelSize();

		boolean vertWasVisible = isVerticalScrollbarVisible();
		boolean horWasVisible = isHorizontalScrollbarVisible();

		// Only if the scrollbar is visible we must respect its size to calculate the visibility of the scrollbar
		int verSbWidth = 0;
		int horSbHeight = 0;
		if (vertWasVisible) {
			verSbWidth = _scr.getVerticalScrollBar().getWidth();
		}
		if (horWasVisible) {
			horSbHeight = _scr.getHorizontalScrollBar().getHeight();
		}

		// If the horizontal scrollbar is on the most left point && the the right end of the diagram without whitespace <= the viewable width incl. the width of the vertical scrollbar we hide the horizontal scrollbar
		if (_scr.getHorizontalScrollBar().getValue() < handler.getGridSize() && diaWithoutWhite.getX() + diaWithoutWhite.getWidth() <= viewSize.getWidth() + verSbWidth) {
			setHorizontalScrollbarVisibility(false);
		}
		else if (_scr.getHorizontalScrollBar().getValue() < handler.getGridSize() && getViewableDiagrampanelSize().width + _scr.getHorizontalScrollBar().getValue() == diaWithoutWhite.getX() + diaWithoutWhite.getWidth()) {
			setHorizontalScrollbarVisibility(false);
		}
		else {
			setHorizontalScrollbarVisibility(true);
		}

		if (_scr.getVerticalScrollBar().getValue() < handler.getGridSize() && diaWithoutWhite.getY() + diaWithoutWhite.getHeight() <= viewSize.getHeight() + horSbHeight) {
			setVerticalScrollbarVisibility(false);
		}
		else if (_scr.getVerticalScrollBar().getValue() < handler.getGridSize() && getViewableDiagrampanelSize().height + _scr.getVerticalScrollBar().getValue() == diaWithoutWhite.getY() + diaWithoutWhite.getHeight()) {
			setVerticalScrollbarVisibility(false);
		}
		else {
			setVerticalScrollbarVisibility(true);
		}

		// REMOVED TO FIX JUMPING PALETTE ENTRIES AT COPYING/CUTTING
		// adjust x and y to avoid jumping diagram if both scrollbars were visible and one of them disappears (only in the upper left corner)
		int adx = 0;
		int ady = 0;
		if (_scr.getHorizontalScrollBar().getValue() != 0 && vertWasVisible && !isVerticalScrollbarVisible()) {
			adx = handler.realignToGrid(false, horSbHeight);
		}
		if (_scr.getVerticalScrollBar().getValue() != 0 && horWasVisible && !isHorizontalScrollbarVisible()) {
			ady = handler.realignToGrid(false, verSbWidth);
		}

		if (adx != 0 || ady != 0) {
			setPreferredSize(new Dimension((int) (getPreferredSize().getWidth() + adx), (int) getPreferredSize().getHeight() + ady));
			changeViewPosition(adx, ady);
		}
	}

	private void setHorizontalScrollbarVisibility(boolean visible) {
		if (visible) {
			_scr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		}
		else {
			_scr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		}
	}

	private void setVerticalScrollbarVisibility(boolean visible) {
		if (visible) {
			_scr.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		}
		else {
			_scr.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		}
	}

	private boolean isHorizontalScrollbarVisible() {
		return _scr.getHorizontalScrollBarPolicy() == ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
	}

	private boolean isVerticalScrollbarVisible() {
		return _scr.getVerticalScrollBarPolicy() == ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
	}

	private void drawGrid(Graphics2D g2d) {
		g2d.setColor(Constants.GRID_COLOR);

		int gridSize = handler.getGridSize();
		if (gridSize == 1) {
			return; // Gridsize 1 would only make the whole screen grey
		}

		int width = 2000 + (int) getPreferredSize().getWidth();
		int height = 1000 + (int) getPreferredSize().getHeight();
		for (int i = gridSize; i < width; i += gridSize) {
			g2d.drawLine(i, 0, i, height);
		}
		for (int i = gridSize; i < height; i += gridSize) {
			g2d.drawLine(0, i, width, i);
		}
	}

	// private void drawDevHelpLines(Graphics2D g2d) {
	// g2d.setStroke(Utils.getStroke(LineType.DASHED, 1));
	//
	// g2d.setColor(Color.BLUE);
	// int w = handler.getDrawPanel().getScrollPane().getViewport().getViewPosition().x;
	// int h = handler.getDrawPanel().getScrollPane().getViewport().getViewPosition().y;
	// g2d.drawRect(w, h, w + 2, h + 2);
	//
	// g2d.setColor(Color.GRAY);
	// Dimension dim = getViewableDiagrampanelSize();
	// g2d.drawRect(0, 0, (int) dim.getWidth(), (int) dim.getHeight());
	//
	// g2d.setColor(Color.RED);
	// Dimension dim2 = getPreferredSize();
	// g2d.drawRect(0, 0, (int) dim2.getWidth(), (int) dim2.getHeight());
	//
	// g2d.setStroke(Utils.getStroke(LineType.SOLID, 1));
	// }

	@Override
	protected void paintChildren(Graphics g) {
		// check if layers have changed and update them
		for (GridElement ge : gridElements) {
			if (!ge.getLayer().equals(getLayer((JComponent) ge.getComponent()))) {
				setLayer((JComponent) ge.getComponent(), ge.getLayer());
			}
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHints(Utils.getUxRenderingQualityHigh(true));
		if (Config.getInstance().isShow_grid()) {
			drawGrid(g2d);
		}
		super.paintComponents(g);
	}

	/**
	 * AB: Returns a copy of the actual origin zoomed to 100%.
	 * Origin marks a point that tracks changes of the diagram panel size and can
	 * be used to regenerate the original position of entities at the time they have been cut/copied,
	 * etc...
	 *
	 * @return a point that marks the diagram origin at a zoom of 100%.
	 */

	public Point getOriginAtDefaultZoom() {
		Point originCopy = new Point(origin);
		originCopy.setLocation(
				origin.x * Constants.DEFAULTGRIDSIZE / handler.getGridSize(),
				origin.y * Constants.DEFAULTGRIDSIZE / handler.getGridSize());
		return originCopy;
	}

	/**
	 * AB: Returns a copy of the actual origin.
	 * Origin marks a point that tracks changes of the diagram panel size and can
	 * be used to regenerate the original position of entities at the time they have been cut/copied,
	 * etc...
	 *
	 * @return a point that marks the diagram origin.
	 */
	public Point getOrigin() {
		log.trace("Diagram origin: " + origin);
		return new Point(origin);
	}

	/**
	 * AB: Moves the origin around the given delta x and delta y
	 * This method is mainly used by updatePanelAndScrollBars() to keep track of the panels size changes.
	 */
	public void moveOrigin(int dx, int dy) {
		log.trace("Move origin to: " + origin);
		origin.translate(handler.realignToGrid(false, dx), handler.realignToGrid(false, dy));
	}

	/**
	 * AB: Zoom the origin from the old grid size to the new grid size
	 * this method is mainly used by the DiagramHandler classes setGridAndZoom method.
	 *
	 * @see DiagramHandler#setGridAndZoom(int)
	 * @param oldGridSize
	 *            the old grid size
	 * @param newGridSize
	 *            the new grid size
	 */
	public void zoomOrigin(int oldGridSize, int newGridSize) {
		log.trace("Zoom origin to: " + origin);
		origin.setLocation(origin.x * newGridSize / oldGridSize, origin.y * newGridSize / oldGridSize);
	}

	public void removeElement(GridElement gridElement) {
		gridElements.remove(gridElement);
		remove((Component) gridElement.getComponent());
	}

	public void addElement(GridElement gridElement) {
		gridElements.add(gridElement);
		add((Component) gridElement.getComponent(), gridElement.getLayer());
	}

	public void updateElements() {
		for (GridElement e : gridElements) {
			e.updateModelFromText();
		}
	}

	public GridElement getElementToComponent(Component component) {
		for (GridElement e : gridElements) {
			if (e.getComponent().equals(component)) {
				return e;
			}
		}
		return null;
	}

	public void scroll(int amount) {
		JScrollBar scrollBar = _scr.getVerticalScrollBar();
		int increment = scrollBar.getUnitIncrement();
		scrollBar.setValue(scrollBar.getValue() + amount * increment);
	}

	private DiagramNotification notification;

	public void setNotification(DiagramNotification newNotification) {
		if (notification != null) {
			remove(notification);
		}

		notification = newNotification;
		add(notification);

		repaint();
	}

}
