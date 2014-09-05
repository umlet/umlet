package com.baselet.gui.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Constants.SystemInfo;
import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.SelectorFrame;
import com.baselet.diagram.draw.geom.Point;

public class DiagramListener extends UniversalListener implements MouseWheelListener {

	private static final Logger log = Logger.getLogger(DiagramListener.class);

	public DiagramListener(DiagramHandler handler) {
		super(handler);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		super.mousePressed(me);

		// If some elements are selected, and the selector key (ctrl or meta) is still hold, don't deselect all elements if the drawpanel was clicked
		if (!selector.getSelectedElements().isEmpty() && (me.getModifiers() & SystemInfo.META_KEY.getMask()) != 0) {
			return;
		}

		// deselect elements of all drawpanels
		for (DiagramHandler h : Main.getInstance().getDiagramsAndPalettes()) {
			h.getDrawPanel().getSelector().deselectAllWithoutUpdatePropertyPanel();
		}
		selector.updateSelectorInformation(); // after everything is deselected updateSelectorInformation (to update property panel content)

		if ((me.getModifiers() & SystemInfo.META_KEY.getMask()) != 0) {
			SelectorFrame selframe = selector.getSelectorFrame();
			selframe.setLocation(getOffset(me).getX(), getOffset(me).getY());
			selframe.setSize(1, 1);
			((JComponent) me.getComponent()).add(selframe, 0);
			selector.setSelectorFrameActive(true);
		}
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		super.mouseMoved(me);
		Main.getInstance().getGUI().setCursor(Constants.DEFAULT_CURSOR);
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		super.mouseDragged(me);
		log.debug("mouseDragged!!");
		dragDiagram();
	}

	@Override
	protected Point getOffset(MouseEvent me) {
		return new Point(me.getX(), me.getY());
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// Only if Ctrl is pressed while scrolling, we zoom in and out
		if ((e.getModifiersEx() & SystemInfo.META_KEY.getMaskDown()) == SystemInfo.META_KEY.getMaskDown()) {
			int actualZoom = Main.getInstance().getDiagramHandler().getGridSize();
			// e.getWheelRotation is -1 if scrolling up and +1 if scrolling down therefore we subtract it
			Main.getInstance().getDiagramHandler().setGridAndZoom(actualZoom - e.getWheelRotation());
		}
		else { // otherwise scroll the diagram
			Main.getInstance().getDiagramHandler().getDrawPanel().scroll(e.getWheelRotation());
		}
	}
}
