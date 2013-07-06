package com.umlet.element.experimental.settings.facets;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Style;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.PropertiesConfig;

public class TemplateClass extends GlobalStatelessFacet {

	private static final String KEY = "template" + SEP;
	
	private static final int SPACE = 6;
	
	@Override
	public boolean checkStart(String line) {
		return line.startsWith(KEY);
	}

	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		String templateValue = line.substring(KEY.length());
		double height = drawer.textHeight() + SPACE;
		double width = drawer.textWidth(templateValue) + SPACE;
		Style style = drawer.getCurrentStyle();
		drawer.setBackgroundColor(ColorOwn.TRANSPARENT);
		drawer.setLineType(LineType.DASHED);
		drawer.drawRectangle(propConfig.getGridElementSize().width - width, 0, width, height);
		drawer.print(templateValue, propConfig.getGridElementSize().width - width/2, height-SPACE/2, AlignHorizontal.CENTER);
		drawer.setStyle(style); // reset style to state before manipulations for drawing the template class
	}

	@Override
	public AutocompletionText[] getAutocompletionStrings() {
		return new AutocompletionText[] {new AutocompletionText(KEY + "txt", "print template rectangle on top right corner", isGlobal())};
	}

}
