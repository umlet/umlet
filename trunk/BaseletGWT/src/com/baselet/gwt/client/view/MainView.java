package com.baselet.gwt.client.view;

import org.apache.log4j.Logger;
import org.vectomatic.file.FileUploadExt;

import com.baselet.gwt.client.BrowserStorage;
import com.baselet.gwt.client.Notification;
import com.baselet.gwt.client.DiagramXmlParser;
import com.baselet.gwt.client.view.widgets.DownloadPopupPanel;
import com.baselet.gwt.client.view.widgets.SaveDialogBox;
import com.baselet.gwt.client.view.widgets.SaveDialogBox.Callback;
import com.baselet.gwt.client.view.widgets.ShortcutDialogBox;
import com.baselet.gwt.client.view.widgets.propertiespanel.PropertiesTextArea;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
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
import com.google.gwt.user.client.ui.RootPanel;
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
	FlowPanel restoreMenuPanel;

	@UiField(provided = true)
	SplitLayoutPanel palettePropertiesSplitter = new SplitLayoutPanel() {
		@Override
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

	private DrawPanel diagramPanel;
	private AutoResizeScrollDropPanel diagramScrollPanel;

	private DrawPanel palettePanel;
	private AutoResizeScrollDropPanel paletteScrollPanel;

	private FileUploadExt hiddenUploadButton = new FileUploadExt();
	private FileOpenHandler handler;

	private Logger log = Logger.getLogger(MainView.class);

	private ScheduledCommand saveCommand = new ScheduledCommand() {
		private SaveDialogBox saveDialogBox = new SaveDialogBox(new Callback() {
			@Override
			public void callback(final String chosenName) {
				boolean itemIsNewlyAdded = BrowserStorage.getSavedDiagram(chosenName) == null;
				BrowserStorage.addSavedDiagram(chosenName, DiagramXmlParser.diagramToXml(diagramPanel.getDiagram()));
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
		diagramPanel = new DrawPanelDiagram(this, propertiesPanel);
		palettePanel = new DrawPanelPalette(this, propertiesPanel, paletteChooser);
		diagramPanel.setOtherDrawFocusPanel(palettePanel);
		palettePanel.setOtherDrawFocusPanel(diagramPanel);
		diagramScrollPanel = new AutoResizeScrollDropPanel(diagramPanel);
		paletteScrollPanel = new AutoResizeScrollDropPanel(palettePanel);
		updateNotificationPosition();

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

		EventHandlingUtils.addEventHandler(mainPanel, diagramPanel, palettePanel);
	}

	private void addRestoreMenuItem(final String chosenName) {
		final HorizontalPanel hp = new HorizontalPanel();

		Label label = new Label(chosenName);
		label.setTitle("open diagram " + chosenName);
		label.addStyleName(style.menuItem());
		label.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				diagramPanel.setDiagram(DiagramXmlParser.xmlToDiagram(BrowserStorage.getSavedDiagram(chosenName)));
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

	@UiHandler("importMenuItem")
	void onImportMenuItemClick(ClickEvent event) {
		hiddenUploadButton.click();
	}

	@UiHandler("exportMenuItem")
	void onExportMenuItemClick(ClickEvent event) {
		log.info("Exporting: " + DiagramXmlParser.diagramToXml(true, diagramPanel.getDiagram()));
		String uxfUrl = "data:text/xml;charset=utf-8," + DiagramXmlParser.diagramToXml(true, diagramPanel.getDiagram());
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
}
