package com.baselet.plugin.gui;

import java.util.List;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

import com.baselet.control.constants.MenuConstants;
import com.baselet.control.enums.Program;
import com.baselet.gui.CurrentGui;

public class MenuContributor extends ExtensionContributionFactory {

	// private IAction customnew;

	private final MenuFactoryEclipse menuFactory = MenuFactoryEclipse.getInstance();
	private IMenuManager zoomMenu;
	private List<IAction> exportAsActionList;

	@Override
	public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot contributionRoot) {
		((EclipseGUI) CurrentGui.getInstance().getGui()).setMenuContributor(this);
		final IMenuManager menu = new MenuManager(Program.getInstance().getProgramName().toString());

		IMenuManager custom = new MenuManager(MenuConstants.CUSTOM_ELEMENTS);
		IMenuManager help = new MenuManager(MenuConstants.HELP);

		// custom.add(customnew = menuFactory.createNewCustomElement());
		// custom.add(menuFactory.createNewCustomElementFromTemplate(this));
		custom.add(new Separator());
		custom.add(menuFactory.createCustomElementsTutorial());

		help.add(menuFactory.createOnlineHelp());
		help.add(menuFactory.createOnlineSampleDiagrams());
		help.add(menuFactory.createVideoTutorial());
		help.add(new Separator());
		help.add(menuFactory.createProgramHomepage());
		help.add(menuFactory.createRateProgram());
		help.add(new Separator());
		help.add(menuFactory.createAboutProgram());

		menu.add(menuFactory.createGenerate());
		menu.add(menuFactory.createGenerateOptions());

		zoomMenu = menuFactory.createZoom();
		menu.add(zoomMenu);

		exportAsActionList = menuFactory.createExportAsActions();
		IMenuManager export = new MenuManager("Export as");
		for (IAction action : exportAsActionList) {
			export.add(action);
		}
		menu.add(export);

		menu.add(menuFactory.createEditCurrentPalette());
		menu.add(custom);
		menu.add(menuFactory.createMailTo());
		menu.add(new Separator());
		menu.add(help);
		menu.add(menuFactory.createOptions());

		// register top level menu
		menu.setVisible(false);
		// manager.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);
		contributionRoot.addContributionItem(menu, null);

		// register listener to switch visibility of menu
		serviceLocator.getService(IPartService.class).addPartListener(new IPartListener() {

			@Override
			public void partOpened(IWorkbenchPart arg0) {}

			@Override
			public void partDeactivated(IWorkbenchPart arg0) {}

			@Override
			public void partClosed(IWorkbenchPart arg0) {
				if (arg0 instanceof Editor) {
					menu.setVisible(false);
				}
			}

			@Override
			public void partBroughtToTop(IWorkbenchPart arg0) {

				if (arg0 instanceof IEditorPart) {
					menu.setVisible(arg0 instanceof Editor);
				}
			}

			@Override
			public void partActivated(IWorkbenchPart arg0) {
				if (arg0 instanceof IEditorPart) {
					menu.setVisible(arg0 instanceof Editor);
				}
			}
		});
	}

	public void setExportAsEnabled(boolean enabled) {
		// AB: We cannot disable the MenuManager, so we have to disable every entry in the export menu :P
		for (IAction action : exportAsActionList) {
			action.setEnabled(enabled);
		}
	}

	public void updateZoomMenuRadioButton(int newGridSize) {
		for (IContributionItem item : zoomMenu.getItems()) {
			IAction action = ((ActionContributionItem) item).getAction();
			int actionGridSize = Integer.parseInt(action.getText().substring(0, action.getText().length() - 2));
			if (actionGridSize == newGridSize) {
				action.setChecked(true);
			}
			else {
				action.setChecked(false);
			}
		}
	}
}
