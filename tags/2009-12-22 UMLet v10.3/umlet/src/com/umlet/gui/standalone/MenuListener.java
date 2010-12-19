package com.umlet.gui.standalone;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.command.AddGroup;
import com.umlet.control.command.ChangeBGColor;
import com.umlet.control.command.ChangeFGColor;
import com.umlet.control.command.RemoveElement;
import com.umlet.control.command.Ungroup;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.Selector;
import com.umlet.custom.CustomElement;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Group;

public class MenuListener implements ActionListener, MouseListener, MouseMotionListener {

	private static MenuListener _menulistener;

	public static MenuListener getInstance() {
		if (_menulistener == null) _menulistener = new MenuListener();
		return _menulistener;
	}

	protected MenuListener() {}

	public void actionPerformed(ActionEvent e) {
		DiagramHandler handler = Umlet.getInstance().getDiagramHandler(); // selected diagram / also palette possible
		if ((e.getSource() instanceof JCheckBoxMenuItem) && (handler != null)) {
			JCheckBoxMenuItem cbi = (JCheckBoxMenuItem) e.getSource();
			if (cbi.getActionCommand().startsWith("color_fgc_")) { // foreground
				handler.getController().executeCommand(new ChangeFGColor(cbi));// colors
			}
			else if (cbi.getActionCommand().startsWith("color_bgc_")) { // background
				handler.getController().executeCommand(new ChangeBGColor(cbi)); // colors
			}
		}
		else if (e.getSource() instanceof JMenuItem) {

			if (handler != null) // only execute if at least one diagram is available
			{
				JMenuItem b = (JMenuItem) e.getSource();
				Selector selector = handler.getDrawPanel().getSelector();
				if (b.getText() == "Delete") {
					Vector<Entity> v = selector.getSelectedEntities();
					if (v.size() > 0) {
						handler.getController().executeCommand(
								new RemoveElement(v));
					}
				}
				else if (b.getText().equals("Group")) {
					handler.getController().executeCommand(new AddGroup());
				}
				else if (b.getText().equals("Ungroup")) {
					Vector<Entity> entities = selector.getSelectedEntities();
					for (int i = 0; i < entities.size(); i++) {
						if (entities.get(i) instanceof Group) handler.getController().executeCommand(
								new Ungroup((Group) entities.get(i)));
					}
				}
				else if (b.getText().equals("Edit Selected...")) {
					Entity entity = Umlet.getInstance().getEditedEntity();
					if ((entity instanceof CustomElement) && (entity != null)) {
						if (Umlet.getInstance().getGUI().getCurrentCustomHandler().closeEntity()) {
							Umlet.getInstance().getGUI().setCustomPanelEnabled(true);
							Umlet.getInstance().getGUI().getCurrentCustomHandler().getPanel().setCustomElementIsNew(false);
							Umlet.getInstance().getGUI().getCurrentCustomHandler().editEntity((CustomElement) entity);
						}
					}
				}
			}
		}
	}

	public void mouseClicked(MouseEvent me) {

	}

	public void mouseEntered(MouseEvent me) {
		Umlet.getInstance().getGUI().setCursor(Constants.HAND_CURSOR);
		JLabel label = (JLabel) me.getComponent();
		label.setForeground(Color.blue);
	}

	public void mouseExited(MouseEvent me) {
		Umlet.getInstance().getGUI().setCursor(Constants.DEFAULT_CURSOR);
		JLabel label = (JLabel) me.getComponent();
		label.setForeground(Color.black);
	}

	public void mousePressed(MouseEvent me) {
		JLabel label = (JLabel) me.getComponent();
		if (!label.getText().startsWith("Discard")) Umlet.getInstance().getGUI().getCurrentCustomHandler().saveEntity();
		if (Umlet.getInstance().getGUI().getCurrentCustomHandler().closeEntity()) {
			Umlet.getInstance().getGUI().setCustomPanelEnabled(false);
		}
	}

	public void mouseReleased(MouseEvent me) {

	}

	public void mouseDragged(MouseEvent me) {

	}

	public void mouseMoved(MouseEvent me) {

	}
}
