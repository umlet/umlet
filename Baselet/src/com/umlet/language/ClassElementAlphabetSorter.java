package com.umlet.language;

import java.util.Comparator;

public class ClassElementAlphabetSorter implements Comparator<ClassElement> {

	@Override
	public int compare(ClassElement e1, ClassElement e2) {
		return ClassDiagramConverter.getClassName(e1.getParsedClass()).compareTo(ClassDiagramConverter.getClassName(e2.getParsedClass()));
	}
}
