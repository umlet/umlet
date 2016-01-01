package com.baselet.element.elementnew.plot.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser {

	private static final Logger log = LoggerFactory.getLogger(Parser.class);

	// The parserResult contains every information which is relevant after input parsing is finished
	private final ParserResult parserResult;

	// The following fields are only used during parsing but never referenced after parsing is finished
	// The values HashsMap contains the actual state of key->value assignments which is copied to every plot at its time of creation
	private final HashMap<String, KeyValue> tempPlotValuesCache;
	// The datasetNr is used for sequential naming of not explicitly named datasets
	private int datasetNr = 1;
	// The datasetlist is filled during parsing. After parsing every plot gets its dataset injected
	private final ArrayList<DataSet> datasetList;

	public Parser() {
		parserResult = new ParserResult();
		datasetList = new ArrayList<DataSet>();
		tempPlotValuesCache = new HashMap<String, KeyValue>();
	}

	public ParserResult parse(String source) {

		List<String> inputList = Arrays.asList(source.split("\n", -1));
		ListIterator<String> inputIterator = inputList.listIterator();

		while (inputIterator.hasNext()) {
			String line = inputIterator.next();
			if (line.isEmpty() || line.matches(PlotConstants.REGEX_COMMENT)) {/* ignore empty lines and comments */}
			else if (line.matches(PlotConstants.REGEX_PLOT)) {
				parserResult.addPlotState(createPlotStateObject(line.split(" "), inputIterator));
			}
			else if (line.matches(PlotConstants.REGEX_PLOT_ADD)) {
				List<PlotState> plotStates = parserResult.getPlotStateList();
				if (plotStates.isEmpty()) {
					// if no plotStates, create a new one
					parserResult.addPlotState(createPlotStateObject(line.split(" "), inputIterator));
				}
				else {
					// if plots exist, add new plotState to last plotState
					PlotState last = plotStates.get(plotStates.size() - 1);
					last.addSubPlot(createPlotStateObject(line.split(" "), inputIterator));
				}
			}
			else if (line.matches(PlotConstants.REGEX_DATA)) {
				createDatasetObject(line.split(" "), inputIterator);
			}
			else if (line.matches(PlotConstants.REGEX_DATA_GUESS)) {
				inputIterator.previous(); // Must go 1 step back to avoid skipping the first line in createDatasetObject
				createDatasetObject(new String[] { PlotConstants.DATA }, inputIterator);
			}
			else if (line.matches(PlotConstants.REGEX_VALUE_ASSIGNMENT)) {
				createKeyValueAssignment(line, inputIterator.nextIndex());
			}
			else {
				throw new ParserException("Invalid line: " + line + "(line: " + inputIterator.nextIndex() + ")");
			}
		}

		analyseDatasets();
		addDatasetsToPlotStates();
		return parserResult;
	}

	/**
	 * Is called after parsing everything to analyse the dataset content
	 */
	private void analyseDatasets() {
		for (DataSet dataset : datasetList) {
			dataset.analyseMatrix();
		}
	}

	/**
	 * Is called after parsing everything to fill datasets in each plotState Object
	 */
	private void addDatasetsToPlotStates() {
		if (datasetList.isEmpty()) {
			throw new ParserException("You must specify at least one dataset.");
		}
		int actualAutoDatasetNr = 0;
		for (PlotState plotState : parserResult.getPlotStateList()) {
			actualAutoDatasetNr = addDataset(plotState, actualAutoDatasetNr);
			// also add datasets to subplots
			for (PlotState subPlotState : plotState.getSubplots()) {
				log.info("Add dataset for subplot");
				actualAutoDatasetNr = addDataset(subPlotState, actualAutoDatasetNr);
			}
		}
	}

	private int addDataset(PlotState plotState, int actualAutoDatasetNr) {
		String datasetId = plotState.getValue(PlotConstants.DATA, null);
		if (datasetId == null) {
			if (actualAutoDatasetNr >= datasetList.size()) {
				actualAutoDatasetNr = 0;
			}
			plotState.setDataSet(datasetList.get(actualAutoDatasetNr++));
		}
		else {
			DataSet dataset = null;
			if (datasetId.startsWith("#")) {
				String datasetNr = datasetId.substring(1);
				for (DataSet tempDataset : datasetList) {
					if (datasetNr.equals(String.valueOf(tempDataset.getNr()))) {
						dataset = tempDataset;
					}
				}
			}
			else {
				for (DataSet tempDataset : datasetList) {
					if (datasetId.equals(tempDataset.getId())) {
						dataset = tempDataset;
					}
				}
			}
			if (dataset != null) {
				plotState.setDataSet(dataset);
			}
			else {
				throw new ParserException(PlotConstants.DATA, datasetId, plotState.getLine(PlotConstants.DATA));
			}
		}

		return actualAutoDatasetNr;
	}

	/**
	 * Creates a dataset with the second argument as its id (if it has no such parameter it gets a generated id)
	 * This method is called if the input string starts with "data" or if the input string contains a tab (then a dataset is assumed)
	 * All lines until the next empty line are part of the dataset
	 *
	 * @param args any parameters to the data command including the command itself as first parameter
	 */
	private void createDatasetObject(String[] args, ListIterator<String> inputIterator) {
		int lineNr = inputIterator.nextIndex();
		String datasetId = null;
		if (args != null) {
			if (args.length > 1) {
				datasetId = args[1];
				/* handle further parameters here */
			}
		}
		DataSet newDataset = new DataSet(datasetId, datasetNr++, lineNr);
		while (inputIterator.hasNext()) {
			String nextLine = inputIterator.next();
			if (nextLine.matches(PlotConstants.REGEX_COMMENT)) {
				continue;
			}
			else if (nextLine.trim().isEmpty()) {
				break;
			}
			else {
				newDataset.addLine(nextLine.split(PlotConstants.REGEX_DATA_SEPARATOR));
			}
		}

		if (datasetId != null) {
			for (DataSet ds : datasetList) {
				if (datasetId.equals(ds.getId())) {
					throw new ParserException("The dataset name \"" + datasetId + "\" (line: " + lineNr + ") already exists");
				}
			}
		}
		datasetList.add(newDataset);
	}

	/**
	 * Creates a plotValues Object which contains the dataset as its second argument (if it has no such parameter it cycles through all datasets)
	 * This method is called if the input string starts with "plot". All values which are stored in the parser are copied to the plot
	 *
	 * @param args any parameters to the data command including the command itself as first parameter
	 */
	private PlotState createPlotStateObject(String[] args, ListIterator<String> inputIterator) {
		int lineNr = inputIterator.nextIndex();
		HashMap<String, KeyValue> localCopyOfValuesCache = copyHashMap(tempPlotValuesCache);
		if (args != null) {
			// Arguments are handled as any other key->value assignment but are only valid for this plot
			for (int i = 1; i < args.length; i++) {
				String[] split = args[i].split("=");
				if (split.length == 1) {
					split = new String[] { split[0], "" };
				}
				localCopyOfValuesCache.put(split[0], new KeyValue(split[0], split[1], lineNr));
			}
		}
		// If no dataset is specified the data-value is set to auto
		if (localCopyOfValuesCache.get(PlotConstants.DATA) == null) {
			localCopyOfValuesCache.put(PlotConstants.DATA, new KeyValue(PlotConstants.DATA, PlotConstants.DEFAULT_VALUE, lineNr));
		}
		PlotState newPlotState = new PlotState(lineNr, localCopyOfValuesCache);

		return newPlotState;
	}

	/**
	 * Adds a key-&gt;value assignment to the values HashMap
	 */
	private void createKeyValueAssignment(String line, int lineNr) {
		String[] split = line.split("=");
		if (split.length == 1) {
			split = new String[] { split[0], "" };
		}
		if (split[0].matches(PlotConstants.KEY_INT_GRID_WIDTH)) {
			parserResult.addPlotGridValue(split[0], new KeyValue(split[0], split[1], lineNr));
		}
		else {
			tempPlotValuesCache.put(split[0], new KeyValue(split[0], split[1], lineNr));
		}
	}

	private HashMap<String, KeyValue> copyHashMap(HashMap<String, KeyValue> inputHashMap) {
		HashMap<String, KeyValue> returnHashMap = new HashMap<String, KeyValue>();
		for (Entry<String, KeyValue> entry : inputHashMap.entrySet()) {
			returnHashMap.put(entry.getKey(), entry.getValue());
		}
		return returnHashMap;
	}
}
