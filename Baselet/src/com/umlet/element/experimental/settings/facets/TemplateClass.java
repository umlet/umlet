package com.umlet.element.experimental.settings.facets;

import com.baselet.control.Constants;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.BaseDrawHandlerSwing;
import com.baselet.diagram.draw.Style;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;

public class TemplateClass implements Facet {

	private static final String KEY = "template=";
	
	private static final int SPACE = 6;
	
	@Override
	public boolean checkStart(String line) {
		return line.startsWith(KEY);
	}

	@Override
	public void handleLine(String line, BaseDrawHandlerSwing drawer, PropertiesConfig propConfig) {
		String templateValue = line.substring(KEY.length());
		float height = drawer.textHeight() + SPACE;
		float width = drawer.textWidth(templateValue) + SPACE;
		Style style = drawer.getCurrentStyle();
		drawer.setBackgroundAlpha(Constants.ALPHA_FULL_TRANSPARENCY);
		drawer.setLineType(LineType.DASHED);
		drawer.drawRectangle(propConfig.getGridElementSize().width - width, 0, width, height);
		drawer.print(templateValue, propConfig.getGridElementSize().width - width/2, height-SPACE/2, AlignHorizontal.CENTER);
		drawer.setCurrentStyle(style);
	}

	@Override
	public boolean replacesText(String line) {
		return true;
	}

	@Override
	public AutocompletionText[] getAutocompletionStrings() {
		return new AutocompletionText[] {new AutocompletionText(KEY + "txt", "print template rectangle on top right corner")};
	}

}
