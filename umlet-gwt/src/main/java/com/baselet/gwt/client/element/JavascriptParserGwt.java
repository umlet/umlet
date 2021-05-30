package com.baselet.gwt.client.element;

import com.baselet.control.constants.FacetConstants;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.LineType;
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
		exportBasicColors();
		exportValidateDrawerConfig();
		exportKeyValueDrawProperties(getDrawer(), FacetConstants.BACKGROUND_COLOR_KEY, FacetConstants.FOREGROUND_COLOR_KEY, LineType.SOLID.getValue(), FacetConstants.LINE_WIDTH_DEFAULT);
		exportExecuteDraw(getDrawer());
		exportDrawFunctions(getDrawer());

		parseJavascript(line);
	}

	public native void parseJavascript(String line)/*-{
													new Function(line)();
													}-*/;

	public native String exportBasicColors() /*-{
												$wnd.red = 'red';
												$wnd.green = 'green';
												$wnd.blue = 'blue';
												}-*/;

	public native String exportKeyValueDrawProperties(DrawHandler drawer, String defaultBgColor,
			String defaultFgColor, String defaultLineType, Double defaultLineWidth) /*-{
																													bg = defaultBgColor;
																													fg = defaultFgColor;
																													lt = defaultLineType;
																													lw = defaultLineWidth;
																													transparency=null;
																													setKeyValueDrawProperties = function () {
																													drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::setLineWidth(D)(lw);
																													drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::setLineType(Ljava/lang/String;)(lt);
																													drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::setBackgroundColorAndKeepTransparency(Ljava/lang/String;)(bg);
																													drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::setForegroundColor(Ljava/lang/String;)(fg);
																													if(transparency != null)
																													drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::setTransparency(D)(transparency);
																													}
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

	public native String exportExecuteDraw(DrawHandler drawer) /*-{
																executeDraw = $entry(function(drawFunction){
																	var oldLineWidth = drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::getLineWidth()();
																	drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::setLineWidth(D)(lw);
																	var oldLineType = drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::getLineType()();
																	drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::setLineType(Ljava/lang/String;)(lt);
																	var oldColorBg = drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::getBackgroundColor()();
																	drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::setBackgroundColorAndKeepTransparency(Ljava/lang/String;)(bg);
																	var oldColorFg = drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::getForegroundColor()();
																	drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::setForegroundColor(Ljava/lang/String;)(fg);
																	if(transparency != null)
																		drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::setTransparency(D)(transparency);

																	drawFunction();

																	drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::setForegroundColor(Lcom/baselet/diagram/draw/helper/ColorOwn;)(oldColorFg);
																	drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::setBackgroundColor(Lcom/baselet/diagram/draw/helper/ColorOwn;)(oldColorBg);
																	drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::setLineType(Lcom/baselet/control/enums/LineType;)(oldLineType);
																	drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::setLineWidth(D)(oldLineWidth);
																	});
																}-*/;

	public native String exportValidateDrawerConfig() /*-{
																			validateDrawerConfig = $entry(function(drawerConfig){
																				if(drawerConfig==null){
																					return {bg: null, fg: null, lt:null, lw:null, transparency:null}
																				}
																				return drawerConfig;
																			});
																			}-*/;

	public native void exportDrawFunctions(DrawHandler drawer) /*-{
																center = $wnd.center;
																left = $wnd.left;
																right = $wnd.right;
																width = $wnd.width;
																height = $wnd.height;
																red = $wnd.red;
																green = $wnd.green;
																blue = $wnd.blue;
																
																
																drawCircle = $entry(function(x,y,radius,drawerConfig) {
																	drawerConfig = validateDrawerConfig(drawerConfig);
																	function drawFunction(){
																		drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawCircle(DDDLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Double;Ljava/lang/Double;)(x,y,radius,drawerConfig.bg,drawerConfig.fg,drawerConfig.lt,drawerConfig.lw,drawerConfig.transparency);
																	}
																	executeDraw(drawFunction);
																});
																drawRectangle = $entry(function(x,y,width,height,drawerConfig) {
																	function drawFunction(){
																		drawerConfig = validateDrawerConfig(drawerConfig);
																		drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawRectangle(DDDDLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Double;Ljava/lang/Double;)(x,y,width,height,drawerConfig.bg,drawerConfig.fg,drawerConfig.lt,drawerConfig.lw,drawerConfig.transparency);
																	}
																	executeDraw(drawFunction);
																});
																drawLine = $entry(function(x1,y1,x2,y2,drawerConfig) {
																	function drawFunction(){
																		drawerConfig = validateDrawerConfig(drawerConfig);
																		drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawLine(DDDDLjava/lang/String;Ljava/lang/String;Ljava/lang/Double;)(x1,y1,x2,y2,drawerConfig.fg,drawerConfig.lt,drawerConfig.lw);
																	}
																	executeDraw(drawFunction);
																});
																drawRectangleRound = $entry(function(x,y,width,height,radius,drawerConfig) {
																	function drawFunction(){
																		drawerConfig = validateDrawerConfig(drawerConfig);
																		drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawRectangleRound(DDDDDLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Double;Ljava/lang/Double;)(x,y,width,height,radius,drawerConfig.bg,drawerConfig.fg,drawerConfig.lt,drawerConfig.lw,drawerConfig.transparency);
																	}
																	executeDraw(drawFunction);
																});
																drawEllipse = $entry(function(x,y,width,height,drawerConfig) {
																	function drawFunction(){
																		drawerConfig = validateDrawerConfig(drawerConfig);
																		drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawEllipse(DDDDLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Double;Ljava/lang/Double;)(x,y,width,height,drawerConfig.bg,drawerConfig.fg,drawerConfig.lt,drawerConfig.lw,drawerConfig.transparency);
																	}
																	executeDraw(drawFunction);
																});
																drawArc = $entry(function(x,y,width,height,start,extent,open,drawerConfig) {
																	function drawFunction(){
																		drawerConfig = validateDrawerConfig(drawerConfig);
																		drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::drawArc(DDDDDDZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Double;Ljava/lang/Double;)(x,y,width,height,start,extent,open,drawerConfig.bg,drawerConfig.fg,drawerConfig.lt,drawerConfig.lw,drawerConfig.transparency);
																	}
																	executeDraw(drawFunction);
																});
																drawText = $entry(function(multiLineWithMarkup,x,y,align,drawerConfig) {
																	function drawFunction(){
																		drawerConfig = validateDrawerConfig(drawerConfig);
																		drawer.@com.baselet.gwt.client.element.DrawHandlerGwt::print(Ljava/lang/String;DDLcom/baselet/control/enums/AlignHorizontal;Ljava/lang/String;)(multiLineWithMarkup,x,y,align,drawerConfig.fg);
																	}
																	executeDraw(drawFunction);
																});
																}-*/;

}