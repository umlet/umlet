package com.umlet.element.experimental;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.control.Utils;

public class ElementInitializer {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

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
		try {
			classList = buildClassList();
		} catch (Exception e) {
			log.error("Error at initalizing class map", e);
		}
	}

	public List<Class<?>> buildClassList() throws IOException, ClassNotFoundException {
		List<Class<?>> classesWithId = new ArrayList<Class<?>>();
		List<Class<?>> classObjects = com.baselet.control.Path.getAllClassesInPackage("com.umlet.element.experimental");
		for (Class<?> classObj : classObjects) {
			Id idAnnotation = classObj.getAnnotation(Id.class);
			if (idAnnotation != null) classesWithId.add(classObj);
		}
		return classesWithId;
	}

}
