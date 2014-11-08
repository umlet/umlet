package com.baselet.control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TreeMap;
import java.util.jar.Attributes;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.baselet.control.config.Config;
import com.baselet.control.config.ConfigConst;
import com.baselet.control.config.handler.ConfigHandler;
import com.baselet.control.constants.Constants;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.control.util.CanOpenDiagram;
import com.baselet.control.util.Path;
import com.baselet.control.util.RecentlyUsedFilesList;
import com.baselet.control.util.RunningFileChecker;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.Notifier;
import com.baselet.diagram.PaletteHandler;
import com.baselet.diagram.UpdateCheckTimerTask;
import com.baselet.diagram.io.OpenFileChooser;
import com.baselet.element.GridElement;
import com.baselet.gui.BaseGUI;
import com.baselet.gui.CurrentGui;
import com.baselet.gui.pane.OwnSyntaxPane;
import com.baselet.gui.standalone.StandaloneGUI;

public class Main implements CanCloseProgram, CanOpenDiagram {

	private static final Logger log = Logger.getLogger(Main.class);

	private static Main main = new Main();

	private static String tmp_file;
	private static String tmp_read_file;
	private static boolean file_created = false;

	private GridElement editedGridElement;
	private TreeMap<String, PaletteHandler> palettes;
	private final ArrayList<DiagramHandler> diagrams = new ArrayList<DiagramHandler>();
	private ClassLoader classLoader;

	public static Main getInstance() {
		return main;
	}

	public static void main(final String[] args) {
		initHomeProgramPath();
		main.initLogger();
		main.readManifestInfo();
		ConfigHandler.loadConfig();
		tmp_file = Program.NAME.toLowerCase() + ".tmp";
		tmp_read_file = Program.NAME.toLowerCase() + "_1.tmp";

		if (args.length != 0) {
			String action = null;
			String format = null;
			String filename = null;
			String output = null;
			for (String arg : args) {
				if (arg.startsWith("-action=")) {
					action = arg.substring(8);
				}
				else if (arg.startsWith("-format=")) {
					format = arg.substring(8);
				}
				else if (arg.startsWith("-filename=")) {
					filename = arg.substring(10);
				}
				else if (arg.startsWith("-output=")) {
					output = arg.substring(8);
				}
			}
			// Program started by double-click on diagram file (either diagram filename is passed without prefix or with -filename=... prefix)
			if (action == null && format == null && (filename != null || args.length == 1)) {
				if (filename == null) {
					filename = args[0];
				}
				if (!alreadyRunningChecker(false) || !sendFileNameToRunningApplication(filename)) {
					main.init(new StandaloneGUI(main));
					main.doOpen(filename);
				}
			}
			else if (action != null && format != null && filename != null) {
				if (action.equals("convert")) {
					Program.RUNTIME_TYPE = RuntimeType.BATCH;
					String[] splitFilename = filename.split("(/|\\\\)");
					String localName = splitFilename[splitFilename.length - 1];
					String dir = filename.substring(0, filename.length() - localName.length());
					if (dir.isEmpty()) {
						dir = ".";
					}
					FileFilter fileFilter = new WildcardFileFilter(localName);
					File[] files = new File(dir).listFiles(fileFilter);
					if (files != null) {
						for (File file : files) {
							doConvert(file, format, output);
						}
					}
				}
				else {
					printUsage();
				}
			}
			else {
				printUsage();
			}
		}
		else { // no arguments specified
			alreadyRunningChecker(true); // start checker
			if (ConfigConst.checkForUpdates) {
				new Timer("Update Checker", true).schedule(new UpdateCheckTimerTask(), 0);
			}
			main.init(new StandaloneGUI(main));
			main.doNew();
		}
	}

	private static void initHomeProgramPath() {
		String tempPath, realPath;
		tempPath = Path.executable();
		tempPath = tempPath.substring(0, tempPath.length() - 1);
		tempPath = tempPath.substring(0, tempPath.lastIndexOf('/') + 1);
		realPath = new File(tempPath).getAbsolutePath() + "/";
		Path.setHomeProgram(realPath);
	}

