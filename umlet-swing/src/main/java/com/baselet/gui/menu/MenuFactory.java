package com.baselet.gui.menu;

import static com.baselet.control.constants.MenuConstants.ABOUT_PROGRAM;
import static com.baselet.control.constants.MenuConstants.ALIGN;
import static com.baselet.control.constants.MenuConstants.CLOSE;
import static com.baselet.control.constants.MenuConstants.COPY;
import static com.baselet.control.constants.MenuConstants.CUSTOM_ELEMENTS_TUTORIAL;
import static com.baselet.control.constants.MenuConstants.CUSTOM_ELEMENTS_TUTORIAL_URL;
import static com.baselet.control.constants.MenuConstants.CUT;
import static com.baselet.control.constants.MenuConstants.DELETE;
import static com.baselet.control.constants.MenuConstants.EDIT_CURRENT_PALETTE;
import static com.baselet.control.constants.MenuConstants.EDIT_SELECTED;
import static com.baselet.control.constants.MenuConstants.EXIT;
import static com.baselet.control.constants.MenuConstants.EXPORT;
import static com.baselet.control.constants.MenuConstants.EXPORT_AS;
import static com.baselet.control.constants.MenuConstants.GENERATE_CLASS;
import static com.baselet.control.constants.MenuConstants.GENERATE_CLASS_OPTIONS;
import static com.baselet.control.constants.MenuConstants.GROUP;
import static com.baselet.control.constants.MenuConstants.LAYER;
import static com.baselet.control.constants.MenuConstants.LAYER_DOWN;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.baselet.control.Main;
import com.baselet.control.constants.FacetConstants;
import com.baselet.control.enums.Program;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.SelectorOld;
import com.baselet.diagram.io.ClassChooser;
import com.baselet.element.facet.common.GroupFacet;
import com.baselet.element.facet.common.LayerFacet;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.old.custom.CustomElement;
import com.baselet.generator.ClassDiagramConverter;
import com.baselet.gui.BaseGUI;
import com.baselet.gui.BrowserLauncher;
import com.baselet.gui.CurrentGui;
import com.baselet.gui.ExportHandler;
import com.baselet.gui.OptionPanel;
import com.baselet.gui.command.Align;
import com.baselet.gui.command.ChangeElementSetting;
import com.baselet.gui.command.Copy;
import com.baselet.gui.command.Cut;
import com.baselet.gui.command.Paste;
import com.baselet.gui.command.RemoveElement;

public class MenuFactory {

