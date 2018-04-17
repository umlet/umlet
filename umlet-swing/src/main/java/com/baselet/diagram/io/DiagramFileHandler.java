package com.baselet.diagram.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.baselet.control.config.Config;
import com.baselet.control.constants.Constants;
import com.baselet.control.enums.Program;
import com.baselet.control.util.Path;
import com.baselet.control.util.RecentlyUsedFilesList;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.Notifier;
import com.baselet.element.NewGridElement;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.old.custom.CustomElement;
import com.baselet.gui.CurrentGui;

public class DiagramFileHandler {

	private static final Logger log = LoggerFactory.getLogger(DiagramFileHandler.class);

	private String fileName;
	private final DiagramHandler handler;
	private File file;
	private File lastExportFile;
	private final HashMap<String, FileFilter> filters = new HashMap<String, FileFilter>();
	private final HashMap<FileFilter, String> fileextensions = new HashMap<FileFilter, String>();

	private final OwnFileFilter filterxml = new OwnFileFilter(Program.getInstance().getExtension(), Program.getInstance().getProgramName() + " diagram format");
	private final OwnFileFilter filterbmp = new OwnFileFilter("bmp", "BMP");
	private final OwnFileFilter filtereps = new OwnFileFilter("eps", "EPS");
	private final OwnFileFilter filtergif = new OwnFileFilter("gif", "GIF");
	private final OwnFileFilter filterjpg = new OwnFileFilter("jpg", "JPG");
	private final OwnFileFilter filterpdf = new OwnFileFilter("pdf", "PDF");
	private final OwnFileFilter filterpng = new OwnFileFilter("png", "PNG");
	private final OwnFileFilter filtersvg = new OwnFileFilter("svg", "SVG");

	private final OwnFileFilter[] saveFileFilter = new OwnFileFilter[] { filterxml };
	private final OwnFileFilter[] exportFileFilter = new OwnFileFilter[] { filterbmp, filtereps, filtergif, filterjpg, filterpdf, filterpng, filtersvg };
	private final List<OwnFileFilter> allFileFilters = new ArrayList<OwnFileFilter>();

	protected DiagramFileHandler(DiagramHandler diagramHandler, File file) {
		handler = diagramHandler;
		if (file != null) {
			fileName = file.getName();
		}
		else {
			fileName = "new." + Program.getInstance().getExtension();
		}
		this.file = file;
		lastExportFile = file;

		allFileFilters.addAll(Arrays.asList(saveFileFilter));
		allFileFilters.addAll(Arrays.asList(exportFileFilter));
		for (OwnFileFilter filter : allFileFilters) {
			filters.put(filter.getFormat(), filter);
			fileextensions.put(filter, filter.getFormat());
		}
	}

	public static DiagramFileHandler createInstance(DiagramHandler diagramHandler, File file) {
		return new DiagramFileHandler(diagramHandler, file);
	}

	private JFileChooser createSaveFileChooser(boolean exportCall, String filePath) {
		File initialDirectory;
		if (filePath != null && !filePath.isEmpty()) {
			initialDirectory = new File(directory(filePath));
		} else {
			initialDirectory = calcInitialDir(exportCall);
		}

		JFileChooser fileChooser = new JFileChooser(initialDirectory);
		fileChooser.setAcceptAllFileFilterUsed(false); // We don't want "all files" as a choice
		// The input field should show the diagram name as preset
		File selectedFile;
		if (filePath != null && !filePath.isEmpty()) {
			selectedFile = new File(filename(filePath));
		} else {
			selectedFile = new File(CurrentDiagram.getInstance().getDiagramHandler().getName());
		}
		fileChooser.setSelectedFile(selectedFile);
		return fileChooser;
	}

	private File calcInitialDir(boolean exportCall) {
		if (exportCall && lastExportFile != null) { // if this is an export-diagram call the diagram was exported once before (for consecutive export calls - see Issue 82)
			return lastExportFile;
		}
		else if (file != null) { // otherwise if diagram has a fixed uxf path, use this
			return file;
		}
		else { // otherwise use the last used save path
			return new File(Config.getInstance().getSaveFileHome());
		}
	}

	public String getFileName() {
		return fileName;
	}

	public String getFullPathName() {
		if (file != null) {
			return file.getAbsolutePath();
		}
		return "";
	}

	private void setFileName(String fileName) {
		this.fileName = fileName;
		CurrentGui.getInstance().getGui().updateDiagramName(handler, handler.getName());
	}

	private void createXMLOutputDoc(Document doc, Collection<GridElement> elements, Element current) {
		for (GridElement e : elements) {
			appendRecursively(doc, current, e);
		}
	}

	private void appendRecursively(Document doc, Element parentXmlElement, GridElement e) {
		parentXmlElement.appendChild(createXmlElementForGridElement(doc, e));
	}

