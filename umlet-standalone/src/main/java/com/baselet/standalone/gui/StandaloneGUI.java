package com.baselet.standalone.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.io.File;
import java.util.Collection;
import java.util.Objects;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.baselet.control.CanCloseProgram;
import com.baselet.control.Main;
import com.baselet.control.config.Config;
import com.baselet.control.constants.Constants;
import com.baselet.control.util.Path;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.CustomPreviewHandler;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.StartUpHelpText;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.old.custom.CustomElementHandler;
import com.baselet.gui.BaseGUI;
import com.baselet.gui.pane.OwnSyntaxPane;
import com.baselet.util.logging.Logger;
import com.baselet.util.logging.LoggerFactory;

public class StandaloneGUI extends BaseGUI {

	private final Logger log = LoggerFactory.getLogger(StandaloneGUI.class);

	private JFrame mainFrame;

	private final MenuBuilder menuBuilder = new MenuBuilder();
	private final StandaloneGUIBuilder guiBuilder = new StandaloneGUIBuilder();

	private final File runningFile;

	public StandaloneGUI(CanCloseProgram main, File runningFile) {
		super(main);
		this.runningFile = runningFile;
	}

	@Override
	public void updateDiagramName(DiagramHandler diagram, String name) {
		JTabbedPane diagramtabs = guiBuilder.getDiagramtabs();
		int index = diagramtabs.indexOfComponent(diagram.getDrawPanel().getScrollPane());
		if (index != -1) {
			diagramtabs.setTitleAt(index, name);
			// update only selected tab to keep scrolling tab position
			((TabComponent) diagramtabs.getTabComponentAt(index)).updateUI();
		}
	}

	@Override
	public void setDiagramChanged(DiagramHandler diagram, boolean changed) {
		String change_string = "";
		if (changed) {
			change_string = " *";
		}

		updateDiagramName(diagram, diagram.getName() + change_string);
	}

	@Override
	public void setCustomElementChanged(CustomElementHandler handler, boolean changed) {

	}

	@Override
	public void closeWindow() {
		guiBuilder.getMailPanel().closePanel(); // We must close the mailpanel to save the input date
		if (askSaveForAllDirtyDiagrams()) {
			Path.safeDeleteFile(runningFile, true);
			main.closeProgram();
			System.exit(0); // issue #250: handle closing using a listener (see also handle closing using a listener (see also https://stackoverflow.com/questions/246228/why-does-my-application-still-run-after-closing-main-window)
		}
	}

	private boolean askSaveForAllDirtyDiagrams() {
		boolean ok = true;
		for (DiagramHandler d : Main.getInstance().getDiagrams()) {
			if (!d.askSaveIfDirty()) {
				ok = false;
			}
		}

		if (!getCurrentCustomHandler().closeEntity()) {
			ok = false;
		}
		return ok;
	}

	@Override
	protected void init() {
		mainFrame = guiBuilder.initSwingGui(menuBuilder);
	}

	@Override
	public void showPalette(String palette) {
		super.showPalette(palette);
		guiBuilder.setPaletteActive(palette);
	}

	@Override
	public String getSelectedPalette() {
		return guiBuilder.getPaletteList().getSelectedItem().toString();
	}

	@Override
	public void close(DiagramHandler diagram) {
		guiBuilder.getDiagramtabs().remove(diagram.getDrawPanel().getScrollPane());
		DrawPanel p = getCurrentDiagram();
		if (p != null) {
			CurrentDiagram.getInstance().setCurrentDiagramHandlerAndZoom(p.getHandler());
		}
		else {
			CurrentDiagram.getInstance().setCurrentDiagramHandler(null);
		}
	}

	@Override
	public void open(DiagramHandler diagram) {
		JTabbedPane diagramtabs = guiBuilder.getDiagramtabs();
		diagramtabs.add(diagram.getName(), diagram.getDrawPanel().getScrollPane());
		diagramtabs.setTabComponentAt(diagramtabs.getTabCount() - 1, new TabComponent(diagramtabs, diagram));
		jumpTo(diagram);
	}

