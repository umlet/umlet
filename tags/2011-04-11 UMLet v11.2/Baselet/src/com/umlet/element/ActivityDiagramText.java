package com.umlet.element;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baselet.control.Utils;
import com.baselet.element.StickingPolygon;
import com.umlet.element.activity.AEnd;
import com.umlet.element.activity.Activity;
import com.umlet.element.activity.Condition;
import com.umlet.element.activity.Const;
import com.umlet.element.activity.Container;
import com.umlet.element.activity.Direction;
import com.umlet.element.activity.Element;
import com.umlet.element.activity.End;
import com.umlet.element.activity.EndIf;
import com.umlet.element.activity.EventRaise;
import com.umlet.element.activity.EventRecieve;
import com.umlet.element.activity.Fork;
import com.umlet.element.activity.GoTo;
import com.umlet.element.activity.If;
import com.umlet.element.activity.LineSpacer;
import com.umlet.element.activity.PartActivity;
import com.umlet.element.activity.Row;
import com.umlet.element.activity.Start;
import com.umlet.element.activity.StartElement;
import com.umlet.element.activity.StopElement;
import com.umlet.element.activity.Sync;


@SuppressWarnings("serial")
public class ActivityDiagramText extends com.baselet.element.GridElement {

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

	private final String normalchars = "[^\\~\\>]";// "[ \\w\\\\\\(\\)]";
	private final String conditionChars = "[^\\]]";
	private final String title_pattern = "title\\:(" + normalchars + "+)";
	private final String line_pattern = "(\\t*)" + // tabs 1
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

	public ActivityDiagramText() {

	}

	private void init(Graphics2D graphics) {

		zoom = getHandler().getZoomFactor();

		this.graphics = graphics;
		this.rows = new ArrayList<Row>();
		this.gotos = new ArrayList<GoTo>();
		this.containers = new ArrayList<Container>();
		this.elements = new HashMap<String, Element>();
		this.rows.add(new Row());
		this.root_container = new Container(getHandler(), graphics, null, rows, 0);
		this.current_container = this.root_container;
		this.title = null;
		this.goto_seperation_left = (int) (5 * zoom);
		this.goto_seperation_right = (int) (5 * zoom);

		// Some unimportant initialization stuff; setting color, font
		// quality, etc. You should not have to change this.
		this.graphics = graphics;
		this.graphics.setFont(this.getHandler().getFontHandler().getFont());
		this.graphics.setColor(fgColor);
	}

	private String preparse(String line) {
		String parsed_line = "";
		Pattern p_empty = Pattern.compile("\\s*");
		Pattern p_title = Pattern.compile(this.title_pattern);
		if (!p_empty.matcher(line).matches()) {
			Pattern p = Pattern.compile(this.line_pattern);
			Matcher m_title = p_title.matcher(line);
			if (m_title.matches()) {
				parsed_line = null;
				this.title = m_title.group(1);
			}
			else if (p.matcher(line).matches()) parsed_line = line;
			else parsed_line = null;
		}
		return parsed_line;
	}

	public int getGotoPosition(Direction dir) {
		if (Direction.LEFT.equals(dir)) {
			if (this.goto_seperation_left + (Const.GOTO_SEP * zoom) < (Const.DIAGRAM_PAD * zoom)) this.goto_seperation_left += (Const.GOTO_SEP * zoom);
			return (int) ((Const.DIAGRAM_PAD * zoom) - this.goto_seperation_left);
		}
		else {
			if (this.goto_seperation_right + (Const.GOTO_SEP * zoom) < (Const.DIAGRAM_PAD * zoom)) this.goto_seperation_right += (Const.GOTO_SEP * zoom);
			return (int) (this.root_container.getWidth() + (Const.DIAGRAM_PAD * zoom) +
					this.goto_seperation_right);
		}
	}

	private Vector<String> preparse(Vector<String> lines) {

		if (lines.isEmpty()) return lines;

		Vector<String> parsed_lines = new Vector<String>();
		Iterator<String> it = lines.iterator();
		String current_line = this.preparse(it.next());
		if (current_line == null) current_line = "";
		while (current_line.equals("") && it.hasNext()) {
			current_line = this.preparse(it.next());
			if (current_line == null) current_line = "";
		}
		String previous_line = current_line;
		int current_depth = 0;
		int last_depth = 0;
		while (it.hasNext()) {
			current_line = this.preparse(it.next());
			if (current_line != null) {
				if (!current_line.equals("")) {
					for (current_depth = 0; current_line.charAt(current_depth) == '\t'; current_depth++);
					if (!previous_line.equals("") || (current_depth == last_depth)) parsed_lines.add(previous_line);

					previous_line = current_line;
					last_depth = current_depth;
				}
				else if (!previous_line.equals("")) {
					parsed_lines.add(previous_line);
					previous_line = current_line;
				}
			}
		}

		if (!previous_line.equals("")) parsed_lines.add(previous_line);

		return parsed_lines;
	}

