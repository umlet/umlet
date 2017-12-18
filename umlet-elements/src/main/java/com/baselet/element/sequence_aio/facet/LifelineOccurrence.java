package com.baselet.element.sequence_aio.facet;

import com.baselet.control.basics.Line1D;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.diagram.draw.DrawHandler;

public interface LifelineOccurrence {

	/**
	 * @param drawHandler
	 * @param topLeft the top left corner of the rectangle in which the occurrence can draw
	 * @param size of the rectangle in which the occurrence can draw.
	 * @return if the lifeline is not interrupted then null, otherwise the Point contains the y start and y end of the interrupted area
	 */
	public Line1D draw(DrawHandler drawHandler, PointDouble topLeft, PointDouble size);

	/**
	 * @param drawHandler
	 * @return the minimum width which is needed by this element
	 */
	public double getMinWidth(DrawHandler drawHandler);

	/**
	 * @param drawHandler
	 * @param size of the rectangle in which the occurrence can draw.
	 * @return the amount of additional y space needed to draw the LifelineOccurence
	 * (e.g. a long constraint which is wrapped into  multiple lines).
	 * If the return value is &lt;= 0 then the value is ignored.
	 */
	public double getAdditionalYHeight(DrawHandler drawHandler, PointDouble size);

}
