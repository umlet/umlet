// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.base.relation;

import java.awt.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

@SuppressWarnings("serial")
public class Arrow extends Rectangle {
	  private Point _arrowEndA;
	  private Point _arrowEndB;
	  
	  //A.Mueller start
	  private Point _crossEndA;
	  private Point _crossEndB;
	  private int _arcStart;
	  private int _arcEnd;
	  //A.Mueller end
	  
	  public Point getArrowEndA() { return _arrowEndA; }
	  public Point getArrowEndB() { return _arrowEndB; }
	  public void setArrowEndA(Point p) { _arrowEndA=p; }
	  public void setArrowEndB(Point p) { _arrowEndB=p; }
	  
	  //A.Mueller start
	  public void setCrossEndA(Point p) { _crossEndA=p; }
	  public void setCrossEndB(Point p) { _crossEndB=p; }
	  public int getArcStart() { return _arcStart; }
	  public int getArcEnd() { return _arcEnd; }
	  public void setArcStart(int a) { _arcStart=a; }
	  public void setArcEnd(int a) { _arcEnd=a; }
	  public Point getCrossEndA() { return _crossEndA; }
	  public Point getCrossEndB() { return _crossEndB; }
	  //A.Mueller end
	  
	  private String _arrowType=null;
	  public String getString() { return _arrowType; }

	  public Arrow(String arrowType) {
	    super(0,0,1,1);
	    _arrowType=arrowType;
	  }
	}

