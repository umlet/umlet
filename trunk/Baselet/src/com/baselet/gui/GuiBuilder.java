package com.baselet.gui;

import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.diagram.PaletteHandler;
import com.baselet.gui.listener.DividerListener;
import com.baselet.gui.listener.PaletteComboBoxListener;

public class GuiBuilder {

	private static JPanel palettePanel;
	private static JSplitPane rightSplit;
	private static JComboBox paletteList;
	
	public JPanel getPalettePanel() {
		return palettePanel;
	}
	
	public static JComboBox getPaletteList() {
		return paletteList;
	}
	
	public static JSplitPane getRightSplit() {
		return rightSplit;
	}
	
	public static JPanel newPalettePanel() {
		JPanel palettePanel = new JPanel(new CardLayout());
		palettePanel.addComponentListener(new DividerListener()); // Adding the DividerListener which refreshes Scrollbars here is enough for all dividers
		for (PaletteHandler palette : Main.getInstance().getPalettes().values()) {
			palettePanel.add(palette.getDrawPanel().getScrollPane(), palette.getName());
		}
		return palettePanel;
	}
	
	public static JSplitPane newSplitPane(Component newRightComponent) {
		palettePanel = newPalettePanel();
		
		return newGenericSplitPane(JSplitPane.VERTICAL_SPLIT, palettePanel, newRightComponent, 2, Constants.right_split_position, true);
	}

	public static JSplitPane newGenericSplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent, int dividerSize, int dividerLocation, boolean visible) {
		JSplitPane pane = new JSplitPane(newOrientation, newLeftComponent, newRightComponent);
		pane.setDividerSize(dividerSize);
		pane.setDividerLocation(dividerLocation);
		pane.setResizeWeight(1);
		pane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
		pane.setAlignmentX(Component.RIGHT_ALIGNMENT);
		pane.setVisible(visible);
		return pane;
	}

	public static JPanel newRightPanel(JPanel propTextPanel) {
		rightSplit = GuiBuilder.newSplitPane(propTextPanel);

		return newRightPanelX(newPaletteControlsPanel(), rightSplit);

	}

	private static JPanel newRightPanelX(JPanel paletteControl, JSplitPane splitPane) {
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightSplit.setAlignmentX(Component.CENTER_ALIGNMENT);
		rightPanel.add(paletteControl);
		rightPanel.add(splitPane);
		return rightPanel;
	}

	private static JPanel newPaletteControlsPanel() {
		GuiBuilder.createPaletteList();
		
		JPanel paletteControlsPanel = new JPanel();
		paletteControlsPanel.setLayout(new BoxLayout(paletteControlsPanel, BoxLayout.X_AXIS));
		paletteControlsPanel.add(paletteList);
		return paletteControlsPanel;
	}

	public static void createPaletteList() {
		paletteList = new JComboBox();
		paletteList.setMaximumRowCount(15);
		paletteList.setAlignmentX(Component.CENTER_ALIGNMENT);
		for (PaletteHandler palette : Main.getInstance().getPalettes().values()) {
			paletteList.addItem(palette.getName());
		}
		PaletteComboBoxListener pl = new PaletteComboBoxListener();
		paletteList.addActionListener(pl); // add listeners after adding every paletteList entry to avoid triggering the listener everytime
		paletteList.addMouseWheelListener(pl);
		paletteList.setSelectedItem(Constants.lastUsedPalette);
	}
}
