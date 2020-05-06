package com.baselet.element.facet.common;

import com.baselet.diagram.draw.helper.ColorOwnBase;
import com.baselet.diagram.draw.helper.ColorOwnLight;
import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class TransparencyFacet extends FirstRunKeyValueFacet {

	public static final TransparencyFacet INSTANCE = new TransparencyFacet();

	private TransparencyFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("transparency", false, "0", "background color transparency in percent");
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		try {
			int valInt = Integer.parseInt(value);
			if (valInt < 0 || valInt > 100) {
				throw new NumberFormatException();
			}

			double colorTransparencyValue = 255 - valInt * 2.55; // ColorOwn has 0 for full transparency and 255 for no transparency
			System.out.println("BG-COLOR");
			System.out.println(state.getDrawer().getBackgroundColor());
			ColorOwnBase bgColor = state.getDrawer().getBackgroundColor();
			state.getDrawer().setBackgroundColor(bgColor.transparency((int) colorTransparencyValue));
		} catch (NumberFormatException e) {
			throw new StyleException("The value must be between 0 and 100");
		}
	}
}