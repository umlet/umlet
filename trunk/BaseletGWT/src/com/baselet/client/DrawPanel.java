package com.baselet.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class DrawPanel extends Composite {

	private static DrawPanelUiBinder uiBinder = GWT.create(DrawPanelUiBinder.class);

	interface DrawPanelUiBinder extends UiBinder<Widget, DrawPanel> {}

	@UiField
	TabLayoutPanel diagramTabPanel;

	@UiField
	ListBox paletteChooser;

	@UiField
	TextArea propertiesPanel;

	@UiField(provided=true)
	Canvas paletteCanvas = Canvas.createIfSupported();

	Canvas diagramCanvas = Canvas.createIfSupported();

	CssColor red = CssColor.make("rgba(" + 255 + ", " + 0 + "," + 0 + ", " + 1.0 + ")");

	static final int canvasHeight = 300;
	static final int canvasWidth = 300;

	public DrawPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		diagramTabPanel.add(diagramCanvas,"Tab-CANVAS"); 
		diagramTabPanel.add(new HTML("ONE")," Tab-1 ");
		diagramTabPanel.add(new HTML("TWO")," Tab-2 ");
		diagramTabPanel.add(new HTML("THREE")," Tab-3 "); 

		paletteChooser.addItem("A");
		paletteChooser.addItem("B");
		paletteChooser.addItem("C");

		initAndDrawOnCanvas();
	}

	private void initAndDrawOnCanvas() {
		diagramCanvas.setStyleName("mainCanvas");	

		diagramCanvas.setWidth(canvasWidth + "px");
		diagramCanvas.setCoordinateSpaceWidth(canvasWidth);

		diagramCanvas.setHeight(canvasHeight + "px");
		diagramCanvas.setCoordinateSpaceHeight(canvasHeight);

		Context2d context = diagramCanvas.getContext2d();
		context.setFillStyle(red);
		context.fillRect(0, 0, canvasWidth, canvasHeight);
		context.fill();
	}

}
