package com.umlet.gui.eclipse;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextPane;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.control.io.DiagramFileHandler;
import com.umlet.custom.CustomElementHandler;
import com.umlet.gui.base.UmletGUI;
import com.umlet.gui.base.UmletTextPane;
import com.umlet.plugin.editors.UMLetContributor;
import com.umlet.plugin.editors.UMLetEditor;
import com.umlet.plugin.editors.UMLetContributor.ActionName;

@SuppressWarnings("serial")
public class EclipseGUI extends UmletGUI {

	public enum Pane {
		PROPERTY, CUSTOMCODE, DIAGRAM
	}

	private UMLetEditor editor;
	private Hashtable<DiagramHandler, UMLetEditor> diagrams;
	private UMLetContributor contributor;

	private int mainSplitPosition, rightSplitPosition, mailSplitPosition;

	public EclipseGUI(Umlet umlet) {
		super(umlet);
		this.diagrams = new Hashtable<DiagramHandler, UMLetEditor>();
	}

	@Override
	public void close(DiagramHandler diagram) {
	// eclipse does the closing
	}

	@Override
	public void closeWindow() {
		this.umlet.close();
	}

	@Override
	public void diagramSelected(DiagramHandler handler) {
	// the menues are only visible if a diagram is selected. (contributor manages this)
	}

	@Override
	public void enablePaste() {
		if (this.contributor != null) this.contributor.enablePaste();
	}

	@Override
	public void enableSearch(boolean enable) {
		if (this.editor != null) {
			this.editor.enableSearch(enable);
		}
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
		return mainSplitPosition;
	}

	@Override
	public int getRightSplitPosition() {
		return rightSplitPosition;
	}

	@Override
	public int getMailSplitPosition() {
		return mailSplitPosition;
	}

	@Override
	public String getSelectedPalette() {
		if (this.editor != null) { return this.editor.getSelectedPaletteName(); }
		return null;
	}

	@Override
	public JFrame getTopContainer() {
		return null;
	}

	@Override
	protected void init() {
		// We load the constants as startingsplitpositions into the variables
		mainSplitPosition = Constants.main_split_position;
		rightSplitPosition = Constants.right_split_position;
		mailSplitPosition = Constants.mail_split_position;
	}

	@Override
	public void open(DiagramHandler diagram) {
	// not called by eclipse plugin (handles open by createEditor function)
	}

	@Override
	public void selectPalette(String palette) {
	// not needed because the palettes are handled through a tabbed pane.
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
			this.editor.setCustomPanelEnabled(enable);
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
		UMLetEditor editor = this.diagrams.get(diagram);
		if (editor != null) editor.dirtyChanged();
	}

	@Override
	public void setDiagramChanged(DiagramHandler diagram, boolean changed) {
		UMLetEditor editor = this.diagrams.get(diagram);
		if ((editor != null) && changed) editor.dirtyChanged();
	}

	@Override
	public void setUngroupEnabled(boolean enabled) {

	}

	@Override
	public void setCursor(Cursor cursor) {
		if (this.editor != null) this.editor.setCursor(cursor);
	}

	public void setCurrentEditor(UMLetEditor editor) {
		if (!this.diagrams.containsKey(editor.getDiagram())) this.diagrams.put(editor.getDiagram().getHandler(), editor);
		this.editor = editor;
	}

	public void editorRemoved(UMLetEditor editor) {
		// Before removing the editor, we have to store the actual splitpositions
		// to variables so that a new editor has the same splitpositions
		mainSplitPosition = editor.getMainSplitLocation();
		rightSplitPosition = editor.getRightSplitLocation();
		mailSplitPosition = editor.getMailSplitLocation();
		this.diagrams.remove(editor);
		if (editor.equals(this.editor)) {
			this.editor = null;
		}
	}

	@Override
	public String getPropertyPanelText() {
		if (this.editor != null) return this.editor.getPropertyPane().getText();
		return "";
	}

	@Override
	public void setPropertyPanelText(String text) {
		if (this.editor != null) {
			UmletTextPane propertyTextPane = editor.getPropertyPane();
			if (!propertyTextPane.getText().equals(text)) {
				propertyTextPane.setText(text);
				propertyTextPane.checkPanelForSpecialChars();
				// Reset the vertical and horizontal scrollbar position to the upper left corner
				propertyTextPane.setCaretPosition(0);
			}
		}
	}

	public void panelDoAction(Pane pane, ActionName actionName) {

		JTextPane textpane = null;
		if (pane == Pane.PROPERTY) textpane = editor.getPropertyPane();
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
					e.printStackTrace();
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

	@Override
	public String chooseFileName() {
		String fileName = null;
		if (this.editor != null) {
			int returnVal = DiagramFileHandler.getOpenFileChooser().showOpenDialog(this.editor.getPanel());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				fileName = DiagramFileHandler.getOpenFileChooser().getSelectedFile().getAbsolutePath();
			}
		}
		return fileName;
	}

	@Override
	public void openDialog(String title, JComponent component) {
		if (this.editor != null) {
			javax.swing.SwingUtilities.invokeLater(new ShowDialog(title, component));
		}
	}

	@Override
	public void repaint() {
		if (this.editor != null) this.editor.repaint();
	}

	public void setContributor(UMLetContributor contributor) {
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

}
