package com.baselet.plugin.editor;

import umletplugin.IEditor;

public class UpdateDiagramNameAction implements Runnable {

	private IEditor editor;

	public UpdateDiagramNameAction(IEditor editor) {
		this.editor = editor;
	}

	@Override
	public void run() {
		this.editor.fireDiagramNameChanged();
	}
}
