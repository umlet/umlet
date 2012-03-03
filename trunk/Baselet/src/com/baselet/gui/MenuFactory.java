package com.baselet.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.baselet.control.BrowserLauncher;
import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.ProgramName;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.Selector;
import com.baselet.diagram.command.Align;
import com.baselet.diagram.command.ChangeColor;
import com.baselet.diagram.command.Copy;
import com.baselet.diagram.command.CreateGroup;
import com.baselet.diagram.command.Cut;
import com.baselet.diagram.command.Paste;
import com.baselet.diagram.command.RemoveElement;
import com.baselet.diagram.command.UnGroup;
import com.baselet.element.GridElement;
import com.baselet.element.Group;
import com.baselet.gui.standalone.StandaloneGUI;
import com.umlet.custom.CustomElement;

public class MenuFactory {

	protected final static Logger log = Logger.getLogger(Utils.getClassName());

	//FILE
	public static final String FILE = "File";
	protected static final String NEW = "New";
	protected static final String OPEN = "Open...";
	protected static final String RECENT_FILES = "Recent files";
	protected static final String SAVE = "Save";
	protected static final String SAVE_AS = "Save as...";
	protected static final String EXPORT_AS = "Export as...";
	protected static final String MAIL_TO = "Mail to...";
	protected static final String EDIT_CURRENT_PALETTE = "Edit Current Palette";
	protected static final String OPTIONS = "Options...";
	protected static final String PRINT = "Print...";
	protected static final String EXIT = "Exit";

	//EDIT
	public static final String EDIT = "Edit";
	protected static final String UNDO = "Undo";
	protected static final String REDO = "Redo";
	protected static final String DELETE = "Delete";
	protected static final String SELECT_ALL = "Select All";
	protected static final String GROUP = "Group";
	protected static final String UNGROUP = "Ungroup";
	protected static final String CUT = "Cut";
	protected static final String COPY = "Copy";
	protected static final String PASTE = "Paste";

	//CUSTOM ELEMENTS
	public static final String CUSTOM_ELEMENTS = "Custom Elements";
	protected static final String NEW_CE = "New...";
	protected static final String NEW_FROM_TEMPLATE = "New from Template";
	protected static final String EDIT_SELECTED = "Edit Selected...";
	protected static final String CUSTOM_ELEMENTS_TUTORIAL = "Custom Elements Tutorial...";
	protected static final String CUSTOM_ELEMENTS_TUTORIAL_URL = "http://www.umlet.com/ce/ce.htm";

	// HELP
	public static final String HELP = "Help";
	protected static final String ONLINE_HELP = "Online Help...";
	protected static final String ONLINE_SAMPLE_DIAGRAMS = "Online Sample Diagrams...";
	protected static final String VIDEO_TUTORIAL = "Video Tutorial: Basic Use and Custom Elements";
	protected static final String PROGRAM_HOMEPAGE = Program.PROGRAM_NAME + " Homepage...";
	protected static final String RATE_PROGRAM = "Rate " + Program.PROGRAM_NAME + " at Eclipse Marketplace...";
	protected static final String ABOUT_PROGRAM = "About " + Program.PROGRAM_NAME;

	// CONTEXT ON ELEMENT
	protected static final String SET_FOREGROUND_COLOR = "Set foreground color";
	protected static final String SET_BACKGROUND_COLOR = "Set background color";
	protected static final String ALIGN = "Align";

	// OTHERS
	protected static final String SEARCH = "Search";
	protected static final String ZOOM = "Zoom to";

