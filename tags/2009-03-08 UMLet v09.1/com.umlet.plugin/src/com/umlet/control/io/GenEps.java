// The UMLet source code is distributed under the terms of the GPL; see license.txt
/*
 * Created on 18.02.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.umlet.control.io;

import java.awt.*;
import java.io.*;
import java.util.*;
import org.jibble.epsgraphics.*;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.element.base.Entity;

public class GenEps {
	private static GenEps _instance;
	public static GenEps getInstance() {
		if (_instance==null) {
			_instance=new GenEps();
		}
		return _instance;
	}
	private GenEps() {
	}

	public void paint(Graphics2D g2d, DrawPanel panel) { //perform drawing operations
		Vector<Entity> v = panel.getAllEntities();
		//for (int i=v.size()-1; i>=0; i--) {
		for (int i=0; i<v.size(); i++) {
			Entity e=v.elementAt(i);
			g2d.translate(e.getX(), e.getY());
			e.paintEntity(g2d);
			g2d.translate(-e.getX(), -e.getY());
		}
	}

	public void createAndOutputEPSToFile(String filename, DiagramHandler handler) {
	  try {
		OutputStream ostream = new FileOutputStream(filename);
		createEpsToStream(ostream, handler);
	  } catch (Exception e) {
		System.out.println("IO Exception.");
	  }
	}

	public void createEpsToStream(OutputStream outputStream, DiagramHandler handler) { //create and write stream to file
		try {
			PrintWriter bw = new PrintWriter(outputStream);
			EpsGraphics2D g = new EpsGraphics2D("umlet_diagram");
			paint(g, handler.getDrawPanel());
			bw.print(g.toString());
			bw.flush();
			bw.close();
		}
		catch (Exception fnfe) {
			System.out.println("UMLet: Error: Exception in createEpsToStream: "+fnfe);
		}
	}
}
