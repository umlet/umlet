package com.baselet.gwt.client.view;

import com.baselet.control.config.SharedConfig;
import com.baselet.element.NewGridElement;
import com.baselet.element.interfaces.Diagram;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gwt.client.element.ComponentGwt;
import com.baselet.gwt.client.element.DiagramGwt;
import com.baselet.gwt.client.element.ElementFactoryGwt;
import com.baselet.gwt.client.jsinterop.*;
import com.baselet.gwt.client.logging.CustomLogger;
import com.baselet.gwt.client.logging.CustomLoggerFactory;
import com.baselet.gwt.client.view.widgets.DownloadPopupPanel;
import com.baselet.gwt.client.view.widgets.DownloadType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DrawCanvasPdf {
	private static final CustomLogger logger = CustomLoggerFactory.getLogger(DrawCanvasPdf.class);

	private final PdfContext pdfContext;

	public DrawCanvasPdf(int width, int height) {
		this.pdfContext = new PdfContext(PdfContext.PdfOptions.create(new int[] { width, height }, PdfContext.PdfOptions.Margins.create(0, 0, 0, 0)));
	}

	public Context2dWrapper getContext2d() {
		return new Context2dPdfWrapper(pdfContext);
	}

	public void drawPdf(List<GridElement> gridElements, DownloadPopupPanel receiver, DownloadType type) {
		BlobStream stream = pdfContext.pipe(new BlobStream());
		if (SharedConfig.getInstance().isDev_mode()) {
			CanvasUtils.drawGridOn(getContext2d());
		}
		else {
			Diagram diagram = new DiagramGwt("", new ArrayList<GridElement>());
			List<GridElement> bla = copyElementsInList(gridElements, diagram);
			for (GridElement ge : bla) {
				ComponentGwt pdfComponent = new ComponentGwt(ge, 1d, getContext2d());
				pdfComponent.setBoundsRect(ge.getComponent().getBoundsRect());
				((NewGridElement) ge).setComponent(pdfComponent);
				((ComponentGwt) ge.getComponent()).drawPdf(getContext2d());
			}
		}
		pdfContext.end();

		stream.on("finish", () -> {
			FileReader fileReader = new FileReader();
			Object pdfBlob = stream.toBlob("application/pdf");
			fileReader.onloadend = () -> {
				receiver.onData(fileReader.result, type);
			};
			fileReader.readAsDataURL(pdfBlob);
		});
	}

	private List<GridElement> copyElementsInList(Collection<GridElement> sourceElements, Diagram targetDiagram) {
		List<GridElement> targetElements = new ArrayList<GridElement>();
		for (GridElement ge : sourceElements) {
			GridElement e = ElementFactoryGwt.create(ge, targetDiagram);
			targetElements.add(e);
		}
		return targetElements;
	}
}
