package com.plotlet.element;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.baselet.control.Constants;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.objects.PlotGridDrawConfig;
import com.baselet.element.OldGridElement;
import com.baselet.shared.Matrix;
import com.plotlet.element.plotgrid.AbstractPlot;
import com.plotlet.element.plotgrid.BarPlot;
import com.plotlet.element.plotgrid.LinePlot;
import com.plotlet.element.plotgrid.PiePlot;
import com.plotlet.element.plotgrid.ScatterPlot;
import com.plotlet.parser.Parser;
import com.plotlet.parser.ParserException;
import com.plotlet.parser.ParserResult;
import com.plotlet.parser.PlotConstants;
import com.plotlet.parser.PlotState;



public class PlotGrid extends OldGridElement {
	private Matrix<List<AbstractPlot>> matrix;

	private static final long serialVersionUID = 1L;

	private Integer gridWidth;
	private Double minValue;
	private Double maxValue;

	@Override
	public void paintEntity(Graphics g) {
		try {
			matrix = new Matrix<List<AbstractPlot>>();
			ParserResult parserState = new Parser().parse(getPanelAttributes());
			log.debug(parserState.toString());
		
			gridWidth = Integer.parseInt(parserState.getPlotGridValue(PlotConstants.KEY_INT_GRID_WIDTH, PlotConstants.GRID_WIDTH_DEFAULT));			
		
			fillWithPlots(parserState.getPlotStateList(), g);

			drawPlots();

		} catch (ParserException e) {
//			log.error(null, e);
			BaseDrawHandler draw = new BaseDrawHandler(g, getHandler(), fgColor, bgColor, getSize(), isSelected);
			draw.setForegroundColor("red");
			draw.setBackgroundColor("white");
			draw.setBackgroundAlpha(Constants.ALPHA_NO_TRANSPARENCY);
			draw.drawRectangle(0, 0, getSize().width-1, getSize().height-1);
			draw.printCenter(e.getMessage(), getRealHeight()/2);
		}
	}

	private void fillWithPlots(ArrayList<PlotState> plotStateList, Graphics g) {
		setOverallMinMaxValue(plotStateList);
		for (PlotState plotState : plotStateList) {
			Integer xPos = plotState.getValueAsInt(PlotConstants.KEY_INT_X_POSITION, null);
			Integer yPos = plotState.getValueAsInt(PlotConstants.KEY_INT_Y_POSITION, null);
			// 1 is subtracted from the values because the user counts from 1 to x; java counts from 0 to x-1
			if (xPos != null) xPos -= 1;
			if (yPos != null) yPos -= 1;

			// case1: x and y are specified
			if (xPos != null && yPos != null) {
				setMatrixHeightMinimum(yPos);
				List<List<AbstractPlot>> xCoordinateList = matrix.row(yPos);
				setMinimumListSize(xPos, xCoordinateList);
				xCoordinateList.set(xPos, createPlots(g, plotState, xPos, yPos, "x and y are specified"));
			}
			// case2: only x is specified
			else if (xPos != null) {
				putPlotInFirstFreeVerticalSpaceOrAddPlot(xPos, g, plotState, "only x is specified -> space replaced");
			}
			// case3: only y is specified
			else if (yPos != null) {
				setMatrixHeightMinimum(yPos);
				List<List<AbstractPlot>> xCoordinateList = matrix.row(yPos);
				putPlotInFirstFreeHorizontalSpaceOrAddPlot(xCoordinateList, yPos, g, plotState, "only y specified -> ");
			}
			// case4: no coordinate is specified
			else {
				putPlotInFirstFreeMatrixSpace(g, plotState);
			}
		}
		gridWidth = matrix.cols(); // Recalculate grid width
		log.info("\n" + this.toString() + "\n");
	}

	private void setOverallMinMaxValue(List<PlotState> plotStateList) {
		minValue = Double.MAX_VALUE;
		maxValue = Double.MIN_VALUE;
		Double[][] data;
		for (PlotState state : plotStateList) {
			data = state.getDataSet().data();
			for (Double[] dArray : data) {
				for (Double d : dArray) {
					if (d > maxValue) maxValue = d;
					if (d < minValue) minValue = d;
				}
			}
		}
	}

	private void setMatrixHeightMinimum(Integer minHeight) {
		while (minHeight > matrix.rows()-1) {
			matrix.addLine(new ArrayList<List<AbstractPlot>>());
		}
	}

	private void setMinimumListSize(Integer minWidth, List<List<AbstractPlot>> lineToSet) {
		while (minWidth > lineToSet.size()-1) {
			lineToSet.add(null);
		}
	}
	
	private void putPlotInFirstFreeVerticalSpaceOrAddPlot(Integer xFix, Graphics g, PlotState plotState, String info) {
		boolean plotFilledInFreeSpace = false;
		for (int ySeq = 0; ySeq < matrix.rows(); ySeq++) {
			List<List<AbstractPlot>> xCoordinateList  = matrix.row(ySeq);
			if (xFix >= xCoordinateList.size()) setMinimumListSize(xFix, xCoordinateList);
			if (xCoordinateList.get(xFix) == null) {
				xCoordinateList.set(xFix, createPlots(g, plotState, xFix, ySeq, info));
				plotFilledInFreeSpace = true;
				break;
			}
		}
		// If there is no free space available for the plot, a new line must be added
		if (!plotFilledInFreeSpace) {
			ArrayList<List<AbstractPlot>> newColumn = new ArrayList<List<AbstractPlot>>();
			setMinimumListSize(xFix, newColumn);
			newColumn.set(xFix, createPlots(g, plotState, xFix, matrix.rows(), "only x is specified -> expanded y-list"));
			matrix.addLine(newColumn);
		}
	}

