package com.baselet.standalone;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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
import com.baselet.control.util.Utils.BuildInfo;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.UpdateCheckTimerTask;
import com.baselet.generator.ClassDiagramConverter;
import com.baselet.standalone.gui.StandaloneGUI;

public class MainStandalone {

	private static final Logger log = LoggerFactory.getLogger(MainStandalone.class);

	public static void main(final String[] args) {
		// #369 Before anything else make sure that OSX handles cmd+Q as expected (see #369 and https://stackoverflow.com/questions/2061194/swing-on-osx-how-to-trap-command-q/2061318#2061318)
		System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");

		if (args.length != 0) {
			String actionArg = null;
			String formatArg = null;
			String filenameArg = null;
			String outputArg = null;
			for (String arg : args) {
				if (arg.equals("-help") || arg.equals("-usage")) {
					initAndPrintUsage();
					return;
				}
				else if (arg.startsWith("-action=")) {
					actionArg = arg.substring(8);
				}
				else if (arg.startsWith("-format=")) {
					formatArg = arg.substring(8);
				}
				else if (arg.startsWith("-filename=")) {
					filenameArg = arg.substring(10);
				}
				else if (arg.startsWith("-output=")) {
					outputArg = arg.substring(8);
				}
			}
			// Program started by double-click on diagram file (either diagram filename is passed without prefix or with -filename=... prefix)
			if (actionArg == null && formatArg == null && (filenameArg != null || args.length == 1)) {
				if (filenameArg == null) {
					filenameArg = args[0];
				}
				initAll(RuntimeType.STANDALONE);
				if (!alreadyRunningChecker(false) || !sendFileNameToRunningApplication(filenameArg)) {
					startStandalone(filenameArg);
				}
			}
			else if (actionArg != null && formatArg != null && filenameArg != null) {
				if (actionArg.equals("convert")) {
					initAll(RuntimeType.BATCH);
					String[] splitFilename = filenameArg.split("(/|\\\\)");
					String localName = splitFilename[splitFilename.length - 1];
					String dir = filenameArg.substring(0, filenameArg.length() - localName.length());
					if (dir.isEmpty()) {
						dir = ".";
					}
					FileFilter fileFilter = new WildcardFileFilter(localName);
					File[] files = new File(dir).listFiles(fileFilter);
					if (files != null) {
						for (File file : files) {
							log.info("Converting file " + file.getAbsolutePath());
							doConvert(file, formatArg, outputArg);
						}
					}
				}
				else {
					initAndPrintUsage();
				}
			}
			else if (actionArg != null && filenameArg != null && outputArg != null) {
				if (actionArg.equals("generate")) {
					initAll(RuntimeType.BATCH);

					String[] outputPathNameSplitted = outputArg.split("(/|\\\\)");
					String outputFileName = outputPathNameSplitted[outputPathNameSplitted.length - 1];
					String outputDirName = outputArg.substring(0, outputArg.length() - outputFileName.length());

					File outputPath = new File(outputDirName + '\\' + outputFileName);
					File outputDir = new File(outputDirName);
					if (!outputDir.exists()) {
						if (outputDir.mkdirs()) {
							log.debug("created output dir");
						}
					}

					List<File> inputPaths = new ArrayList<File>();
					String[] inputPathNames = filenameArg.split(",");
					for (String inputpathName : inputPathNames) {

						String[] inputpathNameSplitted = inputpathName.split("(/|\\\\)");
						String inputFileName = inputpathNameSplitted[inputpathNameSplitted.length - 1];
						String inputDirName = inputpathName.substring(0, inputpathName.length() - inputFileName.length());
						if (inputDirName.isEmpty()) {
							inputDirName = ".";
						}

						FileFilter fileFilter = new WildcardFileFilter(inputFileName);
						File[] subInputPaths = new File(inputDirName).listFiles(fileFilter);
						if (subInputPaths != null) {
							for (File subInputPath : subInputPaths) {
								inputPaths.add(subInputPath);
							}
						}
					}

					doGenerate(inputPaths, outputPath);
				}
				else {
					initAndPrintUsage();
				}
			}
			else {
				initAndPrintUsage();
			}
		}
		else { // no arguments specified
			initAll(RuntimeType.STANDALONE);
			alreadyRunningChecker(true); // start checker
			startStandalone(null);
		}
	}

