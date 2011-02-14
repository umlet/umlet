package com.baselet.diagram.command;

import com.baselet.control.Main;
import com.baselet.diagram.CustomPreviewHandler;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.Selector;
import com.baselet.element.GridElement;
import com.baselet.gui.OwnSyntaxPane;

public class CustomCodePropertyChanged extends Command {
	//private GridElement _entity;

	private String _newState;
	private String _oldState;
	private int _oldCaret;
	private int _newCaret;

	public String getNewState() {
		return _newState;
	}

	public String getOldState() {
		return _oldState;
	}
	
	public int getOldCaret() {
		return _oldCaret;
	}
	
	public int getNewCaret() {
		return _newCaret;
	}	

	public CustomCodePropertyChanged(String oldState, String newState, int oldCaret, int newCaret) {
		_newState = newState;
		_oldState = oldState;
		_newCaret = newCaret;
		_oldCaret = oldCaret;
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		
		GridElement gridElement = Main.getInstance().getEditedGridElement();

		//select grid element if nothing is selected
		if (gridElement == null) {
			Selector selector = Main.getInstance().getGUI().getCurrentCustomHandler().getPreviewHandler().getDrawPanel().getSelector();
			selector.selectAll();
			if (selector.getSelectedEntities().size() >= 1) {
				gridElement = selector.getSelectedEntities().firstElement();
			}
		}
		
		if (gridElement != null && gridElement.getHandler() instanceof CustomPreviewHandler) {
			gridElement.setPanelAttributes(_newState);
			
			OwnSyntaxPane pane = Main.getInstance().getGUI().getPropertyPane();
			pane.setText(gridElement.getPanelAttributes());
			
			if (pane.getText().length() >= _newCaret) {
				pane.setCaretPosition(_newCaret);
			}
			
			gridElement.repaint();
		}		
	}

	@Override
	public void undo(DiagramHandler handler) {
		//AB: Do not call super.undo() which would deselect the entity
		//super.undo(handler);
	
		GridElement gridElement = Main.getInstance().getEditedGridElement();

		//select grid element
		if (gridElement == null) {
			Selector selector = Main.getInstance().getGUI().getCurrentCustomHandler().getPreviewHandler().getDrawPanel().getSelector();
			selector.selectAll();
			if (selector.getSelectedEntities().size() >= 1) {
				gridElement = selector.getSelectedEntities().firstElement();
			}
		}		
		
		if (gridElement != null  && gridElement.getHandler() instanceof CustomPreviewHandler) {
			gridElement.setPanelAttributes(_oldState);
			
			OwnSyntaxPane pane = Main.getInstance().getGUI().getPropertyPane();
			pane.setText(gridElement.getPanelAttributes());
			
			if (pane.getText().length() >= _oldCaret) {
				pane.setCaretPosition(_oldCaret);
			}
			
			gridElement.repaint();
		}
	}
		
	@Override
	public String toString()
	{
		return "Changestate from " + getOldState() + " to " + getNewState();		
	}
}
