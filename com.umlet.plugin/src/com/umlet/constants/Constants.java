// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.constants;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.UIManager;

//class in extra package because of security of custom elements
public class Constants
{
	/**** CONSTANTS LOADED FROM CONFIG ****/
	public static int default_fontsize=14;
	public static boolean start_maximized=false;
	public static boolean show_stickingpolygon=true;
	public static String ui_manager = UIManager.getSystemLookAndFeelClassName();
	public static int main_split_position = 600;
	public static int right_split_position = 400;
	public static Dimension umlet_size = new Dimension(960,750);
	public static Point umlet_location = new Point(5,5);

	/**************************************/

	public static String newline = "\n";
	public static final String defaultHelpText;

	//Text formatting labels
	public static final String underlineLabel = "_";
	public static final String boldLabel = "*";
	public static final String italicLabel = "/";

	//init values
	static     
	{
		String ctrl = "Ctrl";;
		if(System.getProperty("os.name").toUpperCase().startsWith("MAC")) {
			Constants.CTRLkey = ActionEvent.META_MASK;
			ctrl = "\u2318";
		}
		else
			Constants.CTRLkey = ActionEvent.CTRL_MASK; 


		defaultHelpText = "// Uncomment the following line to change the fontsize:" + newline + 
		"// fontsize=14" + newline + newline +
		"// Welcome to UMLet!" + newline +
		"//" + newline +
		"// Double-click on UML elements to add them to the diagram." + newline +
		"// Edit element properties by modifying the text in this panel." + newline +
		"// Edit the files in the 'palettes' directory to create your own element palettes." + newline + 
		"// Hold down " + ctrl + " to select multiple elements." + newline +
		"// Press Del to delete elements." + newline + 
		"// Press " + ctrl + "-c to copy an element, and to store the whole UML diagram to the system clipboard." + newline + 
		"// Select \"Custom elements > New...\" to create new element types." + newline + 
		"//" + newline +
		"// This text will be stored with each diagram;  use it for notes.";
	}

	//CTRL key (for meta key enabling on mac)
	public static int CTRLkey;

	public static int printpadding = 20;

	public static final int Custom_Element_Compile_Interval = 500;
	public static int LEFT=0, RIGHT=2, CENTER=1, TOP=0, BOTTOM=2;



	public static final String noautoresize = "autoresize=false";
	public static final String autoresize = "autoresize=";

	public static final Cursor lrCursor = new Cursor(Cursor.E_RESIZE_CURSOR);
	public static final Cursor tbCursor = new Cursor(Cursor.N_RESIZE_CURSOR);
	public static final Cursor nwCursor = new Cursor(Cursor.NW_RESIZE_CURSOR);
	public static final Cursor neCursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
	public static final Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	public static final Cursor moveCursor = new Cursor(Cursor.MOVE_CURSOR);
	public static final Cursor defCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	public static final Cursor crossCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
	public static final Cursor textCursor = new Cursor(Cursor.TEXT_CURSOR);

	public static final String DELIMITER_ENTITIES="~~~~~|||||~~~~~|||||";
	public static final String DELIMITER_STATE_AND_HIDDEN_STATE="/////<<<<</////<<<<<";
	public static final String DELIMITER_FIELDS="#####_____#####_____";
	public static final String DELIMITER_ADDITIONAL_ATTRIBUTES=";";
	public static int INTERFACE_LINE_LENGTH = 40;

	public static int RESIZE_TOP = 1;
	public static int RESIZE_RIGHT = 2;
	public static int RESIZE_BOTTOM = 4;
	public static int RESIZE_LEFT = 8;

	public static Point normalize(Point p, int pixels) {
		Point ret = new Point();
		double d=Math.sqrt(p.x*p.x+p.y*p.y);
		ret.x=(int)((double)p.x/d*(double)pixels);
		ret.y=(int)((double)p.y/d*(double)pixels);
		return ret;
	}

	public static Vector<String> decomposeStringsWithEmptyLines(String s, String delimiter) {
		return decomposeStringsWFilter(s,delimiter,true,false);
	}

	public static Vector<String> decomposeStrings(String s, String delimiter) {
		return decomposeStringsWFilter(s, delimiter, true, true);
	}

	public static Vector<String> decomposeStringsWithComments(String s, String delimiter) {
		return decomposeStringsWFilter(s, delimiter, false, true);
	}

	public static Vector<String> decomposeStrings(String s) {
		return decomposeStrings(s,newline);
	}

