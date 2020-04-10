package com.baselet.control;

import java.util.ArrayList;
import java.util.List;

public class Matrix<T> {

	private List<List<T>> matrix = new ArrayList<List<T>>();

	/**
	 * @return the row size
	 */
	public int rows() {
		return matrix.size();
	}

	/**
	 * @return the column size
	 */
	public int cols() {
		int longestCol = 0;
		for (List<T> row : matrix) {
			if (row.size() > longestCol) {
				longestCol = row.size();
			}
		}
		return longestCol;
	}

	public List<T> row(int index) {
		return matrix.get(index);
	}

	public List<T> col(int index) {
		List<T> result = new ArrayList<T>(cols());
		for (List<T> row : matrix) {
			if (index < row.size()) {
				result.add(row.get(index));
			}
			else {
				result.add(null);
			}
		}
		return result;
	}

	public T cell(int row, int col) {
		return row(row).get(col);
	}

	public boolean hasMoreRowsThanCols() {
		return rows() > cols();
	}

	public void invert() {
		List<List<T>> result = new ArrayList<List<T>>(cols());
		for (int i = 0; i < cols(); i++) {
			result.add(col(i));
		}
		matrix = result;
	}

	public void addLine(List<T> line) {
		matrix.add(line);
	}

	public boolean isEmpty() {
		for (List<T> row : matrix) {
			if (!row.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		for (List<T> row : matrix) {
			for (T value : row) {
				sb.append(value != null ? value : "null").append("\t");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}
