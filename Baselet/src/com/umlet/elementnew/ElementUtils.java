package com.umlet.elementnew;

import java.util.List;

import javax.swing.JComponent;

import com.baselet.control.Main;
import com.baselet.control.geom.Point;
import com.baselet.diagram.draw.Converter;
import com.baselet.element.interfaces.GridElement;
import com.umlet.element.Relation;

public class ElementUtils {

	/**
	 * Must be overwritten because Swing uses this method to tell if 2 elements are overlapping
	 * It's also used to determine which element gets selected if there are overlapping elements (the smallest one)
	 * IMPORTANT: on overlapping elements, contains is called for all elements until the first one returns true, then the others contain methods are not called
	 */
	public static boolean checkForOverlap(GridElement gridElement, Point p) {
		JComponent component = (JComponent) gridElement.getComponent();
		java.awt.Rectangle rectangle = component.getVisibleRect();
		Point absolute = new Point(gridElement.getRectangle().getX() + p.getX(), gridElement.getRectangle().getY() + p.getY());
		if (!rectangle.contains(p.x, p.y)) {
			return false;
		}

		List<GridElement> elements = Main.getHandlerForElement(gridElement).getDrawPanel().getGridElements();
		// Selector selector = drawPanel.getSelector();
		for (GridElement other : elements) {
			if (other == gridElement) {
				continue;
			}
			if (other.getLayer() < gridElement.getLayer())
			{
				continue; // elements with lower layer are ignored
			}

			JComponent otherComponent = (JComponent) other.getComponent();
			if (other.getLayer() > gridElement.getLayer()) { // elements with higher layer can "overwrite" contains-value of this
				// move point to coordinate system of other entity
				Point other_p = new Point(p.x + gridElement.getRectangle().x - other.getRectangle().x, p.y + gridElement.getRectangle().y - other.getRectangle().y);
				if (otherComponent.contains(Converter.convert(other_p))) {
					return false;
				}
			}

			java.awt.Rectangle other_rectangle = otherComponent.getVisibleRect();
			// move bounds to coordinate system of this component
			other_rectangle.x += other.getRectangle().x - gridElement.getRectangle().x;
			other_rectangle.y += other.getRectangle().y - gridElement.getRectangle().y;
			// when elements intersect, select the smaller element except if it is an old relation (because they have a larger rectangle than they use). NOTE: Old Relations are not checked because they do not properly implement isSelectableOn
			if (!(other instanceof Relation) && other.isSelectableOn(absolute) && rectangle.intersects(other_rectangle) && ElementUtils.smaller(other_rectangle, rectangle)) {
				return false;
			}
		}
		return true;
	}

	private static boolean smaller(java.awt.Rectangle a, java.awt.Rectangle b) {
		int areaA = a.getSize().height * a.getSize().width;
		int areaB = b.getSize().height * b.getSize().width;
		if (areaA < areaB) {
			return true;
		}
		return false;
	}

}
