package com.baselet.element.sticking;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.baselet.control.basics.geom.PointDouble;
import com.baselet.element.sticking.StickingPolygon.StickLine;

public class StickablesTest {

	@Test
	public void moveLineLeft40_pointLeft40() throws Exception {
		PointChange change = calcChange(point(20, 20), vLine(20, 10, 30), -40, 0);
		assertPoint(-40, 0, change);
	}

	@Test
	public void moveLineRight40_pointRight40() throws Exception {
		PointChange change = calcChange(point(20, 20), vLine(20, 10, 30), 40, 0);
		assertPoint(40, 0, change);
	}

	@Test
	public void moveLineUp40_pointUp40() throws Exception {
		PointChange change = calcChange(point(20, 20), hLine(10, 30, 20), 0, -40);
		assertPoint(0, -40, change);
	}

	@Test
	public void moveLineLeftDown40_pointLeftDown40() throws Exception {
		PointChange change = calcChange(point(20, 20), hLine(10, 30, 20), 40, 40);
		assertPoint(40, 40, change);
	}

	@Test
	public void moveLineRightDown10_pointRightDown10() throws Exception {
		PointChange change = calcChange(point(20, 20), vLine(20, 10, 30), 10, 10);
		assertPoint(10, 10, change);
	}

	@Test
	public void resizeLineVertical_pointStaysSame() throws Exception {
		PointChange change = calcChange(point(20, 20), vLine(20, 10, 80), vLine(20, 10, 20));
		assertPoint(0, 0, change);
	}

	@Test
	public void resizeLineVertical_pointMovesToLowerEnd() throws Exception {
		PointChange change = calcChange(point(20, 70), vLine(20, 10, 80), vLine(20, 10, 30));
		assertPoint(0, -40, change);
	}

	@Test
	public void resizeLineHorizontal_pointStaysOnLeftEnd() throws Exception {
		PointChange change = calcChange(point(20, 50), hLine(20, 150, 50), hLine(100, 150, 50));
		assertPoint(80, 0, change);
	}

	@Test
	public void resizeLineVertical_pointStaysOnUpperEnd() throws Exception {
		PointChange change = calcChange(point(20, 50), vLine(20, 50, 200), vLine(20, 150, 200));
		assertPoint(0, 100, change);
	}

	@Test
	public void resizeLineHorizontal_pointStaysSame() throws Exception {
		PointChange change = calcChange(point(20, 50), hLine(20, 150, 50), hLine(100, 150, 50));
		assertPoint(80, 0, change);
	}

	@Test
	public void moveHorizontalResizeVertical_pointMovesHorizontalAndStaysSameVertical() throws Exception {
		PointChange change = calcChange(point(100, 100), vLine(100, 10, 200), vLine(60, 10, 150));
		assertPoint(-40, 0, change);
	}

	@Test
	public void moveHorizontalResizeVertical_pointMovesHorizontalAndMovesToEndVertical() throws Exception {
		PointChange change = calcChange(point(50, 50), vLine(50, 10, 200), vLine(100, 10, 30));
		assertPoint(50, -20, change);
	}

	@Test
	public void moveVerticalResizeHorizontal_pointMovesVerticalAndStaysSameHorizontal() throws Exception {
		PointChange change = calcChange(point(50, 50), hLine(10, 200, 50), hLine(10, 50, 100));
		assertPoint(0, 50, change);
	}

	@Test
	public void moveVerticalResizeHorizontal_pointMovesVerticalAndStaysOnLeftEnd() throws Exception {
		PointChange change = calcChange(point(50, 50), hLine(10, 200, 50), hLine(10, 20, 100));
		assertPoint(-30, 50, change);
	}

	private void assertPoint(int x, int y, PointChange change) {
		assertEquals("correct x movement", x, change.getDiffX());
		assertEquals("correct y movement", y, change.getDiffY());
	}

	private PointChange calcChange(PointDouble point, StickLine oldLine, int xChange, int yChange) {
		PointDouble oStart = oldLine.getStart();
		PointDouble oEnd = oldLine.getEnd();
		return Stickables.calcPointDiffBasedOnStickLineChange(0, point, new StickLineChange(oldLine, line(oStart.getX() + xChange, oStart.getY() + yChange, oEnd.getX() + xChange, oEnd.getY() + yChange)));
	}

	private PointChange calcChange(PointDouble point, StickLine oldLine, StickLine newLine) {
		return Stickables.calcPointDiffBasedOnStickLineChange(0, point, new StickLineChange(oldLine, newLine));
	}

	private static PointDouble point(double x, double y) {
		return new PointDouble(x, y);
	}

	private static StickLine line(double xStart, double yStart, double xEnd, double yEnd) {
		return new StickLine(point(xStart, yStart), point(xEnd, yEnd));
	}

	private static StickLine hLine(double xStart, double xEnd, double y) {
		return line(xStart, y, xEnd, y);
	}

	private static StickLine vLine(double x, double yStart, double yEnd) {
		return line(x, yStart, x, yEnd);
	}
}
