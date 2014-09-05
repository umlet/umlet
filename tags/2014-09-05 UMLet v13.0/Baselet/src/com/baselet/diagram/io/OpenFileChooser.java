package com.baselet.diagram.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.control.SharedConstants.Program;

public class OpenFileChooser {

	private static JFileChooser instance;

	private static JFileChooser getInstance() {
		if (instance == null) {
			instance = new JFileChooser(Constants.openFileHome);
			instance.setMultiSelectionEnabled(true);
			instance.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.getName().endsWith("." + Program.EXTENSION) || f.isDirectory();
				}

				@Override
				public String getDescription() {
					return Program.NAME + " diagram format (*." + Program.EXTENSION + ")";
				}
			});
			instance.setAcceptAllFileFilterUsed(false);
		}
		return instance;
	}

	public static List<String> getFilesToOpen() {
		List<String> fileNames = new ArrayList<String>();
		int returnVal = getInstance().showOpenDialog(Main.getInstance().getGUI().getMainFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] selectedFiles = getInstance().getSelectedFiles();
			for (File file : selectedFiles) {
				fileNames.add(file.getAbsolutePath());
			}
			Constants.openFileHome = selectedFiles[0].getParent();
		}
		return fileNames;
	}

}
