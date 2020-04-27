package com.baselet.generator.java;

import java.util.Locale;

public interface JavaClass {

	public String getName();

	public Field[] getFields();

	public Method[] getMethods();

	public ClassRole getRole();

	public enum ClassRole {
		ABSTRACT, CLASS, INTERFACE;

		@Override
		public String toString() {
			return super.toString().toLowerCase(Locale.ENGLISH);
		}
	}

	public String getPackage();
}
