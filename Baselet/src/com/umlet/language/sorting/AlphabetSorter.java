package com.umlet.language.sorting;

import java.util.Comparator;

import com.umlet.language.ClassDiagramConverter;
import com.umlet.language.SortableElement;

public class AlphabetSorter implements Comparator<SortableElement> {

	@Override
	public int compare(SortableElement e1, SortableElement e2) {
		return ClassDiagramConverter.getClassName(e1.getParsedClass()).compareTo(ClassDiagramConverter.getClassName(e2.getParsedClass()));
	}
}
