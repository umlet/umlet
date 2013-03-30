
package com.baselet.diagram;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.baselet.control.Constants.LineType;
import com.baselet.control.Utils;

/**
 * @author unknown
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@SuppressWarnings("serial")
public class SelectorFrame extends JComponent {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

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
		if (Utils.displaceDrawingByOnePixel()) g2.drawRect(1, 1, getWidth() - 1, getHeight() - 1);
		else g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
	}

	public void resizeTo(int x, int y) {

		int locx = this.getX();
		int locy = this.getY();
		int sizx = entity_displacement_x + x - this.getX();
		int sizy = entity_displacement_y + y - this.getY();

		if (sizx - this.offset_left < 0) {
			locx += sizx;
			sizx = sizx * (-1) + this.offset_left;
			this.offset_left = sizx;
			log.info("LEFT (offset: " + offset_left + "px)");
		}
		else {
			sizx -= this.offset_left;
			locx += this.offset_left;
			this.offset_left = 0;
			log.info("RIGHT");
		}

		if (sizy - this.offset_top < 0) {
			locy += sizy;
			sizy = sizy * (-1) + this.offset_top;
			this.offset_top = sizy;
			log.info("UP (offset: " + offset_top + "px)");
		}
		else {
			sizy -= this.offset_top;
			locy += this.offset_top;
			this.offset_top = 0;
			log.info("DOWN");
		}

		log.info("Starting point: (" + locx + "," + locy + ") Width: " + sizx + ", Height: " + sizy);

		this.setLocation(locx, locy);
		this.setSize(sizx, sizy);
		// TODO Uncomment to see the real lasso border -> remove after implementing lasso on entity
		// Main.getInstance().getGUI().getGraphics().drawRect(locx, locy, sizx, sizy);
	}
}
