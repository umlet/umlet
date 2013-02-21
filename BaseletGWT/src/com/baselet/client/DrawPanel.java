package com.baselet.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class DrawPanel extends Composite {

	private static DrawPanelUiBinder uiBinder = GWT.create(DrawPanelUiBinder.class);

	interface DrawPanelUiBinder extends UiBinder<Widget, DrawPanel> {}

	@UiField
	TabLayoutPanel tabPanel;

	public DrawPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		tabPanel.add(new HTML("ONE")," Tab-1 ");
		tabPanel.add(new HTML("TWO")," Tab-2 ");
		tabPanel.add(new HTML("THREE")," Tab-3 "); 
	}

}
