package com.umlet.gui.base;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Robot;

import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.plaf.InsetsUIResource;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.diagram.CustomPreviewHandler;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.control.diagram.PaletteHandler;
import com.umlet.custom.CustomElement;
import com.umlet.custom.CustomElementHandler;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Group;
import com.umlet.gui.base.listeners.GUIListener;
import com.umlet.gui.base.listeners.PropertyPanelListener;
import com.umlet.gui.standalone.MenuListener;

@SuppressWarnings("serial")
public abstract class UmletGUI extends JPanel {

	protected Umlet umlet;
	protected int selected_elements;
	UmletTextPane propertyTextPane;

	public UmletGUI(Umlet umlet) {
		this.umlet = umlet;
	}

	public final void initGUI() {
		try {
			UIManager.setLookAndFeel(Constants.ui_manager);
		} catch (Exception x) {}

		this.initGUIParameters();
		this.addKeyListener(new GUIListener());
		this.setLayout(new BorderLayout());

		this.init();
		this.requestFocus();
	}

	public UmletTextPane createPropertyTextPane() {
		JPanel propertyTextPanel = new JPanel();
		propertyTextPane = new UmletTextPane(propertyTextPanel);

		// add listener to propertyTextPane
		propertyTextPane.addKeyListener(new PropertyPanelListener(propertyTextPane));

		propertyTextPanel.setLayout(new BoxLayout(propertyTextPanel, BoxLayout.Y_AXIS));
		JScrollPane propertyTextScrollPane = new JScrollPane(propertyTextPane);
		propertyTextScrollPane.setBorder(null);
		propertyTextScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		propertyTextScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JLabel propertyLabel = new JLabel(" Properties");
		propertyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		propertyLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
		propertyTextPanel.add(propertyLabel);
		propertyTextPanel.add(propertyTextScrollPane);

		return propertyTextPane;
	}

	public MailPanel createMailPanel() {
		return new MailPanel();
	}

