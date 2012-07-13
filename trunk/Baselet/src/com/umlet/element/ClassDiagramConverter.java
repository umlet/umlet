package com.umlet.element;

import com.baselet.diagram.FontHandler;
import com.baselet.element.GridElement;
import com.umlet.language.java.Field;
import com.umlet.language.java.JavaClass;
import com.umlet.language.java.JavaClass.ClassRole;
import com.umlet.language.java.Method;
import com.umlet.language.java.bcel.BcelJavaClass;
import com.umlet.language.java.jp.JpJavaClass;

/**
 * Creates a class element from a filename pointing to a .class or .java file according to UML standards.
 * Also provides the possibility to resize this class element to minimum size where all text is visible.
 * 
 * @author Lisi Bluemelhuber
 *
 */
public class ClassDiagramConverter {

	public GridElement createElement(String filename) {
		JavaClass parsedClass = parseFile(filename);

		GridElement clazz = new Class();
		clazz.setLocation(10, 10);
		clazz.setPanelAttributes(getElementProperties(parsedClass));
		return clazz;
	}
	
	/**
	 * Adjusts a Class GridElement to the minimum size where all text is visible.
	 * 
	 * @param clazz
	 */
	public void adjustSize(GridElement clazz) {
		String[] strings = clazz.getPanelAttributes().split("\n");
		FontHandler fontHandler = clazz.getHandler().getFontHandler();
		
		int width = 0;
		int height = strings.length;
		double heightTweaker = 0.1;
		for (String string: strings) {
			if (string.isEmpty()) {
				heightTweaker += 1;
			} else if (string.equals("--")) {
				heightTweaker += 0.5;
			}
			if (fontHandler.getTextWidth(string) > width) {
				width = (int) (fontHandler.getTextWidth(string) + fontHandler.getDistanceBetweenTexts()) + 10;
			}
		}
		height = (int) (fontHandler.getFontSize() + fontHandler.getDistanceBetweenTexts()) * (height - (int)heightTweaker);

		clazz.setSize(width, height);
		clazz.repaint();
	}
	
	private String getElementProperties(JavaClass parsedClass) {
		String attributes = "";
		
		attributes = createTopSection(parsedClass, attributes);
		attributes += "--\n";
		
		attributes = createMemberSection(parsedClass, attributes);
		attributes += "--\n";
		
		attributes = createMethodSection(parsedClass, attributes);
		attributes += "--\n";
		
		return attributes;
	}

	private String createMethodSection(JavaClass parsedClass, String attributes) {
		for (Method method: parsedClass.getMethods()) {
			attributes += method.getAccess() + method.getName() + "("+ method.getSignature()+ "): " + method.getReturnType() + "\n";
		}
		return attributes;
	}

	private String createMemberSection(JavaClass parsedClass, String attributes) {
		for (Field field: parsedClass.getFields()) {
			attributes += field.getAccess() + field.getName() + ": " + field.getType() + "\n";
		}
		return attributes;
	}

	private String createTopSection(JavaClass parsedClass, String attributes) {
		ClassRole role = parsedClass.getRole();
		if (role == ClassRole.INTERFACE) {
			attributes += "<<"+role+">>\n";
			attributes += parsedClass.getPackage()+"::"+parsedClass.getName();
		} else if (role == ClassRole.ABSTRACT){
			attributes += "/"+parsedClass.getPackage()+"::"+parsedClass.getName()+"/";
		} else {
			attributes += parsedClass.getPackage()+"::"+parsedClass.getName();
		}
		return attributes+="\n";
	}

	private JavaClass parseFile(String filename) {
		if (getExtension(filename).equals("java")) {
			return parseJavaFile(filename);
		} else if (getExtension(filename).equals("class")) {
			return parseClassFile(filename);
		}
		return null;
	}
	
	private JavaClass parseJavaFile(String filename) {
		return new JpJavaClass(filename);
	}
	
	private JavaClass parseClassFile(String filename) {
		return new BcelJavaClass(filename);
	}
	
	private String getExtension(String filename) {
		int dotPosition = filename.lastIndexOf(".");
		return filename.substring(dotPosition + 1, filename.length()); 
	}
}
