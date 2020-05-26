package com.baselet.gwt.client.view;

import com.baselet.diagram.draw.helper.theme.Theme;
import com.baselet.diagram.draw.helper.theme.ThemeFactory;
import com.baselet.diagram.draw.helper.theme.ThemeChangeListener;
import com.baselet.gwt.client.base.Converter;
import com.baselet.gwt.client.view.VersionChecker.Version;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.user.client.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vectomatic.file.FileUploadExt;

import com.baselet.control.config.SharedConfig;
import com.baselet.element.interfaces.Diagram;
import com.baselet.gwt.client.base.Notification;
import com.baselet.gwt.client.element.WebStorage;
import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.view.panel.wrapper.AutoResizeScrollDropPanel;
import com.baselet.gwt.client.view.panel.wrapper.FileOpenHandler;
import com.baselet.gwt.client.view.utils.DiagramLoader;
import com.baselet.gwt.client.view.utils.DropboxIntegration;
import com.baselet.gwt.client.view.widgets.DownloadPopupPanel;
import com.baselet.gwt.client.view.widgets.FilenameAndScaleHolder;
import com.baselet.gwt.client.view.widgets.SaveDialogBox;
import com.baselet.gwt.client.view.widgets.SaveDialogBox.Callback;
import com.baselet.gwt.client.view.widgets.ShortcutDialogBox;
import com.baselet.gwt.client.view.widgets.propertiespanel.PropertiesTextArea;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;

public class MainView extends Composite implements ThemeChangeListener {

	private static MainViewUiBinder uiBinder = GWT.create(MainViewUiBinder.class);

	interface MainViewUiBinder extends UiBinder<Widget, MainView> {}

	interface MyStyle extends CssResource {
		String menuItem();
	}

	@UiField
	MyStyle style;

	@UiField
	FocusPanel mainPanel;

	@UiField(provided = true)
	SplitLayoutPanel diagramPaletteSplitter = new SplitLayoutPanel(4) {
		@Override
		public void onResize() {
			super.onResize();
			updateNotificationPosition();
		}
	};

	@UiField
	FlowPanel menuPanel;

	@UiField
	DivElement propertiesDiv;

	@UiField
	FlowPanel restoreMenuPanel;

