package com.baselet.diagram.draw;

import java.awt.Color;
import java.awt.Graphics;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.Dimension;

public interface BaseDrawHandler {

	void setGraphics(Graphics g);

	void drawAll(boolean isSelected);

	void drawAll();

	float getDistanceBetweenTexts();

	float textHeight();

	BaseDrawHandler getPseudoDrawHandler();

	float textHeightWithSpace();

	void print(String line, float x, float y, AlignHorizontal align);

	float textWidth(String text);

	void drawRectangle(float x, float y, float width, float height);

	void setHandler(DiagramHandler handler);

	void clearCache();

	void resetStyle();

	void setSize(Dimension size);

	void setForegroundAlpha(float alpha);

	void setBackground(Color color, float alpha);

	void resetColorSettings();

	void setLineType(LineType type);

	void setForegroundColor(Color color);

	void drawLine(float x1, float y1, float x2, float y2);

	void setBackground(String color, float alpha);

	void drawLineVertical(float x);

	void setForegroundColor(String color);

	void setLineType(String type);

	void setLineThickness(float lineThickness);

	void setFontSize(String fontSize);

	Style getCurrentStyle();

	void setBackgroundAlpha(float alpha);

	void setCurrentStyle(Style style);

	void drawEllipse(float x, float y, float radiusX, float radiusY);

}
