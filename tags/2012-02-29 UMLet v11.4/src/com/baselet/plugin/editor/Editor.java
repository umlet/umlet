package com.baselet.plugin.editor;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import com.baselet.control.Constants;
import com.baselet.control.Constants.Program;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.PaletteHandler;
import com.baselet.diagram.io.OutputHandler;
import com.baselet.gui.MailPanel;
import com.baselet.gui.OwnSyntaxPane;
import com.baselet.gui.eclipse.CustomCodePaneFocusListener;
import com.baselet.gui.eclipse.TextPaneFocusListener;
import com.baselet.gui.listener.DividerListener;
import com.baselet.gui.listener.GUIListener;
import com.baselet.gui.listener.PaletteComboBoxListener;
import com.baselet.gui.standalone.SearchListener;
import com.baselet.plugin.MainPlugin;
import com.umlet.custom.CustomElementHandler;
import com.umlet.gui.CustomElementPanel;

public class Editor extends EditorPart {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	private DiagramHandler handler;
	private CustomElementHandler customhandler;
	private CustomElementPanel custompanel;
	private MailPanel mailpanel;
	private Hashtable<String, PaletteHandler> palettes;
	private Panel embedded_panel;
	private OwnSyntaxPane propertyTextPane;
	private JTextField searchfield;
	private JPanel searchPanel;
	private JPanel palettePanel;
	private JPanel rightPanel;
	private JComboBox paletteList;

	private JSplitPane rightSplit;
	private JSplitPane mainSplit;
	private JSplitPane customSplit;
	private JSplitPane searchSplit;
	private JSplitPane mailSplit;
	private boolean mail_panel_visible;

	private static Editor currenteditor;

	public static Editor getCurrent() {
		return currenteditor;
	}

	public Editor() {
		log.info("Create new Editor()");
		//we have to set this to false since multiple instances of Editor are created
		this.customhandler = new CustomElementHandler();
		this.custompanel = this.customhandler.getPanel();
		this.custompanel.getTextPane().addFocusListener(new CustomCodePaneFocusListener());
		this.mailpanel = Main.getInstance().getGUI().createMailPanel();
		this.propertyTextPane = Main.getInstance().getGUI().createPropertyTextPane();
		this.propertyTextPane.addFocusListener(new TextPaneFocusListener());
		this.palettes = Main.getInstance().getPalettes();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		//AB: This should also be made thread safe but the return value makes it difficulty. maybe this works anyway.
		log.info("Call editor.doSave()");
		if (handler.doSave()) {
			log.debug("fireCleanProperty");
			fireCleanProperty();
			log.debug("monitor.done");
			monitor.done();
		}
		log.debug("doSave complete");

	}

	@Override
	public void doSaveAs() {}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		log.info("Call editor.init()");
		this.setSite(site);
		this.setInput(input);
		this.setPartName(input.getName());

		File inputFile = null;

		if (input instanceof IFileEditorInput) { // Files opened from workspace
			inputFile = ((IFileEditorInput) input).getFile().getLocation().toFile();
		}
		else if (input instanceof org.eclipse.ui.ide.FileStoreEditorInput) { // Files from outside of the workspace (eg: edit current palette)
			inputFile = new File(((org.eclipse.ui.ide.FileStoreEditorInput) input).getURI());
		}
		else throw new PartInitException("Editor input not supported.");

		final File file = inputFile; 

