package com.umlet.language;

import com.baselet.element.GridElement;
import com.umlet.language.java.JavaClass;

public class ClassElement {

	private GridElement element;
	private JavaClass parsedClass;
	
	public ClassElement(GridElement element, JavaClass parsedClass) {
		this.element = element;
		this.parsedClass = parsedClass;
	}
	
	public GridElement getElement() {
		return element;
	}
	
	public JavaClass getParsedClass() {
		return parsedClass;
	}
}
