package com.baselet.diagram.command;

import com.baselet.diagram.DiagramHandler;
import com.baselet.element.GridElement;

public class ChangeState extends Command {
	private GridElement _entity;

	public GridElement getEntity() {
		return _entity;
	}

	private String _newState;
	private String _oldState;

	public String getNewState() {
		return _newState;
	}

	public String getOldState() {
		return _oldState;
	}

	public ChangeState(GridElement e, String oldState, String newState) {
		_entity = e;
		_newState = newState;
		_oldState = oldState;
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		_entity.setPanelAttributes(_newState);
		_entity.repaint();
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		_entity.setPanelAttributes(_oldState);
		_entity.repaint();
	}

	@Override
	public boolean isMergeableTo(Command c) {
		if (!(c instanceof ChangeState)) return false;
		ChangeState cs = (ChangeState) c;
		if (this.getEntity() != cs.getEntity()) return false;
		return true;
	}

	@Override
	public Command mergeTo(Command c) {
		ChangeState tmp = (ChangeState) c;
		ChangeState ret = new ChangeState(this.getEntity(), tmp.getOldState(), this.getNewState());
		return ret;
	}
}
