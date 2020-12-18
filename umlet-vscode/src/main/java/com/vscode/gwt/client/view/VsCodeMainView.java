package com.vscode.gwt.client.view;

import com.baselet.gwt.client.view.MainView;

public class VsCodeMainView extends MainView {

	public VsCodeMainView() {
		super();
		initListener();

		diagramPaletteSplitter.setWidgetHidden(diagramPaletteSplitter.getWidget(0), true);
	}

	public void changeFont(String fontData) {
		String[] fonts = fontData.split(",");
		for (String font : fonts) {
			setFontData(font, true);
		}
		updateView();
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
}
