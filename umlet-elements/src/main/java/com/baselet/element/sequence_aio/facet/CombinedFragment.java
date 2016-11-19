package com.baselet.element.sequence_aio.facet;

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
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.TextSplitter;

/**
 * Draws a combined fragment.
 * The border starts at the top of the start tick and ends at the bottom of the end tick + half of the tick y padding.
 *
 */
public class CombinedFragment implements LifelineSpanningTickSpanningOccurrence, Container {

	/** how much space is between the header and the first constraint */
	private static final double HEADER_CONSTRAINT_PADDING = 5;
	private static final double CONSTRAINT_Y_PADDING = 2;
	private static final double COMBINED_FRAGMENT_HORIZONTAL_BORDER_PADDING = 7;
	private static final double COMBINED_FRAGMENT_VERTICAL_BORDER_PADDING = 7;

	private final Lifeline[] coveredLifelines;
	/** first tick which is in the combined fragment, contains the operator and the InteractionConstraint of the first operand */
	private final int startTick;
	private final String[] operatorLines;
	private final Deque<Operand> operands;

	/**
	 *
	 * @param coveredLifelines the lifelines which are covered by this combined fragment
	 * @param startTick
	 * @param operator can be multiple lines (but only \n is allowed)
	 */
	public CombinedFragment(Lifeline[] coveredLifelines, int startTick, String operator) {
		super();
		this.coveredLifelines = Arrays.copyOf(coveredLifelines, coveredLifelines.length);
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

	@Override
	public int getStartTick() {
		return startTick;
	}

	// public void setEndTick(int endTick) {
	// this.endTick = endTick;
	// }

	/**
	 * the last tick which lies in the combined fragment
	 */
	@Override
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
		if (operands.size() == 0 && this.startTick != startTick) {
			throw new IllegalArgumentException("The start tick of the first operand must be equal to the start tick of the combined fragment");
		}
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
		if (operands.size() == 0 && this.startTick != startTick) {
			throw new IllegalArgumentException("The start tick of the first operand must be equal to the start tick of the combined fragment");
		}
		operands.add(new Operand(startTick, endTick, text, constraintLifeline));
	}

