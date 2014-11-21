package com.baselet.gui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.baselet.control.Main;
import com.baselet.control.config.Config;
import com.baselet.control.constants.Constants;
import com.baselet.control.enums.Program;
import com.baselet.custom.CustomElementPanel;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.PaletteHandler;
import com.baselet.element.old.custom.CustomElementHandler;
import com.baselet.gui.listener.DividerListener;
import com.baselet.gui.listener.PaletteComboBoxListener;
import com.baselet.gui.listener.PropertyPanelListener;
import com.baselet.gui.pane.OwnSyntaxPane;

public abstract class BaseGUIBuilder {

	private JPanel palettePanel;
	private CardLayout palettePanelLayout;
	private JSplitPane rightSplit;
	private JComboBox paletteList;

	private CustomElementHandler customHandler;
	private JSplitPane mainSplit;
	private MailPanel mailPanel;
	private JSplitPane customSplit;
	private JSplitPane mailSplit;
	private JPanel rightPanel;
	private OwnSyntaxPane propertyTextPane;

	protected JSplitPane initBase(Component mainComponent, final int mainDividerLoc) {
		propertyTextPane = createPropertyTextPane(); // must be initialized before palettePanel because it could be accessed during palette initialization (eg in case of different default fontsize)
		palettePanel = newPalettePanel();
		rightSplit = newGenericSplitPane(JSplitPane.VERTICAL_SPLIT, palettePanel, propertyTextPane.getPanel(), 2, Config.getInstance().getRight_split_position(), true);
		rightPanel = newRightPanel();

		mainSplit = newGenericSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainComponent, rightPanel, 2, mainDividerLoc, true);
		// hide mainSplit on doubleclick
		((BasicSplitPaneUI) mainSplit.getUI()).getDivider().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					rightPanel.setVisible(!rightPanel.isVisible());
					mainSplit.setDividerLocation(mainDividerLoc);
				}
			}
		});
		customHandler = new CustomElementHandler();
		customHandler.getPanel().setVisible(false);
		customSplit = newGenericSplitPane(JSplitPane.VERTICAL_SPLIT, mainSplit, getCustomPanel(), 0, 0, true);
		mailPanel = new MailPanel();
		mailPanel.setVisible(false);
		mailSplit = newGenericSplitPane(JSplitPane.VERTICAL_SPLIT, mailPanel, customSplit, 0, 0, true);
		return mailSplit;
	}

	public JSplitPane getMailSplit() {
		return mailSplit;
	}

	public JSplitPane getCustomSplit() {
		return customSplit;
	}

	public MailPanel getMailPanel() {
		return mailPanel;
	}

	public JSplitPane getMainSplit() {
		return mainSplit;
	}

	public JPanel getPalettePanel() {
		return palettePanel;
	}

	public JComboBox getPaletteList() {
		return paletteList;
	}

	public JSplitPane getRightSplit() {
		return rightSplit;
	}

	public CustomElementHandler getCustomHandler() {
		return customHandler;
	}

	public CustomElementPanel getCustomPanel() {
		return customHandler.getPanel();
	}

	public JPanel newPalettePanel() {
		palettePanelLayout = new CardLayout();
		JPanel palettePanel = new JPanel(palettePanelLayout);
		palettePanel.addComponentListener(new DividerListener()); // Adding the DividerListener which refreshes Scrollbars here is enough for all dividers
		for (PaletteHandler palette : Main.getInstance().getPalettes().values()) {
			palettePanel.add(palette.getDrawPanel().getScrollPane(), palette.getName());
		}
		return palettePanel;
	}

	public JSplitPane newGenericSplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent, int dividerSize, int dividerLocation, boolean visible) {
		JSplitPane pane = new JSplitPane(newOrientation, newLeftComponent, newRightComponent);
		pane.setDividerSize(dividerSize);
		pane.setDividerLocation(dividerLocation);
		pane.setResizeWeight(1);
		pane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
		pane.setAlignmentX(Component.RIGHT_ALIGNMENT);
		pane.setVisible(visible);
		return pane;
	}

	private JPanel newRightPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		rightSplit.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(newPaletteControlsPanel());
		panel.add(rightSplit);
		return panel;
	}

	private JPanel newPaletteControlsPanel() {
		createPaletteList();

		JPanel paletteControlsPanel = new JPanel();
		paletteControlsPanel.setLayout(new BoxLayout(paletteControlsPanel, BoxLayout.X_AXIS));
		paletteControlsPanel.add(paletteList);
		return paletteControlsPanel;
	}

	public void createPaletteList() {
		paletteList = new JComboBox();
		paletteList.setMaximumRowCount(15);
		paletteList.setAlignmentX(Component.CENTER_ALIGNMENT);
		for (PaletteHandler palette : Main.getInstance().getPalettes().values()) {
			paletteList.addItem(palette.getName());
		}
		PaletteComboBoxListener pl = new PaletteComboBoxListener();
		paletteList.addActionListener(pl); // add listeners after adding every paletteList entry to avoid triggering the listener everytime
		paletteList.addMouseWheelListener(pl);

		// only set last used palette if the program version has not changed and if lastUsedPalette is not null (if the version is not equal or the lastUsedPalette String is invalid it will simply stay at index 0; null must not be set because it invalidates the selectionstate instead of setting it to 0; see Issue 215)
		String lastUsedPalette = Config.getInstance().getLastUsedPalette();
		if (Program.getInstance().getVersion().equals(Config.getInstance().getProgramVersion()) && lastUsedPalette != null) {
			paletteList.setSelectedItem(lastUsedPalette);
		}
	}

	private OwnSyntaxPane createPropertyTextPane() {
		OwnSyntaxPane propertyTextPane = new OwnSyntaxPane();
		PropertyPanelListener pListener = new PropertyPanelListener();
		propertyTextPane.getTextComponent().addKeyListener(pListener);
		propertyTextPane.getTextComponent().getDocument().addDocumentListener(pListener);
		return propertyTextPane;
	}

	public OwnSyntaxPane getPropertyTextPane() {
		return propertyTextPane;
	}

	public void setMailPanelEnabled(boolean enable) {
		getMailPanel().setVisible(enable);
		if (enable) {
			int mailDividerLoc = Math.max(Constants.MIN_MAIL_SPLITPANEL_SIZE, Config.getInstance().getMail_split_position());
			mailSplit.setDividerLocation(mailDividerLoc);
			mailSplit.setDividerSize(2);
		}
		else {
			mailSplit.setDividerSize(0);
		}
	}

	public void setCustomPanelEnabled(boolean enable) {
		CustomElementPanel customPanel = getCustomPanel();
		if (customPanel.isVisible() != enable) {
			int loc = getMainSplit().getDividerLocation();
			customPanel.setVisible(enable);

			if (enable) {
				int rightloc = getRightSplit().getDividerLocation();

				getCustomSplit().setDividerSize(2);
				getRightSplit().setDividerSize(0);

				customPanel.getLeftSplit().setLeftComponent(propertyTextPane.getPanel());
				getCustomSplit().setDividerLocation(rightloc);

				customPanel.getRightSplit().setDividerLocation(loc);
				customPanel.getLeftSplit().setDividerLocation(CurrentDiagram.getInstance().getDiagramHandler().getDrawPanel().getWidth() / 2);
				customPanel.getLeftSplit().updateUI();
			}
			else {
				int rightloc = getCustomSplit().getDividerLocation();
				getCustomSplit().setDividerSize(0);

				getRightSplit().setDividerSize(2);
				getRightSplit().setRightComponent(propertyTextPane.getPanel());
				getRightSplit().setDividerLocation(rightloc);
			}
			getMainSplit().setDividerLocation(loc);
		}
	}

	public void setPaletteActive(String paletteName) {
		palettePanelLayout.show(palettePanel, paletteName);
	}
}
