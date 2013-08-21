package com.baselet.element;

import java.util.Collection;
import java.util.Set;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.Direction;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.DimensionDouble;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;
import com.umlet.element.experimental.Component;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.Stickable;

public interface GridElement extends HasPanelAttributes {

	void setRectangle(Rectangle bounds);

	void setGroup(GridElement object);

	void setLocationDifference(int diffx, int diffy);

	String getAdditionalAttributes();

	void setAdditionalAttributes(String additionalAttributes);

	void setLocation(int x, int y);

	void setSize(int width, int height);

	boolean isPartOfGroup();

	Set<Direction> getResizeArea(int x, int y);

	void setStickingBorderActive(boolean stickingBordersActive);

	boolean isStickingBorderActive();

	StickingPolygon generateStickingBorder(Rectangle rect);

	/**
	 * position of the element on the drawpanel.
	 * x and y: distance from the upper left corner of the drawpanel.
	 * width and height: size of the element.
	 * 
	 */
	Rectangle getRectangle();

	void repaint();

	void changeSize(int diffx, int diffy);

	/**
	 * @return size of the element as if the zoomlevel would be 100% (eg: if zoom is 80% and width is 80 it would be returned as 100)
	 */
	Dimension getRealSize();

	boolean isInRange(Rectangle rectangle);
	
	Component getComponent();

	void updateProperty(String key, String newValue);
	
	void updateModelFromText();

	String getSetting(String key);
	
	Integer getLayer();

	void handleAutoresize(DimensionDouble necessaryElementDimension, AlignHorizontal alignHorizontal);
	
	ElementId getId();
	
	void drag(Collection<Direction> resizeDirection, int diffX, int diffY, Point mousePosBeforeDrag, boolean isShiftKeyDown, boolean firstDrag, Collection<? extends Stickable> stickables);

	boolean isSelectableOn(Point point);

	void dragEnd();
}
