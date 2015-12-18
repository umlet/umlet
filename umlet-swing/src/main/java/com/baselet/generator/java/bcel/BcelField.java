package com.baselet.generator.java.bcel;

import org.apache.bcel.classfile.Field;

public class BcelField extends BcelAccessible implements com.baselet.generator.java.Field {

	private Field field;

	public BcelField(Field field) {
		super(field);
		this.field = field;
	}

	@Override
	public String getName() {
		return field.getName();
	}

	@Override
	public String getType() {
		return field.getType().toString();
	}
}
