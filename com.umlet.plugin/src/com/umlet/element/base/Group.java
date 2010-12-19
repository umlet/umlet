// The UMLet source code is distributed under the terms of the GPL; see license.txt
//Class by A.Mueller Oct.05

package com.umlet.element.base;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.command.AddEntity;
import com.umlet.control.diagram.Selector;
import com.umlet.control.diagram.StickingPolygon;


@SuppressWarnings("serial")
public class Group extends Entity
{
	private Vector<Entity> entities;

	//after adding all elements to a group the function adjustSize has to be called!
	public Group()
	{
		super();
		this.entities = new Vector<Entity>();
	}

	/**
	 * Erstellt einen neue Gruppe
	 *
	 */
	public void group(Vector<Entity> ents) {
		if(ents.isEmpty())
			return;
		Selector s = ents.get(0).getHandler().getDrawPanel().getSelector();		
		
		this.entities.clear();
		for (Iterator<Entity> it = ents.iterator(); it.hasNext();)
			this.addMember(it.next());

		adjustSize();
		s.singleSelect(this);
		(new AddEntity(this, this.getLocation().x, this.getLocation().y)).execute(this.handler);
	}
	
	public void ungroup()
	{
		for (Entity e : this.entities)
		{
			e.setGroup(null);
			e.addMouseListener(this.handler.getEntityListener(e));
			e.addMouseMotionListener(this.handler.getEntityListener(e));
		}

		this.handler.getDrawPanel().remove(this);
		this.handler.getDrawPanel().repaint();
	}
	
	public Vector<Entity> getMembers()
	{
		return entities;
	}
	
	public void addMember(Entity member)
	{
		this.entities.add(member);
		member.setGroup(this);
		if(member.handler != null) {
			member.removeMouseListener(member.handler.getEntityListener(member));
			member.removeMouseMotionListener(member.handler.getEntityListener(member));
		}
	}

	public void removeMemberListeners()
	{
		if(this.handler != null)
		{
			for(Entity e : this.entities) {
				e.removeMouseListener(this.handler.getEntityListener(e));
				e.removeMouseMotionListener(this.handler.getEntityListener(e));
			}
		}
	}
	
	public void paintEntity(Graphics g)
	{
		if (this.isSelected() && !this.isPartOfGroup())
		{
			Graphics2D g2 = (Graphics2D) g;
			this.handler.getFRC(g2);
			g2.setColor(java.awt.Color.green);
			g2.setStroke(Constants.getStroke(1, 1));
			g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
			g2.setStroke(Constants.getStroke(0, 1));
		}
	}
	
	@Override
	public void onDeselected() {
		super.onDeselected();
		for(Entity e : this.entities)
			e.onDeselected();
	}

	@Override
	public void onSelected() {
		super.onSelected();
		for(Entity e : this.entities)
			e.onSelected();
	}

	public void adjustSize()
	{
		if (entities.isEmpty())
			return;
		Enumeration<Entity> e = entities.elements();
		int maxX = 0;
		int maxY = 0;
		int minX = 0;
		int minY = 0;
		boolean first = true;
		while (e.hasMoreElements())
		{
			Entity temp = e.nextElement();
			if(!first) 
			{
				maxX = Math.max(temp.getX() + temp.getWidth(), maxX);
				maxY = Math.max(temp.getY() + temp.getHeight(), maxY);
				minX = Math.min(temp.getX(), minX);
				minY = Math.min(temp.getY(), minY);
			}
			else
			{
				first = false;
				maxX = temp.getX() + temp.getWidth();
				maxY = temp.getY() + temp.getHeight();
				minX = temp.getX();
				minY =temp.getY();
			}
		}
		this.setLocation(minX, minY);
		this.setSize((maxX - minX), (maxY - minY));
	}
	
	@Override
    public StickingPolygon generateStickingBorder(int x, int y, int width, int height)
	{
		return null;
	}

	@Override
	public Entity CloneFromMe()
	{
		Group temp = new Group();	
		for (Entity e : this.entities)
		{
			Entity clone = e.CloneFromMe();
			temp.addMember(clone);
		}
		temp.adjustSize();		
		return temp;
	}
	
	@Override
	public void changeLocation(int diffx, int diffy) {
		super.changeLocation(diffx, diffy);
		if(!this.isPartOfGroup()) {
			for(Entity e : this.entities)
				e.changeLocation(diffx, diffy, true);
		}
	}
	
	@Override
	public void changeLocation(int diffx, int diffy, boolean fromgroup) {
		super.changeLocation(diffx, diffy, fromgroup);
			for(Entity e : this.entities)
				e.changeLocation(diffx, diffy, true);
	}

	public int getPossibleResizeDirections() { //LME: deny resizing of groups
	    return 0;
	}
}
