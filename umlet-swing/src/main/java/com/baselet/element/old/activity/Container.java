package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import com.baselet.control.enums.Direction;
import com.baselet.diagram.DiagramHandler;

public class Container extends Element {

	private final ArrayList<Column> columns;
	private StartElement start;
	private StopElement stop;
	private final Container parent;
	private final ArrayList<Row> rows;
	private int max_row;
	private int current_row;
	private int init_row;
	private final AtomicBoolean autoInsertIF;

	public Container(AtomicBoolean autoInsertIF, DiagramHandler handler, Graphics2D g, Container parent, ArrayList<Row> rows, int row) {
		super(handler, g, 0, null);
		columns = new ArrayList<Column>();
		columns.add(new Column(g));
		this.rows = rows;
		init_row = row;
		max_row = row;
		current_row = row;
		this.parent = parent;

		this.autoInsertIF = autoInsertIF;

		if (autoInsertIF.get() && this.parent != null) {
			setStartElement(new If(handler, g, null));
		}
		else if (this.parent != null) {
			setStartElement(new StartElement(handler, g, 0, null));
		}
	}

	public Container addNewContainer() {
		Container c = new Container(autoInsertIF, getHandler(), getGraphics(), this, rows, current_row);
		columns.get(columns.size() - 1).addElement(c);
		return c;
	}

	public Container addNewWhile(String condition) {
		Container c = new While(autoInsertIF, getHandler(), getGraphics(), this, rows, current_row, condition);
		columns.get(columns.size() - 1).addElement(c);
		return c;
	}

	protected ArrayList<Column> getColumns() {
		return columns;
	}

	protected Column getLastColumn() {
		return columns.get(columns.size() - 1);
	}

	public boolean isRoot() {
		if (parent == null) {
			return true;
		}
		return false;
	}

	public Container close() {
		if (autoInsertIF.get() && parent != null) {
			setStopElement(new EndIf(getHandler(), getGraphics(), null));
		}
		else if (parent != null) {
			setStopElement(new StopElement(getHandler(), getGraphics(), 0, null));
		}
		return parent;
	}

	private Row getNextRow() {
		while (rows.size() <= current_row) {
			rows.add(new Row());
		}
		return rows.get(current_row);
	}

	private Row getFirstRow() {
		int init_row = this.init_row;
		if (start != null) {
			init_row--;
		}
		while (rows.size() <= init_row) {
			rows.add(new Row());
		}
		return rows.get(init_row);
	}

	private Row getLastRow() {
		int max_row = this.max_row;
		if (stop != null) {
			max_row--;
		}
		while (rows.size() <= max_row) {
			rows.add(new Row());
		}
		return rows.get(max_row);
	}

	private void inc_row() {
		current_row++;
		if (current_row > max_row) {
			max_row++;
			if (parent != null) {
				parent.inc_row();
			}
		}
	}

	public void setStartElement(StartElement e) {
		getFirstRow().exchangeElementOrInsert(start, e);
		if (start == null) {
			current_row++;
			init_row++;
			max_row++;
			if (parent != null) {
				parent.inc_row();
			}
		}
		start = e;
	}

	public void setStopElement(StopElement e) {
		getLastRow().exchangeElementOrInsert(stop, e);
		if (stop == null) {
			max_row++;
			if (parent != null) {
				parent.inc_row();
			}
		}
		stop = e;
	}

	public void addColumn() {
		columns.add(new Column(getGraphics()));
		current_row = init_row;
	}

	public void addElement(Element e) {
		columns.get(columns.size() - 1).addElement(e);
		getNextRow().addElement(e);
		inc_row();
	}

	@Override
	public boolean arrowIn() {
		if (start == null) {
			return false;
		}
		else {
			return start.arrowIn();
		}
	}

