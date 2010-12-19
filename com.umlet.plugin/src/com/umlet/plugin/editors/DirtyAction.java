package com.umlet.plugin.editors;

public class DirtyAction implements Runnable {

	private UMLetEditor editor;

	public DirtyAction(UMLetEditor editor) {
		this.editor = editor;
	}

	public void run() {
		this.editor.fireDirtyProperty();
	}
}
