package com.baselet.diagram.command;

import javax.swing.undo.UndoableEdit;

import com.baselet.diagram.DiagramHandler;

public class CustomCodePanelChanged extends Command {

	private UndoableEdit _edit;
	
	public CustomCodePanelChanged(UndoableEdit edit)
	{
		this._edit = edit;
	}
		
	@Override
	public void execute(DiagramHandler handler) {
		if (_edit.canRedo()) _edit.redo();	
	}
	
	@Override
	public void undo(DiagramHandler handler) {
		if (_edit.canUndo()) _edit.undo();
	}
	
}
