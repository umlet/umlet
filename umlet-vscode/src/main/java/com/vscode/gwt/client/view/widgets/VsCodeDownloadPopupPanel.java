package com.vscode.gwt.client.view.widgets;

import com.baselet.control.constants.SharedConstants;
import com.baselet.diagram.draw.helper.theme.ThemeFactory;
import com.baselet.element.interfaces.Diagram;
import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.element.GridElementZoomUtil;
import com.baselet.gwt.client.logging.CustomLogger;
import com.baselet.gwt.client.logging.CustomLoggerFactory;
import com.baselet.gwt.client.view.CanvasUtils;
import com.baselet.gwt.client.view.DrawPanelDiagram;
import com.baselet.gwt.client.view.widgets.*;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

public class VsCodeDownloadPopupPanel extends DownloadPopupPanel {

	private static final CustomLogger log = CustomLoggerFactory.getLogger(DiagramXmlParser.class);

	public VsCodeDownloadPopupPanel() {
		initListener();
	}

	@Override
	public void init(DrawPanelDiagram drawPanel) {
		super.init(drawPanel);
		// MainView installs the fonts after initializing this panel.
		Scheduler.get().scheduleDeferred(() -> notifyExportReady());
	}

	private native void notifyExportReady() /*-{
		$doc.fonts.ready.then(function () {
			$wnd.vscode.postMessage({command: 'pngExportReady'});
		});
	}-*/;

	private void handleExportPngOnSave(final double requestId, String content, double scale) {
		ThemeFactory.THEMES theme = ThemeFactory.getActiveThemeEnum();
		try {
			if (Double.isNaN(scale) || Double.isInfinite(scale) || scale < 1) {
				throw new IllegalArgumentException("Invalid PNG export scale");
			}
			// Detached elements are not theme listeners; create their drawing styles in the export theme.
			ThemeFactory.changeTheme(ThemeFactory.THEMES.LIGHT, null, false);
			// Parse a separate snapshot: never replace the editor's diagram, selection or undo history.
			Diagram diagram = DiagramXmlParser.xmlToDiagram(content.trim().isEmpty() ? "<diagram/>" : content, true);
			int oldZoom = diagram.getZoomLevel();
			diagram.setZoomLevel(SharedConstants.DEFAULT_GRID_SIZE);
			if (oldZoom != SharedConstants.DEFAULT_GRID_SIZE) {
				GridElementZoomUtil.zoomEntities(oldZoom, SharedConstants.DEFAULT_GRID_SIZE, diagram.getGridElements());
			}
			CanvasUtils.createPngCanvasDataUrl(diagram, scale, new DownloadPopupPanel() {
				@Override
				public void prepare(FilenameAndScaleHolder holder) {}

				@Override
				public void onData(String data, DownloadType type) {
					exportPngOnSaveVSCode(requestId, data, null);
				}
			}, DownloadType.PNG);
		} catch (Exception e) {
			exportPngOnSaveVSCode(requestId, null, e.getMessage());
		} finally {
			// Canvas allocation/drawing can fail, too; restore the editor theme in that case.
			ThemeFactory.changeTheme(theme, null, true);
		}
	}

	private native void exportPngOnSaveVSCode(double requestId, String data, String error) /*-{
		$wnd.vscode.postMessage({command: 'exportPngOnSave', requestId: requestId, text: data, error: error});
	}-*/;

