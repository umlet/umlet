package com.umlet.gui;

import java.util.HashMap;
import java.util.regex.Pattern;

import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.lexers.SimpleRegexLexer;

import org.apache.log4j.Logger;

import com.baselet.control.Utils;
import com.plotlet.parser.PlotConstants;
import com.umlet.element.experimental.Properties;
import com.umlet.element.experimental.Properties.SettingKey;

@SuppressWarnings("serial")
public class UmletSyntaxKit extends DefaultSyntaxKit {
	
	protected final static Logger log = Logger.getLogger(Utils.getClassName());

	private static final HashMap<String,Pattern> regExMap = new HashMap<String,Pattern>();
	static {
		// The regex are matched anywhere in the whole text (not only at the beginning of a line)
		// If more than 1 RegEx match, the longest has the priority (eg: "plot" overrules "plo")
		//regExMap.put("KEYWORD", Pattern.compile(PlotConstants.PLOT));
		//		regExMap.put("KEYWORD2", Pattern.compile(PlotConstants.REGEX_COLOR_BASE));
		regExMap.put("COMMENT", Pattern.compile(PlotConstants.REGEX_COMMENT));
		regExMap.put("TYPE", Pattern.compile(PlotConstants.REGEX_VALUE_ASSIGNMENT));
	}

	public UmletSyntaxKit() {
		super(new SimpleRegexLexer(regExMap));
	}
	
	public static String createAutocompletionList(String listSep) {
		String outString = "";
		for (SettingKey setting : SettingKey.values()) {
			for (Object value : setting.autocompletionValues()) {
				outString += setting.getKey().toLowerCase() + Properties.SEPARATOR + value.toString().toLowerCase() + listSep;
			}
		}
		return outString;

	}
}
