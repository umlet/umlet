package com.baselet.gui.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.Main;
import com.baselet.control.basics.Converter;
import com.baselet.control.basics.geom.Point;
import com.baselet.control.constants.SystemInfo;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.SelectorFrame;
import com.baselet.element.interfaces.CursorOwn;
import com.baselet.gui.CurrentGui;

public class DiagramListener extends UniversalListener implements MouseWheelListener {

	private static final Logger log = LoggerFactory.getLogger(DiagramListener.class);

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
		selector.deselectAll(); // this call is only necessary in eclipse plugin - TODO refactor and clear issue why this is necessary there
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
		CurrentGui.getInstance().getGui().setCursor(Converter.convert(CursorOwn.DEFAULT));
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
			int actualZoom = CurrentDiagram.getInstance().getDiagramHandler().getGridSize();
			// e.getWheelRotation is -1 if scrolling up and +1 if scrolling down therefore we subtract it
			CurrentDiagram.getInstance().getDiagramHandler().setGridAndZoom(actualZoom - e.getWheelRotation());
		}
		else { // otherwise scroll the diagram
			CurrentDiagram.getInstance().getDiagramHandler().getDrawPanel().scroll(e.getWheelRotation());
		}
	}
}
