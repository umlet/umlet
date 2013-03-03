package com.umlet.element.experimental;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.LineType;
import com.baselet.control.interfaces.ColorInterface;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.BaseDrawHandlerSwing;
import com.baselet.element.Converter;
import com.baselet.element.Dimension;
import com.baselet.element.GridElement;
import com.baselet.element.Group;
import com.baselet.element.Rectangle;
import com.baselet.element.StickingPolygon;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.facets.DefaultGlobalFacet.ElementStyleEnum;
import com.umlet.element.experimental.settings.facets.DefaultGlobalFacet.GlobalSetting;
import com.umlet.element.experimental.settings.facets.Facet;

public abstract class NewGridElement implements GridElement {
	
	private static final Logger log = Logger.getLogger(NewGridElement.class);

	private boolean stickingBorderActive;

	protected BaseDrawHandler drawer; // this is the drawer for element specific stuff
	private BaseDrawHandler metaDrawer; // this is a separate drawer to draw stickingborder, selection-background etc.
	private DiagramHandler handler;

	protected boolean isSelected = false;

	private Group group = null;

	protected Properties properties;

	protected JComponent component;

	public void init(Rectangle bounds, String panelAttributes, String panelAttributesAdditional, DiagramHandler handler) {
		drawer = new BaseDrawHandlerSwing();
		metaDrawer = new BaseDrawHandlerSwing();
		component = new NewGridElementJComponent(drawer, metaDrawer, this);
		setRectangle(bounds);
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
			this.getComponent().removeMouseListener(this.getHandler().getEntityListener(this));
			this.getComponent().removeMouseMotionListener(this.getHandler().getEntityListener(this));
		}
		this.handler = handler;
		getComponent().addMouseListener(this.getHandler().getEntityListener(this));
		getComponent().addMouseMotionListener(this.getHandler().getEntityListener(this));
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
			c.init(getRectangle(), properties.getPanelAttributes(), properties.getPanelAttributesAdditional(), handler);
			return c;
		} catch (Exception e) {
			log.error("Error at calling CloneFromMe() on entity", e);
		}
		return null;
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
	public boolean isInRange(Rectangle rect1) {
		return (rect1.contains(getRectangle()));
	}

	@Override
	public int getResizeArea(int x, int y) {
		int ret = 0;
		if ((x <= 5) && (x >= 0)) ret = Constants.RESIZE_LEFT;
		else if ((x <= this.getZoomedSize().width) && (x >= this.getZoomedSize().width - 5)) ret = Constants.RESIZE_RIGHT;

		if ((y <= 5) && (y >= 0)) ret = ret | Constants.RESIZE_TOP;
		else if ((y <= this.getZoomedSize().height) && (y >= this.getZoomedSize().height - 5)) ret = ret | Constants.RESIZE_BOTTOM;
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
		this.setSize(this.getZoomedSize().width + diffx, this.getZoomedSize().height + diffy);
	}

	@Override
	public Rectangle getVisibleRectangle() {
		return Converter.convert(component.getVisibleRect());
	}

	@Override
	public void setRectangle(Rectangle bounds) {
		component.setBounds(Converter.convert(bounds));
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		component.setBounds(x, y, width, height);
	}

	@Override
	public void setLocationDifference(int diffx, int diffy) {
		this.setLocation(this.getRectangle().x + diffx, this.getRectangle().y + diffy);
	}

	@Override
	public void setLocation(int x, int y) {
		component.setLocation(x, y);
	}

	@Override
	public void setSize(int width, int height) {
		if (width != getZoomedSize().width || height != getZoomedSize().height) { // only change size if it is really different
			component.setSize(width, height);
			if (!this.autoresizePossiblyInProgress) {
				updateModelFromText();
			}
		}
	}

	@Override
	public Rectangle getRectangle() {
		return Converter.convert(component.getBounds());
	}

	@Override
	public void repaint() {
		component.repaint();
	}

	@Override
	public Dimension getZoomedSize() {
		return new Dimension(component.getWidth(), component.getHeight());
	}

	/**
	 * @see com.baselet.element.GridElement#getRealSize()
	 */
	@Override
	public Dimension getRealSize() {
		return new Dimension((int) (getZoomedSize().width / handler.getZoomFactor()), (int) (getZoomedSize().height / handler.getZoomFactor()));
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
