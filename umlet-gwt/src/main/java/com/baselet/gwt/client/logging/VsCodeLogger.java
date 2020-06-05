package com.baselet.gwt.client.logging;

import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;

public class VsCodeLogger implements CustomLogger {
    @Override
    public void trace(String message) {
        postLog(generatePrefixMessage() + "trace|" + message);
    }

    @Override
    public void info(String message) {
        postLog(generatePrefixMessage() + "info|" + message);
    }

    @Override
    public void error(String message) {
        postLog(generatePrefixMessage() + "error|" + message);
    }

    private String generatePrefixMessage() {
        Date date = new Date();
        return "UMLet|" + DateTimeFormat.getFormat("yyyy.MM.dd").format(date) + "|";
    }

    private native void postLog(String message) /*-{
        window.parent.vscode.postMessage({
            command: 'postLog',
            text: message
        });
    }-*/;

}
