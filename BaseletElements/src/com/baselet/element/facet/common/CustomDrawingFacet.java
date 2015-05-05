package com.baselet.element.facet.common;

import static com.baselet.element.facet.common.CustomDrawingFacet.DrawMethod.ParameterType.ALIGNMENT;
import static com.baselet.element.facet.common.CustomDrawingFacet.DrawMethod.ParameterType.BOOL;
import static com.baselet.element.facet.common.CustomDrawingFacet.DrawMethod.ParameterType.DOUBLE;
import static com.baselet.element.facet.common.CustomDrawingFacet.DrawMethod.ParameterType.STRING;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.FormatLabels;
import com.baselet.control.enums.Priority;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.gui.AutocompletionText;

public class CustomDrawingFacet extends Facet {

	public static final CustomDrawingFacet INSTANCE = new CustomDrawingFacet();
	public static final Logger logger = Logger.getLogger(CustomDrawingFacet.class);
	public static final String CODE_SEP_START = "customelement=";
	public static final String CODE_START_INFO = "indicates the start of custom drawing commands, has no close command.";

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		Object objIsActive = state.getFacetResponse(CustomDrawingFacet.class, false);
		if (objIsActive instanceof Boolean && (Boolean) objIsActive) {
			return true;
		}
		else if (CODE_SEP_START.equals(line)) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void handleLine(String line, PropertiesParserState state) {
		if (CODE_SEP_START.equals(line)) {
			Object objIsActive = state.getFacetResponse(CustomDrawingFacet.class, false);
			if (objIsActive instanceof Boolean && (Boolean) objIsActive) {
				// custom commands are already turned on -> error
				throw new RuntimeException(FormatLabels.BOLD.getValue() + "Invalid value: " + FormatLabels.BOLD.getValue() + CODE_SEP_START + "\nDuplicate command. This command may only occur once.");
			}
			state.setFacetResponse(CustomDrawingFacet.class, true);
		}
		else {
			line = line.trim();
			boolean found = false;
			for (DrawMethod drawMethod : supportedDrawingMethods)
			{
				if (line.startsWith(drawMethod.name + '(') && line.endsWith(")"))
				{
					found = true;
					drawMethod.parseLine(state.getDrawer(), line);
					break;
				}
			}
			if (!found && !line.isEmpty() && !line.startsWith("//")) {
				throw new RuntimeException(FormatLabels.BOLD.getValue() + "Invalid value:" + FormatLabels.BOLD.getValue() + " '" + line + "'\nNo custom drawing command with this name was found.");
			}
		}
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		List<AutocompletionText> autocompletionList = new LinkedList<AutocompletionText>();
		autocompletionList.add(new AutocompletionText(CODE_SEP_START, CODE_START_INFO));
		for (DrawMethod dm : supportedDrawingMethods) {
			autocompletionList.add(new AutocompletionText(dm.name, ""));
		}
		return autocompletionList;
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGHEST;
	}

	DrawMethod[] supportedDrawingMethods = {
			new DrawMethod("drawRectangle", new DrawMethod.ParameterType[] { DOUBLE, DOUBLE, DOUBLE, DOUBLE }, true, true)
			{

				@Override
				protected void draw(DrawHandler drawHandler, Object[] parameters) {
					drawHandler.drawRectangle((Double) parameters[0], (Double) parameters[1], (Double) parameters[2], (Double) parameters[3]);
				}

			},
			new DrawMethod("drawLine", new DrawMethod.ParameterType[] { DOUBLE, DOUBLE, DOUBLE, DOUBLE }, false, true)
			{

				@Override
				protected void draw(DrawHandler drawHandler, Object[] parameters) {
					drawHandler.drawLine((Double) parameters[0], (Double) parameters[1], (Double) parameters[2], (Double) parameters[3]);
				}

			},
			new DrawMethod("drawArc", new DrawMethod.ParameterType[] { DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, BOOL }, true, true)
			{

				@Override
				protected void draw(DrawHandler drawHandler, Object[] parameters) {
					drawHandler.drawArc((Double) parameters[0], (Double) parameters[1], (Double) parameters[2], (Double) parameters[3], (Double) parameters[4], (Double) parameters[5], (Boolean) parameters[6]);
				}

			},
			new DrawMethod("drawCircle", new DrawMethod.ParameterType[] { DOUBLE, DOUBLE, DOUBLE }, true, true)
			{

				@Override
				protected void draw(DrawHandler drawHandler, Object[] parameters) {
					drawHandler.drawCircle((Double) parameters[0], (Double) parameters[1], (Double) parameters[2]);
				}

			},
			new DrawMethod("drawEllipse", new DrawMethod.ParameterType[] { DOUBLE, DOUBLE, DOUBLE, DOUBLE }, true, true)
			{

				@Override
				protected void draw(DrawHandler drawHandler, Object[] parameters) {
					drawHandler.drawEllipse((Double) parameters[0], (Double) parameters[1], (Double) parameters[2], (Double) parameters[3]);
				}

			},
			new DrawMethod("drawRectangleRound", new DrawMethod.ParameterType[] { DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE }, true, true)
			{

				@Override
				protected void draw(DrawHandler drawHandler, Object[] parameters) {
					drawHandler.drawRectangleRound((Double) parameters[0], (Double) parameters[1], (Double) parameters[2], (Double) parameters[3], (Double) parameters[4]);
				}

			},
			new DrawMethod("drawText", new DrawMethod.ParameterType[] { STRING, DOUBLE, DOUBLE, ALIGNMENT }, false, true) {

				@Override
				protected void draw(DrawHandler drawHandler, Object[] parameters) {
					drawHandler.print((String) parameters[0], (Double) parameters[1], (Double) parameters[2], (AlignHorizontal) parameters[3]);
				}

			}
	};

