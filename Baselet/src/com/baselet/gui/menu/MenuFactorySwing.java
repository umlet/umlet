package com.baselet.gui.menu;

import static com.baselet.control.constants.MenuConstants.ABOUT_PROGRAM;
import static com.baselet.control.constants.MenuConstants.ALIGN;
import static com.baselet.control.constants.MenuConstants.COPY;
import static com.baselet.control.constants.MenuConstants.CUSTOM_ELEMENTS_TUTORIAL;
import static com.baselet.control.constants.MenuConstants.CUT;
import static com.baselet.control.constants.MenuConstants.DELETE;
import static com.baselet.control.constants.MenuConstants.EDIT_CURRENT_PALETTE;
import static com.baselet.control.constants.MenuConstants.EDIT_SELECTED;
import static com.baselet.control.constants.MenuConstants.EXIT;
import static com.baselet.control.constants.MenuConstants.EXPORT_AS;
import static com.baselet.control.constants.MenuConstants.GENERATE_CLASS;
import static com.baselet.control.constants.MenuConstants.GENERATE_CLASS_OPTIONS;
import static com.baselet.control.constants.MenuConstants.GROUP;
import static com.baselet.control.constants.MenuConstants.LAYER;
import static com.baselet.control.constants.MenuConstants.LAYER_DOWN;
import static com.baselet.control.constants.MenuConstants.LAYER_UP;
import static com.baselet.control.constants.MenuConstants.MAIL_TO;
import static com.baselet.control.constants.MenuConstants.NEW;
import static com.baselet.control.constants.MenuConstants.NEW_CE;
import static com.baselet.control.constants.MenuConstants.NEW_FROM_TEMPLATE;
import static com.baselet.control.constants.MenuConstants.ONLINE_HELP;
import static com.baselet.control.constants.MenuConstants.ONLINE_SAMPLE_DIAGRAMS;
import static com.baselet.control.constants.MenuConstants.OPEN;
import static com.baselet.control.constants.MenuConstants.OPTIONS;
import static com.baselet.control.constants.MenuConstants.PASTE;
import static com.baselet.control.constants.MenuConstants.PRINT;
import static com.baselet.control.constants.MenuConstants.PROGRAM_HOMEPAGE;
import static com.baselet.control.constants.MenuConstants.RATE_PROGRAM;
import static com.baselet.control.constants.MenuConstants.RECENT_FILES;
import static com.baselet.control.constants.MenuConstants.REDO;
import static com.baselet.control.constants.MenuConstants.SAVE;
import static com.baselet.control.constants.MenuConstants.SAVE_AS;
import static com.baselet.control.constants.MenuConstants.SELECT_ALL;
import static com.baselet.control.constants.MenuConstants.SET_BACKGROUND_COLOR;
import static com.baselet.control.constants.MenuConstants.SET_FOREGROUND_COLOR;
import static com.baselet.control.constants.MenuConstants.UNDO;
import static com.baselet.control.constants.MenuConstants.UNGROUP;
import static com.baselet.control.constants.MenuConstants.VIDEO_TUTORIAL;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.baselet.control.Main;
import com.baselet.control.constants.Constants;
import com.baselet.control.constants.SystemInfo;
import com.baselet.control.enums.Os;
import com.baselet.control.util.RecentlyUsedFilesList;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.gui.helper.PlainColorIcon;

public class MenuFactorySwing extends MenuFactory {

	private static MenuFactorySwing instance = null;

	public static MenuFactorySwing getInstance() {
		if (instance == null) {
			instance = new MenuFactorySwing();
		}
		return instance;
	}

	public JMenuItem createNew() {
		return createJMenuItem(false, NEW, KeyEvent.VK_N, true, null);
	}

	public JMenuItem createOpen() {
		return createJMenuItem(false, OPEN, KeyEvent.VK_O, true, null);
	}

	public JMenu createRecentFiles() {
		final JMenu recentFiles = new JMenu();
		recentFiles.setText(RECENT_FILES);
		recentFiles.addMenuListener(new MenuListener() {
			@Override
			public void menuDeselected(MenuEvent e) {}

			@Override
			public void menuCanceled(MenuEvent e) {}

			@Override
			public void menuSelected(MenuEvent e) {
				recentFiles.removeAll();
				for (String file : RecentlyUsedFilesList.getInstance()) {
					recentFiles.add(createJMenuItem(false, file, RECENT_FILES, file));
				}
			}
		});
		return recentFiles;
	}

