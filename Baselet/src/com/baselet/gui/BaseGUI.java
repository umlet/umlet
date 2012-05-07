package com.baselet.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BoxLayout;
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

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.ProgramName;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.CustomPreviewHandler;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.element.GridElement;
import com.baselet.element.Group;
import com.baselet.gui.listener.PropertyPanelListener;
import com.umlet.custom.CustomElement;
import com.umlet.custom.CustomElementHandler;


@SuppressWarnings("serial")
public abstract class BaseGUI extends JPanel {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	protected Main main;
	protected int selected_elements;
	protected OwnSyntaxPane propertyTextPane;
	protected boolean paletteEdited = false;

	public BaseGUI(Main main) {
		this.main = main;
	}

	public final void initGUI() throws Exception {
		try {
			UIManager.setLookAndFeel(Constants.ui_manager);
		} catch (Exception e) { // If the LookAndFeel cannot be set, it gets logged (without stacktrace) and the default style is used
			log.error(e.getMessage());
		}

		this.initGUIParameters();
		this.setLayout(new BorderLayout());

		this.init();
		this.requestFocus();
	}

	public OwnSyntaxPane createPropertyTextPane() {
		JPanel propertyTextPanel = new JPanel();
		propertyTextPane = new OwnSyntaxPane(propertyTextPanel);

		// add listener to propertyTextPane
		PropertyPanelListener pListener = new PropertyPanelListener(/*propertyTextPane*/);
		propertyTextPane.addKeyListener(pListener);
		propertyTextPane.addFocusListener(pListener);
		
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

		propertyTextPane.initJSyntaxPane();
		
		return propertyTextPane;
	}

	public MailPanel createMailPanel() {
		return new MailPanel();
	}

	public void focusPropertyPane() {
		propertyTextPane.requestFocus();
		// The robot class simulates pressing a certain key
		// try {
		// new Robot().keyPress(character);
		// } catch (AWTException e) {
		// log.error(null, e);
		// }
	}

	public JPopupMenu getContextMenu(Component comp) {
		MenuFactorySwing menuFactory = MenuFactorySwing.getInstance();

		GridElement entity = null;
		if (comp instanceof GridElement) entity = (GridElement) comp;
		else return null;

		JPopupMenu contextMenu = new JPopupMenu();
		if (entity instanceof CustomElement) {
			contextMenu.add(menuFactory.createEditSelected());
		}

		if (!(entity.getHandler() instanceof CustomPreviewHandler)) {
			contextMenu.add(menuFactory.createDelete());
		}
		JMenuItem group = menuFactory.createGroup();
		contextMenu.add(group);
		if (this.selected_elements < 2) group.setEnabled(false);

		JMenuItem ungroup = menuFactory.createUngroup();
		contextMenu.add(ungroup);
		if (!(entity instanceof Group)) ungroup.setEnabled(false);

		if (Program.PROGRAM_NAME == ProgramName.UMLET) {
			contextMenu.add(menuFactory.createSetColor(true));
			contextMenu.add(menuFactory.createSetColor(false));
		}

		// insert alignment menu
		JMenu alignmentMenu = menuFactory.createAlign();
		alignmentMenu.setEnabled(this.selected_elements > 1); // only enable when at least 2 elements are selected
		contextMenu.add(alignmentMenu);

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

	public void setPaletteEdited(boolean isEdited) {
		this.paletteEdited = isEdited;
	}

	public boolean getPaletteEdited() {
		return this.paletteEdited;
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

	public abstract void setPaste(boolean value);

	public abstract void setUngroupEnabled(boolean enabled);

	public abstract void setCustomElementSelected(boolean selected);

	public abstract void diagramSelected(DiagramHandler handler);

	public abstract void enableSearch(boolean enable);

	public abstract int getMainSplitPosition();

	public abstract int getMailSplitPosition();

	public abstract int getRightSplitPosition();

	public abstract JFrame getTopContainer();

	public abstract OwnSyntaxPane getPropertyPane();

	public abstract String getPropertyPanelText();

	public abstract void setPropertyPanelText(String text);

	public abstract String chooseFileName();

	public abstract void openDialog(String title, JComponent component);

	public abstract void setValueOfZoomDisplay(int i);
}
