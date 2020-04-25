package com.baselet.element.facet.customdrawings;

import com.baselet.element.facet.customdrawings.gen.ParseException;
import com.baselet.element.facet.customdrawings.gen.TokenMgrException;

public class CustomDrawingParserException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 6582281959899320822L;

	public CustomDrawingParserException() {}

	public CustomDrawingParserException(String message) {
		super(message);
	}

	public CustomDrawingParserException(Throwable cause) {
		super(cause);
	}

	public CustomDrawingParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomDrawingParserException(ParseException e) {
		// remove the trailing \n from the error message
		this(e.getMessage().substring(0, e.getMessage().length() - 1), e);
	}

	public CustomDrawingParserException(TokenMgrException e) {
		// remove the trailing \n from the error message
		this(e.getMessage().substring(0, e.getMessage().length() - 1), e);
	}
}
