package com.baselet.diagram.draw.swing;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.JavascriptCodeParser;

public class JavascriptParserSwing extends JavascriptCodeParser {

	public JavascriptParserSwing(DrawHandler drawer) {
		this.drawer = drawer;
	}

	@Override
	public void parse(String line) {

		Context cx = Context.enter();

		Scriptable scope = cx.initStandardObjects();

		ScriptableObject.putProperty(scope, "drawer", drawer);

		cx.evaluateString(scope, line, "custom drawings js script", 1, null);

	}
}
