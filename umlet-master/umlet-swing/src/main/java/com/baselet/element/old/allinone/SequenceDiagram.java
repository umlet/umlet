package com.baselet.element.old.allinone;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.font.TextLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sourceforge.jlibeps.epsgraphics.EpsGraphics2D;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.Direction;
import com.baselet.control.enums.FormatLabels;
import com.baselet.control.enums.LineType;
import com.baselet.control.util.Utils;
import com.baselet.diagram.FontHandler;
import com.baselet.element.interfaces.GridElementDeprecatedAddons;
import com.baselet.element.old.OldGridElement;

// An interaction represents a synchronous/asynchronous message
// that is sent between two objects.
class Interaction {

	private final int srcObj;
	private final boolean srcObjHasControl;
	private final int arrowKind; // 1=SYNC, 2= ASYNC, 3=EDGE, 4=FILLED
	private final int lineKind; // 1=SOLID, 2=DOTTED
	private final int destObj;
	private final boolean destObjHasControl;
	private final String methodName;

	public Interaction(int srcObj, boolean srcObjHasControl, int arrowKind, int lineKind,
			int destObj, boolean destObjHasControl, String methodName) {
		this.srcObj = srcObj;
		this.srcObjHasControl = srcObjHasControl;
		this.arrowKind = arrowKind;
		this.lineKind = lineKind;
		this.destObj = destObj;
		this.destObjHasControl = destObjHasControl;
		this.methodName = methodName;
	}

	public boolean hasControl(int objNum) {
		return srcObjHasControl && srcObj == objNum ||
				destObjHasControl && destObj == objNum;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Interaction)) {
			return false;
		}
		Interaction i = (Interaction) o;

		return srcObj == i.srcObj &&srcObjHasControl == i.srcObjHasControl &&
				arrowKind == i.arrowKind && destObj == i.destObj &&
				destObjHasControl == i.destObjHasControl &&
				methodName == null || methodName.equals(i.methodName);
	}

	@Override
	public int hashCode() {
		return (methodName != null ? methodName.hashCode() : 1) +srcObj +
				(srcObjHasControl ? 1 : 0) + arrowKind + destObj + (destObjHasControl ? 1 : 0);
	}

	public int getSrcObj() {
		return srcObj;
	}

	public boolean getSrcObjHasControl() {
		return srcObjHasControl;
	}

	public int getArrowKind() {
		return arrowKind;
	}

	public int getLineKind() {
		return lineKind;
	}

	public int getDestObj() {
		return destObj;
	}

	public boolean getDestObjHasControl() {
		return destObjHasControl;
	}

	public String getMethodName() {
		return methodName;
	}
}

// Contains all interactions entered by the user and
// offers various comfort-functions for finding and
// working with interactions
class InteractionManagement {
	private final Set<Interaction>[] level;

	// private Set[] level;

	@SuppressWarnings("unchecked")
	InteractionManagement(int numLevels) {
		level = new HashSet[numLevels];
		for (int i = 0; i < numLevels; i++) {
			level[i] = new HashSet<Interaction>();
		}
	}

	public boolean controlBoxExists(int levelNum, int objNum) {
		Iterator<Interaction> it = level[levelNum - 1].iterator();
		while (it.hasNext()) {
			Interaction ia = it.next();
			if (ia.hasControl(objNum)) {
				return true;
			}
		}
		return false;
	}

	public void add(int numLevel, Interaction i) {
		level[numLevel - 1].add(i);
	}

	public Set<Interaction> getInteractionsInLevel(int levelNum) {
		return level[levelNum - 1];
	}

	public int getNumLevels() {
		return level.length;
	}
}

@SuppressWarnings("serial")
public class SequenceDiagram extends OldGridElement {

	public int controlFlowBoxWidth = 20;

	// the dimensions for the rectangle(s) (=objects) in the first line
	public int rectDistance; // computed distance between two columns
	public int rectHeight; // computed
	public int rectWidth; // computed
	// int extraTextSpace = 0;

	public int borderDistance = 10; // d between the component bordeR and the diagram
	private int yOffsetforTitle = 0;

	public int rectToFirstLevelDistance = 0;
	public int levelHeight = 30;// LME//60;

