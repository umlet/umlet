package com.baselet.element.elementnew.plot;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.Matrix;
import com.baselet.control.basics.geom.Dimension;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.ElementId;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.element.NewGridElement;
import com.baselet.element.elementnew.plot.drawer.PlotGridDrawConfig;
import com.baselet.element.elementnew.plot.elements.AbstractPlot;
import com.baselet.element.elementnew.plot.elements.BarPlot;
import com.baselet.element.elementnew.plot.elements.LinePlot;
import com.baselet.element.elementnew.plot.elements.PiePlot;
import com.baselet.element.elementnew.plot.elements.ScatterPlot;
import com.baselet.element.elementnew.plot.parser.Parser;
import com.baselet.element.elementnew.plot.parser.ParserException;
import com.baselet.element.elementnew.plot.parser.ParserResult;
import com.baselet.element.elementnew.plot.parser.PlotConstants;
import com.baselet.element.elementnew.plot.parser.PlotConstants.PlotType;
import com.baselet.element.elementnew.plot.parser.PlotState;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;
import com.baselet.element.settings.SettingsManualresizeCenter;
import com.baselet.gui.AutocompletionText;

public class PlotGrid extends NewGridElement {

	private static final Logger log = LoggerFactory.getLogger(PlotGrid.class);

	private Matrix<List<AbstractPlot>> matrix;

	private Integer gridWidth;
	private Double minValue;
	private Double maxValue;

	/**
	 * this facet is only here to show autocompletion and include PlotGrid in the new parser logic which uses facets
	 */
	public static final Facet PSEUDO_PLOT_FACET = new Facet() {
		@Override
		public void handleLine(String line, PropertiesParserState propConfig) {
			// do nothing
		}

		@Override
		public List<AutocompletionText> getAutocompletionStrings() {
			return PlotConstants.AUTOCOMPLETION_LIST;
		}

		@Override
		public boolean checkStart(String line, PropertiesParserState propConfig) {
			return true;
		}
	};

	private void fillWithPlots(ParserResult parserState) {
		ArrayList<PlotState> plotStateList = parserState.getPlotStateList();
		DrawHandler drawer = parserState.getDrawer();
		setOverallMinMaxValue(plotStateList);
		for (PlotState plotState : plotStateList) {
			Integer xPos = plotState.getValueAsInt(PlotConstants.KEY_INT_X_POSITION, null);
			Integer yPos = plotState.getValueAsInt(PlotConstants.KEY_INT_Y_POSITION, null);
			// 1 is subtracted from the values because the user counts from 1 to x; java counts from 0 to x-1
			if (xPos != null) {
				xPos -= 1;
			}
			if (yPos != null) {
				yPos -= 1;
			}

			// case1: x and y are specified
			if (xPos != null && yPos != null) {
				setMatrixHeightMinimum(yPos);
				List<List<AbstractPlot>> xCoordinateList = matrix.row(yPos);
				setMinimumListSize(xPos, xCoordinateList);
				xCoordinateList.set(xPos, createPlots(drawer, plotState, xPos, yPos, "x and y are specified"));
			}
			// case2: only x is specified
			else if (xPos != null) {
				putPlotInFirstFreeVerticalSpaceOrAddPlot(drawer, xPos, plotState, "only x is specified -> space replaced");
			}
			// case3: only y is specified
			else if (yPos != null) {
				setMatrixHeightMinimum(yPos);
				List<List<AbstractPlot>> xCoordinateList = matrix.row(yPos);
				putPlotInFirstFreeHorizontalSpaceOrAddPlot(drawer, xCoordinateList, yPos, plotState, "only y specified -> ");
			}
			// case4: no coordinate is specified
			else {
				putPlotInFirstFreeMatrixSpace(drawer, plotState);
			}
		}
		gridWidth = matrix.cols(); // Recalculate grid width
		log.debug("\n" + toString() + "\n");
	}

