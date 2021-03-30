package com.baselet.gwt.client.element;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.constants.SharedConstants;
import com.baselet.element.GridElementUtils;
import com.baselet.element.interfaces.GridElement;

import java.util.List;

public class GridElementZoomUtil {
	public static void zoomEntities(int fromFactor, int toFactor, List<GridElement> selectedEntities) {
		for (GridElement entity : selectedEntities) {
			Rectangle oldRect = entity.getRectangle();
			int newX = oldRect.getX() * toFactor / fromFactor;
			int newY = oldRect.getY() * toFactor / fromFactor;
			int newW = oldRect.getWidth() * toFactor / fromFactor;
			int newH = oldRect.getHeight() * toFactor / fromFactor;
			oldRect.setBounds((int) GridElementUtils.realignTo(newX, toFactor), (int) GridElementUtils.realignTo(newY, toFactor), (int) GridElementUtils.realignTo(newW, toFactor), (int) GridElementUtils.realignTo(newH, toFactor));
			entity.setRectangle(oldRect);

			double zoomFactor = toFactor / (double) SharedConstants.DEFAULT_GRID_SIZE;
			((DrawHandlerGwt) entity.getComponent().getDrawHandler()).setZoomFactor(zoomFactor);
			((DrawHandlerGwt) entity.getComponent().getMetaDrawHandler()).setZoomFactor(zoomFactor);

			entity.updateModelFromText();
		}
	}
}