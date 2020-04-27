package com.baselet.element.elementnew.plot.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.baselet.gui.AutocompletionText;

public class PlotConstants {

	/** Shared Value Constants **/

	// Some key->value assignments have a list as value and the following separator is used to separate the list entries
	public static final String VALUE_LIST_SEPARATOR = ",";
	public static final String KEY_VALUE_SEP = "=";
	// If a variable is set to DEFAULT_VALUE, it gets removed from the parsers plotValuesCache, therefore the DEFAULT is used again
	public static final String DEFAULT_VALUE = "auto";

	/** Parser Constants **/

	public static final String PLOT = "plot";
	public static final String DATA = "data";
	public static final String REGEX_COMMENT = "(//.*)";
	public static final String REGEX_KEY = "([(\\w)\\.]+)";
	// a value is a comma separated list of word characters or "-" (for negative int eg: min_val=-3) or # (for color decoding) or . (for hierarchies)
	public static final String REGEX_VALUE = "([-(\\w)#\\." + VALUE_LIST_SEPARATOR + "]*)";
	public static final String REGEX_VALUE_ASSIGNMENT = "(" + REGEX_KEY + KEY_VALUE_SEP + REGEX_VALUE + ")";
	// plot followed by an optional space or plot followed by 1 or more value assignments (values which are only valid for the plot)
	public static final String REGEX_PLOT = "((" + PLOT + " ?)|(" + PLOT + " (" + REGEX_VALUE_ASSIGNMENT + " )*" + REGEX_VALUE_ASSIGNMENT + "))";
	// +plot (overlap plots) followed by an optional space or plot followed by 1 or more value assignments (values which are only valid for the plot)
	public static final String REGEX_PLOT_ADD = "(\\+" + REGEX_PLOT + ")";
	// data followed by an optional space or by space and a name which consists of word characters
	public static final String REGEX_DATA = "((" + DATA + " ?)|(" + DATA + " (\\w)+))";
	// 1 non-comment-line which contains at least 1 Tab is an interpreted dataset
	public static final String REGEX_DATA_SEPARATOR = "([\t ]+)";
	public static final String REGEX_DATA_GUESS = "((?!(" + REGEX_COMMENT + "))(([^=]+)|(.*" + REGEX_DATA_SEPARATOR + ".*)))";

	// The following line is needed to color everything which doesn't match another RegEx
	// public static final String REGEX_COLOR_BASE = "(?!((" + REGEX_COMMENT + ")|(" + PLOT + ")|(" + REGEX_VALUE_ASSIGNMENT + "))).*";

	/* The following variables are automatically parsed for the autocompletion. Therefore some conventions must be made: 1.) The possible values of a key must be listed in the following lines or they will not be recognized by the autocompletion 2.) Every key is separated in 3 parts: KEY_<type>_<name>. <type> can be STRING,INT,LIST,BOOL (in future there may be more types) 3.) If there is a limited number of possible values it must be named: <name>_<value> where <name> must match the <name> tag in the key 4.) values with _DEFAULT at the end are ignored by the autocompletion. */

	/** Plotgrid Value Constants **/

	public static final String KEY_INT_GRID_WIDTH = "grid.width";
	public static final String GRID_WIDTH_DEFAULT = "3";

	/** Plot Value Constants **/

	public static final String KEY_BOOL_DATA_INVERT = "data.invert";
	public static final Boolean DATA_INVERT_DEFAULT = false;

	public static final String KEY_BOOL_PLOT_TILT = "tilt";
	public static final Boolean PLOT_TILT_DEFAULT = false;

	public static final String KEY_INT_X_POSITION = "pos.x"; // DEFAULT: filling grid from upper left to lower right corner
	public static final String KEY_INT_Y_POSITION = "pos.y";

	public static final String KEY_INT_MIN_VALUE = "value.min"; // DEFAULT: the lowest/highest value in the plot
	public static final String MIN_VALUE_ALL = "all";
	public static final String KEY_INT_MAX_VALUE = "value.max";
	public static final String MAX_VALUE_ALL = "all";

	public static final String KEY_LIST_COLORS = "colors"; // DEFAULT: cycling through colors-list
	public static final List<String> COLORS_DEFAULT = Collections.unmodifiableList(Arrays.asList("red", "blue", "green", "orange", "cyan", "magenta", "pink"));

	public static interface PlotSetting {
		public String getValue();
	}

	public static enum PlotType implements PlotSetting {
		Bar, Line, Pie, Scatter;

