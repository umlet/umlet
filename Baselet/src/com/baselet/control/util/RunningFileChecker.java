package com.baselet.control.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TimerTask;

public class RunningFileChecker extends TimerTask {

	private final File file;
	private final File ok_file;
	private final CanOpenDiagram canOpenDiagram;

	public RunningFileChecker(String filename, String okfile, CanOpenDiagram canOpenDiagram) {
		this.canOpenDiagram = canOpenDiagram;
		file = new File(filename);
		ok_file = new File(okfile);
	}

	@Override
	public void run() {
		try {
			Utils.safeCreateFile(file, false);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String filename = reader.readLine();
			reader.close();
			if (filename != null) {
				Utils.safeCreateFile(ok_file, true);
				Utils.safeDeleteFile(file, false);
				Utils.safeCreateFile(file, true);
				canOpenDiagram.doOpen(filename);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