	private Element createXmlElementForGridElement(Document doc, GridElement e) {
		// insert normal entity element
		java.lang.Class<? extends GridElement> c = e.getClass();
		String sElType = c.getName();
		String sElPanelAttributes = e.getPanelAttributes();
		String sElAdditionalAttributes = e.getAdditionalAttributes();

		Element el = doc.createElement("element");

		if (e instanceof NewGridElement) {
			Element elType = doc.createElement("id");
			elType.appendChild(doc.createTextNode(((NewGridElement) e).getId().toString()));
			el.appendChild(elType);
		}
		else { // OldGridElement
			Element elType = doc.createElement("type");
			elType.appendChild(doc.createTextNode(sElType));
			el.appendChild(elType);
		}

		Element elCoor = doc.createElement("coordinates");
		el.appendChild(elCoor);

		Element elX = doc.createElement("x");
		elX.appendChild(doc.createTextNode("" + e.getRectangle().x));
		elCoor.appendChild(elX);

		Element elY = doc.createElement("y");
		elY.appendChild(doc.createTextNode("" + e.getRectangle().y));
		elCoor.appendChild(elY);

		Element elW = doc.createElement("w");
		elW.appendChild(doc.createTextNode("" + e.getRectangle().width));
		elCoor.appendChild(elW);

		Element elH = doc.createElement("h");
		elH.appendChild(doc.createTextNode("" + e.getRectangle().height));
		elCoor.appendChild(elH);

		Element elPA = doc.createElement("panel_attributes");
		elPA.appendChild(doc.createTextNode(sElPanelAttributes));
		el.appendChild(elPA);

		Element elAA = doc.createElement("additional_attributes");
		elAA.appendChild(doc.createTextNode(sElAdditionalAttributes));
		el.appendChild(elAA);

		if (e instanceof CustomElement) {
			Element elCO = doc.createElement("custom_code");
			elCO.appendChild(doc.createTextNode(((CustomElement) e).getCode()));
			el.appendChild(elCO);
		}
		return el;
	}

	protected String createStringToBeSaved() {
		DocumentBuilder db = null;
		String returnString = null;

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();

			Element root = doc.createElement("diagram");
			root.setAttribute("program", Program.getInstance().getProgramName().toLowerCase());
			root.setAttribute("version", String.valueOf(Program.getInstance().getVersion()));
			doc.appendChild(root);

			// save helptext
			String helptext = handler.getHelpText();
			if (!helptext.equals(Constants.getDefaultHelptext())) {
				Element help = doc.createElement("help_text");
				help.appendChild(doc.createTextNode(helptext));
				root.appendChild(help);
			}

			// save zoom
			Element zoom = doc.createElement("zoom_level");
			zoom.appendChild(doc.createTextNode(String.valueOf(handler.getGridSize())));
			root.appendChild(zoom);

			createXMLOutputDoc(doc, handler.getDrawPanel().getGridElements(), root);

			// output the stuff...
			DOMSource source = new DOMSource(doc);
			StringWriter stringWriter = new StringWriter();
			StreamResult result = new StreamResult(stringWriter);

			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			transformer.transform(source, result);

			returnString = stringWriter.toString();
		} catch (Exception e) {
			log.error("Error saving XML.", e);
		}

