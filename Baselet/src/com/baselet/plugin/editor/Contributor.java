package com.baselet.plugin.editor;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorActionBarContributor;

import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.ProgramName;
import com.baselet.control.Constants.RuntimeType;
import com.baselet.control.Main;
import com.baselet.gui.MenuFactory;
import com.baselet.gui.eclipse.EclipseGUI;
import com.baselet.gui.eclipse.EclipseGUI.Pane;
import com.baselet.gui.eclipse.MenuFactoryEclipse;
import com.baselet.gui.eclipse.UpdateActionBars;
import com.baselet.plugin.MainPlugin;

public class Contributor extends EditorActionBarContributor {

	public enum ActionName {
		COPY, CUT, PASTE, SELECTALL
	}

	private MenuFactoryEclipse menuFactory = MenuFactoryEclipse.getInstance();

	private IAction customnew;
	private IAction customedit;
	private IAction undoActionGlobal;
	private IAction redoActionGlobal;
	private IAction printActionGlobal;
	private IAction copyActionDiagram;
	private IAction cutActionDiagram;
	private IAction pasteActionDiagram;
	private IAction deleteActionDiagram;
	private IAction selectallActionDiagram;
	private IAction searchActionDiagram;
	private IAction copyActionPropPanel;
	private IAction cutActionPropPanel;
	private IAction pasteActionPropPanel;
	private IAction selectAllActionPropPanel;
	private IAction copyActionCustomPanel;
	private IAction cutActionCustomPanel;
	private IAction pasteActionCustomPanel;
	private IAction selectAllActionCustomPanel;

	private List<IAction> exportAsActionList;

	private boolean customPanelEnabled;
	private boolean custom_element_selected;

	private IMenuManager zoomMenu;

	public Contributor() {
		this.customPanelEnabled = false;
		this.custom_element_selected = false;
	}

	private Action createPanelAction(final Pane pane, final ActionName action) {
		Action copyActionPropPanel = new Action() {
			@Override
			public void run() {
				((EclipseGUI) Main.getInstance().getGUI()).panelDoAction(pane, action);
			}
		};
		return copyActionPropPanel;
	}

	@Override
	public void init(IActionBars actionBars) {
		super.init(actionBars);

		customedit = menuFactory.createEditSelected();
		customedit.setEnabled(false);

		undoActionGlobal = menuFactory.createUndo();
		redoActionGlobal = menuFactory.createRedo();
		printActionGlobal = menuFactory.createPrint();

		cutActionDiagram = menuFactory.createCut();
		cutActionDiagram.setEnabled(false);
		pasteActionDiagram = menuFactory.createPaste();
		pasteActionDiagram.setEnabled(false);
		deleteActionDiagram = menuFactory.createDelete();
		deleteActionDiagram.setEnabled(false);
		searchActionDiagram = menuFactory.createSearch();
		copyActionDiagram = menuFactory.createCopy();
		selectallActionDiagram = menuFactory.createSelectAll();

		copyActionCustomPanel = createPanelAction(Pane.CUSTOMCODE, ActionName.COPY);
		cutActionCustomPanel = createPanelAction(Pane.CUSTOMCODE, ActionName.CUT);
		pasteActionCustomPanel = createPanelAction(Pane.CUSTOMCODE, ActionName.PASTE);
		selectAllActionCustomPanel = createPanelAction(Pane.CUSTOMCODE, ActionName.SELECTALL);

		copyActionPropPanel = createPanelAction(Pane.PROPERTY, ActionName.COPY);
		cutActionPropPanel = createPanelAction(Pane.PROPERTY, ActionName.CUT);
		pasteActionPropPanel = createPanelAction(Pane.PROPERTY, ActionName.PASTE);
		selectAllActionPropPanel = createPanelAction(Pane.PROPERTY, ActionName.SELECTALL);

		setGlobalActionHandlers(Pane.DIAGRAM);

		MainPlugin.getGUI().setContributor(this);
	}

	@Override
	public void contributeToMenu(IMenuManager manager) {
		if (Program.RUNTIME_TYPE == RuntimeType.ECLIPSE_PLUGIN) (MainPlugin.getGUI()).setContributor(this);

		IMenuManager menu = new MenuManager(Program.PROGRAM_NAME.toString());
		IMenuManager custom = new MenuManager(MenuFactory.CUSTOM_ELEMENTS);
		IMenuManager help = new MenuManager(MenuFactory.HELP);
		manager.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);

		custom.add(customnew = menuFactory.createNewCustomElement());
		custom.add(menuFactory.createNewCustomElementFromTemplate(this));
		custom.add(new Separator());
		custom.add(menuFactory.createCustomElementsTutorial());

