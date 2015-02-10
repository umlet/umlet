package com.baselet.element.old.activity;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import com.baselet.diagram.DiagramHandler;

public class While extends Container {

	private WhileElement while_element;

	public While(AtomicBoolean autoInsertIF, DiagramHandler handler, Graphics2D g, Container parent, ArrayList<Row> rows, int row, String condition) {
		super(autoInsertIF, handler, g, parent, rows, row);
		if (condition != null && !condition.equals("")) {
			while_element = new Condition(handler, condition, g);
		}
		else {
			while_element = new LineSpacer(handler, g);
		}
		super.setStartElement(new If(handler, g, null));

		addElement(while_element);
		addColumn();
	}

	@Override
	public int getLeftWidth() {
		int width = getColumns().get(0).getWidth();
		if (getColumns().size() > 1) {
			width += getColumns().get(1).getLeftWidth() + Const.COLUMN_PAD * getZoom();
		}
		return width;
	}

	@Override
	protected int getRightWidth() {
		int width = 0;
		ArrayList<Column> columns = getColumns();
		if (columns.size() > 1) {
			width += columns.get(1).getRightWidth();
		}
		for (int i = 2; i < columns.size(); i++) {
			width += columns.get(i).getWidth() + Const.COLUMN_PAD * getZoom();
		}
		return width;
	}

	@Override
	public void paint() {
		getStartElement().setNotTerminated();
		getStartElement().paint();
		getStopElement().paint();

		while_element.connectTo(getStartElement(), getStopElement());
		while_element.paint();

		ArrayList<Column> columns = getColumns();
		for (int i = 1; i < columns.size(); i++) {
			Column c = columns.get(i);
			getStartElement().connectTo(c.getFirstElement());
			c.paint();
			getStopElement().connectTo(c.getLastElement());
		}
	}

	// dont allow change of start/stop elements
	@Override
	public void setStartElement(StartElement e) {

	}

	@Override
	public void setStopElement(StopElement e) {
		super.setStopElement(new EndIf(getHandler(), getGraphics(), null));
	}

}
