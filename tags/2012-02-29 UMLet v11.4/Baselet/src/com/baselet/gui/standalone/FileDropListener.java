package com.baselet.gui.standalone;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.baselet.control.Main;
import com.baselet.control.Utils;

public class FileDropListener implements FileDrop.Listener {

	private static Logger log = Logger.getLogger(Utils.getClassName());

	@Override
	public void filesDropped(File[] files) {
		for (int i = 0; i < files.length; i++) {
			try {
				String filename = files[i].getCanonicalPath();
				Main.getInstance().doOpen(filename);
			} catch (IOException e) {
				log.error("Cannot open file dropped");
			}
		}
	}
}
