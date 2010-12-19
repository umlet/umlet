package com.umlet.element.base.activity;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class While extends Container {

	private WhileElement while_element;
	
	public While(Graphics2D g, Container parent, ArrayList<Row> rows, int row, String condition) {
		super(g, parent, rows, row);
		if(condition != null && !condition.equals("")) 
			this.while_element = new Condition(condition,g);
		else 
			this.while_element = new LineSpacer(g);
		super.setStartElement(new If(g,null));
		
		this.addElement(this.while_element);
		this.addColumn();
	}

	@Override
	protected int getLeftWidth() {
		int width = this.getColumns().get(0).getWidth();
		if(this.getColumns().size() > 1)
			width += this.getColumns().get(1).getLeftWidth() + Const.COLUMN_PAD;
		return width;
	}

	@Override
	protected int getRightWidth() {
		int width=0;
		ArrayList<Column> columns = this.getColumns();
		if(columns.size() > 1)
			width += columns.get(1).getRightWidth();
		for(int i=2; i < columns.size(); i++)
			width += columns.get(i).getWidth() + Const.COLUMN_PAD;
		return width;
	}

	@Override
	public void paint() {
		this.getStartElement().setNotTerminated();
		this.getStartElement().paint();
		this.getStopElement().paint();
		
		this.while_element.connectTo(this.getStartElement(), this.getStopElement());
		this.while_element.paint();
		
		ArrayList<Column> columns = this.getColumns();
		for(int i=1; i < columns.size(); i++) {
			Column c = columns.get(i);
			this.getStartElement().connectTo(c.getFirstElement());
			c.paint();
			this.getStopElement().connectTo(c.getLastElement());
		}
	}

	//dont allow change of start/stop elements
	@Override
	public void setStartElement(StartElement e) {

	}

	@Override
	public void setStopElement(StopElement e) {
		super.setStopElement(new EndIf(this.getGraphics(),null));
	}
	
	
}