	@Override
	public void jumpTo(DiagramHandler diagram) {
		guiBuilder.getDiagramtabs().setSelectedComponent(diagram.getDrawPanel().getScrollPane());
		diagram.getDrawPanel().getSelector().updateSelectorInformation();
		DrawPanel p = getCurrentDiagram();
		if (p != null) {
			CurrentDiagram.getInstance().setCurrentDiagramHandlerAndZoom(p.getHandler());
		}
		else {
			CurrentDiagram.getInstance().setCurrentDiagramHandler(null);
		}
	}

	@Override
	public DrawPanel getCurrentDiagram() {
		JScrollPane scr = (JScrollPane) guiBuilder.getDiagramtabs().getSelectedComponent();
		if (scr != null) {
			return (DrawPanel) scr.getViewport().getView();
		}
		else {
			return null;
		}
	}

	@Override
	public void elementsSelected(Collection<GridElement> selectedElements) {
		super.elementsSelected(selectedElements);
		menuBuilder.elementsSelected(selectedElements);
	}

	@Override
	public void enablePasteMenuEntry() {
		menuBuilder.enablePasteMenuEntry();
	}

	@Override
	public void setCustomPanelEnabled(boolean enable) {
		guiBuilder.setCustomPanelEnabled(enable);
		menuBuilder.setNewCustomElementMenuItemsEnabled(!enable); // disable "New" menu items if panel is visible
		setDrawPanelEnabled(!enable);
	}

	private void setDrawPanelEnabled(boolean enable) {
		JTabbedPane diagramtabs = guiBuilder.getDiagramtabs();
		guiBuilder.getPalettePanel().setEnabled(enable);
		for (Component c : guiBuilder.getPalettePanel().getComponents()) {
			c.setEnabled(enable);
		}
		diagramtabs.setEnabled(enable);
		for (Component c : diagramtabs.getComponents()) {
			c.setEnabled(enable);
		}
		for (int i = 0; i < diagramtabs.getTabCount(); i++) {
			diagramtabs.getTabComponentAt(i).setEnabled(enable);
		}
		guiBuilder.getSearchField().setEnabled(enable);
	}

	@Override
	public void setMailPanelEnabled(boolean enable) {
		guiBuilder.setMailPanelEnabled(enable);
	}

	@Override
	public boolean isMailPanelVisible() {
		return guiBuilder.getMailPanel().isVisible();
	}

	@Override
	public void setCustomElementSelected(boolean selected) {
		// Custom Element Edit is only enabled if a CE is selected and the panel is not visible
		menuBuilder.setEditCustomElementMenuItemEnabled(selected && !guiBuilder.getCustomPanel().isVisible());
	}

	@Override
	public void diagramSelected(DiagramHandler handler) {
		updateGrayedOutMenuItems(handler);
	}

	@Override
	public void updateGrayedOutMenuItems(DiagramHandler handler) {
		menuBuilder.updateGrayedOutMenuItems(handler);
	}

	@Override
	public void enableSearch(boolean enable) {
		guiBuilder.getSearchField().requestFocus();
	}

	@Override
	public int getMainSplitPosition() {
		return guiBuilder.getMainSplit().getDividerLocation();
	}

	@Override
	public int getRightSplitPosition() {
		return guiBuilder.getRightSplit().getDividerLocation();
	}

	@Override
	public int getMailSplitPosition() {
		return Config.getInstance().getMail_split_position(); // must return stored value in Constants, otherwise 0 will be returned in case of a closed panel
	}

	@Override
	public Frame getMainFrame() {
		return mainFrame;
	}

	@Override
	public CustomElementHandler getCurrentCustomHandler() {
		return guiBuilder.getCustomHandler();
	}

