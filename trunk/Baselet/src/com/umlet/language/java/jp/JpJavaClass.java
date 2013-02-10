package com.umlet.language.java.jp;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.TypeDeclaration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.umlet.language.ClassParserException;
import com.umlet.language.java.Field;
import com.umlet.language.java.JavaClass;
import com.umlet.language.java.Method;

public class JpJavaClass implements JavaClass {

	private CompilationUnit cu;
	private List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	private List<ConstructorDeclaration> constructors = new ArrayList<ConstructorDeclaration>();
	private ClassOrInterfaceDeclaration clazz;
	private List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();
	
	public JpJavaClass(String filename) throws ClassParserException {
		FileInputStream in = null;

		try {
            in = new FileInputStream(filename);
            this.cu = JavaParser.parse(in);
        } catch (Exception e) {
			throw new ClassParserException("Javaparser library failed to parse "+filename, e);
        } finally {
            try {
				if (in != null) in.close();
			} catch (IOException ignored) {}
        }
		extractInformation(filename);
	}

	private void extractInformation(String filename) throws ClassParserException {
		List<TypeDeclaration> types = cu.getTypes();
		for (TypeDeclaration type: types) {
			if (type instanceof ClassOrInterfaceDeclaration) {
				clazz = (ClassOrInterfaceDeclaration) type;
			}
			List<BodyDeclaration> members = type.getMembers();
            for (BodyDeclaration member : members) {
                if (member instanceof FieldDeclaration) {
                	this.fields.add((FieldDeclaration) member);
                } else if (member instanceof ConstructorDeclaration) {
                	this.constructors.add((ConstructorDeclaration) member);
            	}else if (member instanceof MethodDeclaration) {
                	this.methods.add((MethodDeclaration) member);
                }
            }
		}
		if (clazz == null) {
			throw new ClassParserException("Could not successfully parse "+filename+".");
		}
	}

	@Override
	public String getName() {
		return clazz.getName().toString();
	}

	@Override
	public Field[] getFields() {
		Field[] newFields = new Field[this.fields.size()];
		int i = 0;
		for (FieldDeclaration field: fields) {
			newFields[i] = new JpField(field); 
			i++;
		}
		return newFields;
	}

	@Override
	public Method[] getMethods() {
		Method[] newMethods = new Method[this.methods.size()+this.constructors.size()];
		int i = 0;
		for (ConstructorDeclaration constructor: constructors) {
			newMethods[i] = new JpConstructor(constructor);
			i++;
		}
		for (MethodDeclaration method: methods) {
			newMethods[i] = new JpMethod(method); 
			i++;
		}
		return newMethods;
	}

	@Override
	public ClassRole getRole() {
		if (clazz.isInterface()) {
			return ClassRole.INTERFACE;
		} else if ((clazz.getModifiers() & ModifierSet.ABSTRACT) != 0) {
			return ClassRole.ABSTRACT;
		} else return ClassRole.CLASS;
	}

	@Override
	public String getPackage() {
		PackageDeclaration packageDecl = cu.getPackage();
		if (packageDecl == null) return "";
		String packageWithExtra = packageDecl.toString().replace("package ", "");
		return packageWithExtra.substring(0, packageWithExtra.lastIndexOf(";"));
	}
}
