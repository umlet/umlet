package com.baselet.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.ConfigClassGen;
import com.baselet.control.enums.ElementId;
import com.baselet.control.enums.generator.FieldOptions;
import com.baselet.control.enums.generator.MethodOptions;
import com.baselet.control.enums.generator.SignatureOptions;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.FontHandler;
import com.baselet.element.interfaces.GridElement;
import com.baselet.generator.java.Field;
import com.baselet.generator.java.JavaClass;
import com.baselet.generator.java.Method;
import com.baselet.generator.java.Accessible.AccessFlag;
import com.baselet.generator.java.JavaClass.ClassRole;
import com.baselet.generator.java.bcel.BcelJavaClass;
import com.baselet.generator.java.jp.ClassParserException;
import com.baselet.generator.java.jp.JpJavaClass;
import com.baselet.generator.sorting.AlphabetLayout;
import com.baselet.generator.sorting.HeightLayout;
import com.baselet.generator.sorting.PackageLayout;
import com.baselet.generator.sorting.RelationLayout;
import com.baselet.generator.sorting.SortableElement;
import com.baselet.gui.command.AddElement;
import com.umlet.elementnew.ElementFactorySwing;

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
		GRIDSIZE = CurrentDiagram.getInstance().getDiagramHandler().getGridSize();
	}

	public void createClassDiagram(String filename) {
		List<String> fileNames = new ArrayList<String>();
		fileNames.add(filename);
		createClassDiagrams(fileNames);
	}

	public void createClassDiagrams(List<String> filesToOpen) {
		List<SortableElement> elements = new ArrayList<SortableElement>();
		for (String filename : filesToOpen) {
			SortableElement element = createElement(filename);
			if (element != null) {
				elements.add(element);
			}
		}

		switch (ConfigClassGen.getInstance().getGenerateClassSortings()) {
			case PACKAGE:
				new PackageLayout().layout(elements);
				break;
			case ALPHABET:
				new AlphabetLayout().layout(elements);
				break;
			case RELATIONS:
				new RelationLayout().layout(elements);
				break;
			default:
				new HeightLayout().layout(elements); // by height
		}

		addElementsToDiagram(elements);
	}

	private SortableElement createElement(String filename) {
		JavaClass parsedClass = parseFile(filename);
		if (parsedClass == null) {
			return null;
		}

		String propertiesText = getElementProperties(parsedClass);
		List<String> propList = Arrays.asList(propertiesText.split("\n"));
		Rectangle initialSize = adjustSize(propList);
		GridElement clazz = ElementFactorySwing.create(ElementId.UMLClass, initialSize, propertiesText, null, CurrentDiagram.getInstance().getDiagramHandler());
		return new SortableElement(clazz, parsedClass);
	}

	private void addElementsToDiagram(List<SortableElement> elements) {
		DiagramHandler handler = CurrentDiagram.getInstance().getDiagramHandler();

		for (SortableElement e : elements) {
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
	 * @return
	 */
	private Rectangle adjustSize(List<String> strings) {
		// GridElement clazz not yet fully initialized, cannot call clazz.getHandler();
		FontHandler fontHandler = CurrentDiagram.getInstance().getDiagramHandler().getFontHandler();

		int width = 0;
		int height = strings.size();
		double heightTweaker = 0.1;
		for (String string : strings) {
			if (string.isEmpty()) {
				heightTweaker += 1;
			}
			else if (string.equals("--")) {
				heightTweaker += 0.5;
			}
			if (fontHandler.getTextWidth(string) > width) {
				width = (int) (fontHandler.getTextWidth(string) + fontHandler.getDistanceBetweenTexts()) + 10;
			}
		}
		height = (int) (fontHandler.getFontSize() + fontHandler.getDistanceBetweenTexts()) * (height - (int) heightTweaker);

		return new Rectangle(0, 0, align(width), align(height)); // width&height must be multiples of grid size
	}

	private int align(int n) {
		return n - n % GRIDSIZE + GRIDSIZE;
	}

	private String getElementProperties(JavaClass parsedClass) {
		StringBuilder sb = new StringBuilder("");

		createTopSection(parsedClass, sb);
		sb.append("--\n");

		createFieldSection(parsedClass, sb);
		sb.append("--\n");

		createMethodSection(parsedClass, sb);
		sb.append("--\n");

		return sb.toString();
	}

	private void createMethodSection(JavaClass parsedClass, StringBuilder sb) {
		for (Method method : parsedClass.getMethods()) {
			if (ConfigClassGen.getInstance().getGenerateClassMethods() == MethodOptions.PUBLIC && method.getAccess() == AccessFlag.PUBLIC) {
				sb.append(getMethodString(method));
			}
			else if (ConfigClassGen.getInstance().getGenerateClassMethods() == MethodOptions.ALL) {
				sb.append(getMethodString(method));
			}
		}
	}

	private String getMethodString(Method method) {
		if (ConfigClassGen.getInstance().getGenerateClassSignatures() == SignatureOptions.PARAMS_ONLY) {
			return method.getAccess() + method.getName() + "(" + method.getSignature() + ")\n";
		}
		else if (ConfigClassGen.getInstance().getGenerateClassSignatures() == SignatureOptions.RETURN_ONLY) {
			return method.getAccess() + method.getName() + ": " + method.getReturnType() + "\n";
		}
		else {
			return method.getAccess() + method.getName() + "(" + method.getSignature() + "): " + method.getReturnType() + "\n";
		}
	}

	private void createFieldSection(JavaClass parsedClass, StringBuilder sb) {
		for (Field field : parsedClass.getFields()) {
			if (ConfigClassGen.getInstance().getGenerateClassFields() == FieldOptions.PUBLIC && field.getAccess() == AccessFlag.PUBLIC) {
				sb.append(field.getAccess()).append(field.getName()).append(": ").append(field.getType()).append("\n");
			}
			else if (ConfigClassGen.getInstance().getGenerateClassFields() == FieldOptions.ALL) {
				sb.append(field.getAccess()).append(field.getName()).append(": ").append(field.getType()).append("\n");
			}
		}
	}

	private void createTopSection(JavaClass parsedClass, StringBuilder sb) {
		ClassRole role = parsedClass.getRole();
		if (role == ClassRole.INTERFACE) {
			sb.append("<<").append(role).append(">>\n").append(AlphabetLayout.getClassName(parsedClass));
		}
		else if (role == ClassRole.ABSTRACT) {
			sb.append("/").append(AlphabetLayout.getClassName(parsedClass)).append("/");
		}
		else {
			sb.append(AlphabetLayout.getClassName(parsedClass));
		}
		sb.append("\n");
	}

	private JavaClass parseFile(String filename) {
		try {
			if (getExtension(filename).equals("java")) {
				return parseJavaFile(filename);
			}
			else if (getExtension(filename).equals("class")) {
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
