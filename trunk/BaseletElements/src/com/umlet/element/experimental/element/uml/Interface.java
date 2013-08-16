package com.umlet.element.experimental.element.uml;

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
import com.umlet.element.experimental.facets.DefaultGlobalTextFacet.ElementStyleEnum;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.SeparatorLine;

public class Interface extends NewGridElement {

	private int TOP_DISTANCE = 10;
	private int CIRCLE_SIZE = 20;

	private Rectangle circleSpace = null;
	
	@Override
	public ElementId getId() {
		return ElementId.UMLInterface;
	}

	@Override
	protected void updateConcreteModel(BaseDrawHandler drawer, Properties properties) {
		drawer.drawEllipse(circleSpace.x, circleSpace.y, circleSpace.width, circleSpace.height);
		properties.drawPropertiesText();
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon(x, y);
		p.addRectangle(circleSpace);
		return p;
	}

	/**
	 * circle is in the middle. At the moment it's not always on grid (otherwise it would "jump" around)
	 * TODO let circle have a fixed position and grow element around it (like in the old Interface)
	 */
	@Override
	public void onParsingStart() {
		//		int gridSize = (int) (getHandler().getZoomFactor() * NewGridElementConstants.DEFAULT_GRID_SIZE);
		int middlePos = getRealSize().getWidth() / 2 - CIRCLE_SIZE/2;
		//		return middlePos - (middlePos % gridSize);
		
		circleSpace = new Rectangle(middlePos, TOP_DISTANCE, CIRCLE_SIZE-1, CIRCLE_SIZE-1); //TODO -1 because otherwise circle would be cut if text is not width enough (typical problem of width based drawing, also in usecase and class)
	}

	@Override
	protected Settings createSettings() {
		return new Settings() {
			@Override
			public XValues getXValues(double y, int height, int width) {
				return new XValues(0, width);
			}
			@Override
			public AlignVertical getVAlign() {
				return AlignVertical.CENTER;
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
			public double getYPosStart() {
				return circleSpace.getY2(); // space reserved for the top circle
			}
			@Override
			public Facet[] createFacets() {
				return new Facet[]{new SeparatorLine()};
			}
		};
	}
}
