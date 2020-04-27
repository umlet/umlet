package com.baselet.element.facet.specific;

import java.util.List;
import java.util.Locale;

import com.baselet.control.basics.XValues;
import com.baselet.control.basics.geom.Dimension;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.element.facet.KeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

public class SpecialStateTypeFacet extends KeyValueFacet {

	public static final SpecialStateTypeFacet INSTANCE = new SpecialStateTypeFacet();

	private SpecialStateTypeFacet() {}

	private enum StateTypeEnum {
		INITIAL, FINAL, FLOW_FINAL, TERMINATION, DECISION, HISTORY_SHALLOW, HISTORY_DEEP
	}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("type",
				new ValueInfo(StateTypeEnum.INITIAL, "an initial state"),
				new ValueInfo(StateTypeEnum.FINAL, "a final state for the activity"),
				new ValueInfo(StateTypeEnum.FLOW_FINAL, "a final state for a flow"),
				new ValueInfo(StateTypeEnum.HISTORY_SHALLOW, "a shallow history state"),
				new ValueInfo(StateTypeEnum.HISTORY_DEEP, "a deep history state"),
				new ValueInfo(StateTypeEnum.TERMINATION, "a termination state"),
				new ValueInfo(StateTypeEnum.DECISION, "a decision"));
	}

	@Override
	public void handleValue(final String value, final PropertiesParserState state) {
		DrawHandler drawer = state.getDrawer();
		StateTypeEnum type = StateTypeEnum.valueOf(value.toUpperCase(Locale.ENGLISH));
		Dimension s = state.getGridElementSize();
		// IMPORTANT NOTE: Make sure the element looks good in Swing and GWT because it's quite complex because of 1px displacements (check in UML State Machine palette)
		double w = getWidth(s);
		double h = getHeight(s);
		if (type == StateTypeEnum.INITIAL) {
			drawBlackEllipse(drawer, w - 1, h - 1, 1);
		}
		else if (type == StateTypeEnum.FINAL) {
			drawer.drawEllipse(0, 0, w, h);
			ColorOwn oldFg = drawer.getForegroundColor();
			drawer.setForegroundColor(ColorOwn.TRANSPARENT); // don't use foregroundcolor for the inner circle, because otherwise in Swing it would look very ugly
			double ellipseDistance = Math.max(w - 1, h - 1) / 5.5;
			drawBlackEllipse(drawer, w - ellipseDistance * 2, h - ellipseDistance * 2, ellipseDistance);
			drawer.setForegroundColor(oldFg);
		}
		else if (type == StateTypeEnum.FLOW_FINAL) {
			drawer.drawEllipse(0, 0, w, h);
			double upperY = h / 6;
			double lowerY = h - upperY;
			XValues upperXVal = XValues.createForEllipse(upperY, h, w);
			XValues lowerXVal = XValues.createForEllipse(lowerY, h, w);
			drawer.drawLine(upperXVal.getLeft(), upperY, lowerXVal.getRight(), lowerY);
			drawer.drawLine(lowerXVal.getLeft(), lowerY, upperXVal.getRight(), upperY);
		}
		else if (type == StateTypeEnum.HISTORY_SHALLOW || type == StateTypeEnum.HISTORY_DEEP) {
			String text;
			if (type == StateTypeEnum.HISTORY_SHALLOW) {
				text = "*H*";
			}
			else {
				text = "*H**";
			}
			drawer.drawEllipse(0, 0, w, h);
			double x = (w - drawer.textWidth(text)) / 2;
			double y = (h + drawer.textHeight(text)) / 2;
			drawer.print(text, new PointDouble(x, y), AlignHorizontal.LEFT);
		}
		else if (type == StateTypeEnum.TERMINATION) {
			drawer.drawLine(0, 0, w, h);
			drawer.drawLine(w, 0, 0, h);
		}
		else if (type == StateTypeEnum.DECISION) {
			drawDecision(drawer, w, h);
		}
	}

	private void drawDecision(final DrawHandler drawer, final double w, final double h) {
		drawer.drawLines(new PointDouble(0.5 + w / 2, 1), new PointDouble(w, 0.5 + h / 2), new PointDouble(0.5 + w / 2, h), new PointDouble(1, 0.5 + h / 2), new PointDouble(0.5 + w / 2, 1));
	}

	private void drawBlackEllipse(final DrawHandler drawer, double width, double height, double xY) {
		ColorOwn oldBg = drawer.getBackgroundColor();
		if (drawer.getBackgroundColor() == ColorOwn.DEFAULT_BACKGROUND) {
			drawer.setBackgroundColor(ColorOwn.BLACK.transparency(Transparency.FOREGROUND));
		}
		else {
			drawer.setBackgroundColor(drawer.getBackgroundColor().transparency(Transparency.FOREGROUND));
		}
		drawer.drawEllipse(xY, xY, width, height);
		drawer.setBackgroundColor(oldBg);
	}

	@Override
	public void parsingFinished(PropertiesParserState state, List<String> handledLines) {
		if (handledLines.isEmpty()) { // default is decision
			Dimension s = state.getGridElementSize();
			drawDecision(state.getDrawer(), getWidth(s), getHeight(s));
		}
	}

	private int getHeight(Dimension s) {
		return s.getHeight() - 1;
	}

	private int getWidth(Dimension s) {
		return s.getWidth() - 1;
	}
}