	private static void initAndPrintUsage() {
		readBuildInfoAndInitVersion(RuntimeType.BATCH);
		printUsage();
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
			e.printStackTrace();
		}
	}

	static void doGenerate(List<File> inputFiles, File outputFile) {
		List<String> validInputFileNames = new ArrayList<String>();
		for (File inputFile : inputFiles) {
			if (!inputFile.exists()) {
				printToConsole("File '" + inputFile.getAbsolutePath() + "' not found.");
			}
			else {
				validInputFileNames.add(inputFile.getAbsolutePath());
			}
		}

		try {
			FileOutputStream fos = null;
			try {
				String program = Program.getInstance().getProgramName().toLowerCase();
				String version = String.valueOf(Program.getInstance().getVersion());
				String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><diagram program=\"" + program + "\" version=\"" + version + "\"></diagram>";

				fos = new FileOutputStream(outputFile);
				fos.write(content.getBytes("UTF-8"));
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				if (fos != null) {
					fos.close();
				}
				return;
			}

			DiagramHandler handler = new DiagramHandler(outputFile);
			CurrentDiagram.getInstance().setCurrentDiagramHandler(handler);
			new ClassDiagramConverter().createClassDiagrams(validInputFileNames);
			handler.doSave();

			printToConsole("Generation finished: \"" + validInputFileNames + "\" to \"" + outputFile.getAbsolutePath() + "\"");
		} catch (Exception e) {
			e.printStackTrace();
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
		String uxf = "." + Program.getInstance().getExtension();
		if (fileName.endsWith(uxf)) { // #451: remove uxf suffix before adding the new extension
			fileName = fileName.substring(0, fileName.length() - uxf.length());
		}
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
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f1), "UTF-8"));
			writer.println(filename);
			writer.close();
			return true;
		} catch (UnsupportedEncodingException e) {
			return false;
		} catch (FileNotFoundException e) {
			return false;
		}
	}

	private static boolean alreadyRunningChecker(boolean force) {
		try {
			File f = tmpFile();
			if (f.exists() && !force) {
				return true;
			}
			Path.safeCreateFile(f, false);
			new Timer("alreadyRunningChecker", true).schedule(new RunningFileChecker(tmpFile(), Main.getInstance()), 0, 1000);
		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		}
		return false;
	}

	private static File tmpFile() {
		String userPart = System.getProperty("user.name").replaceAll("[^a-zA-Z0-9\\._]+", "_"); // #535: append username to file for multiuser systems (but strip out invalid filename chars)
		return new File(Path.temp() + Program.getInstance().getProgramName().toLowerCase() + "-" + userPart + ".tmp");
	}

	public static void readBuildInfoAndInitVersion(RuntimeType runtime) {
		BuildInfo buildInfo = Utils.readBuildInfo();
		Program.init(buildInfo.version, runtime);
	}

	private static void printUsage() {
		StringBuilder formatBuilder = new StringBuilder("pdf|svg|eps");
		for (String format : ImageIO.getWriterFileSuffixes()) {
			formatBuilder.append("|").append(format);
		}
		printToConsole("USAGE FOR CONVERTING: -action=convert -format=(" + formatBuilder.toString() + ") -filename=inputfile." + Program.getInstance().getExtension() + " [-output=outputfile[.extension]]");
		printToConsole("USAGE FOR GENERATING: -action=generate -filename=inputfile.java[,*.java] -output=outputfile." + Program.getInstance().getExtension());
	}

}
