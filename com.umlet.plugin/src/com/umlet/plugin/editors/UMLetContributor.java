package com.umlet.plugin.editors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorActionBarContributor;

import com.umlet.constants.Constants;
import com.umlet.constants.Constants.UmletType;
import com.umlet.control.BrowserLauncher;
import com.umlet.control.TemplateSorter;
import com.umlet.control.Umlet;
import com.umlet.control.command.Copy;
import com.umlet.control.command.Cut;
import com.umlet.control.command.Paste;
import com.umlet.control.command.RemoveElement;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.custom.CustomElement;
import com.umlet.element.base.Entity;
import com.umlet.gui.base.AboutPanel;
import com.umlet.gui.base.OptionPanel;
import com.umlet.gui.eclipse.EclipseGUI;
import com.umlet.gui.eclipse.Print;
import com.umlet.gui.eclipse.UpdateActionBars;
import com.umlet.gui.eclipse.EclipseGUI.Pane;
import com.umlet.plugin.UmletPlugin;

public class UMLetContributor extends EditorActionBarContributor {

	public enum ActionName {
		COPY, CUT, PASTE, SELECTALL
	}

	private Action help;
	private Action samples;
	private Action website;
	private Action rateUmlet;
	private Action about;
	private ArrayList<Action> zoomactions;
	private ArrayList<Action> exportactions;
	private Action customnew;
	private List<String> newfromtemplate;
	private Hashtable<String, Action> templateactions;
	private Action customedit;
	private Action customtutorial;
	private Action options;
	private Action sendPerMail;

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

	private boolean custom_panel_shown;
	private boolean custom_element_selected;

	public UMLetContributor() {
		this.newfromtemplate = new ArrayList<String>();
		this.zoomactions = new ArrayList<Action>();
		this.exportactions = new ArrayList<Action>();
		this.templateactions = new Hashtable<String, Action>();
		createMenuActions();
		this.custom_panel_shown = false;
		this.custom_element_selected = false;
	}

	@Override
	public void init(IActionBars actionBars) {
		super.init(actionBars);
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();

		// actions of the eclipse menu
		undoActionGlobal = new Action() {
			@Override
			public void run() {
				DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
				if (handler != null) {
					handler.getDrawPanel().getSelector().deselectAll();
					handler.getController().undo();
				}
			}
		};
		undoActionGlobal.setImageDescriptor(
				sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));

