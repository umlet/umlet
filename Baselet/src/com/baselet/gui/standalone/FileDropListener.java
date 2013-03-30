package com.baselet.gui.standalone;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.umlet.language.ClassDiagramConverter;

public class FileDropListener implements FileDrop.Listener {

	private static Logger log = Logger.getLogger(Utils.getClassName());

	@Override
	public void filesDropped(File[] files) {
		for (int i = 0; i < files.length; i++) {
			try {
				String filename = files[i].getCanonicalPath();
				if (isJavaFile(filename)) { 
					new ClassDiagramConverter().createClassDiagram(filename);
				} else {
					Main.getInstance().doOpen(filename);
				}
			} catch (IOException e) {
				log.error("Cannot open file dropped");
			}
		}
	}
	
	private boolean isJavaFile(String filename) {
		int dotPosition = filename.lastIndexOf(".");
		String extension = filename.substring(dotPosition + 1, filename.length()); 
		if (extension.equals("class") || extension.equals("java")) return true;
		return false;
	}
}