	private void putPlotInFirstFreeHorizontalSpaceOrAddPlot(List<List<AbstractPlot>> xCoordinateList, Integer yFix, Graphics g, PlotState plotState, String info) {
		for (int xSeq = 0; true; xSeq++) {
			if (xSeq == xCoordinateList.size()) {
				xCoordinateList.add(createPlots(g, plotState, xSeq, yFix, info + "added new x-entry"));
				return;
			}
			if (xCoordinateList.get(xSeq) == null) {
				xCoordinateList.set(xSeq, createPlots(g, plotState, xSeq, yFix, info + "replaced x-entry"));
				return;
			}
		}
	}

	private void putPlotInFirstFreeMatrixSpace(Graphics g, PlotState plotState) {
		// Go through all lines and all values in each line
		for (int ySeq = 0; ySeq < matrix.rows(); ySeq++) {
			List<List<AbstractPlot>> oneLine = matrix.row(ySeq);

			for (int xSeq = 0; xSeq < oneLine.size(); xSeq++) {
				List<AbstractPlot> oneValue = oneLine.get(xSeq);
				// If a free space is found use it
				if (oneValue == null) {
					oneLine.set(xSeq, createPlots(g, plotState, xSeq, ySeq, "no coordinate specified -> free space found"));
					return;
				}
			}
			// If the actual x-coordinates line is < than the default grid width add a new value
			if (oneLine.size() < gridWidth) {
				oneLine.add(createPlots(g, plotState, oneLine.size(), ySeq, "no coordinate specified -> expanded x-list"));
				return;
			}
		}
		// If every space in the matrix is occupied and the position is still not found add a new line and fill its first place
		List<List<AbstractPlot>> newLine = new ArrayList<List<AbstractPlot>>();
		newLine.add(createPlots(g, plotState, 0, matrix.rows(), "no coordinate specified -> every matrix space occupied, expanded y-list"));
		matrix.addLine(newLine);
	}

	private List<AbstractPlot> createPlots(Graphics g, PlotState plotState, Integer xPos, Integer yPos, String info)
	{
		List<AbstractPlot> plotList = new ArrayList<AbstractPlot>();
		
		//create and add base plot
		plotList.add(createPlot(g, plotState, xPos, yPos, info));
		
		//create and add sub plots
		for (PlotState subPlotState: plotState.getSubplots()) {
			plotList.add(createPlot(g, subPlotState, xPos, yPos, info));
		}
		
		return plotList;
	}	
	
	private AbstractPlot createPlot(Graphics g, PlotState plotState, int xPos, int yPos, String info) {
		String type = plotState.getValueValidated(PlotConstants.KEY_STRING_TYPE, PlotConstants.TYPE_BAR, PlotConstants.getValuesForKey(PlotConstants.KEY_STRING_TYPE));
		log.info("PlotGrid insert : " + type + " (" + xPos + ";" + yPos + ") " + info);
		PlotGridDrawConfig plotDrawConfig = new PlotGridDrawConfig(this.getHandler(), this.getRealSize(),  this.getSize(), this.getFgColor(), this.getBgColor(), this.isSelected, this.minValue, this.maxValue);
		if (PlotConstants.TYPE_PIE.equals(type)) return  new PiePlot(g,plotDrawConfig, plotState, xPos, yPos);
		else if (PlotConstants.TYPE_LINE.equals(type)) return  new LinePlot(g,plotDrawConfig, plotState, xPos, yPos);
		else if (PlotConstants.TYPE_SCATTER.equals(type)) return  new ScatterPlot(g,plotDrawConfig, plotState, xPos, yPos);
		else return new BarPlot(g,plotDrawConfig, plotState, xPos, yPos);
	}

	private Dimension getRealSize() {
		return new Dimension(getRealWidth(), getRealHeight());
	}
	public void drawPlots() {
		for (int row = 0; row < matrix.rows(); row++) {
			for (int col = 0; col < matrix.row(row).size(); col++) {
				List<AbstractPlot> oneCell = matrix.cell(row,col);
				for (AbstractPlot onePlot: oneCell)
				{
					if (onePlot != null) {
						if (col != onePlot.getXPosition()) log.error("Plot contains wrong coordinates: " + col + " != " + onePlot.getXPosition());
						if (row != onePlot.getYPosition()) log.error("Plot contains wrong coordinates: " + row + " != " + onePlot.getYPosition());
						onePlot.plot(matrix.cols(), matrix.rows());
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		String returnString = "------------------------------\n";
		for (int i = 0; i < matrix.rows(); i++) {
			List<List<AbstractPlot>> row = matrix.row(i);
			for (List<AbstractPlot> oneCell: row)
			{
				for (AbstractPlot onePlot : oneCell) {
					if (onePlot == null) returnString += "null" + "\t";
					else returnString += onePlot.getPlotLineNr() + "\t";
				}
			}
			returnString += "\n";
		}
		return returnString + "------------------------------";
	}

}
