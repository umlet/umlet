package com.umlet.language;

import com.baselet.element.GridElement;
import com.umlet.language.java.JavaClass;

public class SortableElement implements Comparable<SortableElement> {

	private GridElement element;
	private JavaClass parsedClass;
	private String name;

	public SortableElement(GridElement element, String name) {
		this.element = element;
		this.name = name;
	}
	
	public SortableElement(GridElement element, JavaClass parsedClass) {
		this(element, parsedClass.getPackage());
		this.parsedClass = parsedClass;
	}
	
	public GridElement getElement() {
		return element;
	}
	
	public JavaClass getParsedClass() {
		return parsedClass;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public int compareTo(SortableElement o) {
		return this.name.compareTo(o.name);
	}
}
