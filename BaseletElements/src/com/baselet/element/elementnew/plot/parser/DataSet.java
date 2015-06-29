package com.baselet.element.elementnew.plot.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.control.Matrix;

public class DataSet {
	private String id;
	private Integer nr;
	private int lineNr;
	private boolean isInverted;

	private List<String> titleRow = null;
	private List<String> titleCol = null;

	private Matrix<Double> valueMatrix;
	private Matrix<String> analyseMatrix;

	public static final Double VALUE_DEFAULT = 0.0; // used for invalid value fields or missing fields

	protected DataSet(String id, int nr, int lineNr) {
		this.id = id;
		this.nr = nr;
		this.lineNr = lineNr;
		analyseMatrix = new Matrix<String>();
	}

	public String getId() {
		return id;
	}

	public Integer getNr() {
		return nr;
	}

	public int getLineNr() {
		return lineNr;
	}

	/**
	 * @return the row size
	 */
	public int rows() {
		return valueMatrix.rows();
	}

	/**
	 * @return the column size
	 */
	public int cols() {
		return valueMatrix.cols();
	}

	public boolean isEmpty() {
		return valueMatrix.isEmpty();
	}

	/**
	 * @param index
	 *            the index of the row
	 * @return a Double[] containing the cells of the row
	 */
	public Double[] row(int index) {
		List<Double> list = valueMatrix.row(index);
		return list.toArray(new Double[list.size()]);
	}

	public Double[][] data() {
		if (valueMatrix.isEmpty()) {
			throw new ParserException("The dataset (line: " + getLineNr() + ") has no values");
		}
		Double[][] returnArray = new Double[rows()][];
		for (int i = 0; i < rows(); i++) {
			returnArray[i] = row(i);
		}
		return returnArray;
	}

	/**
	 * Changed the manual inversion of the dataset. The dataset must only be inverted if the value has changed because
	 * it is only referenced from plots, therefore the last inversion will be dragged to further plot-calls without a problem
	 */
	public void setInvert(boolean shouldBeInverted) {
		if (isInverted == !shouldBeInverted) {
			analyseMatrix.invert();
			separateTitleRowColFromContent();
			isInverted = shouldBeInverted;
		}
	}

	public String[] titleRow() {
		return titleRow.toArray(new String[titleRow.size()]);
	}

	public String[] titleCol() {
		return titleCol.toArray(new String[titleCol.size()]);
	}

	@Override
	public String toString() {
		return "Dataset (" + id + ")\n" + analyseMatrix;
	}

	protected void addLine(String[] line) {
		analyseMatrix.addLine(new ArrayList<String>(Arrays.asList(line)));
	}

	protected void analyseMatrix() {
		separateTitleRowColFromContent();
		// If the valuematrix has more rows than cols the analyseMatrix must be inverted and analysed again
		// if (!valueMatrix.isEmpty() && valueMatrix.hasMoreRowsThanCols()) analyseMatrix.invert();
		// separateTitleRowColFromContent();
	}

	private void separateTitleRowColFromContent() {
		if (analyseMatrix.isEmpty()) {
			throw new ParserException("The dataset (line: " + getLineNr() + ") has no content");
		}
		List<String> firstRow = analyseMatrix.row(0);
		List<String> firstCol = analyseMatrix.col(0);
		boolean hasTitleRow = isTitleLine(firstRow);
		boolean hasTitleCol = isTitleLine(firstCol);

		if (hasTitleRow && hasTitleCol) {
			if (!firstRow.get(0).isEmpty() || !firstCol.get(0).isEmpty()) {
				throw new ParserException("If a dataset has a title row and column, the upper left space must be empty");
			}
			titleRow = firstRow.subList(1, firstRow.size()); // ignore first cell
			titleCol = firstCol.subList(1, firstCol.size()); // ignore first cell
		}
		else if (hasTitleRow && !hasTitleCol) {
			titleRow = firstRow;
			titleCol = createEmptyList(firstCol.size() - 1);

		}
		else if (!hasTitleRow && hasTitleCol) {
			titleRow = createEmptyList(firstRow.size() - 1);
			titleCol = firstCol;
		}
		else /* if (!hasTitleRow && !hasTitleCol) */ {
			titleRow = createEmptyList(firstRow.size());
			titleCol = createEmptyList(firstCol.size());
		}

		valueMatrix = new Matrix<Double>();
		for (int r = hasTitleRow ? 1 : 0; r < analyseMatrix.rows(); r++) {
			List<String> row = analyseMatrix.row(r);
			List<Double> rowDouble = new ArrayList<Double>();
			for (int c = hasTitleCol ? 1 : 0; c < row.size(); c++) {
				String val = row.get(c);
				try {
					if (val == null) {
						throw new NumberFormatException();
					}
					else {
						rowDouble.add(Double.parseDouble(val));
					}
				} catch (NumberFormatException ex) {
					throw new ParserException("The Dataset (line: " + getLineNr() + ") contains invalid values");
				}
			}
			valueMatrix.addLine(rowDouble);
		}
	}

	private boolean isTitleLine(List<String> row) {
		int numbersInRow = 0;
		for (String cell : row) {
			if (cell == null) {
				continue;
			}
			try {
				Double.parseDouble(cell);
				numbersInRow++;
			} catch (NumberFormatException ex) {/* do nothing */}
		}

		return row.size() - numbersInRow > numbersInRow;
	}

	private List<String> createEmptyList(int size) {
		List<String> returnList = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			returnList.add("");
		}
		return returnList;
	}
}
