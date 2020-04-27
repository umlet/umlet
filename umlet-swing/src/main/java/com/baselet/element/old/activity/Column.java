package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class Column {

	private ArrayList<Element> elements;
	private Graphics2D graphics;

	public Column(Graphics2D graphics) {
		elements = new ArrayList<Element>();
		this.graphics = graphics;
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public Element getFirstElement() {
		if (!elements.isEmpty()) {
			return elements.get(0);
		}
		return null;
	}

	public Element getLastElement() {
		if (!elements.isEmpty()) {
			return elements.get(elements.size() - 1);
		}
		return null;
	}

	public void addElement(Element e) {
		elements.add(e);
	}

	public int getHeight() {
		int height = 0;
		for (Element e : elements) {
			height += e.getHeight() + e.getPadding() * 2;
		}
		return height;
	}

	public int getLeftWidth() {
		int width = 0;
		for (Element e : elements) {
			int w = e.getLeftWidth();
			if (w > width) {
				width = w;
			}
		}
		return width;
	}

	public int getRightWidth() {
		int width = 0;
		for (Element e : elements) {
			int w = e.getRightWidth();
			if (w > width) {
				width = w;
			}
		}
		return width;
	}

	public int getWidth() {
		return getLeftWidth() + getRightWidth();
	}

	public void setX(int x) {
		for (Element e : elements) {
			e.setX(x);
		}
	}

	public void paint() {
		Element current = null;
		for (Element e : elements) {
			if (current != null) {
				new Connector(graphics, current, e).paint();
			}
			e.paint();
			current = e;
		}
	}

	public void printData(String prefix) {
		for (Element e : elements) {
			e.printData(prefix);
		}
	}
}
