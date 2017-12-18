package com.baselet.gwt.client.view.widgets;

import java.util.Date;

import com.baselet.control.enums.Program;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

public class DownloadPopupPanel extends MyPopupPanel {

	private static DateTimeFormat DTF = DateTimeFormat.getFormat("yyyy-MM-dd HH-mm-ss");

	public DownloadPopupPanel(final String uxfUrl, final String pngUrl, final FilenameHolder filenameHolder) {
		super(true, Type.POPUP);
		setHeader("Export Diagram");
		FlowPanel panel = new FlowPanel();
		HTML w = new HTML("Optionally set a filename");
		panel.add(w);
		final TextBox textBox = new TextBox();
		panel.add(textBox);
		textBox.setValue(filenameHolder.getFilename());
		final HTML downloadLinkHtml = new HTML(createDownloadLinks(uxfUrl, pngUrl, filenameHolder.getFilename()));
		panel.add(downloadLinkHtml);
		panel.add(new HTML("<div style=\"color:gray;\">To change the target directory</div><div style=\"color:gray;\">use \"Right click -&gt; Save as\"</div>"));
		setWidget(panel);
		// listen to all input events from the browser (http://stackoverflow.com/a/43089693)
		textBox.addDomHandler(new InputHandler() {
			@Override
			public void onInput(InputEvent event) {
				filenameHolder.setFilename(textBox.getText());
				downloadLinkHtml.setHTML(createDownloadLinks(uxfUrl, pngUrl, filenameHolder.getFilename()));
			}
		}, InputEvent.getType());
	}

	private String createDownloadLinks(String uxfUrl, String pngUrl, String filename) {
		if (filename.isEmpty()) {
			filename = "Diagram " + DTF.format(new Date());
		}
		return "<p class=\"exportLink\">" + link(uxfUrl, filename + "." + Program.getInstance().getExtension()) + "Save Diagram File</a></p>" + "<p class=\"exportLink\">" + link(pngUrl, filename + ".png") + "Save Image File</a></p>";
	}

	private String link(String uxfUrl, String filename) {
		return "<a download=\"" + filename + "\" href='" + uxfUrl.replace("'", "&apos;") + "'>"; // apostrophes in datauris must be escaped because it's the closing sign fore the href
	}
}