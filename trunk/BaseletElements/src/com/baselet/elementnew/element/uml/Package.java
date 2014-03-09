package com.baselet.elementnew.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.element.sticking.PointDoubleStickingPolygonGenerator;
import com.baselet.elementnew.ElementId;
import com.baselet.elementnew.NewGridElement;
import com.baselet.elementnew.PropertiesConfig;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.common.SeparatorLineFacet;
import com.baselet.elementnew.facet.specific.PackageName;
import com.baselet.elementnew.settings.Settings;
import com.baselet.elementnew.settings.SettingsManualresizeCenter;

public class Package extends NewGridElement {

	@Override
	protected Settings createSettings() {
		return new SettingsManualresizeCenter() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(PackageName.INSTANCE, SeparatorLineFacet.INSTANCE);
			}
		};
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLPackage;
	}

	@Override
	protected void drawCommonContent(BaseDrawHandler drawer, PropertiesConfig propCfg) {
		String packageName = propCfg.getFacetResponse(PackageName.class, null);
		double packageHeight = 20;
		double packageWidth = getRealSize().getWidth()/2.5;
		double txtHeight = drawer.textHeightWithSpace();
		if (packageName != null) {
			packageHeight = txtHeight + 5;
			packageWidth = Math.max(packageWidth, drawer.textWidth(packageName) + drawer.getDistanceHorizontalBorderToText()*2);
		}
		int height = getRealSize().getHeight();
		int width = getRealSize().getWidth();
		List<PointDouble> points = Arrays.asList(
				new PointDouble(0, packageHeight),
				new PointDouble(width, packageHeight),
				new PointDouble(width, height),
				new PointDouble(0, height),
				new PointDouble(0, height),
				new PointDouble(0, 0),
				new PointDouble(packageWidth, 0),
				new PointDouble(packageWidth, packageHeight),
				new PointDouble(0, packageHeight)
				);
		drawer.drawLines(points);
		propCfg.setMinTopBuffer(packageHeight);
		if (packageName != null) {
			drawer.print(packageName, new PointDouble(drawer.getDistanceHorizontalBorderToText(), txtHeight), AlignHorizontal.LEFT); 
		}
		propCfg.setStickingPolygonGenerator(new PointDoubleStickingPolygonGenerator(points));
	}

}

