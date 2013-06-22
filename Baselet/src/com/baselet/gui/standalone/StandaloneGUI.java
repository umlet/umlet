package com.baselet.gui.standalone;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Cursor;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.gui.BaseGUI;
import com.baselet.gui.OwnSyntaxPane;
import com.baselet.gui.TabComponent;
import com.umlet.custom.CustomElementHandler;

public class StandaloneGUI extends BaseGUI {

	private JFrame mainFrame;

	private MenuBuilder menuBuilder = new MenuBuilder();
	private StandaloneGUIBuilder guiBuilder = new StandaloneGUIBuilder();

	public StandaloneGUI(Main main) {
		super(main);
	}

	@Override
	public void updateDiagramName(DiagramHandler diagram, String name) {
		JTabbedPane diagramtabs = guiBuilder.getDiagramtabs();
		int index = diagramtabs.indexOfComponent(diagram.getDrawPanel().getScrollPane());
		if (index != -1) {
			diagramtabs.setTitleAt(index, name);
			// update only selected tab to keep scrolling tab position
			((TabComponent)diagramtabs.getTabComponentAt(index)).updateUI();
		}
	}

	@Override
	public void setDiagramChanged(DiagramHandler diagram, boolean changed) {
		String change_string = "";
		if (changed) change_string = " *";

		this.updateDiagramName(diagram, diagram.getName() + change_string);
	}

	@Override
	public void setCustomElementChanged(CustomElementHandler handler, boolean changed) {

	}

	@Override
	public void closeWindow() {
		guiBuilder.getMailPanel().closePanel(); // We must close the mailpanel to save the input date
		if (this.askSaveForAllDirtyDiagrams()) {
			this.main.closeProgram();
			this.mainFrame.dispose();
			System.exit(0);
		}
	}

	private boolean askSaveForAllDirtyDiagrams() {
		boolean ok = true;
		for (DiagramHandler d : Main.getInstance().getDiagrams()) {
			if (!d.askSaveIfDirty()) ok = false;
		}

		if (!getCurrentCustomHandler().closeEntity()) ok = false;
		return ok;
	}

	@Override
	protected void init() {
		mainFrame = guiBuilder.initSwingGui(menuBuilder);
	}

	@Override
	public void showPalette(String palette) {
		super.showPalette(palette);
		CardLayout cl = (CardLayout) (this.guiBuilder.getPalettePanel().getLayout());
		cl.show(this.guiBuilder.getPalettePanel(), palette);
	}

	@Override
	public String getSelectedPalette() {
		return this.guiBuilder.getPaletteList().getSelectedItem().toString();
	}

	@Override
	public void close(DiagramHandler diagram) {
		guiBuilder.getDiagramtabs().remove(diagram.getDrawPanel().getScrollPane());
		DrawPanel p = this.getCurrentDiagram();
		if (p != null) Main.getInstance().setCurrentDiagramHandler(p.getHandler());
		else Main.getInstance().setCurrentDiagramHandler(null);
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
		DrawPanel p = this.getCurrentDiagram();
		if (p != null) Main.getInstance().setCurrentDiagramHandler(p.getHandler());
		else Main.getInstance().setCurrentDiagramHandler(null);
	}

	@Override
	public DrawPanel getCurrentDiagram() {
		JScrollPane scr = (JScrollPane) guiBuilder.getDiagramtabs().getSelectedComponent();
		if (scr != null) return (DrawPanel) scr.getViewport().getView();
		else return null;
	}

	@Override
	public void elementsSelected(int count) {
		super.elementsSelected(count);
		menuBuilder.elementsSelected(count);
	}

	@Override
	public void enablePasteMenuEntry() {
		menuBuilder.enablePasteMenuEntry();
	}

	@Override
	public void setUngroupEnabled(boolean enabled) {
		menuBuilder.setUngroupEnabled(enabled);
	}

	@Override
	public void setCustomPanelEnabled(boolean enable) {
		guiBuilder.setCustomPanelEnabled(enable);
		menuBuilder.setNewCustomElementMenuItemsEnabled(!enable); // disable "New" menu items if panel is visible
		setDrawPanelEnabled(!enable);
	}

	private void setDrawPanelEnabled(boolean enable) {
		JTabbedPane diagramtabs = guiBuilder.getDiagramtabs();
		this.guiBuilder.getPalettePanel().setEnabled(enable);
		for (Component c : guiBuilder.getPalettePanel().getComponents())
			c.setEnabled(enable);
		diagramtabs.setEnabled(enable);
		for (Component c : diagramtabs.getComponents())
			c.setEnabled(enable);
		for (int i = 0; i < diagramtabs.getTabCount(); i++)
			diagramtabs.getTabComponentAt(i).setEnabled(enable);
		this.guiBuilder.getSearchField().setEnabled(enable);
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
		return this.guiBuilder.getMainSplit().getDividerLocation();
	}

	@Override
	public int getRightSplitPosition() {
		return this.guiBuilder.getRightSplit().getDividerLocation();
	}

	@Override
	public int getMailSplitPosition() {
		return Constants.mail_split_position; // must return stored value in Constants, otherwise 0 will be returned in case of a closed panel
	}

	public JFrame getMainFrame() {
		return this.mainFrame;
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
		mainFrame.setCursor(cursor);
	}

	@Override
	public void requestFocus() {
		mainFrame.requestFocus();
	}
}
