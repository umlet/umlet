package com.vscode.gwt.client.view;

import com.baselet.gwt.client.jsinterop.FontData;
import com.baselet.gwt.client.logging.CustomLogger;
import com.baselet.gwt.client.logging.CustomLoggerFactory;
import com.baselet.gwt.client.view.MainView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;

public class VsCodeMainView extends MainView {
	private static final CustomLogger log = CustomLoggerFactory.getLogger(VsCodeMainView.class);

	public VsCodeMainView() {
		super();
		FontData fontData = GWT.create(FontData.class);
		diagramPaletteSplitter.setWidgetHidden(diagramPaletteSplitter.getWidget(0), true);
		initializeListeners();
		setFontData("normal@" + fontData.fontNormal().getText());
		setFontData("italic@" + fontData.fontItalic().getText());
		setFontData("bold@" + fontData.fontBold().getText());
		/*updateView();
		exportUpdateView();*/
	}

	public void setFontData(String fontData) {
		String[] fontDataSeparated = fontData.split("@");
		String fontType = fontDataSeparated[0];
		String fontDataBase64 = fontDataSeparated[1];

		Element fontFaceStyle = DOM.createElement("style");
		fontFaceStyle.setAttribute("type", "text/css");

		Document document = Document.get();
		NodeList<Element> headElements = Document.get().getHead().getElementsByTagName("style");

		loop: for (int i = 0; i < headElements.getLength(); i++) {
			Element item = headElements.getItem(i);
			String itemInnerHtml = item.getInnerHTML();
			if (itemInnerHtml.contains("@font-face")) {
				switch (fontType) {
					case "normal":
						if (!itemInnerHtml.contains("italic") && !item.getInnerHTML().contains("bold")) {
							item.removeFromParent();
							break loop;
						}
						break;
					case "italic":
						if (itemInnerHtml.contains("italic")) {
							item.removeFromParent();
							break loop;
						}
						break;
					case "bold":
						if (itemInnerHtml.contains("bold")) {
							item.removeFromParent();
							break loop;
						}
						break;
				}
			}
		}

		switch (fontType) {
			case "normal":
				fontFaceStyle.setInnerHTML("@font-face {font-family: UmletCustomFont; src: url('data:font/ttf;base64," + fontDataBase64 + "');}");
				break;
			case "italic":
				fontFaceStyle.setInnerHTML("@font-face {font-family: UmletCustomFont; font-style: italic; src: url('data:font/ttf;base64," + fontDataBase64 + "');}");
				break;
			case "bold":
				fontFaceStyle.setInnerHTML("@font-face {font-family: UmletCustomFont; font-weight: bold; src: url('data:font/ttf;base64," + fontDataBase64 + "');}");
				break;
		}
		document.getHead().appendChild(fontFaceStyle);
		log.info("LENGTH AFTER INSERT: " + document.getHead().getElementsByTagName("style").getLength());
	}

	public void updateView() {
		log.info("UPDATING VIEW");
		diagramPanel.updateGridElements();
		palettePanel.updateGridElements();
	}

	private native void initializeListeners() /*-{
		var that = this;
		$wnd.addEventListener('message', function (event) {
			var message = event.data;
			console.log('MESSAGE: ' + message.command)
			switch (message.command) {
				case 'changeFont':
					that.@com.vscode.gwt.client.view.VsCodeMainView::setFontData(Ljava/lang/String;)(message.text);
					break;
				case 'updateView':
					that.@com.vscode.gwt.client.view.VsCodeMainView::updateView()();
					break;
			}
		});
	}-*/;

	public native void exportUpdateView() /*-{
		$wnd.updateView =
				$entry(this.@com.vscode.gwt.client.view.VsCodeMainView::updateView()());
	}-*/;
}