	// these two constants are important for the arrowhead
	public int arrowX = 5;
	public int arrowY = 5;

	public static final int SYNC = 1;
	public static final int ASYNC = 2;
	public static final int EDGE = 3; // LME
	public static final int FILLED = 4; // LME
	public static final int SOLID = 1; // LME
	public static final int DOTTED = 2; // LME

	private Map<String, Integer> labeltonumber;
	private int levelNum = 0;
	private InteractionManagement im;

	@Override
	public void paintEntity(Graphics g) {
		rectDistance = 60;

		zoomValues();
		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		// Some unimportant initialization stuff; setting color, font
		// quality, etc. You should not have to change this.
		Graphics2D g2 = (Graphics2D) g;
		FontHandler fontHandler = HandlerElementMap.getHandlerForElement(this).getFontHandler();
		g2.setFont(fontHandler.getFont());
		g2.setColor(fgColor);

		// draw the border
		g2.drawRect(0, 0, getRectangle().width - 1, getRectangle().height - 1);

		levelNum = 1;

		Vector<String> lines = Utils.decomposeStrings(getPanelAttributes());

		if (lines.size() == 0) {
			return;
		}
		if (lines.elementAt(0).startsWith("title:")) {
			String title = lines.elementAt(0).substring("title:".length());
			if (title != null && title.length() > 0) {
				fontHandler.writeText(g2, title, (int) (5 * zoom), (int) fontHandler.getFontSize() + (int) fontHandler.getDistanceBetweenTexts(), AlignHorizontal.LEFT);
				int titlewidth = (int) fontHandler.getTextWidth(title);
				int ty = (int) (8 * zoom) + (int) (fontHandler.getFontSize() + fontHandler.getDistanceBetweenTexts());
				g2.drawLine(0, ty, titlewidth + (int) (10 * zoom), ty);
				g2.drawLine(titlewidth + (int) (10 * zoom), ty, titlewidth + ty + (int) (10 * zoom), 0);
				lines.remove(0);
				yOffsetforTitle = (int) (25 * zoom);
			}
		}
		else {
			yOffsetforTitle = 0;
		}

		for (int i = 1; i < lines.size(); i++) {
			String element = lines.elementAt(i);
			if (element.indexOf("iframe{") >= 0) {
				element = "9999->0:" + element; // dummy: space for interactionframe
				lines.set(i, element);
			}
			else if (element.indexOf("iframe}") >= 0) {
				element = "9999<-0:" + element; // dummy: space for interactionframe
				lines.set(i, element);
			}
			// AB: match whitespace characters from the beginning to the end of the line
			if (lines.elementAt(i).matches("\\A\\s*\\z")) {
				continue;
			}
			levelNum++;
		}

		if (lines.size() == 0) {
			return; // return if it only has the title line (Issue 146)
		}

		String firstLine = lines.elementAt(0);
		Vector<String> obj = Utils.decomposeStrings(firstLine, "|");
		int numObjects = obj.size();

		// LABEL ADDING
		// get the labels of the Sequencediagram
		StringBuilder sb = new StringBuilder(""); // delete the ids from the header
		labeltonumber = new HashMap<String, Integer>();

		Pattern p_label = Pattern.compile("([^\\~]+)(\\~([a-zA-Z0-9]+))?(\\_)?");
		for (int i = 1; i <= numObjects; i++) {
			Matcher m = p_label.matcher(obj.get(i - 1));
			if (m.matches() && m.group(2) != null) {
				labeltonumber.put(m.group(3), i);
				sb.append("|").append(m.group(1)).append(m.group(4) == null ? "" : m.group(4));
			}
			else {
				sb.append("|").append(obj.get(i - 1));
			}
			if (!labeltonumber.containsKey(Integer.toString(i))) // only write number if no other label has this number
			{
				labeltonumber.put(Integer.toString(i), i); // columnnumber as label for backward compatibility
			}
		}
		String newhead = sb.toString();
		obj = Utils.decomposeStrings(newhead.length() > 0 ? newhead.substring(1) : "", "|");
		// LABELADDING STOP (exchanged parseInteger Methods with labeltonumber.get methods

		calcWidthOfLineHeaderBoxes(g2, fontHandler, obj, numObjects);

		// parse the messages
		int curLevel = 0;
		im = new InteractionManagement(levelNum);
		String boxStrings = "";
		for (int i = 1; i < lines.size(); i++) {
			String methodName = "";
			if (lines.elementAt(i).matches("\\A\\s*\\z")) {
				continue;
			}
			curLevel++;
			Vector<String> interactions = Utils.decomposeStrings(lines.elementAt(i), ";");

			for (int j = 0; j < interactions.size(); j++) {
				Pattern p = Pattern.compile("\\A(\\w+)(->>|->|-/>|.>>|.>|./>|->>>|.>>>|<<-|<-|</-|<<.|<.|</.|<<<-|<<<.)(\\w+)(:((\\w+)(,(\\w+))*))?(?::(.*))?\\z");
				// Pattern.compile("\\A(\\d+)(->>|->|-/>|.>>|.>|./>|->>>|.>>>)(\\d+)(:((\\d+)(,(\\d+))*))*(?::(.*))?\\z");

				// 1->2:1,2:methodName
				// 1->2:abc
				Matcher m = p.matcher(interactions.elementAt(j));

				if (!m.matches()) {
					continue;
				}
				Integer srcObj = labeltonumber.get(m.group(1));
				Integer destObj = labeltonumber.get(m.group(3));
				String methodNameFromText = m.group(9);
				if (srcObj != null && destObj != null && methodNameFromText != null) {
					Integer span = Math.abs(srcObj - destObj);
					if (span != 0) {
						double lineSpaceRequiredForMessage = fontHandler.getTextWidth(methodNameFromText) / span;
						double totalDist = lineSpaceRequiredForMessage - rectWidth + controlFlowBoxWidth; // add the rectWidth (because the text can exceed the half rect to the left and right and add the controlFlowBoxWidth to avoid text overlapping with control flow boxes
						rectDistance = (int) Math.max(rectDistance, totalDist);
					}
				}

				int arrowKind = -1;
				int lineKind = -1;
				boolean reverse = false; // arrow direction flag
				if (m.group(2).equals("->")) {
					arrowKind = ASYNC;
					lineKind = SOLID;
				}
				else if (m.group(2).equals("->>")) {
					arrowKind = SYNC;
					lineKind = SOLID;
				}
				else if (m.group(2).equals("-/>")) {
					arrowKind = EDGE;
					lineKind = SOLID;
				} // LME
				else if (m.group(2).equals("->>>")) {
					arrowKind = FILLED;
					lineKind = SOLID;
				} // LME
				else if (m.group(2).equals(".>")) {
					arrowKind = ASYNC;
					lineKind = DOTTED;
				} // LME
				else if (m.group(2).equals(".>>")) {
					arrowKind = SYNC;
					lineKind = DOTTED;
				} // LME
				else if (m.group(2).equals("./>")) {
					arrowKind = EDGE;
					lineKind = DOTTED;
				} // LME
				else if (m.group(2).equals(".>>>")) {
					arrowKind = FILLED;
					lineKind = DOTTED;
				} // LME
				else if (m.group(2).equals("<-")) {
					arrowKind = ASYNC;
					lineKind = SOLID;
					reverse = true;
				}
				else if (m.group(2).equals("<<-")) {
					arrowKind = SYNC;
					lineKind = SOLID;
					reverse = true;
				}
				else if (m.group(2).equals("</-")) {
					arrowKind = EDGE;
					lineKind = SOLID;
					reverse = true;
				} // LME
				else if (m.group(2).equals("<<<-")) {
					arrowKind = FILLED;
					lineKind = SOLID;
					reverse = true;
				} // LME
				else if (m.group(2).equals("<.")) {
					arrowKind = ASYNC;
					lineKind = DOTTED;
					reverse = true;
				} // LME
				else if (m.group(2).equals("<<.")) {
					arrowKind = SYNC;
					lineKind = DOTTED;
					reverse = true;
				} // LME
				else if (m.group(2).equals("</.")) {
					arrowKind = EDGE;
					lineKind = DOTTED;
					reverse = true;
				} // LME
				else if (m.group(2).equals("<<<.")) {
					arrowKind = FILLED;
					lineKind = DOTTED;
					reverse = true;
				} // LME

				String group = m.group(5);
				if (group == null) {
					group = "#";
					String element = interactions.elementAt(j);
					if (element.indexOf("iframe") >= 0) {
						group += element.substring(element.indexOf("iframe")); // append info for interactionframe
					}
				}
				else // LABLING ADD
				{
					String[] grouparray = group.split(",");
					group = "";
					for (String tmp : grouparray) {
						Integer tempgroup = labeltonumber.get(tmp);
						if (tempgroup != null) {
							group += "," + tempgroup;
						}
					}
					if (group.length() > 0) {
						group = group.substring(1);
					}
					else {
						if (grouparray.length == 1) {
							group = "#";
							methodName = grouparray[0];
						}
					}
				} // STOP LABLING ADD
				boxStrings += ";" + group; // LME: get alive Objects

				boolean srcObjHasControl = srcObj != null ? group.contains(String.valueOf(srcObj)) : false;
				boolean destObjHasControl = destObj != null ? group.contains(String.valueOf(destObj)) : false;

				if (methodName == null || methodName.isEmpty()) {
					methodName = methodNameFromText;
				}

				// LME: removed (in V6) since not necessary
				// if(destObj==srcObj) levelNum++; //LME: expand the Entity's size

				if (srcObj == null || destObj == null) {
					continue;
				}

				if (!reverse) {
					im.add(curLevel, new Interaction(srcObj, srcObjHasControl, arrowKind, lineKind, destObj, destObjHasControl, methodName)); // normal arrow direction 1->2
				}
				else {
					im.add(curLevel, new Interaction(destObj, destObjHasControl, arrowKind, lineKind, srcObj, srcObjHasControl, methodName)); // reverse arrow direction 1<-2
				}
			} // #for
		}
		// end message parsing

		// draw the first line of the sequence diagram
		int ypos = borderDistance + yOffsetforTitle;
		int xpos = borderDistance;
		for (int i = 0; i < numObjects; i++) {
			boolean underline = false;
			String s = obj.elementAt(i);
			if (s.startsWith(FormatLabels.UNDERLINE.getValue()) && s.endsWith(FormatLabels.UNDERLINE.getValue()) && s.length() > 2) {
				underline = true;
				s = s.substring(1, s.length() - 1);
			}
			TextLayout layout = new TextLayout(s, fontHandler.getFont(),
					g2.getFontRenderContext());

			g2.drawRect(xpos, ypos, rectWidth - 1, rectHeight - 1);

			int dx = (rectWidth - 2 - (int) Math.floor(layout.getBounds().getWidth() + 1)) / 2;
			int dy = (rectHeight - 2 - (int) Math.floor(layout.getBounds().getHeight() + 1)) / 2;
			int tx = xpos + dx;
			int ty = ypos + dy + (int) layout.getBounds().getHeight();

			layout.draw(g2, tx, ty);

			if (underline) {
				g2.drawLine(tx,
						ty + (int) fontHandler.getDistanceBetweenTexts() / 2,
						tx + (int) layout.getBounds().getWidth(),
						ty + (int) fontHandler.getDistanceBetweenTexts() / 2);
			}

			xpos += rectWidth + rectDistance;
		}

		// draw the messages
		int maxTextXpos = drawMessages(g2);
		maxTextXpos += 3 * fontHandler.getDistanceBetweenTexts(); // add extra space
		if (boxStrings.length() > 1) {
			try {
				drawControlFlowBoxesWithLines(g2, boxStrings.substring(1), numObjects); // LME: 1,2;1,2;... cut first ;-character
			} catch (ArrayIndexOutOfBoundsException e) {
				// do nothing: this exception is thrown, when entering text,
				// that is not rendered to an control flow box
			}
		}

		// set our component to the correct size
		int rWidth = rectWidth * numObjects + rectDistance * (numObjects - 1) + 2 * borderDistance;
		int rHeight = 2 * borderDistance + yOffsetforTitle + rectHeight + rectToFirstLevelDistance + levelNum * levelHeight;
		rWidth = rWidth > maxTextXpos ? rWidth : maxTextXpos;
		// align the borders to the grid
		rWidth += HandlerElementMap.getHandlerForElement(this).getGridSize() - rWidth % HandlerElementMap.getHandlerForElement(this).getGridSize();
		rHeight += HandlerElementMap.getHandlerForElement(this).getGridSize() - rHeight % HandlerElementMap.getHandlerForElement(this).getGridSize();
		setSize(rWidth, rHeight);
	}

