package com.baselet.element.sequence_aio.facet;

import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.AlignHorizontal;

/**
 * Is used to refer to a specific point on the lifeline.
 *
 * From the UML 2.5 specification: "OccurrenceSpecifications are merely syntactic points at the ends of Messages
 * or at the beginning/end of an ExecutionSpecification."
 *
 * Primarily it is used to implement the GeneralOrdering, but it could also be used for the following elements of the
 * sequence diagram: DurationConstraint, DurationObservation, TimeConstraint, TimeObservation
 *
 * @see GeneralOrdering
 */
public interface OccurrenceSpecification {

	/**
	 * @return the lifeline on which this object lies
	 */
	Lifeline getLifeline();

	/**
	 * @return if the horizontal position on the lifeline is fixed (message) or is flexible (execution specification)
	 */
	boolean hasFixedPosition();

	/**
	 * If hasFixedPosition() returns false this method can return any value, CENTER would be a reasonable choice.
	 * @return the relative position on the lifeline
	 */
	AlignHorizontal getFixedPositionAlignment();

	/**
	 * @param hDrawingInfo
	 * @return the x value of the fixed position or the center position.
	 */
	double getHorizonatlPosition(HorizontalDrawingInfo hDrawingInfo);

	/**
	 * <b>If hasFixedPosition() returns true this may not return valid values!</b>
	 * @param hDrawingInfo
	 * @param left if the position on the left or right side of the execution specification should be returned
	 * @return the x value of the left or right position on the lifeline
	 */
	double getHorizontalPosition(HorizontalDrawingInfo hDrawingInfo, boolean left);

	/**
	 * @param drawingInfo
	 * @return the fixed position or the center position.
	 */
	PointDouble getPosition(DrawingInfo drawingInfo);

	/**
	 * <b>If hasFixedPosition() returns true this may not return valid values!</b>
	 * @param drawingInfo
	 * @param left if the position on the left or right side of the execution specification should be returned
	 * @return the left or right position on the lifeline
	 */
	PointDouble getPosition(DrawingInfo drawingInfo, boolean left);

}
