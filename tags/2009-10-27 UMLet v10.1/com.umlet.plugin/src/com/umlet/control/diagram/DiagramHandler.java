package com.umlet.control.diagram;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.io.DiagramFileHandler;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Group;
import com.umlet.element.base.Relation;
import com.umlet.gui.base.StartUpHelpText;
import com.umlet.gui.base.listeners.DiagramListener;
import com.umlet.gui.base.listeners.EntityListener;
import com.umlet.gui.base.listeners.RelationListener;
import com.umlet.gui.standalone.StandaloneGUI;

public class DiagramHandler {
	private boolean isChanged;
	private DiagramFileHandler fileHandler;
	private DrawPanel drawpanel;
	private Controller controller;
	protected DiagramListener listener;
	private String helptext;
	private boolean enabled;
	private int gridSize;
	private Integer fontsize;

	public DiagramHandler(File diagram) {
		this(diagram, false);
	}

	protected DiagramHandler(File diagram, boolean nolistener) {
		gridSize = Constants.DEFAULTGRIDSIZE;
		this.isChanged = false;
		this.enabled = true;
		this.drawpanel = new DrawPanel(this);
		this.controller = new Controller(this);
		this.fileHandler = DiagramFileHandler.createInstance(this, diagram);
		if (!nolistener) this.setListener(new DiagramListener(this));
		new StartUpHelpText(drawpanel); // Create a new startup help panel
		if (diagram != null) this.fileHandler.doOpen();
		if (this.helptext == null) this.helptext = Constants.DEFAULT_HELPTEXT;
		if (Umlet.getInstance().getGUI() instanceof StandaloneGUI) {
			((StandaloneGUI) Umlet.getInstance().getGUI()).setValueOfZoomDisplay(getGridSize());
		}
	}

	public void setEnabled(boolean en) {
		if (!en && enabled) {
			this.drawpanel.removeMouseListener(this.listener);
			this.drawpanel.removeMouseMotionListener(this.listener);
			enabled = false;
		}
		else if (en && !enabled) {
			this.drawpanel.addMouseListener(this.listener);
			this.drawpanel.addMouseMotionListener(this.listener);
			enabled = true;
		}
	}

	protected void setListener(DiagramListener listener) {
		this.listener = listener;
		this.drawpanel.addMouseListener(this.listener);
		this.drawpanel.addMouseMotionListener(this.listener);
		this.drawpanel.addMouseWheelListener(this.listener);
	}

	public float getZoomedFontsize() {
		return (this.getRealFontsize() * getGridSize()) / Constants.DEFAULTGRIDSIZE;
	}

	public float getRealFontsize() {
		return this.getFontsize();
	}

	public Font getZoomedFont() {
		return new Font(Constants.FONT, Font.PLAIN, (int) getZoomedFontsize());
	}

	public Font getRealFont() {
		return new Font(Constants.FONT, Font.PLAIN, (int) getRealFontsize());
	}

	public float getZoomedDistLineToText() {
		return getZoomedFontsize() / 4;
	}

	public float getZoomedDistTextToLine() {
		return getZoomedFontsize() / 4;
	}

	public float getZoomedDistTextToText() {
		return getZoomedFontsize() / 4;
	}

	public float getRealDistLineToText() {
		return getRealFontsize() / 4;
	}

	public float getRealDistTextToLine() {
		return getRealFontsize() / 4;
	}

	public float getRealdDistTextToText() {
		return getRealFontsize() / 4;
	}

	public Dimension getZoomedTextSize(Graphics2D g2, String s) {
		if (s == null) return null;
		if (s.length() == 0) return new Dimension(0, 0);
		TextLayout tl = new TextLayout(s, getZoomedFont(), getFRC(g2));
		return new Dimension((int) tl.getBounds().getWidth(), (int) tl.getBounds().getHeight());
	}

	public Dimension getRealTextSize(Graphics2D g2, String s) {
		if (s == null) return null;
		if (s.length() == 0) return new Dimension(0, 0);
		TextLayout tl = new TextLayout(s, getRealFont(), getFRC(g2));
		return new Dimension((int) tl.getBounds().getWidth(), (int) tl.getBounds().getHeight());
	}

	public int getZoomedTextWidth(Graphics2D g2, String s) {
		if (s == null) return 0;
		return (int) this.getZoomedTextSize(g2, s).getWidth();
	}

	public int getRealTextWidth(Graphics2D g2, String s) {
		if (s == null) return 0;
		return (int) this.getRealTextSize(g2, s).getWidth();
	}

	public int getZoomedTextHeight(Graphics2D g2, String s) {
		if (s == null) return 0;
		return (int) this.getZoomedTextSize(g2, s).getHeight();
	}

