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
	
	private List<Class<?>> classList;
	
	private static ElementInitializer instance;
	
	private static String pathToClasses = "com" + File.separator + "umlet" + File.separator + "element" + File.separator + "experimental";

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
		try {
			classList = buildClassList();
		} catch (Exception e) {
			log.error("Error at initalizing class map", e);
		}
	}



	public List<Class<?>> buildClassList() throws IOException, ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
			List<String> classStringList = getNewGridElementList();
			for (String classString : classStringList) {
				String className = classString.toString();
				String relativeClassName = className.substring(className.indexOf(pathToClasses));
				String correctedClassName = relativeClassName.substring(0, relativeClassName.length() - ".class".length());
				Class<?> classObj = Class.forName(correctedClassName.replace(File.separator, "."));
				Id idAnnotation = classObj.getAnnotation(Id.class);
				if (idAnnotation != null) classes.add(classObj);
			}
		return classes;
	}
	
	private List<String> getNewGridElementList() throws IOException {
		List<String> fileList = new ArrayList<>();
		String homePath = com.baselet.control.Path.homeProgram();
		addAll(fileList, Paths.get(homePath));
		return fileList;
	}

	private void addAll(List<String> fileList, Path path) throws IOException {
		DirectoryStream<Path> subPathes = Files.newDirectoryStream(path); 
		for (Path subPath : subPathes) {
			if (Files.isDirectory(subPath)) addAll(fileList, subPath);
			else {
				String pathAsString = subPath.toString();
				if (pathAsString.endsWith(".class") && pathAsString.contains(pathToClasses)) fileList.add(subPath.toString());
			}
		}
	}

}
