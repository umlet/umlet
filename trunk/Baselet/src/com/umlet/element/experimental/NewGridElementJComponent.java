package com.umlet.element.experimental;

import java.awt.Graphics;

import javax.swing.JComponent;

import com.baselet.control.Utils;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.swing.BaseDrawHandlerSwing;
import com.baselet.diagram.draw.swing.Converter;

public class NewGridElementJComponent extends JComponent implements ComponentInterface {
	private static final long serialVersionUID = 1L;
	
	private BaseDrawHandlerSwing drawer = new BaseDrawHandlerSwing();
	private BaseDrawHandlerSwing metaDrawer = new BaseDrawHandlerSwing();
	private NewGridElement gridElement;
	
	public NewGridElementJComponent(NewGridElement gridElement) {
		this.gridElement = gridElement;
	}

	@Override
	public void paint(Graphics g) {
		drawer.setGraphics(g);
		metaDrawer.setGraphics(g);
		drawer.drawAll(gridElement.isSelected);
		metaDrawer.drawAll();
	}

	@Override
	public BaseDrawHandler getDrawHandler() {
		return drawer;
	}

	@Override
	public BaseDrawHandler getMetaDrawHandler() {
		return metaDrawer;
	}

	/**
	 * Must be overwritten because Swing uses this method to tell if 2 elements are overlapping
	 * It's also used to determine which element gets selected if there are overlapping elements (the smallest one)
	 * IMPORTANT: on overlapping elements, contains is called for all elements until the first one returns true, then the others contain methods are not called
	 */
	@Override
	public boolean contains(java.awt.Point p) {
		return this.contains(p.x, p.y);
	}

	/**
	 * Must be overwritten because Swing sometimes uses this method instead of contains(Point)
	 */
	@Override
	public boolean contains(int x, int y) {
		return Utils.contains(gridElement, new Point(x, y));
	}

	@Override
	public Rectangle getBoundsRect() {
		return Converter.convert(getBounds());
	}

	@Override
	public void repaintComponent() {
		this.repaint();
	}

	@Override
	public void setBoundsRect(Rectangle rect) {
		this.setBounds(rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	public void afterModelUpdate() {
		/*do nothing*/
	}

}
