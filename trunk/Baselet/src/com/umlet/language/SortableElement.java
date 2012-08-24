package com.umlet.language;

import com.baselet.element.GridElement;
import com.umlet.language.java.JavaClass;

public class SortableElement {

	private GridElement element;
	private JavaClass parsedClass;
	
	public SortableElement(GridElement element) {
		this.element = element;
	}
	
	public SortableElement(GridElement element, JavaClass parsedClass) {
		this(element);
		this.parsedClass = parsedClass;
	}
	
	public GridElement getElement() {
		return element;
	}
	
	public JavaClass getParsedClass() {
		return parsedClass;
	}
}
