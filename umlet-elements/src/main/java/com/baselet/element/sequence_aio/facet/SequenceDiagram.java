package com.baselet.element.sequence_aio.facet;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.TextSplitter;
import com.baselet.element.sequence_aio.facet.LifelineSpanningTickSpanningOccurrence.ContainerPadding;

public class SequenceDiagram {

	private static final double LIFELINE_X_PADDING = 40;
	private static final double LIFELINE_Y_PADDING = 20;
	private static final double LIFELINE_MIN_WIDTH = 100;
	private static final double DESCRIPTION_V_PADDING = 10;
	private static final double DESCRIPTION_H_PADDING = 10;

	private static final double TICK_HEIGHT = 20;
	private static final double TICK_Y_PADDING = 6;// should be odd and the nothing should draw at the center!

	private String[] titleLines;
	private String[] descLines;

	// options
	private Lifeline[] lifelines;

	private final Collection<LifelineSpanningTickSpanningOccurrence> spanningLifelineOccurrences;

	private int lastTick;

	public SequenceDiagram() {
		titleLines = new String[] { "" };
		descLines = new String[] { "" };
		lifelines = new Lifeline[0];
		spanningLifelineOccurrences = new LinkedList<LifelineSpanningTickSpanningOccurrence>();
	}

	/**
	 * @param title lines which are separated by a \n
	 */
	public void setTitle(String title) {
		titleLines = title.split("\n");
	}

	// /**
	// * @param titleLines an array of lines which build the title (each element must not contain a \n)
	// */
	// public void setTitle(String[] titleLines) {
	// this.titleLines = titleLines;
	// }

	/**
	 * @param text description lines which are separated by a \n
	 */
	public void setText(String text) {
		descLines = text.split("\n");
	}

	// /**
	// * @param textLines an array of lines which build the description (each element must not contain a \n)
	// */
	// public void setText(String[] textLines) {
	// descLines = textLines;
	// }

	public int getLastTick() {
		return lastTick;
	}

	public void setLastTick(int lastTick) {
		this.lastTick = lastTick;
	}

	/**
	 *
	 * @param headText
	 * @param headType
	 * @param createdOnStart if false the lifeline will be created by the first message sent to this lifeline
	 * @param execSpecFromStart
	 */
	public Lifeline addLiveline(String headText, Lifeline.LifelineHeadType headType, boolean createdOnStart, boolean execSpecFromStart) {
		lifelines = Arrays.copyOf(lifelines, lifelines.length + 1);
		lifelines[lifelines.length - 1] = new Lifeline(headText, lifelines.length - 1, headType, createdOnStart, execSpecFromStart);
		return lifelines[lifelines.length - 1];
	}

	public void addLifelineSpanningTickSpanningOccurrence(LifelineSpanningTickSpanningOccurrence occurrence) {
		spanningLifelineOccurrences.add(occurrence);
	}

	/**
	 *
	 * @return how many lifelines the diagram has
	 */
	public int getLifelineCount() {
		return lifelines.length;
	}

	public List<Lifeline> getLifelines() {
		return Arrays.asList(lifelines);
	}

	public Lifeline[] getLifelinesArray() {
		return Arrays.copyOf(lifelines, lifelines.length);
	}

	public DimensionDouble draw(DrawHandler drawHandler) {
		DoubleConverter identity = new DoubleConverter() {
			@Override
			public double convert(double value) {
				return value;
			}
		};
		return draw(drawHandler, identity, identity);
	}

