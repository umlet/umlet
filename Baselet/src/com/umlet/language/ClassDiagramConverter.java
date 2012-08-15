package com.umlet.language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.FontHandler;
import com.baselet.diagram.command.AddElement;
import com.baselet.element.GridElement;
import com.umlet.element.Class;
import com.umlet.language.java.Field;
import com.umlet.language.java.JavaClass;
import com.umlet.language.java.JavaClass.ClassRole;
import com.umlet.language.java.Method;
import com.umlet.language.java.bcel.BcelJavaClass;
import com.umlet.language.java.jp.JpJavaClass;

/**
 * Creates a class element from a filename pointing to a .class or .java file according to UML standards, 
 * adds the class to the current diagram and resizes this class element to minimum size where all text is visible.
 * 
 * @author Lisi Bluemelhuber
 *
 */
public class ClassDiagramConverter {
	
	private static final int GRIDSIZE = Main.getInstance().getDiagramHandler().getGridSize();
	
	public void createClassDiagram(String filename) {
		List<String> fileNames = new ArrayList<String>();
		fileNames.add(filename);
		createClassDiagrams(fileNames);
	}

	public void createClassDiagrams(List<String> filesToOpen) {
		List<GridElement> elements = new ArrayList<GridElement>();
		for (String filename: filesToOpen) {
			elements.add(createElement(filename));
		}
		determineLocations(elements);
		addElementsToDiagram(elements);
	}

	private GridElement createElement(String filename) {
		JavaClass parsedClass = parseFile(filename);

		GridElement clazz = new Class();
		if (parsedClass != null) {
			clazz.setPanelAttributes(getElementProperties(parsedClass));
		}
		adjustSize(clazz);
		return clazz;
	}

	private void determineLocations(List<GridElement> elements) {
		int maxHeight = 0;
		int sumWidth = 0;
		for (GridElement e: elements) {
			if (e.getSize().height > maxHeight) {
				maxHeight = e.getSize().height;
			}
			sumWidth += e.getSize().width;
		}
		// start with a rectangle with one row with all elements in it and determine
		// the multiplicator by solving: (x / m) / (y * m) = desired relation of width to height  
		double m = Math.sqrt(sumWidth / (0.4 * maxHeight));
		int desiredWidth = (int) (sumWidth / m);
		
		Collections.sort(elements, new GridElementHeightSorter()); // descending
		
		int curWidth = GRIDSIZE; 
		int curHeight = GRIDSIZE;
		int maxHeightThisRow = 0;
		for (GridElement e: elements) {
			e.setLocation(curWidth, curHeight);
			if (e.getSize().height > maxHeightThisRow) {
				maxHeightThisRow = e.getSize().height;
			}
			if (curWidth > desiredWidth) { 
				curHeight += maxHeightThisRow + GRIDSIZE;
				curWidth = GRIDSIZE;
				maxHeightThisRow = 0;
			} else {
				curWidth += e.getSize().width + GRIDSIZE;
			}
		}
	}

	private void addElementsToDiagram(List<GridElement> elements) {
		DiagramHandler handler = Main.getInstance().getDiagramHandler();

		for (GridElement e: elements) {
			new AddElement(e, 
					handler.realignToGrid(e.getLocation().x),
					handler.realignToGrid(e.getLocation().y), false).execute(handler);
		}
		handler.setChanged(true);		
	}
	
	/**
	 * Adjusts a Class GridElement to the minimum size where all text is visible.
	 * 
	 * @param clazz
	 */
	private void adjustSize(GridElement clazz) {
		String[] strings = clazz.getPanelAttributes().split("\n");
		//GridElement clazz not yet fully initialized, cannot call clazz.getHandler();  
		FontHandler fontHandler = Main.getInstance().getDiagramHandler().getFontHandler(); 
		
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
		
		clazz.setSize(align(width), align(height)); // width&height must be multiples of grid size
		clazz.repaint();
	}
	
	private int align(int n) {
		return n - (n % GRIDSIZE) + GRIDSIZE;
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
		try {
			if (getExtension(filename).equals("java")) {
				return parseJavaFile(filename);
			} else if (getExtension(filename).equals("class")) {
				return parseClassFile(filename);
			}
		} catch (Exception ignored) {}
		return null;
	}
	
	private JavaClass parseJavaFile(String filename) {
		try {
			return new JpJavaClass(filename);
		} catch (ClassParserException e) {
			return null;
		}
	}
	
	private JavaClass parseClassFile(String filename) {
		return new BcelJavaClass(filename);
	}
	
	private String getExtension(String filename) {
		int dotPosition = filename.lastIndexOf(".");
		return filename.substring(dotPosition + 1, filename.length()); 
	}
}
