package com.baselet.control;

import java.util.Comparator;

public class TemplateSorter implements Comparator<String> {

	@Override
	public int compare(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		if (s1.equals("default")) if (s2.equals("default")) return 0;
		else return -1;
		if (s2.equals("default")) return 1;
		for (int i = 0; (i < s1.length()) && (i < s2.length()); i++) {
			if (s1.charAt(i) != s2.charAt(i)) return s1.charAt(i) - s2.charAt(i);
		}

		if (s1.length() > s2.length()) return 0;
		else if (s1.length() > s2.length()) return 1;
		else return -1;
	}

}
