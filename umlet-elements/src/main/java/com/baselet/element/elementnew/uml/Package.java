package com.baselet.element.elementnew.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.ElementId;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.DrawHandler.Layer;
import com.baselet.element.NewGridElement;
import com.baselet.element.draw.DrawHelper;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;
import com.baselet.element.facet.common.SeparatorLineFacet;
import com.baselet.element.facet.common.TextBeforeFirstSeparatorCollectorFacet;
import com.baselet.element.facet.common.TextBeforeFirstSeparatorCollectorFacet.TextBeforeFirstSeparatorCollectorFacetResponse;
import com.baselet.element.settings.SettingsManualresizeCenter;
import com.baselet.element.sticking.polygon.PointDoubleStickingPolygonGenerator;

public class Package extends NewGridElement {

	@Override
	protected Settings createSettings() {
		return new SettingsManualresizeCenter() {
			@Override
			protected List<Facet> createFacets() {
				return listOf(super.createFacets(), TextBeforeFirstSeparatorCollectorFacet.INSTANCE, SeparatorLineFacet.INSTANCE);
			}
		};
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLPackage;
	}

	@Override
	protected void drawCommonContent(PropertiesParserState state) {
		DrawHandler drawer = state.getDrawer();
		List<String> packageTitle = getTitleLines(state);
		double packageHeight = 0;
		double packageWidth = getRealSize().getWidth() / 2.5;
		double txtHeight = drawer.textHeightMaxWithSpace();
		for (String line : packageTitle) {
			packageHeight += txtHeight;
			packageWidth = Math.max(packageWidth, drawer.textWidth(line) + drawer.getDistanceBorderToText() * 2);
			drawer.setLayer(Layer.Foreground); // text should be in front of the package-border
			drawer.print(line, new PointDouble(drawer.getDistanceBorderToText(), packageHeight), AlignHorizontal.LEFT);
			drawer.setLayer(Layer.Background);
		}
		packageHeight += drawer.getDistanceBorderToText();
		int height = getRealSize().getHeight();
		int width = getRealSize().getWidth();
		List<PointDouble> points = DrawHelper.drawPackage(drawer, 0, 0, packageHeight, packageWidth, height, width);
		state.getBuffer().setTopMin(packageHeight);
		state.setStickingPolygonGenerator(new PointDoubleStickingPolygonGenerator(points));
	}

	private static List<String> getTitleLines(PropertiesParserState state) {
		List<String> packageTitle;
		TextBeforeFirstSeparatorCollectorFacetResponse packageTitleResponse = state.getFacetResponse(TextBeforeFirstSeparatorCollectorFacet.class, null);
		if (packageTitleResponse != null) {
			packageTitle = packageTitleResponse.getLines();
		}
		else {
			packageTitle = Arrays.asList("");
		}
		return packageTitle;
	}

}
