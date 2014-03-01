package com.umlet.element.experimental.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.SharedUtils;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.sticking.StickingPolygon;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.base.SeparatorLine;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsAutoresize;

public class Actor extends NewGridElement {

	@Override
	protected Settings createSettings() {
		return new SettingsAutoresize() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(SeparatorLine.INSTANCE);
			}
		};
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLActor;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		propCfg.addToYPos(headToLegLength());
		propCfg.updateCalculatedElementWidth(armLength()*2);
		
		int hCenter = getRealSize().width/2;
		drawer.drawCircle(hCenter, headRadius(), headRadius()); // Head
		drawer.drawLine(hCenter-armLength(), armHeight(), hCenter+armLength(), armHeight()); // Arms
		drawer.drawLine(hCenter, headRadius()*2, hCenter, headToBodyLength()); // Body
		drawer.drawLine(hCenter, headToBodyLength(), hCenter-legSpan(), headToLegLength()); // Legs
		drawer.drawLine(hCenter, headToBodyLength(), hCenter+legSpan(), headToLegLength()); // Legs
	}

	@Override
	public StickingPolygon generateStickingBorder(Rectangle rect) {
		double hCenter = getRealSize().width/2;
		int left = SharedUtils.realignToGrid(false, hCenter-armLength(), false);
		int right = SharedUtils.realignToGrid(false, hCenter+armLength(), true)-1;
		int head = (int) headToLegLength();
		
		StickingPolygon p = new StickingPolygon(rect.x, rect.y);
		p.addPoint(left, 0);
		p.addPoint(right, 0);
		p.addPoint(right, head);
		p.addPoint(left, head, true);
		return p;
	}

	private double headToLegLength() {
		return legSpan()*2+headToBodyLength();
	}

	private double legSpan() {
		return getDrawer().getCurrentStyle().getFontSize();
	}

	private double headToBodyLength() {
		return getDrawer().getCurrentStyle().getFontSize()*2+headRadius()*2;
	}

	private double armHeight() {
		return armLength();
	}

	private double armLength() {
		return getDrawer().getCurrentStyle().getFontSize()*1.5;
	}

	private double headRadius() {
		return getDrawer().getCurrentStyle().getFontSize()/2;
	}
}

