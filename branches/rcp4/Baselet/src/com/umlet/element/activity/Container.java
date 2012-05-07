package com.umlet.element.activity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import com.baselet.diagram.DiagramHandler;


public class Container extends Element {

	private ArrayList<Column> columns;
	private StartElement start;
	private StopElement stop;
	private Container parent;
	private ArrayList<Row> rows;
	private int max_row;
	private int current_row;
	private int init_row;

	public Container(DiagramHandler handler, Graphics2D g, Container parent, ArrayList<Row> rows, int row) {
		super(handler, g, 0, null);
		this.columns = new ArrayList<Column>();
		this.columns.add(new Column(g));
		this.rows = rows;
		this.init_row = row;
		this.max_row = row;
		this.current_row = row;
		this.parent = parent;

		if (Const.AutoInsertIF && (this.parent != null)) this.setStartElement(new If(handler, g, null));
		else if (this.parent != null) this.setStartElement(new StartElement(handler, g, 0, null));
	}

	public Container addNewContainer() {
		Container c = new Container(this.getHandler(), this.getGraphics(), this, this.rows, this.current_row);
		this.columns.get(this.columns.size() - 1).addElement(c);
		return c;
	}

	public Container addNewWhile(String condition) {
		Container c = new While(this.getHandler(), this.getGraphics(), this, this.rows, this.current_row, condition);
		this.columns.get(this.columns.size() - 1).addElement(c);
		return c;
	}

	protected ArrayList<Column> getColumns() {
		return this.columns;
	}

	protected Column getLastColumn() {
		return this.columns.get(this.columns.size() - 1);
	}

	public boolean isRoot() {
		if (this.parent == null) return true;
		return false;
	}

	public Container close() {
		if (Const.AutoInsertIF && (this.parent != null)) this.setStopElement(new EndIf(this.getHandler(), this.getGraphics(), null));
		else if (this.parent != null) this.setStopElement(new StopElement(this.getHandler(), this.getGraphics(), 0, null));
		return this.parent;
	}

	private Row getNextRow() {
		while (this.rows.size() <= current_row)
			this.rows.add(new Row());
		return this.rows.get(current_row);
	}

	private Row getFirstRow() {
		int init_row = this.init_row;
		if (this.start != null) init_row--;
		while (this.rows.size() <= init_row)
			this.rows.add(new Row());
		return this.rows.get(init_row);
	}

	private Row getLastRow() {
		int max_row = this.max_row;
		if (this.stop != null) max_row--;
		while (this.rows.size() <= max_row)
			this.rows.add(new Row());
		return this.rows.get(max_row);
	}

	private void inc_row() {
		this.current_row++;
		if (this.current_row > this.max_row) {
			this.max_row++;
			if (this.parent != null) this.parent.inc_row();
		}
	}

	public void setStartElement(StartElement e) {
		this.getFirstRow().exchangeElementOrInsert(this.start, e);
		if (this.start == null) {
			this.current_row++;
			this.init_row++;
			this.max_row++;
			if (this.parent != null) this.parent.inc_row();
		}
		this.start = e;
	}

	public void setStopElement(StopElement e) {
		this.getLastRow().exchangeElementOrInsert(this.stop, e);
		if (this.stop == null) {
			this.max_row++;
			if (this.parent != null) this.parent.inc_row();
		}
		this.stop = e;
	}

	public void addColumn() {
		this.columns.add(new Column(this.getGraphics()));
		this.current_row = this.init_row;
	}

	public void addElement(Element e) {
		this.columns.get(this.columns.size() - 1).addElement(e);
		this.getNextRow().addElement(e);
		this.inc_row();
	}

	@Override
	public boolean arrowIn() {
		if (start == null) return false;
		else return start.arrowIn();
	}

	@Override
	public boolean connectIn() {
		for (Column c : this.columns)
			if (c.getFirstElement().connectIn()) return true;
		return false;
	}

	@Override
	public boolean connectOut_overrideable() {
		for (Column c : this.columns)
			if (c.getLastElement().connectOut()) return true;
		return false;
	}

	@Override
	protected Point getConnect(Direction dir) {
		if (dir.equals(Direction.TOP)) {
			if (this.start != null) return this.start.getConnect(dir);
		}
		if (dir.equals(Direction.BOTTOM)) {
			if (this.stop != null) return this.stop.getConnect(dir);
		}
		return super.getConnect(dir);
	}

	@Override
	protected int getHeight() {
		int height = 0;
		for (Column c : this.columns) {
			int h = c.getHeight();
			if (h > height) height = h;
		}
		if (this.start != null) height += this.start.getHeight();
		if (this.stop != null) height += this.stop.getHeight();
		return height;
	}

	@Override
	public int getLeftWidth() {
		int width = 0, i = 0;
		for (i = 0; i < this.columns.size() / 2; i++)
			width += this.columns.get(i).getWidth();
		if (this.columns.size() % 2 == 1) width += this.columns.get(i).getLeftWidth();

		width += ((this.columns.size() - 1)) / 2.0 * (Const.COLUMN_PAD * getZoom());

		if (start != null) if (start.getLeftWidth() > width) width = start.getLeftWidth();
		if (stop != null) if (stop.getLeftWidth() > width) width = stop.getLeftWidth();
		return width;

	}

	@Override
	protected int getRightWidth() {
		int width = 0, i = this.columns.size() / 2;
		if (this.columns.size() % 2 == 1) {
			width += this.columns.get(i).getRightWidth();
			i++;
		}
		for (; i < this.columns.size(); i++)
			width += this.columns.get(i).getWidth();

		width += ((this.columns.size() - 1)) / 2.0 * (Const.COLUMN_PAD * getZoom());

		if (start != null) if (start.getRightWidth() > width) width = start.getRightWidth();
		if (stop != null) if (stop.getRightWidth() > width) width = stop.getRightWidth();
		return width;
	}

	@Override
	public int getWidth() {
		int width = 0, i = 0;
		for (i = 0; i < this.columns.size(); i++)
			width += this.columns.get(i).getWidth();

		width += (this.columns.size() - 1) * (Const.COLUMN_PAD * getZoom());

		if (start != null) if (start.getWidth() > width) width = start.getWidth();
		if (stop != null) if (stop.getWidth() > width) width = stop.getWidth();
		return width;
	}

	@Override
	public void setX(int x) {

		if (this.start != null) this.start.setX(x);
		if (this.stop != null) this.stop.setX(x);

		if (this.columns.size() == 1) this.columns.get(0).setX(x);
		else {
			x -= this.getLeftWidth();
			int i = 1;
			for (Column c : this.columns) {
				x += c.getLeftWidth();
				c.setX(x);
				x += c.getRightWidth();
				if (i < this.columns.size()) x += (Const.COLUMN_PAD * getZoom());
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
		for (int i = 0; i < this.columns.size();) {
			if (this.columns.get(i).isEmpty()) this.columns.remove(i);
			else i++;
		}
	}

	@Override
	public void paint() {
		boolean paintstart = this.connectIn();
		boolean paintstop = this.connectOut();
		if (this.start == null) paintstart = false;

		if (this.stop == null) paintstop = false;

		if (paintstart) this.start.paint();
		if (paintstop) this.stop.paint();

		for (Column c : this.columns) {
			if (paintstart) this.start.connectTo(c.getFirstElement());
			c.paint();
			if (paintstop) this.stop.connectTo(c.getLastElement());
		}
	}

	@Override
	public void printData(String prefix) {
		prefix += "    ";
		for (Column c : this.columns) {
			c.printData(prefix + "\n");
		}
	}

}