	public int getRealTextHeight(Graphics2D g2, String s) {
		if (s == null) return 0;
		return (int) this.getRealTextSize(g2, s).getHeight();
	}

	public Font makeFontPlain(Font f) {
		return new Font(f.getName(), Font.PLAIN, f.getSize());
	}

	public Font makeFontBold(Font f) {
		return new Font(f.getName(), Font.BOLD, f.getSize());
	}

	public Font makeFontItalic(Font f) {
		return new Font(f.getName(), Font.ITALIC, f.getSize());
	}

	public Font makeFontBoldItalic(Font f) {
		return new Font(f.getName(), Font.BOLD | Font.ITALIC, f.getSize());
	}

	public FontRenderContext getFRC(Graphics2D g2) {
		FontRenderContext rendering;
		rendering = new FontRenderContext(null, true, true);
		g2.setRenderingHints(Constants.getUxRenderingQualityHigh());
		return rendering;
	}

	public void write(Graphics2D g2, String s, int x, int y, int align, int valign) {
		if (s == null) return;

		boolean underline = false;
		boolean bold = false;
		boolean italic = false;

		String checkedString = checkStringForValidFormatsAndRemoveTheirLabels(s);
		if (checkedString.charAt(0) == '1') underline = true;
		if (checkedString.charAt(1) == '1') bold = true;
		if (checkedString.charAt(2) == '1') italic = true;
		s = checkedString.substring(3);

		if (bold && italic) g2.setFont(makeFontBoldItalic(g2.getFont()));
		else if (bold) g2.setFont(makeFontBold(g2.getFont()));
		else if (italic) g2.setFont(makeFontItalic(g2.getFont()));
		else g2.setFont(makeFontPlain(g2.getFont()));

		s = s.replaceAll("<<", "\u00AB");
		s = s.replaceAll(">>", "\u00BB");

		if (align == Constants.CENTER) x = x - getZoomedTextWidth(g2, s) / 2;
		else if (align == Constants.RIGHT) x = x - getZoomedTextWidth(g2, s);

		if (valign == Constants.CENTER) y = y + getZoomedTextHeight(g2, s) / 2;
		else if (valign == Constants.TOP) y = y + getZoomedTextHeight(g2, s);

		g2.drawString(s, x, y);
		if (underline) {
			TextLayout l = new TextLayout(s, g2.getFont(), getFRC(g2));
			Rectangle2D r2d = l.getBounds();
			if (bold) {
				g2.drawLine(x, y + (int) getZoomedDistTextToLine() / 2, x + (int) r2d.getWidth(), y + (int) getZoomedDistTextToLine() / 2);
			}
			else {
				g2.drawLine(x, y + (int) getZoomedDistTextToLine() / 2, x + (int) r2d.getWidth(), y + (int) getZoomedDistTextToLine() / 2);
			}
		}
		if (italic || bold) makeFontPlain(g2.getFont());
	}

	/**
	 * Checks the String for formats and returns the String without the valid format labels and with a starting
	 * sequence of numbers which describe the valid format labels
	 * The first number shows if the text is underlined, the second bold, the third italic
	 */
	public static String checkStringForValidFormatsAndRemoveTheirLabels(String s) {
		boolean underline = false;
		boolean bold = false;
		boolean italic = false;

		/*
		 * NOT USED NOW
		 * int spaceBefore = 0;
		 * int spaceAfter = 0;
		 * //Before scanning for text format we remove spaces
		 * while (s.startsWith(" ")) {
		 * spaceBefore++;
		 * s = s.substring(1);
		 * }
		 * while (s.endsWith(" ")) {
		 * spaceAfter++;
		 * s = s.substring(0, s.length()-1);
		 * }
		 */

		// As long as any text format applies the loop continues (any format type is only allowed once)
		while (true) {
			if (!underline && s.startsWith(Constants.FormatLabels.UNDERLINE) && s.endsWith(Constants.FormatLabels.UNDERLINE) && (s.length() > 2)) {
				underline = true;
				s = s.substring(1, s.length() - 1);
				continue;
			}
			if (!bold && s.startsWith(Constants.FormatLabels.BOLD) && s.endsWith(Constants.FormatLabels.BOLD) && (s.length() > 2)) {
				bold = true;
				s = s.substring(1, s.length() - 1);
				continue;
			}
			if (!italic && s.startsWith(Constants.FormatLabels.ITALIC) && s.endsWith(Constants.FormatLabels.ITALIC) && (s.length() > 2)) {
				italic = true;
				s = s.substring(1, s.length() - 1);
				continue;
			}
			break;
		}

		/*
		 * NOT USED NOW
		 * //After scanning we add the spaces again to the string
		 * for (int i = 0; i < spaceBefore; spaceBefore--) {
		 * spaceBefore--;
		 * s = " " + s;
		 * }
		 * for (int i = 0; i < spaceAfter; spaceAfter--) {
		 * spaceAfter--;
		 * s = s + " ";
		 * }
		 */

		// The returning String starts with a number sequence to show which labels applies.
		// Warning: The first added digit is the last of the sequence after adding all digits, so we must add the last at first and the first at last
		if (italic) s = "1" + s;
		else s = "0" + s;
		if (bold) s = "1" + s;
		else s = "0" + s;
		if (underline) s = "1" + s;
		else s = "0" + s;

		return s;
	}

