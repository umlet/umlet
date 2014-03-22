package com.baselet.elementnew.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.SharedUtils;
import com.baselet.diagram.draw.DrawHandler;
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
			int left = SharedUtils.realignToGrid(false, hCenter-armLength(getDrawer()), false);
			int right = SharedUtils.realignToGrid(false, hCenter+armLength(getDrawer()), true);
			int head = (int) headToLegLength(getDrawer());
			
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
	protected void drawCommonContent(DrawHandler drawer, PropertiesConfig propCfg) {
		// IMPORTANT: drawer must be used as parameter, because sometimes (eg: for autoresize calculation), the commonContent will be drawn by other drawers
		propCfg.addToYPos(headToLegLength(drawer));
		propCfg.updateCalculatedElementWidth(armLength(drawer)*2);
		
		int hCenter = getRealSize().width/2;
		drawer.drawCircle(hCenter, headRadius(drawer), headRadius(drawer)); // Head
		drawer.drawLine(hCenter-armLength(drawer), armHeight(drawer), hCenter+armLength(drawer), armHeight(drawer)); // Arms
		drawer.drawLine(hCenter, headRadius(drawer)*2, hCenter, headToBodyLength(drawer)); // Body
		drawer.drawLine(hCenter, headToBodyLength(drawer), hCenter-legSpan(drawer), headToLegLength(drawer)); // Legs
		drawer.drawLine(hCenter, headToBodyLength(drawer), hCenter+legSpan(drawer), headToLegLength(drawer)); // Legs

		propCfg.setStickingPolygonGenerator(actorStickingPolygonGenerator);
	}
	
	private double headToLegLength(DrawHandler drawer) {
		return legSpan(drawer)*2+headToBodyLength(drawer);
	}

	private double legSpan(DrawHandler drawer) {
		return drawer.getCurrentStyle().getFontSize();
	}

	private double headToBodyLength(DrawHandler drawer) {
		return drawer.getCurrentStyle().getFontSize()*2+headRadius(drawer)*2;
	}

	private double armHeight(DrawHandler drawer) {
		return armLength(drawer);
	}

	private double armLength(DrawHandler drawer) {
		return drawer.getCurrentStyle().getFontSize()*1.5;
	}

	private double headRadius(DrawHandler drawer) {
		return drawer.getCurrentStyle().getFontSize()/2;
	}
}

