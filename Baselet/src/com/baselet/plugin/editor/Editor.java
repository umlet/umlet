package com.baselet.plugin.editor;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.baselet.control.Constants;
import com.baselet.control.Constants.Program;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.PaletteHandler;
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

	private UUID uuid = UUID.randomUUID();

	@Override
	public void doSave(IProgressMonitor monitor) {
		handler.doSave();
		monitor.done();
	}

	@Override
	public void doSaveAs() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handler.doSaveAs(Program.EXTENSION);
			}
		});
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		log.info("Call editor.init() " + uuid.toString());
		this.setSite(site);
		this.setInput(input);
		this.setPartName(input.getName());
		final File file = getFile(input); 

		try { //use invokeAndWait to make sure the following code is only invoked after everything is initialized
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					log.debug("Create new DiagramHandler");
					handler = new DiagramHandler(file);

					customhandler = new CustomElementHandler();
					custompanel = customhandler.getPanel();
					custompanel.getTextPane().addFocusListener(new CustomCodePaneFocusListener());
					mailpanel = Main.getInstance().getGUI().createMailPanel();
					propertyTextPane = Main.getInstance().getGUI().createPropertyTextPane();
					propertyTextPane.getTextComponent().addFocusListener(new TextPaneFocusListener());

					embedded_panel = new Panel();
					embedded_panel.setLayout(new BorderLayout());
					embedded_panel.add(createEditor());
					embedded_panel.addKeyListener(new GUIListener());
					log.debug("DiagramHandler created...");
				}
			});
		} catch (InterruptedException e) {
			log.error("Create DiagramHandler interrupted");
		} catch (InvocationTargetException e) {
			log.error("Create DiagramHandler invocation exception");
		}

	}

	private File getFile(IEditorInput input) throws PartInitException {
		if (input instanceof IFileEditorInput) { // Files opened from workspace
			return ((IFileEditorInput) input).getFile().getLocation().toFile();
		}
		else if (input instanceof org.eclipse.ui.ide.FileStoreEditorInput) { // Files from outside of the workspace (eg: edit current palette)
			return new File(((org.eclipse.ui.ide.FileStoreEditorInput) input).getURI());
		}
		else throw new PartInitException("Editor input not supported.");
	}

	@Override
	public boolean isDirty() {
		return this.handler.isChanged();
	}

	@Override
	public void createPartControl(Composite parent) {
		log.info("Call editor.createPartControl() " + uuid.toString());
		final Frame frame = SWT_AWT.new_Frame(new Composite(parent, SWT.EMBEDDED));
		frame.add(embedded_panel);
	}

	public boolean hasFocus() {
		return this.equals(this.getSite().getPage().getActivePart());
	}

	@Override
	public void setFocus() {
		if (hasFocus()) {
			log.info("Call editor.setFocus() skipped (view already has focus)" + uuid.toString());
			return;
		}
		log.info("Call editor.setFocus() " + uuid.toString());

		MainPlugin.getGUI().setCurrentEditor(Editor.this);
		Main.getInstance().setCurrentDiagramHandler(handler);	
		if (handler != null) handler.getDrawPanel().getSelector().updateSelectorInformation();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {	
				/**
				 * usually the palettes get lost (for unknown reasons) after switching the editor, therefore recreate them.
				 * also reselect the current palette and repaint every element with scrollbars (otherwise they have a visual error)
				 */
				if (palettePanel.getComponentCount()==0) {
					for (PaletteHandler palette : Main.getInstance().getPalettes().values()) {
						palettePanel.add(palette.getDrawPanel().getScrollPane(), palette.getName());
					}
				}
				showPalette(getSelectedPaletteName());
				Main.getInstance().getGUI().setValueOfZoomDisplay(handler.getGridSize());
			}
		});
	}

	public DrawPanel getDiagram() {
		if (handler == null) return null;
		return this.handler.getDrawPanel();
	}

	@Override
	public void dispose() {
		super.dispose();
		log.info("Call editor.dispose( )" + uuid.toString());
		//AB: The eclipse plugin might hang sometimes if this section is not placed into an event queue, since swing or swt is not thread safe!
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (mailpanel.isVisible()) mailpanel.closePanel();
				MainPlugin.getGUI().editorRemoved(Editor.this);
			}
		});
	}

	public void setCursor(Cursor cursor) {
		this.embedded_panel.setCursor(cursor);
	}

	public OwnSyntaxPane getPropertyPane() {
		return this.propertyTextPane;
	}

	public JTextComponent getCustomPane() {
		return custompanel.getTextPane();
	}

	public void requestFocus() {
		this.embedded_panel.requestFocus();
	}

	public void dirtyChanged() {
		org.eclipse.swt.widgets.Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				firePropertyChange(IEditorPart.PROP_DIRTY);
			}
		});
	}

	public void diagramNameChanged() {
		org.eclipse.swt.widgets.Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				firePropertyChange(IWorkbenchPart.PROP_TITLE);
			}
		});
	}

	private JPanel createEditor() {
		JPanel editor = new JPanel();
		editor.setLayout(new BorderLayout());

		palettePanel = new JPanel(new CardLayout());
		paletteList = new JComboBox();
		paletteList.setMaximumRowCount(15);
		for (PaletteHandler palette : Main.getInstance().getPalettes().values()) {
			palettePanel.add(palette.getDrawPanel().getScrollPane(), palette.getName());
			paletteList.addItem(palette.getName());
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

		paletteList.setSelectedItem(Constants.lastUsedPalette);

		this.searchPanel = new JPanel();
		this.searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
		JLabel searchlabel = new JLabel("Search:");
		searchlabel.setFont(Constants.PANEL_HEADER_FONT);
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
			int mailDividerLoc= Math.max(Constants.MIN_MAIL_SPLITPANEL_SIZE, Constants.mail_split_position);
			mailSplit.setDividerLocation(mailDividerLoc);
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
		this.handler.getDrawPanel().getScrollPane().setEnabled(enable);
	}

	public void repaint() {
		SwingUtilities.invokeLater(new Runnable() {
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

	public void showPalette(final String paletteName) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				CardLayout palettePanelLayout = (CardLayout) palettePanel.getLayout();
				palettePanelLayout.show(palettePanel, paletteName);
				palettePanel.repaint();
				paletteList.repaint();
			}
		});
	}
}
