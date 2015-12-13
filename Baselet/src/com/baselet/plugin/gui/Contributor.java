package com.baselet.plugin.gui;

import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorActionBarContributor;

import com.baselet.element.interfaces.GridElement;
import com.baselet.gui.CurrentGui;
import com.baselet.plugin.gui.EclipseGUI.Pane;

public class Contributor extends EditorActionBarContributor {

	public enum ActionName {
		COPY, CUT, PASTE, SELECTALL
	}

	private final MenuFactoryEclipse menuFactory = MenuFactoryEclipse.getInstance();

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

	private boolean customPanelEnabled;
	private boolean custom_element_selected;

	public Contributor() {
		customPanelEnabled = false;
		custom_element_selected = false;
	}

	private Action createPanelAction(final Pane pane, final ActionName action) {
		Action copyActionPropPanel = new Action() {
			@Override
			public void run() {
				((EclipseGUI) CurrentGui.getInstance().getGui()).panelDoAction(pane, action);
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
	}

	@Override
	public void contributeToMenu(IMenuManager manager) {
		((EclipseGUI) CurrentGui.getInstance().getGui()).setContributor(this);
	}

	public void setPaste(boolean value) {
		pasteActionDiagram.setEnabled(value);
	}

	public void setCustomElementSelected(boolean selected) {
		custom_element_selected = selected;
		customedit.setEnabled(selected && !customPanelEnabled);
	}

	public void setElementsSelected(Collection<GridElement> selectedElements) {
		if (selectedElements.isEmpty()) {
			deleteActionDiagram.setEnabled(false);
			cutActionDiagram.setEnabled(false);
		}
		else {
			cutActionDiagram.setEnabled(true);
			deleteActionDiagram.setEnabled(true);
		}
	}

	public boolean isCustomPanelEnabled() {
		return customPanelEnabled;
	}

	public void setCustomPanelEnabled(boolean enable) {
		customPanelEnabled = enable;
		customedit.setEnabled(!enable && custom_element_selected);
		customnew.setEnabled(!enable);
		searchActionDiagram.setEnabled(!enable);
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

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				getActionBars().updateActionBars();
			}
		});
	}

}
