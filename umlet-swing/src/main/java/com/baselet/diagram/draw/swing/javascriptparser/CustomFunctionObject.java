package com.baselet.diagram.draw.swing.javascriptparser;

import java.lang.reflect.Member;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;

@SuppressWarnings("serial")
public class CustomFunctionObject extends FunctionObject {

	CustomFunctionObject(String name, Member methodOrConstructor, Scriptable parentScope) {
		super(name, methodOrConstructor, parentScope);
	}

	/* When a function is called, Rhino invokes the FunctionObject.call() method that is passed a reference to this. In case the function is a global function, it is called without a reference to this (i.e. xxx() instead of this.xxx()), the value of the this variable that gets passed to the FunctionObject.call() method is the scope in which the call was made (i.e. in this case the value of the this parameter will be same as the value of the scope parameter). This becomes a problem in case the java method being invoked is an instance method because as per the JavaDocs of constructor of FunctionObject class: If the method is not static, the Java this value will correspond to the JavaScript this value. Any attempt to call the function with a this value that is not of the right Java type will result in an error. And in the scenario described above that is exactly the case. The javascript this value does NOT correspond to the java this value and results in an incompatible object error. The solution is to subclass FunctionObject, override the call() method, forcefully 'fix' the this reference, and then let the call proceed normally. Source: https://stackoverflow.com/questions/3441947/how-do-i-call-a-method-of-a-java-instance-from-javascript/16479685#16479685 */
	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		return super.call(cx, scope, getParentScope(), args);
	}
}
