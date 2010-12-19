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
public class Qualifier extends Rectangle {
  String _string;
  public String getString() { return _string; }
  public Qualifier(String s,int a, int b, int c, int d) {
    super(a,b,c,d);
    _string=s;
  }
}