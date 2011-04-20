package com.baselet.plugin.editor;

public class DirtyAction implements Runnable {

	private Editor editor;

	public DirtyAction(Editor editor) {
		this.editor = editor;
	}

	@Override
	public void run() {
		this.editor.fireDirtyProperty();
	}
}
