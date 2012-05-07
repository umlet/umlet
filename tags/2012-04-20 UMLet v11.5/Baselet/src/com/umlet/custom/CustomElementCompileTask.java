package com.umlet.custom;

import java.util.TimerTask;

public class CustomElementCompileTask extends TimerTask {

	private CustomElementHandler handler;

	public CustomElementCompileTask(CustomElementHandler handler) {
		this.handler = handler;
	}

	@Override
	public void run() {
		handler.runCompilation();
	}

}
