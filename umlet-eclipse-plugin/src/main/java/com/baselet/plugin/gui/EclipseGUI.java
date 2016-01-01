package com.baselet.plugin.gui;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.text.JTextComponent;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.CanCloseProgram;
import com.baselet.control.config.Config;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.old.custom.CustomElementHandler;
import com.baselet.gui.BaseGUI;
import com.baselet.gui.CurrentGui;
import com.baselet.gui.pane.OwnSyntaxPane;
import com.baselet.plugin.gui.Contributor.ActionName;

public class EclipseGUI extends BaseGUI {

	public enum Pane {
		PROPERTY, CUSTOMCODE, DIAGRAM
	}

	private static final Logger log = LoggerFactory.getLogger(EclipseGUI.class);

	private Editor editor;
	private final HashMap<DiagramHandler, Editor> diagrams;
	private Contributor contributor;

	public EclipseGUI(CanCloseProgram main) {
		super(main);
		diagrams = new HashMap<DiagramHandler, Editor>();
	}

	@Override
	public void close(DiagramHandler diagram) {
		// eclipse does the closing
	}

	@Override
	public void closeWindow() {
		main.closeProgram();
	}

	@Override
	public void diagramSelected(DiagramHandler handler) {
		// the menues are only visible if a diagram is selected. (contributor manages this)
		// AB: just update the export menu
		DrawPanel currentDiagram = CurrentGui.getInstance().getGui().getCurrentDiagram();
		if (currentDiagram == null) {
			return; // Possible if method is called at loading a palette
		}
		boolean enable = handler != null && !currentDiagram.getGridElements().isEmpty();
		contributor.setExportAsEnabled(enable);
	}

	@Override
	public void enablePasteMenuEntry() {
		if (contributor != null) {
			contributor.setPaste(true);
		}
	}

	@Override
	public CustomElementHandler getCurrentCustomHandler() {
		if (editor == null) {
			return null;
		}
		return editor.getCustomElementHandler();
	}

	@Override
	public DrawPanel getCurrentDiagram() {
		if (editor == null) {
			return null;
		}
		return editor.getDiagram();
	}

	@Override
	public int getMainSplitPosition() {
		return Config.getInstance().getMain_split_position(); // in Eclipse the Editors overwrite this constant everytime they are closed (editor.getMainSplitLocation() wouldn't work because the editor is already null)
	}

	@Override
	public int getRightSplitPosition() {
		return Config.getInstance().getRight_split_position();
	}

	@Override
	public int getMailSplitPosition() {
		return Config.getInstance().getMail_split_position();
	}

	@Override
	public String getSelectedPalette() {
		if (editor != null) {
			return editor.getSelectedPaletteName();
		}
		return null;
	}

	@Override
	protected void init() {}

	@Override
	public void open(DiagramHandler diagram) {
		if (editor != null) {
			editor.open(diagram);
		}
	}

	@Override
	public void jumpTo(DiagramHandler diagram) {
		// not called by eclipse plugin (handles open by createEditor function)
	}

	@Override
	public void showPalette(String palette) {
		super.showPalette(palette);
		if (editor != null) {
			editor.showPalette(palette);
		}
	}

	@Override
	public void setCustomElementChanged(CustomElementHandler handler,
			boolean changed) {

	}

	@Override
	public void setCustomElementSelected(boolean selected) {
		if (editor != null && contributor != null) {
			contributor.setCustomElementSelected(selected);
		}
	}

	@Override
	public void setCustomPanelEnabled(boolean enable) {
		if (editor != null) {
			editor.setCustomPanelEnabled(enable);
			if (contributor != null) {
				contributor.setCustomPanelEnabled(enable);
			}
		}
	}

	@Override
	public void setMailPanelEnabled(boolean enable) {
		if (editor != null) {
			editor.setMailPanelEnabled(enable);
		}
	}

	@Override
	public boolean isMailPanelVisible() {
		return editor.isMailPanelVisible();
	}

	@Override
	public void updateDiagramName(DiagramHandler diagram, String name) {
		Editor editor = diagrams.get(diagram);
		if (editor != null) {
			editor.diagramNameChanged();
		}
	}

