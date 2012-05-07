package com.baselet.plugin.editor;

import umletplugin.IEditor;

public class DirtyAction implements Runnable {

	private IEditor editor;

	public DirtyAction(IEditor editor) {
		this.editor = editor;
	}

	@Override
	public void run() {
		this.editor.fireDirtyProperty();
	}
}
