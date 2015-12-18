package com.baselet.generator.java.jp;

import com.baselet.generator.java.Field;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.ModifierSet;

public class JpField implements Field {

	private final FieldDeclaration field;

	public JpField(FieldDeclaration field) {
		this.field = field;
	}

	@Override
	/**
	 * Code duplicated in JpMethod&JpConstructor because the extended class
	 * BodyDeclaration does not provide a getModifiers() method.
	 */
	public AccessFlag getAccess() {
		int modifiers = field.getModifiers();
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

	@Override
	public String getName() {
		String varWithBraces = field.getVariables().toString();
		return varWithBraces.substring(1, varWithBraces.length() - 1);
	}

	@Override
	public String getType() {
		return field.getType().toString();
	}
}
