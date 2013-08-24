package com.baselet.gwt.client.view;


import org.apache.log4j.Logger;
import org.vectomatic.file.FileUploadExt;

import com.baselet.gwt.client.BrowserStorage;
import com.baselet.gwt.client.Notification;
import com.baselet.gwt.client.OwnXMLParser;
import com.baselet.gwt.client.view.widgets.DownloadPopupPanel;
import com.baselet.gwt.client.view.widgets.PropertiesTextArea;
import com.baselet.gwt.client.view.widgets.SaveDialogBox;
import com.baselet.gwt.client.view.widgets.SaveDialogBox.Callback;
import com.baselet.gwt.client.view.widgets.ShortcutDialogBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
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
	
	@UiField
	FocusPanel mainPanel;

	@UiField(provided=true)
	SplitLayoutPanel diagramPaletteSplitter = new SplitLayoutPanel(4);

	@UiField
	FlowPanel menuPanel;

	@UiField
	FlowPanel restoreMenuPanel;

	@UiField(provided=true)
	SplitLayoutPanel palettePropertiesSplitter = new SplitLayoutPanel() {
		public void onResize() {
			diagramPanel.redraw();
			palettePanel.redraw();
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
				boolean itemIsNewlyAdded = BrowserStorage.getSavedDiagram(chosenName) == null;
				BrowserStorage.addSavedDiagram(chosenName, OwnXMLParser.diagramToXml(diagramPanel.getDiagram()));
				if (itemIsNewlyAdded) {
					addRestoreMenuItem(chosenName);
				}
				Notification.showInfo("Diagram saved as: " + chosenName);
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
		diagramPaletteSplitter.setWidgetSnapClosedSize(palettePropertiesSplitter, 100);
		diagramPaletteSplitter.setWidgetMinSize(palettePropertiesSplitter, 200);
		diagramPaletteSplitter.setWidgetToggleDisplayAllowed(menuPanel, true);
		diagramPaletteSplitter.setWidgetSnapClosedSize(menuPanel, 25);
		diagramPaletteSplitter.setWidgetMinSize(menuPanel, 50);
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
		
		
		//TODO doesnt work for long strings or < / > in prop panel
//		String base64Diagram = Location.getParameter("open");
//		if (base64Diagram != null) {
//			diagramPanel.setDiagram(OwnXMLParser.xmlToDiagram(base64Diagram));
//			log.info(base64Diagram);
//			String fix = "%3Cdiagram%20program=%22umlet_web%22%20version=%2212.2%22%3E%3Czoom_level%3E10%3C/zoom_level%3E%3Celement%3E%3Cid%3EUMLUseCase%3C/id%3E%3Ccoordinates%3E%3Cx%3E10%3C/x%3E%3Cy%3E310%3C/y%3E%3Cw%3E170%3C/w%3E%3Ch%3E100%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3EThis%20usecase%20has%0Acustom%20colors%20and%20linetype%0A--%0A*fg=%25235c2b00*%0A*bg=orange*%0A*lt=.*%0Afg=%25235c2b00%0Abg=orange%0Alt=.%3C/panel_attributes%3E%3Cadditional_attributes%3E%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3EUMLUseCase%3C/id%3E%3Ccoordinates%3E%3Cx%3E190%3C/x%3E%3Cy%3E310%3C/y%3E%3Cw%3E150%3C/w%3E%3Ch%3E100%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3Ethis%20usecase%20has%0A*halign=left*%0A--%0AAs%20you%20can%20see%20the%0Atext%20is%20always%20within%20the%0Ausecase%20circle%0A%0Ahalign=LEFT%3C/panel_attributes%3E%3Cadditional_attributes%3E%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3EUMLInterface%3C/id%3E%3Ccoordinates%3E%3Cx%3E20%3C/x%3E%3Cy%3E420%3C/y%3E%3Cw%3E70%3C/w%3E%3Ch%3E80%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3EInterface%0A--%0AOperation1%0AOperation2%3C/panel_attributes%3E%3Cadditional_attributes%3E%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3EUMLActor%3C/id%3E%3Ccoordinates%3E%3Cx%3E90%3C/x%3E%3Cy%3E420%3C/y%3E%3Cw%3E70%3C/w%3E%3Ch%3E170%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3ELarge%0AActor%0Alt=..%0Afg=gray%0Afontsize=20%3C/panel_attributes%3E%3Cadditional_attributes%3E%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3EUMLActor%3C/id%3E%3Ccoordinates%3E%3Cx%3E150%3C/x%3E%3Cy%3E420%3C/y%3E%3Cw%3E50%3C/w%3E%3Ch%3E90%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3EActor2%0Abg=red%3C/panel_attributes%3E%3Cadditional_attributes%3E%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3ERelation%3C/id%3E%3Ccoordinates%3E%3Cx%3E190%3C/x%3E%3Cy%3E400%3C/y%3E%3Cw%3E180%3C/w%3E%3Ch%3E120%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3El=&gt;%0Ar=&lt;%3C/panel_attributes%3E%3Cadditional_attributes%3E20.0;60.0;80.0;60.0;120.0;20.0%3C/additional_attributes%3E%3C/element%3E%3Celement%3E%3Cid%3EText%3C/id%3E%3Ccoordinates%3E%3Cx%3E210%3C/x%3E%3Cy%3E490%3C/y%3E%3Cw%3E150%3C/w%3E%3Ch%3E30%3C/h%3E%3C/coordinates%3E%3Cpanel_attributes%3Ethis%20is%20a%20text%20element%3C/panel_attributes%3E%3Cadditional_attributes%3E%3C/additional_attributes%3E%3C/element%3E%3C/diagram%3E";
//		}
	}

	private void addRestoreMenuItem(final String chosenName) {
		final HorizontalPanel hp = new HorizontalPanel();

		Label label = new Label(chosenName);
		label.setTitle("open diagram " + chosenName);
		label.addStyleName(style.menuItem());
		label.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				diagramPanel.setDiagram(OwnXMLParser.xmlToDiagram(BrowserStorage.getSavedDiagram(chosenName)));
				Notification.showInfo("Diagram opened: " + chosenName);
			}
		});
		Image img = new Image("data:image/gif;base64,R0lGODlhCgAKAJEAAAAAAP////8AAP///yH5BAEAAAMALAAAAAAKAAoAAAIUnI8jgmvLlHtwnpqkpZh72UTZUQAAOw==");
		img.setTitle("delete diagram " + chosenName);
		img.addStyleName(style.menuItem());
		img.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (Window.confirm("Delete saved diagram " + chosenName)) {
					BrowserStorage.removeSavedDiagram(chosenName);
					restoreMenuPanel.remove(hp);
					Notification.showInfo("Deleted diagram: " + chosenName);
				}
			}
		});

		hp.add(img);
		hp.add(label);
		restoreMenuPanel.add(hp);
	}


	//	@UiHandler("newMenuItem")
	//	void onNewMenuItemClick(ClickEvent event) {
	//		Window.open(Window.Location.getQueryString(),"_blank",""); // TODO doesn't work in compiled version
	//	}

	@UiHandler("importMenuItem")
	void onImportMenuItemClick(ClickEvent event) {
		hiddenUploadButton.click();
	}

	@UiHandler("exportMenuItem")
	void onExportMenuItemClick(ClickEvent event) {
		log.info("Exporting: " + OwnXMLParser.diagramToXml(true, diagramPanel.getDiagram()));
		String uxfUrl = "data:text/xml;charset=utf-8," + OwnXMLParser.diagramToXml(true, diagramPanel.getDiagram());
		String pngUrl = CanvasUtils.createPngCanvasDataUrl(diagramPanel.getDiagram());
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
	@UiHandler("paletteChooser")
	void onPaletteChooserMouseWheel(MouseWheelEvent event) {
		// determine new index based on scroll direction
		int newIndex = paletteChooser.getSelectedIndex();
		if (event.getDeltaY() < 0) {
			newIndex--;
		} else {
			newIndex++;
		}
		
		// set new index (if it's valid) and trigger change event (is not automatically triggered)
		if (newIndex >= 0 && newIndex < paletteChooser.getItemCount()) {
			paletteChooser.setSelectedIndex(newIndex);
			DomEvent.fireNativeEvent(Document.get().createChangeEvent(), paletteChooser);
		}
	}
}
