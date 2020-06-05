package com.baselet.gwt.client.logging;

import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VsCodeLogger implements CustomLogger {

    private final int levelValue;

    public VsCodeLogger() {
        this.levelValue = Logger.getLogger("").getLevel().intValue();
    }

    @Override
    public void trace(String message) {
        if (levelValue == Level.ALL.intValue() || levelValue <= Level.FINEST.intValue()) {
            postLog(generatePrefixMessage() + "trace|" + message);
        }
    }

    @Override
    public void debug(String message) {
        if (levelValue == Level.ALL.intValue() || levelValue <= Level.FINE.intValue()) {
            postLog(generatePrefixMessage() + "debug|" + message);
        }
    }

    @Override
    public void debug(String message, Throwable throwable) {
        // Ignoring throwable
        debug(message);
    }

    @Override
    public void info(String message) {
        if (levelValue == Level.ALL.intValue() || levelValue <= Level.INFO.intValue()) {
            postLog(generatePrefixMessage() + "info|" + message);
        }
    }

    @Override
    public void error(String message) {
        if (levelValue == Level.ALL.intValue() || levelValue <= Level.SEVERE.intValue()) {
            postLog(generatePrefixMessage() + "error|" + message);
        }
    }

    @Override
    public void error(String message, Throwable throwable) {
        // Ignoring throwable
        error(message);
    }

    private String generatePrefixMessage() {
        Date date = new Date();
        return "UMLet|" + DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(date) + "|";
    }

    private native void postLog(String message) /*-{
        window.parent.vscode.postMessage({
            command: 'postLog',
            text: message
        });
    }-*/;

}
