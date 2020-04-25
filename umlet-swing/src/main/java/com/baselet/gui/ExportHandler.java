package com.baselet.gui;

import com.baselet.control.config.Config;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.Notifier;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ExportHandler {
	private static final String DEFAULT_EXPORT_EXTENSION = "pdf";

	private Map<DiagramHandler, String> lastExportedFilePathsPerTab = new HashMap<DiagramHandler, String>();

	private static ExportHandler instance;

	public static ExportHandler getInstance() {
		if (instance == null) {
			instance = new ExportHandler();
		}
		return instance;
	}

	private ExportHandler() {
		// NOOP
	}

	public void diagramTabIsClosed() {
		lastExportedFilePathsPerTab.remove(currentDiagramHandler());
	}

	private boolean currentDiagramHasBeenExported() {
		return lastExportedFilePathsPerTab.containsKey(currentDiagramHandler());
	}

	private String getLastExportFilePath() {
		return lastExportedFilePathsPerTab.get(currentDiagramHandler());
	}

	public void setExportedFilePath(String exportedFilePath) {
		lastExportedFilePathsPerTab.put(currentDiagramHandler(), exportedFilePath);
	}

	private DiagramHandler currentDiagramHandler() {
		return CurrentDiagram.getInstance().getDiagramHandler();
	}

	public void export() {
		String exportFilePathWithoutExtension;
		String exportExtension;

		if (currentDiagramHasBeenExported()) {
			String lastExportFilePath = getLastExportFilePath();
			exportFilePathWithoutExtension = filePathWithoutExtension(lastExportFilePath);
			exportExtension = extension(lastExportFilePath);
		} else {
			exportFilePathWithoutExtension = null;
			exportExtension = determineDefaultExportExtension();
		}

		String exportedFilePath = currentDiagramHandler().doSaveAs(exportFilePathWithoutExtension, exportExtension);

		if (exportedFilePath != null && !exportedFilePath.isEmpty()) {
			setExportedFilePath(exportedFilePath);
			String extension = extension(exportedFilePath);
			if (!extension.isEmpty()) {
				Config.getInstance().setLastExportFormat(extension);
			}
			Notifier.getInstance().showInfo("Diagram exported to " + exportedFilePath);
		}
	}

	private String determineDefaultExportExtension() {
		String configuredExportFormat = Config.getInstance().getLastExportFormat();
		if (configuredExportFormat.isEmpty()) {
			return DEFAULT_EXPORT_EXTENSION;
		} else {
			return configuredExportFormat;
		}
	}

	private String filePathWithoutExtension(String filePath) {
		int extensionPos = filePath.lastIndexOf(".");
		if (extensionPos > 0) {
			return filePath.substring(0, extensionPos);
		} else {
			return filePath;
		}
	}

	private String extension(String filePath) {
		int extensionPos = filePath.lastIndexOf(".");
		if (extensionPos > 0) {
			return filePath.substring(extensionPos + 1);
		} else {
			return "";
		}
	}
}