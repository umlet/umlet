// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.base.relation;

import java.awt.Rectangle;

@SuppressWarnings("serial")
public class EmptyShape extends Rectangle {
	public EmptyShape(int fontsize) {
		super(0, 0, fontsize, fontsize);
	}
}
