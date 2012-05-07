package com.baselet.element;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import com.baselet.diagram.DiagramHandler;

public interface GridElement {

	DiagramHandler getHandler();

	String getPanelAttributes();

	Rectangle getVisibleRect();

	boolean contains(Point p);

	boolean isSelected();

	void setPanelAttributes(String panelAttributes);

	void setBounds(Rectangle bounds);

	void setHandlerAndInitListeners(DiagramHandler handler);

	void setGroup(Group object);

	void addMouseListener(MouseListener mouseListener);

	void addMouseMotionListener(MouseMotionListener mouseMotionListener);

	void removeMouseMotionListener(MouseMotionListener mouseMotionListener);

	void removeMouseListener(MouseListener mouseListener);

	GridElement CloneFromMe();

	void changeLocation(int diffx, int diffy);

	void onDeselected();

	void onSelected();

	Group getGroup();

	String getAdditionalAttributes();

	void setBounds(int x, int y, int width, int height);

	void setAdditionalAttributes(String additional_attributes);

	void setLocation(int x, int y);

	void setSize(int width, int height);

	boolean isPartOfGroup();

	int getResizeArea(int x, int y);

	int getPossibleResizeDirections();

	void setStickingBorderActive(boolean stickingBordersActive);

	boolean isStickingBorderActive();

	StickingPolygon generateStickingBorder(int x, int y, int width, int height);

	void setManualResized();

	Point getLocation();

	Point getLocationOnScreen();

	Rectangle getBounds();

	void repaint();

	String getFGColorString();

	void changeSize(int diffx, int diffy);

	void setColor(String colorString, boolean isForegroundColor);

	Dimension getSize();

	boolean isInRange(Point upperLeft, Dimension size);

	void paint(Graphics g);
	
	JComponent getComponent();
	
}
