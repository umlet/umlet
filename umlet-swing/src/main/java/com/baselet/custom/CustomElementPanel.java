package com.baselet.custom;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.text.JTextComponent;

import com.baselet.control.config.DerivedConfig;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.element.old.custom.CustomElementHandler;
import com.baselet.gui.listener.DividerListener;

@SuppressWarnings("serial")
public class CustomElementPanel extends JPanel {

	private final CustomElementAddDiscardListener saveListener = new CustomElementAddDiscardListener();

	private final JSplitPane custompanelsplitleft;
	private final JSplitPane custompanelsplitright;
	private final JLabel savelabel;
	private final CustomElementHandler customhandler;
	private final CustomCodeSyntaxPane customcodepane;
	private final DrawPanel custompreviewpanel;

	public CustomElementPanel(CustomElementHandler customhandler) {
		this.customhandler = customhandler;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel custompanel2 = new JPanel();
		custompanel2.setLayout(new BoxLayout(custompanel2, BoxLayout.Y_AXIS));

		JLabel codelabel = new JLabel(" Code");
		codelabel.setFont(DerivedConfig.getPanelHeaderFont());
		codelabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		custompanel2.add(codelabel);
		customcodepane = customhandler.getCodePane();
		JPanel codePanel = customcodepane.getPanel();
		codePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		custompanel2.add(codePanel);

		JPanel custompanel3 = new JPanel();
		custompanel3.setLayout(new BoxLayout(custompanel3, BoxLayout.Y_AXIS));
		DiagramHandler d = customhandler.getPreviewHandler();
		custompreviewpanel = d.getDrawPanel();
		custompreviewpanel.getScrollPane().setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel previewlabel = new JLabel(" Preview");
		previewlabel.setFont(DerivedConfig.getPanelHeaderFont());
		previewlabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel labelpanel = new JPanel();
		labelpanel.setLayout(new BoxLayout(labelpanel, BoxLayout.Y_AXIS));
		labelpanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		savelabel = new JLabel(" Add to diagram and close editor") {
			private boolean enabled = false;

			@Override
			public void setEnabled(boolean en) {
				if (!enabled && en) {
					enabled = en;
					addMouseListener(saveListener);
				}
				else if (enabled && !en) {
					enabled = en;
					removeMouseListener(saveListener);
				}
			}
		};
		savelabel.setFont(DerivedConfig.getPanelHeaderFont());
		savelabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		savelabel.setEnabled(true);

		JLabel discardlabel = new JLabel("Discard and close editor");
		discardlabel.setFont(DerivedConfig.getPanelHeaderFont());
		discardlabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		discardlabel.addMouseListener(saveListener);

		custompanel3.add(previewlabel);
		custompanel3.add(custompreviewpanel.getScrollPane());
		custompanel3.add(labelpanel);
		custompanel3.addComponentListener(new DividerListener());
		labelpanel.add(Box.createHorizontalGlue());
		labelpanel.add(savelabel);
		labelpanel.add(discardlabel);

		custompanelsplitleft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, null, custompanel2);
		custompanelsplitleft.setDividerSize(5);
		custompanelsplitleft.setResizeWeight(0);
		custompanelsplitleft.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));
		custompanelsplitright = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, custompanelsplitleft, custompanel3);
		custompanelsplitright.setDividerSize(5);
		custompanelsplitright.setResizeWeight(1);
		custompanelsplitright.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 0));

		this.add(custompanelsplitright);
	}

	public void setCustomElementSaveable(boolean enable) {
		customhandler.getPreviewHandler().getDrawPanel().setEnabled(enable);
		savelabel.setEnabled(enable);
		if (enable) {
			savelabel.setForeground(Color.black);
		}
		else {
			savelabel.setForeground(Color.gray);
		}
	}

	// specifies if the custom element is opened as new element or as edited element
	public void setCustomElementIsNew(boolean isnew) {
		if (isnew) {
			savelabel.setText("Add to diagram and close editor");
		}
		else {
			savelabel.setText("Update in diagram and close editor");
		}
	}

	public JSplitPane getLeftSplit() {
		return custompanelsplitleft;
	}

	public JSplitPane getRightSplit() {
		return custompanelsplitright;
	}

	public JTextComponent getTextPane() {
		return customcodepane.getTextComponent();
	}

	@Override
	public void repaint() {
		super.repaint();
		if (customcodepane != null) {
			customcodepane.repaint();
		}
		if (custompreviewpanel != null && custompreviewpanel.getScrollPane() != null) {
			custompreviewpanel.getScrollPane().repaint();
		}
	}
}
