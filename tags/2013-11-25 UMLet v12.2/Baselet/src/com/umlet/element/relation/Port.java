package com.umlet.element.relation;

import com.baselet.diagram.draw.geom.Rectangle;

@SuppressWarnings("serial")
public class Port extends Rectangle {

	String _string;

	public String getString() {
		return _string;
	}

	public Port(String s, int a, int b, int c, int d) {
		super(a, b, c, d);

		_string = s;

	}
}
