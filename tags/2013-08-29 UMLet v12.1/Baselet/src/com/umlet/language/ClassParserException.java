package com.umlet.language;

@SuppressWarnings("serial")
public class ClassParserException extends Exception {
	
	public ClassParserException(String message, Exception e) {
		super(message, e);
	}

	public ClassParserException(String message) {
		super(message);
	}
}
