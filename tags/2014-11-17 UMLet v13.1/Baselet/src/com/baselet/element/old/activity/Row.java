package com.baselet.element.old.activity;

import java.util.ArrayList;

public class Row {

	private ArrayList<Element> elements;

	public Row() {
		elements = new ArrayList<Element>();
	}

	public void exchangeElementOrInsert(Element old_element, Element new_element) {
		if (elements.contains(old_element)) {
			elements.set(elements.indexOf(old_element), new_element);
		}
		else {
			addElement(new_element);
		}
		new_element.setRow(this);
	}

	public boolean isLeft(Element e) {
		if (e.equals(elements.get(0))) {
			return true;
		}
		return false;
	}

	public boolean isRight(Element e) {
		if (e.equals(elements.get(elements.size() - 1))) {
			return true;
		}
		return false;
	}

	public ArrayList<Row> makeExclusiveLeft(Element e, ArrayList<Row> rows) {
		Row new_row = new Row();
		int index = elements.indexOf(e);
		for (int i = 0; i < index; i++) {
			new_row.addElement(elements.get(0));
			elements.remove(0);
		}
		rows.add(rows.indexOf(this) + 1, new_row);
		return rows;
	}

	public ArrayList<Row> makeExclusiveRight(Element e, ArrayList<Row> rows) {
		Row new_row = new Row();
		int index = elements.indexOf(e);
		int size = elements.size();
		for (int i = index + 1; i < size; i++) {
			new_row.addElement(elements.get(index + 1));
			elements.remove(index + 1);
		}
		rows.add(rows.indexOf(this) + 1, new_row);
		return rows;
	}

	public void addElement(Element e) {
		elements.add(e);
		e.setRow(this);
	}

	public int setElementYPosition(int offset) {
		int h = getHeight();
		int y = offset + h / 2;
		for (Element e : elements) {
			e.setY(y);
		}
		return offset + h;
	}

	public int getHeight() {
		int height = 0;
		for (Element e : elements) {
			int h = e.getHeight() + e.getPadding() * 2;
			if (h > height) {
				height = h;
			}
		}
		return height;
	}
}
