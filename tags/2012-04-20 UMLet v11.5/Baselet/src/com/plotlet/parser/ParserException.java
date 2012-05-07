package com.plotlet.parser; 

import java.util.Arrays;

public class ParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ParserException() {
		super("Unknown Error");
	}
	
	public ParserException(String message) {
		super(message);
	}
	
	public ParserException(String key, String value, int line) {
		this("Invalid assignment: \"" + key + "=" + value + "\" (line: " + line + ")");
	}
	
	public ParserException(String key, String value, int line, String additionalInfo) {
		this("Invalid assignment: \"" + key + "=" + value + "\" (line: " + line + ") [" + additionalInfo + "]");
	}

	public ParserException(String key, String[] values, int line) {
		this("The following values are colliding: \"" + key + "=" + Arrays.asList(values) + "\" (line: " + line + ")");
	}
	
}
