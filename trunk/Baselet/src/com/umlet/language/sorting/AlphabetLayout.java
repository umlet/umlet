package com.umlet.language.sorting;

import java.util.Comparator;
import java.util.List;

import com.baselet.control.config.ConfigConst;
import com.umlet.language.java.JavaClass;

public class AlphabetLayout extends Layout {

	@Override
	public void layout(List<SortableElement> elements) {
		super.simpleLayout(new AlphabetSorter(), elements);
	}

	public static String getClassName(JavaClass parsedClass) {
		String result = "";
		if (ConfigConst.generateClassPackage) {
			result += parsedClass.getPackage() + "::";
		}
		result += parsedClass.getName();
		return result;
	}

	private static class AlphabetSorter implements Comparator<SortableElement> {

		@Override
		public int compare(SortableElement e1, SortableElement e2) {
			return getClassName(e1.getParsedClass()).compareTo(getClassName(e2.getParsedClass()));
		}
	}
}
