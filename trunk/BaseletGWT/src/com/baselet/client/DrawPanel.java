package com.baselet.client;

import java.util.ArrayList;
import java.util.List;

import com.baselet.client.MouseDragUtils.MouseDragHandler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
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

	@UiField
	ScrollPanel palettePanel;

	public DrawPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		Canvas diagramCanvas = new DrawPanelCanvas().makeCanvas(1500, 1500);
		diagramTabPanel.add(new ScrollPanel(diagramCanvas),"Tab-CANVAS"); 
		diagramTabPanel.add(new OwnDropPanel().getDropPanel(),"DROP-CANVAS"); 
		diagramTabPanel.add(new HTML("ONE")," Tab-1 ");
		diagramTabPanel.add(new HTML("TWO")," Tab-2 ");
		diagramTabPanel.add(new HTML("THREE")," Tab-3 "); 

		paletteChooser.addItem("A");
		paletteChooser.addItem("B");
		paletteChooser.addItem("C");

		Canvas paletteCanvas = new DrawPanelCanvas().makeCanvas(1500, 1500);
		palettePanel.add(paletteCanvas);
	}

}
