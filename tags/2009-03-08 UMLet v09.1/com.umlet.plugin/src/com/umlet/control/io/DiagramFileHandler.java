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

import com.umlet.control.*;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.custom.CustomElement;
import com.umlet.custom.CustomElementSecurityManager;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Group;

public class DiagramFileHandler {
	
	private static JFileChooser openFileChooser;
	
	public static DiagramFileHandler createInstance(DiagramHandler diagramHandler, File file)
	{
		return new DiagramFileHandler(diagramHandler, file);
	}
	
	public static JFileChooser getOpenFileChooser() {
		if(openFileChooser == null) {
			openFileChooser = new JFileChooser(System.getProperty("user.dir"));
			openFileChooser.setFileFilter(new FileFilter() {
		      	public boolean accept(File f) {
		      		return (f.getName().endsWith(".uxf") || f.isDirectory());
		    	}
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
	private DiagramHandler diagramHandler;
	private File file;
	private JFileChooser fileChooser;
	private HashMap<String,FileFilter> filters;
	private HashMap<FileFilter, String> fileextensions;
	
	protected DiagramFileHandler(DiagramHandler diagramHandler, File file)
	{
		this.diagramHandler = diagramHandler;
		if(file != null)
			this.fileName = file.getName();
		else
			this.fileName = "new.uxf";
		this.file = file;
		if(this.file != null)
			this.fileChooser = new JFileChooser(this.file);
		else
			this.fileChooser = new JFileChooser(System.getProperty("user.dir"));
		
		
		FileFilter filteruxf = new UMLetFileFilter("uxf", "UMLet diagram format");
      	this.fileChooser.addChoosableFileFilter(filteruxf);
      	FileFilter filterpdf = new UMLetFileFilter("pdf", "PDF format");
      	this.fileChooser.addChoosableFileFilter(filterpdf);
      	FileFilter filtersvg = new UMLetFileFilter("svg", "SVG format");
      	this.fileChooser.addChoosableFileFilter(filtersvg);
      	FileFilter filtereps = new UMLetFileFilter("eps", "EPS format");
      	this.fileChooser.addChoosableFileFilter(filtereps);
      	this.fileChooser.setAcceptAllFileFilterUsed(false);
      	
      	this.filters = new HashMap<String,FileFilter>();
      	this.filters.put("uxf", filteruxf);
      	this.filters.put("pdf", filterpdf);
      	this.filters.put("svg", filtersvg);
      	this.filters.put("eps", filtereps);
      	
      	this.fileextensions = new HashMap<FileFilter,String>();
      	this.fileextensions.put(filteruxf,"uxf");
      	this.fileextensions.put(filterpdf,"pdf");
      	this.fileextensions.put(filtersvg,"svg");
      	this.fileextensions.put(filtereps,"eps");
      	
      	for(String format : ImageIO.getWriterFileSuffixes())
      	{
      		FileFilter filter = new UMLetFileFilter(format, format.toUpperCase() + " Image");
      		this.fileChooser.addChoosableFileFilter(filter);
      		this.filters.put(format, filter);
      		this.fileextensions.put(filter, format);
      	}
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public String getFullPathName() {
		if(this.file != null)
			return this.file.getAbsolutePath();
		return "";
	}
	
	private void setFileName(String fileName)
	{
		this.fileName = fileName;
		Umlet.getInstance().getGUI().updateDiagramName(this.diagramHandler, this.diagramHandler.getName());
	}
	
	private void generateElementTree(Document doc, List<Entity> entities, Element current, Group group)
	  {
		//list of elements that are not inserted yet (to increase performance)
	  	List<Entity> toBeCheckedAgain = new ArrayList<Entity>();
	  	List<Group> insert_groups = new ArrayList<Group>();
	    for (Entity e : entities) 
	    {
	    	//only insert element in right grouping element
	    	boolean insert_here = false;
	    	if(group == null && e.getGroup() == null)
	    		insert_here = true;
	    	else if(group != null)
	    		if(group.equals(e.getGroup()))
	    			insert_here = true;	
	    	
	    	if(insert_here)
	    	{
	    		if(e instanceof Group)
	    			insert_groups.add((Group)e);
	    		else //insert normal entity element
	    		{
			    	java.lang.Class<? extends Entity> c = e.getClass();
			        String sElType = c.getName();
			        int[] coor = e.getCoordinates();
			        String sElPanelAttributes = e.getPanelAttributes();
			        String sElAdditionalAttributes = e.getAdditionalAttributes();
			        
			        Element el=doc.createElement("element");
			        current.appendChild(el);
			        
			        Element elType= doc.createElement("type");
			        elType.appendChild(doc.createTextNode(sElType));
			        el.appendChild(elType);
		
			        Element elCoor= doc.createElement("coordinates");
			        el.appendChild(elCoor);
			        
			        Element elX= doc.createElement("x");
			        elX.appendChild(doc.createTextNode(""+coor[0]));
			        elCoor.appendChild(elX);
		
			        Element elY= doc.createElement("y");
			        elY.appendChild(doc.createTextNode(""+coor[1]));
			        elCoor.appendChild(elY);
			        
			        Element elW= doc.createElement("w");
			        elW.appendChild(doc.createTextNode(""+coor[2]));
			        elCoor.appendChild(elW);
			        
			        Element elH= doc.createElement("h");
			        elH.appendChild(doc.createTextNode(""+coor[3]));
			        elCoor.appendChild(elH);
			        
			        Element elPA= doc.createElement("panel_attributes");
			        elPA.appendChild(doc.createTextNode(sElPanelAttributes));
			        el.appendChild(elPA);
			        
			        Element elAA= doc.createElement("additional_attributes");
			        elAA.appendChild(doc.createTextNode(sElAdditionalAttributes));
			        el.appendChild(elAA);
			        
			        if(e instanceof CustomElement) {
			        	Element elCO = doc.createElement("custom_code");
			        	elCO.appendChild(doc.createTextNode(((CustomElement)e).getCode()));
			        	el.appendChild(elCO);
			        }
	    		}
	    	}
	    	else
	    		toBeCheckedAgain.add(e);
	    }
	    
	    for(Group g : insert_groups)
	    {
			Element el = doc.createElement("group");
			current.appendChild(el);
			generateElementTree(doc, toBeCheckedAgain, el, g);
	    }
	  }
	  
	 protected String createStringToBeSaved() {
	    Component[] components = this.diagramHandler.getDrawPanel().getComponents();
	    List<Entity> entities = new ArrayList<Entity>();
	    for(int i=0; i < components.length; i++) {
	    	if(components[i] instanceof Entity)
	    		entities.add((Entity)components[i]);
	    }
	
	    DocumentBuilder db= null;
	    try
	    {
	      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	      db = dbf.newDocumentBuilder();
	    } catch (Exception e) {
	      System.err.println("Error saving XML.");      
	    }
	    Document doc= db.newDocument();
	    
	    Element root= doc.createElement("umlet_diagram");
	    doc.appendChild(root);
	    
	    //save helptext
	    Element help = doc.createElement("help_text");
	    help.appendChild(doc.createTextNode(this.diagramHandler.getHelpText()));
	    root.appendChild(help);
	    
	    //save elements (group = null = rootlayer)
	    this.generateElementTree(doc, entities, root, null);
	    
	    //output the stuff...
	    StringWriter stringWriter= null;
	    try {
	      DOMSource source = new DOMSource(doc);
	      stringWriter= new StringWriter();
	      StreamResult result= new StreamResult(stringWriter);
	    
	      TransformerFactory transFactory = TransformerFactory.newInstance();
	      Transformer transformer = transFactory.newTransformer();
	
	      transformer.transform(source, result);
	    } catch (Exception e){
	      System.err.println("Error saving XML.");
	      e.printStackTrace();
	    }
	
	    return stringWriter.toString();
	
	  }
	  
	  public void doOpen() {   
		try {
		    try {
	            SAXParser parser= SAXParserFactory.newInstance().newSAXParser();
	            FileInputStream input = new FileInputStream(this.file);
	            XMLContentHandler xmlhandler = new XMLContentHandler(this.diagramHandler);
	            parser.parse(input, xmlhandler);
	        } catch (SAXException e) {
		          System.err.println("Error parsing the inputstream.");
		          System.err.println(e.getMessage());      
		    }
	    /*} catch (IOException ioe) {
	    	System.err.println("IOException: "+ioe);*/
	    } catch (Exception e) {
	        StackTraceElement[] trace=e.getStackTrace();
	        String out="";
	        for (int i=0; i<trace.length; i++) {
	            out+=trace[i].toString()+"\n";
	        }
	        if(Umlet.getInstance().getGUI() != null)
	        	Umlet.getInstance().getGUI().setPropertyPanelText("EX="+out);
	        System.err.println("EXCEPTION: "+out);
	    }   
	  }

	  public boolean doSaveAs(String fileextension) {
	    String fileName=this.chooseFileName(this.filters.get(fileextension));
	    String extension = this.fileextensions.get(this.fileChooser.getFileFilter());
	    if (fileName==null) return false;
	    if(!fileName.endsWith("." + extension))
	    	fileName += "." + extension;
	    
	    if(extension.equals("uxf"))
	    { 
	    	this.file = new File(fileName);
	    	this.setFileName(this.file.getName());
	    	return save();
	    }
	    else if(extension.equals("pdf"))
	    	this.doSaveAsPdf(fileName);
	    else if(extension.equals("svg"))
	    	this.doSaveAsSvg(fileName);
	    else if(extension.equals("eps"))
	    	this.doSaveAsEps(fileName);
	    else
	    	this.doSaveAsIMG(extension, fileName);
	    
	    return false;
	  }
	  
	  public boolean doSave() {
        if (file==null) 
        	return doSaveAs("uxf");
        else if(!file.exists())
        	return doSaveAs("uxf");
        else 
        	return save(); 
	  }
	  
	  public boolean isImageExtension(String ext)
	  {
		  for(String format : ImageIO.getWriterFileSuffixes())
			  if(ext.toLowerCase().equals(format))
				  return true;
				  
		  return false;
	  }
	  
	  public void doSaveAsSvg(String fileName) {
		CustomElementSecurityManager.addThreadPrivileges(Thread.currentThread(), fileName);  
        GenSvg.createAndOutputSvgToFile(fileName, this.diagramHandler);
        CustomElementSecurityManager.remThreadPrivileges(Thread.currentThread());
	  }
	  
	  public void doSaveAsEps(String fileName) {
		CustomElementSecurityManager.addThreadPrivileges(Thread.currentThread(), fileName);
        GenEps.getInstance().createAndOutputEPSToFile(fileName, this.diagramHandler);
        CustomElementSecurityManager.remThreadPrivileges(Thread.currentThread());
	  }  
	  
	  public void doSaveAsPdf(String fileName) {
		CustomElementSecurityManager.addThreadPrivileges(Thread.currentThread(), fileName);
        GenPdf.getInstance().createAndOutputPdfToFile(fileName, this.diagramHandler);
        CustomElementSecurityManager.remThreadPrivileges(Thread.currentThread());
	  }
	  
	  public void doSaveAsIMG(String imgtype, String fileName) {
		CustomElementSecurityManager.addThreadPrivileges(Thread.currentThread(), fileName);
        GenPic.getInstance().createAndOutputImgToFile(imgtype, fileName, this.diagramHandler);
        CustomElementSecurityManager.remThreadPrivileges(Thread.currentThread());
	  }
	  
	  private boolean save() {
	    String tmp=this.createStringToBeSaved();
	    try {
	      PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
	      out.print(tmp);
	      out.close();
	    } catch (java.io.IOException e) {
	      return false;
	    }
	    return true;
	  }
	  
	  public String chooseFileName(FileFilter filefilter) {
	    	String fileName=null;
	    	this.fileChooser.setFileFilter(filefilter);
	        int returnVal = fileChooser.showSaveDialog(Umlet.getInstance().getGUI());
	 	    if(returnVal == JFileChooser.APPROVE_OPTION) {
		      fileName=fileChooser.getSelectedFile().getAbsolutePath();
		    }
		    return fileName;
	  }
	  
	  protected class UMLetFileFilter extends FileFilter
	  {
		  private String format;
		  private String description;
		  
		  protected UMLetFileFilter(String format, String description)
		  {
			  this.format = format;
			  this.description = description;
		  }

	      public boolean accept(File f) {
	    	  return (f.getName().endsWith("." + this.format) || f.isDirectory());
	      }
	      
	      public String getDescription() {
	    	  return this.description + " (*." + this.format + ")";
	      }		 
	  }

	@Override
	public boolean equals(Object o) {
		if(o instanceof DiagramFileHandler && o != null)
		{
			if(this.file != null)
				return this.getFullPathName().equals(((DiagramFileHandler)o).getFullPathName());
		}
		
		return false;
	}
	  
	  
}