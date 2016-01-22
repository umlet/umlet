package com.baselet.diagram.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.baselet.control.config.Config;
import com.baselet.gui.CurrentGui;

public class ClassChooser {

	private static JFileChooser instance;
	private static final String ALLOWED_EXTENSIONS = ".*.(java|class)";
	private static final int TOO_MANY_FILES = 10;

	private static JFileChooser getInstance() {
		if (instance == null) {
			instance = new JFileChooser(Config.getInstance().getOpenFileHome());
			instance.setMultiSelectionEnabled(true);
			instance.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			instance.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return Pattern.matches(ALLOWED_EXTENSIONS, f.getName()) || f.isDirectory();
				}

				@Override
				public String getDescription() {
					return ".java/.class file or directory";
				}
			});
			instance.setAcceptAllFileFilterUsed(false);
		}
		return instance;
	}

	public static List<String> getFilesToOpen() {
		List<String> fileNames = new ArrayList<String>();
		int returnVal = getInstance().showOpenDialog(CurrentGui.getInstance().getGui().getMainFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] selectedFiles = getInstance().getSelectedFiles();
			for (File file : selectedFiles) {
				searchRecursively(file, fileNames);
			}
			Config.getInstance().setOpenFileHome(selectedFiles[0].getAbsoluteFile().getParent());
			if (fileNames.size() > TOO_MANY_FILES) {
				returnVal = JOptionPane.showConfirmDialog(CurrentGui.getInstance().getGui().getMainFrame(),
						"Your selection contains " + fileNames.size() + " files which may " +
						"clutter up your diagram. Continue?",
						"Confirm selection", JOptionPane.OK_CANCEL_OPTION);
				if (returnVal == JOptionPane.CANCEL_OPTION) {
					fileNames.clear();
				}
			}
		}
		return fileNames;
	}

	private static void searchRecursively(File file, List<String> fileNames) {
		if (Pattern.matches(ALLOWED_EXTENSIONS, file.getName())) {
			fileNames.add(file.getAbsolutePath());
		}
		else if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				searchRecursively(f, fileNames);
			}
		}
	}
}
