package com.baselet.control.enums;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

public enum Metakey {
	CTRL, CMD;

	@Override
	public String toString() {
		if (this == CTRL) {
			return "Ctrl";
		}
		else {
			return "Cmd";
		}
	}

	// Use these masks as modifiers if you want to enforce ctrl or meta in addition to a keyevent
	public int getMask() {
		if (this == CTRL) {
			return ActionEvent.CTRL_MASK;
		}
		else {
			return ActionEvent.META_MASK;
		}
	}

	public int getMaskDown() {
		if (this == CTRL) {
			return InputEvent.CTRL_DOWN_MASK;
		}
		else {
			return InputEvent.META_DOWN_MASK;
		}
	}
}