package com.baselet.gwt.client.element;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.JavascriptCodeParser;
import com.baselet.gwt.client.logging.CustomLogger;
import com.baselet.gwt.client.logging.CustomLoggerFactory;

public class JavascriptParserGwt extends JavascriptCodeParser {
	private static final CustomLogger log = CustomLoggerFactory.getLogger(JavascriptParserGwt.class);

	private final DrawHandler drawer;

	public JavascriptParserGwt(DrawHandler drawer) {
		this.drawer = drawer;
	}

	@Override
	public void parse(String line) {
		exportDrawFunctions(drawer);

		parseJavascript(formatDrawFunctions(line));
	}

	public native void parseJavascript(String line)/*-{
													eval(line);
													}-*/;

	private String formatDrawFunctions(String line) {
		line = line.replace("drawCircle", "$wnd.drawCircle");
		line = line.replace("drawRectangle", "$wnd.drawRectangle");
		line = line.replace("drawLine", "$wnd.drawLine");
		return line;
	}

	public native void exportDrawFunctions(DrawHandler drawer) /*-{
																$wnd.drawCircle = $entry(function(x,y,radius) {
																drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawCircle(DDD)(x,y,radius);
																});
																$wnd.drawRectangle = $entry(function(x,y,width,height) {
																drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawRectangle(DDDD)(x,y,width,height);
																});
																$wnd.drawLine = $entry(function(x1,y1,x2,y2) {
																drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawLine(DDDD)(x1,y1,x2,y2);
																});
																}-*/;

}
