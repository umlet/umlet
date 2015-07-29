package com.baselet.element.facet.specific.sequence_aio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.baselet.control.basics.Line1D;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.control.enums.LineType;
import com.baselet.diagram.draw.AdvancedTextSplitter;
import com.baselet.diagram.draw.DrawHandler;

public class CombinedFragment implements LifelineSpanningTickSpanningOccurrence {

	/** how much space is between the header and the first constraint */
	private static final double HEADER_CONSTRAINT_PADDING = 5;
	private static final double CONSTRAINT_Y_PADDING = 2;
	private static final double COMBINED_FRAGMENT_BORDER_PADDING = 5;

	private final Lifeline[] coveredLifelines;
	/** first tick which is in the combined fragment, contains the operator and the InteractionConstraint of the first operand */
	private final int startTick;
	private final String[] operatorLines;
	private final Deque<Operand> operands;

	/**
	 *
	 * @param intervalStart
	 * @param intervalEnd
	 * @param startTick
	 * @param operator can be multiple lines (but only \n is allowed)
	 */
	public CombinedFragment(Lifeline[] coveredLifelines, int startTick, String operator) {
		super();
		this.coveredLifelines = coveredLifelines;
		this.startTick = startTick;
		operatorLines = operator.split("\n");
		operands = new LinkedList<CombinedFragment.Operand>();
	}

	@Override
	public Lifeline getFirstLifeline() {
		return coveredLifelines[0];
	}

	@Override
	public Lifeline getLastLifeline() {
		return coveredLifelines[coveredLifelines.length - 1];
	}

	// public void setEndTick(int endTick) {
	// this.endTick = endTick;
	// }

	/**
	 * the last tick which lies in the combined fragment
	 */
	public int getEndTick() {
		if (operands.isEmpty()) {
			return startTick;
		}
		return operands.getLast().endTick;
	}

	/**
	 * Adds an operand to the combined fragement.
	 * @param startTick
	 * @param endTick
	 */
	public void addOperand(int startTick, int endTick) {
		operands.add(new Operand(startTick, endTick));
	}

	/**
	 * Adds an operand with an interaction constraint to the combined fragment.
	 * @param startTick
	 * @param endTick
	 * @param text
	 * @param constraintLifeline
	 * @throws SequenceDiagramCheckedException  if the lifeline already contains an occurrence at the startTick or
	 * if the lifeline is not created on start and the startTick is prior in time to the create tick
	 */
	public void addOperand(int startTick, int endTick, String text, Lifeline constraintLifeline) throws SequenceDiagramCheckedException {
		operands.add(new Operand(startTick, endTick, text, constraintLifeline));
	}

