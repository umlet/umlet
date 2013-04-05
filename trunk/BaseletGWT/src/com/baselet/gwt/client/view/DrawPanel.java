package com.baselet.gwt.client.view;

import java.util.List;

import org.apache.log4j.Logger;
import org.vectomatic.file.FileUploadExt;

import com.baselet.element.GridElement;
import com.baselet.gwt.client.EventBus;
import com.baselet.gwt.client.OwnXMLParser;
import com.baselet.gwt.client.EventBus.PropertiesTextChanged;
import com.baselet.gwt.client.view.OwnTextArea.InstantValueChangeHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
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
	OwnTextArea propertiesPanel;

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
	
	private EventBus eventBus = EventBus.getInstance();

	private DrawPanelCanvas diagramHandler = new DrawPanelCanvas();
	private AutoResizeScrollDropPanel diagramScrollPanel = new AutoResizeScrollDropPanel(diagramHandler);

	private DrawPanelCanvas paletteHandler = new DrawPanelCanvas();
	private AutoResizeScrollDropPanel paletteScrollPanel = new AutoResizeScrollDropPanel(paletteHandler);
	
	private FileUploadExt hiddenUploadButton = new FileUploadExt();
	private FileOpenHandler handler;
	
	private Storage localStorage = Storage.getLocalStorageIfSupported();

	private Logger log = Logger.getLogger(DrawPanel.class);
	
	private String DEFAULT_UXF = "<diagram program=\"umlet_web\" version=\"12.2\"><zoom_level>10</zoom_level><element><id>UMLClass</id><coordinates><x>10</x><y>100</y><w>400</w><h>60</h></coordinates><panel_attributes>This class has the setting&#10;*elementstyle=autoresize*&#10;--&#10;Write text and watch how the class automatically grows/shrinks&#10;elementstyle=autoresize</panel_attributes><additional_attributes></additional_attributes></element><element><id>UMLUseCase</id><coordinates><x>10</x><y>280</y><w>210</w><h>120</h></coordinates><panel_attributes>This usecase has&#10;custom colors and linetype&#10;--&#10;*fg=#5c2b00*&#10;*bg=orange*&#10;*lt=.*&#10;fg=#5c2b00&#10;bg=orange&#10;lt=.</panel_attributes><additional_attributes></additional_attributes></element><element><id>UMLClass</id><coordinates><x>10</x><y>170</y><w>210</w><h>90</h></coordinates><panel_attributes>This class has the setting&#10;*elementstyle=wordwrap*&#10;--&#10;Write text and watch how the linebreak is added automatically at the expected position to fill the whole class.&#10;&#10;You can also resize the class and see that the text will always fit the border&#10;elementstyle=wordwrap</panel_attributes><additional_attributes></additional_attributes></element><element><id>UMLClass</id><coordinates><x>10</x><y>10</y><w>400</w><h>80</h></coordinates><panel_attributes>This palette contains the new grid elements UMLet offers since v12 (at the moment Class and UseCase).&#10;&#10;Press Ctrl+Space to open the possible settings to customize your elements as you can see below.&#10;elementstyle=wordwrap</panel_attributes><additional_attributes></additional_attributes></element><element><id>UMLUseCase</id><coordinates><x>230</x><y>280</y><w>180</w><h>120</h></coordinates><panel_attributes>this usecase has&#10;*halign=left*&#10;--&#10;As you can see the&#10;text is always within the&#10;usecase circle&#10;&#10;halign=LEFT</panel_attributes><additional_attributes></additional_attributes></element><element><id>UMLClass</id><coordinates><x>230</x><y>170</y><w>180</w><h>90</h></coordinates><panel_attributes>This class has the settings&#10;*valign=center*&#10;*halign=center*&#10;*fontsize=12*&#10;*lth=2.5*&#10;valign=center&#10;halign=center&#10;fontsize=12&#10;lth=2.5</panel_attributes><additional_attributes></additional_attributes></element><element><id>Relation</id><coordinates><x>20</x><y>430</y><w>180</w><h>120</h></coordinates><panel_attributes>relation</panel_attributes><additional_attributes>24;64;80;64;120;24</additional_attributes></element></diagram>";
	
	public DrawPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		propertiesPanel.addInstantValueChangeHandler(new InstantValueChangeHandler() {
			@Override
			public void onValueChange(String value) {
				eventBus.fireEvent(new EventBus.PropertiesTextChanged(value));
			}
		});
		
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

		diagramHandler.setGridElements(OwnXMLParser.parse(DEFAULT_UXF));
		paletteHandler.setGridElements(OwnXMLParser.parse(DEFAULT_UXF));
		
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
				if (localStorage == null) throw new RuntimeException("The Browser doesn't support saving files");
				localStorage.setItem("A", diagramHandler.toXml());
			}
		});

		restoreMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				if (localStorage == null) throw new RuntimeException("The Browser doesn't support saving files");
				List<GridElement> storedGridElements = OwnXMLParser.parse(localStorage.getItem("A"));
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
