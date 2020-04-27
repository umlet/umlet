package com.baselet.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baselet.control.constants.Constants;

public class CompileError {

	// Pattern for error messages from custom element compiler (1. Group = LineNr, 2. Group = line, 3. Group = error)
	private static final Pattern error_pattern = Pattern.compile(".*ERROR.*at line ([0-9]+).*\\n(.*)\\n.*\\n(.*)");
	// EXAMPLE:
	// 1. ERROR in C:\Users\Andi\AppData\Local\Temp\CustomElementImpl.java (at line 27)
	// int y=textHeight();
	// ^^^^^^^^^^^^
	// Type mismatch: cannot convert from float to int

	private final int lineNr;
	private final String line;
	private final String error;

	public static List<CompileError> getListFromString(String errorString, int beforecodelines) {
		List<CompileError> returnList = new ArrayList<CompileError>();
		String[] splitErrors = errorString.replaceAll("\r\n", Constants.NEWLINE).split(Constants.NEWLINE + "----------" + Constants.NEWLINE);
		for (String error : splitErrors) {
			Matcher m = error_pattern.matcher(error);
			if (m.find()) {
				Integer newLineNr = Integer.parseInt(m.group(1)) - beforecodelines;
				String newLine = m.group(2);
				String newError = m.group(3);
				returnList.add(new CompileError(newLineNr, newLine, newError));
			}
		}
		return returnList;
	}

	private CompileError(int lineNr, String line, String error) {
		super();
		this.lineNr = lineNr;
		this.line = line;
		this.error = error;
	}

	public int getLineNr() {
		return lineNr;
	}

	public String getLine() {
		return line;
	}

	public String getError() {
		return error;
	}

}
