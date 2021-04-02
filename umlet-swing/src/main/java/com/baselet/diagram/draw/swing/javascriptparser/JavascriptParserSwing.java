package com.baselet.diagram.draw.swing.javascriptparser;

import java.lang.reflect.Method;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.JavascriptCodeParser;

public class JavascriptParserSwing extends JavascriptCodeParser {

	private final static String DRAWMETHODS_PREFIX = "draw";

	private final DrawerScriptable drawerScriptable;

	public JavascriptParserSwing(DrawHandler drawer) {
		this.drawer = drawer;
		drawerScriptable = new DrawerScriptable(drawer);
	}

	@Override
	public void parse(String line, int width, int height) {

		Context cx = Context.enter();

		Scriptable scope = cx.initStandardObjects();

		// Create an instance of the class whose instance methods is to be made available in javascript as a global function.
		Scriptable scriptable = drawerScriptable;
		scriptable.setParentScope(scope);

		for (Method method : DrawerScriptable.class.getMethods()) {
			if (method.getName().startsWith(DRAWMETHODS_PREFIX)) {
				// Create the FunctionObject that binds the above function name to the instance method.
				FunctionObject scriptableInstanceMethodBoundJavascriptFunction = new CustomFunctionObject(method.getName(), method, scriptable);
				// Make it accessible within the scriptExecutionScope.
				scope.put(method.getName(), scope, scriptableInstanceMethodBoundJavascriptFunction);
			}
		}

		scope.put("width", scope, width);
		scope.put("height", scope, height);
		scope.put("center", scope, "center");
		scope.put("left", scope, "left");
		scope.put("right", scope, "right");

		cx.evaluateString(scope, line, "JS", 1, null);
	}
}
