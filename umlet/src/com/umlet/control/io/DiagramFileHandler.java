package com.umlet.control.io;

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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.umlet.control.Umlet;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.custom.CustomElement;
import com.umlet.custom.CustomElementSecurityManager;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Group;

public class DiagramFileHandler {

	private static Logger log = Logger.getLogger(DiagramFileHandler.class.getName());

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
					return (f.getName().endsWith(".uxf") || f.isDirectory());
				}

				@Override
				public String getDescription() {
					return "UMLet diagram format (*.uxf)";
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
		saveFileChooser.setSelectedFile(new File(Umlet.getInstance().getDiagramHandler().getName()));
		return saveFileChooser;
	}

	public static String chooseFileName() {
		return Umlet.getInstance().getGUI().chooseFileName();
	}

	private String fileName;
	private DiagramHandler handler;
	private File file;
	private HashMap<String, FileFilter> filters;
	private HashMap<FileFilter, String> fileextensions;

	private FileFilter filteruxf = new UMLetFileFilter("uxf", "UMLet diagram format");
	private FileFilter filterbmp = new UMLetFileFilter("bmp", "BMP");
	private FileFilter filtereps = new UMLetFileFilter("eps", "EPS");
	private FileFilter filtergif = new UMLetFileFilter("gif", "GIF");
	private FileFilter filterjpg = new UMLetFileFilter("jpg", "JPG");
	private FileFilter filterpdf = new UMLetFileFilter("pdf", "PDF");
	private FileFilter filterpng = new UMLetFileFilter("png", "PNG");
	private FileFilter filtersvg = new UMLetFileFilter("svg", "SVG");

	protected DiagramFileHandler(DiagramHandler diagramHandler, File file) {
		this.handler = diagramHandler;
		if (file != null) this.fileName = file.getName();
		else this.fileName = "new.uxf";
		this.file = file;

		this.filters = new HashMap<String, FileFilter>();
		this.filters.put("uxf", filteruxf);
		this.filters.put("bmp", filterbmp);
		this.filters.put("eps", filtereps);
		this.filters.put("gif", filtergif);
		this.filters.put("jpg", filterjpg);
		this.filters.put("pdf", filterpdf);
		this.filters.put("png", filterpng);
		this.filters.put("svg", filtersvg);

		this.fileextensions = new HashMap<FileFilter, String>();
		this.fileextensions.put(filteruxf, "uxf");
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
		 * FileFilter filter = new UMLetFileFilter(format, format.toUpperCase() + " Image");
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
		Umlet.getInstance().getGUI().updateDiagramName(this.handler, this.handler.getName());
	}

	private void generateElementTree(Document doc, List<Entity> entities, Element current, Group group) {
		// list of elements that are not inserted yet (to increase performance)
		List<Entity> toBeCheckedAgain = new ArrayList<Entity>();
		List<Group> insert_groups = new ArrayList<Group>();
		for (Entity e : entities) {
			// only insert element in right grouping element
			boolean insert_here = false;
			if ((group == null) && (e.getGroup() == null)) insert_here = true;
			else if (group != null) if (group.equals(e.getGroup())) insert_here = true;

			if (insert_here) {
				if (e instanceof Group) insert_groups.add((Group) e);
				else // insert normal entity element
				{
					java.lang.Class<? extends Entity> c = e.getClass();
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
			generateElementTree(doc, toBeCheckedAgain, el, g);
		}
	}

	protected String createStringToBeSaved() {
		Component[] components = this.handler.getDrawPanel().getComponents();
		List<Entity> entities = new ArrayList<Entity>();
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof Entity) entities.add((Entity) components[i]);
		}

		DocumentBuilder db = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
		} catch (Exception e) {
			log.error("Error saving XML.", e);
		}
		Document doc = db.newDocument();

		Element root = doc.createElement("umlet_diagram");
		doc.appendChild(root);

		// save helptext
		Element help = doc.createElement("help_text");
		help.appendChild(doc.createTextNode(this.handler.getHelpText()));
		root.appendChild(help);

		// save zoom
		Element zoom = doc.createElement("zoom_level");
		zoom.appendChild(doc.createTextNode(String.valueOf(this.handler.getGridSize())));
		root.appendChild(zoom);

		// save elements (group = null = rootlayer)
		this.generateElementTree(doc, entities, root, null);

		// output the stuff...
		StringWriter stringWriter = null;
		try {
			DOMSource source = new DOMSource(doc);
			stringWriter = new StringWriter();
			StreamResult result = new StreamResult(stringWriter);

			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();

			transformer.transform(source, result);
		} catch (Exception e) {
			log.error("Error saving XML.", e);
			log.error(null, e);
		}

		return stringWriter.toString();

	}

	public void doOpen() {
		try {
			try {
				SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
				FileInputStream input = new FileInputStream(this.file);
				XMLContentHandler xmlhandler = new XMLContentHandler(this.handler);
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
		String fileName;
		fileName = this.chooseFileName(fileextension.equals("uxf"), filters.get(fileextension));
		String extension = this.fileextensions.get(saveFileChooser.getFileFilter());
		if (fileName == null) return; // If the filechooser has been closed without saving
		if (!fileName.endsWith("." + extension)) fileName += "." + extension;

		if (extension.equals("uxf")) {
			this.file = new File(fileName);
			this.setFileName(this.file.getName());
			save();
		}
		else doExportAs(extension, fileName);
	}

	public File saveTempFileWithDiagram(String name, String extension) throws IOException {
		File tempFile = new File(name + "." + extension);
		tempFile.deleteOnExit();

		if (extension.equals("uxf")) save(tempFile);
		else doExportAs(extension, tempFile.getName());

		return tempFile;
	}

	public void doSave() throws IOException {
		if ((file == null) || !file.exists()) doSaveAs("uxf");
		else save();
	}

	public void doExportAs(String extension, String fileName) throws IOException {

		CustomElementSecurityManager.addThreadPrivileges(Thread.currentThread(), fileName);
		try {
			Gen.createAndOutputToFile(extension, fileName, this.handler);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
		CustomElementSecurityManager.remThreadPrivileges(Thread.currentThread());

	}

	private void save() throws UnsupportedEncodingException, FileNotFoundException {
		save(file); // If save is called without a parameter it uses the class variable "file"
	}

	private void save(File saveToFile) throws UnsupportedEncodingException, FileNotFoundException {
		String tmp = this.createStringToBeSaved();
		PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(saveToFile), "UTF-8"));
		out.print(tmp);
		out.close();
	}

	private String chooseFileName(boolean uxf, FileFilter filefilter) {
		String fileName = null;

		// filechooser must be recreated to avoid a bug where getSelectedFile() was null (if a file is saved more than one time by doubleclicking on an existing file)
		reloadSaveFileChooser();

		setAvailableFileFilters(uxf);
		saveFileChooser.setFileFilter(filefilter);

		int returnVal = saveFileChooser.showSaveDialog(Umlet.getInstance().getGUI());
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File selectedFileWithExt = new File(saveFileChooser.getSelectedFile().getName() + "." + fileextensions.get(saveFileChooser.getFileFilter()));
			// We must check for the file with and without extension (saving without adding the extension automatically adds the selected extension)
			if (saveFileChooser.getSelectedFile().exists() || selectedFileWithExt.exists()) {
				int overwriteQuestionResult = JOptionPane.showConfirmDialog(Umlet.getInstance().getGUI(), "File already exists! Overwrite?", "Overwrite File", JOptionPane.YES_NO_OPTION);
				if (overwriteQuestionResult == JOptionPane.NO_OPTION) return chooseFileName(uxf, filefilter);
			}
			fileName = saveFileChooser.getSelectedFile().getAbsolutePath();
		}
		return fileName;
	}

	/**
	 * Updates the available FileFilter to "only uxf" or "all but uxf"
	 * 
	 * @param uxf
	 *            If this param is set, only uxf is visible, otherwise all but oxf is visible
	 */
	private void setAvailableFileFilters(boolean uxf) {
		if (uxf) {
			saveFileChooser.resetChoosableFileFilters();
			saveFileChooser.addChoosableFileFilter(filteruxf);
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

	protected class UMLetFileFilter extends FileFilter {
		private String format;
		private String description;

		protected UMLetFileFilter(String format, String description) {
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
	public boolean equals(Object o) {
		if ((o instanceof DiagramFileHandler) && (o != null)) {
			if (this.file != null) return this.getFullPathName().equals(((DiagramFileHandler) o).getFullPathName());
		}

		return false;
	}

}
