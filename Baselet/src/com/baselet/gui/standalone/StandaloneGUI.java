package com.baselet.gui.standalone;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;

import com.baselet.control.Constants;
import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.ProgramName;
import com.baselet.control.Main;
import com.baselet.control.Path;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.gui.BaseGUI;
import com.baselet.gui.OwnSyntaxPane;
import com.baselet.gui.TabComponent;
import com.baselet.gui.listener.GUIListener;
import com.umlet.custom.CustomElementHandler;

@SuppressWarnings("serial")
public class StandaloneGUI extends BaseGUI {

	private JFrame mainFrame;

	private MenuBuilder menu = new MenuBuilder();
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

		this.addKeyListener(new GUIListener());

		this.mainFrame = new JFrame();
		this.mainFrame.setContentPane(this);
		this.mainFrame.addWindowListener(new WindowListener());
		this.mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.mainFrame.setBounds(Constants.program_location.x, Constants.program_location.y, Constants.program_size.width, Constants.program_size.height);
		if (Program.PROGRAM_NAME.equals(ProgramName.UMLET)) this.mainFrame.setTitle("UMLet - Free UML Tool for Fast UML Diagrams");
		else if (Program.PROGRAM_NAME.equals(ProgramName.PLOTLET)) this.mainFrame.setTitle("Plotlet - Free Tool for Fast Plots");

		/************************ SET PROGRAM ICON ************************/
		String iconPath = Path.homeProgram() + "img/" + Program.PROGRAM_NAME.toLowerCase() + "_logo.png";
		this.mainFrame.setIconImage(new ImageIcon(iconPath).getImage());
		/*************************************************************/

		/*********** SET WINDOW BOUNDS **************/
		if (Constants.start_maximized) {
			// If Main starts maximized we set fixed bounds and must set the frame visible
			// now to avoid a bug where the right sidebar doesn't have the correct size
			this.mainFrame.setExtendedState(this.mainFrame.getExtendedState() | Frame.MAXIMIZED_BOTH);
			this.mainFrame.setVisible(true);
		}


		// Set the finished menu
		this.mainFrame.setJMenuBar(menu.createMenu(guiBuilder.createSearchPanel(), guiBuilder.createZoomPanel(), guiBuilder.createMailButton()));

		/************************ CREATE SUB PANELS ******************/

		// create custom element handler

		int mainDividerLoc = Math.min(mainFrame.getSize().width-Constants.MIN_MAIN_SPLITPANEL_SIZE, Constants.main_split_position);
		
		/********************* ADD KEYBOARD ACTIONS ************************/
		Action findaction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.getInstance().getGUI().enableSearch(true);
			}
		};
		this.getActionMap().put("focussearch", findaction);
		this.getInputMap().put(KeyStroke.getKeyStroke('/'), "focussearch");

		/************************ ADD TOP COMPONENT ************************/
		this.add(guiBuilder.initSwingGui(mainDividerLoc));
		/************************** SET DEFAULT INITIALIZATION VALUES ******/
		ToolTipManager.sharedInstance().setInitialDelay(100);
		/*************************************************************/

		this.mainFrame.setVisible(true);
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
		menu.elementsSelected(count);
	}

	@Override
	public void enablePasteMenuEntry() {
		menu.enablePasteMenuEntry();
	}

	@Override
	public void setUngroupEnabled(boolean enabled) {
		menu.setUngroupEnabled(enabled);
	}

	@Override
	public void setCustomPanelEnabled(boolean enable) {
		guiBuilder.setCustomPanelEnabled(enable);
		menu.setCustomPanelEnabled(enable);
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
		menu.setCustomElementSelected(selected, guiBuilder.getCustomPanel().isEnabled());
	}

	@Override
	public void diagramSelected(DiagramHandler handler) {
		updateGrayedOutMenuItems(handler);
	}

	public void updateGrayedOutMenuItems(DiagramHandler handler) {
	menu.updateGrayedOutMenuItems(handler);
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
}
