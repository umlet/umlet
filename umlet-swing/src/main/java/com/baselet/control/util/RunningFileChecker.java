package com.baselet.control.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TimerTask;

public class RunningFileChecker extends TimerTask {

	private final File file;
	private final CanOpenDiagram canOpenDiagram;

	public RunningFileChecker(File file, CanOpenDiagram canOpenDiagram) {
		this.canOpenDiagram = canOpenDiagram;
		this.file = file;
	}

	@Override
	public void run() {
		try {
			Path.safeCreateFile(file, false);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String filename = reader.readLine();
			reader.close();
			if (filename != null) {
				Path.safeDeleteFile(file, false);
				Path.safeCreateFile(file, true);
				canOpenDiagram.doOpen(filename);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
