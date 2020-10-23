package com.baselet.gwt.client.jsinterop;

import com.baselet.control.config.SharedConfig;
import com.baselet.control.constants.SharedConstants;
import com.baselet.element.NewGridElement;
import com.baselet.element.Selector;
import com.baselet.element.interfaces.Diagram;
import com.baselet.element.interfaces.DrawHandlerInterface;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.sticking.StickableMap;
import com.baselet.gwt.client.element.ComponentGwt;
import com.baselet.gwt.client.element.DiagramGwt;
import com.baselet.gwt.client.element.DrawHandlerGwt;
import com.baselet.gwt.client.element.ElementFactoryGwt;
import com.baselet.gwt.client.logging.CustomLogger;
import com.baselet.gwt.client.logging.CustomLoggerFactory;
import com.baselet.gwt.client.view.CanvasUtils;
import com.baselet.gwt.client.view.CanvasWrapper;
import com.baselet.gwt.client.view.SelectorNew;
import org.apache.commons.collections.MapUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DrawPdfCanvas extends CanvasWrapper {
	private static final CustomLogger logger = CustomLoggerFactory.getLogger(DrawPdfCanvas.class);

	// TODO DELETE THIS MAYBE
	private final PdfContext pdfContext;
	private final SvgContext svgContext;
	private final RealPdfContext realPdfContext;

	String blobString;

	public DrawPdfCanvas(int width, int height) {
		this.pdfContext = new PdfContext(new BlobStream());
		this.svgContext = new SvgContext(500, 500);
		this.realPdfContext = new RealPdfContext(RealPdfContext.PdfOptions.create(new int[] { width, height }, RealPdfContext.PdfOptions.Margins.create(0, 0, 0, 0)));
	}

	@Override
	public Context2dWrapper getContext2d() {
		return new Context2dPdfWrapper(pdfContext, realPdfContext);
	}

	@Override
	public void clearAndSetSize(int width, int height) {
		pdfContext.clearRect(0, 0, width, height);
		svgContext.setWidth(width);
		svgContext.setHeight(height);
		//realPdfContext.clearRect(0, 0, width, height);
	}

	@Override
	public int getWidth() {
		return svgContext.getWidth();
	}

	@Override
	public int getHeight() {
		return svgContext.getHeight();
	}

	@Override
	public String toDataUrl(String type) {
		// logger.info(svgContext.getSerializedSvg(true));
		// return "";
		/* logger.info("PDF toDataUrl"); logger.info(pdfContext.stream.toString()); pdfContext.stream.on("finish", () -> { this.blobString = pdfContext.stream.toBlobURL(type); FileReader fileReader = new FileReader(); Object pdfBlob = pdfContext.stream.toBlob(type); fileReader.onloadend = () -> { logger.info(fileReader.result); }; fileReader.readAsDataURL(pdfBlob); }); pdfContext.end(); */

		return "";
	}

	public void drawSvg(List<GridElement> gridElements) {
		BlobStream stream = realPdfContext.pipe(new BlobStream());
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
				logger.info(ge.getRectangle().toString());
				logger.info(((NewGridElement) ge).getRealRectangle().toString());
				((ComponentGwt) ge.getComponent()).drawSvg(getContext2d());
			}
		}
		realPdfContext.end();

		stream.on("finish", () -> {
			this.blobString = stream.toBlobURL("application/pdf");
			FileReader fileReader = new FileReader();
			Object pdfBlob = stream.toBlob("application/pdf");
			fileReader.onloadend = () -> {
				logger.info(fileReader.result);
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
