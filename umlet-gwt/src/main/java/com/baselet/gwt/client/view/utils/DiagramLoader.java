package com.baselet.gwt.client.view.utils;

import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.view.MainView;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

public class DiagramLoader {

	public static void getFromUrl(String urlOrUxf, MainView mainView) {
		if (urlOrUxf.startsWith("<")) {
			mainView.setDiagram(DiagramXmlParser.xmlToDiagram(urlOrUxf));
		}
		else {
			RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, urlOrUxf);
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
							mainView.setDiagram(DiagramXmlParser.xmlToDiagram(response.getText()));
							// Any value of ?presentation will hide the sidebars (only if diagram get successfully loaded)
							String presentation = Window.Location.getParameter("presentation");
							if (presentation != null) {
								mainView.hideSideBars();
							}
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
	}
}
