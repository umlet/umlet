package com.umlet.element.experimental.uml;

import java.util.ArrayList;
import java.util.List;

import com.baselet.diagram.draw.geom.Point;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsRelation;

public class Relation extends NewGridElement {

	private List<Point> points = new ArrayList<Point>();
	
	@Override
	public ElementId getId() {
		return ElementId.Relation;
	}
	
	@Override
	public void updateConcreteModel() {
		drawer.drawLine(0, 0, getRealSize().width-1, getRealSize().height-1);
		properties.drawPropertiesText();
	}
	
	@Override
	public void setAdditionalAttributes(String additionalAttributes) {
		Integer firstPos = null;
		for (String pos : additionalAttributes.split(";")) {
			if (firstPos == null) {
				firstPos = Integer.valueOf(pos);
			} else {
				points.add(new Point(firstPos, Integer.valueOf(pos)));
				firstPos = null;
			}
		}
	}


	@Override
	public Settings getSettings() {
		return new SettingsRelation();
	}
}

