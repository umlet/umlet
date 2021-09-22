package com.baselet.gwt.client.view;

import com.baselet.control.StringStyle;
import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.gwt.client.text.Font;
import com.google.gwt.canvas.dom.client.*;
import com.google.gwt.dom.client.CanvasElement;

import java.util.ArrayList;

public interface Context2dWrapper {

	void setFillStyle(FillStrokeStyle fillStyle);

	void translate(double x, double y);

	void drawImage(CanvasElement image, double dx, double dy);

	void fillRect(double x, double y, double w, double h);

	void clearRect(double x, double y, double w, double h);

	void setTransform(int m11, int m12, int m21, int m22, int dx, int dy);

	void scale(double x, double y);

	Font getFont();

	double measureText(String text);

	void setFont(double fontSize, StringStyle stringStyle);

	void setFont(Font font);

	void save();

	void beginPath();

	void moveTo(double x, double y);

	void arc(double x, double y, double radius, double startAngle, double endAngle, boolean anticlockwise);

	void closePath();

	void restore();

	void fill();

	void stroke();

	void arc(double x, double y, double radius, double startAngle, double endAngle);

	void setStrokeStyle(FillStrokeStyle strokeStyle);

	void setLineWidth(double lineWidth);

	void bezierCurveTo(double cp1x, double cp1y, double cp2x, double cp2y, double x, double y);

	void setTextAlign(Context2d.TextAlign ctxAlign);

	void fillText(String text, Double x, Double y);

	void rect(double x, double y, int width, int height);

	void lineTo(double x, double y);

	void quadraticCurveTo(double cpx, double cpy, double x, double y);

	void setLineDash(double dash);
}
