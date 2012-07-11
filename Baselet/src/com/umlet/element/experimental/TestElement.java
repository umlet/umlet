package com.umlet.element.experimental;

import com.umlet.element.experimental.drawable.Rectangle;
import com.umlet.element.experimental.facet.Facets;



@Id("Test")
public class TestElement extends NewGridElement {

	@Override
	public void updateModelFromText() {
		drawables.clear();
		drawables.add(new Rectangle(0, 0, getRealSize().width-1, getRealSize().height-1));
		drawables.addAll(Facets.getClassDrawables(this.getPanelAttributes(), drawer.textHeight()+2, getRealSize().width-1));
	}

}

