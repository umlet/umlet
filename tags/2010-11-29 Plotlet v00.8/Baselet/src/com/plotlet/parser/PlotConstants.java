package com.plotlet.parser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.control.Utils;

public class PlotConstants {

	protected final static Logger log = Logger.getLogger(Utils.getClassName());

	/** Shared Value Constants **/

	// Some key->value assignments have a list as value and the following separator is used to separate the list entries
	public static final String VALUE_LIST_SEPARATOR = ",";
	// If a variable is set to DEFAULT_VALUE, it gets removed from the parsers plotValuesCache, therefore the DEFAULT is used again
	public static final String DEFAULT_VALUE = "auto";

	/** Parser Constants **/

	public static final String PLOT = "plot";
	public static final String DATA = "data";
	public static final String REGEX_COMMENT = "(//.*)";
	public static final String REGEX_KEY = "([(\\w)\\.]+)";
	//a value is a comma separated list of word characters or "-" (for negative int eg: min_val=-3) or # (for color decoding) or . (for hierarchies)
	public static final String REGEX_VALUE = "([-(\\w)#\\." + VALUE_LIST_SEPARATOR + "]*)";
	public static final String REGEX_VALUE_ASSIGNMENT = "(" + REGEX_KEY + "=" + REGEX_VALUE + ")";
	//plot followed by an optional space or plot followed by 1 or more value assignments (values which are only valid for the plot)
	public static final String REGEX_PLOT = "((" + PLOT + " ?)|(" + PLOT + " (" + REGEX_VALUE_ASSIGNMENT + " )*" + REGEX_VALUE_ASSIGNMENT + "))";
	//+plot (overlap plots) followed by an optional space or plot followed by 1 or more value assignments (values which are only valid for the plot)
	public static final String REGEX_PLOT_ADD = "(\\+" + REGEX_PLOT + ")";
	//data followed by an optional space or by space and a name which consists of word characters
	public static final String REGEX_DATA = "((" + DATA + " ?)|(" + DATA + " (\\w)+))";
	// 1 non-comment-line which contains at least 1 Tab is an interpreted dataset
	public static final String REGEX_DATA_SEPARATOR = "(\t)";
	public static final String REGEX_DATA_GUESS = "((?!(" + REGEX_COMMENT + "))(([^=]+)|(.*" + REGEX_DATA_SEPARATOR + ".*)))";

	// The following line is needed to color everything which doesn't match another RegEx
	//	public static final String REGEX_COLOR_BASE = "(?!((" + REGEX_COMMENT + ")|(" + PLOT + ")|(" + REGEX_VALUE_ASSIGNMENT + "))).*";



	/* The following variables are automatically parsed for the autocompletion. Therefore some conventions must be made:
	 * 1.) The possible values of a key must be listed in the following lines or they will not be recognized by the autocompletion
	 * 2.) Every key is separated in 3 parts: KEY_<type>_<name>. <type> can be STRING,INT,LIST,BOOL (in future there may be more types)
	 * 3.) If there is a limited number of possible values it must be named: <name>_<value> where <name> must match the <name> tag in the key
	 * 4.) values with _DEFAULT at the end are ignored by the autocompletion.
	 */

	/** Plotgrid Value Constants **/

	public static final String KEY_INT_GRID_WIDTH = "grid.width";
	public static final String GRID_WIDTH_DEFAULT = "3";

	/** Plot Value Constants **/

	public static final String KEY_STRING_DATA = DATA; //DEFAULT: cycling through datasets

	public static final String KEY_STRING_TYPE = "type"; //DEFAULT: TYPE_BAR
	public static final String TYPE_BAR = "bar";
	public static final String TYPE_LINE = "line";
	public static final String TYPE_PIE = "pie";
	public static final String TYPE_SCATTER = "scatter";

	public static final String KEY_BOOL_DATA_INVERT = "data.invert";
	public static final Boolean DATA_INVERT_DEFAULT = false;

	public static final String KEY_BOOL_PLOT_TILT = "tilt";
	public static final Boolean PLOT_TILT_DEFAULT = false;

	public static final String KEY_INT_X_POSITION = "pos.x"; //DEFAULT: filling grid from upper left to lower right corner
	public static final String KEY_INT_Y_POSITION = "pos.y";

	public static final String KEY_INT_MIN_VALUE = "value.min"; //DEFAULT: the lowest/highest value in the plot
	public static final String MIN_VALUE_ALL = "all";
	public static final String KEY_INT_MAX_VALUE = "value.max";
	public static final String MAX_VALUE_ALL = "all";

	public static final String KEY_LIST_COLORS = "colors"; //DEFAULT: cycling through colors-list
	public static final List<String> COLORS_DEFAULT = Arrays.asList(new String[] {"red", "blue", "green", "orange", "cyan", "magenta", "pink"});

	public static final String KEY_LIST_DESC_AXIS_SHOW = "axis.desc.show";
	public static final String DESC_AXIS_SHOW_AXIS = "axis";
	public static final String DESC_AXIS_SHOW_LINE = "line";
	public static final String DESC_AXIS_SHOW_MARKER = "marker";
	public static final String DESC_AXIS_SHOW_TEXT = "text";
	public static final String DESC_AXIS_SHOW_NOTHING = "";

	public static final String KEY_LIST_VALUE_AXIS_SHOW = "axis.value.show";
	public static final String VALUE_AXIS_SHOW_AXIS = "axis";
	public static final String VALUE_AXIS_SHOW_LINE = "line";
	public static final String VALUE_AXIS_SHOW_MARKER = "marker";
	public static final String VALUE_AXIS_SHOW_TEXT = "text";
	public static final String VALUE_AXIS_SHOW_NOTHING = "";
	
	public static final String KEY_LIST_VALUE_AXIS_LIST = "axis.value.list";
	public static final String VALUE_AXIS_LIST_RELEVANT = "relevant";
	public static final String VALUE_AXIS_LIST_NOTHING = "";

	public static List<String> getValuesForKey(String key) {
		String fieldContent;
		List<String> returnList = null;
		try {
			Field[] fs = PlotConstants.class.getFields();
			returnList = new ArrayList<String>();
			boolean startReadingValues = false;
			for (Field f : fs) {
				fieldContent = String.valueOf(f.get(String.class));
				if (startReadingValues) {
					if (!f.getName().startsWith("KEY_")) returnList.add(fieldContent);
					else break;
				}
				if (fieldContent.equals(key)) startReadingValues = true;
			}
		}
		catch (Exception e) {
			log.error("Error at creating valuelist from key " + key, e);
		}
		return returnList;
	}

}
