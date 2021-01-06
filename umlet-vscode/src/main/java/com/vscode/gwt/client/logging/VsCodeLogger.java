package com.vscode.gwt.client.logging;

import com.baselet.gwt.client.logging.CustomLogger;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VsCodeLogger implements CustomLogger {

	private int levelValue;

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

	@Override
	public void init(Class<?> clazz) {
		initListener();
		this.levelValue = Logger.getLogger("").getLevel().intValue();
	}

	private String generatePrefixMessage() {
		Date date = new Date();
		return "UMLet|" + DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(date) + "|";
	}

	private void setDebugLevel(int debugLevel) {
		if (debugLevel == 0) {
			// Standard level
			this.levelValue = Level.INFO.intValue();
		}
		else {
			// Detailed level
			this.levelValue = Level.FINEST.intValue();
		}
	}

	private native void postLog(String message) /*-{
		$wnd.vscode.postMessage({
			command: 'postLog',
			text: message
			});
	}-*/;

	private native void initListener() /*-{
		var that = this;
		$wnd.addEventListener('message', function (event) {
			var message = event.data;
			switch (message.command) {
				case 'debugLevel':
					that.@com.vscode.gwt.client.logging.VsCodeLogger::setDebugLevel(*)(message.text);
					break;
			}
		});
	}-*/;

}
