package com.umlet.element.experimental.uml;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.element.StickingPolygon;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.Properties;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.facets.DefaultGlobalFacet.ElementStyleEnum;
import com.umlet.element.experimental.settings.facets.Facet;
import com.umlet.element.experimental.settings.facets.SeparatorLine;

public class Actor extends NewGridElement {
	
	private static class SettingsActor extends Settings {

		@Override
		public XValues getXValues(double y, int height, int width) {
			return new XValues(0, width);
		}

		@Override
		public AlignVertical getVAlign() {
			return AlignVertical.TOP;
		}

		@Override
		public AlignHorizontal getHAlign() {
			return AlignHorizontal.CENTER;
		}

		@Override
		public ElementStyleEnum getElementStyle() {
			return ElementStyleEnum.RESIZE;
		}

		@Override
		public Facet[] createFacets() {
			return new Facet[]{new SeparatorLine()};
		}
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLActor;
	}
	
	@Override
	protected void updateConcreteModel(BaseDrawHandler drawer, Properties properties) {
		double fontSize = drawer.getCurrentStyle().getFontSize();
		int hCenter = getRealSize().width/2;
		double headRadius = fontSize/2;
		double armLength = fontSize*1.5;
		double armHeight = fontSize*1.5;
		double headAndBodyLength = fontSize*2+headRadius*2;
		double headBodyLegLength = fontSize*2+headAndBodyLength;
		double legSpan = fontSize;
		drawer.drawCircle(hCenter, headRadius, headRadius); // Head
		drawer.drawLine(hCenter-armLength, armHeight, hCenter+armLength, armHeight); // Arms
		drawer.drawLine(hCenter, headRadius*2, hCenter, headAndBodyLength); // Body
		drawer.drawLine(hCenter, headAndBodyLength, hCenter-legSpan, headBodyLegLength); // Legs
		drawer.drawLine(hCenter, headAndBodyLength, hCenter+legSpan, headBodyLegLength); // Legs
		properties.addToYPos(fontSize*5);
		properties.drawPropertiesText();
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		double fontSize = getDrawer().getCurrentStyle().getFontSize();
		double armLength = fontSize*1.5;
		double headBodyLegLength = fontSize*5;
		int hCenter = getRealSize().width/2;
		
		StickingPolygon p = new StickingPolygon(x, y);

		p.addPoint((int)(hCenter-armLength), 0);
		p.addPoint((int)(hCenter+armLength), 0);
		p.addPoint((int)(hCenter+armLength), (int)(headBodyLegLength));
		p.addPoint((int)(hCenter-armLength), (int)(headBodyLegLength), true);
		return p;
	}

	@Override
	public Settings getSettings() {
		return new SettingsActor();
	}
}

