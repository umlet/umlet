package com.umlet.element.experimental;



@Id("Test")
public class TestElement extends NewGridElement {

	@Override
	protected void paintElement() {
		drawer.drawRectangle(10, 10, 30, 30);
	}

}