	public void init(BaseGUI gui) {
		CurrentGui.getInstance().setGui(gui);
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE); // Tooltips should not hide after some time
		gui.initGUI(); // show gui
	}

	public void initLogger() {
		String log4jFilePath = Path.homeProgram() + Constants.LOG4J_PROPERTIES;
		try {
			// If no log4j.properties file exists, we create a simple one
			if (!new File(log4jFilePath).exists()) {
				File tempLog4jFile = File.createTempFile(Constants.LOG4J_PROPERTIES, null);
				tempLog4jFile.deleteOnExit();
				log4jFilePath = tempLog4jFile.getAbsolutePath();
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
			FileInputStream inStream = new FileInputStream(log4jFilePath);
			props.load(inStream);
			inStream.close();
			PropertyConfigurator.configure(props);
			log.info("Logger configuration initialized");
		} catch (Exception e) {
			System.err.println("Initialization of " + Constants.LOG4J_PROPERTIES + " failed:");
			e.printStackTrace();
		}
	}

	private void readManifestInfo() {
		try {
			Attributes attributes = Path.manifest().getMainAttributes();
			Program.init(attributes.getValue(Constants.MANIFEST_BUNDLE_VERSION));
		} catch (Exception e) {
			// log.error(null, e);
			e.printStackTrace(); // Logger is not initialized here
		}
	}

	public void displayError(String error) {
		JOptionPane.showMessageDialog(CurrentGui.getInstance().getGui().getMainFrame(), error, "ERROR", JOptionPane.ERROR_MESSAGE);
	}

	private static void printToConsole(String text) {
		System.out.println(text);
	}

	private static void printUsage() {
		String formats = "pdf|svg|eps";
		for (String format : ImageIO.getWriterFileSuffixes()) {
			formats += "|" + format;
		}
		printToConsole("USAGE: -action=convert -format=(" + formats + ") -filename=inputfile." + Program.EXTENSION + " [-output=outputfile[.extension]]");
	}

	public static void doConvert(File inputFile, String outputFormat, String outputParam) {
		if (!inputFile.exists()) {
			printToConsole("File '" + inputFile.getAbsolutePath() + "' not found.");
			return;
		}
		DiagramHandler handler = new DiagramHandler(inputFile);

		String outputFileName = determineOutputName(inputFile, outputFormat, outputParam);

		try {
			handler.getFileHandler().doExportAs(outputFormat, new File(outputFileName));
			printToConsole("Conversion finished: " + inputFile.getAbsolutePath());
		} catch (Exception e) {
			printToConsole(e.getMessage());
		}
	}

	private static String determineOutputName(File inputFile, String outputFormat, String outputParam) {
		String outputFileName;
		if (outputParam == null) {
			outputFileName = inputFile.getAbsolutePath();
		}
		else if (new File(outputParam).isDirectory()) { // if outputdir already exists
			outputFileName = outputParam + File.separator + inputFile.getName();
		}
		else {
			outputFileName = outputParam;
		}
		return createBatchOutputName(outputFormat, outputFileName);
	}

	private static String createBatchOutputName(String extension, String fileName) {
		if (fileName.endsWith(extension)) {
			return fileName;
		}
		else {
			return fileName + "." + extension;
		}
	}

	private static boolean alreadyRunningChecker(boolean force) {
		try {
			File f = new File(Path.temp() + tmp_file);
			if (f.exists() && !force) {
				return true;
			}
			f.createNewFile();
			file_created = true;
			new Timer("alreadyRunningChecker", true).schedule(new RunningFileChecker(Path.temp() + tmp_file, Path.temp() + tmp_read_file, main), 0, 1000);
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
		} catch (InterruptedException ex) {/* no special handling */}
		File f2 = new File(Path.temp() + tmp_read_file);
		if (!f2.exists() || !write_successful) // if the ok file does not exist or the filename couldnt be written.
		{
			alreadyRunningChecker(true);
			return false;
		}
		else {
			f2.delete();
		}
		return true;
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
		OwnSyntaxPane propertyPane = CurrentGui.getInstance().getGui().getPropertyPane();
		if (e != null) {
			propertyPane.switchToElement(e);
		}
		else {
			DiagramHandler handler = CurrentDiagram.getInstance().getDiagramHandler();
			if (handler == null) {
				propertyPane.switchToNonElement("");
			}
			else {
				propertyPane.switchToNonElement(handler.getHelpText());
			}
		}
	}

	public GridElement getGridElementFromPath(String path) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (classLoader == null)
		{
			classLoader = Thread.currentThread().getContextClassLoader(); // use classloader of current thread (not systemclassloader - important for eclipse)
		}
		Class<?> foundClass = null;
		String[] possiblePackages = new String[] { "com.umlet.element", "com.umlet.element.custom", "com.plotlet.element", "com.baselet.element" };
		try {
			foundClass = classLoader.loadClass(path);
		} catch (ClassNotFoundException e) {
			String className = path.substring(path.lastIndexOf("."));
			for (String possPackage : possiblePackages) {
				try {
					foundClass = classLoader.loadClass(possPackage + className);
					break;
				} catch (ClassNotFoundException e1) {/* do nothing; try next package */}
			}
		}
		if (foundClass == null) {
			ClassNotFoundException ex = new ClassNotFoundException("class " + path + " not found");
			log.error(null, ex);
			throw ex;
		}
		else {
			return (GridElement) foundClass.newInstance();
		}
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
		if (lastTabIsEmpty())
		{
			return; // If the last tab is empty do nothing (it's already new)
		}
		DiagramHandler diagram = new DiagramHandler(null);
		diagrams.add(diagram);
		CurrentGui.getInstance().getGui().open(diagram);
		if (diagrams.size() == 1) {
			setPropertyPanelToGridElement(null);
		}
	}

	public void doOpenFromFileChooser() {
		List<String> files = new OpenFileChooser().getFilesToOpen(CurrentGui.getInstance().getGui().getMainFrame());
		for (String file : files) {
			doOpen(file);
		}
	}

	@Override
	public void doOpen(final String filename) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				doOpenHelper(filename);
			}
		});
	}

	private void doOpenHelper(String filename) {
		File file = new File(filename);
		if (!file.exists()) {
			Notifier.getInstance().showNotification(filename + " does not exist");
			return;
		}
		Config.getInstance().setOpenFileHome(file.getParent());
		DiagramHandler handler = getDiagramHandlerForFile(filename);
		if (handler != null) { // File is already opened -> jump to the tab
			CurrentGui.getInstance().getGui().jumpTo(handler);
			Notifier.getInstance().showNotification("switched to " + filename);
		}
		else {
			if (lastTabIsEmpty())
			{
				diagrams.get(diagrams.size() - 1).doClose(); // If the last tab is empty close it (the opened diagram replaces the new one)
			}
			editedGridElement = null; // must be set to null here, otherwise the change listener of the property panel will change element text to help_text of diagram (see google code Issue 174)
			DiagramHandler diagram = new DiagramHandler(file);
			diagrams.add(diagram);
			CurrentGui.getInstance().getGui().open(diagram);
			if (diagrams.size() == 1) {
				setPropertyPanelToGridElement(null);
			}
			RecentlyUsedFilesList.getInstance().add(filename);
			Notifier.getInstance().showNotification(filename + " opened");
		}
	}

	private DiagramHandler getDiagramHandlerForFile(String file) {
		for (DiagramHandler d : diagrams) {
			if (d.getFullPathName().equalsIgnoreCase(file)) {
				return d;
			}
		}
		return null;
	}

	/**
	 * If the last diagram tab and it's undo history (=controller) is empty return true, else return false
	 */
	private boolean lastTabIsEmpty() {
		if (!diagrams.isEmpty()) {
			DiagramHandler lastDiagram = diagrams.get(diagrams.size() - 1);
			if (lastDiagram.getController().isEmpty() && lastDiagram.getDrawPanel().getGridElements().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * called by UI when main is closed
	 */
	@Override
	public void closeProgram() {
		ConfigHandler.saveConfig(CurrentGui.getInstance().getGui());
		if (file_created) {
			new File(Path.temp() + tmp_file).delete();
		}
	}

	public TreeMap<String, PaletteHandler> getPalettes() {
		if (palettes == null) {
			palettes = new TreeMap<String, PaletteHandler>(Constants.DEFAULT_FIRST_COMPARATOR);
			// scan palettes
			List<File> palettes = scanForPalettes();
			for (File palette : palettes) {
				this.palettes.put(getFilenameWithoutExtension(palette), new PaletteHandler(palette));
			}
		}
		return palettes;
	}

	private String getFilenameWithoutExtension(File file) {
		return file.getName().substring(0, file.getName().indexOf("."));
	}

	private List<File> scanForPalettes() {
		// scan palettes directory...
		FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		File[] paletteFiles = fileSystemView.getFiles(new File(Path.homeProgram() + "palettes/"), false);
		List<File> palettes = new ArrayList<File>();
		for (File palette : paletteFiles) {
			if (palette.getName().endsWith("." + Program.EXTENSION)) {
				palettes.add(palette);
			}
		}
		return palettes;
	}

	public List<String> getTemplateNames() {
		ArrayList<String> templates = new ArrayList<String>();
		// scan palettes directory...
		FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		File[] templateFiles = fileSystemView.getFiles(new File(Path.customElements()), false);
		for (File template : templateFiles) {
			if (template.getName().endsWith(".java")) {
				templates.add(template.getName().substring(0, template.getName().length() - 5));
			}
		}
		Collections.sort(templates, Constants.DEFAULT_FIRST_COMPARATOR);
		return templates;
	}

	public List<DiagramHandler> getDiagrams() {
		return diagrams;
	}

	public Collection<DiagramHandler> getDiagramsAndPalettes() {
		List<DiagramHandler> returnList = new ArrayList<DiagramHandler>(getDiagrams());
		returnList.addAll(getPalettes().values());
		return returnList;
	}

	public GridElement getEditedGridElement() {
		return editedGridElement;
	}

	public PaletteHandler getPalette() {
		String name = CurrentGui.getInstance().getGui().getSelectedPalette();
		if (name != null) {
			return getPalettes().get(name);
		}
		return null;
	}

	public DrawPanel getPalettePanel() {
		return getPalette().getDrawPanel();
	}

	/**
	 * Workaround to avoid storing the handler directly in the GridElement
	 * (necessary as a first step in the direction of GridElements which do not know where they are painted)
	 */
	private static HashMap<GridElement, DiagramHandler> gridElementToHandlerMapping = new HashMap<GridElement, DiagramHandler>();

	public static DiagramHandler getHandlerForElement(GridElement element) {
		return gridElementToHandlerMapping.get(element);
	}

	public static DiagramHandler setHandlerForElement(GridElement element, DiagramHandler handler) {
		return gridElementToHandlerMapping.put(element, handler);
	}

}