		return returnString;

	}

	public void doOpen() {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			if (Config.getInstance().isSecureXmlProcessing()) {
				// use secure xml processing (see https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#JAXP_DocumentBuilderFactory.2C_SAXParserFactory_and_DOM4J)
				spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
				spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
				spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
				spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
			}
			SAXParser parser = spf.newSAXParser();
			FileInputStream input = new FileInputStream(file);
			InputHandler xmlhandler = new InputHandler(handler);
			parser.parse(input, xmlhandler);
			input.close();
		} catch (Exception e) {
			log.error("Cannot open the file: " + file.getAbsolutePath(), e);
		}
	}

	public String doSaveAs(String filePath, String extension) throws IOException {
		boolean ownXmlFormat = extension.equals(Program.getInstance().getExtension());
		JFileChooser fileChooser = createSaveFileChooser(!ownXmlFormat, filePath);

		String chosenFileName = chooseFileName(ownXmlFormat, filters.get(extension), fileChooser);
		String chosenExtension = fileextensions.get(fileChooser.getFileFilter());
		if (chosenFileName == null) {
			return null; // If the filechooser has been closed without saving
		}
		if (!chosenFileName.endsWith("." + chosenExtension)) {
			chosenFileName += "." + chosenExtension;
		}

		File fileToSave = new File(chosenFileName);
		Config.getInstance().setSaveFileHome(fileToSave.getParent());
		if (chosenExtension.equals(Program.getInstance().getExtension())) {
			file = fileToSave;
			setFileName(file.getName());
			save();
		}
		else {
			lastExportFile = fileToSave;
			doExportAs(extension, fileToSave);
		}

		return fileToSave.getAbsolutePath();
	}

	public String doSaveAs(String fileextension) throws IOException {
		return doSaveAs(null, fileextension);
	}

	public File doSaveTempDiagram(String filename, String fileextension) throws IOException {
		File tempFile = new File(Path.temp() + filename + "." + fileextension);
		tempFile.deleteOnExit();

		if (fileextension.equals(Program.getInstance().getExtension())) {
			save(tempFile, true);
		}
		else {
			doExportAs(fileextension, tempFile);
		}

		return tempFile;
	}

	public void doSave() throws IOException {
		if (file == null || !file.exists()) {
			doSaveAs(Program.getInstance().getExtension());
		}
		else {
			save();
		}
	}

	public void doExportAs(String extension, File file) throws IOException {
		// CustomElementSecurityManager.addThreadPrivileges(Thread.currentThread(), fileName);
		try {
			OutputHandler.createAndOutputToFile(extension, file, handler);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
		// CustomElementSecurityManager.remThreadPrivileges(Thread.currentThread());
	}

	private void save() throws UnsupportedEncodingException, FileNotFoundException {
		save(file, false); // If save is called without a parameter it uses the class variable "file"
	}

	private void save(File saveToFile, boolean tempFile) throws UnsupportedEncodingException, FileNotFoundException {
		String tmp = createStringToBeSaved();
		PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(saveToFile), "UTF-8"));
		out.print(tmp);
		out.close();
		if (!tempFile) {
			handler.setChanged(false);
			RecentlyUsedFilesList.getInstance().add(saveToFile.getAbsolutePath());
		}
		Notifier.getInstance().showInfo(saveToFile.getAbsolutePath() + " saved");
	}

	private String chooseFileName(boolean ownXmlFormat, FileFilter filefilter, JFileChooser fileChooser) {
		String fileName = null;

		setAvailableFileFilters(ownXmlFormat, fileChooser);
		fileChooser.setFileFilter(filefilter);

		int returnVal = fileChooser.showSaveDialog(CurrentGui.getInstance().getGui().getMainFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File selectedFileWithExt = getFileWithExtension(fileChooser);
			if (selectedFileWithExt.exists()) {
				int overwriteQuestionResult = JOptionPane.showConfirmDialog(CurrentGui.getInstance().getGui().getMainFrame(), "File already exists! Overwrite?", "Overwrite File", JOptionPane.YES_NO_OPTION);
				if (overwriteQuestionResult == JOptionPane.NO_OPTION) {
					return chooseFileName(ownXmlFormat, filefilter, fileChooser);
				}
			}
			fileName = selectedFileWithExt.getAbsolutePath();
		}
		return fileName;
	}

	private String directory(String filePath) {
		File fileObject = new File(filePath);
		File fileObjectAbsolute = new File(fileObject.getAbsolutePath());
		return fileObjectAbsolute.getParent();
	}

	private String filename(String filePath) {
		File fileObject = new File(filePath);
		String filename = fileObject.getName();
		int extensionPos = filename.lastIndexOf(".");

		if (extensionPos > 0) {
			String filenameWithoutExtension = filename.substring(0, extensionPos);
			return filenameWithoutExtension;
		} else {
			return filename;
		}
	}

	/**
	 * If the filename of the filechooser has no extension, the extension from the filefilter is added to the name
	 * @param saveFileChooser2
	 */
	private File getFileWithExtension(JFileChooser fileChooser) {
		String extension = "." + fileextensions.get(fileChooser.getFileFilter());
		String filename = fileChooser.getSelectedFile().getAbsolutePath();
		if (!filename.endsWith(extension)) {
			filename += extension;
		}
		File selectedFileWithExt = new File(filename);
		return selectedFileWithExt;
	}

	/**
	 * Updates the available FileFilter to "only uxf/pxf" or "all but uxf/pxf"
	 *
	 * @param ownXmlFormat
	 *            If this param is set, only uxf/pxf is visible, otherwise all but uxf/pxf is visible
	 */
	private void setAvailableFileFilters(boolean ownXmlFormat, JFileChooser fileChooser) {
		if (ownXmlFormat) {
			fileChooser.resetChoosableFileFilters();
			fileChooser.addChoosableFileFilter(filterxml);
		}
		else {
			fileChooser.resetChoosableFileFilters();
			fileChooser.addChoosableFileFilter(filterbmp);
			fileChooser.addChoosableFileFilter(filtereps);
			fileChooser.addChoosableFileFilter(filtergif);
			fileChooser.addChoosableFileFilter(filterjpg);
			fileChooser.addChoosableFileFilter(filterpdf);
			fileChooser.addChoosableFileFilter(filterpng);
			fileChooser.addChoosableFileFilter(filtersvg);
		}
	}

	protected static class OwnFileFilter extends FileFilter {
		private final String format;
		private final String description;

		protected OwnFileFilter(String format, String description) {
			this.format = format;
			this.description = description;
		}

		@Override
		public boolean accept(File f) {
			return f.getName().endsWith("." + format) || f.isDirectory();
		}

		@Override
		public String getDescription() {
			return description + " (*." + format + ")";
		}

		public String getFormat() {
			return format;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (file == null ? 0 : file.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DiagramFileHandler other = (DiagramFileHandler) obj;
		if (file == null) {
			if (other.file != null) {
				return false;
			}
		}
		else if (!file.equals(other.file)) {
			return false;
		}
		return true;
	}

}
