package com.umlet.element.experimental.element.uml;

import com.baselet.control.SharedUtils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.AlignVertical;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.geom.XValues;
import com.baselet.element.StickingPolygon;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.NewGridElement;
import com.umlet.element.experimental.Properties;
import com.umlet.element.experimental.Settings;
import com.umlet.element.experimental.facets.DefaultGlobalNonRelationFacet.ElementStyleEnum;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.GlobalFacet;
import com.umlet.element.experimental.facets.SeparatorLine;
import com.umlet.element.experimental.facets.defaultgl.BackgroundColorFacet;
import com.umlet.element.experimental.facets.defaultgl.FontSizeFacet;
import com.umlet.element.experimental.facets.defaultgl.ForegroundColorFacet;
import com.umlet.element.experimental.facets.defaultgl.LayerFacet;
import com.umlet.element.experimental.facets.defaultgl.LineThicknessFacet;

public class Actor extends NewGridElement {

	@Override
	protected Settings createSettings() {
		return new Settings() {
			@Override
			public XValues getXValues(double y, int height, int width) {
				return new XValues(0, width);
			}
			@Override
			public AlignVertical getVAlign() {
				return AlignVertical.TOP;
			}
			@Override
			public AlignHorizontal getHAlign() {
				return AlignHorizontal.CENTER;
			}
			@Override
			public ElementStyleEnum getElementStyle() {
				return ElementStyleEnum.AUTORESIZE;
			}
			@Override
			public Facet[] createFacets() {
				return new Facet[]{new SeparatorLine()};
			}
			@Override
			protected GlobalFacet[] createDefaultGlobalFacets() {
				return new GlobalFacet[] {new BackgroundColorFacet(), new FontSizeFacet(), new ForegroundColorFacet(), new LayerFacet(), new LineThicknessFacet()};
			}
			@Override
			public double getYPosStart() {
				return headToLegLength(); // equals headBodyLegLength
			}
			@Override
			public double getMinElementWidthForAutoresize() {
				return armLength()*2; // armLength
			}
		};
	}

	@Override
	public ElementId getId() {
		return ElementId.UMLActor;
	}

	@Override
	protected void updateConcreteModel(BaseDrawHandler drawer, Properties properties) {
		properties.drawPropertiesText();
		int hCenter = getRealSize().width/2;
		drawer.drawCircle(hCenter, headRadius(), headRadius()); // Head
		drawer.drawLine(hCenter-armLength(), armHeight(), hCenter+armLength(), armHeight()); // Arms
		drawer.drawLine(hCenter, headRadius()*2, hCenter, headToBodyLength()); // Body
		drawer.drawLine(hCenter, headToBodyLength(), hCenter-legSpan(), headToLegLength()); // Legs
		drawer.drawLine(hCenter, headToBodyLength(), hCenter+legSpan(), headToLegLength()); // Legs
	}

	@Override
	public StickingPolygon generateStickingBorder(Rectangle rect) {
		double hCenter = getRealSize().width/2;
		int left = SharedUtils.realignToGrid(false, hCenter-armLength(), false);
		int right = SharedUtils.realignToGrid(false, hCenter+armLength(), true)-1;
		int head = (int) headToLegLength();
		
		StickingPolygon p = new StickingPolygon(rect.x, rect.y);
		p.addPoint(left, 0);
		p.addPoint(right, 0);
		p.addPoint(right, head);
		p.addPoint(left, head, true);
		return p;
	}

	private double headToLegLength() {
		return legSpan()*2+headToBodyLength();
	}

	private double legSpan() {
		return getDrawer().getCurrentStyle().getFontSize();
	}

	private double headToBodyLength() {
		return getDrawer().getCurrentStyle().getFontSize()*2+headRadius()*2;
	}

	private double armHeight() {
		return armLength();
	}

	private double armLength() {
		return getDrawer().getCurrentStyle().getFontSize()*1.5;
	}

	private double headRadius() {
		return getDrawer().getCurrentStyle().getFontSize()/2;
	}
}

