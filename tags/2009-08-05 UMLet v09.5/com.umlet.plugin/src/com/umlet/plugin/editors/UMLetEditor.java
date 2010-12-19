package com.umlet.plugin.editors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.control.diagram.PaletteHandler;
import com.umlet.control.io.GenEps;
import com.umlet.control.io.GenPdf;
import com.umlet.control.io.GenPic;
import com.umlet.control.io.GenSvg;
import com.umlet.custom.CustomElementHandler;
import com.umlet.gui.CustomElementPanel;
import com.umlet.gui.SearchListener;
import com.umlet.gui.TextpaneFocusListener;
import com.umlet.gui.UmletTextPane;
import com.umlet.gui.eclipse.RepaintGUI;
import com.umlet.plugin.UmletPlugin;

public class UMLetEditor extends EditorPart {

	private DiagramHandler diagram;
	private CustomElementHandler customhandler;
	private CustomElementPanel custompanel;
	private Hashtable<String, PaletteHandler> palettes;
	private Panel embedded_umlet_panel;
	private UmletTextPane propertytextpane;
	private JTextField searchfield;
	private JPanel searchPanel;
	private JTabbedPane palettetabs;
	
	
	
	private JSplitPane rightSplit;
	private JSplitPane mainSplit;
	private JSplitPane customSplit;
	private JSplitPane searchSplit;
	
	private static UMLetEditor currenteditor;
	
	public static UMLetEditor getCurrent() {
		return currenteditor;
	}
	
