package com.baselet.element;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.DimensionDouble;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.ComponentInterface;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.settings.facets.DefaultGlobalFacet.GlobalSetting;

public interface GridElement {

	String getPanelAttributes();

	boolean isSelected();

	void setSelected(Boolean selected);

	void setPanelAttributes(String panelAttributes);

	void setRectangle(Rectangle bounds);

	void setGroup(GroupGridElement object);

	GridElement CloneFromMe();

	void setLocationDifference(int diffx, int diffy);

	GroupGridElement getGroup();

	String getAdditionalAttributes();

	void setAdditionalAttributes(String additionalAttributes);

	void setLocation(int x, int y);

	void setSize(int width, int height);

	boolean isPartOfGroup();

	Set<Direction> getResizeArea(int x, int y);

	void setStickingBorderActive(boolean stickingBordersActive);

	boolean isStickingBorderActive();

	StickingPolygon generateStickingBorder(int x, int y, int width, int height);

	/**
	 * position of the element on the drawpanel.
	 * x and y: distance from the upper left corner of the drawpanel.
	 * width and height: size of the element.
	 * 
	 */
	Rectangle getRectangle();

	void repaint();

	void changeSize(int diffx, int diffy);

	Dimension getZoomedSize();

	/**
	 * @return size of the element as if the zoomlevel would be 100% (eg: if zoom is 80% and width is 80 it would be returned as 100)
	 */
	Dimension getRealSize();

	boolean isInRange(Rectangle rectangle);
	
	ComponentInterface getComponent();

	void updateProperty(GlobalSetting key, String newValue);
	
	void updateModelFromText();

	List<AutocompletionText> getAutocompletionList();

	String getSetting(GlobalSetting key);
	
	Integer getLayer();

	void handleAutoresize(DimensionDouble necessaryElementDimension);
	
	ElementId getId();
	
	void drag(Collection<Direction> resizeDirection, int diffX, int diffY, Point mousePosBeforeDrag, boolean isShiftKeyDown, boolean firstDrag);

	boolean isSelectableOn(Point point);

	void dragEnd();
}