	@Override
	public OwnSyntaxPane getPropertyPane() {
		return guiBuilder.getPropertyTextPane();
	}

	@Override
	public void setValueOfZoomDisplay(int i) {
		JComboBox zoomComboBox = guiBuilder.getZoomComboBox();
		// This method should just set the value without ActionEvent therefore we remove the listener temporarily
		if (zoomComboBox != null) {
			zoomComboBox.removeActionListener(guiBuilder.getZoomListener());
			zoomComboBox.setSelectedIndex(i - 1);
			zoomComboBox.addActionListener(guiBuilder.getZoomListener());
		}
	}

	@Override
	public void focusPropertyPane() {
		guiBuilder.getPropertyTextPane().getTextComponent().requestFocus();
	}

	@Override
	public void setCursor(Cursor cursor) {
		if (mainFrame != null) {
			mainFrame.setCursor(cursor);
		}
	}

	@Override
	public void requestFocus() {
		mainFrame.requestFocus();
	}

	@Override
	public void setLookAndFeel(String newui, JFrame optionframe) {
		try {
			Frame topFrame = getMainFrame();
			boolean isDarkMode = Config.getInstance().getUiManager().equals(Constants.FLAT_DARCULA_THEME);
			DiagramHandler diagramHandler = CurrentDiagram.getInstance().getDiagramHandler();
			CustomElementHandler customElementHandler = getCurrentCustomHandler();
			OwnSyntaxPane propertyPane = getPropertyPane();

			UIManager.setLookAndFeel(newui);
			SwingUtilities.updateComponentTreeUI(topFrame);
			SwingUtilities.updateComponentTreeUI(optionframe);

			Color background = isDarkMode ? new Color(40, 40, 40) : Color.WHITE;
			Color foreground = isDarkMode ? Color.LIGHT_GRAY : Color.BLACK;

			if (diagramHandler != null && diagramHandler.getDrawPanel() != null) {
				updatePropertyPaneTheme(propertyPane, background, foreground);
				propertyPane.setLineHighlightColor(isDarkMode);
				updateStartupHelpText(diagramHandler.getDrawPanel());
			}

			if (customElementHandler != null) {
				if (customElementHandler.getCodePane() != null) {
					updateCustomElementHandlerTheme(customElementHandler, background, foreground);
					customElementHandler.getCodePane().setLineHighlightColor(isDarkMode);
				}
				if (customElementHandler.getPreviewHandler() != null) {
					updatePreviewPanelTheme(customElementHandler.getPreviewHandler(), background, foreground);
				}
			}
			topFrame.pack();
			optionframe.pack();

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			log.error("Cannot set LookAndFeel", e);
		}
	}

	private void updateStartupHelpText(DrawPanel drawPanel) {
		StartUpHelpText helpText = drawPanel.getStartupHelpText();
		if (helpText != null) {
			helpText.updateHtmlBackground();
			helpText.repaint();
		}
	}

	private void updatePreviewPanelTheme(CustomPreviewHandler handler, Color background, Color foreground) {
		if (Objects.nonNull(handler.getDrawPanel())) {
			handler.getDrawPanel().setBackground(background);
			handler.getDrawPanel().setForeground(foreground);
		}
	}

	private void updateCustomElementHandlerTheme(CustomElementHandler handler, Color background, Color foreground) {
		handler.getCodePane().getTextComponent().setBackground(background);
		handler.getCodePane().getTextComponent().setForeground(foreground);
		handler.getCodePane().getScrollPane().getGutter().setBackground(background);

		handler.getCodePane().repaint();
	}

	private void updatePropertyPaneTheme(OwnSyntaxPane pane, Color background, Color foreground) {
		pane.getTextComponent().setBackground(background);
		pane.getTextComponent().setForeground(foreground);
	}

	@Override
	public boolean hasExtendedContextMenu() {
		return true;
	}

	@Override
	public boolean saveWindowSizeInConfig() {
		return true;
	}
}
