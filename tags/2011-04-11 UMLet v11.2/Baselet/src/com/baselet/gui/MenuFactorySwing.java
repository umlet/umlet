package com.baselet.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.baselet.control.Constants;
import com.baselet.control.Constants.SystemInfo;
import com.baselet.control.Main;

public class MenuFactorySwing extends MenuFactory {

	private static MenuFactorySwing instance = null;
	public static MenuFactorySwing getInstance() {
		if (instance == null) instance = new MenuFactorySwing();
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
			@Override public void menuDeselected(MenuEvent e) {}
			@Override public void menuCanceled(MenuEvent e) {}
			@Override public void menuSelected(MenuEvent e) {
				recentFiles.removeAll();
				for (final String file : Main.getInstance().getRecentFiles()) {
					recentFiles.add(createJMenuItem(false, file, RECENT_FILES, file));
				}
			}
		});
		return recentFiles;
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
		return createJMenuItem(false, DELETE, KeyEvent.VK_DELETE, false, null);
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

	public JMenuItem createProgramHomepage() {
		return createJMenuItem(false, PROGRAM_HOMEPAGE, null);
	}

	public JMenuItem createRateProgram() {
		return createJMenuItem(false, RATE_PROGRAM, null);
	}

	public JMenu createSetColor(boolean fg) {
		String name = (fg == true ? SET_FOREGROUND_COLOR : SET_BACKGROUND_COLOR);
		JMenu menu = new JMenu(name);
		menu.add(createJMenuItem(false, "default", name, "default"));
		for (String color : Constants.colorMap.keySet()) {
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
		for (String direction : new String[]{"Left", "Right", "Top", "Bottom"}) {
			alignMenu.add(createJMenuItem(false, direction, ALIGN, direction));
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
			menuItem.setAccelerator(KeyStroke.getKeyStroke(mnemonic, (!meta ? 0 : SystemInfo.META_KEY.getMask())));
		}
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doAction(actionName, param);
			}
		});
		if (grayWithoutDiagram) diagramDependendComponents.add(menuItem);
		return menuItem;
	}
}