	public void doAction(final String menuItem, final Object param) {
		// AB: Hopefully this will resolve threading issues and work for eclipse AND standalone
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Main main = Main.getInstance();
				BaseGUI gui = CurrentGui.getInstance().getGui();
				DiagramHandler diagramHandler = gui.getCurrentDiagram().getHandler();
				DiagramHandler actualHandler = CurrentDiagram.getInstance().getDiagramHandler();
				SelectorOld actualSelector = actualHandler == null ? null : actualHandler.getDrawPanel().getSelector();

				if (menuItem.equals(NEW)) {
					main.doNew();
				}
				else if (menuItem.equals(OPEN)) {
					main.doOpenFromFileChooser();
				}
				else if (menuItem.equals(CLOSE)) {
					diagramHandler.doCloseAndAddNewIfNoLeft();
				}
				else if (menuItem.equals(RECENT_FILES)) {
					main.doOpen((String) param);
				}
				else if (menuItem.equals(GENERATE_CLASS)) {
					new ClassDiagramConverter().createClassDiagrams(ClassChooser.getFilesToOpen());
				}
				else if (menuItem.equals(GENERATE_CLASS_OPTIONS)) {
					GenerateOptionPanel.getInstance().showPanel();
				}
				else if (menuItem.equals(SAVE) && diagramHandler != null) {
					diagramHandler.doSave();
				}
				else if (menuItem.equals(SAVE_AS) && diagramHandler != null) {
					diagramHandler.doSaveAs(Program.getInstance().getExtension());
				}
				else if (menuItem.equals(EXPORT) && diagramHandler != null) {
					ExportHandler.getInstance().export();
				}
				else if (menuItem.equals(EXPORT_AS) && diagramHandler != null) {
					String exportedDiagramFilePath = diagramHandler.doSaveAs((String) param);
					if (exportedDiagramFilePath != null && !exportedDiagramFilePath.isEmpty()) {
						ExportHandler.getInstance().setExportedFilePath(exportedDiagramFilePath);
					}
				}
				else if (menuItem.equals(MAIL_TO)) {
					gui.setMailPanelEnabled(!gui.isMailPanelVisible());
				}
				else if (menuItem.equals(EDIT_CURRENT_PALETTE)) {
					main.doOpen(main.getPalette().getFileHandler().getFullPathName());
				}
				else if (menuItem.equals(OPTIONS)) {
					OptionPanel.getInstance().showOptionPanel();
				}
				else if (menuItem.equals(PRINT) && diagramHandler != null) {
					diagramHandler.doPrint();
				}
				else if (menuItem.equals(EXIT)) {
					CurrentGui.getInstance().getGui().closeWindow();
				}
				else if (menuItem.equals(UNDO) && actualHandler != null && actualSelector != null) {
					actualHandler.getController().undo();
				}
				else if (menuItem.equals(REDO) && actualHandler != null) {
					actualHandler.getController().redo();
				}
				else if (menuItem.equals(DELETE) && actualHandler != null && actualSelector != null) {
					List<GridElement> v = actualSelector.getSelectedElements();
					if (v.size() > 0) {
						actualHandler.getController().executeCommand(new RemoveElement(v));
					}
				}
				else if (menuItem.equals(SELECT_ALL) && actualHandler != null && actualSelector != null) {
					actualSelector.selectAll();
				}
				else if (menuItem.equals(GROUP) && actualHandler != null && actualSelector != null) {
					actualHandler.getController().executeCommand(new ChangeElementSetting(GroupFacet.KEY, actualSelector.getUnusedGroup().toString(), actualSelector.getSelectedElements()));
				}
				else if (menuItem.equals(UNGROUP) && actualHandler != null && actualSelector != null) {
					actualHandler.getController().executeCommand(new ChangeElementSetting(GroupFacet.KEY, null, actualSelector.getSelectedElements()));
				}
				else if (menuItem.equals(CUT) && actualHandler != null) {
					if (!actualHandler.getDrawPanel().getGridElements().isEmpty()) {
						actualHandler.getController().executeCommand(new Cut());
					}
				}
				else if (menuItem.equals(COPY) && actualHandler != null) {
					if (!actualHandler.getDrawPanel().getGridElements().isEmpty()) {
						actualHandler.getController().executeCommand(new Copy());
					}
				}
				else if (menuItem.equals(PASTE) && actualHandler != null) {
					actualHandler.getController().executeCommand(new Paste());
				}
				else if (menuItem.equals(NEW_CE)) {
					if (gui.getCurrentCustomHandler().closeEntity()) {
						gui.setCustomPanelEnabled(true);
						gui.getCurrentCustomHandler().getPanel().setCustomElementIsNew(true);
						gui.getCurrentCustomHandler().newEntity();
					}
				}
				else if (menuItem.equals(NEW_FROM_TEMPLATE)) {
					if (gui.getCurrentCustomHandler().closeEntity()) {
						gui.setCustomPanelEnabled(true);
						gui.getCurrentCustomHandler().getPanel().setCustomElementIsNew(true);
						gui.getCurrentCustomHandler().newEntity((String) param);
					}
				}
				else if (menuItem.equals(EDIT_SELECTED)) {
					GridElement entity = main.getEditedGridElement();
					if (entity != null && entity instanceof CustomElement) {
						if (gui.getCurrentCustomHandler().closeEntity()) {
							gui.setCustomPanelEnabled(true);
							gui.getCurrentCustomHandler().getPanel().setCustomElementIsNew(false);
							gui.getCurrentCustomHandler().editEntity((CustomElement) entity);
						}
					}
				}
				else if (menuItem.equals(CUSTOM_ELEMENTS_TUTORIAL)) {
					BrowserLauncher.openURL(CUSTOM_ELEMENTS_TUTORIAL_URL);
				}
				else if (menuItem.equals(ONLINE_HELP)) {
					BrowserLauncher.openURL(Program.getInstance().getWebsite() + "/faq.htm");
				}
				else if (menuItem.equals(ONLINE_SAMPLE_DIAGRAMS)) {
					BrowserLauncher.openURL("http://www.itmeyer.at/umlet/uml2/");
				}
				else if (menuItem.equals(VIDEO_TUTORIAL)) {
					BrowserLauncher.openURL("http://www.youtube.com/watch?v=3UHZedDtr28");
				}
				else if (menuItem.equals(PROGRAM_HOMEPAGE)) {
					BrowserLauncher.openURL(Program.getInstance().getWebsite());
				}
				else if (menuItem.equals(RATE_PROGRAM)) {
					BrowserLauncher.openURL("http://marketplace.eclipse.org/content/umlet-uml-tool-fast-uml-diagrams");
				}
				else if (menuItem.equals(ABOUT_PROGRAM)) {
					AboutDialog.show();
				}
				else if (menuItem.equals(SET_FOREGROUND_COLOR) && actualHandler != null && actualSelector != null) {
					actualHandler.getController().executeCommand(new ChangeElementSetting(FacetConstants.FOREGROUND_COLOR_KEY, (String) param, actualSelector.getSelectedElements()));
				}
				else if (menuItem.equals(SET_BACKGROUND_COLOR) && actualHandler != null && actualSelector != null) {
					actualHandler.getController().executeCommand(new ChangeElementSetting(FacetConstants.BACKGROUND_COLOR_KEY, (String) param, actualSelector.getSelectedElements()));
				}
				else if (menuItem.equals(ALIGN) && actualHandler != null && actualSelector != null) {
					List<GridElement> v = actualSelector.getSelectedElements();
					if (v.size() > 0) {
						actualHandler.getController().executeCommand(new Align(v, actualSelector.getDominantEntity(), (String) param));
					}
				}
				else if (menuItem.equals(LAYER) && actualHandler != null && actualSelector != null) {
					int change = param.equals(LAYER_DOWN) ? -1 : +1;
					Map<GridElement, String> valueMap = new HashMap<GridElement, String>();
					for (GridElement e : actualSelector.getSelectedElements()) {
						valueMap.put(e, Integer.toString(e.getLayer() + change));
					}
					actualHandler.getController().executeCommand(new ChangeElementSetting(LayerFacet.KEY, valueMap));
				}
			}
		});
	}

	// These components should only be enabled if the drawpanel is not empty
	protected List<JComponent> diagramDependendComponents = new ArrayList<JComponent>();

	public void updateDiagramDependendComponents() {
		DrawPanel currentDiagram = CurrentGui.getInstance().getGui().getCurrentDiagram();
		if (currentDiagram == null) {
			return; // Possible if method is called at loading a palette
		}
		DiagramHandler handler = currentDiagram.getHandler();
		boolean enable = !(handler == null || handler.getDrawPanel().getGridElements().isEmpty());
		for (JComponent component : diagramDependendComponents) {
			component.setEnabled(enable);
		}

	}

}
