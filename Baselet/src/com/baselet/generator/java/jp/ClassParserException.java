package com.baselet.generator.java.jp;

@SuppressWarnings("serial")
public class ClassParserException extends Exception {

	public ClassParserException(String message, Throwable e) {
		super(message, e);
	}

	public ClassParserException(String message) {
		super(message);
	}
}
