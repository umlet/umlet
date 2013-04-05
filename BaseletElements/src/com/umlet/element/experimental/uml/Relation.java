package com.umlet.element.experimental.uml;

import java.util.ArrayList;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.Properties;
import com.umlet.element.experimental.settings.Settings;
import com.umlet.element.experimental.settings.SettingsRelation;

public class Relation extends NewGridElement {

	private final float SELECTBOXSIZE = 10;
	private final float SELECTCIRCLERADIUS = 10;

	private List<Point> points = new ArrayList<Point>();

	@Override
	public ElementId getId() {
		return ElementId.Relation;
	}

	@Override
	protected void updateConcreteModel(BaseDrawHandler drawer, Properties properties) {
		//		properties.drawPropertiesText();

		// draw lines
		for (int i = 1; i < points.size(); i++) {
			Point a = points.get(i-1);
			Point b = points.get(i);
			drawer.drawLine(a.x, a.y, b.x, b.y);
		}
	}

	@Override
	protected void updateMetaDrawer(BaseDrawHandler drawer) {
		drawer.clearCache();
		if (isSelected) {
			drawer.setForegroundColor(ColorOwn.SELECTION_FG);
			// draw selection circles
			for (Point p : points) {
				drawer.drawCircle(p.x, p.y, SELECTCIRCLERADIUS);
			}
			// draw drag-box TODO place position correctly
			int x = Math.abs(points.get(points.size()-2).x - points.get(points.size()-1).x)/2 + points.get(points.size()-2).x;
			int y = Math.abs(points.get(points.size()-2).y - points.get(points.size()-1).y)/2;
			drawer.drawRectangle(x - SELECTBOXSIZE/2, y - SELECTBOXSIZE/2, SELECTBOXSIZE, SELECTBOXSIZE);
		}
	}

	@Override
	public void setAdditionalAttributes(String additionalAttributes) {
		super.setAdditionalAttributes(additionalAttributes);
		String[] split = additionalAttributes.split(";");
		for (int i = 0; i < split.length; i += 2) {
			points.add(new Point(Integer.valueOf(split[i]), Integer.valueOf(split[i+1])));
		}
	}

	@Override
	public Settings getSettings() {
		return new SettingsRelation();
	}
}