	@Override
	public void prepare(FilenameAndScaleHolder filenameAndScaleHolder) {
		Diagram diagram = drawPanelDiagram.getDiagram();

		setHeader("Export Diagram");
		FlowPanel panel = new FlowPanel();

		// handle scaling
		HTML scaleHtml = new HTML("Set scaling of Image file:");
		panel.add(scaleHtml);
		final TextBox scaleBox = new TextBox();
		panel.add(scaleBox);
		scaleBox.setValue("1.0");
		filenameAndScaleHolder.setScaling(1d);

		panel.add(new HTML("<br>"));

		String uxfUrl = DiagramXmlParser.diagramToXml(diagram);

		Button saveDiagramButton = new Button();
		saveDiagramButton.setText("Save Diagram File");
		saveDiagramButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				exportDiagramVSCode(uxfUrl);
			}
		});
		panel.add(saveDiagramButton);
		Button savePictureButton = new Button();
		savePictureButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/*String scaledPngUrl = CanvasUtils.createPngCanvasDataUrl(diagram, filenameAndScaleHolder.getScaling());
				exportPngVSCode(scaledPngUrl);

				/* this line is to prevent a bug in the vs code version if this is not recalculated with a 1.0 scaling, then the display size of the elements in the diagram will change to whatever scaling was calculated for exporting */
				//CanvasUtils.createPngCanvasDataUrl(diagram, 1.0d);
			}
		});
		savePictureButton.setText("Save Image File");
		panel.add(savePictureButton);
		setWidget(panel);
		// renew download links when scaling is changed
		scaleBox.addDomHandler(new InputHandler() {
			@Override
			public void onInput(InputEvent event) {
				try {
					double scalingValue = Double.parseDouble(scaleBox.getValue());
					if (scalingValue <= 0)
						throw new Exception();
					filenameAndScaleHolder.setScaling(scalingValue);
				} catch (Exception e) {
					// wrong scaling value, just default to standard
					filenameAndScaleHolder.setScaling(1.0d);
				}
			}
		}, InputEvent.getType());
		// listen to all input events from the browser (http://stackoverflow.com/a/43089693)
		/* textBox.addDomHandler(new InputHandler() {
		 * @Override public void onInput(InputEvent event) { //not needed in vs code version since filename will be entered in popup dialog //filenameAndScaleHolder.setFilename(textBox.getText()); } }, InputEvent.getType()); */
	}

	@Override
	public void onData(String data, DownloadType downloadType) {
		switch (downloadType){
			case UXF:
				break;
			case PNG:
				exportPngVSCode(data);
				break;
			case PDF:
				exportPdfVSCode(data);
				break;
		}
	}

	private void handleExportPng(String size) {
		double scalingValue = Double.parseDouble(size);
		int oldZoom = VsCodeDownloadPopupPanel.this.drawPanelDiagram.getDiagram().getZoomLevel();
		drawPanelDiagram.setGridAndZoom(SharedConstants.DEFAULT_GRID_SIZE, false, null);
		CanvasUtils.createPngCanvasDataUrl(drawPanelDiagram.getDiagram(), scalingValue, VsCodeDownloadPopupPanel.this, DownloadType.PNG);
		drawPanelDiagram.setGridAndZoom(oldZoom, false, null);
	}

	private void handleExportPdf() {
		int oldZoom = drawPanelDiagram.getDiagram().getZoomLevel();
		drawPanelDiagram.setGridAndZoom(SharedConstants.DEFAULT_GRID_SIZE, false, null);
		CanvasUtils.createPdfCanvasDataUrl(drawPanelDiagram.getDiagram(), VsCodeDownloadPopupPanel.this, DownloadType.PDF);
		drawPanelDiagram.setGridAndZoom(oldZoom, false, null);
	}

	private native void initListener() /*-{
		var that = this;
		$wnd.addEventListener('message', function (event) {
			var message = event.data;
			switch (message.command) {
				case 'requestExportPngOnSave':
					$doc.fonts.ready.then(function () {
						that.@com.vscode.gwt.client.view.widgets.VsCodeDownloadPopupPanel::handleExportPngOnSave(DLjava/lang/String;D)(message.requestId, message.text, message.scale);
					});
					break;
				case 'requestExportPng':
					that.@com.vscode.gwt.client.view.widgets.VsCodeDownloadPopupPanel::handleExportPng(Ljava/lang/String;)(message.text);
					break;
				case 'requestExportPdf':
					that.@com.vscode.gwt.client.view.widgets.VsCodeDownloadPopupPanel::handleExportPdf()();
					break;
				case 'myUpdate': //message.text is expected to be the new diagram the editor should changed to
					that.@com.vscode.gwt.client.view.widgets.VsCodeDownloadPopupPanel::handleUpdateContent(Ljava/lang/String;)(message.text);
					break;
			}
		});
	}-*/;

	public void handleUpdateContent(String content) {
		Diagram parsedDiagram = DiagramXmlParser.xmlToDiagram(content);
		if (parsedDiagram != null) {
			this.drawPanelDiagram.setDiagram(parsedDiagram);
			this.drawPanelDiagram.setTempInvalid(false);
		}
		else {
			this.drawPanelDiagram.setTempInvalid(true);
		}

	}

	private native void exportDiagramVSCode(String msg) /*-{
		window.parent.vscode.postMessage({
			command: 'exportUxf',
			text: msg
		});
	}-*/;

	private native void exportPngVSCode(String msg) /*-{
		window.parent.vscode.postMessage({
			command: 'exportPng',
			text: msg
		});
	}-*/;

	private native void exportPdfVSCode(String msg) /*-{
		window.parent.vscode.postMessage({
			command: 'exportPdf',
			text: msg
		});
	}-*/;
}
