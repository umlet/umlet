package com.baselet.element.elementnew.plot.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.baselet.diagram.draw.DrawHandler;

public class ParserResult {

	private DrawHandler drawer;
	private final ArrayList<PlotState> plotStateList;
	// These are few variables which influence the plotgrid
	private final HashMap<String, KeyValue> plotGridValues;

	public ParserResult() {
		plotStateList = new ArrayList<PlotState>();
		plotGridValues = new HashMap<String, KeyValue>();
	}

	public void setDrawer(DrawHandler drawer) {
		this.drawer = drawer;
	}

	public DrawHandler getDrawer() {
		return drawer;
	}

	public ArrayList<PlotState> getPlotStateList() {
		return plotStateList;
	}

	public String getPlotGridValue(String key, String defaultValue) {
		KeyValue keyValue = plotGridValues.get(key);
		if (keyValue != null) {
			keyValue.setUsed(true);
		}
		if (keyValue == null || keyValue.getValue().equals(PlotConstants.DEFAULT_VALUE)) {
			return defaultValue;
		}
		else {
			return keyValue.getValue();
		}
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
		StringBuilder sb = new StringBuilder("\n-----------------------------\n");
		sb.append("--------PARSER CONTENT-------\n");
		sb.append("-----------------------------\n\n");
		sb.append("##########PlotStates#########\n\n");
		for (PlotState plotState : plotStateList) {
			sb.append(plotState.toString()).append("\n");
		}
		sb.append("#########PlotGridValues########\n\n");
		for (Entry<String, KeyValue> e : plotGridValues.entrySet()) {
			sb.append("\t").append(e.getKey()).append(" -> ").append(e.getValue()).append("\n");
		}
		sb.append("\n-----------------------------\n");
		sb.append("-----------------------------\n");
		return sb.toString();
	}
}
