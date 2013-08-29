package com.baselet.gui.eclipse;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.HashMap;

import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.gui.BaseGUI;
import com.baselet.gui.OwnSyntaxPane;
import com.baselet.plugin.MainPlugin;
import com.baselet.plugin.editor.Contributor;
import com.baselet.plugin.editor.Contributor.ActionName;
import com.baselet.plugin.editor.Editor;
import com.umlet.custom.CustomElementHandler;

public class EclipseGUI extends BaseGUI {

	public enum Pane {
		PROPERTY, CUSTOMCODE, DIAGRAM
	}
	
	private static final Logger log = Logger.getLogger(EclipseGUI.class);

	private Editor editor;
	private HashMap<DiagramHandler, Editor> diagrams;
	private Contributor contributor;

	public EclipseGUI(Main main) {
		super(main);
		this.diagrams = new HashMap<DiagramHandler, Editor>();
	}

	@Override
	public void close(DiagramHandler diagram) {
		// eclipse does the closing
	}

	@Override
	public void closeWindow() {
		this.main.closeProgram();
	}

	@Override
	public void diagramSelected(DiagramHandler handler) {
		// the menues are only visible if a diagram is selected. (contributor manages this)
		//AB: just update the export menu	
		DrawPanel currentDiagram = Main.getInstance().getGUI().getCurrentDiagram();
		if (currentDiagram == null) return; //Possible if method is called at loading a palette
		boolean enable = (handler != null) && !currentDiagram.getAllEntities().isEmpty();
		contributor.setExportAsEnabled(enable);
	}

	@Override
	public void enablePasteMenuEntry() {
		if (this.contributor != null) this.contributor.setPaste(true);
	}

	@Override
	public CustomElementHandler getCurrentCustomHandler() {
		if (this.editor == null) return null;
		return this.editor.getCustomElementHandler();
	}

	@Override
	public DrawPanel getCurrentDiagram() {
		if (this.editor == null) return null;
		return this.editor.getDiagram();
	}

	@Override
	public int getMainSplitPosition() {
		return Constants.main_split_position; // in Eclipse the Editors overwrite this constant everytime they are closed (editor.getMainSplitLocation() wouldn't work because the editor is already null)
	}

	@Override
	public int getRightSplitPosition() {
		return Constants.right_split_position;
	}

	@Override
	public int getMailSplitPosition() {
		return Constants.mail_split_position;
	}

	@Override
	public String getSelectedPalette() {
		if (this.editor != null) return this.editor.getSelectedPaletteName();
		return null;
	}

	@Override
	protected void init() {
	}

	@Override
	public void open(DiagramHandler diagram) {
		if (editor != null) editor.open(diagram);
	}

	@Override
	public void jumpTo(DiagramHandler diagram) {
		// not called by eclipse plugin (handles open by createEditor function)
	}

	@Override
	public void showPalette(String palette) {
		super.showPalette(palette);
		if (editor != null) editor.showPalette(palette);
	}

	@Override
	public void setCustomElementChanged(CustomElementHandler handler,
			boolean changed) {

	}

	@Override
	public void setCustomElementSelected(boolean selected) {
		if ((this.editor != null) && (this.contributor != null)) this.contributor.setCustomElementSelected(selected);
	}

	@Override
	public void setCustomPanelEnabled(boolean enable) {
		if (this.editor != null) {
			editor.setCustomPanelEnabled(enable);
			if (this.contributor != null) this.contributor.setCustomPanelEnabled(enable);
		}
	}

	@Override
	public void setMailPanelEnabled(boolean enable) {
		if (this.editor != null) {
			this.editor.setMailPanelEnabled(enable);
		}
	}

	@Override
	public boolean isMailPanelVisible() {
		return editor.isMailPanelVisible();
	}

	@Override
	public void updateDiagramName(DiagramHandler diagram, String name) {
		Editor editor = this.diagrams.get(diagram);
		if (editor != null) editor.diagramNameChanged();
	}

	@Override
	public void setDiagramChanged(DiagramHandler diagram, boolean changed) {
		Editor editor = this.diagrams.get(diagram);
		if (editor != null) editor.dirtyChanged();
	}

	@Override
	public void setUngroupEnabled(boolean enabled) {

	}

	@Override
	public void setCursor(Cursor cursor) {
		if (this.editor != null) this.editor.setCursor(cursor);
	}
	
	public void registerEditorForDiagramHandler(Editor editor, DiagramHandler handler) {
		this.diagrams.put(handler, editor);
	}

	public void setCurrentDiagramHandler(DiagramHandler handler) {
		Main.getInstance().setCurrentDiagramHandler(handler);	
	}

	public void setCurrentEditor(Editor editor) {
		this.editor = editor;
	}

	public void editorRemoved(Editor editor) {
		// Before removing the editor, we have to store the actual splitpositions and lastUsedPalette to variables so that a new editor has the same values
		Constants.main_split_position = editor.getMainSplitLocation();
		Constants.right_split_position = editor.getRightSplitLocation();
		Constants.lastUsedPalette = getSelectedPalette();
		this.diagrams.remove(editor.getDiagram().getHandler());
		if (editor.equals(this.editor)) {
			this.editor = null;
		}
	}

	@Override 
	public OwnSyntaxPane getPropertyPane() {
		if (editor != null) return editor.getPropertyPane();
		else return null;
	}

	public void panelDoAction(Pane pane, ActionName actionName) {
		JTextComponent textpane = null;
		if (pane == Pane.PROPERTY) textpane = editor.getPropertyPane().getTextComponent();
		else if (pane == Pane.CUSTOMCODE) textpane = editor.getCustomPane();

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
		if (this.editor != null) this.editor.requestFocus();
	}

	public void setContributor(Contributor contributor) {
		this.contributor = contributor;
	}

	@Override
	public void elementsSelected(int count) {
		super.elementsSelected(count);
		if (this.contributor != null) this.contributor.setElementsSelected(count);
	}

	public void setPaneFocused(Pane pane) {
		if (this.contributor != null) this.contributor.setGlobalActionHandlers(pane);
	}

	@Override
	public void setValueOfZoomDisplay(int i) {
		if (contributor != null) contributor.updateZoomMenuRadioButton(i);
	}

	@Override
	public void afterSaving() {
		super.afterSaving();
		MainPlugin.refreshWorkspace();
	}

	@Override
	public void focusPropertyPane() {
		editor.focusPropertyPane();
	}
}
