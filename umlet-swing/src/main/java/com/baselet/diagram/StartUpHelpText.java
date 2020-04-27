package com.baselet.diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.swing.JEditorPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.constants.SystemInfo;
import com.baselet.control.enums.Metakey;
import com.baselet.control.enums.Program;
import com.baselet.control.util.Utils;
import com.baselet.gui.CurrentGui;
import com.baselet.gui.listener.HyperLinkActiveListener;

public class StartUpHelpText extends JEditorPane implements ContainerListener, ComponentListener {

	private static final long serialVersionUID = 1L;

	static final Logger log = LoggerFactory.getLogger(StartUpHelpText.class);

	private DrawPanel panel;
	private boolean visible;

	public StartUpHelpText(DrawPanel panel) {
		super();
		this.panel = panel;

		// If the GUI is null (e.g.: if main is used in batch mode) the startup help text is not required
		if (CurrentGui.getInstance().getGui() == null) {
			return;
		}

		panel.addContainerListener(this);
		panel.addComponentListener(this);
		addMouseListener(new DelegatingMouseListener());
		try {
			if (UpdateCheckTimerTask.getInstance().getFilename() == null) {
				showHTML(createTempFileWithText(getDefaultTextWithReplacedSystemspecificMetakeys()));
			}
			else {
				showHTML(UpdateCheckTimerTask.getInstance().getFilename());
			}
		} catch (IOException e) {
			// #361: such an error should not crash UMLet, instead a simple information should be shown that something has gone wrong
			log.error("Cannot load startupinfo", e);
			setText("Cannot load startupinfo");
			setSize(130, 10);
		}
	}

	// Must be overwritten to hide the helptext if a the custom elements panel is toggled without elements on the drawpanel
	@Override
	public void setEnabled(boolean en) {
		super.setEnabled(en);
		if (en && visible) {
			if (panel.getGridElements().size() == 0) {
				setVisible(true);
			}
			else {
				visible = false;
			}
		}
		else {
			visible = isVisible();
			setVisible(false);
		}
	}

	static InputStream getStartUpFileName() {
		return StartUpHelpText.class.getClassLoader().getResourceAsStream("startuphelp.html");
	}

	private void showHTML(String filename) throws MalformedURLException, IOException {
		this.setPage(new URL("file:///" + filename));
		addHyperlinkListener(new HyperLinkActiveListener());
		setEditable(false);
		setBackground(Color.WHITE);
		setSelectionColor(getBackground());
		setSelectedTextColor(getForeground());
	}

	static String createTempFileWithText(String textToWriteIntoFile) throws IOException {
		File tempFile = File.createTempFile(Program.getInstance().getProgramName() + "_startupfile", ".html");
		tempFile.deleteOnExit();
		FileWriter w = new FileWriter(tempFile);
		w.write(textToWriteIntoFile);
		w.close();
		return tempFile.getAbsolutePath();
	}

	private static String getDefaultTextWithReplacedSystemspecificMetakeys() throws FileNotFoundException {
		StringBuilder sb = new StringBuilder("");
		Scanner sc = null;
		try {
			sc = new Scanner(getStartUpFileName());
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (SystemInfo.META_KEY == Metakey.CTRL) {
					line = line.replace(Metakey.CMD.toString(), Metakey.CTRL.toString());
				}
				else if (SystemInfo.META_KEY == Metakey.CMD) {
					line = line.replace(Metakey.CTRL.toString(), Metakey.CMD.toString());
				}
				sb.append(line).append("\n");
			}
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
		return sb.toString();
	}

	@Override
	public void componentAdded(ContainerEvent e) {
		boolean gridElementAdded = panel.getElementToComponent(e.getChild()) != null;
		if (gridElementAdded) {
			setVisible(false);
		}
	}

	@Override
	public void componentRemoved(ContainerEvent e) {
		if (e.getContainer().getComponentCount() <= 1 && !equals(e.getChild())) {
			setVisible(true);
		}
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		Dimension size = panel.getSize();
		Dimension labelSize = getPreferredSize();
		this.setSize(labelSize);
		int minDistanceFromTop = 25;
		int labelSizeToSubtract = Math.max(150, labelSize.height); // the upper border of the startup panel is at least 200px over the middle of the screen (necessary to have a good position for small update info windows)
		this.setLocation(size.width / 2 - labelSize.width / 2, Math.max(minDistanceFromTop, size.height / 2 - labelSizeToSubtract));
	}

	@Override
	public void paint(Graphics g) {
		// Subpixel rendering must be disabled for the startuphelp (looks bad)
		((Graphics2D) g).setRenderingHints(Utils.getUxRenderingQualityHigh(false));
		super.paint(g);
		((Graphics2D) g).setRenderingHints(Utils.getUxRenderingQualityHigh(true));
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {}

	@Override
	public void componentMoved(ComponentEvent arg0) {}

	@Override
	public void componentShown(ComponentEvent arg0) {}

	/**
	 * The MouseListener of this JEditorPane just delegates the
	 * MouseEvents up to the DiagramListener of the Handler
	 */
	private class DelegatingMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			panel.getHandler().getListener().mouseClicked(e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			panel.getHandler().getListener().mouseEntered(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			panel.getHandler().getListener().mouseExited(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			panel.getHandler().getListener().mousePressed(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			panel.getHandler().getListener().mouseReleased(e);
		}
	}
}