	public DimensionDouble draw(DrawHandler drawHandler, DoubleConverter widthConverter, DoubleConverter heightConverter) {
		HorizontalDrawingInfo horizontalDrawingInfo;
		VerticalDrawingInfo verticalInfo;
		DrawingInfo drawingInfo;
		// calculate the minimum width of the lifelines and the diagram; get all paddings and create the horizontal drawing info
		double lifelineWidth = Math.max(getLifelineWidth(drawHandler), LIFELINE_MIN_WIDTH);
		double diagramMinWidth = Math.max(LIFELINE_MIN_WIDTH,
				TextSplitter.getTextMinWidth(descLines, drawHandler) + DESCRIPTION_H_PADDING * 2);
		diagramMinWidth = Math.max(diagramMinWidth, PentagonDrawingHelper.getMinimumWidth(drawHandler, titleLines));
		Collection<ContainerPadding> allPaddings = new LinkedList<ContainerPadding>();
		for (LifelineSpanningTickSpanningOccurrence lstso : spanningLifelineOccurrences) {
			if (lstso.getPaddingInformation() != null) {
				allPaddings.add(lstso.getPaddingInformation());
			}
		}
		horizontalDrawingInfo = new HorizontalDrawingInfoImpl(0, diagramMinWidth, widthConverter, lifelineWidth,
				LIFELINE_X_PADDING, lifelines.length, lastTick, allPaddings);
		double diagramWidth = horizontalDrawingInfo.getDiagramWidth();

		// calculate and draw the header, then draw top border
		double headerHeight = 0;
		if (titleLines.length > 1 || titleLines.length == 1 && !titleLines[0].isEmpty()) {
			headerHeight = PentagonDrawingHelper.draw(drawHandler, titleLines, diagramWidth, new PointDouble(0, 0)).y;
		}
		drawHandler.drawLine(0, 0, diagramWidth, 0);

		// draw description
		double descHeight = TextSplitter.getSplitStringHeight(descLines, diagramWidth - DESCRIPTION_H_PADDING * 2, drawHandler);
		TextSplitter.drawText(drawHandler, descLines, DESCRIPTION_H_PADDING, headerHeight + DESCRIPTION_V_PADDING,
				diagramWidth - DESCRIPTION_H_PADDING * 2, descHeight, AlignHorizontal.LEFT, AlignVertical.CENTER);

		double lifelineHeadTop = headerHeight + descHeight + DESCRIPTION_H_PADDING * 2 + LIFELINE_Y_PADDING;
		double lifelineHeadHeight = getLifelineHeadHeight(drawHandler, horizontalDrawingInfo);
		verticalInfo = new VerticalDrawingInfoImpl(lifelineHeadTop, lifelineHeadHeight, TICK_HEIGHT, TICK_Y_PADDING,
				calculateAddiontalHeights(drawHandler, horizontalDrawingInfo), allPaddings);
		drawingInfo = new DrawingInfoImpl(horizontalDrawingInfo, verticalInfo, getLifelineCount());

		// first draw the occurrences which affect more than one lifeline which stores the interrupted areas in the
		// corresponding LifelineDrawingInfo. This info is then passed to the lifeline so it can be drawn
		if (lifelines.length > 0) {
			for (LifelineSpanningTickSpanningOccurrence llstso : spanningLifelineOccurrences) {
				llstso.draw(drawHandler, drawingInfo);
			}
			for (Lifeline ll : lifelines) {
				ll.draw(drawHandler, drawingInfo.getDrawingInfo(ll), lastTick);
			}
		}
		// draw left,right and bottom border
		double bottomY = heightConverter.convert(verticalInfo.getVerticalEnd(lastTick) + LIFELINE_Y_PADDING);
		drawHandler.drawLine(0, bottomY, diagramWidth, bottomY);
		drawHandler.drawLine(0, 0, 0, bottomY);
		drawHandler.drawLine(diagramWidth, 0, diagramWidth, bottomY);
		return new DimensionDouble(diagramWidth, bottomY);
	}

	private double getLifelineWidth(DrawHandler drawHandler) {
		double maxMinWidth = 0;
		for (Lifeline ll : lifelines) {
			maxMinWidth = Math.max(maxMinWidth, ll.getMinWidth(drawHandler));
		}
		for (LifelineSpanningTickSpanningOccurrence llstso : spanningLifelineOccurrences) {
			int llCount = llstso.getLastLifeline().getIndex() - llstso.getFirstLifeline().getIndex() + 1;
			maxMinWidth = Math.max(maxMinWidth,
					(llstso.getOverallMinWidth(drawHandler, LIFELINE_X_PADDING) - LIFELINE_X_PADDING * (llCount - 1)) / llCount);
		}
		return maxMinWidth;
	}

	private double[] calculateAddiontalHeights(DrawHandler drawHandler, HorizontalDrawingInfo hDrawingInfo) {
		double[] addiontalHeight = new double[lastTick + 1];
		for (Lifeline ll : lifelines) {
			for (Map.Entry<Integer, Double> e : ll.getAdditionalYHeights(drawHandler, hDrawingInfo.getHDrawingInfo(ll),
					TICK_HEIGHT).entrySet()) {
				addiontalHeight[e.getKey()] = Math.max(addiontalHeight[e.getKey()], e.getValue());
			}
		}
		for (LifelineSpanningTickSpanningOccurrence llstso : spanningLifelineOccurrences) {
			for (Map.Entry<Integer, Double> e : llstso.getEveryAdditionalYHeight(drawHandler, hDrawingInfo,
					TICK_HEIGHT).entrySet()) {
				addiontalHeight[e.getKey()] = Math.max(addiontalHeight[e.getKey()], e.getValue());
			}
		}
		return addiontalHeight;
	}

	private double getLifelineHeadHeight(DrawHandler drawHandler, HorizontalDrawingInfo hDrawingInfo) {
		double maxHeight = 0;
		for (Lifeline ll : lifelines) {
			if (ll.isCreatedOnStart()) {
				maxHeight = Math.max(maxHeight, ll.getHeadMinHeight(drawHandler, hDrawingInfo.getHDrawingInfo(ll).getWidth()));
			}
		}
		return maxHeight;
	}

	public static interface DoubleConverter {
		public double convert(double value);
	}
}
