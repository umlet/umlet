package com.baselet.gui.eclipse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.gui.MenuFactory;
import com.baselet.plugin.editor.Contributor;

public class MenuFactoryEclipse extends MenuFactory {

	private final static Logger log = Logger.getLogger(MenuFactoryEclipse.class);	

	private static MenuFactoryEclipse instance = null;
	public static MenuFactoryEclipse getInstance() {
		if (instance == null) instance = new MenuFactoryEclipse();
		return instance;
	}

	@Override
	protected void doAction(final String menuItem, final Object param) {
		log.info("doAction " + menuItem);	
		DiagramHandler actualHandler = Main.getInstance().getDiagramHandler();
		// Edit Palette cannot be put in a separate invokeLater thread, or otherwise getActivePage() will be null!
		if (menuItem.equals(EDIT_CURRENT_PALETTE)) {
			String paletteName = Main.getInstance().getPalette().getFileHandler().getFullPathName();
			IFileStore fileStore = EFS.getLocalFileSystem().getStore(new File(paletteName).toURI());
			if (!fileStore.fetchInfo().isDirectory() && fileStore.fetchInfo().exists()) {
				IWorkbenchPage page= PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditorOnFileStore(page, fileStore);
				} catch (PartInitException e) {
					log.error("Cannot open palette file", e);
				}
			}
		}
		else if (menuItem.equals(SEARCH)) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {			
					Main.getInstance().getGUI().enableSearch(true);
				}
			});				
		}
		else if (menuItem.equals(ZOOM) && (actualHandler != null)) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {			
					Main.getInstance().getDiagramHandler().setGridAndZoom((Integer) param);
				}
			});							
		}
		// If the action is not overwritten, it is part of the default actions
		else 
		{
			log.debug("super.doAction");
			super.doAction(menuItem, param);
			log.debug("super.doAction complete");
		}
		log.debug("doAction complete");			
	}

	public Action createOptions() {
		return createAction(OPTIONS, null);
	}

	public Action createOnlineHelp() {
		return createAction(ONLINE_HELP, null);
	}

	public Action createOnlineSampleDiagrams() {
		return createAction(ONLINE_SAMPLE_DIAGRAMS, null);
	}

	public Action createProgramHomepage() {
		return createAction(PROGRAM_HOMEPAGE, null);
	}

	public Action createMailTo() {
		return createAction(MAIL_TO, null);
	}

	public Action createRateProgram() {
		return createAction(RATE_PROGRAM, null);
	}

	public Action createAboutProgram() {
		return createAction(ABOUT_PROGRAM, null);
	}

	public Action createNewCustomElement() {
		return createAction(NEW_CE, null);
	}

	public Action createCustomElementsTutorial() {
		return createAction(CUSTOM_ELEMENTS_TUTORIAL, null);
	}

	public Action createUndo() {
		return createAction(UNDO, null);
	}

	public Action createRedo() {
		return createAction(REDO, null);
	}

	public Action createPrint() {
		return createAction(PRINT, null);
	}

	public Action createCopy() {
		return createAction(COPY, null);
	}

	public Action createCut() {
		return createAction(CUT, null);
	}

	public Action createPaste() {
		return createAction(PASTE, null);
	}

	public Action createDelete() {
		return createAction(DELETE, null);
	}

	public Action createSelectAll() {
		return createAction(SELECT_ALL, null);
	}

	public Action createSearch() {
		return createAction(SEARCH, null);
	}

	public Action createEditSelected() {
		return createAction(EDIT_SELECTED, null);
	}

	public Action createEditCurrentPalette() {
		return createAction(EDIT_CURRENT_PALETTE, null);
	}

	public List<IAction> createExportAsActions() {
		List<IAction> actions = new ArrayList<IAction>();

		for (final String format : Constants.exportFormatList) {
			actions.add(createAction(format.toUpperCase() + "...", EXPORT_AS, format));
		}	

		return actions;
	}

	public IMenuManager createZoom() {
		final IMenuManager zoom = new MenuManager(ZOOM);
		for (String z : Constants.zoomValueList) {
			zoom.add(createAction(z, ZOOM, Integer.parseInt(z.substring(0, z.length() - 2)),  IAction.AS_RADIO_BUTTON));
		}
		return zoom;
	}

	private List<Action> aList = new ArrayList<Action>();
	public IMenuManager createNewCustomElementFromTemplate(final Contributor con) {
		IMenuManager menu = new MenuManager(NEW_FROM_TEMPLATE);
		for (String template : Main.getInstance().getTemplateNames()) {
			Action a = createAction(template, NEW_FROM_TEMPLATE, template);
			menu.add(a);
			aList.add(a);
		}
		menu.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				for (Action a : aList) {
					a.setEnabled(!con.isCustomPanelEnabled());
				}
			}
		});
		return menu;
	}

	private Action createAction(final String name, final String param) {
		return createAction(name, name, param);
	}

	private Action createAction(final String menuName, final String actionName, final Object param) {
		return createAction(menuName, actionName, param,  IAction.AS_UNSPECIFIED);
	}

	private Action createAction(final String menuName, final String actionName, final Object param, int style) {
		return new Action(menuName, style) {
			@Override
			public void run() {
				doAction(actionName, param);
			}
		};
	}

}