		@Override
		public String getValue() {
			return toString().toLowerCase();
		}

		public static String getKey() {
			return "type";
		}
	}

	public static enum AxisShow implements PlotSetting {
		Axis, Line, Marker, Text, Nothing("");

		private final String value;

		AxisShow() {
			value = toString().toLowerCase();
		}

		public static String getValueList() {
			return KEY_VALUE_SEP + AxisShow.Axis.getValue() + VALUE_LIST_SEPARATOR + AxisShow.Line.getValue() + VALUE_LIST_SEPARATOR + AxisShow.Marker.getValue() + VALUE_LIST_SEPARATOR + AxisShow.Text.getValue();
		}

		AxisShow(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return value;
		}

		public static String getKeyValueAxis() {
			return "axis.value.show";
		}

		public static String getKeyDescAxis() {
			return "axis.desc.show";
		}
	}

	public static enum AxisList implements PlotSetting {
		Relevant, Nothing("");

		private final String value;

		AxisList() {
			value = toString().toLowerCase();
		}

		AxisList(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return value;
		}

		public static String getKey() {
			return "axis.value.list";
		}
	}

	public static List<String> toStringList(PlotSetting[] input) {
		return toStringList(Arrays.asList(input));
	}

	public static List<String> toStringList(List<? extends PlotSetting> input) {
		List<String> returnList = new ArrayList<String>();
		for (PlotSetting o : input) {
			returnList.add(o.getValue());
		}
		return returnList;
	}

	public static final List<AutocompletionText> AUTOCOMPLETION_LIST = Collections.unmodifiableList(Arrays.asList(
			new AutocompletionText(PLOT, "draws the configured plot"),
			new AutocompletionText(DATA, "marks everything until the next empty line as dataset"),
			new AutocompletionText(DATA + KEY_VALUE_SEP + "<name>", "as data but with explicit name"),
			new AutocompletionText(KEY_INT_GRID_WIDTH + KEY_VALUE_SEP + GRID_WIDTH_DEFAULT, "sets the amount of plots per line"),
			new AutocompletionText(KEY_BOOL_DATA_INVERT + KEY_VALUE_SEP + DATA_INVERT_DEFAULT, "inverts the dataset"),
			new AutocompletionText(KEY_BOOL_PLOT_TILT + KEY_VALUE_SEP + PLOT_TILT_DEFAULT, "tilts the plot"),
			new AutocompletionText(KEY_INT_X_POSITION + KEY_VALUE_SEP + "<integer>", "places the next plot at specific horizontal grid position"),
			new AutocompletionText(KEY_INT_Y_POSITION + KEY_VALUE_SEP + "<integer>", "places the next plot at specific vertical grid position"),
			new AutocompletionText(KEY_INT_MIN_VALUE + KEY_VALUE_SEP + MIN_VALUE_ALL, "restrict the highest value shown in the plot"),
			new AutocompletionText(KEY_INT_MAX_VALUE + KEY_VALUE_SEP + MAX_VALUE_ALL, "restrict the lowest value shown in the plot"),
			new AutocompletionText(KEY_LIST_COLORS + KEY_VALUE_SEP + COLORS_DEFAULT.get(0) + VALUE_LIST_SEPARATOR + COLORS_DEFAULT.get(1), "sets a list of colors which will be cycled by the plot"),

			new AutocompletionText(PlotType.getKey() + KEY_VALUE_SEP + PlotType.Bar.getValue(), "sets the plot type to Bar plot"),
			new AutocompletionText(PlotType.getKey() + KEY_VALUE_SEP + PlotType.Line.getValue(), "sets the plot type to Line plot"),
			new AutocompletionText(PlotType.getKey() + KEY_VALUE_SEP + PlotType.Pie.getValue(), "sets the plot type to Pie plot"),
			new AutocompletionText(PlotType.getKey() + KEY_VALUE_SEP + PlotType.Scatter.getValue(), "sets the plot type to Scatter plot"),

			new AutocompletionText(AxisShow.getKeyValueAxis() + AxisShow.getValueList(), "a list of elements to show at the value axis"),
			new AutocompletionText(AxisShow.getKeyDescAxis() + AxisShow.getValueList(), "a list of elements to show at the description axis"),

			new AutocompletionText(AxisList.getKey() + KEY_VALUE_SEP + AxisList.Relevant.getValue(), "restricts shown values to occurring ones")));

}
