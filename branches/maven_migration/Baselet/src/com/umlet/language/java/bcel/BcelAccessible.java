package com.umlet.language.java.bcel;

import com.sun.org.apache.bcel.internal.classfile.FieldOrMethod;
import com.umlet.language.java.Accessible;

public abstract class BcelAccessible implements Accessible {

	private AccessFlag flag;
	
	public BcelAccessible(FieldOrMethod accessible) {
		if (accessible.isPrivate()) {
			this.flag = AccessFlag.PRIVATE;
		} else if (accessible.isProtected()) {
			this.flag = AccessFlag.PROTECTED;
		} else if (accessible.isPublic()) {
			this.flag = AccessFlag.PUBLIC;
		} else {
			this.flag = AccessFlag.PACKAGE;
		}
	}
	
	@Override
	public AccessFlag getAccess() {
		return flag;
	}
}
