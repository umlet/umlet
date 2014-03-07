package com.baselet.elementnew.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.SharedUtils;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.element.sticking.StickingPolygonGenerator;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesConfig;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.common.SeparatorLineFacet;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsAutoresize;

public class Actor extends NewGridElement {

	private StickingPolygonGenerator actorStickingPolygonGenerator = new StickingPolygonGenerator() {
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
	};
	@Override
	protected Settings createSettings() {
		return new SettingsAutoresize() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(SeparatorLineFacet.INSTANCE);
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

		propCfg.setStickingPolygonGenerator(actorStickingPolygonGenerator);
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

