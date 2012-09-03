package com.umlet.element.experimental.settings.facets;

import com.baselet.control.Constants;
import com.baselet.control.Constants.AlignHorizontal;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.gui.AutocompletionText;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import com.umlet.element.experimental.PropertiesConfig;

public class TemplateClass implements Facet {

	private static final String KEY = "template=";
	
	private static final int SPACE = 6;
	
	@Override
	public boolean checkStart(String line) {
		return line.startsWith(KEY);
	}

	@Override
	public void handleLine(String line, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		String templateValue = line.substring(KEY.length());
		float height = drawer.textHeight() + SPACE;
		float width = drawer.textWidth(templateValue) + SPACE;
		drawer.setBackgroundAlpha(Constants.ALPHA_FULL_TRANSPARENCY);
		drawer.drawRectangle(propConfig.getGridElementSize().width - width, 0, width, height);
		drawer.print(templateValue, propConfig.getGridElementSize().width - width/2, height-SPACE/2, AlignHorizontal.CENTER);
	}

	@Override
	public boolean replacesText(String line) {
		return true;
	}

	@Override
	public AutocompletionText[] getAutocompletionStrings() {
		return new AutocompletionText[] {new AutocompletionText(KEY, "prints a template rectangle on the top right corner")};
	}

}
