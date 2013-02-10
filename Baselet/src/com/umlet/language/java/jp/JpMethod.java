package com.umlet.language.java.jp;

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;

import java.util.List;

import com.umlet.language.java.Method;

public class JpMethod implements Method {
	
	private MethodDeclaration method;

	public JpMethod(MethodDeclaration method) {
		this.method = method;
	}
	@Override
	public String getName() {
		return method.getName();
	}

	@Override
	public String getReturnType() {
		return method.getType().toString();
	}

	@Override
	public String getSignature() {
		List<Parameter> params = null;
		if ((params = method.getParameters()) == null) {
			return "";
		}
		String result = "";
		boolean first = true;
		for (Parameter param: params) {
			if (first)  {
				first = false;
				result += param.toString();
			} else  {
				result += ", "+param.toString();
			}
		}
		return result;
	}

	@Override
	/**
	 * Code duplicated in JpConstructor&JpField because the extended class 
	 * BodyDeclaration does not provide a getModifiers() method.
	 */
	public AccessFlag getAccess() {
		int modifiers = method.getModifiers();
		if ((modifiers & ModifierSet.PUBLIC) != 0) {
			return AccessFlag.PUBLIC;
		} else if ((modifiers & ModifierSet.PROTECTED) != 0) {
			return AccessFlag.PROTECTED; 
		} else if ((modifiers & ModifierSet.PRIVATE) != 0) {
			return AccessFlag.PRIVATE; 
		} else {
			return AccessFlag.PACKAGE;
		}
	}
}
