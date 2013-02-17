package com.baselet.gui.standalone;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.baselet.control.Constants;
import com.baselet.gui.BaseGUIBuilder;

public class StandaloneGUIBuilder extends BaseGUIBuilder {

	private JComboBox zoomComboBox;
	private ZoomListener zoomListener;
	
	private JTextField searchField;
	private JPanel zoomPanel;
	private JPanel searchPanel;
	private JTabbedPane diagramtabs;
	
	public JTabbedPane getDiagramtabs() {
		return diagramtabs;
	}

	public JComboBox getZoomComboBox() {
		return zoomComboBox;
	}
	
	public ZoomListener getZoomListener() {
		return zoomListener;
	}

	public JTextField getSearchField() {
		return searchField;
	}
	
	private void createZoomComboBox() {
		zoomComboBox = new JComboBox();
		zoomComboBox.setPreferredSize(new Dimension(80, 24));
		zoomComboBox.setMinimumSize(zoomComboBox.getPreferredSize());
		zoomComboBox.setMaximumSize(zoomComboBox.getPreferredSize());
		zoomListener = new ZoomListener();
		zoomComboBox.addActionListener(zoomListener);
		zoomComboBox.addMouseWheelListener(zoomListener);
		zoomComboBox.setToolTipText("Use ± or mouse wheel to zoom");

		String[] zoomValues = Constants.zoomValueList.toArray(new String[Constants.zoomValueList.size()]);
		zoomComboBox.setModel(new DefaultComboBoxModel(zoomValues));
		zoomComboBox.setSelectedIndex(9);
	}

	public JPanel createZoomPanel() {
		createZoomComboBox();
		
		JPanel zoomPanel = new JPanel();
		zoomPanel.setOpaque(false);
		zoomPanel.setLayout(new BoxLayout(zoomPanel, BoxLayout.X_AXIS));
		zoomPanel.add(new JLabel("Zoom:   "));
		zoomPanel.add(zoomComboBox);
		zoomPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		return zoomPanel;
	}
	
	private void createSearchField() {
		searchField = new JTextField(10);
		searchField.setMinimumSize(searchField.getPreferredSize());
		searchField.setMaximumSize(searchField.getPreferredSize());
		searchField.addKeyListener(new SearchListener());
	}

	public JPanel createSearchPanel() {
		createSearchField();
		
		JPanel searchPanel = new JPanel();
		searchPanel.setOpaque(false);
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
		searchPanel.add(Box.createRigidArea(new Dimension(50, 0)));
		searchPanel.add(new JLabel("Search:   "));
		searchPanel.add(searchField);
		searchPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		return searchPanel;
	}
	
	public JPanel createDiagramTabPanel() {
		JPanel diagramspanel = new JPanel();
		@SuppressWarnings("unused")
		FileDrop drop = new FileDrop(diagramspanel, new FileDropListener()); // enable drag&drop from desktop into diagrampanel

		diagramtabs = new JTabbedPane();
		diagramtabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		diagramspanel.setLayout(new GridLayout(1, 1));
		diagramspanel.add(diagramtabs);

		return diagramspanel;
	}
}
