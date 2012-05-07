package com.baselet.diagram;

import java.awt.Point;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Constants.Program;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.io.DiagramFileHandler;
import com.baselet.element.GridElement;
import com.baselet.element.Group;
import com.baselet.gui.BaseGUI;
import com.baselet.gui.DiagramPopupMenu;
import com.baselet.gui.listener.DiagramListener;
import com.baselet.gui.listener.GridElementListener;
import com.baselet.gui.listener.RelationListener;
import com.baselet.gui.standalone.StandaloneGUI;
import com.umlet.element.Relation;

public class DiagramHandler {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	private boolean isChanged;
	private DiagramFileHandler fileHandler;
	private FontHandler fontHandler;

	protected DrawPanel drawpanel;
	private Controller controller;
	protected DiagramListener listener;
	private String helptext;
	private boolean enabled;
	private int gridSize;

	public DiagramHandler(File diagram) {
		this(diagram, false);
	}

	protected DiagramHandler(File diagram, boolean nolistener) {
		gridSize = Constants.DEFAULTGRIDSIZE;
		this.isChanged = false;
		this.enabled = true;
		this.drawpanel = new DrawPanel(this);
		this.controller = new Controller(this);
		this.fontHandler = new FontHandler(this);
		this.fileHandler = DiagramFileHandler.createInstance(this, diagram);
		if (!nolistener) this.setListener(new DiagramListener(this));
		if (diagram != null) this.fileHandler.doOpen();

		boolean extendedPopupMenu = false;
		BaseGUI gui = Main.getInstance().getGUI();
		if (gui != null) {
			gui.setValueOfZoomDisplay(getGridSize());
			if (gui instanceof StandaloneGUI) extendedPopupMenu = true; // AB: use extended popup menu on standalone gui only
		}

		if (!(this instanceof PaletteHandler)) drawpanel.setComponentPopupMenu(new DiagramPopupMenu(this, extendedPopupMenu));

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

	public DiagramListener getListener() {
		return this.listener;
	}

	public void setChanged(boolean changed) {
		if (this.isChanged != changed) {
			this.isChanged = changed;
			BaseGUI gui = Main.getInstance().getGUI();
			if (gui != null) gui.setDiagramChanged(this, changed);
		}
	}

	public DrawPanel getDrawPanel() {
		return this.drawpanel;
	}

	public DiagramFileHandler getFileHandler() {
		return this.fileHandler;
	}

	public FontHandler getFontHandler() {
		return this.fontHandler;
	}

	public Controller getController() {
		return this.controller;
	}

	// returnvalue needed for eclipse plugin
	// returns true if the file is saved, else returns false
	public boolean doSave() {
		try {
			this.fileHandler.doSave();
			this.reloadPalettes();
			return true;
		} catch (IOException e) {
			Main.displayError(Constants.ERROR_SAVING_FILE);
			return false;
		}
	}

	public void doSaveAs(String extension) {
		if (this.drawpanel.getAllEntities().isEmpty()) Main.displayError(Constants.ERROR_SAVING_EMPTY_DIAGRAM);
		else {
			try {
				this.fileHandler.doSaveAs(extension);
				this.reloadPalettes();
			} catch (IOException e) {
				Main.displayError(Constants.ERROR_SAVING_FILE);
			}
		}
	}

	public void doPrint() {
		PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(this.getDrawPanel());
		if (printJob.printDialog()) try {
			printJob.print();
		} catch (PrinterException pe) {
			Main.displayError(Constants.ERROR_PRINTING);
		}
	}

	// reloads the diagram from file + updates gui
	public void reload() {
		drawpanel.removeAll();
		fileHandler.doOpen();
		drawpanel.updatePanelAndScrollbars();
	}

	// reloads palettes if the palette has been changed.
	private void reloadPalettes() {
		for (DiagramHandler d : Main.getInstance().getPalettes().values()) {
			if (d.getFileHandler().equals(this.getFileHandler()) && !d.equals(this)) d.reload();
		}
		this.getDrawPanel().getSelector().updateSelectorInformation(); // Must be updated to remain in the current Property Panel
	}

	public void doClose() {
		if (this.askSaveIfDirty()) {
			Main.getInstance().getDiagrams().remove(this);
			Main.getInstance().getGUI().close(this);
			this.drawpanel.getSelector().deselectAll();

			// update property panel to now selected diagram (or to empty if no diagram exists)
			DiagramHandler newhandler = Main.getInstance().getDiagramHandler(); // 
			if (newhandler != null) newhandler.getDrawPanel().getSelector().updateSelectorInformation();
			else Main.getInstance().setPropertyPanelToGridElement(null);
		}
	}

	public String getName() {
		String name = this.fileHandler.getFileName();
		if (name.contains(".")) name = name.substring(0, name.lastIndexOf("."));
		return name;
	}

	public String getFullPathName() {
		return this.fileHandler.getFullPathName();
	}

	// function to be able to controll the entitylistener + diagramlistener from the handler
	public GridElementListener getEntityListener(GridElement e) {
		if (e instanceof Relation) return RelationListener.getInstance(this);
		return GridElementListener.getInstance(this);
	}

	public boolean askSaveIfDirty() {
		if (this.isChanged) {
			int ch = JOptionPane.showOptionDialog(Main.getInstance().getGUI(), "Save changes?", Program.PROGRAM_NAME + " - " + this.getName(), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
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
		BaseGUI gui = Main.getInstance().getGUI();
		if (gui != null && !this.helptext.equals(gui.getPropertyPanelText())) gui.setPropertyPanelText(this.helptext);
	}

	public String getHelpText() {
		if (this.helptext == null) return Constants.DEFAULT_HELPTEXT;
		else return this.helptext;
	}

	public boolean isChanged() {
		return this.isChanged;
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

	public int realignToGrid(int val) {
		return realignToGrid(true, val, false);
	}

	public int realignToGrid(boolean logRealign, int val) {
		return realignToGrid(logRealign, val, false);
	}

	/**
	 * returns the integer which is nearest to val but on the grid (round down)
	 * 
	 * @param logRealign
	 *            if true a realign is logged as an error
	 * @param val
	 *            value which should be rounded to the grid
	 * @param roundUp
	 *            if true the realign rounds up instead of down
	 * @return value on the grid
	 */
	public int realignToGrid(boolean logRealign, int val, boolean roundUp) {
		if (val % gridSize != 0) {
			if (logRealign) log.error("realignToGrid");
			val -= val % gridSize;
			if (roundUp) val += gridSize;
		}
		return val;
	}

	static public int realignTo(int val, int toVal) {
		if (val % toVal != 0) {
			val -= val % toVal;
		}
		return val;
	}

	static public void zoomEntity(int fromFactor, int toFactor, GridElement e) {
		Vector<GridElement> vec = new Vector<GridElement>();
		vec.add(e);
		zoomEntities(fromFactor, toFactor, vec);
	}

	static public void zoomEntities(int fromFactor, int toFactor, Vector<GridElement> selectedEntities) {

		/**
		 * The entities must be resized to the new factor
		 */

		for (GridElement entity : selectedEntities) {
			// Entities in groups are not part of the selectedEntities vector. therefore they must be zoomed explicitely
			if (entity instanceof Group) zoomEntities(fromFactor, toFactor, ((Group) entity).getMembers());

			int newX = (entity.getX() * toFactor) / fromFactor;
			int newY = (entity.getY() * toFactor) / fromFactor;
			int newW = (entity.getWidth() * toFactor) / fromFactor;
			int newH = (entity.getHeight() * toFactor) / fromFactor;
			entity.setLocation(realignTo(newX, toFactor), realignTo(newY, toFactor));
			// Normally there should be no realign here but relations and custom elements sometimes must be realigned therefore we don't log it as an error
			entity.setSize(realignTo(newW, toFactor), realignTo(newH, toFactor));

			// Resize the coordinates of the points of the relations
			if (entity instanceof Relation) {
				for (Point point : ((Relation) entity).getLinePoints()) {
					newX = ((int) point.getX() * toFactor) / fromFactor;
					newY = ((int) point.getY() * toFactor) / fromFactor;
					point.setLocation(realignTo(newX, toFactor), realignTo(newY, toFactor));
				}
			}
		}

		/**
		 * After all location and size changes we must adjust the size of the
		 * top level groups (groups who aren't part of other groups)
		 */

		for (GridElement entity : selectedEntities) {
			if ((entity instanceof Group) && !entity.isPartOfGroup()) {
				((Group) entity).adjustSize(true);
			}
		}

	}

	public void setGridAndZoom(int factor) {
		setGridAndZoom(factor, true);
	}

	public void setGridAndZoom(int factor, boolean manualZoom) {

		/**
		 * Store the old gridsize and the new one. Furthermore check if the zoom process must be made
		 */

		int oldGridSize = getGridSize();

		if ((factor < 1) || (factor > 20)) return; // Only zoom between 10% and 200% is allowed
		if (factor == oldGridSize) return; // Only zoom if gridsize has changed

		setGridSize(factor);

		/**
		 * Zoom entities to the new gridsize
		 */

		zoomEntities(oldGridSize, gridSize, getDrawPanel().getAllEntitiesNotInGroup());

		// AB: Zoom origin
		getDrawPanel().zoomOrigin(oldGridSize, gridSize);

		/**
		 * The zoomed diagram will shrink to the upper left corner and grow to the lower right
		 * corner but we want to have the zoom center in the middle of the actual visible drawpanel
		 * so we have to change the coordinates of the entities again
		 */

		if (manualZoom) {
			// Calculate the middle point of the actual diagramspanel size
			float x = ((float) getDrawPanel().getViewableDiagrampanelSize().getWidth() / 2);
			float y = ((float) getDrawPanel().getViewableDiagrampanelSize().getHeight() / 2);

			// And add any space on the upper left corner which is not visible but reachable by scrollbar
			x += getDrawPanel().getScrollPane().getViewport().getViewPosition().getX();
			y += getDrawPanel().getScrollPane().getViewport().getViewPosition().getY();

			// The result is the point where we want to center the zoom of the diagram
			float diffx, diffy;
			diffx = x - (x * gridSize / oldGridSize);
			diffy = y - (y * gridSize / oldGridSize);

			// AB: Move origin in opposite direction
			log.debug("diffX/diffY: " + diffx + "/" + diffy);
			log.debug("Manual Zoom Delta: " + realignToGrid(false, (int) diffx) + "/" + realignToGrid(false, (int) diffy));
			getDrawPanel().moveOrigin(realignToGrid(false, -(int) diffx), realignToGrid(false, -(int) diffy));

			for (GridElement e : getDrawPanel().getAllEntitiesNotInGroup()) {
				e.changeLocation(realignToGrid(false, (int) diffx), realignToGrid(false, (int) diffy));
			}

			/**
			 * Now we have to do some additional "clean up" stuff which is related to the zoom
			 */

			getDrawPanel().updatePanelAndScrollbars();

			// Set the isChanged variable to allow saving in EclipsePlugin and to reflect the changes made by zooming
			setChanged(true);

			BaseGUI gui = Main.getInstance().getGUI();
			if (gui != null) gui.setValueOfZoomDisplay(factor);

			float zoomFactor = Main.getInstance().getDiagramHandler().getZoomFactor() * 100;
			String zoomtext;
			if (Main.getInstance().getDiagramHandler() instanceof PaletteHandler) zoomtext = "Palette zoomed to " + (new Integer((int) zoomFactor).toString()) + "%";
			else zoomtext = "Diagram zoomed to " + (new Integer((int) zoomFactor).toString()) + "%";
			Main.getInstance().getCurrentInfoDiagramHandler().getDrawPanel().showNotification(zoomtext);
		}
	}
}
