package com.plotlet.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PlotState {

	private List<PlotState> subPlots;

	private DataSet dataset;
	private HashMap<String, KeyValue> values;
	private int plotLineNr;

	protected PlotState(int plotLineNr, HashMap<String, KeyValue> values) {
		this.subPlots = new ArrayList<PlotState>();
		this.plotLineNr = plotLineNr;
		this.values = values;
	}

	/**
	 * Is only called once by the parser to calculate the dataset.
	 */
	protected void setDataSet(DataSet dataset) {
		this.dataset = dataset;
	}

	public void addSubPlot(PlotState plotState)	{
		subPlots.add(plotState);
	}

	public List<PlotState> getSubplots() {
		return subPlots;		
	}

	public DataSet getDataSet() {
		return dataset;
	}

	public boolean containsKey(String key) {
		return values.containsKey(key);
	}

	public int getLine(String key) {
		if (values.get(key) != null) return values.get(key).getLine();
		else return -1;
	}

	public int getPlotLineNr() {
		return plotLineNr;
	}

	public String getValue(String key, String defaultValue) {
		KeyValue keyValue = values.get(key);
		if (keyValue != null) keyValue.setUsed(true);
		if (keyValue == null || keyValue.getValue().equals(PlotConstants.DEFAULT_VALUE)) return defaultValue;
		else return keyValue.getValue();
	}

	public String getValueValidated(String key, String defaultValue, List<String> validValues) {
		String value = getValue(key, defaultValue);
		if (!validValues.contains(value)) throw new ParserException(key, value, getLine(key));
		return value;
	}

	public Double getValueAsDouble(String key, Double defaultValue) {
		try {
			String value = this.getValue(key, null);
			if (value == null) return defaultValue;
			else return Double.parseDouble(value);
		} catch (Exception e) {
			throw new ParserException(key, values.get(key).getValue(), values.get(key).getLine());
		}
	}

	public Integer getValueAsInt(String key, Integer defaultValue) {
		try {
			String value = this.getValue(key, null);
			if (value == null) return defaultValue;
			else return Integer.parseInt(value);
		} catch (Exception e) {
			throw new ParserException(key, values.get(key).getValue(), values.get(key).getLine());
		}
	}

	public Boolean getValueAsBoolean(String key, Boolean defaultValue) {
		String value = this.getValue(key, null);
		if (value == null) return defaultValue;
		//Boolean.parseBoolean() cannot be used because it doesn't throw an Exception
		else if (value.equals("true")) return true;
		else if (value.equals("false")) return false;
		else throw new ParserException(key, values.get(key).getValue(), values.get(key).getLine());
	}

	public List<String> getValueList(String key, List<String> defaultValue) {
		List<String> returnArray;
		String value = this.getValue(key, null);
		if (value == null) returnArray = defaultValue;
		else returnArray = Arrays.asList(value.split(PlotConstants.VALUE_LIST_SEPARATOR));
		return returnArray;
	}

	public List<String> getValueListValidated(String key, List<String> defaultValue, List<String> validValues, boolean doubleValuesAllowed) {
		List<String> valueList = getValueList(key, defaultValue);
		for (String value : valueList) {
			boolean intAllowedAndValueIsInt = doubleValuesAllowed && isDoubleValue(value);
			if (!validValues.contains(value) && !intAllowedAndValueIsInt) throw new ParserException(key, value, getLine(key));
		}
		return valueList;
	}

	private boolean isDoubleValue(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} 
		catch (Exception e) { return false; }
	}

	/**
	 * Checks if all declared values are used. It throws an error for declarations which are not used by the plot (=they are invalid)
	 */
	public void checkIfAllValuesUsed() {
		String unusedVariables = "";
		for (KeyValue keyValue : values.values()) {
			if (!keyValue.isUsed()) unusedVariables += "\"" + keyValue.getKey() + "=" + keyValue.getValue() + "\" (line " + keyValue.getLine() + ") ";
		}
		if (!unusedVariables.isEmpty()) throw new ParserException("Invalid variables: " + unusedVariables);
	}

	@Override
	public String toString() {
		String returnString = "";
		returnString += "PlotState (" + plotLineNr + ")\n";
		if (dataset != null) returnString += "\tdataset -> " + dataset.getLineNr() + "\n";
		for (KeyValue keyValue : values.values()) {
			returnString += "\t" + keyValue + "\n";
		}

		if (!subPlots.isEmpty()) {
			returnString += "---Begin Subplots---\n";
			for (PlotState subPlot: subPlots) {
				returnString += subPlot.toString();  
			}
			returnString += "---End Subplots---\n";
		}

		return returnString;
	}
}
