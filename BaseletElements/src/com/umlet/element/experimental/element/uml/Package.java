package com.umlet.element.experimental.element.uml;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.PropertiesConfig;
import com.umlet.element.experimental.facet.Facet;
import com.umlet.element.experimental.facet.specific.PackageName;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsManualresize;

public class Package extends NewGridElement {

	@Override
	protected Settings createSettings() {
		return new SettingsManualresize() {
			@Override
			public List<? extends Facet> createFacets() {
				return Arrays.asList(PackageName.INSTANCE);
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
		double packageWidth = 50;
		if (packageName != null) {
			double txtHeight = drawer.textHeightWithSpace();
			packageHeight = txtHeight + 5;
			packageWidth = drawer.textWidth(packageName) + drawer.getDistanceHorizontalBorderToText()*2;
			drawer.print(packageName, new PointDouble(drawer.getDistanceHorizontalBorderToText(), txtHeight), AlignHorizontal.LEFT); 
		}
		int height = getRealSize().getHeight()-1;
		int width = getRealSize().getWidth()-1;
		List<PointDouble> points = Arrays.asList(
				new PointDouble(0, packageHeight),
				new PointDouble(width, packageHeight),
				new PointDouble(width, height),
				new PointDouble(0, height),
				new PointDouble(0, height),
				new PointDouble(0, 0),
				new PointDouble(packageWidth, 0),
				new PointDouble(packageWidth, packageHeight)
				);
		drawer.drawLines(points);
		propCfg.setMinTopBuffer(packageHeight);
	}
	
}

