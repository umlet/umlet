package com.baselet.gui;

import com.baselet.control.ErrorMessages;
import com.baselet.control.constants.Constants;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.Notifier;
import com.baselet.diagram.io.DiagramFileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExportAsHandler implements KeyListener, ActionListener {
	private JComboBox exportAsComboBox;
	private JButton exportAsButton;
	private Map<DiagramHandler, String> lastExportedFilePathsPerTab = new HashMap<DiagramHandler, String>();

	private static final Logger log = LoggerFactory.getLogger(ExportAsHandler.class);

	private static ExportAsHandler instance;

	public static ExportAsHandler getInstance() {
		if (instance == null) {
			throw new RuntimeException("You need to call init() first");
		}
		return instance;
	}

	public static void init(JComboBox exportAsComboxBox, JButton exportAsButton) {
		if (instance == null) {
			instance = new ExportAsHandler(exportAsComboxBox, exportAsButton);
		}
	}

	private ExportAsHandler(JComboBox exportAsComboxBox, JButton exportAsButton) {
		this.exportAsComboBox = exportAsComboxBox;
		this.exportAsButton = exportAsButton;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!exportAsButton.isEnabled()) {
			return;
		}

		boolean ctrl_e_pressed = e.isControlDown() && e.getKeyCode() == KeyEvent.VK_E;
		if (ctrl_e_pressed) {
			exportAs();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		exportAs();
	}

	public void fileExportViaMenu(String exportedFilePath) {
		setExportedFilePath(exportedFilePath);

		String fileExtension = filePathExtension(exportedFilePath);
		selectExportFormat(fileExtension);
	}

	public void newDiagramTab() {
		selectPdfAsExportFormat();
	}

	public void diagramTabIsActivated() {
		if (noExportForCurrentDiagramYet()) {
			selectPdfAsExportFormat();
		} else {
			String lastExportFormat = filePathExtension(lastExportedFilePathsPerTab.get(currentDiagramHandler()));
			selectExportFormat(lastExportFormat);
		}
	}

	public void diagramTabIsClosed() {
		lastExportedFilePathsPerTab.remove(currentDiagramHandler());
	}

	private String getSelectedExportFileExtension() {
		return exportAsComboBox.getSelectedItem().toString().toLowerCase();
	}

	private void selectExportFormat(String formatExtension) {
		int extensionIndex = Constants.getIndexOfExportFormat(formatExtension);
		if (extensionIndex > -1) {
			exportAsComboBox.setSelectedIndex(extensionIndex);
		}
	}

	private void selectPdfAsExportFormat() {
		int extensionIndex = Constants.getIndexOfPdfExportFormat();
		if (extensionIndex > -1) {
			exportAsComboBox.setSelectedIndex(extensionIndex);
		}
	}

	private String exportWithSaveDialog(String exportExtension) {
		boolean successfullySaved = currentDiagramHandler().doSaveAs(exportExtension);

		if (successfullySaved) {
			return currentDiagramHandler().getFileHandler().getLastExportFilePath();
		} else {
			return null;
		}
	}

	private String exportQuietly(String exportExtension) {
		DiagramFileHandler currentFileHandler = currentDiagramHandler().getFileHandler();
		String lastExportFilePath = currentFileHandler.getLastExportFilePath();
		File exportFile = new File(lastExportFilePath);

		try {
			currentFileHandler.doExportAs(exportExtension, exportFile);
			return exportFile.getAbsolutePath();
		} catch (IOException e) {
			log.error(ErrorMessages.ERROR_SAVING_FILE, e);
			displayError(ErrorMessages.ERROR_SAVING_FILE + e.getMessage());
			return null;
		}
	}

	private boolean noExportForCurrentDiagramYet() {
		return !lastExportedFilePathsPerTab.containsKey(currentDiagramHandler());
	}

	private boolean exportExtensionHasChanged(String exportExtension) {
		if (noExportForCurrentDiagramYet()) {
			return false;
		}

		String exportedFilePath = lastExportedFilePathsPerTab.get(currentDiagramHandler());
		String exportedFileExtension = filePathExtension(exportedFilePath);
		return !exportExtension.equals(exportedFileExtension);
	}

	private void setExportedFilePath(String exportedFilePath) {
		lastExportedFilePathsPerTab.put(currentDiagramHandler(), exportedFilePath);
	}

	private DiagramHandler currentDiagramHandler() {
		return CurrentDiagram.getInstance().getDiagramHandler();
	}

	private void exportAs() {
		String exportExtension = getSelectedExportFileExtension();

		String exportedFilePath;
		if (noExportForCurrentDiagramYet() || exportExtensionHasChanged(exportExtension)) {
			exportedFilePath = exportWithSaveDialog(exportExtension);
		} else {
			exportedFilePath = exportQuietly(exportExtension);
		}

		if (exportedFilePath != null && !exportedFilePath.isEmpty()) {
			setExportedFilePath(exportedFilePath);
			Notifier.getInstance().showInfo("Diagram exported to " + exportedFilePath);
		}
	}

	private String filePathExtension(String filePath) {
		if (filePath == null) {
			return null;
		}

		int extensionPos = filePath.indexOf(".");
		if (extensionPos > 0) {
			return filePath.substring(extensionPos + 1);
		} else {
			return filePath;
		}
	}

	private void displayError(String error) {
		JOptionPane.showMessageDialog(CurrentGui.getInstance().getGui().getMainFrame(), error, "ERROR", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// NOOP
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// NOOP
	}
}