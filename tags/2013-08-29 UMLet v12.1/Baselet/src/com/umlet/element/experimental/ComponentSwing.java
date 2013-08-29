package com.umlet.element.experimental;

import java.awt.Graphics;

import javax.swing.JComponent;

import com.baselet.control.Utils;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.swing.BaseDrawHandlerSwing;
import com.baselet.diagram.draw.swing.Converter;

public class ComponentSwing extends JComponent implements Component {
	private static final long serialVersionUID = 1L;
	
	private BaseDrawHandlerSwing drawer = new BaseDrawHandlerSwing();
	private BaseDrawHandlerSwing metaDrawer = new BaseDrawHandlerSwing();
	private NewGridElement gridElement;
	
	public ComponentSwing(NewGridElement gridElement) {
		this.gridElement = gridElement;
	}

	private boolean selected = false;
	public void setSelected(boolean selected) {
		this.selected = selected;
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		drawer.setGraphics(g);
		metaDrawer.setGraphics(g);
		// the selected state stored in GridElements is NOT the same as the selector holds, therefore it must be set explicitly through a setSelected() method.
		// TODO make sure the selector holds the correct state and a repaint is triggered, then the following line should work:
//		boolean selected = Main.getHandlerForElement(gridElement).getDrawPanel().getSelector().isSelected(gridElement);
		drawer.drawAll(selected);
		if (selected) {
			metaDrawer.drawAll();
		}
	}

	public void translateForExport() {
		drawer.setTranslate(true);
		metaDrawer.setTranslate(true);
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
		// do nothing special
	}

}
