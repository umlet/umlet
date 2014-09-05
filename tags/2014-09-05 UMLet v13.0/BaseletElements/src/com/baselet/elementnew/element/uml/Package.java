package com.baselet.elementnew.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.element.sticking.polygon.PointDoubleStickingPolygonGenerator;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.common.SeparatorLineFacet;
import com.baselet.elementnew.facet.common.TextBeforeFirstSeparatorCollectorFacet;
import com.baselet.elementnew.facet.common.TextBeforeFirstSeparatorCollectorFacet.PackageTitleFacetResponse;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsManualresizeCenter;

public class Package extends NewGridElement {

	@Override
	protected Settings createSettings() {
		return new SettingsManualresizeCenter() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(TextBeforeFirstSeparatorCollectorFacet.INSTANCE, SeparatorLineFacet.INSTANCE);
			}
		};
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLPackage;
	}

	@Override
	protected void drawCommonContent(DrawHandler drawer, PropertiesParserState state) {
		List<String> packageTitle = getTitleLines(state);
		double packageHeight = 0;
		double packageWidth = getRealSize().getWidth() / 2.5;
		double txtHeight = drawer.textHeightMaxWithSpace();
		for (String line : packageTitle) {
			packageHeight += txtHeight;
			packageWidth = Math.max(packageWidth, drawer.textWidth(line) + drawer.getDistanceBorderToText() * 2);
			drawer.setDrawDelayed(true); // text should be in front of the package-border
			drawer.print(line, new PointDouble(drawer.getDistanceBorderToText(), packageHeight), AlignHorizontal.LEFT);
			drawer.setDrawDelayed(false);
		}
		packageHeight += drawer.getDistanceBorderToText();
		int height = getRealSize().getHeight();
		int width = getRealSize().getWidth();
		List<PointDouble> points = drawPackage(drawer, 0, 0, packageHeight, packageWidth, height, width);
		state.setMinTopBuffer(packageHeight);
		state.setStickingPolygonGenerator(new PointDoubleStickingPolygonGenerator(points));
	}

	public static List<PointDouble> drawPackage(DrawHandler drawer, double upperLeftX, double upperLeftY, double titleHeight, double titleWidth, double fullHeight, double fullWidth) {
		PointDouble start = new PointDouble(upperLeftX, upperLeftY);
		List<PointDouble> points = Arrays.asList(
				start,
				new PointDouble(upperLeftX + titleWidth, upperLeftY),
				new PointDouble(upperLeftX + titleWidth, upperLeftY + titleHeight),
				new PointDouble(upperLeftX + fullWidth, upperLeftY + titleHeight),
				new PointDouble(upperLeftX + fullWidth, upperLeftY + fullHeight),
				new PointDouble(upperLeftX, upperLeftY + fullHeight),
				start
				);
		drawer.drawLines(points);
		drawer.drawLines(new PointDouble(upperLeftX, upperLeftY + titleHeight), new PointDouble(upperLeftX + titleWidth, upperLeftY + titleHeight));
		return points;
	}

	private static List<String> getTitleLines(PropertiesParserState state) {
		List<String> packageTitle;
		PackageTitleFacetResponse packageTitleResponse = state.getFacetResponse(TextBeforeFirstSeparatorCollectorFacet.class, null);
		if (packageTitleResponse != null) {
			packageTitle = packageTitleResponse.getLines();
		}
		else {
			packageTitle = Arrays.asList("");
		}
		return packageTitle;
	}

}
