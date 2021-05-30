package com.baselet.diagram.draw.swing.javascriptparser;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.LineType;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.StyleException;

@SuppressWarnings("serial")
public class DrawerScriptable extends ScriptableObject {
	DrawHandler drawer;
	Scriptable scope;

	public DrawerScriptable(DrawHandler drawer) {
		this.drawer = drawer;
	}

	public void setScope(Scriptable scope) {
		this.scope = scope;
	}

	public void drawCircle(final double x, final double y, final double radius, final ScriptableObject drawerConfig) {
		DrawFunction drawFunction = new DrawFunction() {
			@Override
			public void call() {
				DrawerConfig config = getDrawerConfig(drawerConfig);
				drawer.drawCircle(x, y, radius, config.getBgColor(), config.getFgColor(), config.getLineType(), config.getLineWidth(), config.getTransparency());
			}
		};
		executeDraw(drawFunction);
	}

	public void drawRectangle(final double x, final double y, final double width, final double height, final ScriptableObject drawerConfig) {
		DrawFunction drawFunction = new DrawFunction() {
			@Override
			public void call() {
				DrawerConfig config = getDrawerConfig(drawerConfig);
				drawer.drawRectangle(x, y, width, height, config.getBgColor(), config.getFgColor(), config.getLineType(), config.getLineWidth(), config.getTransparency());
			}
		};
		executeDraw(drawFunction);
	}

	public void drawLine(final double x1, final double y1, final double x2, final double y2, final ScriptableObject drawerConfig) {
		DrawFunction drawFunction = new DrawFunction() {
			@Override
			public void call() {
				DrawerConfig config = getDrawerConfig(drawerConfig);
				drawer.drawLine(x1, y1, x2, y2, config.getFgColor(), config.getLineType(), config.getLineWidth());
			}
		};
		executeDraw(drawFunction);

	}

	public void drawArc(final double x, final double y, final double width, final double height, final double start, final double extent, final boolean open, final ScriptableObject drawerConfig) {
		DrawFunction drawFunction = new DrawFunction() {
			@Override
			public void call() {
				DrawerConfig config = getDrawerConfig(drawerConfig);
				drawer.drawArc(x, y, width, height, start, extent, open, config.getBgColor(), config.getFgColor(), config.getLineType(), config.getLineWidth(), config.getTransparency());
			}
		};
		executeDraw(drawFunction);
	}

	public void drawEllipse(final double x, final double y, final double width, final double height, final ScriptableObject drawerConfig) {
		DrawFunction drawFunction = new DrawFunction() {
			@Override
			public void call() {
				DrawerConfig config = getDrawerConfig(drawerConfig);
				drawer.drawEllipse(x, y, width, height, config.getBgColor(), config.getFgColor(), config.getLineType(), config.getLineWidth(), config.getTransparency());
			}
		};
		executeDraw(drawFunction);
	}

	public void drawRectangleRound(final double x, final double y, final double width, final double height, final double radius, final ScriptableObject drawerConfig) {
		DrawFunction drawFunction = new DrawFunction() {
			@Override
			public void call() {
				DrawerConfig config = getDrawerConfig(drawerConfig);
				drawer.drawRectangleRound(x, y, width, height, radius, config.getBgColor(), config.getFgColor(), config.getLineType(), config.getLineWidth(), config.getTransparency());
			}
		};
		executeDraw(drawFunction);
	}

	public void drawText(final String multiLineWithMarkup, final double x, final double y, final String align, final ScriptableObject drawerConfig) {
		DrawFunction drawFunction = new DrawFunction() {
			@Override
			public void call() {
				DrawerConfig config = getDrawerConfig(drawerConfig);
				AlignHorizontal alignHorizontal = getAlignHorizontalEnumFromString(align);
				drawer.print(multiLineWithMarkup, x, y, alignHorizontal, config.getFgColor());
			}
		};
		executeDraw(drawFunction);
	}

	public void executeDraw(DrawFunction drawFunction) {
		double oldLineWidth = drawer.getLineWidth();
		drawer.setLineWidth(Double.valueOf(scope.get("lw", scope).toString()));
		LineType oldLineType = drawer.getLineType();
		drawer.setLineType(scope.get("lt", scope).toString());
		ColorOwn oldColorBg = drawer.getBackgroundColor();
		drawer.setBackgroundColorAndKeepTransparency(scope.get("bg", scope).toString());
		ColorOwn oldColorFg = drawer.getForegroundColor();
		drawer.setForegroundColor(scope.get("fg", scope).toString());
		handleTransparency();

		drawFunction.call();

		drawer.setForegroundColor(oldColorFg);
		drawer.setBackgroundColor(oldColorBg);
		drawer.setLineWidth(oldLineWidth);
		drawer.setLineType(oldLineType);

	}

	private AlignHorizontal getAlignHorizontalEnumFromString(String align) {
		for (AlignHorizontal alignHorizontal : AlignHorizontal.values()) {
			if (alignHorizontal.toString().equalsIgnoreCase(align)) {
				return alignHorizontal;
			}
		}
		throw new StyleException("Allowed values for AlignHorizontal: center, left, right");
	}

	private void handleTransparency() {
		if (scope.get("transparency", scope) != NOT_FOUND) {
			drawer.setTransparency(Double.valueOf(scope.get("transparency", scope).toString()));
		}
	}

	private DrawerConfig getDrawerConfig(ScriptableObject drawerConfig) {
		if (drawerConfig == null) {
			return new DrawerConfig();
		}
		else {
			return new DrawerConfig(
					ScriptableObject.hasProperty(drawerConfig, "bg") ? ScriptableObject.getProperty(drawerConfig, "bg").toString() : null,
					ScriptableObject.hasProperty(drawerConfig, "fg") ? ScriptableObject.getProperty(drawerConfig, "fg").toString() : null,
					ScriptableObject.hasProperty(drawerConfig, "lt") ? ScriptableObject.getProperty(drawerConfig, "lt").toString() : null,
					ScriptableObject.hasProperty(drawerConfig, "lw") ? Double.valueOf(ScriptableObject.getProperty(drawerConfig, "lw").toString()) : null,
					ScriptableObject.hasProperty(drawerConfig, "transparency") ? Double.valueOf(ScriptableObject.getProperty(drawerConfig, "transparency").toString()) : null);
		}
	}

	@Override
	public String getClassName() {
		return getClass().getName();
	}
}
