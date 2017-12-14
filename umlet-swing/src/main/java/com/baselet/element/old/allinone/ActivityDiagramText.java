package com.baselet.element.old.allinone;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sourceforge.jlibeps.epsgraphics.EpsGraphics2D;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.Direction;
import com.baselet.control.util.Utils;
import com.baselet.element.interfaces.GridElementDeprecatedAddons;
import com.baselet.element.old.OldGridElement;
import com.baselet.element.old.activity.AEnd;
import com.baselet.element.old.activity.Activity;
import com.baselet.element.old.activity.Condition;
import com.baselet.element.old.activity.Const;
import com.baselet.element.old.activity.Container;
import com.baselet.element.old.activity.Element;
import com.baselet.element.old.activity.End;
import com.baselet.element.old.activity.EndIf;
import com.baselet.element.old.activity.EventRaise;
import com.baselet.element.old.activity.EventRecieve;
import com.baselet.element.old.activity.Fork;
import com.baselet.element.old.activity.GoTo;
import com.baselet.element.old.activity.If;
import com.baselet.element.old.activity.LineSpacer;
import com.baselet.element.old.activity.PartActivity;
import com.baselet.element.old.activity.Row;
import com.baselet.element.old.activity.Start;
import com.baselet.element.old.activity.StartElement;
import com.baselet.element.old.activity.StopElement;
import com.baselet.element.old.activity.Sync;

@SuppressWarnings("serial")
public class ActivityDiagramText extends OldGridElement {

	private final AtomicBoolean autoInsertIF = new AtomicBoolean();

	private ArrayList<Row> rows;
	private ArrayList<Container> containers;
	private Container root_container;
	private Container current_container;
	private HashMap<String, Element> elements;
	private String title;
	private Graphics2D graphics;
	private ArrayList<GoTo> gotos;
	private int goto_seperation_left;
	private int goto_seperation_right;

	private float zoom;

	private static final String normalchars = "[^\\~\\>]";// "[ \\w\\\\\\(\\)]";
	private static final String conditionChars = "[^\\]]";
	private static final String title_pattern = "title\\:(" + normalchars + "+)";
	private static final String line_pattern = "(\\t*)" + // tabs 1
												"(\\[([ " + conditionChars + "]+)\\])?" + // conditions 2..3
												"(" + // 4
												"(" + // 5
												"(Start)" + // start 6
												"|(End|AEnd)" + // end7
												"|(\\|)" + // linespacer 8
												"|(If|Fork)|(EndIf|Sync)" + // if blocks 9/10
												"|(While(\\[(" + normalchars + "*)\\])?)" + // while 11..13
												"|(\\>(" + normalchars + "+))" + // recieve event 14..15
												"|((" + normalchars + "+)\\>)" + // raise event 16..17
												"|((" + normalchars + "+)\\.\\.)" + // partactivity 18..19
												"|(" + normalchars + "+)" + // activity 20
												")" +
												"(\\~(" + normalchars + "+)?)?" + // ids 21..22
												")?" +
												"\\s*" +
												"(\\-\\>(" + normalchars + "+))?" + // goto 23..24
												"\\s*";

	private void init(Graphics2D graphics) {

		zoom = HandlerElementMap.getHandlerForElement(this).getZoomFactor();

		this.graphics = graphics;
		rows = new ArrayList<Row>();
		gotos = new ArrayList<GoTo>();
		containers = new ArrayList<Container>();
		elements = new HashMap<String, Element>();
		rows.add(new Row());
		root_container = new Container(autoInsertIF, HandlerElementMap.getHandlerForElement(this), graphics, null, rows, 0);
		current_container = root_container;
		title = null;
		goto_seperation_left = (int) (5 * zoom);
		goto_seperation_right = (int) (5 * zoom);

		// Some unimportant initialization stuff; setting color, font
		// quality, etc. You should not have to change this.
		this.graphics = graphics;
		this.graphics.setFont(HandlerElementMap.getHandlerForElement(this).getFontHandler().getFont());
		this.graphics.setColor(fgColor);
	}