	public void writeText(Graphics2D g2, String s, int x, int y, boolean center) {
		if (center) this.writeText(g2, s, x, y, Constants.CENTER, Constants.BOTTOM);
		else this.writeText(g2, s, x, y, Constants.LEFT, Constants.BOTTOM);
	}

	public void writeText(Graphics2D g2, String s, int x, int y, int align, int valign) {
		this.write(g2, s, x, y, align, valign);
	}

	public DiagramListener getListener() {
		return this.listener;
	}

	public void setChanged(boolean changed) {
		if (this.isChanged != changed) {
			this.isChanged = changed;
			Umlet.getInstance().getGUI().setDiagramChanged(this, changed);
		}
	}

	public DrawPanel getDrawPanel() {
		return this.drawpanel;
	}

	public Vector<Entity> getSelectedEntities() {
		return this.drawpanel.getSelector().getSelectedEntities();
	}

	public DiagramFileHandler getFileHandler() {
		return this.fileHandler;
	}

	public Controller getController() {
		return this.controller;
	}

	// returnvalue needed for eclipse plugin
	// returns true if the file is saved, else returns false
	public boolean doSave() {
		if (this.fileHandler.doSave()) {
			this.setChanged(false);
			this.reloadPalettes();
			return true;
		}
		else return false;
	}

	public void doSaveAs(String extension) {
		if (this.fileHandler.doSaveAs(extension)) {
			this.setChanged(false);
			this.reloadPalettes();
		}
	}

	public File saveTempFileWithDiagram(String name, String extension) throws Exception {
		return this.fileHandler.saveTempFileWithDiagram(name, extension);
	}

	public void doPrint() {
		PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(this.getDrawPanel());
		if (printJob.printDialog()) try {
			printJob.print();
		} catch (PrinterException pe) {
			Umlet.displayError("An error occured during printing.");
		}
	}

	// reloads the diagram from file + updates gui
	public void reload() {
		this.drawpanel.removeAll();
		this.fileHandler.doOpen();
	}

	// reloads palettes if the palette has been changed.
	private void reloadPalettes() {
		for (DiagramHandler d : Umlet.getInstance().getPalettes().values()) {
			if (d.getFileHandler().equals(this.getFileHandler()) && !d.equals(this)) d.reload();
		}
	}

	public void doClose() {
		if (this.askSaveIfDirty()) {
			Umlet.getInstance().getDiagrams().remove(this);
			Umlet.getInstance().getGUI().close(this);
			this.drawpanel.getSelector().deselectAll();

			// update property panel to now selected diagram (or to empty if no diagram exists)
			DiagramHandler newhandler = Umlet.getInstance().getDiagramHandler(); // 
			if (newhandler != null) newhandler.getDrawPanel().getSelector().updateSelectorInformation();
			else Umlet.getInstance().setPropertyPanelToEntity(null);
		}
	}

	public String getName() {
		String name = this.fileHandler.getFileName();
		return name.substring(0, name.lastIndexOf("."));
	}

	public String getFullPathName() {
		return this.fileHandler.getFullPathName();
	}

	// function to be able to controll the entitylistener + diagramlistener from the handler
	public EntityListener getEntityListener(Entity e) {
		if (e instanceof Relation) return RelationListener.getInstance(this);
		return EntityListener.getInstance(this);
	}

	public boolean askSaveIfDirty() {
		if (this.isChanged) {
			int ch = JOptionPane.showOptionDialog(Umlet.getInstance().getGUI(), "Save changes?", "UMLet - " + this.getName(), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
			if (ch == JOptionPane.YES_OPTION) {
				this.doSave();
				return true;
			}
			else if (ch == JOptionPane.NO_OPTION) { return true; }
			return false;
		}
		return true;
	}

	public void setHelpText(String helptext) {
		this.helptext = helptext;
		if (!this.helptext.equals(Umlet.getInstance().getGUI().getPropertyPanelText())) Umlet.getInstance().getGUI().setPropertyPanelText(this.helptext);
	}

	public String getHelpText() {
		return this.helptext;
	}

	public boolean isChanged() {
		return this.isChanged;
	}

	public void setFontsize(Integer fontsize) {
		this.fontsize = fontsize;
	}

	public int getFontsize() {
		if (fontsize == null) return Constants.defaultFontsize;
		return fontsize;
	}

	public int getGridSize() {
		return gridSize;
	}

	public float getZoomFactor() {
		return (float) getGridSize() / (float) Constants.DEFAULTGRIDSIZE;
	}

	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
	}

