package com.umlet.gui.base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.umlet.control.Umlet;
import com.umlet.control.diagram.DiagramHandler;

@SuppressWarnings("serial")
public class DiagramPopupMenu extends JPopupMenu {

	private boolean extended = false;

	private DiagramHandler diagramHandler;

	private JMenuItem newDiagram;
	private JMenuItem openDiagram;
	private JMenuItem saveDiagram;
	private JMenuItem saveAsDiagram;

	private JMenu exportDiagram;
	private JMenuItem exportAsBmp;
	private JMenuItem exportAsEps;
	private JMenuItem exportAsGif;
	private JMenuItem exportAsJpg;
	private JMenuItem exportAsPdf;
	private JMenuItem exportAsPng;
	private JMenuItem exportAsSvg;

	private JMenuItem printDiagram;
	private JMenuItem mailDiagram;

	public DiagramPopupMenu(DiagramHandler diagramHandler) {
		this(diagramHandler, false);
	}

	public DiagramPopupMenu(DiagramHandler diagramHandler, boolean extended) {
		this.extended = extended;
		this.diagramHandler = diagramHandler;

		initPopupMenu();
	}

	private void initPopupMenu() {
		// AB: Extended is true for StandaloneGUI
		if (extended) {
			newDiagram = new JMenuItem();
			openDiagram = new JMenuItem();
			saveDiagram = new JMenuItem();
			saveAsDiagram = new JMenuItem();

			newDiagram.setText("New");
			openDiagram.setText("Open...");
			saveDiagram.setText("Save");
			saveAsDiagram.setText("Save as...");

			newDiagram.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
			openDiagram.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
			saveDiagram.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));

			newDiagram.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
					{
					Umlet.getInstance().doNew();
				}
			});

			openDiagram.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
					{
					Umlet.getInstance().doOpen();
				}
			});

			saveDiagram.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
					{
					diagramHandler.doSave();
				}
			});

			saveAsDiagram.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
					{
					diagramHandler.doSaveAs("uxf");
				}
			});

			add(newDiagram);
			add(openDiagram);
			add(saveDiagram);
			add(saveAsDiagram);
		}

		exportDiagram = new JMenu();
		exportAsBmp = new JMenuItem();
		exportAsEps = new JMenuItem();
		exportAsGif = new JMenuItem();
		exportAsJpg = new JMenuItem();
		exportAsPdf = new JMenuItem();
		exportAsPng = new JMenuItem();
		exportAsSvg = new JMenuItem();

		printDiagram = new JMenuItem();
		mailDiagram = new JMenuItem();

		exportDiagram.setText("Export as");
		exportAsBmp.setText("BMP...");
		exportAsEps.setText("EPS...");
		exportAsGif.setText("GIF...");
		exportAsJpg.setText("JPG...");
		exportAsPdf.setText("PDF...");
		exportAsPng.setText("PNG...");
		exportAsSvg.setText("SVG...");

		printDiagram.setText("Print...");
		mailDiagram.setText("Mail to...");

		mailDiagram.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
		printDiagram.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));

		exportAsBmp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
				{
				diagramHandler.doSaveAs("bmp");
			}
		});

		exportAsEps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
				{
				diagramHandler.doSaveAs("eps");
			}
		});

		exportAsGif.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
				{
				diagramHandler.doSaveAs("gif");
			}
		});

		exportAsJpg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
				{
				diagramHandler.doSaveAs("jpg");
			}
		});

		exportAsPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
				{
				diagramHandler.doSaveAs("pdf");
			}
		});

		exportAsPng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
				{
				diagramHandler.doSaveAs("png");
			}
		});

		exportAsSvg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
				{
				diagramHandler.doSaveAs("svg");
			}
		});

		mailDiagram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
				{
				Umlet.getInstance().getGUI().setMailPanelEnabled(!Umlet.getInstance().getGUI().isMailPanelVisible());
			}
		});

		printDiagram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
				{
				diagramHandler.doPrint();
			}
		});

		exportDiagram.add(exportAsBmp);
		exportDiagram.add(exportAsEps);
		exportDiagram.add(exportAsGif);
		exportDiagram.add(exportAsJpg);
		exportDiagram.add(exportAsPdf);
		exportDiagram.add(exportAsPng);
		exportDiagram.add(exportAsSvg);

		add(exportDiagram);
		add(mailDiagram);
		add(printDiagram);

		addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				// En-/Disable Menu Items
				updatePopupMenu();
			}
		});
	}

	private void updatePopupMenu() {
		boolean enabled = !((diagramHandler == null) || diagramHandler.getDrawPanel().getAllEntities().isEmpty());

		if (extended) {
			saveDiagram.setEnabled(enabled && diagramHandler.isChanged()); // AB: Only allow to save if the diagram has been changed
			saveAsDiagram.setEnabled(enabled);
		}

		exportDiagram.setEnabled(enabled);
		exportAsBmp.setEnabled(enabled);
		exportAsEps.setEnabled(enabled);
		exportAsGif.setEnabled(enabled);
		exportAsJpg.setEnabled(enabled);
		exportAsPdf.setEnabled(enabled);
		exportAsPng.setEnabled(enabled);
		exportAsSvg.setEnabled(enabled);
		mailDiagram.setEnabled(enabled);
		printDiagram.setEnabled(enabled);
	}

}
