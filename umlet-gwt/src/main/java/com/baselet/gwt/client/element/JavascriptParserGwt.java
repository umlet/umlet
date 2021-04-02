package com.baselet.gwt.client.element;

import com.baselet.control.enums.AlignHorizontal;
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
	public void parse(String line, int width, int height) {
		exportDrawFunctions(drawer, width, height);
		exportAlignHorizontalEnumValues(AlignHorizontal.CENTER, AlignHorizontal.LEFT, AlignHorizontal.RIGHT);

		parseJavascript(formatDrawFunctions(line));
	}

	public native void parseJavascript(String line)/*-{
													new Function(line)();
													}-*/;

	private String formatDrawFunctions(String line) {
		line = line.replace("drawCircle", "$wnd.drawCircle");
		line = line.replace("drawRectangle", "$wnd.drawRectangle");
		line = line.replace("drawRectangleRound", "$wnd.drawRectangleRound");
		line = line.replace("drawEllipse", "$wnd.drawEllipse");
		line = line.replace("drawArc", "$wnd.drawArc");
		line = line.replace("drawLine", "$wnd.drawLine");
		line = line.replace("drawText", "$wnd.drawText");
		line = line.replace("width", "$wnd.width");
		line = line.replace("height", "$wnd.height");
		line = line.replace("center", "$wnd.center");
		line = line.replace("left", "$wnd.left");
		line = line.replace("right", "$wnd.right");
		return line;
	}

	public native void exportAlignHorizontalEnumValues(AlignHorizontal alignCenter, AlignHorizontal alignLeft, AlignHorizontal alignRight) /*-{
																																			$wnd.center = alignCenter;
																																			$wnd.left = alignLeft;
																																			$wnd.right = alignRight;
																																			}-*/;

	public native void exportDrawFunctions(DrawHandler drawer, int width, int height) /*-{
																													$wnd.width = width;
																													$wnd.height = height;

																													$wnd.drawCircle = $entry(function(x,y,radius) {
																													drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawCircle(DDD)(x,y,radius);
																													});
																													$wnd.drawRectangle = $entry(function(x,y,width,height) {
																													drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawRectangle(DDDD)(x,y,width,height);
																													});
																													$wnd.drawLine = $entry(function(x1,y1,x2,y2) {
																													drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawLine(DDDD)(x1,y1,x2,y2);
																													});
																													$wnd.drawRectangleRound = $entry(function(x,y,width,height,radius) {
																													drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawRectangleRound(DDDDD)(x,y,width,height,radius);
																													});
																													$wnd.drawEllipse = $entry(function(x,y,width,height) {
																													drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawEllipse(DDDD)(x,y,width,height);
																													});
																													$wnd.drawArc = $entry(function(x,y,width,height,start,extent,open) {
																													drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawArc(DDDDDDZ)(x,y,width,height,start,extent,open);
																													});
																													$wnd.drawText = $entry(function(multiLineWithMarkup,x,y,align) {
																													drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::print(Ljava/lang/String;DDLcom/baselet/control/enums/AlignHorizontal;)(multiLineWithMarkup,x,y,align);
																													});

																													}-*/;

}
