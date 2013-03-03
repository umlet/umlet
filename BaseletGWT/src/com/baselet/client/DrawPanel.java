package com.baselet.client;

import java.util.List;

import org.apache.log4j.Logger;
import org.vectomatic.file.FileUploadExt;

import com.baselet.client.element.GridElement;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class DrawPanel extends Composite {

	private static DrawPanelUiBinder uiBinder = GWT.create(DrawPanelUiBinder.class);

	interface DrawPanelUiBinder extends UiBinder<Widget, DrawPanel> {}

	@UiField(provided=true)
	SplitLayoutPanel mainSplit = new SplitLayoutPanel() {
		public void onResize() {
			diagramScrollPanel.updateCanvasMinimalSize();
			paletteScrollPanel.updateCanvasMinimalSize();
		};
	};
	
	@UiField
	TabLayoutPanel diagramTabPanel;

	@UiField
	ListBox paletteChooser;

	@UiField
	TextArea propertiesPanel;

	@UiField
	SimpleLayoutPanel palettePanel;
	
	@UiField
	MenuItem openMenuItem;
	
	@UiField
	MenuItem saveMenuItem;
	
	@UiField
	MenuItem restoreMenuItem;
	
	@UiField
	MenuItem exportMenuItem;

	private DrawPanelCanvas diagramHandler = new DrawPanelCanvas();
	private AutoResizeScrollDropPanel diagramScrollPanel = new AutoResizeScrollDropPanel(diagramHandler);

	private DrawPanelCanvas paletteHandler = new DrawPanelCanvas();
	private AutoResizeScrollDropPanel paletteScrollPanel = new AutoResizeScrollDropPanel(paletteHandler);
	
	private FileUploadExt hiddenUploadButton = new FileUploadExt();
	private FileOpenHandler handler;
	
	private Storage stockStore = Storage.getLocalStorageIfSupported();

	private Logger log = Logger.getLogger(DrawPanel.class);
	
	public DrawPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		log.trace("Main View initialized");

		handler = new FileOpenHandler(diagramHandler);
		
		diagramTabPanel.add(diagramScrollPanel,"Tayb-yCxANxVAS"); 
		diagramTabPanel.add(new HTML("ONE")," Tab-1 ");
		diagramTabPanel.add(new HTML("TWO")," Tab-2 ");
		diagramTabPanel.add(new HTML("THREE")," Tab-3 "); 

		paletteChooser.addItem("A");
		paletteChooser.addItem("B");
		paletteChooser.addItem("C");

		palettePanel.add(paletteScrollPanel);
		
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

		saveMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				if (stockStore == null) throw new RuntimeException("The Browser doesn't support saving files");
				stockStore.setItem("A", diagramHandler.toXml());
			}
		});

		restoreMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				if (stockStore == null) throw new RuntimeException("The Browser doesn't support saving files");
				List<GridElement> storedGridElements = OwnXMLParser.parse(stockStore.getItem("A"));
				diagramHandler.setGridElements(storedGridElements);
			}
		});

		exportMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				String uxfUrl = "data:text/xml;charset=utf-8," + diagramHandler.toXml();
				String pngUrl = diagramHandler.getCanvas().toDataUrl("image/png");
				new DownloadPopupPanel(uxfUrl, pngUrl);
			}
		});
	}

}
