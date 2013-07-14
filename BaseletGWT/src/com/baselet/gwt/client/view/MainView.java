package com.baselet.gwt.client.view;

import org.apache.log4j.Logger;
import org.vectomatic.file.FileUploadExt;

import com.baselet.gwt.client.BrowserStorage;
import com.baselet.gwt.client.OwnXMLParser;
import com.baselet.gwt.client.Utils;
import com.baselet.gwt.client.view.widgets.DownloadPopupPanel;
import com.baselet.gwt.client.view.widgets.PropertiesTextArea;
import com.baselet.gwt.client.view.widgets.SaveDialogBox;
import com.baselet.gwt.client.view.widgets.SaveDialogBox.Callback;
import com.baselet.gwt.client.view.widgets.ShortcutDialogBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainView extends Composite {

	private static MainViewUiBinder uiBinder = GWT.create(MainViewUiBinder.class);

	interface MainViewUiBinder extends UiBinder<Widget, MainView> {}

	interface MyStyle extends CssResource {
		String menuItem();
	}

	@UiField
	MyStyle style;

	@UiField(provided=true)
	SplitLayoutPanel diagramPaletteSplitter = new SplitLayoutPanel(4);

	@UiField
	FlowPanel menuPanel;

	@UiField
	FlowPanel restoreMenuPanel;

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
	SimpleLayoutPanel diagramPanelWrapper;

	@UiField
	ListBox paletteChooser;

	@UiField
	PropertiesTextArea propertiesPanel;

	@UiField
	SimpleLayoutPanel palettePanelWrapper;

	private DrawFocusPanel diagramPanel;
	private AutoResizeScrollDropPanel diagramScrollPanel;

	private DrawFocusPanel palettePanel;
	private AutoResizeScrollDropPanel paletteScrollPanel;

	private FileUploadExt hiddenUploadButton = new FileUploadExt();
	private FileOpenHandler handler;

	private Logger log = Logger.getLogger(MainView.class);

	private ScheduledCommand saveCommand = new ScheduledCommand() {
		private SaveDialogBox saveDialogBox = new SaveDialogBox(new Callback() {
			@Override
			public void callback(final String chosenName) {
				BrowserStorage.addSavedDiagram(chosenName, diagramPanel.toXml());
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
		diagramPaletteSplitter.setWidgetToggleDisplayAllowed(menuPanel, true);
		diagramPaletteSplitter.setWidgetSnapClosedSize(menuPanel, 40);
		palettePropertiesSplitter.setWidgetToggleDisplayAllowed(paletteChooserCanvasSplitter, true);
		diagramPanel = new DrawFocusPanelDiagram(this, propertiesPanel);
		palettePanel = new DrawFocusPanelPalette(this, propertiesPanel, paletteChooser);
		diagramPanel.setOtherDrawFocusPanel(palettePanel);
		palettePanel.setOtherDrawFocusPanel(diagramPanel);
		diagramScrollPanel = new AutoResizeScrollDropPanel(diagramPanel);
		paletteScrollPanel = new AutoResizeScrollDropPanel(palettePanel);

		for (String diagramName : BrowserStorage.getSavedDiagramKeys()) {
			addRestoreMenuItem(diagramName);
		}

		log.trace("Main View initialized");

		handler = new FileOpenHandler(diagramPanel);

		diagramPanelWrapper.add(diagramScrollPanel);

		palettePanelWrapper.add(paletteScrollPanel);

		RootLayoutPanel.get().add(hiddenUploadButton);
		hiddenUploadButton.setVisible(false);
		hiddenUploadButton.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				handler.processFiles(hiddenUploadButton.getFiles());
			}
		});
	}

	private void addRestoreMenuItem(final String chosenName) {
		Label label = new Label(chosenName);
		label.addStyleName(style.menuItem());
		label.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				diagramPanel.setGridElements(OwnXMLParser.xmlToGridElements(BrowserStorage.getSavedDiagram(chosenName)));
			}
		});
		restoreMenuPanel.add(label);
	}


	@UiHandler("newMenuItem")
	void onNewMenuItemClick(ClickEvent event) {
		Window.open(Window.Location.getQueryString(),"_blank","");
	}

	@UiHandler("importMenuItem")
	void onImportMenuItemClick(ClickEvent event) {
		hiddenUploadButton.click();
	}

	@UiHandler("exportMenuItem")
	void onExportMenuItemClick(ClickEvent event) {
		// use base64 encoding to make it work in firefox (one alternative would be encoding <,>,... like the following website does: http://dopiaza.org/tools/datauri/index.php)
		String uxfUrl = "data:text/plain;charset=utf-8;base64," + Utils.b64encode(diagramPanel.toXml());
		String pngUrl = diagramPanel.getCanvas().toDataUrl("image/png");
		new DownloadPopupPanel(uxfUrl, pngUrl);
	}

	@UiHandler("saveMenuItem")
	void onSaveMenuItemClick(ClickEvent event) {
		saveCommand.execute();
	}

	@UiHandler("helpMenuItem")
	void onHelpMenuItemClick(ClickEvent event) {
		ShortcutDialogBox.getInstance().center();
	}
}