	private String preparse(String line) {
		String parsed_line = "";
		Pattern p_empty = Pattern.compile("\\s*");
		Pattern p_title = Pattern.compile(title_pattern);
		if (!p_empty.matcher(line).matches()) {
			Pattern p = Pattern.compile(line_pattern);
			Matcher m_title = p_title.matcher(line);
			if (m_title.matches()) {
				parsed_line = null;
				title = m_title.group(1);
			}
			else if (p.matcher(line).matches()) {
				parsed_line = line;
			}
			else {
				parsed_line = null;
			}
		}
		return parsed_line;
	}

	public int getGotoPosition(Direction dir) {
		if (Direction.LEFT.equals(dir)) {
			if (goto_seperation_left + Const.GOTO_SEP * zoom < Const.DIAGRAM_PAD * zoom) {
				goto_seperation_left += Const.GOTO_SEP * zoom;
			}
			return (int) (Const.DIAGRAM_PAD * zoom - goto_seperation_left);
		}
		else {
			if (goto_seperation_right + Const.GOTO_SEP * zoom < Const.DIAGRAM_PAD * zoom) {
				goto_seperation_right += Const.GOTO_SEP * zoom;
			}
			return (int) (root_container.getWidth() + Const.DIAGRAM_PAD * zoom + goto_seperation_right);
		}
	}

	private Vector<String> preparse(Vector<String> lines) {

		if (lines.isEmpty()) {
			return lines;
		}

		Vector<String> parsed_lines = new Vector<String>();
		Iterator<String> it = lines.iterator();
		String current_line = this.preparse(it.next());
		if (current_line == null) {
			current_line = "";
		}
		while (current_line.equals("") && it.hasNext()) {
			current_line = this.preparse(it.next());
			if (current_line == null) {
				current_line = "";
			}
		}
		String previous_line = current_line;
		int current_depth = 0;
		int last_depth = 0;
		while (it.hasNext()) {
			current_line = this.preparse(it.next());
			if (current_line != null) {
				if (!current_line.equals("")) {
					for (current_depth = 0; current_line.charAt(current_depth) == '\t'; current_depth++) {/* do nothing except increasing current depth */}
					if (!previous_line.equals("") || current_depth == last_depth) {
						parsed_lines.add(previous_line);
					}

					previous_line = current_line;
					last_depth = current_depth;
				}
				else if (!previous_line.equals("")) {
					parsed_lines.add(previous_line);
					previous_line = current_line;
				}
			}
		}

		if (!previous_line.equals("")) {
			parsed_lines.add(previous_line);
		}

		return parsed_lines;
	}

	private void addElement(Element e) {
		current_container.addElement(e);
		elements.put(e.getId(), e);
	}

