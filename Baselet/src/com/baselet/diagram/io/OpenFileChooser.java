package com.baselet.diagram.io;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.baselet.control.Constants.Program;
import com.baselet.control.Main;

public class OpenFileChooser {

	private static JFileChooser instance;

	private static JFileChooser getInstance() {
		if (instance == null) {
			instance = new JFileChooser(System.getProperty("user.dir"));
			instance.setMultiSelectionEnabled(true);
			instance.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return (f.getName().endsWith("." + Program.EXTENSION) || f.isDirectory());
				}

				@Override
				public String getDescription() {
					return Program.PROGRAM_NAME + " diagram format (*." + Program.EXTENSION + ")";
				}
			});
			instance.setAcceptAllFileFilterUsed(false);
		}
		return instance;
	}
	
	public static List<String> getFilesToOpen() {
		List<String> fileNames = new ArrayList<>();
		int returnVal = getInstance().showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			for (File file : getInstance().getSelectedFiles()) {
				fileNames.add(file.getAbsolutePath());
			}
		}
		return fileNames;
	}
	
}
