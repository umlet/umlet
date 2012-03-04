package com.baselet.element;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.baselet.diagram.DiagramHandler;

public interface GridElement {

	DiagramHandler getHandler();

	String getPanelAttributes();

	int getX(); // replace with getLocation().x

	int getY(); // replace with getLocation().y

	Rectangle getVisibleRect();

	boolean contains(Point other_p);

	boolean isSelected();

	void setPanelAttributes(String panelAttributes);

	void setBounds(Rectangle bounds);

	void setHandler(DiagramHandler handler);

	void setGroup(Group object);

	void addMouseListener(MouseListener entityListener);

	void addMouseMotionListener(MouseMotionListener entityListener);

	void removeMouseMotionListener(MouseMotionListener entityListener);

	void removeMouseListener(MouseListener entityListener);

	int getWidth();

	int getHeight();

	GridElement CloneFromMe();

	void changeLocation(int diffx, int diffy);

	void onDeselected();

	void onSelected();

	Group getGroup();

	int[] getCoordinates();

	String getAdditionalAttributes();

	void setBounds(int x, int y, int w, int h);

	void setAdditionalAttributes(String additional_attributes);

	Component getComponent();

	void setLocation(int realignTo, int realignTo2);

	void setSize(int realignTo, int realignTo2);

	boolean isPartOfGroup();

	boolean isInRange(Point upperLeft, Dimension size);

	int getResizeArea(int x, int y);

	int getPossibleResizeDirections();

	void setStickingBorderActive(boolean b);

	boolean isStickingBorderActive();

	StickingPolygon generateStickingBorder(int x, int y, int width, int height);

	void setManualResized();

	Point getLocation();

	Point getLocationOnScreen();

	Rectangle getBounds();

	void repaint();

	String getFGColorString();

	void changeSize(int i, int j);

	void setColor(String colorString, boolean isForegroundColor);
	
}
