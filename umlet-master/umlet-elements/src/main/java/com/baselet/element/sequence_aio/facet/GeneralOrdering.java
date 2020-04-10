package com.baselet.element.sequence_aio.facet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.baselet.control.basics.geom.Line;
import com.baselet.control.enums.LineType;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.relation.helper.RelationDrawer;
import com.baselet.element.relation.helper.RelationDrawer.ArrowEndType;

public class GeneralOrdering implements LifelineSpanningTickSpanningOccurrence {

	private final Lifeline[] coveredLifelines;
	private final OccurrenceSpecification earlierOccurrence;
	private final OccurrenceSpecification laterOccurrence;

	public GeneralOrdering(OccurrenceSpecification earlierOccurrence, OccurrenceSpecification laterOccurrence, Lifeline[] coveredLifelines) {
		if (coveredLifelines.length < 2) {
			throw new IllegalArgumentException("A GeneralOrdering must affect two different lifelines.");
		}
		this.coveredLifelines = Arrays.copyOf(coveredLifelines, coveredLifelines.length);
		this.earlierOccurrence = earlierOccurrence;
		this.laterOccurrence = laterOccurrence;
	}

	@Override
	public Lifeline getFirstLifeline() {
		return coveredLifelines[0];
	}

	@Override
	public Lifeline getLastLifeline() {
		return coveredLifelines[coveredLifelines.length - 1];
	}

	@Override
	public void draw(DrawHandler drawHandler, DrawingInfo drawingInfo) {
		LineType oldLt = drawHandler.getLineType();
		drawHandler.setLineType(LineType.DASHED);
		Line line = new Line(earlierOccurrence.getPosition(drawingInfo), laterOccurrence.getPosition(drawingInfo));
		drawHandler.drawLine(line);
		drawHandler.setLineType(oldLt);
		RelationDrawer.drawArrowToLine(line.getCenter(), drawHandler, line, false, ArrowEndType.NORMAL, false, false);
	}

	@Override
	public double getOverallMinWidth(DrawHandler drawHandler, double lifelineHorizontalPadding) {
		// only the size of the arrow is needed, which is multiplied by 3 to have some padding
		return RelationDrawer.ARROW_LENGTH * 3;
	}

	@Override
	public Map<Integer, Double> getEveryAdditionalYHeight(DrawHandler drawHandler, HorizontalDrawingInfo hInfo,
			double defaultTickHeight) {
		// since it is only a line with an arrow in the middle we assume that we don't need any further space
		return new HashMap<Integer, Double>();
	}

	@Override
	public ContainerPadding getPaddingInformation() {
		return null;
	}
}
