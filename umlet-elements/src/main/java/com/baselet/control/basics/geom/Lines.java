package com.baselet.control.basics.geom;

import java.util.ArrayList;
import java.util.List;

public class Lines {
	public static List<PointDouble> toPoints(Line... lines) {
		List<PointDouble> points = new ArrayList<PointDouble>();
		for (Line line : lines) {
			for (PointDouble p : line.toPoints()) {
				if (points.isEmpty() || !points.get(points.size() - 1).equals(p)) {
					points.add(p);
				}
			}
		}
		return points;
	}
}
