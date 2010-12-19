package com.umlet.gui.eclipse;

import java.awt.Cursor;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.umlet.control.Umlet;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.control.io.DiagramFileHandler;
import com.umlet.custom.CustomElementHandler;
import com.umlet.gui.UmletGUI;
import com.umlet.plugin.editors.UMLetContributor;
import com.umlet.plugin.editors.UMLetEditor;

@SuppressWarnings("serial")
public class EclipseGUI extends UmletGUI {

	private static String configfile = "umletplugin.cfg";
	
	private UMLetEditor editor;
	private Hashtable<DiagramHandler, UMLetEditor> diagrams;
	private UMLetContributor contributor;
	
	public EclipseGUI(Umlet umlet) {
		super(umlet);
		this.diagrams = new Hashtable<DiagramHandler, UMLetEditor>();
	}

	@Override
	public void close(DiagramHandler diagram) {
		//eclipse does the closing
	}

	@Override
	public void closeWindow() {
		this.umlet.close();
	}

	@Override
	public void diagramSelected(DiagramHandler handler) {
		//the menues are only visible if a diagram is selected. (contributor manages this)
	}

	@Override
	public void enablePaste() {
		if(this.contributor != null)
			this.contributor.enablePaste();
	}

	@Override
	public void enableSearch(boolean enable) {
		if(this.editor != null) {
			this.editor.enableSearch(enable);
		}
	}
	
	@Override
	public CustomElementHandler getCurrentCustomHandler() {
		if(this.editor == null)
			return null;
		return this.editor.getCustomElementHandler();
	}

	@Override
	public DrawPanel getCurrentDiagram() {
		if(this.editor == null)
			return null;
		return this.editor.getDiagram();
	}

	@Override
	public int getMainSplitPosition() {
		return -1; //dont write splitposition to config
	}

	@Override
	public int getRightSplitPosition() {
		return -1; //dont write splitposition to config
	}

	@Override
	public String getSelectedPalette() {
		if(this.editor != null)
			return this.editor.getSelectedPalette();
		return null;
	}

	@Override
	public JFrame getTopContainer() {
		return null;
	}

	@Override
	protected void init() {

	}

	@Override
	public void open(DiagramHandler diagram) {
		//not called by eclipse plugin (handles open by createEditor function)
	}

	@Override
	public void selectPalette(String palette) {
		//not needed because the palettes are handled through a tabbed pane.
	}

	@Override
	public void setCustomElementChanged(CustomElementHandler handler,
			boolean changed) {

	}

	@Override
	public void setCustomElementSelected(boolean selected) {
		if(this.editor != null && this.contributor != null)
			this.contributor.setCustomElementSelected(selected);
	}

	@Override
	public void setCustomPanelEnabled(boolean enable) {
		if(this.editor != null) { 
			this.editor.setCustomPanelEnabled(enable);
			if(this.contributor != null)
				this.contributor.setCustomPanelEnabled(enable);
		}
	}

	
	
	@Override
	public void updateDiagramName(DiagramHandler diagram, String name) 
	{
		UMLetEditor editor = this.diagrams.get(diagram);
		if(editor != null) 
			editor.dirtyChanged();
	}

	@Override
	public void setDiagramChanged(DiagramHandler diagram, boolean changed) {
		UMLetEditor editor = this.diagrams.get(diagram);
		if(editor != null && changed) 
			editor.dirtyChanged();
	}

	@Override
	public void setUngroupEnabled(boolean enabled) {

	}
	
	@Override
	public void setCursor(Cursor cursor) {
		if(this.editor != null)
			this.editor.setCursor(cursor);
	}

	public void setCurrentEditor(UMLetEditor editor) {
		if(!this.diagrams.containsKey(editor.getDiagram()))
			this.diagrams.put(editor.getDiagram().getHandler(), editor);
		this.editor = editor;
	}
	
	public void editorRemoved(UMLetEditor editor)  {
		this.diagrams.remove(editor);
		if(editor.equals(this.editor)) {
			this.editor = null;
		}
	}

	@Override
	public String getPropertyPanelText() {
		if(this.editor != null)
			return this.editor.getPropertyPanelText();
		return "";
	}

	@Override
	public void setPropertyPanelText(String text) {
		if(this.editor != null)
			this.editor.setPropertyPanelText(text);
	}

	@Override
	public void requestFocus() {
		if(this.editor != null)
			this.editor.requestFocus();
	}

	@Override
	public String chooseFileName() {
		String fileName = null;
		if(this.editor != null)
		{
			int returnVal = DiagramFileHandler.getOpenFileChooser().showOpenDialog(this.editor.getPanel());
	 	    if(returnVal == JFileChooser.APPROVE_OPTION) {
		      fileName= DiagramFileHandler.getOpenFileChooser().getSelectedFile().getAbsolutePath();
		    }
		}
	    return fileName;
	}

	@Override
	public void openDialog(String title, JComponent component) {	
		if(this.editor != null)
		{
			javax.swing.SwingUtilities.invokeLater(new ShowDialog(title,component));
		}
	}

	@Override
	public String getConfigFile() {
		return configfile;
	}

	@Override
	public void repaint() {
		if(this.editor != null)
			this.editor.repaint();
	}
	
	public void setContributor(UMLetContributor contributor) {
		this.contributor = contributor;
	}

	@Override
	public void elementsSelected(int count) {
		super.elementsSelected(count);
		if(this.contributor != null)
			this.contributor.setElementsSelected(count);
	}

	@Override
	public void setTextPanelFocused(boolean focused) {
		if(this.contributor != null)
			this.contributor.textpanelfocused(focused);
	}
	
	
}
