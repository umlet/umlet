package com.vscode.gwt.client.view;

import com.baselet.gwt.client.jsinterop.FontData;
import com.baselet.gwt.client.jsinterop.FontResource;
import com.baselet.gwt.client.view.MainView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;

public class VsCodeMainView extends MainView {
	private final FontResource fontResource;

	public VsCodeMainView() {
		super();
		initListener();
		fontResource = GWT.create(FontResource.class);

		diagramPaletteSplitter.setWidgetHidden(diagramPaletteSplitter.getWidget(0), true);
		String[] fonts = getFontSettings();
		if (fonts != null && fonts.length > 0) {
			for (int i = 0; i < fonts.length; i++) {
				String font = fonts[i];
				if (font.length() == 0) {
					switch (i) {
						case 1:
							setFontData("italic@" + FontData.fontItalic, false);
							break;
						case 2:
							setFontData("bold@" + FontData.fontBold, false);
							break;
						default:
							setFontData("normal@" + FontData.fontNormal, false);
							break;
					}
				}
				else {
					setFontData(font, false);
				}
			}
		}
		else {
			setFontData("normal@ttf@" + FontData.fontNormal, false);
			setFontData("italic@ttf@" + FontData.fontItalic, false);
			setFontData("bold@ttf@" + FontData.fontBold, false);
		}
		updateView();
	}

	private void setFontData(String fontData, boolean removeOldEntries) {
		String[] fontDataSeparated = fontData.split("@");
		String fontType = fontDataSeparated[0];
		String fontDataBase64 = null;
		String fontFileType = null;
		if (fontDataSeparated.length == 3) {
			fontFileType = fontDataSeparated[1];
			fontDataBase64 = fontDataSeparated[2];
		} else {
			switch (fontType) {
				case "italic":
					fontDataBase64 = fontResource.fontItalic().getText();
					break;
				case "bold":
					fontDataBase64 = fontResource.fontBold().getText();
					break;
				default:
					fontDataBase64 = fontResource.fontNormal().getText();
					break;
			}
			fontFileType = "ttf";
		}

		Document document = Document.get();
		NodeList<Element> headElements = document.getHead().getElementsByTagName("style");

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

		Element fontFaceStyle = DOM.createElement("style");
		fontFaceStyle.setAttribute("type", "text/css");
		switch (fontType) {
			case "italic":
				fontFaceStyle.setInnerHTML("@font-face {font-family: " + FontData.fontName + "; font-style: italic; src: url('data:font/"+getFontUrlType(fontFileType) + ";base64," + fontDataBase64 + "') format('"+getFontUrlFormat(fontFileType) + "');}");
				FontData.setFontItalic(fontDataBase64);
				break;
			case "bold":
				fontFaceStyle.setInnerHTML("@font-face {font-family: " + FontData.fontName + "; font-weight: bold; src: url('data:font/"+getFontUrlType(fontFileType) + ";base64," + fontDataBase64 + "') format('"+getFontUrlFormat(fontFileType) + "');}");
				FontData.setFontBold(fontDataBase64);
				break;
			default:
				fontFaceStyle.setInnerHTML("@font-face {font-family: " + FontData.fontName + "; src: url('data:font/"+getFontUrlType(fontFileType) + ";base64," + fontDataBase64 + "') format('"+getFontUrlFormat(fontFileType) + "');}");
				FontData.setFontNormal(fontDataBase64);
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
				timerRedraw.schedule(100);
			}
		};
		timerUpdate.schedule(100);
	}

	public void changeFont(String fontData) {
		String[] fonts = fontData.split(",");
		for (String font : fonts) {
			setFontData(font, true);
		}
		updateView();
	}

	private String getFontUrlType(String datatype) {
		switch (datatype) {
			case "ttf":
				return "ttf";
			case "otf":
				return "opentype";
			default:
				return "ttf";
		}
	}

	private String getFontUrlFormat(String datatype) {
		switch (datatype) {
			case "ttf":
				return "truetype";
			case "otf":
				return "opentype";
			default:
				return "truetype";
		}
	}

	private native void initListener() /*-{
		var that = this;
		$wnd.addEventListener('message', function (event) {
			var message = event.data;
			switch (message.command) {
				case 'changeFont':
					that.@com.vscode.gwt.client.view.VsCodeMainView::changeFont(Ljava/lang/String;)(message.text);
					break;
			}
		});
	}-*/;

	private native String[] getFontSettings() /*-{
		return $wnd.fonts;
	}-*/;
}