	@Override
	public void paintEntity(Graphics g) {
		init((Graphics2D) g);

		Vector<String> lines = Utils.decomposeStringsWithEmptyLines(getPanelAttributes());
		lines = this.preparse(lines);

		if (lines.size() == 0) {
			return;
		}

		autoInsertIF.set(true);
		while (lines.size() > 0 && lines.elementAt(0).startsWith("var:")) {
			if (lines.elementAt(0).equals("var:noautoif")) {
				autoInsertIF.set(false);
			}
			lines.remove(0);
		}

		int current_depth = 0;
		Pattern p = Pattern.compile(line_pattern);

		StartElement start_element = null;
		Element current_element = null;
		containers.add(root_container);
		for (String line : lines) {
			Matcher m = p.matcher(line);
			Container closed_container = null;

			if (m.matches()) {

				Pattern p_empty = Pattern.compile("\\s*");
				Matcher m_empty = p_empty.matcher(line);
				/* NEW COLUMN IN CURRENT LAYER */
				if (m_empty.matches()) {
					/* start element was no start element (example: IF element without following container) */
					if (start_element != null) {
						addElement(start_element);
						start_element = null;
					}

					if (!current_container.isRoot()) {
						current_container.addColumn();
					}
					// empty line - no need to proceed with other stuff
					continue;
				}

				/* DEPTH */
				if (m.group(1) != null) {
					/* NEW LAYER */
					if (m.group(1).length() > current_depth) {
						for (; current_depth < m.group(1).length(); current_depth++) {
							current_container = current_container.addNewContainer();
							containers.add(current_container);
							if (start_element != null) {
								current_container.setStartElement(start_element);
								elements.put(start_element.getId(), start_element);
								start_element = null;
							}
						}
					}
					else {
						/* start element was no start element (example: IF element without following container) */
						if (start_element != null) {
							addElement(start_element);
							start_element = null;
						}

						/* CLOSE LAYER(s) */
						if (m.group(1).length() < current_depth) {
							for (; current_depth > m.group(1).length(); current_depth--) {
								closed_container = current_container;
								if (!current_container.isRoot()) {
									current_container = current_container.close();
								}
							}

						}
					}
				}

				Element e = null;

				/* BEDINGUNG */
				if (m.group(2) != null) {
					String input = "";
					if (m.group(3) != null) {
						input = m.group(3);
					}
					e = new Condition(HandlerElementMap.getHandlerForElement(this), input, graphics);
					current_element = e;
				}

				if (e != null) {
					addElement(e);
				}

				e = null;

				if (m.group(4) != null) {
					String id = m.group(22);

					/* START */
					if (m.group(6) != null) {
						e = new Start(HandlerElementMap.getHandlerForElement(this), graphics);
					}
					/* END */
					else if (m.group(7) != null) {
						if (m.group(7).equals("AEnd")) {
							e = new AEnd(HandlerElementMap.getHandlerForElement(this), graphics, id);
						}
						else {
							e = new End(HandlerElementMap.getHandlerForElement(this), graphics, id);
						}
					}
					/* LINESPACER */
					else if (m.group(8) != null) {
						e = new LineSpacer(HandlerElementMap.getHandlerForElement(this), graphics);
					}
					/* IF/FORK */
					else if (m.group(9) != null) {
						// these elements are processed as soon as a the
						// new container is opened (or as single elements
						// if none is openend
						if (m.group(9).equals("Fork")) {
							start_element = new Fork(HandlerElementMap.getHandlerForElement(this), graphics, id);
						}
						else {
							start_element = new If(HandlerElementMap.getHandlerForElement(this), graphics, id);
						}
						current_element = start_element;
					}
					/* ENDIF/SYNC */
					else if (m.group(10) != null) {
						// set as stop element if a container has been closed
						StopElement se;
						if (m.group(10).equals("Sync")) {
							se = new Sync(HandlerElementMap.getHandlerForElement(this), graphics, id);
						}
						else {
							se = new EndIf(HandlerElementMap.getHandlerForElement(this), graphics, id);
						}

						if (closed_container != null) {
							closed_container.setStopElement(se);
							elements.put(se.getId(), se);
						}
						else {
							e = se;
						}
						current_element = se;
					}
					/* WHILE */
					else if (m.group(11) != null) {
						current_container = current_container.addNewWhile(m.group(13));
						current_depth++;
					}
					/* GET EVENT */
					else if (m.group(15) != null) {
						e = new EventRecieve(HandlerElementMap.getHandlerForElement(this), graphics, m.group(15), id);
					}
					else if (m.group(17) != null) {
						e = new EventRaise(HandlerElementMap.getHandlerForElement(this), graphics, m.group(17), id);
					}
					else if (m.group(19) != null) {
						e = new PartActivity(HandlerElementMap.getHandlerForElement(this), m.group(19), graphics, id);
					}
					else if (m.group(20) != null) {
						e = new Activity(HandlerElementMap.getHandlerForElement(this), m.group(20), graphics, id);
					}
				}

				if (e != null) {
					current_element = e;
					addElement(e);
				}

				/* GOTO */
				if (m.group(23) != null) {
					if (m.group(24) != null && current_element != null) {
						String connect_to = m.group(24);
						gotos.add(new GoTo(graphics, current_element, connect_to));
						current_element.setTerminated();
					}
				}
			}
		}
		// if a Startelement was the last element
		if (start_element != null) {
			addElement(start_element);
		}

		// close opened containers
		while (!current_container.isRoot()) {
			current_container = current_container.close();
		}

		// remove empty columns of containers (maybe there if only a goto element was there)
		for (Container c : containers) {
			c.removeEmptyColumns();
		}

		// PROCESS GOTO ELEMENTS
		ArrayList<GoTo> valid_gotos = new ArrayList<GoTo>();
		for (GoTo go : gotos) {
			Element from = go.getFromElement();
			go.setToElement(elements.get(go.getToElementId()));
			Element to = go.getToElement();
			if (from != null && to != null) {
				valid_gotos.add(go);
				boolean fromleft = from.getRow().isLeft(from);
				boolean fromright = from.getRow().isRight(from);
				boolean toleft = to.getRow().isLeft(to);
				boolean toright = to.getRow().isRight(to);
				if (fromleft) {
					go.setDirection(Direction.LEFT);
					if (!toleft) {
						rows = to.getRow().makeExclusiveLeft(to, rows);
					}
				}
				else if (toleft) {
					go.setDirection(Direction.LEFT);
					rows = from.getRow().makeExclusiveLeft(from, rows);
				}
				else if (fromright) {
					go.setDirection(Direction.RIGHT);
					if (!toright) {
						rows = to.getRow().makeExclusiveRight(to, rows);
					}
				}
				else if (toright) {
					go.setDirection(Direction.RIGHT);
					rows = from.getRow().makeExclusiveRight(from, rows);
				}
				else {
					go.setDirection(Direction.LEFT);
					rows = from.getRow().makeExclusiveLeft(from, rows);
					rows = to.getRow().makeExclusiveLeft(to, rows);
				}
			}
		}

		// draw title
		int offset = 0;
		int width = (int) (root_container.getWidth() + Const.DIAGRAM_PAD * zoom * 2);
		int height = 0;
		if (title != null) {
			offset += (int) (25 * zoom);
			height += (int) (25 * zoom);

			if (title.length() > 0) {
				HandlerElementMap.getHandlerForElement(this).getFontHandler().writeText(graphics, title, (int) (10 * zoom), (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(), AlignHorizontal.LEFT);
				int titlewidth = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getTextWidth(title);
				int ty = (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getFontSize() + (int) HandlerElementMap.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts() + (int) (8 * zoom);
				graphics.drawLine(0, ty, titlewidth + (int) (10 * zoom), ty);
				graphics.drawLine(titlewidth + (int) (10 * zoom), ty, titlewidth + ty + (int) (10 * zoom), 0);
			}
		}

		/* COMPUTE POSITIONS */
		for (Row r : rows) {
			offset = r.setElementYPosition(offset);
		}

		root_container.setX((int) (root_container.getLeftWidth() + Const.DIAGRAM_PAD * zoom));

		if (Const.DEBUG) {
			root_container.printData("");
		}

		for (Row r : rows) {
			height += r.getHeight();
		}

		if (width < Const.MIN_WIDTH * zoom) {
			width = (int) (Const.MIN_WIDTH * zoom);
		}
		if (height < Const.MIN_HEIGHT * zoom) {
			height = (int) (Const.MIN_HEIGHT * zoom);
		}

		// draw diagram
		this.setSize(width, height);
		graphics.drawRect(0, 0, getRectangle().width - 1, getRectangle().height - 1);
		root_container.paint();

		// draw goto elements
		for (GoTo goTo : valid_gotos) {
			goTo.paint(HandlerElementMap.getHandlerForElement(this).getZoomFactor(), getGotoPosition(goTo.getDirection()));
		}
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		return new HashSet<Direction>(); // deny size changes
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