	private static Vector<String> decomposeStringsWFilter(String s, String delimiter, boolean filterComments, boolean filterNewLines) {
		s = s.replaceAll("\r\n", delimiter); //compatibility to windows \r\n
		Vector<String> ret=new Vector<String>();
		for (;;) {
			int index = s.indexOf(delimiter);
			if (index<0) {
				if(filterComments) {
					s=filterComment(s);
					if(s.startsWith("bg=")||s.startsWith("fg=") ||
							s.startsWith(Constants.autoresize)) s=""; //filter color-setting strings

				}
				if (!s.equals("") || !filterNewLines) {
					ret.add(s);
				}
				return ret;
			}
			String tmp=s.substring(0,index);
			if(filterComments) {
				tmp=filterComment(tmp);
				if(tmp.startsWith("bg=")||tmp.startsWith("fg=") ||
						s.startsWith(Constants.autoresize)) tmp=""; //filter color-setting strings
			}

			if (!tmp.equals("") || !filterNewLines) ret.add(tmp);
			s=s.substring(index+delimiter.length(), s.length());
		}
	}

	private static String filterComment(String s) {

		int pos = s.indexOf("//");
		char c;
		while(pos >= 0)
		{
			if(pos==0) return "";
			c = s.charAt(pos-1);
			if(s.length() > pos+2)
			{
				if(s.charAt(pos+2) != '/' && c != '/' && c != ':') 
					return s.substring(0,pos);
			}		
			else if (c != '/' && c != ':') 
				return s.substring(0,pos);

			pos = s.indexOf("//",pos+1);
		}
		return s;
	}   

	public static Vector<String> decomposeStringsIncludingEmptyStrings(String s, String delimiter) {
		return decomposeStringsWFilter(s,delimiter,false,false);
	}

	public static String composeStrings(Vector<String> v, String delimiter) {
		String ret=null;
		if (v!=null) {
			for (int i=0;i<v.size();i++) {
				if (ret==null) {
					ret=new String(v.elementAt(i));
				} else {
					ret=ret+delimiter+v.elementAt(i);
				}
			}
		}
		if (ret==null) ret="";
		return ret;
	}

	public static BasicStroke getStroke(int strokeType, int lineThickness) {
		BasicStroke stroke=null;
		switch (strokeType) {
		case 0: stroke = new BasicStroke(lineThickness); break;
		case 1: float dash1[] = {8.0f, 5.0f};
		stroke = new BasicStroke(lineThickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash1, 0.0f);
		break;
		case 2: float dash2[] = {1.0f, 2.0f};
		stroke = new BasicStroke(lineThickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash2, 0.0f);;
		case 3: break;
		}
		return stroke;
	}

	private static Map<RenderingHints.Key,Object> UxRenderingQualityHigh() {
		HashMap<RenderingHints.Key,Object> renderingHints= new HashMap<RenderingHints.Key,Object>();
		renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		renderingHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		renderingHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		renderingHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		renderingHints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		renderingHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		return renderingHints;
	}

	private static Map<RenderingHints.Key,Object> UxRenderingQualityLow() {
		HashMap<RenderingHints.Key,Object> renderingHints= new HashMap<RenderingHints.Key,Object>();
		return renderingHints;
	}

	/**
	 * Calculates the angle of the line defined by the koordinates
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return angle (radians)
	 */
	public static double getAngle(double x1, double y1, double x2, double y2 ) {
		double res;
		double x=x2-x1;
		double y=y2-y1;
		res = Math.atan(y/x);
		if(x>=0.0 && y>=0.0) res+=0.0;
		else if(x<0.0 && y>=0.0) res+=Math.PI;
		else if(x<0.0 && y<0.0) res+=Math.PI;
		else if(x>=0.0 && y<0.0) res+=2.0*Math.PI;
		return res;
	}

	/*******************************END STATIC*************************************************************/