	public void focusPropertyPaneAndWriteCharacter(int character) {
		propertyTextPane.requestFocus();
		// The robot class simulates pressing a certain key
		try {
			new Robot().keyPress(character);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void revalidateAllPanels() {
		// updates the preferred size and rechecks if scrollbars are necessary for all palettes and the diagram
		for (PaletteHandler p : Umlet.getInstance().getPalettes().values()) {
			try {
				p.getDrawPanel().removeUnnecessaryWhitespaceAroundDiagram();
			} catch (NullPointerException e1) {}
		}
		try {
			Umlet.getInstance().getDiagramHandler().getDrawPanel().removeUnnecessaryWhitespaceAroundDiagram();
		} catch (NullPointerException e1) {}
	}

	private JMenuItem createColorMenuItem(String name, String colStr, Color color, boolean isForeground) {

		JCheckBoxMenuItem item = new JCheckBoxMenuItem(name);
		item.setActionCommand("color_" + (isForeground ? "fgc_" : "bgc_") + name);
		item.addActionListener(MenuListener.getInstance());
		item.setState((colStr != null) && colStr.equals(name));
		item.setIcon(new PlainColorIcon(color));
		return item;
	}

	public JPopupMenu getContextMenu(Component comp) {
		Entity entity = null;
		if (comp instanceof Entity) entity = (Entity) comp;
		else return null;

		JPopupMenu contextMenu = new JPopupMenu();
		if (entity instanceof CustomElement) {
			JMenuItem customedit = new JMenuItem("Edit Selected...");
			customedit.addActionListener(MenuListener.getInstance());
			contextMenu.add(customedit);
		}

		if (!(entity.getHandler() instanceof CustomPreviewHandler)) {
			JMenuItem delete = new JMenuItem("Delete");
			delete.addActionListener(MenuListener.getInstance());
			contextMenu.add(delete);
		}
		JMenuItem group = new JMenuItem("Group");
		group.addActionListener(MenuListener.getInstance());
		contextMenu.add(group);
		if (this.selected_elements < 2) group.setEnabled(false);
		JMenuItem ungroup = new JMenuItem("Ungroup");
		ungroup.addActionListener(MenuListener.getInstance());
		contextMenu.add(ungroup);
		if (!(entity instanceof Group)) ungroup.setEnabled(false);

		String fgcol = entity.getFGColorString();
		String bgcol = entity.getBGColorString();

		if (entity.supportsColors) {
			// foreground color menu items

			JMenu fg_color = new JMenu("Set foreground color");
			JCheckBoxMenuItem fgc_default = new JCheckBoxMenuItem("default");
			fgc_default.setActionCommand("color_fgc_default");
			fgc_default.addActionListener(MenuListener.getInstance());
			fgc_default.setState((fgcol == null) || fgcol.equals(""));
			fg_color.add(fgc_default);

			fg_color.add(new JPopupMenu.Separator());

			fg_color.add(createColorMenuItem("red", fgcol, Color.red, true));
			fg_color.add(createColorMenuItem("green", fgcol, Color.green, true));
			fg_color.add(createColorMenuItem("blue", fgcol, Color.blue, true));
			fg_color.add(createColorMenuItem("yellow", fgcol, Color.yellow, true));
			fg_color.add(createColorMenuItem("white", fgcol, Color.white, true));
			fg_color.add(createColorMenuItem("black", fgcol, Color.black, true));
			fg_color.add(createColorMenuItem("gray", fgcol, Color.gray, true));
			fg_color.add(createColorMenuItem("orange", fgcol, Color.orange, true));
			fg_color.add(createColorMenuItem("magenta", fgcol, Color.magenta, true));
			fg_color.add(createColorMenuItem("pink", fgcol, Color.pink, true));

			// background color menu items

			JMenu bg_color = new JMenu("Set background color");
			JCheckBoxMenuItem bgc_default = new JCheckBoxMenuItem("default");
			bgc_default.setActionCommand("color_bgc_default");
			bgc_default.addActionListener(MenuListener.getInstance());
			bgc_default.setState((bgcol == null) || bgcol.equals(""));
			bg_color.add(bgc_default);

			bg_color.add(new JPopupMenu.Separator());

			bg_color.add(createColorMenuItem("red", bgcol, Color.red, false));
			bg_color.add(createColorMenuItem("green", bgcol, Color.green, false));
			bg_color.add(createColorMenuItem("blue", bgcol, Color.blue, false));
			bg_color.add(createColorMenuItem("yellow", bgcol, Color.yellow, false));
			bg_color.add(createColorMenuItem("white", bgcol, Color.white, false));
			bg_color.add(createColorMenuItem("black", bgcol, Color.black, false));
			bg_color.add(createColorMenuItem("gray", bgcol, Color.gray, false));
			bg_color.add(createColorMenuItem("orange", bgcol, Color.orange, false));
			bg_color.add(createColorMenuItem("magenta", bgcol, Color.magenta, false));
			bg_color.add(createColorMenuItem("pink", bgcol, Color.pink, false));

			contextMenu.add(fg_color);
			contextMenu.add(bg_color);
		}

		return contextMenu;
	}

	public void elementsSelected(int count) {
		this.selected_elements = count;
	}

	protected void initGUIParameters() {
		UIManager.put("TabbedPane.selected", Color.white);
		UIManager.put("TabbedPane.tabInsets", new InsetsUIResource(0, 4, 1, 0));
		UIManager.put("TabbedPane.contentBorderInsets", new InsetsUIResource(0, 0, 0, 0));
	}

	public abstract CustomElementHandler getCurrentCustomHandler();

	public abstract void setCustomPanelEnabled(boolean enable);

	public abstract void setMailPanelEnabled(boolean enable);

	public abstract boolean isMailPanelVisible();

	public abstract void updateDiagramName(DiagramHandler diagram, String name);

	public abstract void setDiagramChanged(DiagramHandler diagram, boolean changed);

	public abstract void setCustomElementChanged(CustomElementHandler handler, boolean changed);

	public abstract void closeWindow();

	protected abstract void init();

	public abstract String getSelectedPalette();

	public abstract void selectPalette(String palette);

	public abstract void open(DiagramHandler diagram);

	public abstract void close(DiagramHandler diagram);

	public abstract DrawPanel getCurrentDiagram();

	public abstract void enablePaste();

	public abstract void setUngroupEnabled(boolean enabled);

	public abstract void setCustomElementSelected(boolean selected);

	public abstract void diagramSelected(DiagramHandler handler);

	public abstract void enableSearch(boolean enable);

	public abstract int getMainSplitPosition();

	public abstract int getMailSplitPosition();

	public abstract int getRightSplitPosition();

	public abstract JFrame getTopContainer();

	public abstract String getPropertyPanelText();

	public abstract void setPropertyPanelText(String text);

	public abstract String chooseFileName();

	public abstract void openDialog(String title, JComponent component);

}
