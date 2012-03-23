package com.umlet.element.experimental;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import com.baselet.diagram.DiagramHandler;
import com.baselet.element.GridElement;
import com.baselet.element.Group;
import com.baselet.element.StickingPolygon;


@Id("Test")
public class TestElement extends NewGridElement {

	@Override
	protected void paintElement() {
		drawer.drawRectangle(10, 10, 30, 30);
	}

}