	public JMenuItem createGenerate() {
		return createJMenuItem(false, GENERATE_CLASS, null);
	}

	public JMenuItem createGenerateOptions() {
		return createJMenuItem(false, GENERATE_CLASS_OPTIONS, null);
	}

	public JMenuItem createSave() {
		return createJMenuItem(true, SAVE, KeyEvent.VK_S, true, null);
	}

	public JMenuItem createSaveAs() {
		return createJMenuItem(true, SAVE_AS, null);
	}

	public JMenu createExportAs() {
		final JMenu export = new JMenu();
		export.setText(EXPORT_AS);
		diagramDependendComponents.add(export);
		for (final String format : Constants.exportFormatList) {
			export.add(createJMenuItem(true, format.toUpperCase() + "...", EXPORT_AS, format));
		}
		return export;
	}

	public JMenuItem createMailTo() {
		return createJMenuItem(true, MAIL_TO, KeyEvent.VK_M, true, null);
	}

	public JMenuItem createEditCurrentPalette() {
		return createJMenuItem(false, EDIT_CURRENT_PALETTE, null);
	}

	public JMenuItem createOptions() {
		return createJMenuItem(false, OPTIONS, null);
	}

	public JMenuItem createPrint() {
		return createJMenuItem(true, PRINT, KeyEvent.VK_P, true, null);
	}

	public JMenuItem createExit() {
		return createJMenuItem(false, EXIT, null);
	}

	public JMenuItem createUndo() {
		return createJMenuItem(false, UNDO, KeyEvent.VK_Z, true, null);
	}

	public JMenuItem createRedo() {
		return createJMenuItem(false, REDO, KeyEvent.VK_Y, true, null);
	}

	public JMenuItem createDelete() {
		int[] keys = new int[] { KeyEvent.VK_BACK_SPACE, KeyEvent.VK_DELETE }; // backspace AND delete both work for deleting elements
		if (SystemInfo.OS == Os.MAC) { // MacOS shows the backspace key mapping because it's the only one working - see http://stackoverflow.com/questions/4881262/java-keystroke-for-delete/4881606#4881606
			return createJMenuItem(false, DELETE, keys, KeyEvent.VK_BACK_SPACE);
		}
		else {
			return createJMenuItem(false, DELETE, keys, KeyEvent.VK_DELETE);
		}
	}

	public JMenuItem createSelectAll() {
		return createJMenuItem(false, SELECT_ALL, KeyEvent.VK_A, true, null);
	}

	public JMenuItem createGroup() {
		return createJMenuItem(false, GROUP, KeyEvent.VK_G, true, null);
	}

	public JMenuItem createUngroup() {
		return createJMenuItem(false, UNGROUP, KeyEvent.VK_U, true, null);
	}

	public JMenuItem createCut() {
		return createJMenuItem(false, CUT, KeyEvent.VK_X, true, null);
	}

	public JMenuItem createCopy() {
		return createJMenuItem(false, COPY, KeyEvent.VK_C, true, null);
	}

	public JMenuItem createPaste() {
		return createJMenuItem(false, PASTE, KeyEvent.VK_V, true, null);
	}

	public JMenuItem createNewCustomElement() {
		return createJMenuItem(false, NEW_CE, null);
	}

	public JMenu createNewCustomElementFromTemplate() {
		JMenu menu = new JMenu(NEW_FROM_TEMPLATE);
		for (String template : Main.getInstance().getTemplateNames()) {
			menu.add(createJMenuItem(false, template, NEW_FROM_TEMPLATE, template));
		}
		return menu;
	}

	public JMenuItem createEditSelected() {
		return createJMenuItem(false, EDIT_SELECTED, null);
	}

	public JMenuItem createCustomElementTutorial() {
		return createJMenuItem(false, CUSTOM_ELEMENTS_TUTORIAL, null);
	}

	public JMenuItem createOnlineHelp() {
		return createJMenuItem(false, ONLINE_HELP, null);
	}

