package com.baselet.gwt.client.view.utils;

import com.baselet.gwt.client.base.Notification;
import com.baselet.gwt.client.element.DiagramXmlParser;
import com.baselet.gwt.client.view.DrawPanel;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnLoad {

  private final Logger log = LoggerFactory.getLogger(OnLoad.class);

  public static void OnLoad(String url, DrawPanel diagramPanel) {

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
}
