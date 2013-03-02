package com.umlet.element.experimental;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Constants.LineType;
import com.baselet.control.Utils;
import com.baselet.control.interfaces.ColorInterface;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.element.Dimension;
import com.baselet.element.GridElement;
import com.baselet.element.Group;
import com.baselet.element.StickingPolygon;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.facets.DefaultGlobalFacet.ElementStyleEnum;
import com.umlet.element.experimental.settings.facets.DefaultGlobalFacet.GlobalSetting;
import com.umlet.element.experimental.settings.facets.Facet;

public abstract class NewGridElement implements GridElement {

	protected final static Logger log = Logger.getLogger(Utils.getClassName());

	private boolean stickingBorderActive;

	protected BaseDrawHandler drawer; // this is the drawer for element specific stuff
	private BaseDrawHandler metaDrawer; // this is a separate drawer to draw stickingborder, selection-background etc.
	private DiagramHandler handler;

	protected boolean isSelected = false;

	private Group group = null;

	protected Properties properties;

	protected JComponent component = new JComponent() {
		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g) {
			drawer.setGraphics(g);
			metaDrawer.setGraphics(g);
			drawer.drawAll(isSelected);
			metaDrawer.drawAll();
		}

		/**
		 * Must be overwritten because Swing uses this method to tell if 2 elements are overlapping
		 * It's also used to determine which element gets selected if there are overlapping elements (the smallest one)
		 * IMPORTANT: on overlapping elements, contains is called for all elements until the first one returns true, then the others contain methods are not called
		 */
		@Override
		public boolean contains(Point p) {
			return Utils.contains(NewGridElement.this, p);
		}

