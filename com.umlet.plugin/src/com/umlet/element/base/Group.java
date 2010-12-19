// The UMLet source code is distributed under the terms of the GPL; see license.txt
// Class by A.Mueller Oct.05

package com.umlet.element.base;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.command.AddEntity;
import com.umlet.control.diagram.Selector;
import com.umlet.control.diagram.StickingPolygon;

@SuppressWarnings("serial")
public class Group extends Entity {
	private Vector<Entity> entities;

	// after adding all elements to a group the function adjustSize has to be called!
	public Group() {
		super();
		this.entities = new Vector<Entity>();
	}

	/**
	 * Erstellt einen neue Gruppe
	 */
	public void group(Vector<Entity> ents) {
		if (ents.isEmpty()) return;
		Selector s = ents.get(0).getHandler().getDrawPanel().getSelector();

		this.entities.clear();
		for (Iterator<Entity> it = ents.iterator(); it.hasNext();)
			this.addMember(it.next());

		adjustSize(false);
		s.singleSelect(this);
		(new AddEntity(this, this.getLocation().x, this.getLocation().y)).execute(this.getHandler());
	}

	public void ungroup() {
		for (Entity e : this.entities) {
			e.setGroup(null);
			e.addMouseListener(this.getHandler().getEntityListener(e));
			e.addMouseMotionListener(this.getHandler().getEntityListener(e));
		}

		this.getHandler().getDrawPanel().remove(this);
		this.getHandler().getDrawPanel().repaint();
	}

	public Vector<Entity> getMembers() {
		return entities;
	}

	public void addMember(Entity member) {
		this.entities.add(member);
		member.setGroup(this);
		if (member.getHandler() != null) {
			member.removeMouseListener(member.getHandler().getEntityListener(member));
			member.removeMouseMotionListener(member.getHandler().getEntityListener(member));
		}
	}

	public void removeMemberListeners() {
		if (this.getHandler() != null) {
			for (Entity e : this.entities) {
				e.removeMouseListener(this.getHandler().getEntityListener(e));
				e.removeMouseMotionListener(this.getHandler().getEntityListener(e));
			}
		}
	}

	@Override
	public void paintEntity(Graphics g) {
		if (this.isSelected() && !this.isPartOfGroup()) {
			Graphics2D g2 = (Graphics2D) g;
			this.getHandler().getFRC(g2);
			g2.setColor(java.awt.Color.green);
			g2.setStroke(Constants.getStroke(1, 1));
			g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
			g2.setStroke(Constants.getStroke(0, 1));
		}
	}

	@Override
	public void onDeselected() {
		super.onDeselected();
		for (Entity e : this.entities)
			e.onDeselected();
	}

	@Override
	public void onSelected() {
		super.onSelected();
		for (Entity e : this.entities)
			e.onSelected();
	}

	public void adjustSize(boolean recursive) {

		if (entities.isEmpty()) return;

		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = 0;
		int maxY = 0;
		for (Entity e : entities) {
			if (recursive && (e instanceof Group)) ((Group) e).adjustSize(true);
			maxX = Math.max(e.getX() + e.getWidth(), maxX);
			maxY = Math.max(e.getY() + e.getHeight(), maxY);
			minX = Math.min(e.getX(), minX);
			minY = Math.min(e.getY(), minY);
		}
		this.setLocation(minX, minY);
		this.setSize((maxX - minX), (maxY - minY));
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		return null;
	}

	@Override
	public Entity CloneFromMe() {
		Group temp = new Group();
		for (Entity e : this.entities) {
			Entity clone = e.CloneFromMe();
			temp.addMember(clone);
		}
		temp.adjustSize(false);
		return temp;
	}

	@Override
	public void changeLocation(int diffx, int diffy) {
		super.changeLocation(diffx, diffy);
		for (Entity e : this.entities)
			e.changeLocation(diffx, diffy);
	}

	@Override
	public int getPossibleResizeDirections() { // LME: deny resizing of groups
		return 0;
	}
}
