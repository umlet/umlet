// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.io;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.DiagramHandler;

public class GenPic {

	private static GenPic _instance;

	public static GenPic getInstance() {
		if (_instance == null) {
			_instance = new GenPic();
		}
		return _instance;
	}

	private GenPic() {}

	public void createImgToStream(String imgtype, OutputStream ostream, DiagramHandler handler) throws IOException {
		ImageIO.write(this.getImageFromDiagram(handler), imgtype, ostream);
		ostream.flush();
		ostream.close();
	}

	public void createAndOutputImgToFile(String imgtype, String filename, DiagramHandler handler) throws IOException {
		File file = new File(filename);
		ImageIO.write(this.getImageFromDiagram(handler), imgtype, file);
	}

	public BufferedImage getImageFromDiagram(DiagramHandler handler) {
		/* get height and width of drawing area to clip the image */
		Rectangle bounds = handler.getDrawPanel().getContentBounds(Constants.PRINTPADDING);

		BufferedImage im = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = im.createGraphics();

		// tanslate needed for clipping
		graphics2d.translate(-bounds.x, -bounds.y);
		graphics2d.clipRect(bounds.x, bounds.y, bounds.width, bounds.height);

		graphics2d.setColor(Color.white);
		graphics2d.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

		handler.getDrawPanel().paintEntitiesIntoGraphics2D(graphics2d);

		return im;
	}
}
