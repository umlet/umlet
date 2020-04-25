package com.baselet.generator.java.jp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.generator.java.Field;
import com.baselet.generator.java.JavaClass;
import com.baselet.generator.java.Method;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.TypeDeclaration;

public class JpJavaClass implements JavaClass {

	private final Logger log = LoggerFactory.getLogger(JpJavaClass.class);

	private CompilationUnit cu;
	private final List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	private final List<ConstructorDeclaration> constructors = new ArrayList<ConstructorDeclaration>();
	private ClassOrInterfaceDeclaration clazz;
	private final List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();

	public JpJavaClass(String filename) throws ClassParserException {
		FileInputStream in = null;

		try {
			in = new FileInputStream(filename);
			cu = JavaParser.parse(in);
		} catch (Throwable e) { // catch all exceptions and errors (otherwise stuff like a renamed jpg file would silently fail)
			throw new ClassParserException("Cannot parse " + filename + ": " + e.getMessage(), e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				log.error("Error while closing file " + filename, e);
			}
		}
		extractInformation(filename);
	}

	private void extractInformation(String filename) throws ClassParserException {
		List<TypeDeclaration> types = cu.getTypes();
		for (TypeDeclaration type : types) {
			if (type instanceof ClassOrInterfaceDeclaration) {
				clazz = (ClassOrInterfaceDeclaration) type;
			}
			List<BodyDeclaration> members = type.getMembers();
			for (BodyDeclaration member : members) {
				if (member instanceof FieldDeclaration) {
					fields.add((FieldDeclaration) member);
				}
				else if (member instanceof ConstructorDeclaration) {
					constructors.add((ConstructorDeclaration) member);
				}
				else if (member instanceof MethodDeclaration) {
					methods.add((MethodDeclaration) member);
				}
			}
		}
		if (clazz == null) {
			throw new ClassParserException("No toplevel type declaration found in " + filename + ".");
		}
	}

	@Override
	public String getName() {
		return clazz.getName().toString();
	}

	@Override
	public Field[] getFields() {
		Field[] newFields = new Field[fields.size()];
		int i = 0;
		for (FieldDeclaration field : fields) {
			newFields[i] = new JpField(field);
			i++;
		}
		return newFields;
	}

	@Override
	public Method[] getMethods() {
		Method[] newMethods = new Method[methods.size() + constructors.size()];
		int i = 0;
		for (ConstructorDeclaration constructor : constructors) {
			newMethods[i] = new JpConstructor(constructor);
			i++;
		}
		for (MethodDeclaration method : methods) {
			newMethods[i] = new JpMethod(method);
			i++;
		}
		return newMethods;
	}

	@Override
	public ClassRole getRole() {
		if (clazz.isInterface()) {
			return ClassRole.INTERFACE;
		}
		else if ((clazz.getModifiers() & ModifierSet.ABSTRACT) != 0) {
			return ClassRole.ABSTRACT;
		}
		else {
			return ClassRole.CLASS;
		}
	}

	@Override
	public String getPackage() {
		PackageDeclaration packageDecl = cu.getPackage();
		if (packageDecl == null) {
			return "";
		}
		String packageWithExtra = packageDecl.toString().replace("package ", "");
		return packageWithExtra.substring(0, packageWithExtra.lastIndexOf(";"));
	}
}