	private void setOverallMinMaxValue(List<PlotState> plotStateList) {
		minValue = Double.MAX_VALUE;
		maxValue = Double.MIN_VALUE;
		Double[][] data;
		for (PlotState state : plotStateList) {
			data = state.getDataSet().data();
			for (Double[] dArray : data) {
				for (Double d : dArray) {
					if (d > maxValue) {
						maxValue = d;
					}
					if (d < minValue) {
						minValue = d;
					}
				}
			}
		}
	}

	private void setMatrixHeightMinimum(Integer minHeight) {
		while (minHeight > matrix.rows() - 1) {
			matrix.addLine(new ArrayList<List<AbstractPlot>>());
		}
	}

	private void setMinimumListSize(Integer minWidth, List<List<AbstractPlot>> lineToSet) {
		while (minWidth > lineToSet.size() - 1) {
			lineToSet.add(null);
		}
	}

	private void putPlotInFirstFreeVerticalSpaceOrAddPlot(DrawHandler drawer, Integer xFix, PlotState plotState, String info) {
		boolean plotFilledInFreeSpace = false;
		for (int ySeq = 0; ySeq < matrix.rows(); ySeq++) {
			List<List<AbstractPlot>> xCoordinateList = matrix.row(ySeq);
			if (xFix >= xCoordinateList.size()) {
				setMinimumListSize(xFix, xCoordinateList);
			}
			if (xCoordinateList.get(xFix) == null) {
				xCoordinateList.set(xFix, createPlots(drawer, plotState, xFix, ySeq, info));
				plotFilledInFreeSpace = true;
				break;
			}
		}
		// If there is no free space available for the plot, a new line must be added
		if (!plotFilledInFreeSpace) {
			ArrayList<List<AbstractPlot>> newColumn = new ArrayList<List<AbstractPlot>>();
			setMinimumListSize(xFix, newColumn);
			newColumn.set(xFix, createPlots(drawer, plotState, xFix, matrix.rows(), "only x is specified -> expanded y-list"));
			matrix.addLine(newColumn);
		}
	}

	private void putPlotInFirstFreeHorizontalSpaceOrAddPlot(DrawHandler drawer, List<List<AbstractPlot>> xCoordinateList, Integer yFix, PlotState plotState, String info) {
		for (int xSeq = 0; true; xSeq++) {
			if (xSeq == xCoordinateList.size()) {
				xCoordinateList.add(createPlots(drawer, plotState, xSeq, yFix, info + "added new x-entry"));
				return;
			}
			if (xCoordinateList.get(xSeq) == null) {
				xCoordinateList.set(xSeq, createPlots(drawer, plotState, xSeq, yFix, info + "replaced x-entry"));
				return;
			}
		}
	}

	private void putPlotInFirstFreeMatrixSpace(DrawHandler drawer, PlotState plotState) {
		// Go through all lines and all values in each line
		for (int ySeq = 0; ySeq < matrix.rows(); ySeq++) {
			List<List<AbstractPlot>> oneLine = matrix.row(ySeq);

			for (int xSeq = 0; xSeq < oneLine.size(); xSeq++) {
				List<AbstractPlot> oneValue = oneLine.get(xSeq);
				// If a free space is found use it
				if (oneValue == null) {
					oneLine.set(xSeq, createPlots(drawer, plotState, xSeq, ySeq, "no coordinate specified -> free space found"));
					return;
				}
			}
			// If the actual x-coordinates line is < than the default grid width add a new value
			if (oneLine.size() < gridWidth) {
				oneLine.add(createPlots(drawer, plotState, oneLine.size(), ySeq, "no coordinate specified -> expanded x-list"));
				return;
			}
		}
		// If every space in the matrix is occupied and the position is still not found add a new line and fill its first place
		List<List<AbstractPlot>> newLine = new ArrayList<List<AbstractPlot>>();
		newLine.add(createPlots(drawer, plotState, 0, matrix.rows(), "no coordinate specified -> every matrix space occupied, expanded y-list"));
		matrix.addLine(newLine);
	}

