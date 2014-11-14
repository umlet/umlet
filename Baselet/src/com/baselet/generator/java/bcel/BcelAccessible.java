package com.baselet.generator.java.bcel;

import org.apache.bcel.classfile.FieldOrMethod;

import com.baselet.generator.java.Accessible;

public abstract class BcelAccessible implements Accessible {

	private AccessFlag flag;

	public BcelAccessible(FieldOrMethod accessible) {
		if (accessible.isPrivate()) {
			flag = AccessFlag.PRIVATE;
		}
		else if (accessible.isProtected()) {
			flag = AccessFlag.PROTECTED;
		}
		else if (accessible.isPublic()) {
			flag = AccessFlag.PUBLIC;
		}
		else {
			flag = AccessFlag.PACKAGE;
		}
	}

	@Override
	public AccessFlag getAccess() {
		return flag;
	}
}
