// Class by A.Mueller Oct.05

package com.baselet.element;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Vector;

import com.baselet.control.Constants.LineType;
import com.baselet.control.Utils;
import com.baselet.diagram.Selector;
import com.baselet.diagram.command.AddElement;


@SuppressWarnings("serial")
public class Group extends OldGridElement {
	private Vector<GridElement> entities;

	// after adding all elements to a group the function adjustSize has to be called!
	public Group() {
		super();
		this.entities = new Vector<GridElement>();
	}

	/**
	 * Erstellt einen neue Gruppe
	 */
	public void group(Vector<GridElement> ents) {
		if (ents.isEmpty()) return;
		Selector s = ents.get(0).getHandler().getDrawPanel().getSelector();

		this.entities.clear();
		for (Iterator<GridElement> it = ents.iterator(); it.hasNext();)
			this.addMember(it.next());

		adjustSize(false);
		s.singleSelect(this);
		(new AddElement(this, this.getLocation().x, this.getLocation().y)).execute(this.getHandler());
	}

	public void ungroup() {
		for (GridElement e : this.entities) {
			e.setGroup(null);
			e.addMouseListener(this.getHandler().getEntityListener(e));
			e.addMouseMotionListener(this.getHandler().getEntityListener(e));
		}

		this.getHandler().getDrawPanel().removeElement(this);
		this.getHandler().getDrawPanel().repaint();
	}

	public Vector<GridElement> getMembers() {
		return entities;
	}

	public void addMember(GridElement member) {
		this.entities.add(member);
		member.setGroup(this);
		if (member.getHandler() != null) {
			member.removeMouseListener(member.getHandler().getEntityListener(member));
			member.removeMouseMotionListener(member.getHandler().getEntityListener(member));
		}
	}

	public void removeMemberListeners() {
		if (this.getHandler() != null) {
			for (GridElement e : this.entities) {
				e.removeMouseListener(this.getHandler().getEntityListener(e));
				e.removeMouseMotionListener(this.getHandler().getEntityListener(e));
			}
		}
	}

	@Override
	public void paintEntity(Graphics g) {
		if (this.isSelected() && !this.isPartOfGroup()) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(java.awt.Color.green);
			g2.setStroke(Utils.getStroke(LineType.DASHED, 1));

			// Store non-transparent composite
			Composite old = g2.getComposite();

			// Create a weak transparency effect for the group-border
			float alphaFactorStrong = 0.8f;
			AlphaComposite alphaStrong = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaFactorStrong);
			g2.setComposite(alphaStrong);
			g2.drawRect(0, 0, this.getSize().width - 1, this.getSize().height - 1);

			// Create a strong transparency for the group background
			float alphaFactorLight = 0.05f;
			AlphaComposite alphaLight = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaFactorLight);
			g2.setComposite(alphaLight);
			g2.fillRect(0, 0, this.getSize().width - 1, this.getSize().height - 1);

			g2.setComposite(old);
			g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
		}
	}

	@Override
	public void onDeselected() {
		super.onDeselected();
		for (GridElement e : this.entities)
			e.onDeselected();
	}

	@Override
	public void onSelected() {
		super.onSelected();
		for (GridElement e : this.entities)
			e.onSelected();
	}

	public void adjustSize(boolean recursive) {

		if (entities.isEmpty()) return;

		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = 0;
		int maxY = 0;
		for (GridElement e : entities) {
			if (recursive && (e instanceof Group)) ((Group) e).adjustSize(true);
			maxX = Math.max(e.getLocation().x + e.getSize().width, maxX);
			maxY = Math.max(e.getLocation().y + e.getSize().height, maxY);
			minX = Math.min(e.getLocation().x, minX);
			minY = Math.min(e.getLocation().y, minY);
		}
		this.setLocation(minX, minY);
		this.setSize((maxX - minX), (maxY - minY));
	}

	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		return null;
	}

	@Override
	public GridElement CloneFromMe() {
		Group temp = new Group();
		for (GridElement e : this.entities) {
			GridElement clone = e.CloneFromMe();
			temp.addMember(clone);
		}
		temp.adjustSize(false);
		temp.setHandlerAndInitListeners(this.getHandler());
		return temp;
	}

	@Override
	public void changeLocation(int diffx, int diffy) {
		super.changeLocation(diffx, diffy);
		for (GridElement e : this.entities)
			e.changeLocation(diffx, diffy);
	}
}
