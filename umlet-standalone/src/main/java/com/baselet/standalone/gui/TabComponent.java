package com.baselet.standalone.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.baselet.diagram.DiagramHandler;

@SuppressWarnings("serial")
public class TabComponent extends JPanel {
	private DiagramHandler handler;
	private TabListener listener;
	private Color background;
	private JButton button;

	public TabComponent(final JTabbedPane pane, DiagramHandler handler) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.handler = handler;

		setOpaque(false);

		// make JLabel read titles from JTabbedPane
		JLabel label = new JLabel() {
			@Override
			public String getText() {
				int i = pane.indexOfTabComponent(TabComponent.this);
				if (i != -1) {
					return pane.getTitleAt(i);
				}
				return null;
			}
		};

		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 7));
		add(label);
		button = new TabButton();
		add(button);
		listener = new TabListener(handler, pane);
		addMouseListener(listener);
		background = getBackground();
	}

	@Override
	public void setEnabled(boolean en) {
		super.setEnabled(en);
		if (en) {
			setBackground(background);
			addMouseListener(listener);
		}
		else {
			setBackground(Color.lightGray);
			removeMouseListener(listener);
		}
		button.setEnabled(en);
	}

	private class TabButton extends JButton implements MouseListener {

		public TabButton() {
			int size = 17;
			setPreferredSize(new Dimension(size, size));
			setToolTipText("close this tab");
			// Make it transparent
			setContentAreaFilled(false);
			// No need to be focusable
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			// Making nice rollover effect
			// we use the same listener for all buttons
			addMouseListener(this);
		}

		@Override
		public void setEnabled(boolean en) {
			super.setEnabled(en);
			if (en) {
				addMouseListener(this);
				setToolTipText("close this tab");
			}
			else {
				removeMouseListener(this);
				setToolTipText(null);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(true);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(false);
			}
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			handler.doCloseAndAddNewIfNoLeft();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {

		}

		// we don't want to update UI for this button
		@Override
		public void updateUI() {}

		// paint the cross
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			// shift the image for pressed buttons
			if (getModel().isPressed()) {
				g2.translate(1, 1);
			}
			g2.setColor(Color.BLACK);
			int delta = 6;
			g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
			g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
			g2.dispose();
		}
	}
}
