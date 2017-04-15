package com.baselet.standalone.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;

import com.baselet.control.config.Config;
import com.baselet.control.constants.Constants;
import com.baselet.control.enums.Program;
import com.baselet.gui.BaseGUIBuilder;
import com.baselet.gui.filedrop.FileDrop;
import com.baselet.gui.filedrop.FileDropListener;
import com.baselet.gui.listener.GUIListener;
import com.baselet.gui.listener.UmletWindowFocusListener;

public class StandaloneGUIBuilder extends BaseGUIBuilder {

	private JComboBox zoomComboBox;
	private ZoomListener zoomListener;

	private JTextField searchField;
	private JTabbedPane diagramtabs;
	private JToggleButton mailButton;

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

	public JFrame initSwingGui(MenuBuilder menuBuilder) {
		JFrame mainFrame = new JFrame();
		mainFrame.addWindowFocusListener(new UmletWindowFocusListener());
		mainFrame.addKeyListener(new GUIListener());
		mainFrame.addKeyListener(new SearchKeyListener());
		mainFrame.addWindowListener(new SwingWindowListener());
		mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // closing is handled in the StandaloneGUI.closeWindow() callback (fix for issue #250)
		mainFrame.setBounds(Config.getInstance().getProgram_location().x, Config.getInstance().getProgram_location().y, Config.getInstance().getProgram_size().width, Config.getInstance().getProgram_size().height);
		mainFrame.setTitle(Program.getInstance().getProgramName() + " - Free UML Tool for Fast UML Diagrams");

		setImage(mainFrame);

		if (Config.getInstance().isStart_maximized()) {
			// If Main starts maximized we set fixed bounds and must set the frame visible
			// now to avoid a bug where the right sidebar doesn't have the correct size
			mainFrame.setExtendedState(mainFrame.getExtendedState() | Frame.MAXIMIZED_BOTH);
			mainFrame.setVisible(true);
		}

		mainFrame.setJMenuBar(menuBuilder.createMenu(createSearchPanel(), createZoomPanel(), createMailButton()));

		JPanel diagramTabPanel = createDiagramTabPanel();
		int mainDividerLoc = Math.min(mainFrame.getSize().width - Constants.MIN_MAIN_SPLITPANEL_SIZE, Config.getInstance().getMain_split_position());
		JSplitPane baseSplitPane = initBase(diagramTabPanel, mainDividerLoc);
		mainFrame.add(baseSplitPane);

		ToolTipManager.sharedInstance().setInitialDelay(100);
		mainFrame.setVisible(true);

		return mainFrame;
	}

	/**
	 * set several image sizes of umlet_logo*.png
	 */
	private void setImage(JFrame mainFrame) {
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		for (Integer i : new int[] { 16, 20, 24, 32, 40, 48, 64 }) {
			InputStream is = null;
			try {
				is = this.getClass().getClassLoader().getResourceAsStream(Program.getInstance().getProgramName().toLowerCase() + "_logo" + i + ".png");
				images.add(ImageIO.read(is));
			} catch (IOException e) {
				throw new RuntimeException("Cannot read image", e);
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {}
				}
			}
		}
		mainFrame.setIconImages(images);
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

	@SuppressWarnings("unused")
	public JPanel createDiagramTabPanel() {
		JPanel diagramspanel = new JPanel();
		new FileDrop(diagramspanel, new FileDropListener()); // enable drag&drop from desktop into diagrampanel

		diagramtabs = new JTabbedPane();
		diagramtabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		diagramspanel.setLayout(new GridLayout(1, 1));
		diagramspanel.add(diagramtabs);

		return diagramspanel;
	}

	public JToggleButton createMailButton() {
		mailButton = new JToggleButton("Mail diagram");
		mailButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setMailPanelEnabled(!getMailPanel().isVisible());
			}
		});
		return mailButton;
	}

	@Override
	public void setMailPanelEnabled(boolean enable) {
		super.setMailPanelEnabled(enable);
		mailButton.setSelected(enable);
	}
}