	/**
	 *
	 * Method(drawing parameters, foregroundColor, backgroundColor)
	 *
	 */
	abstract static class DrawMethod {

		enum ParameterType {
			DOUBLE, BOOL, STRING, ALIGNMENT
		}

		final String name;
		/**
		 * without background- or foregroundcolor
		 */
		final ParameterType[] parameterList;
		final boolean supportsBackgroundColor;
		final boolean supportsForegroundColor;

		public DrawMethod(String name, ParameterType[] parameterList, boolean supportsBackgroundColor, boolean supportsForegroundColor) {
			super();
			this.name = name;
			this.parameterList = parameterList;
			this.supportsBackgroundColor = supportsBackgroundColor;
			this.supportsForegroundColor = supportsForegroundColor;
		}

		void parseLine(DrawHandler drawHandler, String line)
		{
			if (line.startsWith(name))
			{
				// remove method name an parentheses
				String[] parameters = line.substring(name.length() + 1, line.length() - 1).split(",");
				Object[] parsedParameters = new Object[parameterList.length];
				if (parameters.length >= parameterList.length &&
					parameters.length <= parameterList.length + (supportsBackgroundColor ? 1 : 0) + (supportsForegroundColor ? 1 : 0))
				{
					int i;
					for (i = 0; i < parameterList.length; i++)
					{
						parsedParameters[i] = parse(parameterList[i], parameters[i].trim());
					}
					ColorOwn fg = null;
					ColorOwn bg = null;
					if (i < parameters.length && supportsForegroundColor)
					{
						// foregroundcolor
						fg = ColorOwn.forString(parameters[i].trim(), Transparency.FOREGROUND);
						if (fg == null)
						{
							// TODO exc
						}
						i++;
					}
					if (i < parameters.length)
					{
						// backgroundcolor
						bg = ColorOwn.forString(parameters[i].trim(), Transparency.BACKGROUND);
						if (bg == null)
						{
							// TODO exc
						}
						i++;
					}
					if (fg != null)
					{
						if (bg != null)
						{
							drawWithFgBg(drawHandler, fg, bg, parsedParameters);
						}
						else
						{
							drawWithFg(drawHandler, fg, parsedParameters);
						}
					}
					else
					{
						if (bg != null)
						{
							drawWithBg(drawHandler, bg, parsedParameters);
						}
						else
						{
							draw(drawHandler, parsedParameters);
						}
					}
				}
				else
				{
					// parameters count is mismatching
				}
			}
			else
			{
				// should never happen
			}
		}

		private Object parse(ParameterType type, String str)
		{
			// TODO proper value/exception handling
			switch (type) {
				case DOUBLE:
					return Double.parseDouble(str);
				case BOOL:
					return Boolean.parseBoolean(str);
				case STRING:
					return str;
				case ALIGNMENT:
					String upper = str.toUpperCase();
					return AlignHorizontal.valueOf(upper);
				default:
					// TODO throw exception
					return null;
			}
		}

		private void drawWithFg(DrawHandler drawHandler, ColorOwn fg, Object[] parameters)
		{
			ColorOwn oldFg = drawHandler.getForegroundColor();
			drawHandler.setForegroundColor(fg);
			draw(drawHandler, parameters);
			drawHandler.setForegroundColor(oldFg);
		}

		private void drawWithBg(DrawHandler drawHandler, ColorOwn bg, Object[] parameters)
		{
			ColorOwn oldBg = drawHandler.getBackgroundColor();
			drawHandler.setBackgroundColor(bg);
			draw(drawHandler, parameters);
			drawHandler.setBackgroundColor(oldBg);
		}

		private void drawWithFgBg(DrawHandler drawHandler, ColorOwn fg, ColorOwn bg, Object[] parameters)
		{
			ColorOwn oldBg = drawHandler.getBackgroundColor();
			drawHandler.setBackgroundColor(bg);
			drawWithFg(drawHandler, fg, parameters);
			drawHandler.setBackgroundColor(oldBg);
		}

		protected abstract void draw(DrawHandler drawHandler, Object[] parameters);
	}
}
