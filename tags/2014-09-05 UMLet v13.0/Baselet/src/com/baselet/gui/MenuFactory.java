package com.baselet.gui;

import static com.baselet.control.MenuConstants.ABOUT_PROGRAM;
import static com.baselet.control.MenuConstants.ALIGN;
import static com.baselet.control.MenuConstants.COPY;
import static com.baselet.control.MenuConstants.CUSTOM_ELEMENTS_TUTORIAL;
import static com.baselet.control.MenuConstants.CUSTOM_ELEMENTS_TUTORIAL_URL;
import static com.baselet.control.MenuConstants.CUT;
import static com.baselet.control.MenuConstants.DELETE;
import static com.baselet.control.MenuConstants.EDIT_CURRENT_PALETTE;
import static com.baselet.control.MenuConstants.EDIT_SELECTED;
import static com.baselet.control.MenuConstants.EXIT;
import static com.baselet.control.MenuConstants.EXPORT_AS;
import static com.baselet.control.MenuConstants.GENERATE_CLASS;
import static com.baselet.control.MenuConstants.GENERATE_CLASS_OPTIONS;
import static com.baselet.control.MenuConstants.GROUP;
import static com.baselet.control.MenuConstants.LAYER;
import static com.baselet.control.MenuConstants.LAYER_DOWN;
import static com.baselet.control.MenuConstants.MAIL_TO;
import static com.baselet.control.MenuConstants.NEW;
import static com.baselet.control.MenuConstants.NEW_CE;
import static com.baselet.control.MenuConstants.NEW_FROM_TEMPLATE;
import static com.baselet.control.MenuConstants.ONLINE_HELP;
import static com.baselet.control.MenuConstants.ONLINE_SAMPLE_DIAGRAMS;
import static com.baselet.control.MenuConstants.OPEN;
import static com.baselet.control.MenuConstants.OPTIONS;
import static com.baselet.control.MenuConstants.PASTE;
import static com.baselet.control.MenuConstants.PRINT;
import static com.baselet.control.MenuConstants.PROGRAM_HOMEPAGE;
import static com.baselet.control.MenuConstants.RATE_PROGRAM;
import static com.baselet.control.MenuConstants.RECENT_FILES;
import static com.baselet.control.MenuConstants.REDO;
import static com.baselet.control.MenuConstants.SAVE;
import static com.baselet.control.MenuConstants.SAVE_AS;
import static com.baselet.control.MenuConstants.SELECT_ALL;
import static com.baselet.control.MenuConstants.SET_BACKGROUND_COLOR;
import static com.baselet.control.MenuConstants.SET_FOREGROUND_COLOR;
import static com.baselet.control.MenuConstants.UNDO;
import static com.baselet.control.MenuConstants.UNGROUP;
import static com.baselet.control.MenuConstants.VIDEO_TUTORIAL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.baselet.control.BrowserLauncher;
import com.baselet.control.Main;
import com.baselet.control.SharedConstants.Program;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.SelectorOld;
import com.baselet.diagram.command.Align;
import com.baselet.diagram.command.ChangeElementSetting;
import com.baselet.diagram.command.Copy;
import com.baselet.diagram.command.Cut;
import com.baselet.diagram.command.Paste;
import com.baselet.diagram.command.RemoveElement;
import com.baselet.diagram.io.ClassChooser;
import com.baselet.element.GridElement;
import com.baselet.elementnew.facet.common.BackgroundColorFacet;
import com.baselet.elementnew.facet.common.ForegroundColorFacet;
import com.baselet.elementnew.facet.common.GroupFacet;
import com.baselet.elementnew.facet.common.LayerFacet;
import com.baselet.gui.standalone.StandaloneGUI;
import com.umlet.custom.CustomElement;
import com.umlet.language.ClassDiagramConverter;

public class MenuFactory {

	protected void doAction(final String menuItem, final Object param) {
		// AB: Hopefully this will resolve threading issues and work for eclipse AND standalone
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Main main = Main.getInstance();
				BaseGUI gui = main.getGUI();
				DiagramHandler diagramHandler = gui.getCurrentDiagram().getHandler();
				DiagramHandler actualHandler = main.getDiagramHandler();
				SelectorOld actualSelector = actualHandler == null ? null : actualHandler.getDrawPanel().getSelector();

				if (menuItem.equals(NEW)) {
					main.doNew();
				}
				else if (menuItem.equals(OPEN)) {
					main.doOpenFromFileChooser();
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
					diagramHandler.doSaveAs(Program.EXTENSION);
				}
				else if (menuItem.equals(EXPORT_AS) && diagramHandler != null) {
					diagramHandler.doSaveAs((String) param);
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
					main.getGUI().closeWindow();
				}
				else if (menuItem.equals(UNDO) && actualHandler != null && actualSelector != null) {
					actualHandler.getController().undo();
					if (gui instanceof StandaloneGUI) {
						((StandaloneGUI) gui).updateGrayedOutMenuItems(actualHandler);
					}
				}
				else if (menuItem.equals(REDO) && actualHandler != null) {
					actualHandler.getController().redo();
					if (gui instanceof StandaloneGUI) {
						((StandaloneGUI) gui).updateGrayedOutMenuItems(actualHandler);
					}
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
					BrowserLauncher.openURL(Program.WEBSITE + "/faq.htm");
				}
				else if (menuItem.equals(ONLINE_SAMPLE_DIAGRAMS)) {
					BrowserLauncher.openURL("http://www.itmeyer.at/umlet/uml2/");
				}
				else if (menuItem.equals(VIDEO_TUTORIAL)) {
					BrowserLauncher.openURL("http://www.youtube.com/watch?v=3UHZedDtr28");
				}
				else if (menuItem.equals(PROGRAM_HOMEPAGE)) {
					BrowserLauncher.openURL(Program.WEBSITE);
				}
				else if (menuItem.equals(RATE_PROGRAM)) {
					BrowserLauncher.openURL("http://marketplace.eclipse.org/content/umlet-uml-tool-fast-uml-diagrams");
				}
				else if (menuItem.equals(ABOUT_PROGRAM)) {
					AboutDialog.show();
				}
				else if (menuItem.equals(SET_FOREGROUND_COLOR) && actualHandler != null && actualSelector != null) {
					actualHandler.getController().executeCommand(new ChangeElementSetting(ForegroundColorFacet.KEY, (String) param, actualSelector.getSelectedElements()));
				}
				else if (menuItem.equals(SET_BACKGROUND_COLOR) && actualHandler != null && actualSelector != null) {
					actualHandler.getController().executeCommand(new ChangeElementSetting(BackgroundColorFacet.KEY, (String) param, actualSelector.getSelectedElements()));
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
		DrawPanel currentDiagram = Main.getInstance().getGUI().getCurrentDiagram();
		if (currentDiagram == null)
		{
			return; // Possible if method is called at loading a palette
		}
		DiagramHandler handler = currentDiagram.getHandler();
		boolean enable = !(handler == null || handler.getDrawPanel().getGridElements().isEmpty());
		for (JComponent component : diagramDependendComponents) {
			component.setEnabled(enable);
		}

	}

}
