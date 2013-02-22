package com.baselet.client;

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

	CssColor red = CssColor.make("rgba(" + 255 + ", " + 0 + "," + 0 + ", " + 1.0 + ")");

	public DrawPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		Canvas diagramCanvas = makeCanvas(1500, 1500);
		diagramTabPanel.add(new ScrollPanel(diagramCanvas),"Tab-CANVAS"); 
		diagramTabPanel.add(new HTML("ONE")," Tab-1 ");
		diagramTabPanel.add(new HTML("TWO")," Tab-2 ");
		diagramTabPanel.add(new HTML("THREE")," Tab-3 "); 

		paletteChooser.addItem("A");
		paletteChooser.addItem("B");
		paletteChooser.addItem("C");

		Canvas paletteCanvas = makeCanvas(3000, 3000);
		palettePanel.add(paletteCanvas);
	}

	private Canvas makeCanvas(int width, int height) {
		final Canvas canvas = Canvas.createIfSupported();
		canvas.setStyleName("canvas");
		//		canvas.setWidth(width + "px");
		canvas.setCoordinateSpaceWidth(width);
		//		canvas.setHeight(height + "px");
		canvas.setCoordinateSpaceHeight(height);

		final Context2d context = canvas.getContext2d();

		MouseDragUtils.addMouseDragHandler(canvas, new MouseDragHandler() {
			@Override
			public void onMouseDrag(int diffX, int diffY) {
				context.translate(diffX, diffY);
				draw(context);
			}
		});

//		canvas.addMouseWheelHandler(new MouseWheelHandler() {
//			@Override
//			public void onMouseWheel(MouseWheelEvent event) {
//				float zoom;
//				if(event.getDeltaY() < 0) { // scrolling up = negative values, scrolling down = positive values
//					zoom = 1.1f;
//				} else {
//					zoom = 0.9f;
//				}
//				context.scale(zoom,zoom);
//			}
//		});

		draw(context);

		return canvas;
	}

	private void draw(final Context2d context) {
		context.clearRect(-100000, -100000, 200000, 200000);
		context.setFillStyle(red);
		context.fillRect(0, 0, 30, 30);
		context.fill();
	}
}
