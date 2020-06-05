package com.baselet.gwt.client.view.utils;

import com.baselet.gwt.client.logging.CustomLogger;
import com.baselet.gwt.client.logging.CustomLoggerFactory;

import com.baselet.gwt.client.base.Notification;
import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.view.DrawPanel;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

public class DropboxIntegration {

	private final CustomLogger log = CustomLoggerFactory.getLogger(DropboxIntegration.class);

	private final DrawPanel diagramPanel;

	public DropboxIntegration(DrawPanel diagramPanel) {
		this.diagramPanel = diagramPanel;
	}

	public native void openDropboxExport(String fileURL, String filename) /*-{
		var options = {
			files : [
			// You can specify up to 100 files.
			{
				'url' : fileURL,
				'filename' : filename + '.uxf'
			}
			// ...
			],

			// Success is called once all files have been successfully added to the user's
			// Dropbox, although they may not have synced to the user's devices yet.
			success : function() {
				// Indicate to the user that the files have been saved.
				// alert("Success! Files saved to your Dropbox.");
				$wnd.dropboxShowNotification("Saved diagram to dropbox");
			},

			// Progress is called periodically to update the application on the progress
			// of the user's downloads. The value passed to this callback is a float
			// between 0 and 1. The progress callback is guaranteed to be called at least
			// once with the value 1.
			progress : function(progress) {
			},

			// Cancel is called if the user presses the Cancel button or closes the Saver.
			cancel : function() {
			},

			// Error is called in the event of an unexpected response from the server
			// hosting the files, such as not being able to find a file. This callback is
			// also called if there is an error on Dropbox or if the user is over quota.
			error : function(errorMessage) {
				alert(errorMessage);
			}
		};

		//alert(fileURL + " \n" + filename);

		$wnd.Dropbox.save(options);
	}-*/;

	public native void openDropboxImport() /*-{
		var options = {

			// Required. Called when a user selects an item in the Chooser.
			success : function(files) {
				//alert("Here's the file link: " + files[0].link)
				$wnd.dropboxImportCallback(files[0].link);
				$wnd.dropboxShowNotification("Imported diagram from dropbox");
			},

			// Optional. Called when the user closes the dialog without selecting a file
			// and does not include any parameters.
			cancel : function() {

			},

			// Optional. "preview" (default) is a preview link to the document for sharing,
			// "direct" is an expiring link to download the contents of the file. For more
			// information about link types, see Link types below.
			linkType : "direct", // or "direct"

			// Optional. A value of false (default) limits selection to a single file, while
			// true enables multiple file selection.
			multiselect : false, // or true

			// Optional. This is a list of file extensions. If specified, the user will
			// only be able to select files with these extensions. You may also specify
			// file types, such as "video" or "images" in the list. For more information,
			// see File types below. By default, all extensions are allowed.
			extensions : [ '.xml', '.uxf' ],
		};
		$wnd.Dropbox.choose(options);
	}-*/;

	public native void exposeDropboxImportJSCallback(DropboxIntegration di) /*-{
		$wnd.dropboxImportCallback = function(param) {
			di.@com.baselet.gwt.client.view.utils.DropboxIntegration::openDropboxImportCallback(*)(param);
		}
	}-*/;

	public void openDropboxImportCallback(String file) {
		// log.info(file);
		doGet(file);
	}

	public void doGet(String url) {

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new RequestCallback() {

				@Override
				public void onError(Request request, Throwable exception) {
					if (exception instanceof RequestTimeoutException) {
						Window.alert("The request has timed out");
					}
					else {
						Window.alert(exception.getMessage());
					}
				}

				@Override
				public void onResponseReceived(Request request, Response response) {
					int STATUS_CODE_OK = 200;
					if (STATUS_CODE_OK == response.getStatusCode()) {
						diagramPanel.setDiagram(DiagramXmlParser.xmlToDiagram(response.getText()));
					}
					else {
						Window.alert("Something went wrong: HTTP Status Code: " + response.getStatusCode());
					}

				}
			});
		} catch (RequestException e) {
			throw new RuntimeException(e);
		}
	}

	public native void exposeDropboxShowNotification(DropboxIntegration di) /*-{
		$wnd.dropboxShowNotification = function(param) {
			di.@com.baselet.gwt.client.view.utils.DropboxIntegration::showNOtification(*)(param);
		}
	}-*/;

	public void showNOtification(String text) {
		Notification.showInfo(text);
	}
}
