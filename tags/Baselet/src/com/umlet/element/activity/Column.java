package com.umlet.element.activity;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class Column {

	private ArrayList<Element> elements;
	private Graphics2D graphics;

	public Column(Graphics2D graphics) {
		this.elements = new ArrayList<Element>();
		this.graphics = graphics;
	}

	public boolean isEmpty() {
		return this.elements.isEmpty();
	}

	public Element getFirstElement() {
		if (!this.elements.isEmpty()) return this.elements.get(0);
		return null;
	}

	public Element getLastElement() {
		if (!this.elements.isEmpty()) return this.elements.get(this.elements.size() - 1);
		return null;
	}

	public void addElement(Element e) {
		this.elements.add(e);
	}

	public int getHeight() {
		int height = 0;
		for (Element e : this.elements)
			height += e.getHeight() + e.getPadding() * 2;
		return height;
	}

	public int getLeftWidth() {
		int width = 0;
		for (Element e : this.elements) {
			int w = e.getLeftWidth();
			if (w > width) width = w;
		}
		return width;
	}

	public int getRightWidth() {
		int width = 0;
		for (Element e : this.elements) {
			int w = e.getRightWidth();
			if (w > width) width = w;
		}
		return width;
	}

	public int getWidth() {
		return this.getLeftWidth() + this.getRightWidth();
	}

	public void setX(int x) {
		for (Element e : this.elements)
			e.setX(x);
	}

	public void paint() {
		Element current = null;
		for (Element e : this.elements) {
			if (current != null) (new Connector(this.graphics, current, e)).paint();
			e.paint();
			current = e;
		}
	}

	public void printData(String prefix) {
		for (Element e : this.elements)
			e.printData(prefix);
	}
}