	public int ensureValueIsOnGrid(int val) {
		// val must be a multiple of the gridsize
		if (Math.abs(val) % gridSize != 0) val -= val % gridSize;
		return val;
	}

	public void setGridAndZoom(int factor) {
		setGridAndZoom(factor, true);
	}

	public void setGridAndZoom(int factor, boolean zoomToTheCenter) {

		/**
		 * Store the old gridsize and the new one. Furthermore check if the zoom process must be made
		 */

		int oldGridSize = getGridSize();

		if ((factor < 1) || (factor > 20)) return; // Only zoom between 10% and 200% is allowed
		if (factor == oldGridSize) return; // Only zoom is gridsize has changed

		setGridSize(factor);

		/**
		 * The entities must be resized to the new gridsize
		 */

		// We must resize all entitys to match the new zoom level
		for (Entity entity : getDrawPanel().getAllEntities()) {

			// Groups are handled separately later
			if (entity instanceof Group) continue;

			int newX = (entity.getX() * gridSize) / oldGridSize;
			int newY = (entity.getY() * gridSize) / oldGridSize;
			int newW = (entity.getWidth() * gridSize) / oldGridSize;
			int newH = (entity.getHeight() * gridSize) / oldGridSize;
			entity.changeLocation(newX - entity.getX(), newY - entity.getY());
			entity.changeSize(newW - entity.getWidth(), newH - entity.getHeight());

			// We must also resize the coordinates of the points of the relations
			if (entity instanceof Relation) {
				for (Point point : ((Relation) entity).getLinePoints()) {
					newX = ((int) point.getX() * gridSize) / oldGridSize;
					newY = ((int) point.getY() * gridSize) / oldGridSize;
					point.setLocation(newX, newY);
				}
			}
		}

		/**
		 * The zoomed diagram will shrink to the upper left corner and grow to the lower right
		 * corner but we want to have the zoom center in the middle of the actual visible drawpanel
		 * so we have to change the coordinates of the entities
		 */

		if (zoomToTheCenter) {
			// We calculate the middle point of the actual diagramspanel size
			int x = (int) (getDrawPanel().getViewableDiagrampanelSize().getWidth() / 2);
			int y = (int) (getDrawPanel().getViewableDiagrampanelSize().getHeight() / 2);
			// And add any space on the upper left corner which is not visible but reachable by scrollbar
			x += getDrawPanel().getScrollPanel().getViewport().getViewPosition().getX();
			y += getDrawPanel().getScrollPanel().getViewport().getViewPosition().getY();

			// The result is the point where we want to center the zoom of the diagram
			int diffx, diffy;
			diffx = x - x * gridSize / oldGridSize;
			diffy = y - y * gridSize / oldGridSize;

			for (Entity e : getDrawPanel().getAllEntities()) {
				// Groups are handled separately later
				if (!(e instanceof Group)) e.changeLocation(ensureValueIsOnGrid(diffx), ensureValueIsOnGrid(diffy));
			}
		}

		/**
		 * After all location and size changes we must adjust the size of the
		 * top level groups (groups who aren't part of other groups)
		 */

		for (Entity entity : getDrawPanel().getAllEntities()) {
			if ((entity instanceof Group) && !entity.isPartOfGroup()) {
				((Group) entity).adjustSize(true);
			}
		}

		/**
		 * Now we have to do some additional "clean up" stuff which is related to the zoom
		 */

		// We realign everything to the grid to be compatible to old diagrams where not everything was on grid
		getDrawPanel().realignToGrid();

		// We must adjust the coordinates for the scrollbars and revalidate the drawpanel
		getDrawPanel().insertUpperLeftWhitespaceIfNeeded();
		getDrawPanel().removeUnnecessaryWhitespaceAroundDiagram();
		getDrawPanel().revalidate();

		// We set the isChanged variable to allow saving in EclipsePlugin and to reflect the changes made by zooming
		setChanged(true);

		// If this is the standalone client we must also set the zoom value on the upper panel
		if (Umlet.getInstance().getGUI() instanceof StandaloneGUI) {
			((StandaloneGUI) Umlet.getInstance().getGUI()).setValueOfZoomDisplay(factor);
		}
	}

}
