package com.baselet.element;

import java.util.List;

import javax.swing.JComponent;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.basics.Converter;
import com.baselet.control.basics.geom.Point;
import com.baselet.diagram.DrawPanel;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.relation.Relation;

public class ElementUtils {

	/**
	 * Must be overwritten because Swing uses this method to tell if 2 elements are overlapping
	 * It's also used to determine which element gets selected if there are overlapping elements (the smallest one)
	 * IMPORTANT: on overlapping elements, contains is called for all elements until the first one returns true, then the others contain methods are not called
	 *
	 * In future this logic should be the same as in BaseletGWT.DrawPanel.getGridElementOnPosition() which is much simpler because its externally calculated
	 * (this migration is only possible if there are not GridElement Listeners, but instead only one Diagram Listener which delegates the event to the specific GridElement like in GWT)
	 */
	public static boolean checkForOverlap(GridElement gridElement, Point p) {
		JComponent component = (JComponent) gridElement.getComponent();
		java.awt.Rectangle rectangle = component.getVisibleRect();
		Point absolute = new Point(gridElement.getRectangle().getX() + p.getX(), gridElement.getRectangle().getY() + p.getY());
		if (!rectangle.contains(p.x, p.y)) {
			return false;
		}

		DrawPanel drawPanel = HandlerElementMap.getHandlerForElement(gridElement).getDrawPanel();
		List<GridElement> elements = drawPanel.getGridElements();
		Selector selector = drawPanel.getSelector();
		for (GridElement other : elements) {
			if (other == gridElement || other.getLayer() < gridElement.getLayer() || !other.isSelectableOn(absolute)) {
				continue; // ignore this element, elements with a lower layer and elements which are not selectable on the point
			}

			// issue #260: if a relation is the only selected element its selection should be retained
			if (gridElement instanceof Relation && other instanceof Relation) {
				if (selector.isSelectedOnly(gridElement)) {
					return true;
				}
				if (selector.isSelectedOnly(other)) {
					return false;
				}
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
			if (!(other instanceof com.baselet.element.old.element.Relation) && rectangle.intersects(other_rectangle) && firstSmallerThanSecond(other_rectangle, rectangle)) {
				return false;
			}
		}
		return true;
	}

	private static boolean firstSmallerThanSecond(java.awt.Rectangle first, java.awt.Rectangle second) {
		int areaFirst = first.getSize().height * first.getSize().width;
		int areaSecond = second.getSize().height * second.getSize().width;
		if (areaFirst < areaSecond) {
			return true;
		}
		return false;
	}

}
