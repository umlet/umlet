package com.umlet.custom;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Action;
import javax.swing.ActionMap;

import com.baselet.control.Constants;
import com.baselet.gui.JMultiLineToolTip;
import com.umlet.gui.CustomCodeSyntaxPane;


public class HelpHandler implements KeyListener {
	private CustomCodeSyntaxPane codepane;
	private ArrayList<FunctionDesc> functions;
	private ArrayList<VariableDesc> variables;
	private ArrayList<String> defaultdescriptors; // descriptors that are displayed in empty lines
	private CustomElementHandler handler;

	private class FunctionDesc {
		private String defaults;
		private String description;
		private Pattern pattern;

		private FunctionDesc(Method m) {
			CustomFunction cm = m.getAnnotation(CustomFunction.class);
			String patternstring = "\\s*";
			String patternclosestring = "";
			String mname = m.getName();
			for (int i = 0; i < mname.length(); i++) {
				if (i != 0) {
					patternclosestring += ")?";
					patternstring += "(";
				}
				patternstring += mname.charAt(i);
			}
			patternstring += "\\s*(\\(";
			patternclosestring += ")?";
			defaults = mname + "(";
			description = mname + "(";
			String[] params = cm.param_defaults().split(",");
			Class<?>[] types = m.getParameterTypes();
			for (int i = 0; (i < params.length) && (i < types.length); i++) {
				if (i != 0) {
					defaults += ", ";
					description += ", ";
					patternstring += "(,";
					patternclosestring += ")?";
				}
				patternstring += ".*";
				defaults += params[i];
				description += types[i].getSimpleName() + " " + params[i];
			}
			defaults += ")";
			description += ")";
			patternstring += "\\)?" + patternclosestring + "\\s*";

			pattern = Pattern.compile(patternstring);
		}
	}

	private class VariableDesc {
		private String description;
		private Pattern pattern;

		private VariableDesc(Field f) {
			String patternstring = "\\s*";
			String patternclosestring = "";
			String mname = f.getName();
			for (int i = 0; i < mname.length(); i++) {
				if (i != 0) {
					patternclosestring += ")?";
					patternstring += "(";
				}
				patternstring += mname.charAt(i);
			}
			patternstring += patternclosestring + "\\s*";
			description = mname;
			pattern = Pattern.compile(patternstring);
		}
	}

	public HelpHandler(CustomCodeSyntaxPane codepane, CustomElementHandler handler) {
		this.codepane = codepane;
		this.functions = new ArrayList<FunctionDesc>();
		this.variables = new ArrayList<VariableDesc>();
		this.defaultdescriptors = new ArrayList<String>();
		for (Method m : (CustomElement.class).getDeclaredMethods()) {
			if (m.isAnnotationPresent(CustomFunction.class)) {
				this.functions.add(new FunctionDesc(m));
			}
		}

		for (Field f : (CustomElement.class).getDeclaredFields()) {
			if (f.isAnnotationPresent(CustomVariable.class)) this.variables.add(new VariableDesc(f));
		}

		// Fill defaultdescriptors with all beginnings of custom functions ("draw...","print...",etc)
		for (FunctionDesc func : functions) {
			String firstPartOfFuncName = func.description.split("[A-Z(]")[0] + "...";
			if (!defaultdescriptors.contains(firstPartOfFuncName)) defaultdescriptors.add(firstPartOfFuncName);
		}

		this.handler = handler;
	}

	private void displaytooltip(boolean display) {
		JMultiLineToolTip tip = (JMultiLineToolTip) this.codepane.getToolTip();
		ActionMap m = this.codepane.getActionMap();
		Action post = m.get("postTip");
		Action hide = m.get("hideTip");
		boolean tipvisible = false;
		if (tip != null) {
			tipvisible = (tip.getParent() != null);
		}
		if (display && (post != null)) {
			if (!tipvisible) {
				post.actionPerformed(new ActionEvent(this.codepane, ActionEvent.ACTION_PERFORMED, ""));
				tip = (JMultiLineToolTip) this.codepane.getToolTip();
			}
			else {
				hide.actionPerformed(new ActionEvent(this.codepane, ActionEvent.ACTION_PERFORMED, ""));
				post.actionPerformed(new ActionEvent(this.codepane, ActionEvent.ACTION_PERFORMED, ""));
				tip = (JMultiLineToolTip) this.codepane.getToolTip();
			}

			if (tip.getParent() != null) {
				Point p = this.codepane.getLocationOnScreen();
				int pos = this.codepane.getCaretPosition();
				String[] lines = this.codepane.getText().split(Constants.NEWLINE, -1);
				int linenum = 0;
				for (int cur_pos = 0; (linenum < lines.length) && (cur_pos <= pos); cur_pos += lines[linenum].length() + 1, linenum++);

				p.y += linenum * this.codepane.getFontMetrics(this.codepane.getFont()).getHeight() + 3;
				p.x += 15;
				tip.getParent().getParent().getParent().getParent().setLocation(p);
			}
		}
		else if ((hide != null) && tipvisible) hide.actionPerformed(new ActionEvent(this.codepane, ActionEvent.ACTION_PERFORMED, ""));
	}

	private ArrayList<String> parseFunctionVariableDesc() {
		String[] lines = this.codepane.getText().split(Constants.NEWLINE, -1);
		int linenum = 0;
		int pos = this.codepane.getCaretPosition();
		int cur_pos = 0;
		while ((linenum < lines.length) && (cur_pos <= pos)) {
			cur_pos += lines[linenum].length() + 1;
			linenum++;
		}
		linenum--;
		cur_pos -= lines[linenum].length() + 1;

		ArrayList<String> descriptors = new ArrayList<String>();
		if (lines[linenum].equals("")) descriptors.addAll(this.defaultdescriptors);
		else {
			String[] segments = lines[linenum].split("[=;+-,]|\\*|\\/|\\(");
			for (String line : segments) {
				if ((cur_pos <= pos) && (cur_pos + line.length() >= pos)) {
					for (FunctionDesc fd : this.functions) {
						Matcher m = fd.pattern.matcher(line);
						if (m.matches()) descriptors.add(fd.description);
					}
					for (VariableDesc vd : this.variables) {
						Matcher m = vd.pattern.matcher(line);
						if (m.matches()) descriptors.add(vd.description);
					}
					break;
				}
				cur_pos += line.length() + 1;
			}
		}
		return descriptors;
	}

	@Override
	public void keyPressed(KeyEvent ke) {

	}

	@Override
	public void keyReleased(KeyEvent ke) {
		this.handler.keypressed = true;
		ArrayList<String> descriptors = this.parseFunctionVariableDesc();
		if (descriptors.isEmpty()) this.displaytooltip(false);
		else {
			String text = "";
			for (int i = 0; i < descriptors.size(); i++) {
				if (i != 0) text += Constants.NEWLINE;
				text += descriptors.get(i);
			}
			this.codepane.setToolTipText(text);
			this.displaytooltip(true);
		}
	}

	@Override
	public void keyTyped(KeyEvent ke) {

	}
}