		help.add(menuFactory.createOnlineHelp());
		if (Program.PROGRAM_NAME == ProgramName.UMLET) help.add(menuFactory.createOnlineSampleDiagrams());
		help.add(new Separator());
		help.add(menuFactory.createProgramHomepage());
		help.add(menuFactory.createRateProgram());
		help.add(new Separator());
		help.add(menuFactory.createAboutProgram());

		zoomMenu = menuFactory.createZoom();
		menu.add(zoomMenu);

		exportAsActionList = menuFactory.createExportAsActions();
		IMenuManager export = new MenuManager("Export as");
		for (IAction action: exportAsActionList) {
			export.add(action);
		}
		menu.add(export);

		menu.add(menuFactory.createEditCurrentPalette());
		if (Program.PROGRAM_NAME == ProgramName.UMLET) menu.add(custom);
		menu.add(menuFactory.createMailTo());
		menu.add(new Separator());
		menu.add(help);
		menu.add(menuFactory.createOptions());
	}

	public void setExportAsEnabled(boolean enabled) {
		//AB: We cannot disable the MenuManager, so we have to disable every entry in the export menu :P
		for (IAction action: exportAsActionList) {
			action.setEnabled(enabled);
		}
	}

	public void setPaste(boolean value) {
		this.pasteActionDiagram.setEnabled(value);
	}

	public void setCustomElementSelected(boolean selected) {
		this.custom_element_selected = selected;
		this.customedit.setEnabled(selected && !this.customPanelEnabled);
	}

	public void setElementsSelected(int count) {
		if (count > 0) {
			this.cutActionDiagram.setEnabled(true);
			this.deleteActionDiagram.setEnabled(true);
		}
		else {
			this.deleteActionDiagram.setEnabled(false);
			this.cutActionDiagram.setEnabled(false);
		}
	}

	public boolean isCustomPanelEnabled() {
		return customPanelEnabled;
	}

	public void setCustomPanelEnabled(boolean enable) {
		this.customPanelEnabled = enable;
		this.customedit.setEnabled(!enable && this.custom_element_selected);
		this.customnew.setEnabled(!enable);
		this.searchActionDiagram.setEnabled(!enable);
	}

	public void setGlobalActionHandlers(Pane focusedPane) {

		// Global actions which are always the same
		getActionBars().setGlobalActionHandler(ActionFactory.UNDO.getId(), undoActionGlobal);
		getActionBars().setGlobalActionHandler(ActionFactory.REDO.getId(), redoActionGlobal);
		getActionBars().setGlobalActionHandler(ActionFactory.PRINT.getId(), printActionGlobal);

		// Specific actions depending on the active pane}
		if (focusedPane == Pane.DIAGRAM) {
			getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyActionDiagram);
			getActionBars().setGlobalActionHandler(ActionFactory.CUT.getId(), cutActionDiagram);
			getActionBars().setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteActionDiagram);
			getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteActionDiagram);
			getActionBars().setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectallActionDiagram);
			getActionBars().setGlobalActionHandler(ActionFactory.FIND.getId(), searchActionDiagram);
		}
		else if (focusedPane == Pane.CUSTOMCODE) {
			getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyActionCustomPanel);
			getActionBars().setGlobalActionHandler(ActionFactory.CUT.getId(), cutActionCustomPanel);
			getActionBars().setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteActionCustomPanel);
			getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), null);
			getActionBars().setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectAllActionCustomPanel);
			getActionBars().setGlobalActionHandler(ActionFactory.FIND.getId(), null);
		}
		else if (focusedPane == Pane.PROPERTY) {
			getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyActionPropPanel);
			getActionBars().setGlobalActionHandler(ActionFactory.CUT.getId(), cutActionPropPanel);
			getActionBars().setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteActionPropPanel);
			getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), null);
			getActionBars().setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectAllActionPropPanel);
			getActionBars().setGlobalActionHandler(ActionFactory.FIND.getId(), null);
		}

		org.eclipse.swt.widgets.Display.getDefault().asyncExec(new UpdateActionBars(this.getActionBars()));
	}

	public void updateZoomMenuRadioButton(int newGridSize) {
		for (IContributionItem item : zoomMenu.getItems()) {
			IAction action = ((ActionContributionItem) item).getAction();
			int actionGridSize = Integer.parseInt(action.getText().substring(0, action.getText().length() - 2));
			if (actionGridSize == newGridSize) action.setChecked(true);
			else action.setChecked(false);
		}
	}
}
