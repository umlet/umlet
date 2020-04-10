package com.baselet.element.sequence_aio.facet;

import com.baselet.control.basics.Line1D;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.diagram.draw.DrawHandler;

/**
 * <pre>
 * getLifelineLeftPartWidth
 *        +--+
 *            getLifelineRightPartWidth
 *           +------+
 *
 *           |
 *        +--+--+
 *        |     |
 *        |   +-+---+
 *        |   |     |
 * +-------------------+
 * |      |   |     |  |  height
 * |      |   |     |  |
 * +      |   +-+---+  +
 *        |     |
 *        +--+--+
 *           |
 * +--+             +--+
 * gap               gap
 *
 *</pre>
 */
public class Coregion implements LifelineOccurrence {

	private static final double COREGION_GAP_LIFELINE = 10;
	private static final double COREGION_HEIGHT = 10;

	private final Lifeline correspondingLifeline;
	private final int tick;
	private final boolean start;

	/**
	 * @param lifeline on which the Coregion is specified
	 * @param start if true then it is the beginning of a Coregion, otherwise it is the end of a Coregion
	 */
	public Coregion(Lifeline lifeline, int tick, boolean start) {
		correspondingLifeline = lifeline;
		this.tick = tick;
		this.start = start;
	}

	@Override
	public Line1D draw(DrawHandler drawHandler, PointDouble topLeft, PointDouble size) {
		PointDouble topLeftCoregion = new PointDouble(topLeft.x + size.x / 2.0 - getWidth() / 2.0, topLeft.y + size.y / 2.0 - COREGION_HEIGHT / 2.0);
		if (isStart()) {
			drawHandler.drawLine(topLeftCoregion.x, topLeftCoregion.y, topLeftCoregion.x + getWidth(), topLeftCoregion.y); // horizontal line
		}
		else {
			drawHandler.drawLine(topLeftCoregion.x, topLeftCoregion.y + COREGION_HEIGHT, topLeftCoregion.x + getWidth(), topLeftCoregion.y + COREGION_HEIGHT); // horizontal line
		}
		drawHandler.drawLine(topLeftCoregion.x, topLeftCoregion.y, topLeftCoregion.x, topLeftCoregion.y + COREGION_HEIGHT); // left vertical line
		drawHandler.drawLine(topLeftCoregion.x + getWidth(), topLeftCoregion.y, topLeftCoregion.x + getWidth(), topLeftCoregion.y + COREGION_HEIGHT); // right vertical line
		return null;
	}

	@Override
	public double getMinWidth(DrawHandler drawHandler) {
		return getWidth();
	}

	public double getWidth() {
		return correspondingLifeline.getLifelineRightPartWidth(tick) * 2 + COREGION_GAP_LIFELINE * 2;
	}

	@Override
	public double getAdditionalYHeight(DrawHandler drawHandler, PointDouble size) {
		return COREGION_HEIGHT - size.y;
	}

	public boolean isStart() {
		return start;
	}
}
