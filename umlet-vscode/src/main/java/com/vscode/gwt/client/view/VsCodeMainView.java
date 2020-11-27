package com.vscode.gwt.client.view;

import com.baselet.gwt.client.jsinterop.FontData;
import com.baselet.gwt.client.logging.CustomLogger;
import com.baselet.gwt.client.logging.CustomLoggerFactory;
import com.baselet.gwt.client.view.MainView;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;

public class VsCodeMainView extends MainView {
	private static final CustomLogger log = CustomLoggerFactory.getLogger(VsCodeMainView.class);

	public VsCodeMainView() {
		super();
		diagramPaletteSplitter.setWidgetHidden(diagramPaletteSplitter.getWidget(0), true);
		exportChangeFont();
		String[] fonts = getFontSettings();
		if (fonts != null && fonts.length > 0) {
			for (int i = 0; i < fonts.length; i++) {
				String font = fonts[i];
				if (font.length() == 0) {
					switch (i) {
						case 0:
							setFontData("normal@" + FontData.fontNormal, false);
							break;
						case 1:
							setFontData("italic@" + FontData.fontItalic, false);
							break;
						case 2:
							setFontData("bold@" + FontData.fontBold, false);
							break;
					}
				}
				else {
					setFontData(font, false);
				}
			}
		}
		else {
			setFontData("normal@" + FontData.fontNormal, false);
			setFontData("italic@" + FontData.fontItalic, false);
			setFontData("bold@" + FontData.fontBold, false);
		}
		updateView();
	}

	private void setFontData(String fontData, boolean removeOldEntries) {
		String[] fontDataSeparated = fontData.split("@");
		String fontType = fontDataSeparated[0];
		String fontDataBase64 = fontDataSeparated[1];

		Element fontFaceStyle = DOM.createElement("style");
		fontFaceStyle.setAttribute("type", "text/css");

		Document document = Document.get();
		NodeList<Element> headElements = Document.get().getHead().getElementsByTagName("style");

		if (removeOldEntries) {
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
		}

		switch (fontType) {
			case "normal":
				fontFaceStyle.setInnerHTML("@font-face {font-family: " + FontData.fontName + "; src: url('data:font/ttf;base64," + fontDataBase64 + "') format('truetype');}");
				FontData.fontNormal = fontDataBase64;
				break;
			case "italic":
				fontFaceStyle.setInnerHTML("@font-face {font-family: " + FontData.fontName + "; font-style: italic; src: url('data:font/ttf;base64," + fontDataBase64 + "') format('truetype');}");
				FontData.fontItalic = fontDataBase64;
				break;
			case "bold":
				fontFaceStyle.setInnerHTML("@font-face {font-family: " + FontData.fontName + "; font-weight: bold; src: url('data:font/ttf;base64," + fontDataBase64 + "') format('truetype');}");
				FontData.fontBold = fontDataBase64;
				break;
		}
		document.getHead().appendChild(fontFaceStyle);
	}

	private void updateView() {
		// These timers are needed because otherwise the content won't update...
		Timer timerUpdate = new Timer() {
			@Override
			public void run() {
				diagramPanel.updateGridElements();
				palettePanel.updateGridElements();
				Timer timerRedraw = new Timer() {
					@Override
					public void run() {
						diagramPanel.redraw(true);
						palettePanel.redraw(true);
					}
				};
				timerRedraw.schedule(50);
			}
		};
		timerUpdate.schedule(50);
	}

	public void changeFont(String fontData) {
		setFontData(fontData, true);
		updateView();
	}

	public static native void exportChangeFont() /*-{
		$wnd.changeFont =
			$entry(this.@com.vscode.gwt.client.view.VsCodeMainView::changeFont(Ljava/lang/String;));
	}-*/;

	private native String[] getFontSettings() /*-{
		return $wnd.fonts;
	}-*/;
}
