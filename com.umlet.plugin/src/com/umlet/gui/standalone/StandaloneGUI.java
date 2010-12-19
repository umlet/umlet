package com.umlet.gui.standalone;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.diagram.CustomPreviewHandler;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.control.diagram.PaletteHandler;
import com.umlet.control.io.DiagramFileHandler;
import com.umlet.custom.CustomElementHandler;
import com.umlet.gui.base.CustomElementPanel;
import com.umlet.gui.base.MailPanel;
import com.umlet.gui.base.TabComponent;
import com.umlet.gui.base.UmletGUI;
import com.umlet.gui.base.UmletTextPane;

@SuppressWarnings("serial")
public class StandaloneGUI extends UmletGUI {

	private JComboBox zoomComboBox;
	private JFrame window;
	private JSplitPane customSplit;
	private JSplitPane rightSplit;
	private JSplitPane mainSplit;
	private JSplitPane mailSplit;
	private JPanel diagramspanel;
	private JMenu menu_palettes;
	private JMenu menu_file;
	private JPanel palettepanel;
	private JTabbedPane diagramtabs;
	private JMenuItem menu_file_save;
	private JMenuItem menu_file_save_as;
	private JMenuItem menu_file_export_as_bmp;
	private JMenuItem menu_file_export_as_eps;
	private JMenuItem menu_file_export_as_gif;
	private JMenuItem menu_file_export_as_jpg;
	private JMenuItem menu_file_export_as_pdf;
	private JMenuItem menu_file_export_as_png;
	private JMenuItem menu_file_export_as_svg;
	private JMenuItem menu_edit_delete;
	private JMenuItem menu_edit_group;
	private JMenuItem menu_edit_ungroup;
	private JMenuItem menu_edit_cut;
	private JMenuItem menu_edit_copy;
	private JMenuItem menu_edit_paste;
	private JMenu menu_edit;
	private JMenu menu_export_as;
	private JMenuItem menu_send_per_mail;
	private JMenuItem menu_custom_new;
	private JMenu menu_custom_template;
	private JMenuItem menu_custom_edit;
	private JMenuItem menu_custom_tutorial;
	private JTextField searchField;
	private UmletTextPane propertyTextPane;
	private ZoomListener zoomListener;

	protected String selected_palette;

	private CustomElementHandler customelementhandler;
	private CustomElementPanel customPanel;
	private boolean custom_element_selected;
	private boolean custom_panel_visible;

	private MailPanel mailPanel;
	private JButton mailButton;
	private boolean mail_panel_visible;

	public StandaloneGUI(Umlet umlet) {
		super(umlet);
		custom_element_selected = false;
		custom_panel_visible = false;
		mail_panel_visible = false;
		selected_palette = "";
	}

	private void onDiagramOpened() {
		DrawPanel p = this.getCurrentDiagram();
		if (p != null) {
			Umlet.getInstance().setCurrentDiagram(p.getHandler());
		}
		else Umlet.getInstance().setCurrentDiagram(null);
	}

	private void onDiagramClosed() {
		DrawPanel p = this.getCurrentDiagram();
		if (p != null) Umlet.getInstance().setCurrentDiagram(p.getHandler());
		else Umlet.getInstance().setCurrentDiagram(null);
	}

	@Override
	public void updateDiagramName(DiagramHandler diagram, String name) {
		int index = this.diagramtabs.indexOfComponent(diagram.getDrawPanel().getScrollPanel());
		if (index != -1) {
			this.diagramtabs.setTitleAt(index, name);
		}
		this.diagramtabs.updateUI();
	}

	@Override
	public void setDiagramChanged(DiagramHandler diagram, boolean changed) {
		String change_string = "";
		if (changed) change_string = " *";

		this.updateDiagramName(diagram, diagram.getName() + change_string);
	}

	@Override
	public void setCustomElementChanged(CustomElementHandler handler, boolean changed) {

	}

	@Override
	public void closeWindow() {
		mailPanel.closePanel(); // We must close the mailpanel to save the input date
		if (Umlet.getInstance().askSaveIfDirty()) {
			this.umlet.close();
			this.window.dispose();
			System.exit(0);
		}
	}

