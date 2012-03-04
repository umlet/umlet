package com.umlet.element.experimental;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.control.Utils;

public class ElementInitializer {

	private final static Logger log = Logger.getLogger(Utils.getClassName());
	
	private ClassLoader classLoader;
	private List<Class<?>> classList;
	
	private static ElementInitializer instance;

	public static ElementInitializer getInstance() {
		if (instance == null) {
			 instance = new ElementInitializer();
		}
		return instance;
	}
	
	

	public NewGridElement getGridElementFromId(String id) throws Exception {
		for (Class<?> classEntry: classList) {
			if (classEntry.getAnnotation(Id.class).value().equals(id)) {
				return (NewGridElement) classEntry.newInstance();
		    }
		}
		throw new IOException("No class with id " + id + " found");
	}

	public ElementInitializer() {
		super();
		classLoader = Thread.currentThread().getContextClassLoader(); // use classloader of current thread (not systemclassloader - important for eclipse)
		try {
			classList = buildClassList();
		} catch (Exception e) {
			log.error("Error at initalizing class map", e);
		}
	}



	public List<Class<?>> buildClassList() throws IOException, ClassNotFoundException, URISyntaxException {
		String[] possiblePackages = new String[] {"com" + File.separator + "umlet" + File.separator + "element" + File.separator + "experimental"};
	    List<Class<?>> classes = new ArrayList<Class<?>>();
		for (String possPackage : possiblePackages) {
			Enumeration<URL> classUrlList = classLoader.getResources(possPackage);
			while (classUrlList.hasMoreElements()) {
				URL resource = classUrlList.nextElement();
				Path path = Paths.get(resource.toURI());
			      DirectoryStream<Path> txtFiles = Files.newDirectoryStream(path, "*.class"); 
			        for (Path textFile : txtFiles) {
			        	String className = textFile.toString();
			        	String relativeClassName = className.substring(className.indexOf(possPackage));
			        	String correctedClassName = relativeClassName.substring(0, relativeClassName.length() - ".class".length());
			        	Class<?> classObj = Class.forName(correctedClassName.replace(File.separator, "."));
						Id idAnnotation = classObj.getAnnotation(Id.class);
						if (idAnnotation != null) classes.add(classObj);
			        }
			}
		}
		return classes;
	}

}