	private void addElement(Element e) {
		this.current_container.addElement(e);
		this.elements.put(e.getId(), e);
	}

	@Override
	public void paintEntity(Graphics g) {
		this.init((Graphics2D) g);

		Vector<String> lines = Utils.decomposeStringsWithEmptyLines(this.getPanelAttributes());
		lines = this.preparse(lines);

		if (lines.size() == 0) return;

		Const.AutoInsertIF = true;
		while ((lines.size() > 0) && lines.elementAt(0).startsWith("var:")) {
			if (lines.elementAt(0).equals("var:noautoif")) Const.AutoInsertIF = false;
			lines.remove(0);
		}

		int current_depth = 0;
		Pattern p = Pattern.compile(line_pattern);

		StartElement start_element = null;
		Element current_element = null;
		this.containers.add(this.root_container);
		for (Iterator<String> it = lines.iterator(); it.hasNext();) {
			String line = it.next();
			Matcher m = p.matcher(line);
			Container closed_container = null;

			if (m.matches()) {

				Pattern p_empty = Pattern.compile("\\s*");
				Matcher m_empty = p_empty.matcher(line);
				/* NEW COLUMN IN CURRENT LAYER */
				if (m_empty.matches()) {
					/* start element was no start element (example: IF element without following container) */
					if (start_element != null) {
						this.addElement(start_element);
						start_element = null;
					}

					if (!this.current_container.isRoot()) this.current_container.addColumn();
					// empty line - no need to proceed with other stuff
					continue;
				}

				/* DEPTH */
				if (m.group(1) != null) {
					/* NEW LAYER */
					if (m.group(1).length() > current_depth) {
						for (; current_depth < m.group(1).length(); current_depth++) {
							this.current_container = this.current_container.addNewContainer();
							this.containers.add(this.current_container);
							if (start_element != null) {
								this.current_container.setStartElement(start_element);
								this.elements.put(start_element.getId(), start_element);
								start_element = null;
							}
						}
					}
					else {
						/* start element was no start element (example: IF element without following container) */
						if (start_element != null) {
							this.addElement(start_element);
							start_element = null;
						}

						/* CLOSE LAYER(s) */
						if (m.group(1).length() < current_depth) {
							for (; current_depth > m.group(1).length(); current_depth--) {
								closed_container = this.current_container;
								if (!this.current_container.isRoot()) this.current_container = this.current_container.close();
							}

						}
					}
				}

				Element e = null;

				/* BEDINGUNG */
				if (m.group(2) != null) {
					String input = "";
					if (m.group(3) != null) input = m.group(3);
					e = new Condition(getHandler(), input, this.graphics);
					current_element = e;
				}

				if (e != null) this.addElement(e);

				e = null;

				if (m.group(4) != null) {
					String id = m.group(22);

					/* START */
					if (m.group(6) != null) {
						e = new Start(getHandler(), graphics);
					}
					/* END */
					else if (m.group(7) != null) {
						if (m.group(7).equals("AEnd")) e = new AEnd(getHandler(), graphics, id);
						else e = new End(getHandler(), graphics, id);
					}
					/* LINESPACER */
					else if (m.group(8) != null) {
						e = new LineSpacer(getHandler(), graphics);
					}
					/* IF/FORK */
					else if (m.group(9) != null) {
						// these elements are processed as soon as a the
						// new container is opened (or as single elements
						// if none is openend
						if (m.group(9).equals("Fork")) start_element = new Fork(getHandler(), graphics, id);
						else start_element = new If(getHandler(), graphics, id);
						current_element = start_element;
					}
					/* ENDIF/SYNC */
					else if (m.group(10) != null) {
						// set as stop element if a container has been closed
						StopElement se;
						if (m.group(10).equals("Sync")) se = new Sync(getHandler(), graphics, id);
						else se = new EndIf(getHandler(), graphics, id);

						if (closed_container != null) {
							closed_container.setStopElement(se);
							this.elements.put(se.getId(), se);
						}
						else e = se;
						current_element = se;
					}
					/* WHILE */
					else if (m.group(11) != null) {
						this.current_container = this.current_container.addNewWhile(m.group(13));
						current_depth++;
					}
					/* GET EVENT */
					else if (m.group(15) != null) e = new EventRecieve(getHandler(), graphics, m.group(15), id);
					/* RAISE EVENT */
					else if (m.group(17) != null) e = new EventRaise(getHandler(), graphics, m.group(17), id);
					/* PARTACTIVITY */
					else if (m.group(19) != null) e = new PartActivity(getHandler(), m.group(19), graphics, id);
					/* ACTIVITY */
					else if (m.group(20) != null) e = new Activity(getHandler(), m.group(20), graphics, id);
				}

				if (e != null) {
					current_element = e;
					this.addElement(e);
				}

				/* GOTO */
				if (m.group(23) != null) {
					if ((m.group(24) != null) && (current_element != null)) {
						String connect_to = m.group(24);
						this.gotos.add(new GoTo(graphics, current_element, connect_to));
						current_element.setTerminated();
					}
				}
			}
		}
		// if a Startelement was the last element
		if (start_element != null) this.addElement(start_element);

		// close opened containers
		while (!this.current_container.isRoot()) {
			this.current_container = this.current_container.close();
		}

		// remove empty columns of containers (maybe there if only a goto element was there)
		for (Container c : this.containers)
			c.removeEmptyColumns();

		// PROCESS GOTO ELEMENTS
		ArrayList<GoTo> valid_gotos = new ArrayList<GoTo>();
		for (Iterator<GoTo> it = this.gotos.iterator(); it.hasNext();) {
			GoTo go = it.next();
			Element from = go.getFromElement();
			go.setToElement(this.elements.get(go.getToElementId()));
			Element to = go.getToElement();
			if ((from != null) && (to != null)) {
				valid_gotos.add(go);
				boolean fromleft = from.getRow().isLeft(from);
				boolean fromright = from.getRow().isRight(from);
				boolean toleft = to.getRow().isLeft(to);
				boolean toright = to.getRow().isRight(to);
				if (fromleft) {
					go.setDirection(Direction.LEFT);
					if (!toleft) this.rows = to.getRow().makeExclusiveLeft(to, this.rows);
				}
				else if (toleft) {
					go.setDirection(Direction.LEFT);
					this.rows = from.getRow().makeExclusiveLeft(from, this.rows);
				}
				else if (fromright) {
					go.setDirection(Direction.RIGHT);
					if (!toright) this.rows = to.getRow().makeExclusiveRight(to, this.rows);
				}
				else if (toright) {
					go.setDirection(Direction.RIGHT);
					this.rows = from.getRow().makeExclusiveRight(from, this.rows);
				}
				else {
					go.setDirection(Direction.LEFT);
					this.rows = from.getRow().makeExclusiveLeft(from, this.rows);
					this.rows = to.getRow().makeExclusiveLeft(to, this.rows);
				}
			}
		}

		// draw title
		int offset = 0;
		int width = (int) (this.root_container.getWidth() + (Const.DIAGRAM_PAD * zoom) * 2);
		int height = 0;
		if (this.title != null) {
			offset += (int) (25 * zoom);
			height += (int) (25 * zoom);

			if ((title != null) && (title.length() > 0)) {
				this.getHandler().getFontHandler().writeText(this.graphics, title, (int) (10 * zoom), (int) this.getHandler().getFontHandler().getFontSize() + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts(), false);
				int titlewidth = this.getHandler().getFontHandler().getTextWidth(title);
				int ty = (int) this.getHandler().getFontHandler().getFontSize() + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() + (int) (8 * zoom);
				this.graphics.drawLine(0, ty, titlewidth + (int) (10 * zoom), ty);
				this.graphics.drawLine(titlewidth + (int) (10 * zoom), ty, titlewidth + ty + (int) (10 * zoom), 0);
			}
		}

		/* COMPUTE POSITIONS */
		for (Row r : this.rows)
			offset = r.setElementYPosition(offset);

		this.root_container.setX((int) (this.root_container.getLeftWidth() + (Const.DIAGRAM_PAD * zoom)));

		if (Const.DEBUG) this.root_container.printData("");

		for (Row r : this.rows)
			height += r.getHeight();

		if (width < (Const.MIN_WIDTH * zoom)) width = (int) (Const.MIN_WIDTH * zoom);
		if (height < (Const.MIN_HEIGHT * zoom)) height = (int) (Const.MIN_HEIGHT * zoom);

		// draw diagram
		this.setSize(width, height);
		this.graphics.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		this.root_container.paint();

		// draw goto elements
		for (Iterator<GoTo> it = valid_gotos.iterator(); it.hasNext();)
			it.next().paint(this);
	}

	@Override
	public int getPossibleResizeDirections() {
		return 0;
	} // deny size changes: dynamic resize is done by the diagram itself

	public StickingPolygon generateStickingBorder() {
		return null;
	}
}
