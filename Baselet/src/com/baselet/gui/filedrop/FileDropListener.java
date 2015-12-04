package com.baselet.gui.filedrop;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.baselet.control.Main;
import com.baselet.generator.ClassDiagramConverter;
import com.baselet.gui.CurrentGui;

public class FileDropListener implements FileDrop.Listener {

	private static final Logger log = Logger.getLogger(FileDropListener.class);

	@Override
	public void filesDropped(File[] files) {
		for (File file : files) {
			try {
				String filename = file.getCanonicalPath();
				if (isJavaFile(filename)) {
					List<Exception> failures = new ClassDiagramConverter().createClassDiagram(filename);
					if (!failures.isEmpty()) {
						JOptionPane.showMessageDialog(CurrentGui.getInstance().getGui().getMainFrame(), ClassDiagramConverter.convertFailuresToString(failures), "Errors while generation Diagram", JOptionPane.ERROR_MESSAGE);
					}
				}
				else {
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
		if (extension.equals("class") || extension.equals("java")) {
			return true;
		}
		return false;
	}
}
