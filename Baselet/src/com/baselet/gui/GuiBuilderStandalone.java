package com.baselet.gui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.baselet.control.Constants;
import com.baselet.gui.standalone.SearchListener;
import com.baselet.gui.standalone.ZoomListener;

public class GuiBuilderStandalone extends GuiBuilder {

	private JComboBox zoomComboBox;
	private ZoomListener zoomListener;
	
	private JTextField searchField;
	private JPanel zoomPanel;
	private JPanel searchPanel;

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
}
