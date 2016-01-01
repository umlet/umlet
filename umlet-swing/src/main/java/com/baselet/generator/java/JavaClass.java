package com.baselet.generator.java;

public interface JavaClass {

	public String getName();

	public Field[] getFields();

	public Method[] getMethods();

	public ClassRole getRole();

	public enum ClassRole {
		ABSTRACT, CLASS, INTERFACE;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	public String getPackage();
}
