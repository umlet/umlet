package com.umlet.control.io;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.custom.CustomElement;
import com.umlet.custom.CustomElementSecurityManager;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Group;

public class DiagramFileHandler {

	private static JFileChooser openFileChooser;

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

	public static String chooseFileName() {
		return Umlet.getInstance().getGUI().chooseFileName();
	}

	private String fileName;
	private DiagramHandler handler;
	private File file;
	private JFileChooser fileChooser;
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

		// Set the initial target location for the fileChooser
		if (this.file != null) this.fileChooser = new JFileChooser(this.file);
		else this.fileChooser = new JFileChooser(System.getProperty("user.dir"));

		this.fileChooser.setAcceptAllFileFilterUsed(false); // We don't want "all files" as a choice

		this.fileChooser.addChoosableFileFilter(filteruxf);

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
			System.err.println("Error saving XML.");
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
			System.err.println("Error saving XML.");
			e.printStackTrace();
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
				System.err.println("Error parsing the inputstream.");
				System.err.println(e.getMessage());
			}
			/*
			 * } catch (IOException ioe) {
			 * System.err.println("IOException: "+ioe);
			 */
		} catch (Exception e) {
			StackTraceElement[] trace = e.getStackTrace();
			String out = "";
			for (int i = 0; i < trace.length; i++) {
				out += trace[i].toString() + "\n";
			}
			if (Umlet.getInstance().getGUI() != null) Umlet.getInstance().getGUI().setPropertyPanelText("EX=" + out);
			System.err.println("EXCEPTION: " + out);
		}
	}

	public boolean doSaveAs(String fileextension) {
		String fileName;
		if (fileextension.equals("uxf")) {
			fileName = this.chooseFileName(true, this.filters.get(fileextension));
		}
		else {
			fileName = this.chooseFileName(false, this.filters.get(fileextension));
		}
		String extension = this.fileextensions.get(this.fileChooser.getFileFilter());
		if (fileName == null) return false;
		if (!fileName.endsWith("." + extension)) fileName += "." + extension;

		if (extension.equals("uxf")) {
			this.file = new File(fileName);
			this.setFileName(this.file.getName());
			return save();
		}
		else {
			try {
				doExportAs(extension, fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public File saveTempFileWithDiagram(String name, String extension) throws Exception {
		File tempFile = new File(name + "." + extension);
		tempFile.deleteOnExit();

		if (extension.equals("uxf")) save(tempFile);
		else doExportAs(extension, tempFile.getName());

		return tempFile;
	}

	public boolean doSave() {
		if (file == null) return doSaveAs("uxf");
		else if (!file.exists()) return doSaveAs("uxf");
		else return save();
	}

	public boolean isImageExtension(String ext) {
		for (String format : ImageIO.getWriterFileSuffixes())
			if (ext.toLowerCase().equals(format)) return true;

		return false;
	}

	public void doExportAs(String extension, String fileName) throws Exception {

		// We must deselect all before exporting the diagram
		handler.getDrawPanel().getSelector().deselectAll();

		// We must zoom to the defaultGridsize before execution
		int oldZoom = handler.getGridSize();
		handler.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);

		CustomElementSecurityManager.addThreadPrivileges(Thread.currentThread(), fileName);

		if (extension.equals("svg")) GenSvg.createAndOutputSvgToFile(fileName, this.handler);
		else if (extension.equals("eps")) GenEps.getInstance().createAndOutputEPSToFile(fileName, this.handler);
		else if (extension.equals("pdf")) GenPdf.getInstance().createAndOutputPdfToFile(fileName, this.handler);
		else if (isImageExtension(extension)) GenPic.getInstance().createAndOutputImgToFile(extension, fileName, this.handler);
		else throw new IllegalArgumentException(extension + " is an invalid format");

		CustomElementSecurityManager.remThreadPrivileges(Thread.currentThread());

		// And zoom back to the oldGridsize after execution
		handler.setGridAndZoom(oldZoom, false);

	}

	private boolean save() {
		// If save is called without a parameter it uses the class variable "file"
		return save(file);
	}

	private boolean save(File saveToFile) {
		String tmp = this.createStringToBeSaved();
		try {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(saveToFile), "UTF-8"));
			out.print(tmp);
			out.close();
		} catch (java.io.IOException e) {
			return false;
		}
		return true;
	}

	public String chooseFileName(boolean uxf, FileFilter filefilter) {
		String fileName = null;
		setAvailableFileFilters(uxf); // We set the available FileFilters
		this.fileChooser.setFileFilter(filefilter); // And select the right one
		int returnVal = fileChooser.showSaveDialog(Umlet.getInstance().getGUI());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile().getAbsolutePath();
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
			this.fileChooser.resetChoosableFileFilters();
			this.fileChooser.addChoosableFileFilter(filteruxf);
		}
		else {
			this.fileChooser.resetChoosableFileFilters();
			this.fileChooser.addChoosableFileFilter(filterbmp);
			this.fileChooser.addChoosableFileFilter(filtereps);
			this.fileChooser.addChoosableFileFilter(filtergif);
			this.fileChooser.addChoosableFileFilter(filterjpg);
			this.fileChooser.addChoosableFileFilter(filterpdf);
			this.fileChooser.addChoosableFileFilter(filterpng);
			this.fileChooser.addChoosableFileFilter(filtersvg);
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