	@Override
	public Map<Integer, Line1D[]> draw(DrawHandler drawHandler, double topY, Line1D[] lifelinesHorizontalSpanning,
			double tickHeight, double[] accumulativeAddiontalHeightOffsets) {
		// draw operand lines, the pentagon and the first operand
		Map<Integer, Line1D[]> ret = new HashMap<Integer, Line1D[]>();
		drawOperandLines(drawHandler, topY, lifelinesHorizontalSpanning, tickHeight, accumulativeAddiontalHeightOffsets);
		PointDouble[] rectangle = new PointDouble[] {
				new PointDouble(lifelinesHorizontalSpanning[getFirstLifeline().getIndex()].getLow(),
						topY + startTick * tickHeight + accumulativeAddiontalHeightOffsets[startTick]),
				new PointDouble(lifelinesHorizontalSpanning[getLastLifeline().getIndex()].getHigh(),
						topY + startTick * tickHeight + accumulativeAddiontalHeightOffsets[startTick]),
				new PointDouble(lifelinesHorizontalSpanning[getLastLifeline().getIndex()].getHigh(),
						topY + (getEndTick() + 1) * tickHeight + accumulativeAddiontalHeightOffsets[getEndTick() + 1]),
				new PointDouble(lifelinesHorizontalSpanning[getFirstLifeline().getIndex()].getLow(),
						topY + (getEndTick() + 1) * tickHeight + accumulativeAddiontalHeightOffsets[getEndTick() + 1]),
				null
		};
		rectangle[4] = rectangle[0];
		drawHandler.drawLines(rectangle);
		Collection<Line1D> slopeNotPermittedAreas = new ArrayList<Line1D>(coveredLifelines.length);
		for (Lifeline ll : coveredLifelines) {
			if (ll.getLifelineLeftPartWidth(startTick) > 0) {
				double llCenter = lifelinesHorizontalSpanning[ll.getIndex()].getCenter();
				slopeNotPermittedAreas.add(new Line1D(llCenter - ll.getLifelineLeftPartWidth(startTick),
						llCenter + ll.getLifelineRightPartWidth(startTick)));
			}
		}
		PointDouble headerSize = PentagonDrawingHelper.draw(drawHandler, operatorLines,
				lifelinesHorizontalSpanning[getLastLifeline().getIndex()].getHigh()
						- lifelinesHorizontalSpanning[getFirstLifeline().getIndex()].getLow(),
				rectangle[0], slopeNotPermittedAreas);

		// add interruptions for all affected lifelines
		for (Lifeline ll : coveredLifelines) {
			if (lifelinesHorizontalSpanning[ll.getIndex()].getCenter() - ll.getLifelineLeftPartWidth(startTick)
			<= lifelinesHorizontalSpanning[getFirstLifeline().getIndex()].getLow() + headerSize.x) {
				ret.put(ll.getIndex(), new Line1D[] { new Line1D(rectangle[0].y, rectangle[0].y + headerSize.y) });
			}
			else {
				// we traverse the lifelines from left to right, therfore after the first ll which is not covered we can stop
				break;
			}
		}
		// draw operand and add the interrupted area
		if (operands.size() > 0 && operands.getFirst().constraint != null) {
			Operand firstOperand = operands.getFirst();
			Operand.InteractionConstraint firstIntConst = firstOperand.constraint;
			double constraintTopY = topY + firstOperand.startTick * tickHeight
									+ accumulativeAddiontalHeightOffsets[firstOperand.startTick];
			double constraintBottomY = topY + (firstOperand.startTick + 1) * tickHeight
										+ accumulativeAddiontalHeightOffsets[firstOperand.startTick + 1];
			constraintTopY += headerSize.y + HEADER_CONSTRAINT_PADDING;
			double textHeight = AdvancedTextSplitter.getSplitStringHeight(firstIntConst.textLines,
					lifelinesHorizontalSpanning[firstIntConst.affectedLifeline.getIndex()].getSpace(), drawHandler)
								+ CONSTRAINT_Y_PADDING * 2;
			constraintTopY += (constraintBottomY - constraintTopY - textHeight) / 2;
			AdvancedTextSplitter.drawText(drawHandler, firstIntConst.textLines,
					lifelinesHorizontalSpanning[firstIntConst.affectedLifeline.getIndex()].getLow(),
					constraintTopY,
					lifelinesHorizontalSpanning[firstIntConst.affectedLifeline.getIndex()].getSpace(),
					textHeight, AlignHorizontal.CENTER, AlignVertical.CENTER);
			if (!ret.containsKey(firstIntConst.affectedLifeline.getIndex())) {
				ret.put(firstIntConst.affectedLifeline.getIndex(),
						new Line1D[] { new Line1D(constraintTopY, constraintTopY + textHeight) });
			}
			else {
				Line1D[] tmp = ret.get(firstIntConst.affectedLifeline.getIndex());
				tmp = Arrays.copyOfRange(tmp, 0, tmp.length + 1);
				tmp[tmp.length - 1] = new Line1D(constraintTopY, constraintTopY + textHeight);
				ret.put(firstIntConst.affectedLifeline.getIndex(), tmp);
			}
		}
		return ret;
	}

	private void drawOperandLines(DrawHandler drawHandler, double topY, Line1D[] lifelinesHorizontalSpanning,
			double tickHeight, double[] accumulativeAddiontalHeightOffsets) {
		Iterator<Operand> operandIter = operands.iterator();
		if (operandIter.hasNext()) {
			operandIter.next(); // skip first operand
		}
		drawHandler.setLineType(LineType.DASHED);
		while (operandIter.hasNext()) {
			Operand op = operandIter.next();
			drawHandler.drawLine(lifelinesHorizontalSpanning[getFirstLifeline().getIndex()].getLow(),
					topY + op.startTick * tickHeight + accumulativeAddiontalHeightOffsets[op.startTick],
					lifelinesHorizontalSpanning[getLastLifeline().getIndex()].getHigh(),
					topY + op.startTick * tickHeight + accumulativeAddiontalHeightOffsets[op.startTick]);
		}
		drawHandler.setLineType(LineType.SOLID);
	}

	@Override
	public double getOverallMinWidth(DrawHandler drawHandler, double lifelineHorizontalPadding) {
		// we only need to calculate the minimum width of the pentagon and the first operand,
		// other operands are handled as LifelineOccurrence. Add the width of the execution specification of the last
		// lifeline as buffere because in rare cases the slope could "jump" over it (to avoid that the slope is in the ExecSpec)
		// add the border padding so nested combined fragments look nice
		double minWidth = PentagonDrawingHelper.getMinimumWidth(drawHandler, operatorLines);
		if (operands.size() > 0 && operands.getFirst().constraint != null) {
			double constraintMinWidth = operands.getFirst().constraint.getMinWidth(drawHandler) * coveredLifelines.length
										+ (coveredLifelines.length - 1) * lifelineHorizontalPadding;
			minWidth = Math.max(minWidth, constraintMinWidth);
		}
		return minWidth
				+ getLastLifeline().getLifelineLeftPartWidth(startTick)
				+ getLastLifeline().getLifelineRightPartWidth(startTick);
	}

