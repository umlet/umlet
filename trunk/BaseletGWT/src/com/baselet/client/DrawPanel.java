package com.baselet.client;

import org.vectomatic.dnd.DropPanel;
import org.vectomatic.file.FileUploadExt;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootLayoutPanel;
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
	
	@UiField
	MenuItem openMenuItem;

	@UiField
	MenuItem exportJpgMenuItem;
	
	@UiField
	MenuItem exportPngMenuItem;

	@UiField
	MenuItem exportUxfMenuItem;
	
	private FileUploadExt hiddenUploadButton = new FileUploadExt();
	private FileOpenHandler handler;

	public DrawPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		final DrawPanelCanvas diagramHandler = new DrawPanelCanvas();
		handler = new FileOpenHandler(diagramHandler);
		
		diagramTabPanel.add(new AutoResizeScrollDropPanel(diagramHandler),"Tayb-yCxANxVAS"); 
		diagramTabPanel.add(new HTML("ONE")," Tab-1 ");
		diagramTabPanel.add(new HTML("TWO")," Tab-2 ");
		diagramTabPanel.add(new HTML("THREE")," Tab-3 "); 

		paletteChooser.addItem("A");
		paletteChooser.addItem("B");
		paletteChooser.addItem("C");

		DrawPanelCanvas paletteCanvas = new DrawPanelCanvas();
		palettePanel.add(new AutoResizeScrollDropPanel(paletteCanvas));
		
		RootLayoutPanel.get().add(hiddenUploadButton);
		hiddenUploadButton.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				handler.processFiles(hiddenUploadButton.getFiles());
			}
		});

		openMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				hiddenUploadButton.click();
				handler.processFiles(hiddenUploadButton.getFiles());
			}
		});

		exportPngMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				String exportUrl = diagramHandler.getCanvas().toDataUrl("image/png");
				Window.open(exportUrl, "_blank", "");
			}
		});

		exportJpgMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				String exportUrl = diagramHandler.getCanvas().toDataUrl("image/jpeg");
				Window.open(exportUrl, "_blank", "");
			}
		});
	}
	
	

}
