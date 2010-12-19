// The UMLet source code is distributed under the terms of the GPL; see license.txt
/*
 * Created on 18.02.2004
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.umlet.control.io;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.jibble.epsgraphics.EpsGraphics2D;

import com.umlet.control.diagram.DiagramHandler;

public class GenEps {
	private static GenEps _instance;

	public static GenEps getInstance() {
		if (_instance == null) {
			_instance = new GenEps();
		}
		return _instance;
	}

	private GenEps() {}

	public void createAndOutputEPSToFile(String filename, DiagramHandler handler) {
		try {
			OutputStream ostream = new FileOutputStream(filename);
			createEpsToStream(ostream, handler);
		} catch (Exception e) {
			System.out.println("IO Exception.");
		}
	}

	public void createEpsToStream(OutputStream outputStream, DiagramHandler handler) { // create and write stream to file
		try {
			PrintWriter bw = new PrintWriter(outputStream);
			EpsGraphics2D grapics2d = new EpsGraphics2D("umlet_diagram");
			handler.getDrawPanel().paintEntitiesIntoGraphics2D(grapics2d);
			bw.print(grapics2d.toString());
			bw.flush();
			bw.close();
		} catch (Exception fnfe) {
			System.out.println("UMLet: Error: Exception in createEpsToStream: " + fnfe);
		}
	}
}
