package com.baselet.diagram.io;

import java.awt.Component;
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
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.baselet.control.Constants;
import com.baselet.control.Constants.Program;
import com.baselet.control.Main;
import com.baselet.control.Path;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.GridElement;
import com.baselet.element.Group;
import com.umlet.custom.CustomElement;

public class DiagramFileHandler {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	private static JFileChooser openFileChooser;
	private static JFileChooser saveFileChooser;

	public static DiagramFileHandler createInstance(DiagramHandler diagramHandler, File file) {
		return new DiagramFileHandler(diagramHandler, file);
	}

	public static JFileChooser getOpenFileChooser() {
		if (openFileChooser == null) {
			openFileChooser = new JFileChooser(System.getProperty("user.dir"));
			openFileChooser.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return (f.getName().endsWith("." + Program.EXTENSION) || f.isDirectory());
				}

				@Override
				public String getDescription() {
					return Program.PROGRAM_NAME + " diagram format (*." + Program.EXTENSION + ")";
				}
			});
			openFileChooser.setAcceptAllFileFilterUsed(false);
		}
		return openFileChooser;
	}

	private JFileChooser reloadSaveFileChooser() {
		// Set the initial target location for the fileChooser
		if (this.file != null) saveFileChooser = new JFileChooser(this.file);
		else saveFileChooser = new JFileChooser(System.getProperty("user.dir"));

		saveFileChooser.setAcceptAllFileFilterUsed(false); // We don't want "all files" as a choice
		// The input field should show the diagram name as preset
		saveFileChooser.setSelectedFile(new File(Main.getInstance().getDiagramHandler().getName()));
		return saveFileChooser;
	}

	public static String chooseFileName() {
		return Main.getInstance().getGUI().chooseFileName();
	}

	private String fileName;
	private DiagramHandler handler;
	private File file;
	private HashMap<String, FileFilter> filters;
	private HashMap<FileFilter, String> fileextensions;

	private FileFilter filterxml = new OwnFileFilter(Program.EXTENSION, Program.PROGRAM_NAME + " diagram format");
	private FileFilter filterbmp = new OwnFileFilter("bmp", "BMP");
	private FileFilter filtereps = new OwnFileFilter("eps", "EPS");
	private FileFilter filtergif = new OwnFileFilter("gif", "GIF");
	private FileFilter filterjpg = new OwnFileFilter("jpg", "JPG");
	private FileFilter filterpdf = new OwnFileFilter("pdf", "PDF");
	private FileFilter filterpng = new OwnFileFilter("png", "PNG");
	private FileFilter filtersvg = new OwnFileFilter("svg", "SVG");

	protected DiagramFileHandler(DiagramHandler diagramHandler, File file) {
		this.handler = diagramHandler;
		if (file != null) this.fileName = file.getName();
		else this.fileName = "new." + Program.EXTENSION;
		this.file = file;

		this.filters = new HashMap<String, FileFilter>();
		this.filters.put(Program.EXTENSION, filterxml);
		this.filters.put("bmp", filterbmp);
		this.filters.put("eps", filtereps);
		this.filters.put("gif", filtergif);
		this.filters.put("jpg", filterjpg);
		this.filters.put("pdf", filterpdf);
		this.filters.put("png", filterpng);
		this.filters.put("svg", filtersvg);

		this.fileextensions = new HashMap<FileFilter, String>();
		this.fileextensions.put(filterxml, Program.EXTENSION);
		this.fileextensions.put(filterbmp, "bmp");
		this.fileextensions.put(filtereps, "eps");
		this.fileextensions.put(filtergif, "gif");
		this.fileextensions.put(filterjpg, "jpg");
		this.fileextensions.put(filterpdf, "pdf");
		this.fileextensions.put(filterpng, "png");
		this.fileextensions.put(filtersvg, "svg");

		// UNCOMMENTED BECAUSE WE DONT NEED ALL IMAGE FORMATS AND WE WANT TO SORT THEM ALPHABETICALLY
		/*
		 * for(String format : ImageIO.getWriterFileSuffixes()) {
		 * FileFilter filter = new OwnFileFilter(format, format.toUpperCase() + " Image");
		 * //We don't want to add "jpeg" which is redundant with "jpg" and "wbmp" because it doesn't work
		 * if (format.toUpperCase().equals("JPEG") || format.toUpperCase().equals("WBMP")) continue;
		 * this.fileChooser.addChoosableFileFilter(filter);
		 * this.filters.put(format, filter);
		 * this.fileextensions.put(filter, format);
		 * }
		 */
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getFullPathName() {
		if (this.file != null) return this.file.getAbsolutePath();
		return "";
	}

	private void setFileName(String fileName) {
		this.fileName = fileName;
		Main.getInstance().getGUI().updateDiagramName(this.handler, this.handler.getName());
	}

	private void createXMLOutputDoc(Document doc, List<GridElement> entities, Element current, Group group) {
		// list of elements that are not inserted yet (to increase performance)
		List<GridElement> toBeCheckedAgain = new ArrayList<GridElement>();
		List<Group> insert_groups = new ArrayList<Group>();
		for (GridElement e : entities) {
			// only insert element in right grouping element
			boolean insert_here = false;
			if ((group == null) && (e.getGroup() == null)) insert_here = true;
			else if (group != null) if (group.equals(e.getGroup())) insert_here = true;

			if (insert_here) {
				if (e instanceof Group) insert_groups.add((Group) e);
				else { // insert normal entity element
					java.lang.Class<? extends GridElement> c = e.getClass();
					String sElType = c.getName();
					int[] coor = e.getCoordinates();
					String sElPanelAttributes = e.getPanelAttributes();
					String sElAdditionalAttributes = e.getAdditionalAttributes();

					Element el = doc.createElement("element");
					current.appendChild(el);

					Element elType = doc.createElement("type");
					elType.appendChild(doc.createTextNode(sElType));
					el.appendChild(elType);

					Element elCoor = doc.createElement("coordinates");
					el.appendChild(elCoor);

					Element elX = doc.createElement("x");
					elX.appendChild(doc.createTextNode("" + coor[0]));
					elCoor.appendChild(elX);

					Element elY = doc.createElement("y");
					elY.appendChild(doc.createTextNode("" + coor[1]));
					elCoor.appendChild(elY);

					Element elW = doc.createElement("w");
					elW.appendChild(doc.createTextNode("" + coor[2]));
					elCoor.appendChild(elW);

					Element elH = doc.createElement("h");
					elH.appendChild(doc.createTextNode("" + coor[3]));
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
				}
			}
			else toBeCheckedAgain.add(e);
		}

		for (Group g : insert_groups) {
			Element el = doc.createElement("group");
			current.appendChild(el);
			createXMLOutputDoc(doc, toBeCheckedAgain, el, g);
		}
	}

	protected String createStringToBeSaved() {
		Component[] components = this.handler.getDrawPanel().getComponents();
		List<GridElement> entities = new ArrayList<GridElement>();
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof GridElement) entities.add((GridElement) components[i]);
		}

		DocumentBuilder db = null;
		String returnString = null;

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();

			Element root = doc.createElement("diagram");
			root.setAttribute("program", Program.PROGRAM_NAME.toLowerCase());
			root.setAttribute("version", String.valueOf(Program.VERSION));
			doc.appendChild(root);

			// save helptext
			String helptext = this.handler.getHelpText();
			if (!helptext.equals(Constants.DEFAULT_HELPTEXT)) {
				Element help = doc.createElement("help_text");
				help.appendChild(doc.createTextNode(helptext));
				root.appendChild(help);
			}

			// save zoom
			Element zoom = doc.createElement("zoom_level");
			zoom.appendChild(doc.createTextNode(String.valueOf(this.handler.getGridSize())));
			root.appendChild(zoom);

			// save elements (group = null = rootlayer)
			this.createXMLOutputDoc(doc, entities, root, null);

			// output the stuff...
			DOMSource source = new DOMSource(doc);
			StringWriter stringWriter = new StringWriter();
			StreamResult result = new StreamResult(stringWriter);

			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT,"yes");
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
			try {
				SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
				FileInputStream input = new FileInputStream(this.file);
				InputHandler xmlhandler = new InputHandler(this.handler);
				parser.parse(input, xmlhandler);
			} catch (SAXException e) {
				log.error("Error parsing the inputstream.", e);
			}
			/*
			 * } catch (IOException e) {
			 * log.error(null, e);
			 */
		} catch (Exception e) {
			log.error(null, e);

		}
	}

	public void doSaveAs(String fileextension) throws IOException {
		String fileName = this.chooseFileName(fileextension.equals(Program.EXTENSION), filters.get(fileextension));
		String extension = this.fileextensions.get(saveFileChooser.getFileFilter());
		if (fileName == null) return; // If the filechooser has been closed without saving
		if (!fileName.endsWith("." + extension)) fileName += "." + extension;

		File fileToSave = new File(fileName);
		if (extension.equals(Program.EXTENSION)) {
			this.file = fileToSave;
			this.setFileName(this.file.getName());
			save(true);
		}
		else doExportAs(extension, fileToSave);
	}

	public File doSaveTempDiagram(String filename, String fileextension) throws IOException {
		File tempFile = new File(Path.temp() + filename + "." + fileextension);
		tempFile.deleteOnExit();

		if (fileextension.equals(Program.EXTENSION)) save(tempFile, false);
		else doExportAs(fileextension, tempFile);

		return tempFile;
	}

	public void doSave() throws IOException {
		if ((file == null) || !file.exists()) doSaveAs(Program.EXTENSION);
		else save(true);
	}

	public void doExportAs(String extension, File file) throws IOException {
//		CustomElementSecurityManager.addThreadPrivileges(Thread.currentThread(), fileName);
		try {
			OutputHandler.createAndOutputToFile(extension, file, this.handler);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
//		CustomElementSecurityManager.remThreadPrivileges(Thread.currentThread());
	}

	private void save(boolean removeHandlerChanged) throws UnsupportedEncodingException, FileNotFoundException {
		save(file, removeHandlerChanged); // If save is called without a parameter it uses the class variable "file"
	}

	private void save(File saveToFile, boolean removeHandlerChanged) throws UnsupportedEncodingException, FileNotFoundException {
		String tmp = this.createStringToBeSaved();
		PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(saveToFile), "UTF-8"));
		out.print(tmp);
		out.close();
		if(removeHandlerChanged) handler.setChanged(false);
	}

	private String chooseFileName(boolean ownXmlFormat, FileFilter filefilter) {
		String fileName = null;

		// filechooser must be recreated to avoid a bug where getSelectedFile() was null (if a file is saved more than one time by doubleclicking on an existing file)
		reloadSaveFileChooser();

		setAvailableFileFilters(ownXmlFormat);
		saveFileChooser.setFileFilter(filefilter);

		int returnVal = saveFileChooser.showSaveDialog(Main.getInstance().getGUI());
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File selectedFileWithExt = new File(saveFileChooser.getSelectedFile().getName() + "." + fileextensions.get(saveFileChooser.getFileFilter()));
			// We must check for the file with and without extension (saving without adding the extension automatically adds the selected extension)
			if (saveFileChooser.getSelectedFile().exists() || selectedFileWithExt.exists()) {
				int overwriteQuestionResult = JOptionPane.showConfirmDialog(Main.getInstance().getGUI(), "File already exists! Overwrite?", "Overwrite File", JOptionPane.YES_NO_OPTION);
				if (overwriteQuestionResult == JOptionPane.NO_OPTION) return chooseFileName(ownXmlFormat, filefilter);
			}
			fileName = saveFileChooser.getSelectedFile().getAbsolutePath();
		}
		return fileName;
	}

	/**
	 * Updates the available FileFilter to "only uxf/pxf" or "all but uxf/pxf"
	 * 
	 * @param ownXmlFormat
	 *            If this param is set, only uxf/pxf is visible, otherwise all but uxf/pxf is visible
	 */
	private void setAvailableFileFilters(boolean ownXmlFormat) {
		if (ownXmlFormat) {
			saveFileChooser.resetChoosableFileFilters();
			saveFileChooser.addChoosableFileFilter(filterxml);
		}
		else {
			saveFileChooser.resetChoosableFileFilters();
			saveFileChooser.addChoosableFileFilter(filterbmp);
			saveFileChooser.addChoosableFileFilter(filtereps);
			saveFileChooser.addChoosableFileFilter(filtergif);
			saveFileChooser.addChoosableFileFilter(filterjpg);
			saveFileChooser.addChoosableFileFilter(filterpdf);
			saveFileChooser.addChoosableFileFilter(filterpng);
			saveFileChooser.addChoosableFileFilter(filtersvg);
		}
	}

	protected class OwnFileFilter extends FileFilter {
		private String format;
		private String description;

		protected OwnFileFilter(String format, String description) {
			this.format = format;
			this.description = description;
		}

		@Override
		public boolean accept(File f) {
			return (f.getName().endsWith("." + this.format) || f.isDirectory());
		}

		@Override
		public String getDescription() {
			return this.description + " (*." + this.format + ")";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		DiagramFileHandler other = (DiagramFileHandler) obj;
		if (file == null) {
			if (other.file != null) return false;
		}
		else if (!file.equals(other.file)) return false;
		return true;
	}

}
