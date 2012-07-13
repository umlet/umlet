package com.umlet.language.java.jp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.TypeDeclaration;

import com.baselet.control.Utils;
import com.umlet.language.java.Field;
import com.umlet.language.java.JavaClass;
import com.umlet.language.java.Method;

public class JpJavaClass implements JavaClass {

	private static Logger log = Logger.getLogger(Utils.getClassName());
	
	private CompilationUnit cu;
	private List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	private List<ConstructorDeclaration> constructors = new ArrayList<ConstructorDeclaration>();
	private ClassOrInterfaceDeclaration clazz;
	private List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();
	
	public JpJavaClass(String filename) {
		FileInputStream in = null;

		try {
            in = new FileInputStream(filename);
            this.cu = JavaParser.parse(in);
        } catch (Exception e) {
        	log.error("Javaparser library faild to parse .java file.", e);
        } finally {
            try {
				if (in != null) in.close();
			} catch (IOException ignored) {}
        }
		extractInformation();
	}

	private void extractInformation() {
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
		String packageWithExtra = cu.getPackage().toString().replace("package ", "");
		return packageWithExtra.substring(0, packageWithExtra.lastIndexOf(";"));
	}
}
