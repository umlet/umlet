package com.plotlet.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class ParserResult {

	private ArrayList<PlotState> plotStateList;
	// These are few variables which influence the plotgrid
	private HashMap<String, KeyValue> plotGridValues;
	
	public ParserResult() {
		plotStateList = new ArrayList<PlotState>();
		plotGridValues = new HashMap<String, KeyValue>();
	}

	public ArrayList<PlotState> getPlotStateList() {
		return plotStateList;
	}

	public String getPlotGridValue(String key, String defaultValue) {
		KeyValue keyValue = plotGridValues.get(key);
		if (keyValue != null) keyValue.setUsed(true);
		if (keyValue == null || keyValue.getValue().equals(PlotConstants.DEFAULT_VALUE)) return defaultValue;
		else return keyValue.getValue();
	}

	protected void addPlotState(PlotState plotState) {
		plotStateList.add(plotState);
	}
	
	protected void addPlotGridValue(String key, KeyValue value) {
		plotGridValues.put(key, value);
	}
	
	protected void removePlotGridValue(String key) {
		plotGridValues.remove(key);
	}

	@Override
	public String toString() {
		String returnString = "\n-----------------------------\n";
		returnString += "--------PARSER CONTENT-------\n";
		returnString += "-----------------------------\n\n";
		returnString += ("##########PlotStates#########\n\n");
		for (PlotState plotState : plotStateList) {
			returnString += plotState.toString() + "\n";
		}
		returnString += ("#########PlotGridValues########\n\n");
		for (String key : plotGridValues.keySet()) {
			returnString += ("\t" + key + " -> " + plotGridValues.get(key) + "\n");
		}
		returnString += "\n-----------------------------\n";
		returnString += "-----------------------------\n";
		return returnString;
	}
	
}
