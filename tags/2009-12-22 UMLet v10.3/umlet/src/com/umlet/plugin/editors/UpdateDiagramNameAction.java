package com.umlet.plugin.editors;

public class UpdateDiagramNameAction implements Runnable {

	private UMLetEditor editor;

	public UpdateDiagramNameAction(UMLetEditor editor) {
		this.editor = editor;
	}

	public void run() {
		this.editor.fireDiagramNameChanged();
	}
}