	protected void doAction(final String menuItem, final Object param) {
		//AB: Hopefully this will resolve threading issues and work for eclipse AND standalone
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {			
				Main main = Main.getInstance();
				BaseGUI gui = main.getGUI();
				DiagramHandler diagramHandler = gui.getCurrentDiagram().getHandler();
				DiagramHandler actualHandler = main.getDiagramHandler();
				Selector actualSelector = actualHandler == null ? null : actualHandler.getDrawPanel().getSelector();
				
				if (menuItem.equals(NEW)) {
					main.doNew();
				}
				else if (menuItem.equals(OPEN)) {
					main.doOpenFromFileChooser();
				}
				else if (menuItem.equals(RECENT_FILES)) {
					main.doOpen((String) param);
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
				else if (menuItem.equals(UNDO) && (actualHandler != null) && (actualSelector != null)) {
					actualHandler.getController().undo();
					if (gui instanceof StandaloneGUI) ((StandaloneGUI) gui).updateGrayedOutMenuItems(actualHandler);
				}
				else if (menuItem.equals(REDO) && (actualHandler != null)) {
					actualHandler.getController().redo();
					if (gui instanceof StandaloneGUI) ((StandaloneGUI) gui).updateGrayedOutMenuItems(actualHandler);
				}
				else if (menuItem.equals(DELETE) && (actualHandler != null) && (actualSelector != null)) {
					Vector<GridElement> v = actualSelector.getSelectedEntities();
					if (v.size() > 0) actualHandler.getController().executeCommand(new RemoveElement(v));
				}
				else if (menuItem.equals(SELECT_ALL) && (actualHandler != null) && (actualSelector != null)) {
					actualSelector.selectAll();
				}
				else if (menuItem.equals(GROUP) && (actualHandler != null)) {
					Main.getInstance().getDiagramHandler().getController().executeCommand(new CreateGroup());
				}
				else if (menuItem.equals(UNGROUP) && (actualHandler != null) && (actualSelector != null)) {
					Vector<GridElement> gridElements = actualSelector.getSelectedEntities();
					for (GridElement gridElement : gridElements) {
						if (gridElement instanceof Group) actualHandler.getController().executeCommand(new UnGroup((Group) gridElement));
					}
				}
				else if (menuItem.equals(CUT) && (actualHandler != null)) {
					if (!actualHandler.getDrawPanel().getAllEntities().isEmpty()) actualHandler.getController().executeCommand(new Cut());
				}
				else if (menuItem.equals(COPY) && (actualHandler != null)) {
					if (!actualHandler.getDrawPanel().getAllEntities().isEmpty()) actualHandler.getController().executeCommand(new Copy());
				}
				else if (menuItem.equals(PASTE) && (actualHandler != null)) {
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
					if ((entity != null) && (entity instanceof CustomElement)) {
						if (gui.getCurrentCustomHandler().closeEntity()) {
							gui.setCustomPanelEnabled(true);
							gui.getCurrentCustomHandler().getPanel().setCustomElementIsNew(false);
							gui.getCurrentCustomHandler().editEntity((CustomElement) entity);
						}
					}
				}
				else if (menuItem.equals(CUSTOM_ELEMENTS_TUTORIAL)) {
					BrowserLauncher.openURL(MenuFactory.CUSTOM_ELEMENTS_TUTORIAL_URL);
				}
				else if (menuItem.equals(ONLINE_HELP)) {
					BrowserLauncher.openURL(Program.WEBSITE + "/faq.htm");
				}
				else if (menuItem.equals(ONLINE_SAMPLE_DIAGRAMS)) {
					if (Program.PROGRAM_NAME == ProgramName.UMLET) BrowserLauncher.openURL("http://www.itmeyer.at/umlet/uml2/");
					else if (Program.PROGRAM_NAME == ProgramName.PLOTLET) BrowserLauncher.openURL("http://www.itmeyer.at/umlet/uml2/");
				}
				else if (menuItem.equals(VIDEO_TUTORIAL)) {
					BrowserLauncher.openURL("http://www.youtube.com/watch?v=3UHZedDtr28");
				}
				else if (menuItem.equals(PROGRAM_HOMEPAGE)) {
					BrowserLauncher.openURL(Program.WEBSITE);
				}
				else if (menuItem.equals(RATE_PROGRAM)) {
					if (Program.PROGRAM_NAME == ProgramName.UMLET) BrowserLauncher.openURL("http://marketplace.eclipse.org/content/umlet-uml-tool-fast-uml-diagrams");
					else if (Program.PROGRAM_NAME == ProgramName.PLOTLET) BrowserLauncher.openURL("http://marketplace.eclipse.org/content/plotlet");
				}
				else if (menuItem.equals(ABOUT_PROGRAM)) {
					AboutDialog.show();
				}
				else if (menuItem.equals(SET_FOREGROUND_COLOR) && (actualHandler != null)) {
					actualHandler.getController().executeCommand(new ChangeColor((String) param, true));
				}
				else if (menuItem.equals(SET_BACKGROUND_COLOR) && (actualHandler != null)) {
					actualHandler.getController().executeCommand(new ChangeColor((String) param, false));
				}
				else if (menuItem.equals(ALIGN) && (actualHandler != null) && (actualSelector != null)) {
					Vector<GridElement> v = actualSelector.getSelectedEntities();
					if (v.size() > 0) {
						actualHandler.getController().executeCommand(new Align(v, actualSelector.getDominantEntity(), (String) param));
					}
				}
			}
		});
	}
	
	// These components should only be enabled if the drawpanel is not empty
	protected List<JComponent> diagramDependendComponents = new ArrayList<JComponent>();
	public void updateDiagramDependendComponents() {
		DrawPanel currentDiagram = Main.getInstance().getGUI().getCurrentDiagram();
		if (currentDiagram == null) return; //Possible if method is called at loading a palette
		DiagramHandler handler = currentDiagram.getHandler();
		boolean enable = !((handler == null) || handler.getDrawPanel().getAllEntities().isEmpty());
		for (JComponent component : diagramDependendComponents) {
			component.setEnabled(enable);
		}

	}

}

