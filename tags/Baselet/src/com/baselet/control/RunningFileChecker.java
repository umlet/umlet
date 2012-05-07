package com.baselet.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TimerTask;

public class RunningFileChecker extends TimerTask {

	private File file;
	private File ok_file;

	public RunningFileChecker(String filename, String okfile) {
		file = new File(filename);
		ok_file = new File(okfile);
	}

	@Override
	public void run() {
		try {
			if (!file.exists()) file.createNewFile();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String filename = reader.readLine();
			reader.close();
			if (filename != null) {
				ok_file.createNewFile();
				file.delete();
				file.createNewFile();
				Main.getInstance().doOpen(filename);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
