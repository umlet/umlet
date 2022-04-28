package com.web.gwt.client.view.widgets;

import com.baselet.control.constants.SharedConstants;
import com.baselet.control.enums.Program;
import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.view.CanvasUtils;
import com.baselet.gwt.client.view.widgets.*;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

import java.util.Date;

public class WebDownloadPopupPanel extends DownloadPopupPanel {

	private static DateTimeFormat DTF = DateTimeFormat.getFormat("yyyy-MM-dd HH-mm-ss");

	private FilenameAndScaleHolder filenameAndScaleHolder;

	private Timer timer;

	@Override
	public void prepare(FilenameAndScaleHolder filenameAndScaleHolder) {
		this.filenameAndScaleHolder = filenameAndScaleHolder;

		setHeader("Export Diagram");
		FlowPanel panel = new FlowPanel();
		HTML w = new HTML("Optionally set a filename");
		panel.add(w);
		final TextBox textBox = new TextBox();
		panel.add(textBox);

		// handle scaling
		HTML scaleHtml = new HTML("Set scaling of Image file:");
		panel.add(scaleHtml);
		final TextBox scaleBox = new TextBox();
		panel.add(scaleBox);
		scaleBox.setValue("1.0");
		filenameAndScaleHolder.setScaling(1d);

		textBox.setValue(filenameAndScaleHolder.getFilename());
		Button bFile = new Button("Save Diagram File", buttonClickHandler(DownloadType.UXF));
		Button bPNG = new Button("Save PNG File", buttonClickHandler(DownloadType.PNG));
		Button bPDF = new Button("Save PDF File", buttonClickHandler(DownloadType.PDF));
		setButtonStyle(bFile.getElement().getStyle());
		setButtonStyle(bPNG.getElement().getStyle());
		setButtonStyle(bPDF.getElement().getStyle());
		panel.add(bFile);
		panel.add(bPNG);
		panel.add(bPDF);
		setWidget(panel);
		// listen to all input events from the browser (http://stackoverflow.com/a/43089693)
		textBox.addDomHandler(new InputHandler() {
			@Override
			public void onInput(InputEvent event) {
				filenameAndScaleHolder.setFilename(textBox.getText());
			}
		}, InputEvent.getType());
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
	}

	private void setButtonStyle(Style style) {
		style.setWidth(100, Style.Unit.PCT);
		style.setDisplay(Style.Display.BLOCK);
		style.setMarginTop(5, Style.Unit.PX);
	}

	@Override
	public void onData(String data, DownloadType downloadType) {
		if (downloadType == DownloadType.UXF) {
			data = "data:text/xml;charset=utf-8," + data;
			data = data.replace("\"", "&quot;");
		}

		String filename = filenameAndScaleHolder.getFilename();
		if (filename.isEmpty()) {
			filename = "Diagram " + DTF.format(new Date());
		}

		if (downloadType == DownloadType.UXF) {
			filename += "." + Program.getInstance().getExtension();
		}

		if (downloadType == DownloadType.PDF && !filename.endsWith(".pdf")) {
			filename += ".pdf";
		}

		Element element = Document.get().createElement("a");
		element.setAttribute("download", filename);
		element.setAttribute("href", data);
		Document.get().getBody().appendChild(element);
		clickElement(element);
		element.removeFromParent();
	}

	private ClickHandler buttonClickHandler(DownloadType downloadType) {
		return clickEvent -> {
			if (timer != null) {
				timer.cancel();
			}
			timer = new Timer() {
				@Override
				public void run() {
					int oldZoom = drawPanelDiagram.getGridSize();
					switch (downloadType) {
						case UXF:
							DiagramXmlParser.diagramToXml(true, false, drawPanelDiagram.getDiagram(), WebDownloadPopupPanel.this, DownloadType.UXF);
							break;
						case PNG:
							double scalingValue = filenameAndScaleHolder.getScaling();
							drawPanelDiagram.setGridAndZoom(SharedConstants.DEFAULT_GRID_SIZE, false, null);
							CanvasUtils.createPngCanvasDataUrl(drawPanelDiagram.getDiagram(), scalingValue, WebDownloadPopupPanel.this, DownloadType.PNG);
							drawPanelDiagram.setGridAndZoom(oldZoom, false, null);
							break;
						case PDF:
							drawPanelDiagram.setGridAndZoom(SharedConstants.DEFAULT_GRID_SIZE, false, null);
							CanvasUtils.createPdfCanvasDataUrl(drawPanelDiagram.getDiagram(), WebDownloadPopupPanel.this, DownloadType.PDF);
							drawPanelDiagram.setGridAndZoom(oldZoom, false, null);
							break;
					}
				}
			};
			timer.schedule(0);
		};
	}

	// Neither GWT nor elemental2 currently seem to support firing of click-events
	// (see: https://github.com/google/elemental2/issues/152)
	public static native void clickElement(Element elem) /*-{
		elem.click();
	}-*/;
}