	@Override
	public void draw(DrawHandler drawHandler, DrawingInfo drawingInfo) {
		// draw operand lines, the pentagon and the first operand
		drawOperandLines(drawHandler, drawingInfo);
		PointDouble[] rectangle = new PointDouble[] {
				new PointDouble(drawingInfo.getHorizontalStart(this),
						drawingInfo.getVerticalStart(this)),
				new PointDouble(drawingInfo.getHorizontalEnd(this),
						drawingInfo.getVerticalStart(this)),
				new PointDouble(drawingInfo.getHorizontalEnd(this),
						drawingInfo.getVerticalEnd(this)),
				new PointDouble(drawingInfo.getHorizontalStart(this),
						drawingInfo.getVerticalEnd(this)),
				null
		};
		rectangle[4] = rectangle[0];
		drawHandler.drawLines(rectangle);
		Collection<Line1D> slopeNotPermittedAreas = new ArrayList<Line1D>(coveredLifelines.length);
		for (Lifeline ll : coveredLifelines) {
			if (ll.getLifelineLeftPartWidth(startTick) > 0) {
				double llCenter = drawingInfo.getHDrawingInfo(ll).getHorizontalCenter();
				slopeNotPermittedAreas.add(new Line1D(llCenter - ll.getLifelineLeftPartWidth(startTick),
						llCenter + ll.getLifelineRightPartWidth(startTick)));
			}
		}
		PointDouble headerSize = PentagonDrawingHelper.draw(drawHandler, operatorLines,
				drawingInfo.getWidth(this), rectangle[0], slopeNotPermittedAreas);

		// add interruptions for all affected lifelines
		double endOfHeadX = drawingInfo.getHorizontalStart(this) + headerSize.x;
		for (Lifeline ll : coveredLifelines) {
			if (drawingInfo.getHDrawingInfo(ll).getHorizontalCenter() - ll.getLifelineLeftPartWidth(startTick) <= endOfHeadX) {
				drawingInfo.getDrawingInfo(ll).addInterruptedArea(new Line1D(rectangle[0].y, rectangle[0].y + headerSize.y));
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
			double constraintTopY = drawingInfo.getVerticalStart(this) + headerSize.y + HEADER_CONSTRAINT_PADDING;
			double constraintBottomY = drawingInfo.getVerticalEnd(startTick);

			double textHeight = TextSplitter.getSplitStringHeight(firstIntConst.textLines,
					drawingInfo.getHDrawingInfo(firstIntConst.affectedLifeline).getSymmetricWidth(startTick), drawHandler);
			textHeight += CONSTRAINT_Y_PADDING * 2;
			constraintTopY += (constraintBottomY - constraintTopY - textHeight) / 2;
			TextSplitter.drawText(drawHandler, firstIntConst.textLines,
					drawingInfo.getHDrawingInfo(firstIntConst.affectedLifeline).getSymmetricHorizontalStart(startTick),
					constraintTopY,
					drawingInfo.getHDrawingInfo(firstIntConst.affectedLifeline).getSymmetricWidth(startTick),
					textHeight, AlignHorizontal.CENTER, AlignVertical.CENTER);
			drawingInfo.getDrawingInfo(firstIntConst.affectedLifeline).addInterruptedArea(
					new Line1D(constraintTopY, constraintTopY + textHeight));
		}
	}

	private void drawOperandLines(DrawHandler drawHandler, DrawingInfo drawingInfo) {
		Iterator<Operand> operandIter = operands.iterator();
		if (operandIter.hasNext()) {
			operandIter.next(); // skip first operand
		}
		drawHandler.setLineType(LineType.DASHED);
		while (operandIter.hasNext()) {
			Operand op = operandIter.next();
			drawHandler.drawLine(drawingInfo.getHorizontalStart(this),
					drawingInfo.getVerticalStart(op.startTick) - drawingInfo.getTickVerticalPadding() / 2.0,
					drawingInfo.getHorizontalEnd(this),
					drawingInfo.getVerticalStart(op.startTick) - drawingInfo.getTickVerticalPadding() / 2.0);
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
			double constraintMinWidth = operands.getFirst().constraint.getMinWidth(drawHandler) * coveredLifelines.length + (coveredLifelines.length - 1) * lifelineHorizontalPadding;
			minWidth = Math.max(minWidth, constraintMinWidth);
		}
		return minWidth + getLastLifeline().getLifelineLeftPartWidth(startTick) + getLastLifeline().getLifelineRightPartWidth(startTick);
	}

	@Override
	public Map<Integer, Double> getEveryAdditionalYHeight(DrawHandler drawHandler, HorizontalDrawingInfo hInfo, double defaultTickHeight) {
		Map<Integer, Double> ret = new HashMap<Integer, Double>();

		// we only need to calculate the pentagon + the first operand/InteractionConstrain because
		// the separating lines are too thin to make a difference and the other operands are
		// handled as LifelineOccurrence
		double headerHeight = PentagonDrawingHelper.getHeight(drawHandler, operatorLines, hInfo.getWidth(this));
		headerHeight -= COMBINED_FRAGMENT_VERTICAL_BORDER_PADDING; // head draws into the padding
		if (operands.size() > 0 && operands.getFirst().constraint != null) {
			headerHeight += HEADER_CONSTRAINT_PADDING;
			headerHeight += TextSplitter.getSplitStringHeight(operands.getFirst().constraint.textLines,
					hInfo.getHDrawingInfo(operands.getFirst().constraint.affectedLifeline).getSymmetricWidth(startTick),
					drawHandler);
			headerHeight += CONSTRAINT_Y_PADDING * 2;
		}
		if (headerHeight > defaultTickHeight) {
			ret.put(startTick, headerHeight - defaultTickHeight);
		}
		return ret;
	}

	@Override
	public ContainerPadding getPaddingInformation() {
		return new ContainerPadding(this, COMBINED_FRAGMENT_HORIZONTAL_BORDER_PADDING,
				COMBINED_FRAGMENT_HORIZONTAL_BORDER_PADDING,
				COMBINED_FRAGMENT_VERTICAL_BORDER_PADDING,
				COMBINED_FRAGMENT_VERTICAL_BORDER_PADDING);
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
					double textHeight = TextSplitter.getSplitStringHeight(textLines, size.x, drawHandler);
					Line1D interruptedArea = new Line1D(topLeft.y + (size.y - textHeight) / 2 - CONSTRAINT_Y_PADDING,
							topLeft.y + (size.y - textHeight) / 2 + textHeight + CONSTRAINT_Y_PADDING);
					TextSplitter.drawText(drawHandler, textLines, topLeft.x, interruptedArea.getLow() + CONSTRAINT_Y_PADDING,
							size.x, textHeight, AlignHorizontal.CENTER, AlignVertical.CENTER);
					return interruptedArea;
				}
			}

			@Override
			public double getMinWidth(DrawHandler drawHandler) {
				return TextSplitter.getTextMinWidth(textLines, drawHandler);
			}

			@Override
			public double getAdditionalYHeight(DrawHandler drawHandler, PointDouble size) {
				if (isFirstOperand()) {
					// first operand is handled by the combined fragment
					return -1;
				}
				else {
					return TextSplitter.getSplitStringHeight(textLines, size.x, drawHandler) + CONSTRAINT_Y_PADDING * 2 - size.y;
				}
			}

		}
	}

}