	private Integer _fontsize;
	private Float zoom;

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}

	public float getZoom() {
		if(zoom == null)
			zoom = new Float(0.7);
		return zoom;
	}

	public Constants()
	{
		this._fontsize = null; 
	}  

	public int getPixelWidth(Graphics2D g2, String s) {
		if(s == null) return 0;
		return (int)this.getTextSize(g2,s).getWidth();
	}

	public int getPixelHeight(Graphics2D g2, String s) {
		if(s == null) return 0;
		return (int)this.getTextSize(g2,s).getHeight();
	}

	public Dimension getTextSize(Graphics2D g2, String s) {
		if(s == null) return null;
		if(s.length() == 0) return new Dimension(0,0);
		TextLayout tl=new TextLayout(s, getFont(), getFRC(g2));
		return new Dimension((int)tl.getBounds().getWidth(),(int)tl.getBounds().getHeight());
	}

	public FontRenderContext getFRC(Graphics2D g2) {
		FontRenderContext rendering;
		if (getFontsize()>12) {
			rendering=new FontRenderContext(null, true, true);
			g2.setRenderingHints(Constants.UxRenderingQualityHigh());
		} else {
			rendering=new FontRenderContext(null, false, false);
			g2.setRenderingHints(Constants.UxRenderingQualityLow());
		}
		return rendering;
	}

	public void write(Graphics2D g2, String s, int x, int y, int align, int valign) {
		if (s==null) return;

		boolean underline=false;
		boolean bold=false;
		boolean italic=false;
		
		String checkedString = checkStringForValidFormatsAndRemoveTheirLabels(s);
		if (checkedString.charAt(0) == '1') underline = true;
		if (checkedString.charAt(1) == '1') bold = true;
		if (checkedString.charAt(2) == '1') italic = true;
		s = checkedString.substring(3);
		
		if (bold && italic) g2.setFont(getFontBoldItalic());
		else if (bold) g2.setFont(getFontBold());
		else if (italic) g2.setFont(getFontItalic());

		s = s.replaceAll("<<", "\u00AB");
		s = s.replaceAll(">>", "\u00BB");


		if (align == CENTER)
			x = x-getPixelWidth(g2,s)/2;
		else if(align == RIGHT)
			x = x-getPixelWidth(g2,s);

		if(valign == CENTER) 
			y = y+getPixelHeight(g2,s)/2;
		else if(valign == TOP)
			y = y+getPixelHeight(g2,s);

		g2.drawString(s,x,y);
		if (underline) {
			TextLayout l=new TextLayout(s, g2.getFont(), getFRC(g2));
			Rectangle2D r2d=l.getBounds();
			if (bold) {
				g2.drawLine(x, y+getDistTextToLine()/2, x+(int)r2d.getWidth(), y+getDistTextToLine()/2);
			}
			else {
				g2.drawLine(x, y+getDistTextToLine()/2, x+(int)r2d.getWidth(), y+getDistTextToLine()/2);
			}
		}
		if (italic || bold) g2.setFont(getFont());
	}
	
	
	/**
	 * Checks the String for formats and returns the String without the valid format labels and with a starting
	 * sequence of numbers which describe the valid format labels
	 * The first number shows if the text is underlined, the second bold, the third italic
	 */
	public static String checkStringForValidFormatsAndRemoveTheirLabels(String s) {
		boolean underline=false;
		boolean bold=false;
		boolean italic=false;

		// As long as any text format applies the loop continues (any format type is only allowed once)
		while (true) {
			if (!underline && s.startsWith(Constants.underlineLabel) && s.endsWith(Constants.underlineLabel) && s.length()>2) {
				underline=true;
				s=s.substring(1,s.length()-1);
				continue;
			}
			if (!bold && s.startsWith(Constants.boldLabel) && s.endsWith(Constants.boldLabel) && s.length()>2) {
				bold=true;
				s=s.substring(1,s.length()-1);
				continue;
			}
			if (!italic && s.startsWith(Constants.italicLabel) && s.endsWith(Constants.italicLabel) && s.length()>2) {
				italic=true;
				s=s.substring(1,s.length()-1);
				continue;
			}
			break;
		}

		//The returning String starts with a number sequence to show which labels applies.
		//Warning: The first added digit is the last of the sequence after adding all digits, so we must add the last at first and the first at last
		if (italic) s = "1" + s;
		else s = "0" + s;
		if (bold) s = "1" + s;
		else s = "0" + s;
		if (underline) s = "1" + s;
		else s = "0" + s;
		
		return s;
	}

	public void setFontsize(Integer fontsize) {
		_fontsize = fontsize;
	}

	public int getFontsize() { 
		if(_fontsize == null)
			return Constants.default_fontsize;
		return _fontsize; 
	}

	public int getDistLineToText() {
		return (int)getFontsize()/4;
	}

	public int getDistTextToLine() {
		return (int)getFontsize()/4;
	}

	public int getDistTextToText() {
		return (int)getFontsize()/4;
	}

	public Font getFont() {
		return new Font("SansSerif", Font.PLAIN, getFontsize());
	}

	public Font getFontItalic() {
		return new Font("SansSerif", Font.ITALIC, getFontsize());
	}

	public Font getFontBold() {
		return new Font("SansSerif", Font.BOLD, getFontsize());
	}
	
	public Font getFontBoldItalic() {
		return new Font("SansSerif", Font.BOLD|Font.ITALIC, getFontsize());
	}
}