	@UiField(provided = true)
	SplitLayoutPanel palettePropertiesSplitter = new SplitLayoutPanel() {
		@Override
		public void onResize() {
			diagramPanel.redraw();
			palettePanel.redraw();
		}
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

	private final DrawPanel diagramPanel;
	private final AutoResizeScrollDropPanel diagramScrollPanel;

	private final DrawPanel palettePanel;
	private final AutoResizeScrollDropPanel paletteScrollPanel;

	private final FileUploadExt hiddenUploadButton = new FileUploadExt();
	private final FileOpenHandler handler;

	private final Logger log = LoggerFactory.getLogger(MainView.class);

	private final DropboxIntegration dropboxInt;

	private final FilenameAndScaleHolder lastExportFilename = new FilenameAndScaleHolder("");

	private final ScheduledCommand saveCommand = new ScheduledCommand() {
		private final SaveDialogBox saveDialogBox = new SaveDialogBox(new Callback() {
			@Override
			public void callback(final String chosenName) {
				boolean itemIsNewlyAdded = WebStorage.getSavedDiagram(chosenName) == null;
				WebStorage.addSavedDiagram(chosenName, DiagramXmlParser.diagramToXml(diagramPanel.getDiagram()));
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

	private final ScheduledCommand saveCommandVSCode = new ScheduledCommand() {
		@Override
		public void execute() {
			initialiseExportDialog();
		}
	};

	private final ScheduledCommand exportToDropbox = new ScheduledCommand() {
		private final SaveDialogBox saveDialogBox = new SaveDialogBox(new Callback() {
			@Override
			public void callback(final String chosenName) {

				String uxfUrl = "data:text/xml;charset=utf-8," + DiagramXmlParser.diagramToXml(true, false, diagramPanel.getDiagram());
				dropboxInt.openDropboxExport(uxfUrl, chosenName);

			}
		}, "Export to Dropbox");

		@Override
		public void execute() {
			saveDialogBox.clearAndCenter();
		}
	};

	public ScheduledCommand getSaveCommand() {
		if (VersionChecker.GetVersion() == VersionChecker.Version.VSCODE)
			return saveCommandVSCode;
		else
			return saveCommand;
	}

	public void setDiagram(Diagram diagram) {
		diagramPanel.setDiagram(diagram);
	}

	public void hideSideBars() {
		diagramPaletteSplitter.setWidgetSize(menuPanel, 0.0);
		diagramPaletteSplitter.setWidgetSize(palettePropertiesSplitter, 0.0);
	}

	public MainView() {
		initWidget(uiBinder.createAndBindUi(this));

		if (VersionChecker.GetVersion() == Version.VSCODE) {
			diagramPaletteSplitter.setWidgetHidden(diagramPaletteSplitter.getWidget(0), true);
		}
		diagramPaletteSplitter.setWidgetToggleDisplayAllowed(palettePropertiesSplitter, true);
		diagramPaletteSplitter.setWidgetSnapClosedSize(palettePropertiesSplitter, 100);
		diagramPaletteSplitter.setWidgetMinSize(palettePropertiesSplitter, 200);
		diagramPaletteSplitter.setWidgetToggleDisplayAllowed(menuPanel, true);
		diagramPaletteSplitter.setWidgetSnapClosedSize(menuPanel, 25);
		diagramPaletteSplitter.setWidgetMinSize(menuPanel, 50);
		palettePropertiesSplitter.setWidgetToggleDisplayAllowed(paletteChooserCanvasSplitter, true);
		diagramPanel = new DrawPanelDiagram(this, propertiesPanel);


		palettePanel = new DrawPanelPalette(this, propertiesPanel, paletteChooser);
		diagramPanel.setOtherDrawFocusPanel(palettePanel);
		palettePanel.setOtherDrawFocusPanel(diagramPanel);
		diagramScrollPanel = new AutoResizeScrollDropPanel(diagramPanel);
		paletteScrollPanel = new AutoResizeScrollDropPanel(palettePanel);
		updateNotificationPosition();
		ThemeFactory.addListener(this);

		for (String diagramName : WebStorage.getSavedDiagramKeys()) {
			addRestoreMenuItem(diagramName);
		}

		onThemeChange();

		log.trace("Main View initialized");

		handler = new FileOpenHandler(diagramPanel);

		//Load diagram if one was passed from vscode
		if(VersionChecker.GetVersion() == Version.VSCODE && VersionChecker.vsCodePredefinedFile() != null)
		{
			try {
				diagramPanel.setDiagram(DiagramXmlParser.xmlToDiagram(VersionChecker.vsCodePredefinedFile() ));
			} catch (Exception e)
			{
				GWT.log("failed to load diagram passed from vscode, loading defaults...");
			}
		}

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

		EventHandlingUtils.addEventHandler(mainPanel, diagramPanel, palettePanel);
		mainPanel.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				SharedConfig.getInstance().setStickingEnabled(true); // shift button may have stopped being pressed, therefore assume sticking is enabled again
			}
		});

		// Add Dropbox dropins.js
		ScriptInjector.fromUrl("https://www.dropbox.com/static/api/2/dropins.js?data-app-key='3mmyizdvtldctng'")
				.setWindow(ScriptInjector.TOP_WINDOW)
				.inject();
		dropboxInt = new DropboxIntegration(diagramPanel);
		dropboxInt.exposeDropboxImportJSCallback(dropboxInt);
		dropboxInt.exposeDropboxShowNotification(dropboxInt);

		// if uxf parameter is set, a GET request is made to get and load the diagram from the specified URL
		String uxfStartup = Window.Location.getParameter("uxf");
		if (uxfStartup != null) {
			DiagramLoader.getFromUrl(uxfStartup, this);
		}
		onThemeChange();
	}



	private void addRestoreMenuItem(final String chosenName) {
		final HorizontalPanel hp = new HorizontalPanel();

		Label label = new Label(chosenName);
		label.setTitle("open diagram " + chosenName);
		label.addStyleName(style.menuItem());
		label.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				diagramPanel.setDiagram(DiagramXmlParser.xmlToDiagram(WebStorage.getSavedDiagram(chosenName)));
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
					WebStorage.removeSavedDiagram(chosenName);
					restoreMenuPanel.remove(hp);
					Notification.showInfo("Deleted diagram: " + chosenName);
				}
			}
		});

		hp.add(img);
		hp.add(label);
		restoreMenuPanel.add(hp);
	}

	@UiHandler("importMenuItem")
	void onImportMenuItemClick(ClickEvent event) {
		hiddenUploadButton.click();
	}

	@UiHandler("exportMenuItem")
	void onExportMenuItemClick(ClickEvent event) {
		initialiseExportDialog();
	}

	public void initialiseExportDialog() {
		String uxfUrl = "data:text/xml;charset=utf-8," + DiagramXmlParser.diagramToXml(true, true, diagramPanel.getDiagram());
		log.info("Exporting: " + uxfUrl);
		String pngUrl = CanvasUtils.createPngCanvasDataUrl(diagramPanel.getDiagram());
		new DownloadPopupPanel(uxfUrl, pngUrl, diagramPanel.getDiagram(), lastExportFilename).center();
	}

	@UiHandler("importDropboxMenuItem")
	void onImportDropboxMenuItemClick(ClickEvent event) {
		dropboxInt.openDropboxImport();
	}

	@UiHandler("exportDropboxMenuItem")
	void onExportDropboxMenuItemClick(ClickEvent event) {
		exportToDropbox.execute();
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
		}
		else {
			newIndex++;
		}

		// set new index (if it's valid) and trigger change event (is not automatically triggered)
		if (newIndex >= 0 && newIndex < paletteChooser.getItemCount()) {
			paletteChooser.setSelectedIndex(newIndex);
			DomEvent.fireNativeEvent(Document.get().createChangeEvent(), paletteChooser);
		}
	}

	private void updateNotificationPosition() {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				RootPanel.get("featurewarning").getElement().getStyle().setMarginLeft(menuPanel.getOffsetWidth(), Unit.PX);
			}
		});
	}

	@Override
	public void onThemeChange() {
		String backgroundColor = Converter.convert(ThemeFactory.getCurrentTheme().getColor(Theme.ColorStyle.DEFAULT_BACKGROUND)).value();
		String foregroundColor = Converter.convert(ThemeFactory.getCurrentTheme().getColor(Theme.ColorStyle.DEFAULT_FOREGROUND)).value();
		String splitterColor = Converter.convert(ThemeFactory.getCurrentTheme().getColor(Theme.ColorStyle.DEFAULT_SPLITTER_COLOR)).value();
		diagramScrollPanel.getElement().getStyle().setBackgroundColor(backgroundColor);
		paletteScrollPanel.getElement().getStyle().setBackgroundColor(backgroundColor);
		paletteChooser.getElement().getStyle().setBackgroundColor(backgroundColor);
		paletteChooser.getElement().getStyle().setColor(foregroundColor);

		// Splitter between main-canvas and east side
		diagramPaletteSplitter.getWidget(3).getElement().getStyle().setBackgroundColor(splitterColor);

		// Splitter between palette chooser and textarea
		palettePropertiesSplitter.getWidget(1).getElement().getStyle().setBackgroundColor(splitterColor);

		propertiesDiv.getStyle().setBackgroundColor(splitterColor);
		propertiesDiv.getStyle().setColor(foregroundColor);
		propertiesPanel.getElement().getStyle().setBackgroundColor(backgroundColor);
		propertiesPanel.getElement().getStyle().setColor(foregroundColor);
		propertiesPanel.getElement().getStyle().setBorderColor(backgroundColor);

		this.getElement().getStyle().setBackgroundColor(backgroundColor);
	}
}
