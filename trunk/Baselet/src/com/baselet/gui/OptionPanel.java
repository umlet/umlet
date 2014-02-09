package com.baselet.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.baselet.control.Config;
import com.baselet.control.Constants;
import com.baselet.control.SharedConstants.Program;
import com.baselet.control.Main;
import com.baselet.control.SharedConstants.RuntimeType;
import com.baselet.diagram.DiagramHandler;
import com.baselet.gui.standalone.StandaloneGUI;


@SuppressWarnings("serial")
public class OptionPanel extends JPanel implements ActionListener {

	public static OptionPanel optionpanel;

	public static OptionPanel getInstance() {
		if (optionpanel == null) optionpanel = new OptionPanel();
		return optionpanel;
	}

	private JFrame optionframe;
	private JCheckBox show_stickingpolygon;
	private JCheckBox show_grid;
	private JCheckBox enable_custom_elements;
	private JCheckBox checkForUpdates;
	private JComboBox ui_manager;
	private JComboBox default_fontsize;
	private JComboBox propertiesPanelFontsize;
	private JComboBox default_fontfamily;

	private Vector<String> uis_technicalNameVector;

	private OptionPanel() {
		this.setLayout(new GridLayout(0, 2, 4, 4));
		this.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.show_stickingpolygon = new JCheckBox();
		this.show_grid = new JCheckBox();
		this.enable_custom_elements = new JCheckBox();
		this.checkForUpdates = new JCheckBox();
		uis_technicalNameVector = new Vector<String>();
		Vector<String> uis_humanReadableNameVector = new Vector<String>();
		LookAndFeelInfo[] lookAndFeelInfoArray = Constants.lookAndFeels.toArray(new LookAndFeelInfo[Constants.lookAndFeels.size()]);
		for (LookAndFeelInfo info : lookAndFeelInfoArray) {
			uis_technicalNameVector.add(info.getClassName());
			uis_humanReadableNameVector.add(info.getName());
		}
		this.ui_manager = new JComboBox(uis_humanReadableNameVector);
		this.default_fontsize = new JComboBox(new Integer[] {9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20});
		this.propertiesPanelFontsize = new JComboBox(new Integer[] {9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20});
		this.default_fontfamily = new JComboBox(Constants.fontFamilyList.toArray(new String[Constants.fontFamilyList.size()]));

		this.add(new JLabel("Show sticking ploygon"));
		this.add(this.show_stickingpolygon);
		this.add(new JLabel("Show grid"));
		this.add(this.show_grid);
		this.add(new JLabel("Enable Custom Elements"));
		this.add(this.enable_custom_elements);
		this.add(new JLabel("Check for " + Program.NAME + " updates"));
		this.add(this.checkForUpdates);
		if (Program.RUNTIME_TYPE == RuntimeType.STANDALONE) {
			this.add(new JLabel(Program.NAME + " style"));
			this.add(this.ui_manager);
		}
		this.add(new JLabel("Default fontsize"));
		this.add(this.default_fontsize);
		this.add(new JLabel("Properties panel fontsize (requires restart)"));
		this.add(this.propertiesPanelFontsize);
		this.add(new JLabel("Default fontfamily"));
		this.add(this.default_fontfamily);

		JButton button_ok = new JButton("Ok");
		button_ok.setActionCommand("Ok");
		button_ok.addActionListener(this);
		JButton button_cancel = new JButton("Cancel");
		button_cancel.setActionCommand("Cancel");
		button_cancel.addActionListener(this);

		JPanel button_panel = new JPanel();
		button_panel.setLayout(new BoxLayout(button_panel, BoxLayout.X_AXIS));
		button_panel.add(Box.createHorizontalGlue());
		button_panel.add(button_cancel);
		button_panel.add(Box.createRigidArea(new Dimension(20, 0)));
		button_panel.add(button_ok);
		button_panel.add(Box.createHorizontalGlue());
		button_panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel parent = new JPanel();
		parent.setLayout(new BoxLayout(parent, BoxLayout.Y_AXIS));
		parent.add(Box.createRigidArea(new Dimension(10, 10)));
		parent.add(this);
		parent.add(Box.createRigidArea(new Dimension(0, 20)));
		parent.add(button_panel);
		parent.add(Box.createRigidArea(new Dimension(0, 20)));

		this.optionframe = new JFrame(Program.NAME + " Options");
		this.optionframe.setContentPane(parent);
		this.optionframe.pack(); //autoresize of the optionframe
	}

	public void showOptionPanel() {
		this.show_stickingpolygon.setSelected(Constants.show_stickingpolygon);
		this.show_grid.setSelected(Constants.show_grid);
		this.enable_custom_elements.setSelected(Constants.enable_custom_elements);
		this.checkForUpdates.setSelected(Constants.checkForUpdates);
		this.ui_manager.setSelectedIndex(uis_technicalNameVector.indexOf(Config.getInstance().getUiManager()));
		this.default_fontsize.setSelectedItem(Constants.defaultFontsize);
		this.propertiesPanelFontsize.setSelectedItem(Constants.propertiesPanelFontsize);
		this.default_fontfamily.setSelectedItem(Constants.defaultFontFamily);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				optionframe.setLocationRelativeTo(Main.getInstance().getGUI().getMainFrame());
				optionframe.setVisible(true);
				optionframe.toFront();
			}
		});
	}

	private void hideOptionPanel() {
		this.optionframe.setVisible(false);
	}

	// ok or cancel button pressed
	@Override
	public void actionPerformed(ActionEvent ae) {
		this.hideOptionPanel();

		if (ae.getActionCommand().equals("Ok")) {
			Constants.show_stickingpolygon = this.show_stickingpolygon.isSelected();
			Constants.show_grid = this.show_grid.isSelected();
			Constants.enable_custom_elements = this.enable_custom_elements.isSelected();
			Constants.checkForUpdates = this.checkForUpdates.isSelected();
			String newui = this.uis_technicalNameVector.get(this.ui_manager.getSelectedIndex());
			if (newui != null) {
				Config.getInstance().setUiManager(newui);
				try {
					BaseGUI gui = Main.getInstance().getGUI();
					if (gui instanceof StandaloneGUI) {
						Frame topFrame = ((StandaloneGUI) gui).getMainFrame();
						UIManager.setLookAndFeel(newui);
						SwingUtilities.updateComponentTreeUI(topFrame);
						SwingUtilities.updateComponentTreeUI(this.optionframe);
						topFrame.pack();
						this.optionframe.pack();
					}

				} catch (Exception ex) {}

			}

			Integer newsize = (Integer) this.default_fontsize.getSelectedItem();
			if (!Constants.defaultFontsize.equals(newsize)) {
				Constants.defaultFontsize = newsize;
				for (DiagramHandler d : Main.getInstance().getDiagramsAndPalettes()) {
					d.getFontHandler().resetFontSize();
					d.getDrawPanel().updateElements();
					d.getDrawPanel().repaint();
				}
			}
			Constants.propertiesPanelFontsize = (Integer) propertiesPanelFontsize.getSelectedItem();

			String newfamily = (String) this.default_fontfamily.getSelectedItem();
			Constants.defaultFontFamily = newfamily;
		}
	}
}