	@Override
	public boolean connectIn() {
		for (Column c : columns) {
			if (c.getFirstElement().connectIn()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean connectOut_overrideable() {
		for (Column c : columns) {
			if (c.getLastElement().connectOut()) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected Point getConnect(Direction dir) {
		if (dir.equals(Direction.UP)) {
			if (start != null) {
				return start.getConnect(dir);
			}
		}
		if (dir.equals(Direction.DOWN)) {
			if (stop != null) {
				return stop.getConnect(dir);
			}
		}
		return super.getConnect(dir);
	}

	@Override
	protected int getHeight() {
		int height = 0;
		for (Column c : columns) {
			int h = c.getHeight();
			if (h > height) {
				height = h;
			}
		}
		if (start != null) {
			height += start.getHeight();
		}
		if (stop != null) {
			height += stop.getHeight();
		}
		return height;
	}

	@Override
	public int getLeftWidth() {
		int width = 0, i = 0;
		for (i = 0; i < columns.size() / 2; i++) {
			width += columns.get(i).getWidth();
		}
		if (columns.size() % 2 == 1) {
			width += columns.get(i).getLeftWidth();
		}

		width += (columns.size() - 1) / 2.0 * (Const.COLUMN_PAD * getZoom());

		if (start != null) {
			if (start.getLeftWidth() > width) {
				width = start.getLeftWidth();
			}
		}
		if (stop != null) {
			if (stop.getLeftWidth() > width) {
				width = stop.getLeftWidth();
			}
		}
		return width;

	}

	@Override
	protected int getRightWidth() {
		int width = 0, i = columns.size() / 2;
		if (columns.size() % 2 == 1) {
			width += columns.get(i).getRightWidth();
			i++;
		}
		for (; i < columns.size(); i++) {
			width += columns.get(i).getWidth();
		}

		width += (columns.size() - 1) / 2.0 * (Const.COLUMN_PAD * getZoom());

		if (start != null) {
			if (start.getRightWidth() > width) {
				width = start.getRightWidth();
			}
		}
		if (stop != null) {
			if (stop.getRightWidth() > width) {
				width = stop.getRightWidth();
			}
		}
		return width;
	}

	@Override
	public int getWidth() {
		int width = 0, i = 0;
		for (i = 0; i < columns.size(); i++) {
			width += columns.get(i).getWidth();
		}

		width += (columns.size() - 1) * (Const.COLUMN_PAD * getZoom());

		if (start != null) {
			if (start.getWidth() > width) {
				width = start.getWidth();
			}
		}
		if (stop != null) {
			if (stop.getWidth() > width) {
				width = stop.getWidth();
			}
		}
		return width;
	}

	@Override
	public void setX(int x) {

		if (start != null) {
			start.setX(x);
		}
		if (stop != null) {
			stop.setX(x);
		}

		if (columns.size() == 1) {
			columns.get(0).setX(x);
		}
		else {
			x -= getLeftWidth();
			int i = 1;
			for (Column c : columns) {
				x += c.getLeftWidth();
				c.setX(x);
				x += c.getRightWidth();
				if (i < columns.size()) {
					x += Const.COLUMN_PAD * getZoom();
				}
				i++;
			}
		}
	}

	protected StartElement getStartElement() {
		return start;
	}

	protected StopElement getStopElement() {
		return stop;
	}

	public void removeEmptyColumns() {
		// remove empty columns first (may be possible because of goto element
		for (int i = 0; i < columns.size();) {
			if (columns.get(i).isEmpty()) {
				columns.remove(i);
			}
			else {
				i++;
			}
		}
	}

	@Override
	public void paint() {
		boolean paintstart = connectIn();
		boolean paintstop = connectOut();
		if (start == null) {
			paintstart = false;
		}

		if (stop == null) {
			paintstop = false;
		}

		if (paintstart) {
			start.paint();
		}
		if (paintstop) {
			stop.paint();
		}

		for (Column c : columns) {
			if (paintstart) {
				start.connectTo(c.getFirstElement());
			}
			c.paint();
			if (paintstop) {
				stop.connectTo(c.getLastElement());
			}
		}
	}

	@Override
	public void printData(String prefix) {
		prefix += "    ";
		for (Column c : columns) {
			c.printData(prefix + "\n");
		}
	}

}
