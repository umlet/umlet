package com.baselet.gui.base;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
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

import com.baselet.control.Constants;
import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.RuntimeType;
import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;


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
	private JComboBox ui_manager;
	private JComboBox default_fontsize;

	private Vector<String> uis;

	private OptionPanel() {
		this.setLayout(new GridLayout(0, 2, 4, 4));
		this.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.show_stickingpolygon = new JCheckBox();
		this.show_grid = new JCheckBox();
		uis = new Vector<String>();
		Vector<String> uis_shown = new Vector<String>();
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

			// The Eclipse Plugin doesn't work with GTKLookAndFeel, therefore we remove it from the choosable options
			if ((Program.RUNTIME_TYPE == RuntimeType.ECLIPSE_PLUGIN) &&
					info.getClassName().equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
				continue;
			}

			uis.add(info.getClassName());
			uis_shown.add(info.getName());
		}
		this.ui_manager = new JComboBox(uis_shown);
		List<Integer> fontSizes = new ArrayList<Integer>();
		for (int i = Constants.FONT_MIN; i <= Constants.FONT_MAX; i++) {
			fontSizes.add(i);
		}
		this.default_fontsize = new JComboBox(fontSizes.toArray(new Integer[fontSizes.size()]));

		this.add(new JLabel("Show sticking ploygon"));
		this.add(this.show_stickingpolygon);
		this.add(new JLabel("Show grid"));
		this.add(this.show_grid);
		this.add(new JLabel("Select " + Program.PROGRAM_NAME + " style"));
		this.add(this.ui_manager);
		this.add(new JLabel("Select default fontsize"));
		this.add(this.default_fontsize);

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

		this.optionframe = new JFrame(Program.PROGRAM_NAME + " Options");
		this.optionframe.setContentPane(parent);
		this.optionframe.setLocation(50, 50);
		this.optionframe.setSize(300, 210);
	}

	public void showOptionPanel() {
		this.show_stickingpolygon.setSelected(Constants.show_stickingpolygon);
		this.show_grid.setSelected(Constants.show_grid);
		this.ui_manager.setSelectedIndex(uis.indexOf(Constants.ui_manager));
		this.default_fontsize.setSelectedItem(Constants.defaultFontsize);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
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
			String newui = this.uis.get(this.ui_manager.getSelectedIndex());
			if (!Constants.ui_manager.equals(newui) && (newui != null)) {
				Constants.ui_manager = newui;
				try {
					JFrame frame = Main.getInstance().getGUI().getTopContainer();
					if (frame != null) {
						UIManager.setLookAndFeel(newui);
						SwingUtilities.updateComponentTreeUI(frame);
						SwingUtilities.updateComponentTreeUI(this.optionframe);
						frame.pack();
						this.optionframe.pack();
					}

				} catch (Exception ex) {}

			}

			Integer newsize = (Integer) this.default_fontsize.getSelectedItem();
			if (Constants.defaultFontsize != newsize) {
				Constants.defaultFontsize = newsize;
				for (DiagramHandler d : Main.getInstance().getDiagrams()) {
					d.getFontHandler().resetFontSize();
				}
				for (DiagramHandler d : Main.getInstance().getPalettes().values()) {
					d.getFontHandler().resetFontSize();
				}
			}

			// After changing some options (eg: grid or fontsize) the GUI needs to be repainted
			Main.getInstance().getGUI().repaint();
		}
	}
}
