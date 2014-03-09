package com.baselet.elementnew.facet.specific;

import java.util.Arrays;
import java.util.List;

import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.elementnew.PropertiesConfig;
import com.baselet.elementnew.facet.KeyValueFacet;

public class SymbolFacet extends KeyValueFacet {

	public static SymbolFacet INSTANCE = new SymbolFacet();
	private SymbolFacet() {}

	private enum SymbolEnum {USECASE, ARTIFACT, COMPONENT}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("symbol", 
				new ValueInfo(SymbolEnum.USECASE, "draw a use case symbol"),
				new ValueInfo(SymbolEnum.ARTIFACT, "draw an artifact symbol"),
				new ValueInfo(SymbolEnum.COMPONENT, "draw a component symbol"));
	}

	private static final int DISTANCE = 5;

	@Override
	public void handleValue(String value, BaseDrawHandler drawer, PropertiesConfig propConfig) {
		drawer.setDrawDelayed(true);
		ColorOwn prevBackgroundColor = drawer.getCurrentStyle().getBgColor();
		drawer.setBackgroundColor(ColorOwn.TRANSPARENT);
		SymbolEnum symbol = SymbolEnum.valueOf(value.toUpperCase());
		double eW = propConfig.getGridElementSize().getWidth();
		double fs = drawer.getCurrentStyle().getFontSize();
		if (symbol == SymbolEnum.USECASE) {
			double cW = fs*2.5;
			double cH = fs;
			drawer.drawEllipse(eW-cW-DISTANCE, DISTANCE, cW, cH);
			propConfig.addToTopBuffer(DISTANCE);
		}
		else if (symbol == SymbolEnum.ARTIFACT) {
			double cW = fs*1.5;
			double cH = fs*1.8;
			double corner = fs*0.5;
			List<PointDouble> p = Arrays.asList(
					new PointDouble(eW-cW-DISTANCE,DISTANCE),
					new PointDouble(eW-DISTANCE-corner,DISTANCE),
					new PointDouble(eW-DISTANCE,DISTANCE+corner),
					new PointDouble(eW-DISTANCE,DISTANCE+cH),
					new PointDouble(eW-cW-DISTANCE,DISTANCE+cH)
					);
			PointDouble px = new PointDouble(eW-DISTANCE-corner, DISTANCE+corner);
			drawer.drawLines(p.get(0), p.get(1), p.get(2), p.get(3), p.get(4), p.get(0));
			drawer.drawLines(p.get(1), px, p.get(2));
			propConfig.addToTopBuffer(DISTANCE + fs*0.3);
		}
		else if (symbol == SymbolEnum.COMPONENT) {
			double partHeight = fs*0.4;
			double nonPartHeight = fs*0.3;
			double partWidth = partHeight*2;
			double cH = partHeight * 2 + nonPartHeight * 3;
			double cW = cH * 0.8;
			drawer.drawRectangle(eW-cW-partWidth/2-DISTANCE, DISTANCE+nonPartHeight, partWidth, partHeight); // upper small rect
			drawer.drawRectangle(eW-cW-partWidth/2-DISTANCE, DISTANCE+partHeight+nonPartHeight*2, partWidth, partHeight); // lower small rect
			drawer.drawLine(eW-cW-DISTANCE, DISTANCE+partHeight+nonPartHeight, eW-cW-DISTANCE, DISTANCE+partHeight+nonPartHeight*2); // connection between 2 rects
			drawer.drawLines(Arrays.asList(
					new PointDouble(eW-cW-DISTANCE, DISTANCE+nonPartHeight),
					new PointDouble(eW-cW-DISTANCE, DISTANCE),
					new PointDouble(eW-DISTANCE, DISTANCE),
					new PointDouble(eW-DISTANCE, DISTANCE+cH),
					new PointDouble(eW-cW-DISTANCE, DISTANCE+cH),
					new PointDouble(eW-cW-DISTANCE, DISTANCE+cH-nonPartHeight)
					));
			propConfig.addToTopBuffer(DISTANCE + fs*0.3);
		}
		drawer.setDrawDelayed(false);
		drawer.setBackgroundColor(prevBackgroundColor);
	}

}
