package com.baselet.diagram;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.enums.LineType;
import com.baselet.control.util.Utils;

/**
 * @author unknown
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@SuppressWarnings("serial")
public class SelectorFrame extends JComponent {

	private static final Logger log = LoggerFactory.getLogger(SelectorFrame.class);

	// If the frame is drawn from the right to the left or from bottom to top the offset is
	// the distance from the drawing-start-point to the actual position of the mouse cursor
	private int offset_left;
	private int offset_top;

	// If the lasso is started on an entity the starting position must be displaced by the coordinates of the entity
	private int entity_displacement_x;
	private int entity_displacement_y;

	public SelectorFrame() {
		super();
		reset();
	}

	public void reset() {
		offset_left = 0;
		offset_top = 0;
		entity_displacement_x = 0;
		entity_displacement_y = 0;
	}

	public void setDisplacement(int x, int y) {
		entity_displacement_x = x;
		entity_displacement_y = y;
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
		g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
	}

	public void resizeTo(int x, int y) {

		int locx = getX();
		int locy = getY();
		int sizx = entity_displacement_x + x - getX();
		int sizy = entity_displacement_y + y - getY();

		if (sizx - offset_left < 0) {
			locx += sizx;
			sizx = sizx * -1 + offset_left;
			offset_left = sizx;
			log.trace("LEFT (offset: " + offset_left + "px)");
		}
		else {
			sizx -= offset_left;
			locx += offset_left;
			offset_left = 0;
			log.trace("RIGHT");
		}

		if (sizy - offset_top < 0) {
			locy += sizy;
			sizy = sizy * -1 + offset_top;
			offset_top = sizy;
			log.trace("UP (offset: " + offset_top + "px)");
		}
		else {
			sizy -= offset_top;
			locy += offset_top;
			offset_top = 0;
			log.trace("DOWN");
		}

		log.trace("Starting point: (" + locx + "," + locy + ") Width: " + sizx + ", Height: " + sizy);

		this.setLocation(locx, locy);
		this.setSize(sizx, sizy);
		// TODO Uncomment to see the real lasso border -> remove after implementing lasso on entity
		// CurrentGui.getInstance().getGUI().getGraphics().drawRect(locx, locy, sizx, sizy);
	}
}
