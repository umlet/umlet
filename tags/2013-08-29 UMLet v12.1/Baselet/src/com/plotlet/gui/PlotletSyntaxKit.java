package com.plotlet.gui;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.plotlet.parser.PlotConstants;

public class PlotletSyntaxKit {
	
	private static final Logger log = Logger.getLogger(PlotletSyntaxKit.class);

	private static final HashMap<String,Pattern> regExMap = new HashMap<String,Pattern>();
	static {	
		// The regex are matched anywhere in the whole text (not only at the beginning of a line)
		// If more than 1 RegEx match, the longest has the priority (eg: "plot" overrules "plo")
		regExMap.put("KEYWORD", Pattern.compile(PlotConstants.PLOT));
		//		regExMap.put("KEYWORD2", Pattern.compile(PlotConstants.REGEX_COLOR_BASE));
		regExMap.put("COMMENT", Pattern.compile(PlotConstants.REGEX_COMMENT));
		regExMap.put("TYPE", Pattern.compile(PlotConstants.REGEX_VALUE_ASSIGNMENT));
	}
	
	/**
	 * TODO Must be refactored to work with RSyntaxTextArea.
	 * See how UMLet solves syntaxhighlighting and autocompletion and implement it appropriately (ie: move this code to PlotGrid and make it work with RSyntaxTextArea)
	 */
	public static String createAutocompletionList(String listSep) {
		String outString = "plot" + listSep + "data" + listSep + "data=" + /*"<dataset_name>" +*/ listSep;

			String fieldName = "", fieldContent = "", keyName = "", keyContent = "", keyType = "";
			try {
				Field[] fs = PlotConstants.class.getFields();
				for (Field f : fs) {
					fieldName = f.getName();
					fieldContent = String.valueOf(f.get(String.class));
					if (fieldName.startsWith("KEY_")) {
						if (keyType.equals("LIST")) {
							if (outString.lastIndexOf(PlotConstants.VALUE_LIST_SEPARATOR) != outString.length()-1) { // If no list values are listed
								outString += /*"<a>" + PlotConstants.VALUE_LIST_SEPARATOR + "<b>" + PlotConstants.VALUE_LIST_SEPARATOR + "..." + */listSep;
							}
							else {
								outString = outString.substring(0, outString.length()-PlotConstants.VALUE_LIST_SEPARATOR.length()) + listSep;
							}
						}
						String[] keySplit = fieldName.split("_");
						keyType = keySplit[1];
						keyName = keySplit[2];
						keyContent = fieldContent;
						if (keyType.equals("BOOL")) {
							outString += keyContent + "=" + "true" + listSep;
							outString += keyContent + "=" + "false" + listSep;
						}
						else if (keyType.equals("INT")) {
							outString += keyContent + "=" + /*"<int>" + */listSep;
						}
						else if (keyType.equals("LIST")) {
							outString += keyContent + "="; 
						}
					}
					else if (!keyName.isEmpty() && fieldName.startsWith(keyName) && !fieldName.endsWith("DEFAULT") && !fieldContent.isEmpty()) {
						if (keyType.equals("LIST")) outString += fieldContent + PlotConstants.VALUE_LIST_SEPARATOR;
						else outString += keyContent + "=" + fieldContent + listSep;
					}
				}
				if (outString.lastIndexOf(PlotConstants.VALUE_LIST_SEPARATOR) == outString.length()-1) {
					outString = outString.substring(0, outString.length()-PlotConstants.VALUE_LIST_SEPARATOR.length()) + listSep;
				}
			}
			catch (Exception e) {
				log.error("Error at creating keyValueMap", e);
			}
		
		return outString;

	}
}
