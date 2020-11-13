package com.baselet.gwt.client.view;

import java.util.ArrayList;
import java.util.List;

import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.enums.LineType;
import com.baselet.diagram.draw.helper.theme.Theme;
import com.baselet.diagram.draw.helper.theme.ThemeFactory;
import com.baselet.element.Selector;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.interfaces.HasGridElements;
import com.baselet.gwt.client.element.DrawHandlerGwt;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.CanvasElement;

public class SelectorNew extends Selector {

	private HasGridElements gridElementProvider;

	private boolean isLassoActive = false;

	private final Canvas canvas = Canvas.createIfSupported();
	private final DrawHandlerGwt drawer;
	private Point startPosition;
	private Rectangle rectangle;
	private int lassoWidth = 0;
	private int lassoHeight = 0;

	public SelectorNew(HasGridElements gridElementProvider) {
		this.gridElementProvider = gridElementProvider;
		this.drawer = new DrawHandlerGwt(new Context2dGwtWrapper(this.canvas.getContext2d()), 1d);
		this.drawer.setLineType(LineType.DASHED);
	}

	public void setGridElementProvider(HasGridElements gridElementProvider) {
		this.gridElementProvider = gridElementProvider;
	}

	private List<GridElement> selectedElements = new ArrayList<GridElement>();

	public GridElement getSingleSelected() {
		if (selectedElements.size() == 1) {
			return selectedElements.get(0);
		}
		else {
			return null;
		}
	}

	public void startSelection(Point startPosition) {
		this.deselectAll();
		this.startPosition = startPosition;
		this.isLassoActive = true;
		this.lassoWidth = 0;
		this.lassoHeight = 0;

		// Theme might have been changed
		this.drawer.setForegroundColor(ThemeFactory.getCurrentTheme().getColor(Theme.ColorStyle.DEFAULT_FOREGROUND));
		this.drawer.setBackgroundColor(ThemeFactory.getCurrentTheme().getColor(Theme.PredefinedColors.TRANSPARENT));
	}

	public void updateLasso(int diffX, int diffY) {
		this.drawer.clearCache();
		this.lassoWidth += diffX;
		this.lassoHeight += diffY;

		int absWidth = Math.abs(this.lassoWidth);
		int absHeight = Math.abs(this.lassoHeight);

		this.drawer.drawRectangle(0, 0, absWidth, absHeight);

		this.rectangle = this.getLassoRectangle();
	}

	public void drawLasso(Context2dWrapper context) {
		CanvasElement el = this.canvas.getCanvasElement();
		this.canvas.getContext2d().clearRect(0, 0, el.getWidth(), el.getHeight());
		this.canvas.getCanvasElement().setWidth((int) (this.rectangle.getWidth()) + 1); // canvas size is +1px to make sure a rectangle with width pixels is still visible (in Swing the bound-checking happens in BaseDrawHandlerSwing because you cannot extend the clipping area)
		this.canvas.getCanvasElement().setHeight((int) (this.rectangle.getHeight()) + 1);
		this.drawer.drawAll();
		context.drawImage(this.canvas.getCanvasElement(), this.rectangle.getX(), this.rectangle.getY());
	}

	public Rectangle getLassoRectangle() {
		// We need to always have positive widths and heights, therefore we move the start position once these values
		// become negative.
		int absWidth = Math.abs(this.lassoWidth);
		int absHeight = Math.abs(this.lassoHeight);
		Point correctedStartPosition = new Point(this.startPosition.getX(), this.startPosition.getY());
		if (this.lassoWidth < 0) {
			correctedStartPosition.setX(this.startPosition.getX() - absWidth);
		}
		if (this.lassoHeight < 0) {
			correctedStartPosition.setY(this.startPosition.getY() - absHeight);
		}
		return new Rectangle(correctedStartPosition.getX(), correctedStartPosition.getY(), absWidth, absHeight);
	}

	@Override
	public List<GridElement> getSelectedElements() {
		return selectedElements;
	}

	@Override
	public List<GridElement> getAllElements() {
		return gridElementProvider.getGridElements();
	}

	public boolean isLassoActive() {
		return this.isLassoActive;
	}

	public void selectElementsInsideLasso(List<GridElement> gridElements) {
		this.isLassoActive = false;
		List<GridElement> elementsToSelect = new ArrayList<>();
		Rectangle lassoRect = this.getLassoRectangle();
		for (GridElement gridElement : gridElements) {
			if (lassoRect.contains(gridElement.getRectangle())) {
				elementsToSelect.add(gridElement);
			}
		}
		this.select(elementsToSelect);
	}
}
