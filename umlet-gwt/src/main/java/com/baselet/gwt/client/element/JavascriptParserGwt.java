package com.baselet.gwt.client.element;

import com.baselet.control.enums.AlignHorizontal;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.JavascriptCodeParser;

public class JavascriptParserGwt extends JavascriptCodeParser {

	public JavascriptParserGwt(DrawHandler drawer) {
		setDrawer(drawer);
	}
	@Override
	public void parse(String line, int width, int height) {
		exportWidthAndHeight(width, height);
		exportAlignHorizontalEnumValues(AlignHorizontal.CENTER, AlignHorizontal.LEFT, AlignHorizontal.RIGHT);
		exportDrawFunctions(getDrawer());

		parseJavascript(line);
	}

	public native void parseJavascript(String line)/*-{
													new Function(line)();
													}-*/;

	public native void exportAlignHorizontalEnumValues(AlignHorizontal alignCenter, AlignHorizontal alignLeft, AlignHorizontal alignRight) /*-{
																																			$wnd.center = alignCenter;
																																			$wnd.left = alignLeft;
																																			$wnd.right = alignRight;
																																			}-*/;

	public native String exportWidthAndHeight(int width, int height) /*-{
																			$wnd.width = width;
																			$wnd.height = height;
																			}-*/;

	public native void exportDrawFunctions(DrawHandler drawer) /*-{
																						center = $wnd.center;
																						left = $wnd.left;
																						right = $wnd.right;
																						width = $wnd.width;
																						height = $wnd.height;
																						drawCircle = $entry(function(x,y,radius) {
																						drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawCircle(DDD)(x,y,radius);
																						});
																						drawRectangle = $entry(function(x,y,width,height) {
																						drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawRectangle(DDDD)(x,y,width,height);
																						});
																						drawLine = $entry(function(x1,y1,x2,y2) {
																						drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawLine(DDDD)(x1,y1,x2,y2);
																						});
																						drawRectangleRound = $entry(function(x,y,width,height,radius) {
																						drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawRectangleRound(DDDDD)(x,y,width,height,radius);
																						});
																						drawEllipse = $entry(function(x,y,width,height) {
																						drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawEllipse(DDDD)(x,y,width,height);
																						});
																						drawArc = $entry(function(x,y,width,height,start,extent,open) {
																						drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawArc(DDDDDDZ)(x,y,width,height,start,extent,open);
																						});
																						drawText = $entry(function(multiLineWithMarkup,x,y,align) {
																						drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::print(Ljava/lang/String;DDLcom/baselet/control/enums/AlignHorizontal;)(multiLineWithMarkup,x,y,align);
																						});
																
																						}-*/;

}
