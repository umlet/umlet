package com.baselet.generator.java.jp;

import java.util.List;

import com.baselet.generator.java.Method;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.Parameter;

public class JpMethod implements Method {

	private final MethodDeclaration method;

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
		StringBuilder sb = new StringBuilder("");
		boolean first = true;
		for (Parameter param : params) {
			if (first) {
				first = false;
				sb.append(param.toString());
			}
			else {
				sb.append(", ").append(param.toString());
			}
		}
		return sb.toString();
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
		}
		else if ((modifiers & ModifierSet.PROTECTED) != 0) {
			return AccessFlag.PROTECTED;
		}
		else if ((modifiers & ModifierSet.PRIVATE) != 0) {
			return AccessFlag.PRIVATE;
		}
		else {
			return AccessFlag.PACKAGE;
		}
	}
}
