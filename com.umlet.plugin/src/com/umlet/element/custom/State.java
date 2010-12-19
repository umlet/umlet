// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.*;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class State extends Entity
{
	public void paintEntity(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.handler.getFont());
		Composite[] composites = colorize(g2); //enable colors
		g2.setColor(_activeColor);
		this.handler.getFRC(g2);

		g2.setComposite(composites[1]);
	    g2.setColor(_fillColor);
	    g2.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 30, 30);
		g2.setComposite(composites[0]);
	    if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
	    
		g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 30, 30);
		
		Vector<String> tmp = Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos = 0;
		// A.Mueller start
		if (tmp.contains("--") || tmp.contains("-."))
			yPos = 2 * this.handler.getDistLineToText();
		else
			// A.Mueller end
			yPos = this.getHeight() / 2 - tmp.size() * (this.handler.getFontsize() + this.handler.getDistTextToText()) / 2;

		for (int i = 0; i < tmp.size(); i++)
		{
			String s = tmp.elementAt(i);
			// A.Mueller start
			if (s.equals("--"))
			{
				yPos += this.handler.getDistTextToLine();
				g2.drawLine(0, yPos, getWidth(), yPos);
				yPos += this.handler.getDistLineToText();
			} else if (s.equals("-."))
			{
				yPos += this.handler.getDistTextToLine();
				g2.setStroke(Constants.getStroke(1,1));
				g2.drawLine(0, yPos, getWidth(), yPos);
				g2.setStroke(Constants.getStroke(0,1));
				yPos += this.handler.getDistLineToText();
			} else
			{
				// A.Mueller end
				yPos += this.handler.getFontsize();
				this.handler.writeText(g2, s, (int) this.getWidth() / 2, yPos, true);
				yPos += this.handler.getDistTextToText();
				// A.Mueller start
			}
			// A.Mueller end
		}

		
	}
}
