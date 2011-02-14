package com.baselet.diagram.command;

import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.GridElement;
import com.baselet.gui.OwnSyntaxPane;

public class ChangeState extends Command {
	private GridElement _entity;

	public GridElement getEntity() {
		return _entity;
	}

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

	public ChangeState(GridElement e, String oldState, String newState, int oldCaret, int newCaret) {
		_entity = e;
		_newState = newState;
		_oldState = oldState;
		_newCaret = newCaret;
		_oldCaret = oldCaret;
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		_entity.setPanelAttributes(_newState);
		_entity.repaint();

		GridElement gridElement = Main.getInstance().getEditedGridElement();
		if (gridElement != null && gridElement.equals(_entity)) {
			OwnSyntaxPane pane = Main.getInstance().getGUI().getPropertyPane();
			pane.setText(gridElement.getPanelAttributes());
			
			if (pane.getText().length() >= _newCaret) {
				pane.setCaretPosition(_newCaret);
			}
		}
	}

	@Override
	public void undo(DiagramHandler handler) {
		//AB: Do not call super.undo() which would deselect the entity
		//super.undo(handler);
		_entity.setPanelAttributes(_oldState);
		_entity.repaint();
	
		GridElement gridElement = Main.getInstance().getEditedGridElement();
		if (gridElement != null && gridElement.equals(_entity)) {
			OwnSyntaxPane pane = Main.getInstance().getGUI().getPropertyPane();
			pane.setText(gridElement.getPanelAttributes());
			
			if (pane.getText().length() >= _oldCaret) {
				pane.setCaretPosition(_oldCaret);
			}
		}
	}
	
	@Override
	public boolean isMergeableTo(Command c) {
		//method is not mergeable (to allow undo of property changes)
		return false;
	}

	@Override
	public Command mergeTo(Command c) {
		ChangeState tmp = (ChangeState) c;
		ChangeState ret = new ChangeState(this.getEntity(), tmp.getOldState(), this.getNewState(), tmp.getOldCaret(), this.getNewCaret());
		return ret;
	}
	
	@Override
	public String toString()
	{
		return "Changestate from " + getOldState() + " to " + getNewState();		
	}
}
