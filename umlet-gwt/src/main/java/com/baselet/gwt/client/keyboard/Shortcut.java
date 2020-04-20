package com.baselet.gwt.client.keyboard;

import com.google.gwt.event.dom.client.KeyCodeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.EventHandler;

public enum Shortcut {
	// DIAGRAM SHORTCUTS
	DELETE_ELEMENT("DELETE BACKSPACE", "delete the currently selected elements", Category.DIAGRAM, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return code == KeyCodes.KEY_DELETE || code == KeyCodes.KEY_BACKSPACE;
		}
	}),
	SELECT_ALL("Ctrl+A", "select all elements", Category.DIAGRAM, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return event.isControlKeyDown() && code == 'A';
		}
	}),
	DESELECT_ALL("Ctrl+Shift+A Ctrl+D", "deselect all elements", Category.DIAGRAM, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return event.isControlKeyDown() && code == 'D' ||
					event.isControlKeyDown() && event.isShiftKeyDown() && code == 'A';
		}
	}),
	COPY("Ctrl+C", "copy selected elements", Category.DIAGRAM, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return event.isControlKeyDown() && code == 'C';
		}
	}),
	CUT("Ctrl+X", "cut selected elements", Category.DIAGRAM, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return event.isControlKeyDown() && code == 'X';
		}
	}),
	PASTE("Ctrl+V", "paste cut or copied elements", Category.DIAGRAM, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return event.isControlKeyDown() && code == 'V';
		}
	}),
	SAVE("Ctrl+S", "save current diagram in browser storage", Category.DIAGRAM, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return event.isControlKeyDown() && code == 'S';
		}
	}),
	DISABLE_STICKING("SHIFT", "hold to disable sticking of elements", Category.DIAGRAM, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return code == KeyCodes.KEY_SHIFT;
		}
	}),
	MOVE_UP("Cursor ↑", "moves selected element(s) up", Category.DIAGRAM, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return code == KeyCodes.KEY_UP;
		}
	}),
	MOVE_DOWN("Cursor ↓", "moves selected element(s) down", Category.DIAGRAM, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return code == KeyCodes.KEY_DOWN;
		}
	}),
	MOVE_LEFT("Cursor ←", "moves selected element(s) left", Category.DIAGRAM, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return code == KeyCodes.KEY_LEFT;
		}
	}),
	MOVE_RIGHT("Cursor →", "moves selected element(s) right", Category.DIAGRAM, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return code == KeyCodes.KEY_RIGHT;
		}
	}),

	// BROWSER SHORTCUTS
	FULLSCREEN("F11", "switch to fullscreen", Category.BROWSER, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return KeyCodesExt.isSwitchToFullscreen(code);
		}
	}),
	ZOOM_IN("Ctrl+PLUS", "zoom diagram in", Category.BROWSER, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return event.isControlKeyDown() && KeyCodesExt.isPlus(code);
		}
	}),
	ZOOM_OUT("Ctrl+MINUS", "zoom diagram out", Category.BROWSER, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return event.isControlKeyDown() && KeyCodesExt.isMinus(code);
		}
	}),
	ZOOM_RESET("Ctrl+0", "reset diagram zoom", Category.BROWSER, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return event.isControlKeyDown() && KeyCodesExt.isZero(code);
		}
	}),

	// PROPERTIES PANEL SHORTCUTS
	SHOW_AUTOCOMPLETION("Ctrl+SPACE", "Show all autocompletion suggestions", Category.PROPERTIES, new Check() {
		@Override
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event) {
			return event.isControlKeyDown() && KeyCodesExt.isSpace(code);
		}
	}), ;

	private static interface Check {
		public boolean check(int code, KeyCodeEvent<? extends EventHandler> event);
	}

	public enum Category {
		DIAGRAM("DIAGRAM"),
		BROWSER("BROWSER (only if browser supports them)"),
		PROPERTIES("PROPERTIES PANEL");

		private String header;

		private Category(String header) {
			this.header = header;
		}

		public String getHeader() {
			return header;
		}
	}

	private String shortcut;
	private String description;
	private Category category;
	private Check check;

	Shortcut(String shortcut, String description, Category category, Check check) {
		this.shortcut = shortcut;
		this.description = description;
		this.category = category;
		this.check = check;
	}

	public boolean matches(KeyCodeEvent<? extends EventHandler> event) {
		return check.check(event.getNativeKeyCode(), event);
	}

	public String getShortcut() {
		return shortcut;
	}

	public String getDescription() {
		return description;
	}

	public Category getCategory() {
		return category;
	}
}
