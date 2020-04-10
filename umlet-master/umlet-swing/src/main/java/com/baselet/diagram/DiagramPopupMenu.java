package com.baselet.diagram;

import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.baselet.gui.menu.MenuFactorySwing;

@SuppressWarnings("serial")
public class DiagramPopupMenu extends JPopupMenu {

	public DiagramPopupMenu(boolean extendedForStandaloneGUI) {
		final MenuFactorySwing menuFactory = MenuFactorySwing.getInstance();

		add(menuFactory.createPaste());
		if (extendedForStandaloneGUI) { // Extended is true for StandaloneGUI
			add(menuFactory.createNew());
			add(menuFactory.createOpen());
			add(menuFactory.createClose());
			add(menuFactory.createRecentFiles());
			add(menuFactory.createSave());
			add(menuFactory.createSaveAs());
		}
		add(menuFactory.createExportAs());
		add(menuFactory.createMailTo());
		add(menuFactory.createPrint());

		addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				menuFactory.updateDiagramDependendComponents();
			}
		});
	}
}
