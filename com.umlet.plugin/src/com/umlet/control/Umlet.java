// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control;

import java.io.File;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.control.diagram.PaletteHandler;
import com.umlet.control.io.DiagramFileHandler;
import com.umlet.custom.CustomElementSecurityManager;
import com.umlet.element.base.Entity;
import com.umlet.gui.base.UmletGUI;
import com.umlet.gui.standalone.StandaloneGUI;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2001
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class Umlet {

	private static String CUSTOM_ELEMENTS_PATH = "custom_elements/";
	private static Umlet _instance;
	private static String umlet_file = "umlet.tmp";
	private static String umlet_read_file = "umlet_1.tmp";
	private static boolean file_created = false;
	private static Timer timer;

	public static Umlet getInstance() {
		if (_instance == null) {
			_instance = new Umlet();
		}
		return _instance;
	}

	public static String getCustomElementPath() {
		return Umlet.CUSTOM_ELEMENTS_PATH;
	}

	public static void displayError(String error) {
		JOptionPane.showMessageDialog(null,
				error,
				"ERROR",
				JOptionPane.ERROR_MESSAGE);
	}

	private static void printUsage() {
		String formats = "pdf|svg|eps";
		for (String format : ImageIO.getWriterFileSuffixes())
			formats += "|" + format;
		JOptionPane.showMessageDialog(null,
				"USAGE: -action=convert -format=(" + formats + ") -filename=inputfile.uxf [-output=outputfile[.extension]]",
				"USAGE",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static void doConvert(String fileName, String format, String outputfilename) {
		File file = new File(fileName);
		if (!file.exists()) {
			displayError("File '" + file.getAbsolutePath() + "' not found.");
			return;
		}
		DiagramHandler handler = new DiagramHandler(file);
		if (outputfilename == null) outputfilename = fileName.substring(0, fileName.indexOf(".uxf")) + "." + format;
		else if (!outputfilename.endsWith("." + format)) outputfilename += "." + format;

		try {
			if (format != null) handler.getFileHandler().doExportAs(format, outputfilename);
		} catch (Exception e) {
			printUsage();
		}
	}

	private static boolean alreadyRunningChecker(boolean force) {
		try {
			File f = new File(umlet_file);
			if (f.exists() && !force) return true;
			f.createNewFile();
			file_created = true;
			timer = new Timer(true);
			timer.schedule(new UmletRunningFileChecker(umlet_file, umlet_read_file), 0, 1000);
		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		}
		return false;
	}

	private static boolean sendFileNameToRunningApplication(String filename) {
		// send the filename per file to the running application
		File f1 = new File(umlet_file);
		boolean write_successful = true;
		try {
			PrintWriter writer = new PrintWriter(f1);
			writer.println(filename);
			writer.close();
		} catch (Exception ex) {
			write_successful = false;
		}
		try {
			Thread.sleep(2000);
		} catch (Exception ex) {}
		File f2 = new File(umlet_read_file);
		if (!f2.exists() || !write_successful) // if the ok file does not exist or the filename couldnt be written.
		{
			alreadyRunningChecker(true);
			return false;
		}
		else f2.delete();
		return true;
	}

	public static void main(String args[]) {

		System.setSecurityManager(new CustomElementSecurityManager());
		Umlet umlet = Umlet.getInstance();
		String tempPath, realPath;

		tempPath = umlet.getProtectionDomainPath();
		tempPath = tempPath.substring(0, tempPath.length() - 1);
		tempPath = tempPath.substring(0, tempPath.lastIndexOf('/') + 1);
		realPath = new File(tempPath).getAbsolutePath() + "/";
		umlet.setHomePath(realPath);

		if (args.length != 0) {
			String action = null;
			String format = null;
			String filename = null;
			String output = null;
			for (int i = 0; i < args.length; i++) {
				if (args[i].startsWith("-action=")) action = args[i].substring(8);
				else if (args[i].startsWith("-format=")) format = args[i].substring(8);
				else if (args[i].startsWith("-filename=")) filename = args[i].substring(10);
				else if (args[i].startsWith("-output=")) output = args[i].substring(8);
			}

			if ((action == null) && (format == null) && (filename != null)) {
				if (alreadyRunningChecker(false)) // start checker - if not checked - send file info
				{
					if (!sendFileNameToRunningApplication(filename)) {
						umlet.init(new StandaloneGUI(umlet));
						umlet.doOpen(filename);
					}
				}
				else {
					umlet.init(new StandaloneGUI(umlet));
					umlet.doOpen(filename);
				}
			}
			else if ((action != null) && (format != null) && (filename != null)) {
				if (action.equals("convert")) {
					doConvert(filename, format, output);
				}
				else printUsage();
			}
			else printUsage();
		}
		else { // no arguments specified
			alreadyRunningChecker(true); // start checker
			umlet.init(new StandaloneGUI(umlet));
			umlet.doNew();
		}
	}

	private UmletGUI gui;
	private Entity editedEntity;
	private Hashtable<String, PaletteHandler> palettes;
	private ArrayList<DiagramHandler> diagrams;
	private DiagramHandler currentdiagram;
	private String homepath;

	private Umlet() {
		this.diagrams = new ArrayList<DiagramHandler>();
		this.currentdiagram = null;
	}

	public void setHomePath(String homepath) {
		assert (this.homepath == null);
		this.homepath = homepath;
	}

	public void init(UmletGUI gui) {
		this.gui = gui;
		Config.loadConfig(Constants.configFilename); // only load config after gui is set (because of homepath!!)
		gui.initGUI(); // show gui
	}

	// sets the current diagram the user works with - that may be a palette too
	public void setCurrentDiagram(DiagramHandler handler) {
		this.currentdiagram = handler;
		this.gui.diagramSelected(handler);
	}

	public Hashtable<String, PaletteHandler> getPalettes() {
		if (this.palettes == null) {
			this.palettes = new Hashtable<String, PaletteHandler>();
			// scan palettes
			List<File> palettes = this.scanForPalettes();
			for (File palette : palettes)
				this.palettes.put(palette.getName(), new PaletteHandler(palette));
		}
		return this.palettes;
	}

	public List<File> scanForPalettes() {
		// scan palettes directory...
		FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		File[] paletteFiles = fileSystemView.getFiles(new File(this.getHomePath() + "palettes/"), false);
		List<File> palettes = new ArrayList<File>();
		for (File palette : paletteFiles) {
			if (palette.getName().endsWith(".uxf")) palettes.add(palette);
		}
		return palettes;
	}

	public List<String> getPaletteNames() {
		return this.getPaletteNames(this.getPalettes());
	}

	public List<String> getPaletteNames(Hashtable<String, PaletteHandler> palettes) {
		List<String> palettenames = new ArrayList<String>();
		for (String palette : palettes.keySet())
			palettenames.add(palette.substring(0, palette.length() - 4));
		Collections.sort(palettenames, new PaletteSorter());
		return palettenames;
	}

	public List<String> getTemplateNames() {
		ArrayList<String> templates = new ArrayList<String>();
		// scan palettes directory...
		FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		File[] templateFiles = fileSystemView.getFiles(new File(this.getHomePath() + Umlet.CUSTOM_ELEMENTS_PATH), false);
		for (File template : templateFiles) {
			if (template.getName().endsWith(".java")) templates.add(template.getName().substring(0, template.getName().length() - 5));
		}
		Collections.sort(templates, new TemplateSorter());
		return templates;
	}

	public List<DiagramHandler> getDiagrams() {
		return this.diagrams;
	}

	public String getHomePath() {
		return this.homepath;
	}

	public String getProtectionDomainPath() {
		String path;
		try {
			// We convert to an URI to avoid HTML problems with special characters like space,ä,ö,ü,...
			path = Umlet.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException e) {
			// If the conversion fails, we don't convert to an URI but we have to manually replace those special characters later
			// WARNING: Only space will be replaced here, other special characters like ä,ö,ü,... won't (add them if needed)
			path = Umlet.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("%20", " ");
		}
		return path;
	}

	public UmletGUI getGUI() {
		return this.gui;
	}

	public Entity getEditedEntity() {
		return editedEntity;
	}

	public String getPropertyString() {
		return this.gui.getPropertyPanelText();
	}

	public PaletteHandler getPalette() {
		String name = this.gui.getSelectedPalette();
		if (name != null) return this.getPalettes().get(name);
		return null;
	}

	public DrawPanel getPalettePanel() {
		return this.getPalette().getDrawPanel();
	}

	public void setPropertyPanelToCustomEntity(Entity e) {
		editedEntity = e;
	}

	public void setPropertyPanelToEntity(Entity e) {
		editedEntity = e;
		if (e != null) {
			this.gui.setPropertyPanelText(e.getPanelAttributes());
		}
		else {
			DiagramHandler handler = this.getDiagramHandler();
			if (handler == null) this.gui.setPropertyPanelText("");
			else this.gui.setPropertyPanelText(handler.getHelpText());
		}
	}

	// returns the current diagramhandler the user works with - may be a diagramhandler of a palette too
	public DiagramHandler getDiagramHandler() {
		return this.currentdiagram;
	}

	// ask for save for all diagrams (if umlet is closed)
	public boolean askSaveIfDirty() {
		boolean ok = true;
		for (DiagramHandler d : this.getDiagrams()) {
			if (!d.askSaveIfDirty()) ok = false;
		}

		if (!this.getGUI().getCurrentCustomHandler().closeEntity()) ok = false;
		return ok;
	}

	public void doNew() {
		closeEmptyNewTab();
		DiagramHandler diagram = new DiagramHandler(null);
		this.diagrams.add(diagram);
		this.gui.open(diagram);
		if (this.diagrams.size() == 1) this.setPropertyPanelToEntity(null);
	}

	public void doOpen() {
		String fn = DiagramFileHandler.chooseFileName();
		if (fn != null) this.doOpen(fn);
	}

	public void doOpen(String filename) {
		closeEmptyNewTab();
		File file = new File(filename);
		if (file == null) return;
		if (!file.exists()) return;
		DiagramHandler diagram = new DiagramHandler(file);
		this.diagrams.add(diagram);
		this.gui.open(diagram);
		if (this.diagrams.size() == 1) this.setPropertyPanelToEntity(null);
	}

	/**
	 * If the last diagram tab and it's undo history (=controller) is empty, we close
	 * this tab before opening a new one to avoid unnecessary new tabs
	 */
	private void closeEmptyNewTab() {
		if (!this.diagrams.isEmpty()) {
			DiagramHandler lastDiagram = this.diagrams.get(diagrams.size() - 1);
			if (lastDiagram.getController().isEmpty() && lastDiagram.getDrawPanel().getAllEntities().isEmpty()) {
				lastDiagram.doClose();
			}
		}
	}

	// called by UI when umlet is closed
	public void close() {
		Config.saveConfig();
		if (file_created) {
			timer.cancel();
			(new File(umlet_file)).delete();
		}
	}
}
