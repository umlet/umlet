package com.umlet.gui.eclipse;

import com.umlet.control.diagram.DiagramHandler;

public class Print implements Runnable {

	private DiagramHandler handler;

	public Print(DiagramHandler handler) {
		this.handler = handler;
	}

	public void run() {
		this.handler.doPrint();
	}
}
