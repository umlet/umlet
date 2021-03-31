package com.baselet.diagram.draw.swing.javascriptparser;

import java.lang.reflect.Method;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.JavascriptCodeParser;

public class JavascriptParserSwing extends JavascriptCodeParser {

	private final DrawerScriptable drawerScriptable;

	public JavascriptParserSwing(DrawHandler drawer) {
		this.drawer = drawer;
		drawerScriptable = new DrawerScriptable(drawer);
	}

	@Override
	public void parse(String line) {

		Context cx = Context.enter();

		Scriptable scope = cx.initStandardObjects();
		
		// Create an instance of the class whose instance methods is to be made available in javascript as a global function.
		Scriptable scriptable = drawerScriptable;
		scriptable.setParentScope(scope);

		for (Method method : DrawerScriptable.class.getMethods()) {
			if (method.getName().startsWith("draw")) {
				// Create the FunctionObject that binds the above function name to the instance method.
				FunctionObject scriptableInstanceMethodBoundJavascriptFunction = new CustomFunctionObject(method.getName(), method, scriptable);
				// Make it accessible within the scriptExecutionScope.
				scope.put(method.getName(), scope, scriptableInstanceMethodBoundJavascriptFunction);
			}
		}

		cx.evaluateString(scope, line, "custom drawings js script", 1, null);
	}
}