	@Override
	protected void init() {

		this.window = new JFrame();
		this.window.setContentPane(this);
		this.window.addWindowListener(new WindowListener());
		this.window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.window.setBounds(Constants.umlet_location.x, Constants.umlet_location.y, Constants.umlet_size.width, Constants.umlet_size.height);
		this.window.setTitle("UMLet - Free UML Tool for Fast UML Diagrams");

		/************************ CREATE PROP PANE *****************/
		this.propertyTextPane = this.createPropertyTextPane();

		/*********** SET WINDOW BOUNDS **************/
		if (Constants.start_maximized) {
			// If UMLet starts maximized we set fixed bounds and must set the frame visible
			// now to avoid a bug where the right sidebar doesn't have the correct size
			this.window.setExtendedState(this.window.getExtendedState() | Frame.MAXIMIZED_BOTH);
			this.window.setVisible(true);
		}

		this.window.setTitle("UMLet - Free UML Tool for Fast UML Diagrams");
		/***************************************/

		/*********** CREATE MENU *****************/
		JMenuBar menu = new JMenuBar();
		StandaloneMenuListener mlistener = new StandaloneMenuListener();

		// File Menu
		menu_file = new JMenu("File");
		menu_file.setMnemonic(KeyEvent.VK_F);

		JMenuItem menu_file_new = new JMenuItem("New");
		menu_file_new.setMnemonic(KeyEvent.VK_N);
		menu_file_new.addActionListener(mlistener);
		menu_file_new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Constants.CTRLMETA_MASK));
		JMenuItem menu_file_open = new JMenuItem("Open...");
		menu_file_open.setMnemonic(KeyEvent.VK_O);
		menu_file_open.addActionListener(mlistener);
		menu_file_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Constants.CTRLMETA_MASK));
		menu_file_save = new JMenuItem("Save");
		menu_file_save.setMnemonic(KeyEvent.VK_S);
		menu_file_save.addActionListener(mlistener);
		menu_file_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Constants.CTRLMETA_MASK));
		menu_file_save_as = new JMenuItem("Save as...");
		menu_file_save_as.addActionListener(mlistener);

		// Submenu "Export as" entries
		menu_file_export_as_bmp = new JMenuItem("BMP...");
		menu_file_export_as_bmp.addActionListener(mlistener);
		menu_file_export_as_eps = new JMenuItem("EPS...");
		menu_file_export_as_eps.addActionListener(mlistener);
		menu_file_export_as_gif = new JMenuItem("GIF...");
		menu_file_export_as_gif.addActionListener(mlistener);
		menu_file_export_as_jpg = new JMenuItem("JPG...");
		menu_file_export_as_jpg.addActionListener(mlistener);
		menu_file_export_as_pdf = new JMenuItem("PDF...");
		menu_file_export_as_pdf.addActionListener(mlistener);
		menu_file_export_as_png = new JMenuItem("PNG...");
		menu_file_export_as_png.addActionListener(mlistener);
		menu_file_export_as_svg = new JMenuItem("SVG...");
		menu_file_export_as_svg.addActionListener(mlistener);

		// Add the submenu entries to the menupoint "Export as"
		menu_export_as = new JMenu("Export as");
		menu_export_as.add(menu_file_export_as_bmp);
		menu_export_as.add(menu_file_export_as_eps);
		menu_export_as.add(menu_file_export_as_gif);
		menu_export_as.add(menu_file_export_as_jpg);
		menu_export_as.add(menu_file_export_as_pdf);
		menu_export_as.add(menu_file_export_as_png);
		menu_export_as.add(menu_file_export_as_svg);

		menu_send_per_mail = new JMenuItem("Mail to...");
		menu_send_per_mail.setMnemonic(KeyEvent.VK_M);
		menu_send_per_mail.addActionListener(mlistener);
		menu_send_per_mail.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, Constants.CTRLMETA_MASK));

		JMenuItem menu_file_exit = new JMenuItem("Exit");
		menu_file_exit.addActionListener(mlistener);
		JMenuItem menu_file_options = new JMenuItem("Options...");
		menu_file_options.addActionListener(mlistener);
		JMenuItem menu_file_print = new JMenuItem("Print...");
		menu_file_print.setMnemonic(KeyEvent.VK_P);
		menu_file_print.addActionListener(mlistener);
		menu_file_print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Constants.CTRLMETA_MASK));

		menu_file.add(menu_file_new);
		menu_file.add(menu_file_open);
		menu_file.add(menu_file_save);
		menu_file.add(menu_file_save_as);
		menu_file.add(menu_export_as);
		menu_file.add(menu_send_per_mail);
		menu_file.addSeparator();
		menu_file.add(menu_file_options);
		menu_file.addSeparator();
		menu_file.add(menu_file_print);
		menu_file.addSeparator();
		menu_file.add(menu_file_exit);

		// Edit Menu
		menu_edit = new JMenu("Edit");
		menu_edit.setMnemonic(KeyEvent.VK_E);

		JMenuItem menu_edit_undo = new JMenuItem("Undo");
		menu_edit_undo.addActionListener(mlistener);
		menu_edit_undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Constants.CTRLMETA_MASK));
		JMenuItem menu_edit_redo = new JMenuItem("Redo");
		menu_edit_redo.addActionListener(mlistener);
		menu_edit_redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Constants.CTRLMETA_MASK));
		menu_edit_delete = new JMenuItem("Delete");
		menu_edit_delete.addActionListener(mlistener);
		menu_edit_delete.setAccelerator(KeyStroke.getKeyStroke("DELETE"));

		JMenuItem menu_edit_select_all = new JMenuItem("Select all");
		menu_edit_select_all.addActionListener(mlistener);
		menu_edit_select_all.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Constants.CTRLMETA_MASK));
		menu_edit_group = new JMenuItem("Group");
		menu_edit_group.addActionListener(mlistener);
		menu_edit_group.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, Constants.CTRLMETA_MASK));
		menu_edit_ungroup = new JMenuItem("Ungroup");
		menu_edit_ungroup.addActionListener(mlistener);
		menu_edit_ungroup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, Constants.CTRLMETA_MASK));

		menu_edit_cut = new JMenuItem("Cut");
		menu_edit_cut.addActionListener(mlistener);
		menu_edit_cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Constants.CTRLMETA_MASK));
		menu_edit_copy = new JMenuItem("Copy");
		menu_edit_copy.addActionListener(mlistener);
		menu_edit_copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Constants.CTRLMETA_MASK));
		menu_edit_paste = new JMenuItem("Paste");
		menu_edit_paste.addActionListener(mlistener);
		menu_edit_paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Constants.CTRLMETA_MASK));

		menu_edit.add(menu_edit_undo);
		menu_edit.add(menu_edit_redo);
		menu_edit.add(menu_edit_delete);
		menu_edit.addSeparator();
		menu_edit.add(menu_edit_select_all);
		menu_edit.add(menu_edit_group);
		menu_edit.add(menu_edit_ungroup);
		menu_edit.addSeparator();
		menu_edit.add(menu_edit_cut);
		menu_edit.add(menu_edit_copy);
		menu_edit.add(menu_edit_paste);

		// disable menues that require some opened diagram...
		menu_file_save.setEnabled(false);
		menu_file_save_as.setEnabled(false);
		menu_file_export_as_bmp.setEnabled(false);
		menu_file_export_as_eps.setEnabled(false);
		menu_file_export_as_gif.setEnabled(false);
		menu_file_export_as_jpg.setEnabled(false);
		menu_file_export_as_pdf.setEnabled(false);
		menu_file_export_as_png.setEnabled(false);
		menu_file_export_as_svg.setEnabled(false);
		menu_edit.setEnabled(false);
		menu_edit_delete.setEnabled(false);
		menu_edit_group.setEnabled(false);
		menu_edit_cut.setEnabled(false);
		menu_edit_paste.setEnabled(false);
		menu_edit_ungroup.setEnabled(false);

		// palette menu
		PaletteMenuListener pmlistener = new PaletteMenuListener();
		this.menu_palettes = new JMenu("Palettes");
		this.menu_palettes.setMnemonic(KeyEvent.VK_P);
		List<String> palettenames = this.umlet.getPaletteNames();
		for (String palette : palettenames) {
			JMenuItem palette_item = new JMenuItem(palette);
			palette_item.addActionListener(pmlistener);
			this.menu_palettes.add(palette_item);
		}
		JMenuItem palette_edit = new JMenuItem("Edit Current Palette");
		palette_edit.addActionListener(mlistener);
		this.menu_palettes.addSeparator();
		this.menu_palettes.add(palette_edit);

		// Custom Element Menu
		JMenu menu_custom = new JMenu("Custom Elements");
		menu_custom.setMnemonic(KeyEvent.VK_C);
		menu_custom_new = new JMenuItem("New...");
		menu_custom_new.addActionListener(mlistener);
		TemplateMenuListener tmlistener = new TemplateMenuListener();
		menu_custom_template = new JMenu("New from Template");
		List<String> templatenames = this.umlet.getTemplateNames();
		for (String template : templatenames) {
			JMenuItem template_item = new JMenuItem(template);
			template_item.addActionListener(tmlistener);
			this.menu_custom_template.add(template_item);
		}
		menu_custom_edit = new JMenuItem("Edit Selected...");
		menu_custom_edit.addActionListener(mlistener);
		menu_custom_edit.setEnabled(false);
		menu_custom_tutorial = new JMenuItem("Custom Elements Tutorial...");
		menu_custom_tutorial.addActionListener(mlistener);
		menu_custom.add(menu_custom_new);
		menu_custom.add(menu_custom_template);
		menu_custom.add(menu_custom_edit);
		menu_custom.addSeparator();
		menu_custom.add(menu_custom_tutorial);

		// Help Menu
		JMenu menu_help = new JMenu("Help");
		menu_help.setMnemonic(KeyEvent.VK_H);

		JMenuItem menu_help_onlineHelp = new JMenuItem("Online Help...");
		menu_help_onlineHelp.addActionListener(mlistener);
		menu_help.add(menu_help_onlineHelp);

		JMenuItem menu_help_samples = new JMenuItem("Online Sample Diagrams...");
		menu_help_samples.addActionListener(mlistener);
		menu_help.add(menu_help_samples);

		menu_help.addSeparator();

		JMenuItem menu_help_homepage = new JMenuItem("UMLet Homepage...");
		menu_help_homepage.addActionListener(mlistener);
		menu_help.add(menu_help_homepage);

		JMenuItem menu_help_rateUmlet = new JMenuItem("Rate UMLet at EclipsePluginCentral...");
		menu_help_rateUmlet.addActionListener(mlistener);
		menu_help.add(menu_help_rateUmlet);

		menu_help.addSeparator();

		JMenuItem menu_help_about = new JMenuItem("About UMLet");
		menu_help_about.addActionListener(mlistener);
		menu_help.add(menu_help_about);

		// Add all submenus to the main menu

		menu.add(menu_file);
		menu.add(menu_edit);
		menu.add(this.menu_palettes);
		menu.add(menu_custom);
		menu.add(menu_help);

		/* Search field */

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
		JLabel searchLabel = new JLabel("Search:   ");
		searchField = new JTextField(10);
		searchField.setMinimumSize(searchField.getPreferredSize());
		searchField.setMaximumSize(searchField.getPreferredSize());
		searchField.addKeyListener(new SearchListener());

		// searchPanel.add(Box.createHorizontalGlue());
		searchPanel.add(Box.createRigidArea(new Dimension(50, 0)));
		searchPanel.add(searchLabel);
		searchPanel.add(searchField);
		searchPanel.add(Box.createRigidArea(new Dimension(20, 0)));

		menu.add(searchPanel);

		/* Zoom field */

		JPanel zoomPanel = new JPanel();
		zoomPanel.setLayout(new BoxLayout(zoomPanel, BoxLayout.X_AXIS));
		JLabel zoomLabel = new JLabel("Zoom:   ");
		zoomComboBox = new JComboBox();
		zoomComboBox.setPreferredSize(new Dimension(80, 24));
		zoomComboBox.setMinimumSize(zoomComboBox.getPreferredSize());
		zoomComboBox.setMaximumSize(zoomComboBox.getPreferredSize());
		zoomListener = new ZoomListener();
		zoomComboBox.addActionListener(zoomListener);
		zoomComboBox.setToolTipText("Use ± or Ctrl+mouse wheel to zoom");

		String[] zoomValues = new String[] { "10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "100%", "110%", "120%", "130%", "140%", "150%", "160%", "170%", "180%", "190%", "200%" };
		zoomComboBox.setModel(new DefaultComboBoxModel(zoomValues));
		zoomComboBox.setSelectedIndex(9);

		zoomPanel.add(zoomLabel);
		zoomPanel.add(zoomComboBox);
		zoomPanel.add(Box.createRigidArea(new Dimension(20, 0)));

		menu.add(zoomPanel);

		mailButton = new JButton("Mail diagram");

		mailButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMailPanelEnabled(!isMailPanelVisible());
			}
		});
		menu.add(mailButton);

		// Set the finished menu
		this.window.setJMenuBar(menu);

		/************************ CREATE SUB PANELS ******************/

		// create custom element handler
		this.customelementhandler = new CustomElementHandler();
		this.customPanel = this.customelementhandler.getPanel();

		diagramspanel = new JPanel();
		this.palettepanel = new JPanel(new CardLayout());

		rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, palettepanel, this.propertyTextPane.getPanel());
		rightSplit.setDividerSize(2);
		rightSplit.setDividerLocation(Constants.right_split_position);
		rightSplit.setResizeWeight(1);
		rightSplit.setBorder(null);

		mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, diagramspanel, rightSplit);
		mainSplit.setDividerSize(2);
		mainSplit.setDividerLocation(Constants.main_split_position);
		mainSplit.setResizeWeight(1);
		mainSplit.setBorder(null);

		customSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainSplit, customPanel);
		customSplit.setDividerSize(0);
		customSplit.setResizeWeight(1);
		customSplit.setBorder(null);
		customPanel.setVisible(false);

		mailPanel = this.createMailPanel();
		mailSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mailPanel, customSplit);
		mailSplit.setDividerSize(0);
		mailSplit.setDividerLocation(Constants.mail_split_position);
		mailSplit.setResizeWeight(1);
		mailSplit.setBorder(null);
		mailPanel.setVisible(false);

		DividerListener dividerListener = new DividerListener();
		customSplit.addComponentListener(dividerListener);
		rightSplit.addComponentListener(dividerListener);
		mainSplit.addComponentListener(dividerListener);
		mailSplit.addComponentListener(dividerListener);

		/******************* ADD PALETTES ***************************/
		Hashtable<String, PaletteHandler> palettetable = umlet.getPalettes();
		for (String palname : palettetable.keySet()) {
			DrawPanel panel = palettetable.get(palname).getDrawPanel();
			palettepanel.add(panel.getScrollPanel(), palname);
		}

		/***************** SETTING TABS AND TAB LAYOUT ***********************/

		diagramtabs = new JTabbedPane();
		diagramtabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		diagramspanel.setLayout(new GridLayout(1, 1));
		diagramspanel.add(diagramtabs);

		/********************* ADD KEYBOARD ACTIONS ************************/
		Action findaction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				Umlet.getInstance().getGUI().enableSearch(true);
			}
		};
		this.getActionMap().put("focussearch", findaction);
		this.getInputMap().put(KeyStroke.getKeyStroke('/'), "focussearch");

		/************************ ADD TOP COMPONENT ************************/
		this.add(mailSplit);
		/************************** SET DEFAULT INITIALIZATION VALUES ******/
		if (!palettenames.isEmpty()) this.selectPalette(palettenames.get(0));

		ToolTipManager.sharedInstance().setInitialDelay(100);
		/*************************************************************/

		this.window.setVisible(true);
	}

	@Override
	public void selectPalette(String palette) {
		if (this.selected_palette.equals(palette)) return;

		for (Component c : this.menu_palettes.getMenuComponents()) {
			if (c instanceof JMenuItem) {
				JMenuItem item = (JMenuItem) c;
				if (item.getText().equals(this.selected_palette)) {
					// item.setEnabled(true);
					item.setIcon(null);
					item.repaint();
				}
				else if (item.getText().equals(palette)) {
					// item.setEnabled(false);
					item.setIcon(new ImageIcon("icons/ok.gif"));
					item.repaint();
				}
			}
		}

		this.selected_palette = palette;

		CardLayout cl = (CardLayout) (this.palettepanel.getLayout());
		cl.show(this.palettepanel, this.getSelectedPalette());
	}

	@Override
	public String getSelectedPalette() {
		return this.selected_palette + ".uxf";
	}

	@Override
	public void close(DiagramHandler diagram) {
		diagramtabs.remove(diagram.getDrawPanel().getScrollPanel());
		this.onDiagramClosed();
	}

	@Override
	public void open(DiagramHandler diagram) {
		diagramtabs.add(diagram.getName(), diagram.getDrawPanel().getScrollPanel());
		diagramtabs.setTabComponentAt(diagramtabs.getTabCount() - 1, new TabComponent(diagramtabs, diagram));
		diagramtabs.setSelectedComponent(diagram.getDrawPanel().getScrollPanel());
		diagram.getDrawPanel().getSelector().updateSelectorInformation();
		this.onDiagramOpened();
	}

	@Override
	public DrawPanel getCurrentDiagram() {
		JScrollPane scr = (JScrollPane) this.diagramtabs.getSelectedComponent();
		if (scr != null) return (DrawPanel) scr.getViewport().getView();
		else return null;
	}

	@Override
	public void elementsSelected(int count) {
		super.elementsSelected(count);
		if (count > 0) {
			menu_edit_delete.setEnabled(true);
			menu_edit_cut.setEnabled(true);
			// menu_edit_copy.setEnabled(true);
			if (count > 1) menu_edit_group.setEnabled(true);
			else menu_edit_group.setEnabled(false);
		}
		else {
			menu_edit_delete.setEnabled(false);
			menu_edit_group.setEnabled(false);
			menu_edit_cut.setEnabled(false);
			// IMPORTANT: Copy must be enabled even if no entity is selected to guarantee the export of the full diagram to the system clipboard.
			// menu_edit_copy.setEnabled(false);
		}
	}

	@Override
	public void enablePaste() {
		menu_edit_paste.setEnabled(true);
	}

	@Override
	public void setUngroupEnabled(boolean enabled) {
		menu_edit_ungroup.setEnabled(enabled);
	}

	@Override
	public void setCustomPanelEnabled(boolean enable) {
		this.custom_panel_visible = enable;
		if (this.customPanel.isVisible() != enable) {
			int loc = this.mainSplit.getDividerLocation();
			if (enable) {
				int rightloc = this.rightSplit.getDividerLocation();
				this.customSplit.setDividerLocation(rightloc);
				this.customSplit.setDividerSize(2);
				this.mainSplit.setRightComponent(this.palettepanel);
				this.customPanel.getRightSplit().setDividerLocation(loc);
				this.customPanel.getRightSplit().updateUI();
				this.customPanel.getLeftSplit().setLeftComponent(this.propertyTextPane.getPanel());
				this.customPanel.getLeftSplit().setDividerLocation(this.diagramspanel.getWidth() / 2);
				this.customPanel.getLeftSplit().updateUI();
			}
			else {
				int rightloc = this.customSplit.getDividerLocation();
				this.customSplit.setDividerSize(0);
				this.mainSplit.setRightComponent(this.rightSplit);
				this.rightSplit.setLeftComponent(this.palettepanel);
				this.rightSplit.setRightComponent(this.propertyTextPane.getPanel());
				this.rightSplit.setDividerLocation(rightloc);
				this.rightSplit.updateUI();
				this.customPanel.getLeftSplit().setDividerLocation(this.diagramspanel.getWidth() / 2);
			}

			this.mainSplit.setDividerLocation(loc);
			this.menu_custom_edit.setEnabled(!enable && this.custom_element_selected);
			this.menu_custom_new.setEnabled(!enable);
			this.menu_custom_template.setEnabled(!enable);
			this.customPanel.setVisible(enable);

		}
		this.setDiagramsEnabled(!enable);
	}

	@Override
	public void setMailPanelEnabled(boolean enable) {
		if (enable) {
			mailSplit.setDividerSize(2);
			mailButton.setBackground(Constants.GRID_COLOR);
		}
		if (!enable) {
			mailSplit.setDividerSize(0);
			mailButton.setBackground(null);
		}
		mailPanel.setVisible(enable);
		if (enable) mailSplit.setDividerLocation(Constants.mail_split_position);
		mail_panel_visible = enable;
	}

	@Override
	public boolean isMailPanelVisible() {
		return mail_panel_visible;
	}

	@Override
	public void setCustomElementSelected(boolean selected) {
		this.custom_element_selected = selected;
		this.menu_custom_edit.setEnabled(selected && !this.custom_panel_visible);
	}

	@Override
	public void diagramSelected(DiagramHandler handler) {
		if (handler == null) {
			menu_file_save.setEnabled(false);
			menu_file_save_as.setEnabled(false);
			menu_file_export_as_bmp.setEnabled(false);
			menu_file_export_as_eps.setEnabled(false);
			menu_file_export_as_gif.setEnabled(false);
			menu_file_export_as_jpg.setEnabled(false);
			menu_file_export_as_pdf.setEnabled(false);
			menu_file_export_as_png.setEnabled(false);
			menu_file_export_as_svg.setEnabled(false);
			menu_edit.setEnabled(false);
		}
		else if (handler instanceof CustomPreviewHandler) {
			menu_edit.setEnabled(false); // do not enable delete / cut commands
		}
		else {
			menu_file_save.setEnabled(true);
			menu_file_save_as.setEnabled(true);
			menu_file_export_as_bmp.setEnabled(true);
			menu_file_export_as_eps.setEnabled(true);
			menu_file_export_as_gif.setEnabled(true);
			menu_file_export_as_jpg.setEnabled(true);
			menu_file_export_as_pdf.setEnabled(true);
			menu_file_export_as_png.setEnabled(true);
			menu_file_export_as_svg.setEnabled(true);
			menu_edit.setEnabled(true);
		}
	}

	@Override
	public void enableSearch(boolean enable) {
		this.searchField.requestFocus();
	}

	private void setDiagramsEnabled(boolean enable) {
		this.palettepanel.setEnabled(enable);
		for (Component c : this.palettepanel.getComponents())
			c.setEnabled(enable);
		this.diagramtabs.setEnabled(enable);
		for (Component c : this.diagramtabs.getComponents())
			c.setEnabled(enable);
		for (int i = 0; i < this.diagramtabs.getTabCount(); i++)
			this.diagramtabs.getTabComponentAt(i).setEnabled(enable);
		this.menu_palettes.setEnabled(enable);
		this.menu_file.setEnabled(enable);
		this.searchField.setEnabled(enable);
	}

	@Override
	public int getMainSplitPosition() {
		return this.mainSplit.getDividerLocation();
	}

	@Override
	public int getRightSplitPosition() {
		return this.rightSplit.getDividerLocation();
	}

	@Override
	public int getMailSplitPosition() {
		return this.mailSplit.getDividerLocation();
	}

	@Override
	public JFrame getTopContainer() {
		return this.window;
	}

	@Override
	public CustomElementHandler getCurrentCustomHandler() {
		return this.customelementhandler;
	}

	@Override
	public String getPropertyPanelText() {
		if (this.propertyTextPane != null) return this.propertyTextPane.getText();
		else return "";
	}

	@Override
	public void setPropertyPanelText(String text) {
		if (this.propertyTextPane != null) { // needed because of convert function
			this.propertyTextPane.setText(text);
			// Reset the vertical and horizontal scrollbar position to the upper left corner
			this.propertyTextPane.setCaretPosition(0);
		}
	}

	@Override
	public String chooseFileName() {
		String fileName = null;
		int returnVal = DiagramFileHandler.getOpenFileChooser().showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			fileName = DiagramFileHandler.getOpenFileChooser().getSelectedFile().getAbsolutePath();
		}
		return fileName;
	}

	@Override
	public void openDialog(String title, JComponent component) {
		JDialog dialog = (new JOptionPane(component, JOptionPane.PLAIN_MESSAGE)).createDialog(title);
		dialog.setVisible(true);
	}

	public String getValueOfZoomDisplay() {
		return (String) zoomComboBox.getSelectedItem();
	}

	public void setValueOfZoomDisplay(int i) {
		// This method should just set the value without ActionEvent therefore we remove the listener temporarily
		if (zoomComboBox != null) {
			zoomComboBox.removeActionListener(zoomListener);
			zoomComboBox.setSelectedIndex(i - 1);
			zoomComboBox.addActionListener(zoomListener);
		}
	}
}
