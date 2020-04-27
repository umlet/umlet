package com.baselet.element.facet.customdrawings;

import com.baselet.element.facet.customdrawings.gen.ParseException;
import com.baselet.element.facet.customdrawings.gen.TokenMgrException;

public class CustomDrawingParserRuntimeException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 6582281959899320822L;

	public CustomDrawingParserRuntimeException() {}

	public CustomDrawingParserRuntimeException(String message) {
		super(message);
	}

	public CustomDrawingParserRuntimeException(Throwable cause) {
		super(cause);
	}

	public CustomDrawingParserRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomDrawingParserRuntimeException(ParseException e) {
		// remove the trailing \n from the error message
		this(e.getMessage().substring(0, e.getMessage().length() - 1), e);
	}

	public CustomDrawingParserRuntimeException(TokenMgrException e) {
		// remove the trailing \n from the error message
		this(e.getMessage().substring(0, e.getMessage().length() - 1), e);
	}
}
