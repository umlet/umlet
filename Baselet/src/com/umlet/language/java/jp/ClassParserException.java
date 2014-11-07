package com.umlet.language.java.jp;

@SuppressWarnings("serial")
public class ClassParserException extends Exception {

	public ClassParserException(String message, Exception e) {
		super(message, e);
	}

	public ClassParserException(String message) {
		super(message);
	}
}