	@Override
	public Map<Integer, Double> getEveryAdditionalYHeight(DrawHandler drawHandler,
			Line1D[] lifelinesHorizontalSpanning, double tickHeight) {
		Map<Integer, Double> ret = new HashMap<Integer, Double>();
		// we only need to calculate the pentagon + the first operand/InteractionConstrain because
		// the separating lines are too thin to make a difference and the other operands are
		// handled as LifelineOccurrence
		double headerHeight = PentagonDrawingHelper.getHeight(drawHandler, operatorLines,
				lifelinesHorizontalSpanning[getLastLifeline().getIndex()].getHigh()
						- lifelinesHorizontalSpanning[getFirstLifeline().getIndex()].getLow());
		if (operands.size() > 0 && operands.getFirst().constraint != null) {
			headerHeight += HEADER_CONSTRAINT_PADDING;
			headerHeight += AdvancedTextSplitter.getSplitStringHeight(operands.getFirst().constraint.textLines,
					lifelinesHorizontalSpanning[operands.getFirst().constraint.affectedLifeline.getIndex()].getSpace(),
					drawHandler);
			headerHeight += CONSTRAINT_Y_PADDING * 2;
		}
		if (headerHeight > tickHeight) {
			ret.put(startTick, headerHeight - tickHeight);
		}
		return ret;
	}

	private class Operand {
		private final int startTick;
		private final int endTick;

		private final InteractionConstraint constraint;

		public Operand(int startTick, int endTick) {
			super();
			this.startTick = startTick;
			this.endTick = endTick;
			constraint = null;
		}

		/**
		 *
		 * @param startTick
		 * @param endTick
		 * @param text
		 * @param constraintLifeline
		 * @throws SequenceDiagramCheckedException if the lifeline already contains an occurrence at the startTick or
		 * if the lifeline is not created on start and the startTick is prior in time to the create tick
		 */
		public Operand(int startTick, int endTick, String text, Lifeline constraintLifeline) throws SequenceDiagramCheckedException {
			super();
			this.startTick = startTick;
			this.endTick = endTick;
			constraint = new InteractionConstraint(text, constraintLifeline);
		}

		public boolean isFirstOperand() {
			return operands.getFirst() == this;
		}

		public boolean isLastOperand() {
			return operands.getLast() == this;
		}

		/**
		 * Represents an interaction constraint of an operand of a combined fragment.
		 * The drawing and calculation of the height for the interaction constraint
		 * of the first operand is handled by the combined fragment because the
		 * positioning of it is tightly coupled with the operator drawing.
		 *
		 */
		private class InteractionConstraint implements LifelineOccurrence {

			private final String[] textLines;
			private final Lifeline affectedLifeline;

			/**
			 * @param constraintText the constraint without the square brackets
			 * @param affectedLifeline the lifeline on which the constraint should be placed
			 * @throws SequenceDiagramCheckedException if the lifeline already contains an occurrence at the {@link Operand#startTick} or
			 * if the lifeline is not created on start and the {@link Operand#startTick} is prior in time to the create tick
			 */
			public InteractionConstraint(String constraintText, Lifeline affectedLifeline) throws SequenceDiagramCheckedException {
				super();
				textLines = ('[' + constraintText + ']').split("\n");
				this.affectedLifeline = affectedLifeline;
				affectedLifeline.addLifelineOccurrenceAtTick(this, startTick);
			}

			@Override
			public Line1D draw(DrawHandler drawHandler, PointDouble topLeft, PointDouble size) {
				if (isFirstOperand()) {
					// first operand is handled by the combined fragment
					return null;
				}
				else {
					double textHeight = AdvancedTextSplitter.getSplitStringHeight(textLines, size.x, drawHandler);
					Line1D interruptedArea = new Line1D(topLeft.y + (size.y - textHeight) / 2 - CONSTRAINT_Y_PADDING,
							topLeft.y + (size.y - textHeight) / 2 + textHeight + CONSTRAINT_Y_PADDING);
					AdvancedTextSplitter.drawText(drawHandler, textLines, topLeft.x, interruptedArea.getLow() + CONSTRAINT_Y_PADDING,
							size.x, textHeight, AlignHorizontal.CENTER, AlignVertical.CENTER);
					return interruptedArea;
				}
			}

			@Override
			public double getMinWidth(DrawHandler drawHandler) {
				return AdvancedTextSplitter.getTextMinWidth(textLines, drawHandler);
			}

			@Override
			public double getAdditionalYHeight(DrawHandler drawHandler, PointDouble size) {
				if (isFirstOperand()) {
					// first operand is handled by the combined fragment
					return -1;
				}
				else {
					return AdvancedTextSplitter.getSplitStringHeight(textLines, size.x, drawHandler)
							+ CONSTRAINT_Y_PADDING * 2 - size.y;
				}
			}

		}
	}

}
