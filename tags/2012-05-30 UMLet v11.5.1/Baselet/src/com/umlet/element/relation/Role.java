package com.umlet.element.relation;

import java.awt.Rectangle;

@SuppressWarnings("serial")
public class Role extends Rectangle {
	String _string;

	public String getString() {
		return _string;
	}

	public Role(String s, int a, int b, int c, int d) {
		super(a, b, c, d);
		_string = s;
	}
}
