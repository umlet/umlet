package com.baselet.elementnew.facet.specific;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.elementnew.PropertiesParserState;
import com.baselet.elementnew.element.uml.Actor;
import com.baselet.elementnew.element.uml.Package;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.GlobalFacet;
import com.baselet.gui.AutocompletionText;

public class HierarchyFacet extends GlobalFacet {

	private static final double ARROW_LENGTH = 12;
	private static final double CIRCLE_DIAMETER = 10;
	private static final String KEY = "type";

	private static final class ReferencePoint {
		PointDouble p;
		boolean hasSymbol = false;

		public ReferencePoint(PointDouble p) {
			super();
			this.p = p;
		}

	}

	private static final class Cache {
		HierarchyType type = HierarchyType.Actor;
		private int lineNr;
		private final List<ReferencePoint> points = new ArrayList<ReferencePoint>();
	}

	public static enum HierarchyType {
		Actor, Package, WorkProcess;
	}

	public static final HierarchyFacet INSTANCE = new HierarchyFacet();

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		return true;
	}

	@Override
	public void handleLine(String line, DrawHandler drawer, PropertiesParserState state) {
		if (line.isEmpty()) {
			return;
		}

		Cache cache = state.getOrInitFacetResponse(HierarchyFacet.class, new Cache());
		for (HierarchyType type : HierarchyType.values()) {
			if (line.equals(KEY + Facet.SEP + type)) {
				cache.type = type;
				return;
			}
		}

		drawer.setDrawDelayed(true);
		String lineWithoutTabs = line.replace("\t", "");
		int tabCount = line.length() - lineWithoutTabs.length();
		int border = 10;
		PointDouble upperLeftPoint = null;
		if (cache.type == HierarchyType.Actor) {
			int actorDimension = 10;
			int actorHCenter = border + actorDimension + actorDimension * 5 * tabCount;
			int actorVTop = border + cache.lineNr * actorDimension * 6;
			Actor.drawActor(drawer, actorHCenter, actorVTop, actorDimension);

			upperLeftPoint = new PointDouble(actorHCenter, actorVTop + actorDimension * 5.5 + ARROW_LENGTH);
			PointDouble lowerRightPoint = new PointDouble(actorHCenter - actorDimension * 2, actorVTop + actorDimension * 2.5);
			drawLinesAndUpperLeftSymbol(lowerRightPoint, drawer, cache, lineWithoutTabs, tabCount, true);
			drawer.print(lineWithoutTabs, new PointDouble(actorHCenter + actorDimension * 2, actorVTop + actorDimension * 3), AlignHorizontal.LEFT);
		}
		else if (cache.type == HierarchyType.Package) {
			int fullHeight = 20;
			int fullWidth = 30;
			double xPos = border + tabCount * fullWidth * 1.4;
			double yPos = border + cache.lineNr * fullHeight * 1.6;
			Package.drawPackage(drawer, xPos, yPos, 5, 10, fullHeight, fullWidth);

			upperLeftPoint = new PointDouble(xPos + fullWidth * 0.3, yPos + fullHeight + CIRCLE_DIAMETER);
			PointDouble lowerRightPoint = new PointDouble(xPos, yPos + fullHeight * 0.5);
			drawLinesAndUpperLeftSymbol(lowerRightPoint, drawer, cache, lineWithoutTabs, tabCount, false);
			drawer.print(lineWithoutTabs, new PointDouble(xPos + fullWidth * 1.15, yPos + fullHeight * 0.8), AlignHorizontal.LEFT);
		}
		else if (cache.type == HierarchyType.WorkProcess) {
			int fullHeight = 40;
			int fullWidth = 140;
			double xPos = border + tabCount * fullWidth;
			double yPos = border + cache.lineNr * fullHeight * 1.2;
			drawer.drawEllipse(xPos, yPos, fullWidth, fullHeight);

			upperLeftPoint = new PointDouble(xPos + fullWidth * 0.5, yPos + fullHeight + ARROW_LENGTH);
			PointDouble lowerRightPoint = new PointDouble(xPos, yPos + fullHeight * 0.5);
			drawLinesAndUpperLeftSymbol(lowerRightPoint, drawer, cache, lineWithoutTabs, tabCount, true);
			drawer.print(lineWithoutTabs, new PointDouble(xPos + fullWidth / 2, yPos + fullHeight / 2 + drawer.textHeight(lineWithoutTabs) / 2), AlignHorizontal.CENTER);
		}

		// store last point as reference
		if (tabCount == 0) {
			cache.points.clear();
		}
		else if (cache.points.size() > tabCount) {
			cache.points.remove(cache.points.size() - 1);
		}
		cache.points.add(new ReferencePoint(upperLeftPoint));

		cache.lineNr++;
		drawer.setDrawDelayed(false);
	}

	private static void drawLinesAndUpperLeftSymbol(PointDouble lowerRightPoint, DrawHandler drawer, Cache cache, String lineWithoutTabs, int tabCount, boolean arrow) {
		if (tabCount != 0) {
			try {
				ReferencePoint ref = cache.points.get(tabCount - 1);
				PointDouble p1 = new PointDouble(lowerRightPoint.x, lowerRightPoint.y);
				PointDouble p2 = new PointDouble(ref.p.x, lowerRightPoint.y);
				PointDouble p3 = new PointDouble(ref.p.x, ref.p.y);
				drawer.drawLines(p1, p2, p3);
				if (!ref.hasSymbol) {
					ref.hasSymbol = true;
					if (arrow) {
						PointDouble upper = new PointDouble(ref.p.x, ref.p.y - ARROW_LENGTH);
						PointDouble lowerLeft = new PointDouble(ref.p.x - ARROW_LENGTH / 2, ref.p.y);
						PointDouble lowerRight = new PointDouble(ref.p.x + ARROW_LENGTH / 2, ref.p.y);
						drawer.drawLines(upper, lowerLeft, lowerRight, upper);
					}
					else {
						int dist = 2;
						double circleRadius = CIRCLE_DIAMETER / 2;
						drawer.drawCircle(ref.p.x, ref.p.y - circleRadius, circleRadius);
						drawer.drawLine(ref.p.x, ref.p.y - CIRCLE_DIAMETER + dist, ref.p.x, ref.p.y - dist);
						drawer.drawLine(ref.p.x - circleRadius + dist, ref.p.y - circleRadius, ref.p.x + circleRadius - dist, ref.p.y - circleRadius);
					}
				}
			} catch (IndexOutOfBoundsException e) {
				throw new RuntimeException("Too many tabs in line nr." + (cache.lineNr + 1) + ": " + lineWithoutTabs);
			}
		}
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Arrays.asList(new AutocompletionText(KEY + Facet.SEP + HierarchyType.Actor, "draws hierarchy of actors"),
				new AutocompletionText(KEY + Facet.SEP + HierarchyType.Package, "draws hierarchy of packages"),
				new AutocompletionText(KEY + Facet.SEP + HierarchyType.WorkProcess, "draws hierarchy of work processes"));
	}

}
