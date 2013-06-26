package com.baselet.gwt.client.view;

import org.apache.log4j.Logger;
import org.vectomatic.file.FileUploadExt;

import com.baselet.gwt.client.BrowserStorage;
import com.baselet.gwt.client.OwnXMLParser;
import com.baselet.gwt.client.Utils;
import com.baselet.gwt.client.view.SaveDialogBox.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainView extends Composite {

	private static MainViewUiBinder uiBinder = GWT.create(MainViewUiBinder.class);

	interface MainViewUiBinder extends UiBinder<Widget, MainView> {}

	@UiField
	SplitLayoutPanel diagramPaletteSplitter;

	@UiField(provided=true)
	SplitLayoutPanel palettePropertiesSplitter = new SplitLayoutPanel() {
		public void onResize() {
			diagramScrollPanel.updateCanvasMinimalSize();
			paletteScrollPanel.updateCanvasMinimalSize();
		};
	};

	@UiField
	DockLayoutPanel paletteChooserCanvasSplitter;

	@UiField
	SimpleLayoutPanel diagramPanel;

	@UiField
	ListBox paletteChooser;

	@UiField
	OwnTextArea propertiesPanel;

	@UiField
	SimpleLayoutPanel palettePanel;

	@UiField
	MenuItem newMenuItem;

	@UiField
	MenuItem saveMenuItem;

	@UiField
	MenuBar restoreMenuBar;

	@UiField
	MenuItem importMenuItem;
	
	@UiField
	MenuItem exportMenuItem;

	@UiField
	MenuItem deleteMenuItem;

	@UiField
	MenuItem cutMenuItem;

	@UiField
	MenuItem copyMenuItem;

	@UiField
	MenuItem pasteMenuItem;
	
	private DrawFocusPanel diagramHandler;
	private AutoResizeScrollDropPanel diagramScrollPanel;

	private DrawFocusPanel paletteHandler;
	private AutoResizeScrollDropPanel paletteScrollPanel;

	private FileUploadExt hiddenUploadButton = new FileUploadExt();
	private FileOpenHandler handler;

	private Logger log = Logger.getLogger(MainView.class);

	private String DEFAULT_UXF = "<diagram program=\"umlet_web\" version=\"12.2\"><zoom_level>10</zoom_level><element><id>UMLClass</id><coordinates><x>10</x><y>100</y><w>400</w><h>60</h></coordinates><panel_attributes>This class has the setting&#10;*elementstyle=autoresize*&#10;--&#10;Write text and watch how the class automatically grows/shrinks&#10;elementstyle=autoresize</panel_attributes><additional_attributes></additional_attributes></element><element><id>UMLUseCase</id><coordinates><x>10</x><y>280</y><w>210</w><h>120</h></coordinates><panel_attributes>This usecase has&#10;custom colors and linetype&#10;--&#10;*fg=#5c2b00*&#10;*bg=orange*&#10;*lt=.*&#10;fg=#5c2b00&#10;bg=orange&#10;lt=.</panel_attributes><additional_attributes></additional_attributes></element><element><id>UMLClass</id><coordinates><x>10</x><y>170</y><w>210</w><h>90</h></coordinates><panel_attributes>This class has the setting&#10;*elementstyle=wordwrap*&#10;--&#10;Write text and watch how the linebreak is added automatically at the expected position to fill the whole class.&#10;&#10;You can also resize the class and see that the text will always fit the border&#10;elementstyle=wordwrap</panel_attributes><additional_attributes></additional_attributes></element><element><id>UMLClass</id><coordinates><x>10</x><y>10</y><w>400</w><h>80</h></coordinates><panel_attributes>This palette contains the new grid elements UMLet offers since v12 (at the moment Class and UseCase).&#10;&#10;Press Ctrl+Space to open the possible settings to customize your elements as you can see below.&#10;elementstyle=wordwrap</panel_attributes><additional_attributes></additional_attributes></element><element><id>UMLUseCase</id><coordinates><x>230</x><y>280</y><w>180</w><h>120</h></coordinates><panel_attributes>this usecase has&#10;*halign=left*&#10;--&#10;As you can see the&#10;text is always within the&#10;usecase circle&#10;&#10;halign=LEFT</panel_attributes><additional_attributes></additional_attributes></element><element><id>UMLClass</id><coordinates><x>230</x><y>170</y><w>180</w><h>90</h></coordinates><panel_attributes>This class has the settings&#10;*valign=center*&#10;*halign=center*&#10;*fontsize=12*&#10;*lth=2.5*&#10;valign=center&#10;halign=center&#10;fontsize=12&#10;lth=2.5</panel_attributes><additional_attributes></additional_attributes></element><element><id>Relation</id><coordinates><x>20</x><y>430</y><w>180</w><h>120</h></coordinates><panel_attributes>relation</panel_attributes><additional_attributes>20;60;80;60;120;20</additional_attributes></element></diagram>";

	private ScheduledCommand saveCommand = new ScheduledCommand() {
		private SaveDialogBox saveDialogBox = new SaveDialogBox(new Callback() {
			@Override
			public void callback(final String chosenName) {
				BrowserStorage.addSavedDiagram(chosenName, diagramHandler.toXml());
				addRestoreMenuItem(chosenName);
			}
		});
		@Override
		public void execute() {
			saveDialogBox.clearAndCenter();
		}
	};
	
	public ScheduledCommand getSaveCommand() {
		return saveCommand;
	}

	public MainView() {
		initWidget(uiBinder.createAndBindUi(this));
		diagramPaletteSplitter.setWidgetToggleDisplayAllowed(palettePropertiesSplitter, true);
		palettePropertiesSplitter.setWidgetToggleDisplayAllowed(paletteChooserCanvasSplitter, true);
		diagramHandler = new DrawFocusPanel(this, propertiesPanel);
		diagramScrollPanel = new AutoResizeScrollDropPanel(diagramHandler);
		paletteHandler = new DrawFocusPanel(this, propertiesPanel);
		paletteScrollPanel = new AutoResizeScrollDropPanel(paletteHandler);
		
		for (String diagramName : BrowserStorage.getSavedDiagramKeys()) {
			addRestoreMenuItem(diagramName);
		}

		log.trace("Main View initialized");

		handler = new FileOpenHandler(diagramHandler);

		diagramPanel.add(diagramScrollPanel); 

		paletteChooser.addItem("A");
		paletteChooser.addItem("B");
		paletteChooser.addItem("C");

		palettePanel.add(paletteScrollPanel);

		diagramHandler.setGridElements(OwnXMLParser.xmlToGridElements(DEFAULT_UXF, diagramHandler.getSelector()));
		paletteHandler.setGridElements(OwnXMLParser.xmlToGridElements(DEFAULT_UXF, paletteHandler.getSelector()));

		RootLayoutPanel.get().add(hiddenUploadButton);
		hiddenUploadButton.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				handler.processFiles(hiddenUploadButton.getFiles());
			}
		});

		newMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				Window.open(Window.Location.getQueryString(),"_blank","");
			}
		});

		importMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				hiddenUploadButton.click();
			}
		});

		saveMenuItem.setScheduledCommand(saveCommand );

		exportMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				// use base64 encoding to make it work in firefox (one alternative would be encoding <,>,... like the following website does: http://dopiaza.org/tools/datauri/index.php)
				String uxfUrl = "data:text/plain;charset=utf-8;base64," + Utils.b64encode(diagramHandler.toXml());
				String pngUrl = diagramHandler.getCanvas().toDataUrl("image/png");
				new DownloadPopupPanel(uxfUrl, pngUrl);
			}
		});

		deleteMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				diagramHandler.getCommandInvoker().removeSelectedElements();
			}
		});

		cutMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				diagramHandler.getCommandInvoker().cutSelectedElements();
			}
		});

		copyMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				diagramHandler.getCommandInvoker().copySelectedElements();
				}
		});

		pasteMenuItem.setScheduledCommand(new ScheduledCommand() {
			@Override
			public void execute() {
				diagramHandler.getCommandInvoker().pasteElements();
				}
		});
		
	}

	private void addRestoreMenuItem(final String chosenName) {
		restoreMenuBar.addItem(new MenuItem(chosenName, false, new ScheduledCommand() {
			@Override
			public void execute() {
				diagramHandler.setGridElements(OwnXMLParser.xmlToGridElements(BrowserStorage.getSavedDiagram(chosenName), diagramHandler.getSelector()));
			}
		}));
	}


}
