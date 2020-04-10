package com.baselet.gui.menu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.baselet.control.config.ConfigClassGen;
import com.baselet.control.enums.generator.FieldOptions;
import com.baselet.control.enums.generator.MethodOptions;
import com.baselet.control.enums.generator.SignatureOptions;
import com.baselet.control.enums.generator.SortOptions;

@SuppressWarnings("serial")
public class GenerateOptionPanel extends JDialog {

	private static GenerateOptionPanel optionpanel;

	private JCheckBox packageInfo;
	private ButtonGroup fields;
	private ButtonGroup methods;
	private ButtonGroup signatures;
	private ButtonGroup sortings;

	private static final String okButton = "Ok";
	private static final String cancelButton = "Cancel";

	private GenerateOptionPanel() {
		Container content = getContentPane();

		content.add(createOptionPanel(), BorderLayout.CENTER);
		content.add(createButtonPanel(), BorderLayout.SOUTH);
		setTitle("Import Details");
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getWidth() / 2);
	}

	private JPanel createOptionPanel() {
		JPanel optionPanel = new JPanel();
		optionPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		packageInfo = new JCheckBox("Show package");
		packageInfo.setSelected(true);
		optionPanel.add(packageInfo, layout(c, 0, 0));

		fields = createButtonGroup(FieldOptions.values());
		optionPanel.add(createSubPanel("Show fields", fields), layout(c, 0, 1));

		methods = createButtonGroup(MethodOptions.values());
		optionPanel.add(createSubPanel("Show methods", methods), layout(c, 1, 1));

		signatures = createButtonGroup(SignatureOptions.values());
		optionPanel.add(createSubPanel("Show signatures", signatures), layout(c, 0, 2));

		sortings = createButtonGroup(SortOptions.values());
		optionPanel.add(createSubPanel("Sorting", sortings), layout(c, 1, 2));

		optionPanel.validate();
		return optionPanel;
	}

	private <E extends Enum<E>> ButtonGroup createButtonGroup(E[] values) {
		ButtonGroup group = new ButtonGroup();
		for (E value : values) {
			JRadioButton button = new JRadioButton(value.toString());
			button.setActionCommand(value.toString());
			group.add(button);
		}
		return group;
	}

	private GridBagConstraints layout(GridBagConstraints c, int gridx, int gridy) {
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = gridx;
		c.gridy = gridy;
		return c;
	}

	private JPanel createSubPanel(String title, ButtonGroup group) {
		Enumeration<AbstractButton> e = group.getElements();
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		panel.setBorder(BorderFactory.createTitledBorder(title));
		while (e.hasMoreElements()) {
			panel.add(e.nextElement());
		}
		return panel;
	}

	private JPanel createButtonPanel() {
		CancelOkListener listener = new CancelOkListener();
		JButton button_ok = new JButton(okButton);
		button_ok.setActionCommand(okButton);
		button_ok.addActionListener(listener);
		JButton button_cancel = new JButton(cancelButton);
		button_cancel.setActionCommand(cancelButton);
		button_cancel.addActionListener(listener);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(button_cancel);
		buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPanel.add(button_ok);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		return buttonPanel;
	}

	public static GenerateOptionPanel getInstance() {
		if (optionpanel == null) {
			optionpanel = new GenerateOptionPanel();
		}
		return optionpanel;
	}

	private class CancelOkListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			optionpanel.setVisible(false);
			if (e.getActionCommand().equals(okButton)) {
				ConfigClassGen genCfg = ConfigClassGen.getInstance();
				genCfg.setGenerateClassPackage(packageInfo.isSelected());
				genCfg.setGenerateClassFields(FieldOptions.getEnum(fields.getSelection().getActionCommand().toString()));
				genCfg.setGenerateClassMethods(MethodOptions.getEnum(methods.getSelection().getActionCommand().toString()));
				genCfg.setGenerateClassSignatures(SignatureOptions.getEnum(signatures.getSelection().getActionCommand().toString()));
				genCfg.setGenerateClassSortings(SortOptions.getEnum(sortings.getSelection().getActionCommand().toString()));
			}
		}
	}

	public void showPanel() {
		ConfigClassGen genCfg = ConfigClassGen.getInstance();
		packageInfo.setSelected(genCfg.isGenerateClassPackage());
		setSelectedRadioButton(fields, genCfg.getGenerateClassFields());
		setSelectedRadioButton(methods, genCfg.getGenerateClassMethods());
		setSelectedRadioButton(signatures, genCfg.getGenerateClassSignatures());
		setSelectedRadioButton(sortings, genCfg.getGenerateClassSortings());
		setVisible(true);
		toFront();
	}

	private <E extends Enum<E>> void setSelectedRadioButton(ButtonGroup group, E value) {
		Enumeration<AbstractButton> buttons = group.getElements();
		while (buttons.hasMoreElements()) {
			AbstractButton button = buttons.nextElement();
			if (button.getActionCommand().equals(value.toString())) {
				button.setSelected(true);
			}
		}
	}
}