	@Override
	public void setDiagramChanged(DiagramHandler diagram, boolean changed) {
		Editor editor = diagrams.get(diagram);
		if (editor != null) {
			editor.dirtyChanged();
		}
	}

	@Override
	public void setCursor(Cursor cursor) {
		if (editor != null) {
			editor.setCursor(cursor);
		}
	}

	public void registerEditorForDiagramHandler(Editor editor, DiagramHandler handler) {
		diagrams.put(handler, editor);
	}

	public void setCurrentDiagramHandler(DiagramHandler handler) {
		CurrentDiagram.getInstance().setCurrentDiagramHandler(handler);
	}

	public void setCurrentEditor(Editor editor) {
		this.editor = editor;
	}

	public void editorRemoved(Editor editor) {
		// Before removing the editor, we have to store the actual splitpositions and lastUsedPalette to variables so that a new editor has the same values
		Config.getInstance().setMain_split_position(editor.getMainSplitLocation());
		Config.getInstance().setRight_split_position(editor.getRightSplitLocation());
		Config.getInstance().setLastUsedPalette(getSelectedPalette());
		diagrams.remove(editor.getDiagram().getHandler());
		if (editor.equals(this.editor)) {
			this.editor = null;
		}
	}

	@Override
	public OwnSyntaxPane getPropertyPane() {
		if (editor != null) {
			return editor.getPropertyPane();
		}
		else {
			return null;
		}
	}

	public void panelDoAction(Pane pane, ActionName actionName) {
		JTextComponent textpane = null;
		if (pane == Pane.PROPERTY) {
			textpane = editor.getPropertyPane().getTextComponent();
		}
		else if (pane == Pane.CUSTOMCODE) {
			textpane = editor.getCustomPane();
		}

		if (textpane != null) {
			if (actionName == ActionName.COPY) {
				textpane.copy();
			}
			else if (actionName == ActionName.CUT) {
				textpane.cut();
				int pos = textpane.getSelectionStart();
				textpane.setCaretPosition(pos);
			}
			else if (actionName == ActionName.PASTE) {
				try {
					// We retrieve the content from the system clipboard
					Transferable cont = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
					if (cont != null) {
						// If it's not null we save the length of the content and add the actual selection start
						int pos = ((String) cont.getTransferData(DataFlavor.stringFlavor)).length() + textpane.getSelectionStart();
						textpane.paste();
						// After pasting the clipboard content we want to set the actual cursor position to the end of the pasted content
						textpane.setCaretPosition(pos);
					}
				} catch (Exception e) {
					log.error(null, e);
				}
			}
			else if (actionName == ActionName.SELECTALL) {
				textpane.selectAll();
			}
		}
	}

	@Override
	public void requestFocus() {
		if (editor != null) {
			editor.requestFocus();
		}
	}

	public void setContributor(Contributor contributor) {
		this.contributor = contributor;
	}

	@Override
	public void elementsSelected(Collection<GridElement> selectedElements) {
		super.elementsSelected(selectedElements);
		if (contributor != null) {
			contributor.setElementsSelected(selectedElements);
		}
	}

	public void setPaneFocused(final Pane pane) {
		if (contributor != null) {
			// must be executed from within the SWT Display thread (see https://stackoverflow.com/questions/5980316/invalid-thread-access-error-with-java-swt)
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					contributor.setGlobalActionHandlers(pane);
				}
			});
		}
	}

	@Override
	public void setValueOfZoomDisplay(int i) {
		if (contributor != null) {
			contributor.updateZoomMenuRadioButton(i);
		}
	}

	@Override
	public void afterSaving() {
		super.afterSaving();
		EclipseGUI.refreshWorkspace();
	}

	@Override
	public void focusPropertyPane() {
		editor.focusPropertyPane();
	}

	@Override
	public Frame getMainFrame() {
		return editor.getMainFrame();
	}

	public static void refreshWorkspace() {
		IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		try {
			myWorkspaceRoot.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			log.error("Error at refreshing the workspace", e);
		}
	}

	@Override
	public boolean hasExtendedContextMenu() {
		return false;
	}

	@Override
	public boolean saveWindowSizeInConfig() {
		return false;
	}
}
