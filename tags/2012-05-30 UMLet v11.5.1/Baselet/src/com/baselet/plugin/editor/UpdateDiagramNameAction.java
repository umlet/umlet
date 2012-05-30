package com.baselet.plugin.editor;

public class UpdateDiagramNameAction implements Runnable {

	private Editor editor;

	public UpdateDiagramNameAction(Editor editor) {
		this.editor = editor;
	}

	@Override
	public void run() {
		this.editor.fireDiagramNameChanged();
	}
}
