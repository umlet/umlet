package com.baselet.gui;

import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.JTextField;
import javax.swing.UIManager.LookAndFeelInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.Main;
import com.baselet.control.config.Config;
import com.baselet.control.config.SharedConfig;
import com.baselet.control.constants.Constants;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.diagram.DiagramHandler;

@SuppressWarnings("serial")
public class OptionPanel extends JPanel implements ActionListener {

	private final Logger log = LoggerFactory.getLogger(OptionPanel.class);
	private static OptionPanel optionpanel;

	public static OptionPanel getInstance() {
		if (optionpanel == null) {
			optionpanel = new OptionPanel();
		}
		return optionpanel;
	}

	private final JFrame optionframe;
	private final JCheckBox show_stickingpolygon = new JCheckBox();
	private final JCheckBox show_grid = new JCheckBox();
	private final JCheckBox enable_custom_elements = new JCheckBox();
	private final JCheckBox checkForUpdates = new JCheckBox();
	private final JCheckBox developerMode = new JCheckBox();
	private final JTextField pdfFont = new HintTextField("Path to font e.g.; c:/windows/fonts/msgothic.ttc,1");
	private final JTextField pdfFontBold = new HintTextField("same as above but used for bold text");
	private final JTextField pdfFontItalic = new HintTextField("same as above but used for italic text");
	private final JTextField pdfFontBoldItalic = new HintTextField("same as above but used for bold+italic text");
	private final JComboBox ui_manager;
	private final JComboBox default_fontsize = new JComboBox(new Integer[] { 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 });
	private final JComboBox propertiesPanelFontsize = new JComboBox(new Integer[] { 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 });
	private final JComboBox default_fontfamily = new JComboBox(Constants.fontFamilyList.toArray(new String[Constants.fontFamilyList.size()]));

	private final Vector<String> uis_technicalNames = new Vector<String>();

	private OptionPanel() {
		setLayout(new GridLayout(0, 2, 4, 4));
		setAlignmentX(Component.LEFT_ALIGNMENT);

		Vector<String> uis_humanReadableNameVector = new Vector<String>();
		LookAndFeelInfo[] lookAndFeelInfoArray = Constants.lookAndFeels.toArray(new LookAndFeelInfo[Constants.lookAndFeels.size()]);
		for (LookAndFeelInfo info : lookAndFeelInfoArray) {
			uis_technicalNames.add(info.getClassName());
			uis_humanReadableNameVector.add(info.getName());
		}
		ui_manager = new JComboBox(uis_humanReadableNameVector);

		this.add(new JLabel("Show sticking polygon"));
		this.add(show_stickingpolygon);
		this.add(new JLabel("Show grid"));
		this.add(show_grid);
		this.add(new JLabel("Enable Custom Elements"));
		this.add(enable_custom_elements);
		this.add(new JLabel("Check for " + Program.getInstance().getProgramName() + " updates"));
		this.add(checkForUpdates);
		if (Program.getInstance().getRuntimeType() == RuntimeType.STANDALONE) {
			this.add(new JLabel(Program.getInstance().getProgramName() + " style"));
			this.add(ui_manager);
		}
		this.add(new JLabel("Default fontsize"));
		this.add(default_fontsize);
		this.add(new JLabel("Properties panel fontsize (requires restart)"));
		this.add(propertiesPanelFontsize);
		this.add(new JLabel("Default fontfamily"));
		this.add(default_fontfamily);
		this.add(new JLabel("Developer Mode (show extended Element Info)"));
		this.add(developerMode);
		this.add(new JLabel("Optional font to embedd in PDF - normal text"));
		this.add(pdfFont);
		this.add(new JLabel("Optional font to embedd in PDF - bold text"));
		this.add(pdfFontBold);
		this.add(new JLabel("Optional font to embedd in PDF - italic text"));
		this.add(pdfFontItalic);
		this.add(new JLabel("Optional font to embedd in PDF - bold+italic"));
		this.add(pdfFontBoldItalic);

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

		optionframe = new JFrame(Program.getInstance().getProgramName() + " Options");
		optionframe.setContentPane(parent);
		optionframe.pack(); // autoresize of the optionframe
	}

	public void showOptionPanel() {
		show_stickingpolygon.setSelected(SharedConfig.getInstance().isShow_stickingpolygon());
		show_grid.setSelected(Config.getInstance().isShow_grid());
		enable_custom_elements.setSelected(Config.getInstance().isEnable_custom_elements());
		checkForUpdates.setSelected(Config.getInstance().isCheckForUpdates());
		developerMode.setSelected(SharedConfig.getInstance().isDev_mode());
		ui_manager.setSelectedIndex(uis_technicalNames.indexOf(Config.getInstance().getUiManager()));
		default_fontsize.setSelectedItem(Config.getInstance().getDefaultFontsize());
		propertiesPanelFontsize.setSelectedItem(Config.getInstance().getPropertiesPanelFontsize());
		default_fontfamily.setSelectedItem(Config.getInstance().getDefaultFontFamily());
		pdfFont.setText(Config.getInstance().getPdfExportFont());
		pdfFontBold.setText(Config.getInstance().getPdfExportFontBold());
		pdfFontItalic.setText(Config.getInstance().getPdfExportFontItalic());
		pdfFontBoldItalic.setText(Config.getInstance().getPdfExportFontBoldItalic());
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				optionframe.setLocationRelativeTo(CurrentGui.getInstance().getGui().getMainFrame());
				optionframe.setVisible(true);
				optionframe.toFront();
			}
		});
	}

	private void hideOptionPanel() {
		optionframe.setVisible(false);
	}

	// ok or cancel button pressed
	@Override
	public void actionPerformed(ActionEvent ae) {
		hideOptionPanel();

		if (ae.getActionCommand().equals("Ok")) {
			SharedConfig.getInstance().setShow_stickingpolygon(show_stickingpolygon.isSelected());
			Config.getInstance().setShow_grid(show_grid.isSelected());
			Config.getInstance().setEnable_custom_elements(enable_custom_elements.isSelected());
			Config.getInstance().setCheckForUpdates(checkForUpdates.isSelected());
			SharedConfig.getInstance().setDev_mode(developerMode.isSelected());
			Config.getInstance().setDefaultFontsize((Integer) default_fontsize.getSelectedItem());
			Config.getInstance().setPdfExportFont(pdfFont.getText());
			Config.getInstance().setPdfExportFontBold(pdfFontBold.getText());
			Config.getInstance().setPdfExportFontItalic(pdfFontItalic.getText());
			Config.getInstance().setPdfExportFontBoldItalic(pdfFontBoldItalic.getText());

			String newui = uis_technicalNames.get(ui_manager.getSelectedIndex());
			// only set look and feel if it has changed, because it messes up frame-size
			if (newui != null && !newui.equals(Config.getInstance().getUiManager())) {
				Config.getInstance().setUiManager(newui);
				CurrentGui.getInstance().getGui().setLookAndFeel(newui, optionframe);
			}

			// redraw every element to apply changes (like show stickingpolygon, fontsize, ...)
			for (DiagramHandler d : Main.getInstance().getDiagramsAndPalettes()) {
				d.getFontHandler().resetFontSize();
				d.getDrawPanel().updateElements();
				d.getDrawPanel().repaint();
			}
			Config.getInstance().setPropertiesPanelFontsize((Integer) propertiesPanelFontsize.getSelectedItem());

			String newfamily = (String) default_fontfamily.getSelectedItem();
			Config.getInstance().setDefaultFontFamily(newfamily);
		}
	}
}