		redoActionGlobal = new Action() {
			@Override
			public void run() {
				DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
				if (handler != null) handler.getController().redo();
			}
		};
		redoActionGlobal.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));

		printActionGlobal = new Action() {
			@Override
			public void run() {
				DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
				if (handler != null) javax.swing.SwingUtilities.invokeLater(new Print(handler));
			}
		};

		copyActionDiagram = new Action() {
			@Override
			public void run() {
				DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
				if (handler != null) (new Copy()).execute(handler);
			}
		};
		copyActionDiagram.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));

		cutActionDiagram = new Action() {
			@Override
			public void run() {
				DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
				if (handler != null) handler.getController().executeCommand(new Cut());
			}
		};
		cutActionDiagram.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		cutActionDiagram.setEnabled(false);

		pasteActionDiagram = new Action() {
			@Override
			public void run() {
				DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
				if (handler != null) handler.getController().executeCommand(new Paste());
			}
		};
		pasteActionDiagram.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		pasteActionDiagram.setEnabled(false);

		deleteActionDiagram = new Action() {
			@Override
			public void run() {
				DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
				if (handler != null) {
					Vector<Entity> v = handler.getDrawPanel().getSelector().getSelectedEntities();
					if (v.size() > 0) {
						handler.getController().executeCommand(new RemoveElement(v));
					}
				}
			}
		};
		deleteActionDiagram.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		deleteActionDiagram.setEnabled(false);

		selectallActionDiagram = new Action() {
			@Override
			public void run() {
				DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
				if (handler != null) handler.getDrawPanel().getSelector().selectAll();
			}
		};

		searchActionDiagram = new Action() {
			@Override
			public void run() {
				Umlet.getInstance().getGUI().enableSearch(true);
			}
		};

		copyActionPropPanel = new Action() {
			@Override
			public void run() {
				((EclipseGUI) Umlet.getInstance().getGUI()).panelDoAction(Pane.PROPERTY, ActionName.COPY);
			}
		};

		cutActionPropPanel = new Action() {
			@Override
			public void run() {
				((EclipseGUI) Umlet.getInstance().getGUI()).panelDoAction(Pane.PROPERTY, ActionName.CUT);
			}
		};

		pasteActionPropPanel = new Action() {
			@Override
			public void run() {
				((EclipseGUI) Umlet.getInstance().getGUI()).panelDoAction(Pane.PROPERTY, ActionName.PASTE);
			}
		};

		selectAllActionPropPanel = new Action() {
			@Override
			public void run() {
				((EclipseGUI) Umlet.getInstance().getGUI()).panelDoAction(Pane.PROPERTY, ActionName.SELECTALL);
			}
		};

		copyActionCustomPanel = new Action() {
			@Override
			public void run() {
				((EclipseGUI) Umlet.getInstance().getGUI()).panelDoAction(Pane.CUSTOMCODE, ActionName.COPY);
			}
		};

		cutActionCustomPanel = new Action() {
			@Override
			public void run() {
				((EclipseGUI) Umlet.getInstance().getGUI()).panelDoAction(Pane.CUSTOMCODE, ActionName.CUT);
			}
		};

		pasteActionCustomPanel = new Action() {
			@Override
			public void run() {
				((EclipseGUI) Umlet.getInstance().getGUI()).panelDoAction(Pane.CUSTOMCODE, ActionName.PASTE);
			}
		};

		selectAllActionCustomPanel = new Action() {
			@Override
			public void run() {
				((EclipseGUI) Umlet.getInstance().getGUI()).panelDoAction(Pane.CUSTOMCODE, ActionName.SELECTALL);
			}
		};

		setGlobalActionHandlers(Pane.DIAGRAM);

		if (Constants.UMLETTYPE == UmletType.ECLIPSE_PLUGIN) (UmletPlugin.getGUI()).setContributor(this);
	}

	private class ZoomAction extends Action {
		private String zoomFactor;

		private ZoomAction(String name, String zoomFactor) {
			super(name);
			this.zoomFactor = zoomFactor;
		}

		@Override
		public void run() {
			if (Umlet.getInstance().getDiagramHandler() != null) {
				Umlet.getInstance().getDiagramHandler().setGridAndZoom(Integer.parseInt(zoomFactor));
			}
		}
	}

	private class ExportAction extends Action {
		private String type;

		private ExportAction(String name, String type) {
			super(name);
			this.type = type.toUpperCase();
		}

		@Override
		public void run() {
			if (UMLetEditor.getCurrent() != null) UMLetEditor.getCurrent().exportToFormat(type);
		}
	}

	private void createMenuActions() {
		help = new Action("Online Help...") {
			@Override
			public void run() {
				BrowserLauncher.openURL("http://www.umlet.com/faq.htm");
			}
		};
		samples = new Action("Online Sample Diagrams...") {
			@Override
			public void run() {
				BrowserLauncher.openURL("http://www.itmeyer.at/umlet/uml2/");
			}
		};
		website = new Action("UMLet Homepage...") {
			@Override
			public void run() {
				BrowserLauncher.openURL("http://www.umlet.com/");
			}
		};
		rateUmlet = new Action("Rate UMLet at EclipsePluginCentral...") {
			@Override
			public void run() {
				BrowserLauncher.openURL("http://www.eclipseplugincentral.com/modules.php?op=modload&name=Web_Links&file=index&req=ratelink&lid=465&ttitle=UMLet_-_UML_Tool_for_Fast_UML_Diagrams");
			}
		};
		about = new Action("About UMLet") {
			@Override
			public void run() {
				AboutPanel.getInstance().setVisible();
			}
		};
		this.exportactions.add(new ExportAction("BMP...", "bmp"));
		this.exportactions.add(new ExportAction("EPS...", "eps"));
		this.exportactions.add(new ExportAction("GIF...", "gif"));
		this.exportactions.add(new ExportAction("JPG...", "jpg"));
		this.exportactions.add(new ExportAction("PDF...", "pdf"));
		this.exportactions.add(new ExportAction("PNG...", "png"));
		this.exportactions.add(new ExportAction("SVG...", "svg"));

		this.zoomactions.add(new ZoomAction("10%", "1"));
		this.zoomactions.add(new ZoomAction("20%", "2"));
		this.zoomactions.add(new ZoomAction("30%", "3"));
		this.zoomactions.add(new ZoomAction("40%", "4"));
		this.zoomactions.add(new ZoomAction("50%", "5"));
		this.zoomactions.add(new ZoomAction("60%", "6"));
		this.zoomactions.add(new ZoomAction("70%", "7"));
		this.zoomactions.add(new ZoomAction("80%", "8"));
		this.zoomactions.add(new ZoomAction("90%", "9"));
		this.zoomactions.add(new ZoomAction("100%", "10"));
		this.zoomactions.add(new ZoomAction("110%", "11"));
		this.zoomactions.add(new ZoomAction("120%", "12"));
		this.zoomactions.add(new ZoomAction("130%", "13"));
		this.zoomactions.add(new ZoomAction("140%", "14"));
		this.zoomactions.add(new ZoomAction("150%", "15"));
		this.zoomactions.add(new ZoomAction("160%", "16"));
		this.zoomactions.add(new ZoomAction("170%", "17"));
		this.zoomactions.add(new ZoomAction("180%", "18"));
		this.zoomactions.add(new ZoomAction("190%", "19"));
		this.zoomactions.add(new ZoomAction("200%", "20"));

		// UNCOMMENTED BECAUSE WE DONT NEED ALL IMAGE FORMATS AND WE WANT TO SORT THEM ALPHABETICALLY
		/*
		 * for(String format : ImageIO.getWriterFileSuffixes()) {
		 * //We don't want to add "jpeg" which is redundant with "jpg" and "wbmp" because it doesn't work
		 * if (format.toUpperCase().equals("JPEG") || format.toUpperCase().equals("WBMP")) continue;
		 * this.exportactions.add(new ExportAction("Export to " + format.toUpperCase(), format));
		 * }
		 */

		this.customnew = new Action("New...") {
			@Override
			public void run() {
				if (Umlet.getInstance().getGUI().getCurrentCustomHandler() != null) {
					if (Umlet.getInstance().getGUI().getCurrentCustomHandler().closeEntity()) {
						Umlet.getInstance().getGUI().setCustomPanelEnabled(true);
						Umlet.getInstance().getGUI().getCurrentCustomHandler().getPanel().setCustomElementIsNew(true);
						Umlet.getInstance().getGUI().getCurrentCustomHandler().newEntity();
					}
				}
			}
		};

		this.newfromtemplate = Umlet.getInstance().getTemplateNames();
		for (String template : this.newfromtemplate) {
			Action tmp = new Action(template) {
				@Override
				public void run() {
					if (Umlet.getInstance().getGUI().getCurrentCustomHandler() != null) {
						if (Umlet.getInstance().getGUI().getCurrentCustomHandler().closeEntity()) {
							Umlet.getInstance().getGUI().setCustomPanelEnabled(true);
							Umlet.getInstance().getGUI().getCurrentCustomHandler().getPanel().setCustomElementIsNew(true);
							Umlet.getInstance().getGUI().getCurrentCustomHandler().newEntity(this.getText());
						}
					}
				}
			};
			this.templateactions.put(template, tmp);
		}

		this.customedit = new Action("Edit Selected...") {
			@Override
			public void run() { // copied from menulistener
				Entity entity = Umlet.getInstance().getEditedEntity();
				if ((entity instanceof CustomElement) && (entity != null) && (Umlet.getInstance().getGUI().getCurrentCustomHandler() != null)) {
					if (Umlet.getInstance().getGUI().getCurrentCustomHandler().closeEntity()) {
						Umlet.getInstance().getGUI().setCustomPanelEnabled(true);
						Umlet.getInstance().getGUI().getCurrentCustomHandler().getPanel().setCustomElementIsNew(false);
						Umlet.getInstance().getGUI().getCurrentCustomHandler().editEntity((CustomElement) entity);
					}
				}
			}
		};
		this.customedit.setEnabled(false);

		customtutorial = new Action("Custom Elements Tutorial...") {
			@Override
			public void run() {
				BrowserLauncher.openURL("http://www.umlet.com/ce/ce.htm");
			}
		};

		this.options = new Action("Options...") {
			@Override
			public void run() {
				OptionPanel.getInstance().showOptionPanel();
			}
		};

		this.sendPerMail = new Action("Mail to...") {
			@Override
			public void run() {
				Umlet.getInstance().getGUI().setMailPanelEnabled(!Umlet.getInstance().getGUI().isMailPanelVisible());
			}
		};
	}

	@Override
	public void contributeToMenu(IMenuManager manager) {
		if (Constants.UMLETTYPE == UmletType.ECLIPSE_PLUGIN) (UmletPlugin.getGUI()).setContributor(this);

		IMenuManager menu = new MenuManager("UMLet");
		IMenuManager zoom = new MenuManager("Zoom to");
		IMenuManager export = new MenuManager("Export as");
		IMenuManager custom = new MenuManager("Custom Elements");
		IMenuManager templates = new MenuManager("New from Template");
		IMenuManager help = new MenuManager("Help");
		manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);

		for (Action a : this.zoomactions)
			zoom.add(a);

		for (Action a : this.exportactions)
			export.add(a);

		Collections.sort(this.newfromtemplate, new TemplateSorter());
		for (String name : this.newfromtemplate)
			templates.add(this.templateactions.get(name));

		custom.add(this.customnew);
		custom.add(templates);
		custom.add(this.customedit);
		custom.add(new Separator());
		custom.add(this.customtutorial);

		help.add(this.help);
		help.add(this.samples);
		help.add(new Separator());
		help.add(this.website);
		help.add(this.rateUmlet);
		help.add(new Separator());
		help.add(this.about);

		menu.add(zoom);
		menu.add(export);
		menu.add(sendPerMail);
		menu.add(custom);
		menu.add(help);
		menu.add(options);
	}

	public void enablePaste() {
		this.pasteActionDiagram.setEnabled(true);
	}

	public void setCustomElementSelected(boolean selected) {
		this.custom_element_selected = selected;
		this.customedit.setEnabled(selected && !this.custom_panel_shown);
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

	public void setCustomPanelEnabled(boolean enable) {
		this.custom_panel_shown = enable;
		this.customedit.setEnabled(!enable && this.custom_element_selected);
		this.customnew.setEnabled(!enable);
		for (Action a : this.templateactions.values())
			a.setEnabled(!enable);
		this.searchActionDiagram.setEnabled(!enable);
	}

	public void setGlobalActionHandlers(Pane focusedPane) {

		// Global actions which are always the same
		getActionBars().setGlobalActionHandler(ActionFactory.UNDO.getId(), undoActionGlobal);
		getActionBars().setGlobalActionHandler(ActionFactory.REDO.getId(), redoActionGlobal);
		getActionBars().setGlobalActionHandler(ActionFactory.PRINT.getId(), printActionGlobal);

		// Specific actions depending on the active panel
		if (focusedPane == Pane.PROPERTY) {
			getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyActionPropPanel);
			getActionBars().setGlobalActionHandler(ActionFactory.CUT.getId(), cutActionPropPanel);
			getActionBars().setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteActionPropPanel);
			getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), null);
			getActionBars().setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectAllActionPropPanel);
			getActionBars().setGlobalActionHandler(ActionFactory.FIND.getId(), null);
		}
		else if (focusedPane == Pane.CUSTOMCODE) {
			getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyActionCustomPanel);
			getActionBars().setGlobalActionHandler(ActionFactory.CUT.getId(), cutActionCustomPanel);
			getActionBars().setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteActionCustomPanel);
			getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), null);
			getActionBars().setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectAllActionCustomPanel);
			getActionBars().setGlobalActionHandler(ActionFactory.FIND.getId(), null);
		}
		else {
			getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyActionDiagram);
			getActionBars().setGlobalActionHandler(ActionFactory.CUT.getId(), cutActionDiagram);
			getActionBars().setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteActionDiagram);
			getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteActionDiagram);
			getActionBars().setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectallActionDiagram);
			getActionBars().setGlobalActionHandler(ActionFactory.FIND.getId(), searchActionDiagram);
		}

		org.eclipse.swt.widgets.Display.getDefault().asyncExec(new UpdateActionBars(this.getActionBars()));
	}
}