	private List<AbstractPlot> createPlots(DrawHandler drawer, PlotState plotState, Integer xPos, Integer yPos, String info) {
		List<AbstractPlot> plotList = new ArrayList<AbstractPlot>();

		// create and add base plot
		plotList.add(createPlot(drawer, plotState, xPos, yPos, info));

		// create and add sub plots
		for (PlotState subPlotState : plotState.getSubplots()) {
			plotList.add(createPlot(drawer, subPlotState, xPos, yPos, info));
		}

		return plotList;
	}

	private AbstractPlot createPlot(DrawHandler drawer, PlotState plotState, int xPos, int yPos, String info) {
		String type = plotState.getValueValidated(PlotType.getKey(), PlotType.Bar.getValue(), PlotConstants.toStringList(PlotType.values()));
		log.debug("PlotGrid insert : " + type + " (" + xPos + ";" + yPos + ") " + info);
		PlotGridDrawConfig plotDrawConfig = new PlotGridDrawConfig(getRealSize(), new Dimension(getRectangle().width, getRectangle().height), minValue, maxValue);
		if (PlotType.Pie.getValue().equals(type)) {
			return new PiePlot(drawer, plotDrawConfig, plotState, xPos, yPos);
		}
		else if (PlotType.Line.getValue().equals(type)) {
			return new LinePlot(drawer, plotDrawConfig, plotState, xPos, yPos);
		}
		else if (PlotType.Scatter.getValue().equals(type)) {
			return new ScatterPlot(drawer, plotDrawConfig, plotState, xPos, yPos);
		}
		else {
			return new BarPlot(drawer, plotDrawConfig, plotState, xPos, yPos);
		}
	}

	public void drawPlots() {
		for (int row = 0; row < matrix.rows(); row++) {
			for (int col = 0; col < matrix.row(row).size(); col++) {
				List<AbstractPlot> oneCell = matrix.cell(row, col);
				for (AbstractPlot onePlot : oneCell) {
					if (onePlot != null) {
						if (col != onePlot.getXPosition()) {
							log.error("Plot contains wrong coordinates: " + col + " != " + onePlot.getXPosition());
						}
						if (row != onePlot.getYPosition()) {
							log.error("Plot contains wrong coordinates: " + row + " != " + onePlot.getYPosition());
						}
						onePlot.plot(matrix.cols(), matrix.rows());
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("------------------------------\n");
		for (int i = 0; i < matrix.rows(); i++) {
			List<List<AbstractPlot>> row = matrix.row(i);
			for (List<AbstractPlot> oneCell : row) {
				for (AbstractPlot onePlot : oneCell) {
					if (onePlot == null) {
						sb.append("null\t");
					}
					else {
						sb.append(onePlot.getPlotLineNr()).append("\t");
					}
				}
			}
			sb.append("\n");
		}
		return sb.append("------------------------------").toString();
	}

	@Override
	protected void drawCommonContent(PropertiesParserState state) {
		DrawHandler drawer = state.getDrawer();
		try {
			matrix = new Matrix<List<AbstractPlot>>();
			ParserResult parserState = new Parser().parse(getPanelAttributes());
			parserState.setDrawer(drawer);
			log.debug(parserState.toString());

			gridWidth = Integer.parseInt(parserState.getPlotGridValue(PlotConstants.KEY_INT_GRID_WIDTH, PlotConstants.GRID_WIDTH_DEFAULT));

			fillWithPlots(parserState);

			drawPlots();

		} catch (ParserException e) {
			drawer.setForegroundColor(ColorOwn.RED);
			drawer.setBackgroundColor(ColorOwn.WHITE);
			drawer.drawRectangle(0, 0, getRectangle().width - 1, getRectangle().height - 1);
			float x = getRectangle().getWidth() / 2.0f;
			drawer.print(e.getMessage(), x, getRealSize().height / 2.0, AlignHorizontal.CENTER);
		}
	}

	@Override
	protected Settings createSettings() {
		return new SettingsManualresizeCenter() {

			@Override
			protected List<Facet> createFacets() {
				return listOf(PSEUDO_PLOT_FACET); // no real facets should be used
			}
		};
	}

	@Override
	public ElementId getId() {
		return ElementId.PlotGrid;
	}

}
