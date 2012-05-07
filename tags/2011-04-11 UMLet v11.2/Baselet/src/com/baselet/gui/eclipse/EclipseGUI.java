package com.baselet.gui.eclipse;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Constants.Program;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.io.DiagramFileHandler;
import com.baselet.gui.BaseGUI;
import com.baselet.gui.OwnSyntaxPane;
import com.baselet.plugin.editor.Contributor;
import com.baselet.plugin.editor.Contributor.ActionName;
import com.baselet.plugin.editor.Editor;
import com.umlet.custom.CustomElementHandler;

@SuppressWarnings("serial")
public class EclipseGUI extends BaseGUI {

	public enum Pane {
		PROPERTY, CUSTOMCODE, DIAGRAM
	}

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	private Editor editor;
	private Hashtable<DiagramHandler, Editor> diagrams;
	private Contributor contributor;

	private int mainSplitPosition, rightSplitPosition, mailSplitPosition;

	public EclipseGUI(Main main) {
		super(main);
		this.diagrams = new Hashtable<DiagramHandler, Editor>();
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
	public void setPaste(boolean value) {
		if (this.contributor != null) this.contributor.setPaste(value);
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
		if (this.editor != null) { return this.editor.getSelectedPaletteName() + "." + Program.EXTENSION; }
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
		editor.selectPalette(palette);
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
		Editor editor = this.diagrams.get(diagram);
		if (editor != null) editor.dirtyChanged();
	}

	@Override
	public void setDiagramChanged(DiagramHandler diagram, boolean changed) {
		Editor editor = this.diagrams.get(diagram);
		if ((editor != null) && changed) editor.dirtyChanged();
	}

	@Override
	public void setUngroupEnabled(boolean enabled) {

	}

	@Override
	public void setCursor(Cursor cursor) {
		if (this.editor != null) this.editor.setCursor(cursor);
	}

	public void setCurrentEditor(Editor editor) {
		if (!this.diagrams.containsKey(editor.getDiagram())) this.diagrams.put(editor.getDiagram().getHandler(), editor);
		this.editor = editor;
	}

	public void editorRemoved(Editor editor) {
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
	public OwnSyntaxPane getPropertyPane()
	{
		return this.editor.getPropertyPane();
	}
	
	@Override
	public String getPropertyPanelText() {
		if (this.editor != null) return this.editor.getPropertyPane().getText();
		return "";
	}

	@Override
	public void setPropertyPanelText(String text) {
		if (this.editor != null) {
			OwnSyntaxPane propertyTextPane = editor.getPropertyPane();
			if (!propertyTextPane.getText().equals(text)) {
				propertyTextPane.setText(text);

				//AB: Can be removed because of jsyntaxpane implementation
				//propertyTextPane.checkPanelForSpecialChars();
				
				// Reset the vertical and horizontal scrollbar position to the upper left corner
				propertyTextPane.setCaretPosition(0);
			}
		}
	}

	public void panelDoAction(Pane pane, ActionName actionName) {

		JTextComponent textpane = null;
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

	public Contributor getContributor() {
		return this.contributor;
	}

	@Override
	public void setValueOfZoomDisplay(int i) {
		if (contributor != null) contributor.updateZoomMenuRadioButton(i);
	}

}