	public JMenuItem createOnlineSampleDiagrams() {
		return createJMenuItem(false, ONLINE_SAMPLE_DIAGRAMS, null);
	}

	public JMenuItem createVideoTutorials() {
		return createJMenuItem(false, VIDEO_TUTORIAL, null);
	}

	public JMenuItem createProgramHomepage() {
		return createJMenuItem(false, PROGRAM_HOMEPAGE, null);
	}

	public JMenuItem createRateProgram() {
		return createJMenuItem(false, RATE_PROGRAM, null);
	}

	public JMenu createSetColor(boolean fg) {
		String name = fg ? SET_FOREGROUND_COLOR : SET_BACKGROUND_COLOR;
		JMenu menu = new JMenu(name);
		menu.add(createJMenuItem(false, "default", name, null));
		for (String color : ColorOwn.COLOR_MAP.keySet()) {
			JMenuItem item = createJMenuItem(false, color, name, color);
			menu.add(item);
			item.setIcon(new PlainColorIcon(color));
		}
		return menu;
	}

	public JMenuItem createAboutProgram() {
		return createJMenuItem(false, ABOUT_PROGRAM, null);
	}

	public JMenu createAlign() {
		JMenu alignMenu = new JMenu(ALIGN);
		for (String direction : new String[] { "Left", "Right", "Top", "Bottom" }) {
			alignMenu.add(createJMenuItem(false, direction, ALIGN, direction));
		}
		return alignMenu;
	}

	public JMenu createLayerUp() {
		JMenu alignMenu = new JMenu(LAYER);
		for (String direction : new String[] { LAYER_DOWN, LAYER_UP }) {
			alignMenu.add(createJMenuItem(false, direction, LAYER, direction));
		}
		return alignMenu;
	}

	private JMenuItem createJMenuItem(boolean grayWithoutDiagram, final String name, Object param) {
		return createJMenuItem(grayWithoutDiagram, name, name, null, null, param);
	}

	private JMenuItem createJMenuItem(boolean grayWithoutDiagram, final String name, Integer mnemonic, Boolean meta, Object param) {
		return createJMenuItem(grayWithoutDiagram, name, name, mnemonic, meta, param);
	}

	private JMenuItem createJMenuItem(boolean grayWithoutDiagram, final String menuName, final String actionName, final Object param) {
		return createJMenuItem(grayWithoutDiagram, menuName, actionName, null, null, param);
	}

	private JMenuItem createJMenuItem(boolean grayWithoutDiagram, final String menuName, final String actionName, Integer mnemonic, Boolean meta, final Object param) {
		JMenuItem menuItem = new JMenuItem(menuName);
		if (mnemonic != null) {
			menuItem.setMnemonic(mnemonic);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(mnemonic, !meta ? 0 : SystemInfo.META_KEY.getMask()));
		}
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doAction(actionName, param);
			}
		});
		if (grayWithoutDiagram) {
			diagramDependendComponents.add(menuItem);
		}
		return menuItem;
	}

	/**
	 * Create a JMenuItem with multiple key bindings (only one mnemonic can be set at any time).
	 * @see "http://docs.oracle.com/javase/tutorial/uiswing/misc/action.html"
	 */
	private JMenuItem createJMenuItem(boolean grayWithoutDiagram, final String name, int[] keyEvents, int preferredMnemonic) {
		JMenuItem menuItem = new JMenuItem(name);

		MultipleKeyBindingsAction action = new MultipleKeyBindingsAction(name, preferredMnemonic);
		for (int keyEvent : keyEvents) {
			addKeyBinding(menuItem, keyEvent, name);
		}
		menuItem.getActionMap().put(name, action);
		menuItem.setAction(action);

		if (grayWithoutDiagram) {
			diagramDependendComponents.add(menuItem);
		}
		return menuItem;
	}

	private void addKeyBinding(JMenuItem menuItem, int keyEvent, String actionName) {
		menuItem.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyEvent, 0), actionName);
	}

	@SuppressWarnings("serial")
	private class MultipleKeyBindingsAction extends AbstractAction {

		public MultipleKeyBindingsAction(String menuName, int preferredMnemonic) {
			super(menuName);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(preferredMnemonic, 0));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			doAction(getValue(NAME).toString(), null);
		}
	}
}
