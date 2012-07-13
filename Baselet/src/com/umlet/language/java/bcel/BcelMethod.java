package com.umlet.language.java.bcel;

import com.sun.org.apache.bcel.internal.classfile.Method;
import com.sun.org.apache.bcel.internal.generic.Type;

public class BcelMethod extends BcelAccessible implements com.umlet.language.java.Method {
	
	private Method method;
	private String className;
	private boolean isConstructor;

	public BcelMethod(Method method, String className) {
		super(method);
		this.method = method;
		this.className = className;
		if (method.getName().equals("<init>") || method.getName().equals("<clinit>")) { 
			isConstructor = true;
		} else isConstructor = false;
	}

	@Override
	public String getName() {
		if (isConstructor) {
			return className;
		}
		return method.getName();
	}

	@Override
	public String getReturnType() {
		if (isConstructor) {
			return "ctor";
		}
		return method.getReturnType().toString();
	}

	@Override
	public String getSignature() {
		String result = "";
		Type[] arguments = method.getArgumentTypes();
		boolean first = true;
		for (Type argument: arguments) {
			if (first) {
				first = false;
				result += argument;
			} else {
				result += ", "+argument;
			}
		}
		return result;
	}
}
