package com.baselet.gui;

import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.baselet.diagram.DiagramHandler;


@SuppressWarnings("serial")
public class DiagramPopupMenu extends JPopupMenu {

	public DiagramPopupMenu(final DiagramHandler handler, boolean extendedForStandaloneGUI) {
		final MenuFactorySwing menuFactory = MenuFactorySwing.getInstance();
		
		if (extendedForStandaloneGUI) { // Extended is true for StandaloneGUI
			add(menuFactory.createNew());
			add(menuFactory.createOpen());
			add(menuFactory.createRecentFiles());
			add(menuFactory.createSave());
			add(menuFactory.createSaveAs());
		}
		add(menuFactory.createExportAs());
		add(menuFactory.createMailTo());
		add(menuFactory.createPrint());
		
		addPopupMenuListener(new PopupMenuListener() {
			@Override public void popupMenuCanceled(PopupMenuEvent e) {}
			@Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
			@Override public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				menuFactory.updateDiagramDependendComponents();
			}
		});
	}
}