		//AB: The eclipse plugin might hang sometimes if this section is not placed into an event queue, since swing or swt is not thread safe!
		//AB: Problem is...using invokeLater might lead to other NullPointerExceptions :P -> use invokeAndWait and hope this works	
		try {
			SwingUtilities.invokeAndWait(
					new Runnable() {
						@Override
						public void run() {
							log.debug("Create new DiagramHandler");
							handler = new DiagramHandler(file);
							log.debug("DiagramHandler created...");
						}
					});
		} catch (InterruptedException e) {
			log.error("Create DiagramHandler interrupted");
		} catch (InvocationTargetException e) {
			log.error("Create DiagramHandler invocation exception");
		}
	}

	@Override
	public boolean isDirty() {
		return this.handler.isChanged();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		log.info("Call editor.createPartControl()");
		Composite goodSWTComposite = new Composite(parent, SWT.EMBEDDED); // we need the embedded attribute set
		Frame frame = org.eclipse.swt.awt.SWT_AWT.new_Frame(goodSWTComposite);
		embedded_panel = new Panel();
		embedded_panel.setLayout(new BorderLayout());
		embedded_panel.add(createEditor());
		embedded_panel.addKeyListener(new GUIListener());
		frame.add(embedded_panel);
	}

	@Override
	public void setFocus() {
		log.info("Call editor.setFocus()");
		currenteditor = this;
		MainPlugin.getGUI().setCurrentEditor(this);
		log.debug("setCurrentEditor complete");

		//AB: The eclipse plugin might hang sometimes if this section is not placed into an event queue, since swing or swt is not thread safe!	
		SwingUtilities.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						log.debug("setFocus thread");
						Main.getInstance().setCurrentDiagramHandler(handler);			
						if (handler != null) handler.getDrawPanel().getSelector().updateSelectorInformation();					

						//when switching to another editor frame, check if palettePanel got lost
						if (palettePanel.getComponentCount()==0) {
							for (String palname : Main.getInstance().getPaletteNames(palettes)) {
								DrawPanel panel = palettes.get(palname + "." + Program.EXTENSION).getDrawPanel();
								palettePanel.add(panel.getScrollPane(), palname);
							}
						}

						selectPalette(getSelectedPaletteName());
						log.debug("editor.setFocus thread complete");
					}		
				});
		Main.getInstance().getGUI().setValueOfZoomDisplay(handler.getGridSize());
		log.debug("editor.setFocus complete");
	}

	public DrawPanel getDiagram() {
		return this.handler.getDrawPanel();
	}

	@Override
	public void dispose() {
		super.dispose();
		log.info("Call editor.dispose()");
		//AB: The eclipse plugin might hang sometimes if this section is not placed into an event queue, since swing or swt is not thread safe!
		final Editor editor = this;
		SwingUtilities.invokeLater(
				new Runnable() {
					@Override
					public void run() {		
						if (mailpanel.isVisible()) mailpanel.closePanel();
						MainPlugin.getGUI().editorRemoved(editor);
					}
				});
	}

	public void setCursor(Cursor cursor) {
		this.embedded_panel.setCursor(cursor);
	}

	public OwnSyntaxPane getPropertyPane() {
		return this.propertyTextPane;
	}

	public JEditorPane getCustomPane() {
		return custompanel.getTextPane();
	}

	public void requestFocus() {
		this.embedded_panel.requestFocus();
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

	// Adds the star right to the tab to show that there are some changes which have not been stored
	protected void fireDirtyProperty() {
		this.firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	// Removes the star right to the tab to show that the changes have yet been stored
	protected void fireCleanProperty() {
		this.firePropertyChange(IEditorPart.PROP_INPUT);
	}

	private JPanel createEditor() {
		JPanel editor = new JPanel();
		editor.setLayout(new BorderLayout());

		palettePanel = new JPanel(new CardLayout());
		paletteList = new JComboBox();
		paletteList.setMaximumRowCount(15);
		for (String palname : Main.getInstance().getPaletteNames(palettes)) {
			DrawPanel panel = palettes.get(palname + "." + Program.EXTENSION).getDrawPanel();
			palettePanel.add(panel.getScrollPane(), palname);
			paletteList.addItem(palname);
		}

		rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, palettePanel, propertyTextPane.getPanel());
		rightSplit.setDividerSize(2);
		rightSplit.setDividerLocation(Main.getInstance().getGUI().getRightSplitPosition());
		rightSplit.setResizeWeight(1);
		rightSplit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));

		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		paletteList.setAlignmentX(Component.CENTER_ALIGNMENT);
		rightSplit.setAlignmentX(Component.CENTER_ALIGNMENT);
		PaletteComboBoxListener pl = new PaletteComboBoxListener();
		paletteList.addActionListener(pl);
		paletteList.addMouseWheelListener(pl);
		rightPanel.add(paletteList);
		rightPanel.add(rightSplit);

		selectPalette(this.getSelectedPaletteName());

		this.searchPanel = new JPanel();
		this.searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
		JLabel searchlabel = new JLabel("Search:");
		searchlabel.setFont(new Font("SansSerif", Font.BOLD, 11));
		this.searchfield = new JTextField();
		SearchListener listener = new SearchListener();
		this.searchfield.addMouseMotionListener(listener);
		this.searchfield.addKeyListener(listener);
		this.searchfield.addFocusListener(new TextPaneFocusListener());
		this.searchfield.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.darkGray));
		this.searchPanel.add(Box.createRigidArea(new Dimension(10, 20)));
		this.searchPanel.add(searchlabel);
		this.searchPanel.add(Box.createRigidArea(new Dimension(10, 20)));
		this.searchPanel.add(this.searchfield);
		this.searchSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, searchPanel, handler.getDrawPanel().getScrollPane());
		this.searchSplit.setDividerSize(0);
		this.searchSplit.setResizeWeight(1);
		this.searchSplit.setEnabled(false);
		this.searchSplit.setBorder(null);
		this.searchPanel.setVisible(false);

		mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, searchSplit, rightPanel);
		mainSplit.setDividerSize(2);
		mainSplit.setDividerLocation(Main.getInstance().getGUI().getMainSplitPosition());
		mainSplit.setResizeWeight(1);
		mainSplit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));

		customSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainSplit, custompanel);
		customSplit.setDividerSize(0);
		customSplit.setResizeWeight(1);
		customSplit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
		custompanel.setVisible(false);

		mailSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mailpanel, customSplit);
		mailSplit.setDividerSize(0);
		mailSplit.setDividerLocation(Constants.mail_split_position);
		mailSplit.setResizeWeight(0);
		mailSplit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
		mailpanel.setVisible(false);

		// Adding the DividerListener which refreshes Scrollbars here is enough for all dividers
		palettePanel.addComponentListener(new DividerListener());

		editor.add(mailSplit);

		return editor;
	}

	public Panel getPanel() {
		return this.embedded_panel;
	}

	public CustomElementHandler getCustomElementHandler() {
		return this.customhandler;
	}

	public void exportToFormat(String format) {
		try {
			this.setFocus();
			ByteArrayOutputStream outdata = new ByteArrayOutputStream();

			try {
				OutputHandler.createToStream(format.toLowerCase(), outdata, this.handler);
			} catch (Exception e) {
				log.error(null, e);
			}

			IFile selFile = (IFile) ((FileEditorInput) getEditorInput()).getStorage();
			IContainer targetFolder = selFile.getParent();

			IPath newFilePath = targetFolder.getFullPath().append("/" + selFile.getName().substring(0, selFile.getName().length() - 4) + "." + format.toLowerCase());
			// [UB]: changed WorkspacePlugin to ResourcesPlugin for eclipse3.0 compatibility
			IFile newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(newFilePath);
			if (!newFile.exists()) {
				newFile.create(new ByteArrayInputStream(outdata.toByteArray()), false, null);
			}
			else {
				newFile.setContents(new ByteArrayInputStream(outdata.toByteArray()), false, true, null);
			}
		} catch (CoreException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void setCustomPanelEnabled(boolean enable) {
		if (this.custompanel.isVisible() != enable) {
			int loc = this.mainSplit.getDividerLocation();
			this.custompanel.setVisible(enable);

			if (enable) {
				int rightloc = this.rightSplit.getDividerLocation();

				this.customSplit.setDividerSize(2);
				this.rightSplit.setDividerSize(0);

				this.custompanel.getLeftSplit().setLeftComponent(this.propertyTextPane.getPanel());
				this.customSplit.setDividerLocation(rightloc);

				this.custompanel.getRightSplit().setDividerLocation(loc);
				this.custompanel.getLeftSplit().setDividerLocation(this.handler.getDrawPanel().getScrollPane().getWidth() / 2);
				this.custompanel.getLeftSplit().updateUI();
			}
			else {
				int rightloc = this.customSplit.getDividerLocation();

				this.customSplit.setDividerSize(0);

				this.rightSplit.setDividerSize(2);
				this.rightSplit.setRightComponent(this.propertyTextPane.getPanel());
				this.rightSplit.setDividerLocation(rightloc);
			}
			this.mainSplit.setDividerLocation(loc);
		}
		this.setDiagramsEnabled(!enable);
	}

	public void setMailPanelEnabled(boolean enable) {
		mailpanel.setVisible(enable);
		if (enable) {
			mailSplit.setDividerLocation(Constants.mail_split_position);
			mailSplit.setDividerSize(2);
		}
		else {
			mailSplit.setDividerSize(0);
		}
		mail_panel_visible = enable;
	}

	public boolean isMailPanelVisible() {
		return mail_panel_visible;
	}

	private void setDiagramsEnabled(boolean enable) {
		// this.palettetabs.setEnabled(enable);
		// for (Component c : this.palettetabs.getComponents())
		// c.setEnabled(enable);
		this.handler.getDrawPanel().getScrollPane().setEnabled(enable);
	}

	public void repaint() {
		SwingUtilities.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						embedded_panel.repaint();
					}
				});
	}

	public void enableSearch(boolean enable) {
		this.searchPanel.setVisible(enable);
		if (enable) {
			this.searchSplit.setDividerSize(1);
			this.searchSplit.setDividerLocation(20);
			this.searchfield.requestFocus();
		}
		else {
			this.searchfield.setText("");
			this.searchSplit.setDividerSize(0);
			this.requestFocus();
		}
	}

	public String getSelectedPaletteName() {
		return this.paletteList.getSelectedItem().toString();
	}

	public int getMainSplitLocation() {
		return mainSplit.getDividerLocation();
	}

	public int getRightSplitLocation() {
		return rightSplit.getDividerLocation();
	}

	public int getMailSplitLocation() {
		return mailSplit.getDividerLocation();
	}

	public void selectPalette(String paletteName) {
		CardLayout palettePanelLayout = (CardLayout) palettePanel.getLayout();
		palettePanelLayout.show(palettePanel, paletteName);
	}
}
