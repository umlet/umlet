package com.baselet.diagram.io;

import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.baselet.control.config.Config;
import com.baselet.control.enums.Program;

public class OpenFileChooser {

	private JFileChooser fileChooser;

	public OpenFileChooser() {
		fileChooser = new JFileChooser(Config.getInstance().getOpenFileHome());
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().endsWith("." + Program.getInstance().getExtension()) || f.isDirectory();
			}

			@Override
			public String getDescription() {
				return Program.getInstance().getProgramName() + " diagram format (*." + Program.getInstance().getExtension() + ")";
			}
		});
		fileChooser.setAcceptAllFileFilterUsed(false);
	}

	public List<String> getFilesToOpen(Frame mainFrame) {
		List<String> fileNames = new ArrayList<String>();
		int returnVal = fileChooser.showOpenDialog(mainFrame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] selectedFiles = fileChooser.getSelectedFiles();
			for (File file : selectedFiles) {
				fileNames.add(file.getAbsolutePath());
			}
		}
		return fileNames;
	}

}
