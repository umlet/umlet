package com.baselet.element.facet.customdrawings;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.facet.customdrawings.gen.CustomDrawingParser;
import com.baselet.element.facet.customdrawings.gen.CustomDrawingParserTokenManager;
import com.baselet.element.facet.customdrawings.gen.ParseException;
import com.baselet.element.facet.customdrawings.gen.Provider;
import com.baselet.element.facet.customdrawings.gen.StringProvider;
import com.baselet.element.facet.customdrawings.gen.TokenMgrException;

public class CustomDrawingParserImpl extends CustomDrawingParser {

	private final double width;
	private final double height;
	private final DrawHandler drawHandler;

	public CustomDrawingParserImpl(CustomDrawingParserTokenManager tm, double width, double height, DrawHandler drawHandler) {
		super(tm);
		this.width = width;
		this.height = height;
		this.drawHandler = drawHandler;
	}

	public CustomDrawingParserImpl(String dsl, double width, double height, DrawHandler drawHandler) {
		this(new StringProvider(dsl), width, height, drawHandler);
	}

	public CustomDrawingParserImpl(Provider stream, double width, double height, DrawHandler drawHandler) {
		super(stream);
		this.width = width;
		this.height = height;
		this.drawHandler = drawHandler;
	}

	/**
	 *
	 * @param line String which should be parsed
	 * @param width of the element, can be used in the drawing command
	 * @param height of the element, can be used in the drawing command
	 * @param drawer is used to execute the drawing commands
	 * @throws CustomDrawingParserRuntimeException if the input couldn't be parsed (cause is a ParseException or TokenMgrException)
	 */
	public static void parse(String line, int width, int height, DrawHandler drawer) {
		new CustomDrawingParserImpl(line, width, height, drawer).parse();
	}

	/**
	 * calls the function for the start symbol and catches exceptions which are re-thrown as CustomDrawingParserRuntimeException
	 * @throws CustomDrawingParserRuntimeException if the input couldn't be parsed (cause is a ParseException or TokenMgrException)
	 * @see CustomDrawingParserImpl#start()
	 */
	public void parse() {
		try {
			start();
		} catch (ParseException e) {
			throw new CustomDrawingParserRuntimeException(e);
		} catch (TokenMgrException e) {
			throw new CustomDrawingParserRuntimeException(e);
		}
	}

	/**
	 *
	 * @param line String which should be parsed
	 * @param width of the element, can be used in the drawing command
	 * @param height of the element, can be used in the drawing command
	 * @param drawer is used to execute the drawing commands
	 * @throws CustomDrawingParserException if the input couldn't be parsed (cause is a ParseException or TokenMgrException)
	 */
	public static void parseCheckedEx(String line, int width, int height, DrawHandler drawer) throws CustomDrawingParserException {
		new CustomDrawingParserImpl(line, width, height, drawer).parse();
	}

	/**
	 * calls the function for the start symbol and catches exceptions which are re-thrown as CustomDrawingParserException
	 * @throws CustomDrawingParserException if the input couldn't be parsed (cause is a ParseException or TokenMgrException)
	 * @see CustomDrawingParserImpl#start()
	 */
	public void parseCheckedEx() throws CustomDrawingParserException {
		try {
			start();
		} catch (ParseException e) {
			throw new CustomDrawingParserException(e);
		} catch (TokenMgrException e) {
			throw new CustomDrawingParserException(e);
		}
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public DrawHandler getDrawHandler() {
		return drawHandler;
	}
}
