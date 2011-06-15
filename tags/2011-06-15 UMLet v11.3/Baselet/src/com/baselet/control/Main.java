package com.baselet.control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileSystemView;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.ProgramName;
import com.baselet.control.Constants.RuntimeType;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.PaletteHandler;
import com.baselet.diagram.io.DiagramFileHandler;
import com.baselet.element.GridElement;
import com.baselet.gui.BaseGUI;
import com.baselet.gui.standalone.StandaloneGUI;

public class Main {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	private static Main instance;

	private static String tmp_file;
	private static String tmp_read_file;
	private static boolean file_created = false;
	private static Timer timer;

	private BaseGUI gui;
	private GridElement editedGridElement;
	private Hashtable<String, PaletteHandler> palettes;
	private ArrayList<DiagramHandler> diagrams = new ArrayList<DiagramHandler>();
	private DiagramHandler currentDiagramHandler;
	private DiagramHandler currentInfoDiagramHandler;
	private ClassLoader classLoader;

	private List<String> recentFiles = new ArrayList<String>();

	public static Main getInstance() {
		if (instance == null) {
			instance = new Main();
		}
		return instance;
	}

	public static void main(String args[]) {

//		System.setSecurityManager(new CustomElementSecurityManager());

		Main main = Main.getInstance();
		main.initOverallSettings();
		tmp_file = Program.PROGRAM_NAME.toLowerCase() + ".tmp";
		tmp_read_file = Program.PROGRAM_NAME.toLowerCase() + "_1.tmp";

		try {
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
				// Program started by double-click on diagram file
				if ((action == null) && (format == null) && (filename != null)) {
					if (!alreadyRunningChecker(false) || !sendFileNameToRunningApplication(filename)) {
						main.init(new StandaloneGUI(main));
						main.doOpen(filename);
					}
				}
				else if ((action != null) && (format != null) && (filename != null)) {
					if (action.equals("convert")) {
						Program.RUNTIME_TYPE = RuntimeType.BATCH;
						doConvert(filename, format, output);
					}
					else printUsage();
				}
				else printUsage();
			}
			else { // no arguments specified
				alreadyRunningChecker(true); // start checker
				main.init(new StandaloneGUI(main));
				main.doNew();
			}
		} catch (Exception e) {
			log.error("Initialization or uncaught outer Exception", e);
		}
	}

	public void init(BaseGUI gui) throws Exception {
		this.gui = gui;
		Config.loadConfig(); // only load config after gui is set (because of homepath)
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE); // Tooltips should not hide after some time
		gui.initGUI(); // show gui
	}

	public void initOverallSettings() {
		readManifestInfo();
		initLogger();
	}

	private void initLogger() {
		String log4jFilePath = Path.homeProgram() + "log4j.properties";
		try {
			// If no log4j.properties file exists, we create a simple one
			if (!new File(log4jFilePath).exists()) {
				log4jFilePath = Path.temp() + Program.PROGRAM_NAME.toLowerCase() + "_log4j.properties";
				File tempLog4jFile = new File(log4jFilePath);
				tempLog4jFile.deleteOnExit();
				Writer writer = new BufferedWriter(new FileWriter(tempLog4jFile));
				writer.write(
						"log4j.rootLogger=ERROR, SYSTEM_OUT\n" +
						"log4j.appender.SYSTEM_OUT=org.apache.log4j.ConsoleAppender\n" + 
						"log4j.appender.SYSTEM_OUT.layout=org.apache.log4j.PatternLayout\n" + 
				"log4j.appender.SYSTEM_OUT.layout.ConversionPattern=%6r | %-5p | %-30c - \"%m\"%n\n");
				writer.close();
			}
			Properties props = new Properties();
			props.put("PROJECT_PATH", Path.homeProgram()); // Put homepath as relative variable in properties file
			props.load(new FileInputStream(log4jFilePath));
			PropertyConfigurator.configure(props);
			log.info("Logger configuration initialized");
		} catch (IOException e) {
			log.error("Initialization of log4j.properties failed", e);
		}
	}

	private void readManifestInfo() {
		try {
			Manifest manifest;
			if (Path.executable().endsWith(".jar")) manifest = new JarFile(Path.executable()).getManifest();
			else manifest = new Manifest(new FileInputStream(Path.homeProgram() + "META-INF" + File.separator + "MANIFEST.MF"));

			Attributes attributes = manifest.getMainAttributes();
			String versionString = attributes.getValue("Bundle-Version");
			String progNameString = attributes.getValue("Bundle-Name");
			ProgramName programName = progNameString.equals("Umlet") ? ProgramName.UMLET : ProgramName.PLOTLET;
			Program.init(programName, versionString);

		} catch (Exception e) {
			log.error(null, e);
		}
	}

	public static void displayError(String error) {
		JOptionPane.showMessageDialog(null, error, "ERROR", JOptionPane.ERROR_MESSAGE);
	}

	private static void printToConsole(String text) {
		System.out.println(text);
	}

	private static void printUsage() {
		String formats = "pdf|svg|eps";
		for (String format : ImageIO.getWriterFileSuffixes())
			formats += "|" + format;
		printToConsole("USAGE: -action=convert -format=(" + formats + ") -filename=inputfile." + Program.EXTENSION + " [-output=outputfile[.extension]]");
	}

	public static void doConvert(String fileName, String format, String outputfilename) {
		File file = new File(fileName);
		if (!file.exists()) {
			printToConsole("File '" + file.getAbsolutePath() + "' not found.");
			return;
		}
		DiagramHandler handler = new DiagramHandler(file);
		if (outputfilename == null) {
			if (fileName.contains("." + Program.EXTENSION)) fileName = fileName.substring(0, fileName.indexOf("." + Program.EXTENSION));
			outputfilename = fileName + "." + format;
		}
		else if (!outputfilename.endsWith("." + format)) outputfilename += "." + format;

		try {
			if (format != null) handler.getFileHandler().doExportAs(format, new File(outputfilename));
		} catch (Exception e) {
			printUsage();
		}
	}

	private static boolean alreadyRunningChecker(boolean force) {
		try {
			File f = new File(Path.temp() + tmp_file);
			if (f.exists() && !force) return true;
			f.createNewFile();
			file_created = true;
			timer = new Timer(true);
			timer.schedule(new RunningFileChecker(Path.temp() + tmp_file, Path.temp() + tmp_read_file), 0, 1000);
		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		}
		return false;
	}

	private static boolean sendFileNameToRunningApplication(String filename) {
		// send the filename per file to the running application
		File f1 = new File(Path.temp() + tmp_file);
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
		File f2 = new File(Path.temp() + tmp_read_file);
		if (!f2.exists() || !write_successful) // if the ok file does not exist or the filename couldnt be written.
		{
			alreadyRunningChecker(true);
			return false;
		}
		else f2.delete();
		return true;
	}

	// sets the current diagram the user works with - that may be a palette too
	public void setCurrentDiagramHandler(DiagramHandler handler) {
		this.setCurrentInfoDiagramHandler(handler);
		this.currentDiagramHandler = handler;
		if (gui != null) gui.diagramSelected(handler);
	}

	public DiagramHandler getCurrentInfoDiagramHandler() {
		return this.currentInfoDiagramHandler;
	}

	public void setCurrentInfoDiagramHandler(DiagramHandler handler) {
		log.debug("trying to setCurrentInfoDiagram");
		log.debug("this.currentdiagram::");
		log.debug(this.currentInfoDiagramHandler);
		log.debug("handler::");
		log.debug(handler);
		if ((!(handler instanceof PaletteHandler)) && (handler != null)) {
			log.debug("SETTING currentInfoDiagram");
			this.currentInfoDiagramHandler = handler;
		}
	}

	public void setPropertyPanelToCustomElement(GridElement e) {
		editedGridElement = e;
	}

	public void setPropertyPanelToGridElement(final GridElement e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setPropertyPanelToGridElementHelper(e);
			}
		});
	}

	private void setPropertyPanelToGridElementHelper(GridElement e) {
		editedGridElement = e;
		if (e != null) this.gui.setPropertyPanelText(e.getPanelAttributes());
		else {			
			DiagramHandler handler = this.getDiagramHandler();
			if (handler == null) this.gui.setPropertyPanelText("");
			else this.gui.setPropertyPanelText(handler.getHelpText());
		}
	}

	public GridElement getGridElementFromPath(String path) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (classLoader == null) classLoader = Thread.currentThread().getContextClassLoader(); // use classloader of current thread (not systemclassloader - important for eclipse)
		Class<?> foundClass = null;
		String[] possiblePackages = new String[] {"com.umlet.element", "com.umlet.element.custom", "com.plotlet.element", "com.baselet.element"};
		try {
			foundClass = classLoader.loadClass(path);
		}
		catch (ClassNotFoundException e) {
			String className = path.substring(path.lastIndexOf("."));
			for (String possPackage : possiblePackages) {
				try {
					foundClass = classLoader.loadClass(possPackage + className);
					break;
				} catch (ClassNotFoundException e1) {/*do nothing; try next package*/}
			}
		}
		if (foundClass == null) {
			ClassNotFoundException ex = new ClassNotFoundException("class " + path + " not found");
			log.error(null, ex);
			throw ex;
		}
		else return (GridElement) foundClass.newInstance();
	}

	public void doNew() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				doNewHelper();
			}
		});
	}

	private void doNewHelper() {
		if (lastTabIsEmpty()) return; // If the last tab is empty do nothing (it's already new)
		DiagramHandler diagram = new DiagramHandler(null);
		this.diagrams.add(diagram);
		this.gui.open(diagram);
		if (this.diagrams.size() == 1) this.setPropertyPanelToGridElement(null);
	}

	public void doOpen() {
		String fn = DiagramFileHandler.chooseFileName();
		if (fn != null) this.doOpen(fn);
	}

	public void doOpen(final String filename) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				doOpenHelper(filename);
			}
		});
	}

	private void doOpenHelper(String filename) {
		if (lastTabIsEmpty()) (diagrams.get(diagrams.size() - 1)).doClose(); // If the last tab is empty close it (the opened diagram replaces the new one)
		File file = new File(filename);
		if (!file.exists()) return;
		DiagramHandler diagram = new DiagramHandler(file);
		this.diagrams.add(diagram);
		this.gui.open(diagram);
		if (this.diagrams.size() == 1) this.setPropertyPanelToGridElement(null);
		if (recentFiles.contains(filename)) recentFiles.remove(filename);
		recentFiles.add(0, filename);
		int maxRecentFiles = 10;
		if (recentFiles.size() > maxRecentFiles) recentFiles.remove(maxRecentFiles);
	}

	/**
	 * If the last diagram tab and it's undo history (=controller) is empty return true, else return false
	 */
	private boolean lastTabIsEmpty() {
		if (!this.diagrams.isEmpty()) {
			DiagramHandler lastDiagram = this.diagrams.get(diagrams.size() - 1);
			if (lastDiagram.getController().isEmpty() && lastDiagram.getDrawPanel().getAllEntities().isEmpty()) { return true; }
		}
		return false;
	}

	// ask for save for all diagrams (if main is closed)
	public boolean askSaveIfDirty() {
		boolean ok = true;
		for (DiagramHandler d : this.getDiagrams()) {
			if (!d.askSaveIfDirty()) ok = false;
		}

		if (!this.getGUI().getCurrentCustomHandler().closeEntity()) ok = false;
		return ok;
	}

	// called by UI when main is closed
	public void closeProgram() {
		Config.saveConfig();
		if (file_created) {
			timer.cancel();
			(new File(Path.temp() + tmp_file)).delete();
		}
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
		File[] paletteFiles = fileSystemView.getFiles(new File(Path.homeProgram() + "palettes/"), false);
		List<File> palettes = new ArrayList<File>();
		for (File palette : paletteFiles) {
			if (palette.getName().endsWith("." + Program.EXTENSION)) palettes.add(palette);
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
		File[] templateFiles = fileSystemView.getFiles(new File(Path.customElements()), false);
		for (File template : templateFiles) {
			if (template.getName().endsWith(".java")) templates.add(template.getName().substring(0, template.getName().length() - 5));
		}
		Collections.sort(templates, new TemplateSorter());
		return templates;
	}

	public List<DiagramHandler> getDiagrams() {
		return this.diagrams;
	}

	public BaseGUI getGUI() {
		return this.gui;
	}

	public GridElement getEditedGridElement() {
		return editedGridElement;
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

	// returns the current diagramhandler the user works with - may be a diagramhandler of a palette too
	public DiagramHandler getDiagramHandler() {
		return this.currentDiagramHandler;
	}

	public List<String> getRecentFiles() {
		return recentFiles;
	}

}
