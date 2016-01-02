package com.baselet.standalone;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Timer;

import javax.imageio.ImageIO;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.Main;
import com.baselet.control.config.Config;
import com.baselet.control.config.handler.ConfigHandler;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.control.util.Path;
import com.baselet.control.util.RunningFileChecker;
import com.baselet.control.util.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.UpdateCheckTimerTask;
import com.baselet.standalone.gui.StandaloneGUI;

public class MainStandalone {

	private static final Logger log = LoggerFactory.getLogger(MainStandalone.class);

	public static void main(final String[] args) {
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
					initAll(RuntimeType.STANDALONE);
					startStandalone(filename);
				}
			}
			else if (action != null && format != null && filename != null) {
				if (action.equals("convert")) {
					initAll(RuntimeType.BATCH);
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
							log.info("Converting file " + file.getAbsolutePath());
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
			initAll(RuntimeType.STANDALONE);
			alreadyRunningChecker(true); // start checker
			startStandalone(null);
		}
	}

	private static void initAll(RuntimeType runtime) {
		readBuildInfoAndInitVersion(runtime);
		initHomeProgramPath();
		ConfigHandler.loadConfig();
	}

	private static void startStandalone(String filenameToOpen) {
		if (Config.getInstance().isCheckForUpdates()) {
			new Timer("Update Checker", true).schedule(UpdateCheckTimerTask.getInstance(), 0);
		}
		Main.getInstance().init(new StandaloneGUI(Main.getInstance(), tmpFile()));
		if (filenameToOpen == null) {
			Main.getInstance().doNew();
		}
		else {
			Main.getInstance().doOpen(filenameToOpen);
		}
	}

	static void doConvert(File inputFile, String outputFormat, String outputParam) {
		if (!inputFile.exists()) {
			printToConsole("File '" + inputFile.getAbsolutePath() + "' not found.");
			return;
		}
		DiagramHandler handler = new DiagramHandler(inputFile);

		String outputFileName = determineOutputName(inputFile, outputFormat, outputParam);

		try {
			handler.getFileHandler().doExportAs(outputFormat, new File(outputFileName));
			printToConsole("Conversion finished: \"" + inputFile.getAbsolutePath() + "\" to \"" + outputFileName + "\"");
		} catch (Exception e) {
			printToConsole(e.getMessage());
		}
	}

	private static void printToConsole(String text) {
		System.out.println(text);
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

	private static void initHomeProgramPath() {
		String tempPath, realPath;
		tempPath = Path.executable();
		tempPath = tempPath.substring(0, tempPath.length() - 1);
		tempPath = tempPath.substring(0, tempPath.lastIndexOf('/') + 1);
		if (tempPath.endsWith("/lib/")) {
			tempPath = tempPath.substring(0, tempPath.length() - "lib/".length());
		}
		realPath = new File(tempPath).getAbsolutePath() + "/";
		Path.setHomeProgram(realPath);
	}

	private static boolean sendFileNameToRunningApplication(String filename) {
		// send the filename per file to the running application
		File f1 = tmpFile();
		try {
			PrintWriter writer = new PrintWriter(f1);
			writer.println(filename);
			writer.close();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	private static boolean alreadyRunningChecker(boolean force) {
		try {
			File f = tmpFile();
			if (f.exists() && !force) {
				return true;
			}
			Utils.safeCreateFile(f, false);
			new Timer("alreadyRunningChecker", true).schedule(new RunningFileChecker(tmpFile(), Main.getInstance()), 0, 1000);
		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		}
		return false;
	}

	private static File tmpFile() {
		return new File(Path.temp() + Program.getInstance().getProgramName().toLowerCase() + ".tmp");
	}

	private static void readBuildInfoAndInitVersion(RuntimeType runtime) {
		InputStream stream = MainStandalone.class.getResourceAsStream("/BuildInfo.properties");
		if (stream == null) {
			throw new RuntimeException("Cannot load BuildInfo.properties");
		}
		else {
			Properties prop = new Properties();
			try {
				prop.load(stream);
				Program.init(prop.getProperty("version"), runtime);
			} catch (IOException e) {
				throw new RuntimeException("Cannot load properties from file BuildInfo.properties", e);
			}
		}
	}

	private static void printUsage() {
		StringBuilder formatBuilder = new StringBuilder("pdf|svg|eps");
		for (String format : ImageIO.getWriterFileSuffixes()) {
			formatBuilder.append("|").append(format);
		}
		printToConsole("USAGE: -action=convert -format=(" + formatBuilder.toString() + ") -filename=inputfile." + Program.getInstance().getExtension() + " [-output=outputfile[.extension]]");
	}

}