	private void calcWidthOfLineHeaderBoxes(Graphics2D g2, FontHandler fontHandler, Vector<String> obj, int numObjects) {
		// find out the width of the column with the longest text
		double maxWidth = 0;
		double maxHeight = 0;
		for (int i = 0; i < numObjects; i++) {
			String s = obj.elementAt(i);
			if (s.startsWith(FormatLabels.UNDERLINE.getValue()) && s.endsWith(FormatLabels.UNDERLINE.getValue()) && s.length() > 2) {
				s = s.substring(1, s.length() - 1);
			}
			TextLayout layout = new TextLayout(s, fontHandler.getFont(), g2.getFontRenderContext());
			maxWidth = Math.max(layout.getBounds().getWidth(), maxWidth);
			maxHeight = Math.max(layout.getBounds().getHeight(), maxHeight);
		}

		rectWidth = (int) Math.floor(maxWidth + 1) + 2 * (int) fontHandler.getDistanceBetweenTexts() + (int) fontHandler.getFontSize();
		rectHeight = (int) Math.floor(maxHeight + 1) + (int) fontHandler.getDistanceBetweenTexts() + (int) fontHandler.getFontSize();
	}

	private int drawMessages(Graphics2D g2) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		int maxTextXpos = 0;
		for (int i = 0; i < im.getNumLevels(); i++) {
			Set<Interaction> interactions = im.getInteractionsInLevel(i + 1);
			Iterator<Interaction> it = interactions.iterator();
			while (it.hasNext()) {
				Interaction ia = it.next();
				if (ia.getSrcObj() == ia.getDestObj()) {
					// draw an arc-arrow
					int xTextOffset = 0;
					int w = (int) (30 * zoom);
					int h = (int) (levelHeight * 0.66);
					int x = hCenterForObj(ia.getSrcObj()) - w / 2;
					// nt y= vCenterForLevel(i+1) - h/2;
					int ay = vCenterForLevel(i + 1) + (int) (5 * zoom); // + levelHeight/2 -1;
					if (im.controlBoxExists(i + 1, ia.getSrcObj())) {
						x += controlFlowBoxWidth / 2;
						xTextOffset = controlFlowBoxWidth / 2;
					}
					g2.drawArc(x, ay, w, h, 90, -180);
					Point p1 = new Point(x + w / 2, ay + h);
					Point d1 = new Point(x + w / 2 + (int) (3 * zoom), p1.y - (int) (6 * zoom));
					Point d2 = new Point(x + w / 2 + (int) (4 * zoom), p1.y + (int) (4 * zoom));

					if (ia.getArrowKind() == ASYNC) { // Pfeil offen
						g2.drawLine(p1.x, p1.y, d1.x, d1.y);
						g2.drawLine(p1.x, p1.y, d2.x, d2.y);
					}
					else if (ia.getArrowKind() == SYNC) {
						int[] xs = { p1.x, d1.x, d2.x };
						int[] ys = { p1.y, d1.y, d2.y };
						Color oldColor = g2.getColor();
						g2.setColor(bgColor);
						g2.fillPolygon(xs, ys, 3);
						g2.setColor(oldColor);
						g2.drawPolygon(xs, ys, 3);
					}
					else if (ia.getArrowKind() == EDGE) {
						g2.drawLine(p1.x, p1.y, d2.x, d2.y);
					}
					else if (ia.getArrowKind() == FILLED) {
						Polygon p = new Polygon();
						p.addPoint(p1.x, p1.y);
						p.addPoint(d1.x, d1.y);
						p.addPoint(d2.x, d2.y);
						g2.fillPolygon(p);
					}

					// print the methodname
					if (ia.getMethodName() != null && !ia.getMethodName().equals("")) {
						int fx1 = x + w + 2;
						int fy1 = ay;
						int fx2 = hCenterForObj(ia.getSrcObj()) + rectWidth / 2;
						int fy2 = ay + h;
						int tx = printMethodName(g2, ia.getMethodName(), fx1 + xTextOffset, fx2 + xTextOffset,
								fy1, fy2, true, false);
						maxTextXpos = maxTextXpos > tx ? maxTextXpos : tx;
					}

				}
				else {
					// draw an arrow from the source-object to the destination object
					int begX = hCenterForObj(ia.getSrcObj());
					int endX = ia.getSrcObj() < ia.getDestObj() ? hCenterForObj(ia.getDestObj()) - 1 : hCenterForObj(ia.getDestObj()) + 1;
					int arrowY = vCenterForLevel(i + 1) + levelHeight / 2 - 1;

					if (ia.getSrcObjHasControl()) { // LME: shrink arrow if box exists
						begX += ia.getSrcObj() < ia.getDestObj() ? controlFlowBoxWidth / 2 : -controlFlowBoxWidth / 2;
					}
					if (ia.getDestObjHasControl()) { // LME: shrink arrow if box exists
						endX += ia.getSrcObj() < ia.getDestObj() ? -controlFlowBoxWidth / 2 : controlFlowBoxWidth / 2;
					}

					drawArrow(g2, new Point(begX, arrowY), new Point(endX, arrowY), ia.getArrowKind(), ia.getLineKind());

					if (ia.getMethodName() != null && !ia.getMethodName().equals("")) {
						final int b = 2;
						if (ia.getSrcObj() < ia.getDestObj()) {
							int tx = printMethodName(g2, ia.getMethodName(), begX + b, endX - arrowX - b, arrowY - 1 - levelHeight / 1, arrowY - 1, false, true);
							maxTextXpos = maxTextXpos > tx ? maxTextXpos : tx;
						}
						else {
							int tx = printMethodName(g2, ia.getMethodName(), endX + arrowX + b, begX - b, arrowY - 1 - levelHeight / 2, arrowY - 1, false, true);
							maxTextXpos = maxTextXpos > tx ? maxTextXpos : tx;
						}
					}
				}
			}
		}
		return maxTextXpos;
	}

	// prints the given methodName in an intelligent manner into
	// the supplied rectangle.
	// The method may put pixels anywhere into the supplied rectangle
	// including the borders. (In other words eg. the point begX/endY
	// may be set by this method.)
	// if the fontsize gets very big this method may cross the vertical borders.
	private int printMethodName(Graphics2D g2, String methodName,
			int begX, int endX, int begY, int endY,
			boolean centerVertically, boolean centerHorizontically) {

		if (methodName == null || methodName.equals("")) {
			log.error("SequenceDiagram->printMethodName was called with an invalid argument.");
			return 0;
		}

		Font font = HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont();
		TextLayout layout = new TextLayout(methodName, font, g2.getFontRenderContext());

		// draw it horizontally centered
		int dx = centerHorizontically ? (endX - begX - (int) layout.getBounds().getWidth()) / 2 : 0;
		int dy = centerVertically ? (endY - begY - (int) layout.getBounds().getHeight()) / 2 : 1;

		layout.draw(g2, begX + dx, endY - dy);

		return begX + dx + (int) layout.getBounds().getWidth();
	}

	public void drawControlFlowBoxesWithLines(Graphics2D g2, String s, int numObjects) { // LME
		int level = 1;
		StringTokenizer mainTokens = new StringTokenizer(s, ";");

		int tokNum = mainTokens.countTokens();
		int[][] tField = new int[numObjects][tokNum + 2];

		Vector<Integer> interactionframes = new Vector<Integer>();
		HashMap<String, String> interactionframesText = new HashMap<String, String>();

		// collect all tokens into an array: create another view to sequentially access data of a specific object
		// 1,2;#;1,3;1,3;1;3 will be transfomed to
		// tField: [110]
		// [000]
		// [101]
		// [101]
		// [100]
		// [001]
		while (mainTokens.hasMoreTokens()) {
			String main = mainTokens.nextToken();
			if (main.indexOf("#") >= 0) { // if no box, clear entire row
				for (int i = 0; i < numObjects; i++) {
					tField[i][level - 1] = 0; // clear
				}
				if (main.indexOf("#iframe{") >= 0 || main.indexOf("#iframe}") >= 0) {
					if (main.indexOf("#iframe{") >= 0) {
						interactionframes.add(Integer.valueOf(level));
					}
					else {
						interactionframes.add(Integer.valueOf(level * -1)); // distinguish betweeen start and end of the iframe
					}
					if (main.indexOf("iframe{:") >= 0) {
						interactionframesText.put("" + level, main.substring(main.indexOf("iframe{:") + 8)); // put text into hashmap
					}
				}
				level++;
			}
			else {
				StringTokenizer innerT = new StringTokenizer(main, ",");
				for (int i = 0; i < numObjects; i++) {
					tField[i][level - 1] = 0; // clear
				}
				while (innerT.hasMoreTokens()) {
					String is = innerT.nextToken();
					int objNum = Integer.parseInt(is);
					tField[objNum - 1][level - 1] = 1;
				}
				level++;
			} // #else
		} // #while

		for (int actObjNum = 0; actObjNum < numObjects; actObjNum++) {
			// controlFlowBoxWidth = controlFlowBoxWidth * getHandler().getGridSize() / 10;
			// rectDistance = rectDistance / getHandler().getGridSize() / 10;
			// levelHeight = levelHeight / getHandler().getGridSize() / 10;

			int offset = 2;
			int objNum = actObjNum + 1;
			int x1 = hCenterForObj(objNum) - controlFlowBoxWidth / 2;
			int startLevel = -1, boxSize = 0;

			int lineX = hCenterForObj(actObjNum + 1);
			int lineY1 = borderDistance + yOffsetforTitle + rectHeight;

			for (int i = 0; i < tokNum + 1; i++) {
				if (tField[actObjNum][i] == 1) {
					if (startLevel == -1) {
						startLevel = i;
					}
					boxSize++;
				}
				if (tField[actObjNum][i] == 0 && startLevel != -1) {
					int y1 = vCenterForLevel(startLevel + offset) - levelHeight - 1;
					g2.drawRect(x1, y1, controlFlowBoxWidth - 1, levelHeight * boxSize); // draw the box

					g2.setStroke(Utils.getStroke(LineType.DASHED, 1)); // #draw the line between the boxes
					g2.drawLine(lineX, lineY1, lineX, y1); // #
					g2.setStroke(Utils.getStroke(LineType.SOLID, 1)); // #
					lineY1 = y1 + levelHeight * boxSize; // #
					startLevel = -1;
					boxSize = 0;
				}
			} // #for(int i
				// LME: draw the tail
			int lineY2 = borderDistance + yOffsetforTitle + rectHeight + levelNum * levelHeight + rectToFirstLevelDistance;
			g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
			g2.drawLine(lineX, lineY1, lineX, lineY2);
			g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
		} // #for(int actObjNum

		// LME: draw the interaction frames
		int fullSets = interactionframes.size() - interactionframes.size() % 2;
		if (fullSets >= 2) {
			int pos = 0;
			while (pos < fullSets) {
				pos = recurseInteractionFrames(g2, interactionframes, interactionframesText, pos, 0);
				pos++;
			}
		}
	}

	/**
	 * Recusion level step of interactionframes
	 * ie:
	 * iframe{
	 * iframe{
	 * }
	 * iframe{
	 * }
	 * }
	 */
	public int recurseInteractionFrames(Graphics2D g2, Vector<Integer> interactionframes, HashMap<String, String> interactionframesText, int pos, int recursionLevel) {
		int pos1 = interactionframes.elementAt(pos);
		int posX;
		while (pos < interactionframes.size() && (posX = interactionframes.elementAt(pos)) > 0) { // traverse through iframes an same level
			pos1 = posX;
			pos++;
			pos = recurseInteractionFrames(g2, interactionframes, interactionframesText, pos, recursionLevel + 1); // step on level deeper into recursion
			if (pos1 <= 0) {
				return pos;
			}
			int pos2 = interactionframes.elementAt(pos) * -1;
			drawInteractionFrame(g2, pos1, pos2, recursionLevel, interactionframesText.get("" + pos1));
			pos++;
		}
		return pos;
	}

	private void drawInteractionFrame(Graphics2D g2, int pos1, int pos2, int recursionLevel, String text) {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		int pos11 = (pos1 + 1) * levelHeight + yOffsetforTitle;
		int h = (pos2 - pos1) * levelHeight;
		int x = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() * 2 + recursionLevel * 4;
		g2.drawRect(x, pos11, getRectangle().width - (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() * 4 - 1 - recursionLevel * 8, h);
		int uLinePos = pos11 + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() + (int) (HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts());
		int textPos = pos11 + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize();

		int textWidth = 0;
		if (text == null || text.equals("")) {
			text = " ";
		}
		g2.drawString(text, x + (int) (10 * zoom), textPos);
		int pW = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextWidth(text);
		textWidth = pW > textWidth ? pW : textWidth;

		g2.drawLine(x, uLinePos, x + textWidth + (int) (15 * zoom), uLinePos);
		g2.drawLine(x + textWidth + (int) (15 * zoom), uLinePos, x + textWidth + (int) (25 * zoom), pos11 + (int) (10 * zoom));
		g2.drawLine(x + textWidth + (int) (25 * zoom), pos11, x + textWidth + (int) (25 * zoom), pos11 + (int) (10 * zoom));
	}

	public void drawArrow(Graphics2D g2, Point srcObj, Point destObj, int arrowKind, int lineKind) {
		Point p1, p2;
		if (srcObj.x < destObj.x) {
			p1 = new Point(destObj.x - arrowX, destObj.y + arrowY);
			p2 = new Point(destObj.x - arrowX, destObj.y - arrowY);
		}
		else {
			p1 = new Point(destObj.x + arrowX, destObj.y + arrowY);
			p2 = new Point(destObj.x + arrowX, destObj.y - arrowY);
		}

		if (arrowKind == SYNC) {
			g2.drawLine(p1.x, p1.y, p2.x, p2.y);
			g2.drawLine(destObj.x, destObj.y, p1.x, p1.y);
			g2.drawLine(destObj.x, destObj.y, p2.x, p2.y);

			if (lineKind == DOTTED) {
				g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
			}
			g2.drawLine(srcObj.x, srcObj.y, p1.x, destObj.y);
		}
		else if (arrowKind == ASYNC) {
			g2.drawLine(destObj.x, destObj.y, p1.x, p1.y);
			g2.drawLine(destObj.x, destObj.y, p2.x, p2.y);

			if (lineKind == DOTTED) {
				g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
			}
			g2.drawLine(srcObj.x, srcObj.y, destObj.x, destObj.y);
		}
		else if (arrowKind == EDGE) {
			g2.drawLine(destObj.x, destObj.y, p2.x, p2.y);

			if (lineKind == DOTTED) {
				g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
			}
			g2.drawLine(srcObj.x, srcObj.y, destObj.x, destObj.y);
		}
		else if (arrowKind == FILLED) {
			Polygon p = new Polygon();
			p.addPoint(p1.x, p1.y);
			p.addPoint(p2.x, p2.y);
			p.addPoint(destObj.x, destObj.y);
			g2.fillPolygon(p);

			if (lineKind == DOTTED) {
				g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
			}
			g2.drawLine(srcObj.x, srcObj.y, p1.x, destObj.y);
		}
		g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
	}

	protected int hCenterForObj(int objNum) {
		return objNum * rectWidth + (objNum - 1) * rectDistance + borderDistance - rectWidth / 2;
	}

	protected int vCenterForLevel(int level) {
		return level * levelHeight +rectToFirstLevelDistance +
				rectHeight + borderDistance + yOffsetforTitle - levelHeight / 2;
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		return new HashSet<Direction>(); // deny size changes
	}

	public void zoomValues() {

		float zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		controlFlowBoxWidth = (int) (20 * zoom);
		rectDistance = (int) (rectDistance * zoom);

		borderDistance = (int) (10 * zoom);
		levelHeight = (int) (30 * zoom);

		arrowX = (int) (5 * zoom);
		arrowY = (int) (5 * zoom);
	}

	@Override
	public boolean isDeprecated() {
		return false;
	}

	@Override
	public GridElementDeprecatedAddons getDeprecatedAddons() {
		return new GridElementDeprecatedAddons() {

			@Override
			public void doBeforeExport() {
				// Issue 159: the old all in one grid elements calculate their real size AFTER painting. although it's bad design it works for most cases, but batch-export can fail if the element width in the uxf is wrong (eg if it was created using another umlet-default-fontsize), therefore a pseudo-paint call is made to get the real size
				paintEntity(new EpsGraphics2D());
			}
		};
	}
}