	public UMLetEditor() {
			this.customhandler = new CustomElementHandler();
		this.custompanel = this.customhandler.getPanel();
		this.propertytextpane = Umlet.getInstance().getGUI().createPropertyTextPane();
		this.palettes = new Hashtable<String, PaletteHandler>();
		  //scan palettes
		  List<File> palettes = Umlet.getInstance().scanForPalettes();
		  for(File palette : palettes) 
			  this.palettes.put(palette.getName(), new PaletteHandler(palette));
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		this.diagram.doSave();
		monitor.done();
	}
	
	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		this.setPartName(input.getName());
		if(input instanceof IFileEditorInput)
		{
			IFileEditorInput in = (IFileEditorInput)input;
			this.diagram = new DiagramHandler(in.getFile().getLocation().toFile());
		}
		else
			throw new PartInitException("Editor input not supported.");
	}

	@Override
	public boolean isDirty() {
		return this.diagram.isChanged();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite goodSWTComposite = new Composite(parent, SWT.EMBEDDED); //we need the embedded attribute set
        Frame frame= org.eclipse.swt.awt.SWT_AWT.new_Frame(goodSWTComposite);
        embedded_umlet_panel = new Panel();
        embedded_umlet_panel.setLayout(new BorderLayout());
        embedded_umlet_panel.add(this.createEditor());
        frame.add(embedded_umlet_panel);
        
	}

	@Override
	public void setFocus() {
		currenteditor = this;
		UmletPlugin.getGUI().setCurrentEditor(this);
		this.diagram.getDrawPanel().getSelector().updateSelectorInformation();
	}
	
	public DrawPanel getDiagram() {
		return this.diagram.getDrawPanel();
	}

	@Override
	public void dispose() {
		super.dispose();
		UmletPlugin.getGUI().editorRemoved(this);
	}
	
	public void setCursor(Cursor cursor) {
		this.embedded_umlet_panel.setCursor(cursor);
	}
	
	public String getPropertyPanelText() {
		return this.propertytextpane.getText();
	}

	public void setPropertyPanelText(String text) {
		if(!this.propertytextpane.getText().equals(text))
		{
			this.propertytextpane.setText(text);
			this.propertytextpane.checkPanelForSpecialChars();
		}	
	}
	
	public void requestFocus() {
		this.embedded_umlet_panel.requestFocus();
	}
	
	public void dirtyChanged() {
		org.eclipse.swt.widgets.Display.getDefault().asyncExec(new DirtyAction(this));
	}
	
	public void diagramNameChanged() {
		org.eclipse.swt.widgets.Display.getDefault().asyncExec(new UpdateDiagramNameAction(this));
	}
	
	protected void fireDiagramNameChanged() {
		this.firePropertyChange(PROP_TITLE);
	}
	
	protected void fireDirtyChanged() {
		this.firePropertyChange(UMLetEditor.PROP_DIRTY);
	}
	
	private JPanel createEditor()
	{	
		JPanel editor = new JPanel();
		editor.setLayout(new BorderLayout());
	    palettetabs = new JTabbedPane();
	    for(String palette : Umlet.getInstance().getPaletteNames(palettes)) {
	    	if(palettes.get(palette + ".uxf") != null)
	    		palettetabs.add(palette, palettes.get(palette + ".uxf").getDrawPanel().getScrollPanel());
	    }
	    
	    rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,palettetabs,propertytextpane.getPanel()); 
	    rightSplit.setDividerSize(2);
	    rightSplit.setDividerLocation(Constants.right_split_position);
	    rightSplit.setResizeWeight(1);
	    rightSplit.setBorder(null);
	    
	    this.searchPanel = new JPanel();
	    this.searchPanel.setLayout(new BoxLayout(searchPanel,BoxLayout.X_AXIS));
	    JLabel searchlabel = new JLabel("Search:");
	    searchlabel.setFont(new Font("SansSerif", Font.BOLD,11));
	    this.searchfield = new JTextField();
	    SearchListener listener = new SearchListener();
	    this.searchfield.addMouseMotionListener(listener);
	    this.searchfield.addKeyListener(listener);
	    this.searchfield.addFocusListener(new TextpaneFocusListener());
	    this.searchfield.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.darkGray));
	    this.searchPanel.add(Box.createRigidArea(new Dimension(10,20)));
	    this.searchPanel.add(searchlabel);
	    this.searchPanel.add(Box.createRigidArea(new Dimension(10,20)));
	    this.searchPanel.add(this.searchfield);
	    this.searchSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,searchPanel,diagram.getDrawPanel().getScrollPanel());
	    this.searchSplit.setDividerSize(0);
	    this.searchSplit.setResizeWeight(1);
	    this.searchSplit.setEnabled(false);
	    this.searchSplit.setBorder(null);
	    this.searchPanel.setVisible(false);
	    

	    mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,searchSplit,rightSplit);
	    mainSplit.setDividerSize(2);
	    mainSplit.setDividerLocation(Constants.main_split_position);
	    mainSplit.setResizeWeight(1);
	    mainSplit.setBorder(null);
	    
	    customSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,mainSplit,custompanel);
	    customSplit.setDividerSize(0);
	    customSplit.setResizeWeight(1);
	    customSplit.setBorder(null);
	    custompanel.setVisible(false);
	    
	    editor.add(customSplit);	    
		return editor;
	}
	
	public Panel getPanel() {
		return this.embedded_umlet_panel;
	}
	
	public CustomElementHandler getCustomElementHandler() {
		return this.customhandler;
	}
	
	public void exportToFormat(String format) {
		try
		{
			this.setFocus();
			ByteArrayOutputStream outdata = new ByteArrayOutputStream();
			if (format.equalsIgnoreCase("PDF"))
				GenPdf.getInstance().createPdfToStream(outdata,this.diagram);
			else if (format.equalsIgnoreCase("SVG"))
				GenSvg.createSvgToStream(outdata,this.diagram);
			else if (format.equalsIgnoreCase("EPS"))
				GenEps.getInstance().createEpsToStream(outdata,this.diagram);
			else
				GenPic.getInstance().createImgToStream(format.toLowerCase(),outdata,this.diagram);
			
			IFile selFile = (IFile) ((FileEditorInput) getEditorInput()).getStorage();
			IContainer targetFolder = selFile.getParent();
					
			IPath newFilePath = targetFolder.getFullPath().append("/"+selFile.getName().substring(0, selFile.getName().length()-4)+"."+format.toLowerCase());
                        //[UB]: changed WorkspacePlugin to ResourcesPlugin for eclipse3.0 compatibility
			IFile newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(newFilePath);
			if (!newFile.exists()) {
				newFile.create(new ByteArrayInputStream(outdata.toByteArray()), false, null);
			} else {
				newFile.setContents(new ByteArrayInputStream(outdata.toByteArray()), false, true, null);
			}
		} catch (CoreException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void setCustomPanelEnabled(boolean enable) {
		if(this.custompanel.isVisible() != enable)
		{
			int loc = this.mainSplit.getDividerLocation();
			if(enable) {
				int rightloc = this.rightSplit.getDividerLocation();
				this.customSplit.setDividerLocation(rightloc);
				this.customSplit.setDividerSize(2);
				this.mainSplit.setRightComponent(this.palettetabs);
				this.custompanel.getRightSplit().setDividerLocation(loc);
				this.custompanel.getRightSplit().updateUI();
				this.custompanel.getLeftSplit().setLeftComponent(this.propertytextpane.getPanel());
				this.custompanel.getLeftSplit().setDividerLocation(this.diagram.getDrawPanel().getScrollPanel().getWidth()/2);
				this.custompanel.getLeftSplit().updateUI();
			}
			else {
				int rightloc = this.customSplit.getDividerLocation();
				this.customSplit.setDividerSize(0);
				this.mainSplit.setRightComponent(this.rightSplit);
				this.rightSplit.setLeftComponent(this.palettetabs);
				this.rightSplit.setRightComponent(this.propertytextpane.getPanel());
				this.rightSplit.setDividerLocation(rightloc);
				this.rightSplit.updateUI();
				this.custompanel.getLeftSplit().setDividerLocation(this.diagram.getDrawPanel().getScrollPanel().getWidth()/2);
			}
			
			this.mainSplit.setDividerLocation(loc);
			this.custompanel.setVisible(enable);
			
			this.setDiagramsEnabled(!enable);
		}
	}
	
	private void setDiagramsEnabled(boolean enable) {
		this.palettetabs.setEnabled(enable);
		for(Component c : this.palettetabs.getComponents())
			c.setEnabled(enable);
		this.diagram.getDrawPanel().getScrollPanel().setEnabled(enable);
	}
	
	public void repaint() 
	{
		javax.swing.SwingUtilities.invokeLater(new RepaintGUI(this.embedded_umlet_panel));
	}
	
	public void enableSearch(boolean enable) {
		this.searchPanel.setVisible(enable);
		if(enable)
		{
		    this.searchSplit.setDividerSize(1);
		    this.searchSplit.setDividerLocation(20);
			this.searchfield.requestFocus();
		}
		else
		{
			this.searchfield.setText("");
		    this.searchSplit.setDividerSize(0);
			this.requestFocus();
		}
	}
	
	public String getSelectedPalette() {
		return this.palettetabs.getTitleAt(this.palettetabs.getSelectedIndex()) + ".uxf";
	}
}