		/**
		 * Must be overwritten because Swing sometimes uses this method instead of contains(Point)
		 */
		@Override
		public boolean contains(int x, int y) {
			return Utils.contains(NewGridElement.this, new Point(x, y));
		}

	};

	public void init(Rectangle bounds, String panelAttributes, String panelAttributesAdditional, DiagramHandler handler) {
		setBounds(bounds);
		drawer = new BaseDrawHandler();
		metaDrawer = new BaseDrawHandler();
		properties = new Properties(panelAttributes, panelAttributesAdditional, drawer);
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
		drawer.setHandler(handler);
		metaDrawer.setHandler(handler);
		updateModelFromText(); // must be updated here because the new handler could have a different zoom level
	}

	@Override
	public String getPanelAttributes() {
		return properties.getPanelAttributes();
	}

	@Override
	public boolean isSelected() {
		return isSelected;
	}

	@Override
	public void setPanelAttributes(String panelAttributes) {
		properties.setPanelAttributes(panelAttributes);
		this.updateModelFromText();
	}

	@Override
	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public GridElement CloneFromMe() {
		try {
			java.lang.Class<? extends GridElement> cx = this.getClass(); // get class of dynamic object
			NewGridElement c = (NewGridElement) cx.newInstance();
			c.init(getBounds(), properties.getPanelAttributes(), properties.getPanelAttributesAdditional(), handler);
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
		updateMetaDrawer();
		this.repaint();
	}

	@Override
	public void onSelected() {
		isSelected = true;
		updateMetaDrawer();
		this.repaint();
	}

	/**
	 * ugly workaround to avoid that the Resize().execute() call which calls setSize() on this model updates the model during the
	 * calculated model update from autoresize. Otherwise the drawer cache would get messed up (it gets cleaned up 2 times in a row and afterwards everything gets drawn 2 times).
	 * Best testcase is an autoresize element with a background. Write some text and everytime autresize triggers, the background is drawn twice.
	 */
	private boolean autoresizePossiblyInProgress = false;
	
	@Override
	public void updateModelFromText() {
		this.autoresizePossiblyInProgress = true;
		drawer.clearCache();
		drawer.resetStyle(); // must be set before actions which depend on the fontsize (otherwise a changed fontsize would be recognized too late)
		Integer oldLayer = getLayer();
		properties.initSettingsFromText(this);
		drawer.setSize(getRealSize()); // must be set after possible resizing due to AUTORESIZE
		updateMetaDrawer();
		updateConcreteModel();
		if (oldLayer != null && !oldLayer.equals(getLayer())) {
			handler.getDrawPanel().setLayer(this.getComponent(), getLayer());
		}
		this.autoresizePossiblyInProgress = false;
	}

	protected abstract void updateConcreteModel();

	private void updateMetaDrawer() {
		metaDrawer.clearCache();
		if (isSelected) { // draw blue rectangle around selected gridelements
			metaDrawer.setForegroundAlpha(Constants.ALPHA_FULL_TRANSPARENCY);
			metaDrawer.setBackground(ColorInterface.BLUE, Constants.ALPHA_NEARLY_FULL_TRANSPARENCY);
			metaDrawer.drawRectangle(0, 0, getRealSize().width, getRealSize().height);
			metaDrawer.resetColorSettings();
			if (Constants.show_stickingpolygon && !this.isPartOfGroup()) {
				drawStickingPolygon();
			}
		}
		metaDrawer.setSize(getRealSize());
	}

	@Override
	public void updateProperty(GlobalSetting key, String newValue) {
		properties.updateSetting(key, newValue);
		this.getHandler().getDrawPanel().getSelector().updateSelectorInformation(); // update the property panel to display changed attributes
		this.updateModelFromText();
		this.repaint();
	}

	@Override
	public String getSetting(GlobalSetting key) {
		return properties.getSetting(key);
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
	public void setAdditionalAttributes(String additional_attributes) {
		/*TODO: perhaps refactor the additionattributes stuff completely (why should it be stored as a string? only for easier saving in uxf?)*/
	}

	@Override
	public boolean isPartOfGroup() {
		if (this.group != null) return true;
		return false;
	}

	@Override
	public boolean isInRange(Point upperLeft, Dimension size) {
		Rectangle2D rect1 = new Rectangle2D.Double(upperLeft.getX(), upperLeft.getY(), size.getWidth(), size.getHeight());
		Rectangle2D rect2 = new Rectangle2D.Double(getLocation().x, getLocation().y, getDimension().width, getDimension().height);
		return (rect1.contains(rect2));
	}

	@Override
	public int getResizeArea(int x, int y) {
		int ret = 0;
		if ((x <= 5) && (x >= 0)) ret = Constants.RESIZE_LEFT;
		else if ((x <= this.getDimension().width) && (x >= this.getDimension().width - 5)) ret = Constants.RESIZE_RIGHT;

		if ((y <= 5) && (y >= 0)) ret = ret | Constants.RESIZE_TOP;
		else if ((y <= this.getDimension().height) && (y >= this.getDimension().height - 5)) ret = ret | Constants.RESIZE_BOTTOM;
		return ret;
	}

	@Override
	public int getPossibleResizeDirections() {
		if (properties.getElementStyle() == ElementStyleEnum.NORESIZE || properties.getElementStyle() == ElementStyleEnum.AUTORESIZE) {
			return Constants.RESIZE_NONE;
		}
		else return Constants.RESIZE_ALL;
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
		p.addPoint(x, y);
		p.addPoint(x + width, y);
		p.addPoint(x + width, y + height);
		p.addPoint(x, y + height, true);
		return p;
	}
	private final void drawStickingPolygon() {
		StickingPolygon poly;
		// The Java Implementations in the displaceDrawingByOnePixel list start at (1,1) to draw while any others start at (0,0)
		if (Utils.displaceDrawingByOnePixel()) poly = this.generateStickingBorder(1, 1, this.getRealSize().width - 1, this.getRealSize().height - 1);
		else poly = this.generateStickingBorder(0, 0, this.getRealSize().width - 1, this.getRealSize().height - 1);
		if (poly != null) {
			metaDrawer.setLineType(LineType.DASHED);
			metaDrawer.setForegroundColor(Constants.DEFAULT_SELECTED_COLOR);
			poly.draw(metaDrawer);
			metaDrawer.setLineType(LineType.SOLID);
			metaDrawer.resetColorSettings();
		}
	}

	@Override
	public void changeSize(int diffx, int diffy) {
		this.setSize(this.getDimension().width + diffx, this.getDimension().height + diffy);
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
		if (width != getDimension().width || height != getDimension().height) { // only change size if it is really different
			component.setSize(width, height);
			if (!this.autoresizePossiblyInProgress) {
				updateModelFromText();
			}
		}
	}

	@Override
	public Point getLocation() {
		return component.getLocation();
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
	public Dimension getDimension() {
		return new Dimension(component.getWidth(), component.getHeight());
	}

	@Override
	public void paint(Graphics g) {
		component.paint(g);
	}

	/**
	 * @see com.baselet.element.GridElement#getRealSize()
	 */
	@Override
	public Dimension getRealSize() {
		return new Dimension((int) (getDimension().width / handler.getZoomFactor()), (int) (getDimension().height / handler.getZoomFactor()));
	}

	@Override
	public JComponent getComponent() {
		return component;
	}

	public abstract Settings getSettings();

	@Override
	public List<AutocompletionText> getAutocompletionList() {
		List<AutocompletionText> returnList = new ArrayList<AutocompletionText>();
		for (Facet f : getSettings().getGlobalFacets()) {
			returnList.addAll(Arrays.asList(f.getAutocompletionStrings()));
		}
		for (Facet f : getSettings().getFacets()) {
			returnList.addAll(Arrays.asList(f.getAutocompletionStrings()));
		}
		return returnList;
	}
	
	@Override
	public Integer getLayer() {
		return properties.getLayer();
	}

}
