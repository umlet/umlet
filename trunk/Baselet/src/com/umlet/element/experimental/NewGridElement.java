package com.umlet.element.experimental;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.BaseDrawHandler;

public abstract class NewGridElement {

	protected final static Logger log = Logger.getLogger(Utils.getClassName());
	
	private JComponent component = new JComponent() {
		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g) {
			drawer = new BaseDrawHandler(g, handler, fgColor, bgColor, component.getSize(), isSelected);
			paintElement();
		}
		
	};
	
	protected BaseDrawHandler drawer;
	private DiagramHandler handler;
	
	protected Color fgColor = Color.black;
	protected Color bgColor = Color.white;
	protected boolean isSelected = false;

	private String panel_attributes;
	private String additional_attributes;
	
	
	protected abstract void paintElement();

	public void setBounds(int x, int y, int w, int h) {
		component.setBounds(x, y, w, h);
	}

	public void init(Rectangle bounds, String panel_attributes, String additional_attributes, DiagramHandler handler) {
		component.setBounds(bounds);
		this.panel_attributes = panel_attributes;
		this.additional_attributes = additional_attributes;
		this.handler = handler;
	}

	public Component getComponent() {
		return component;
	}
}
