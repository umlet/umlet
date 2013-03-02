package com.umlet.language;

import java.util.ArrayList;
import java.util.List;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.FontHandler;
import com.baselet.diagram.command.AddElement;
import com.baselet.element.GridElement;
import com.umlet.element.Class;
import com.umlet.language.java.Accessible.AccessFlag;
import com.umlet.language.java.Field;
import com.umlet.language.java.JavaClass;
import com.umlet.language.java.JavaClass.ClassRole;
import com.umlet.language.java.Method;
import com.umlet.language.java.bcel.BcelJavaClass;
import com.umlet.language.java.jp.JpJavaClass;
import com.umlet.language.sorting.AlphabetLayout;
import com.umlet.language.sorting.HeightLayout;
import com.umlet.language.sorting.PackageLayout;
import com.umlet.language.sorting.RelationLayout;

/**
 * Creates a class element from a filename pointing to a .class or .java file according to UML standards, 
 * adds the class to the current diagram and resizes this class element to minimum size where all text is visible.
 * 
 * @author Lisi Bluemelhuber
 *
 */
public class ClassDiagramConverter {
	
	private final int GRIDSIZE;
	
	public ClassDiagramConverter() {
		 GRIDSIZE = Main.getInstance().getDiagramHandler().getGridSize();
	}
	
	public void createClassDiagram(String filename) {
		List<String> fileNames = new ArrayList<String>();
		fileNames.add(filename);
		createClassDiagrams(fileNames);
	}

	public void createClassDiagrams(List<String> filesToOpen) {
		List<SortableElement> elements = new ArrayList<SortableElement>();
		for (String filename: filesToOpen) {
			SortableElement element = createElement(filename);
			if (element != null) {
				elements.add(element);
			}
		}
		
		switch(Constants.generateClassSortings) {
			case PACKAGE: new PackageLayout().layout(elements); break;
			case ALPHABET: new AlphabetLayout().layout(elements); break;
			case RELATIONS: new RelationLayout().layout(elements); break;
			default: new HeightLayout().layout(elements); // by height
		}
		
		addElementsToDiagram(elements);
	}

	private SortableElement createElement(String filename) {
		JavaClass parsedClass = parseFile(filename);
		if (parsedClass == null) {
			return null;
		}

		GridElement clazz = new Class();
		clazz.setPanelAttributes(getElementProperties(parsedClass));
		adjustSize(clazz);
		return new SortableElement(clazz, parsedClass);
	}

	private void addElementsToDiagram(List<SortableElement> elements) {
		DiagramHandler handler = Main.getInstance().getDiagramHandler();

		for (SortableElement e: elements) {
			new AddElement(e.getElement(), 
					handler.realignToGrid(e.getElement().getRectangle().x),
					handler.realignToGrid(e.getElement().getRectangle().y), false).execute(handler);
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
		
		attributes = createFieldSection(parsedClass, attributes);
		attributes += "--\n";
		
		attributes = createMethodSection(parsedClass, attributes);
		attributes += "--\n";
		
		return attributes;
	}

	private String createMethodSection(JavaClass parsedClass, String attributes) {
		for (Method method: parsedClass.getMethods()) {
			if (Constants.generateClassMethods == MethodOptions.PUBLIC && method.getAccess() == AccessFlag.PUBLIC) {
				attributes += getMethodString(method);
			} else if (Constants.generateClassMethods == MethodOptions.ALL) {
				attributes += getMethodString(method);
			}
		}
		return attributes;
	}
	
	private String getMethodString(Method method) {
		if (Constants.generateClassSignatures == SignatureOptions.PARAMS_ONLY) {
			return method.getAccess() + method.getName() + "(" + method.getSignature() + ")\n";
		} else if (Constants.generateClassSignatures == SignatureOptions.RETURN_ONLY) {
			return method.getAccess() + method.getName() + ": " + method.getReturnType() +"\n";
		} else {
			return method.getAccess() + method.getName() + "("+ method.getSignature()+ "): " + method.getReturnType() + "\n";
		}
	}

	private String createFieldSection(JavaClass parsedClass, String attributes) {
		for (Field field: parsedClass.getFields()) {
			if (Constants.generateClassFields == FieldOptions.PUBLIC && field.getAccess() == AccessFlag.PUBLIC) {
				attributes += field.getAccess() + field.getName() + ": " + field.getType() + "\n";
			} else if (Constants.generateClassFields == FieldOptions.ALL) {
				attributes += field.getAccess() + field.getName() + ": " + field.getType() + "\n";
			}
		}
		return attributes;
	}

	private String createTopSection(JavaClass parsedClass, String attributes) {
		ClassRole role = parsedClass.getRole();
		if (role == ClassRole.INTERFACE) {
			attributes += "<<"+role+">>\n";
			attributes += getClassName(parsedClass);
		} else if (role == ClassRole.ABSTRACT){
			attributes += "/"+getClassName(parsedClass)+"/";
		} else {
			attributes += getClassName(parsedClass);
		}
		return attributes+="\n";
	}
	
	public static String getClassName(JavaClass parsedClass) {
		String result = "";
		if (Constants.generateClassPackage) {
			result += parsedClass.getPackage()+"::";
		} 
		result += parsedClass.getName();
		return result;
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
