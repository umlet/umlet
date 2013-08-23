// Class by A.Mueller Oct.05

package com.baselet.element;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.Direction;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.SelectorOld;
import com.baselet.diagram.command.AddElement;
import com.baselet.element.sticking.StickingPolygon;
import com.umlet.element.experimental.ElementFactory;


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
		SelectorOld s = Main.getHandlerForElement(ents.get(0)).getDrawPanel().getSelector();

		this.entities.clear();
		for (Iterator<GridElement> it = ents.iterator(); it.hasNext();)
			this.addMember(it.next());

		adjustSize(false);
		s.selectOnly(this);
		(new AddElement(this, this.getRectangle().x, this.getRectangle().y)).execute(Main.getHandlerForElement(this));
	}

	public void ungroup() {
		for (GridElement e : this.entities) {
			e.setGroup(null);
			((Component) e.getComponent()).addMouseListener(Main.getHandlerForElement(this).getEntityListener(e));
			((Component) e.getComponent()).addMouseMotionListener(Main.getHandlerForElement(this).getEntityListener(e));
		}

		Main.getHandlerForElement(this).getDrawPanel().removeElement(this);
		Main.getHandlerForElement(this).getDrawPanel().repaint();
	}

	public Vector<GridElement> getMembers() {
		return entities;
	}

	public Vector<GridElement> getMembersRecursive() {
		Vector<GridElement> returnVector = new Vector<GridElement>();
		getMembersAccumulator(returnVector);
		return returnVector;
	}

	private void getMembersAccumulator(Vector<GridElement> vector) {
		for (GridElement member : entities) {
			if (member instanceof Group) {
				((Group) member).getMembersAccumulator(vector);
			}
			else vector.add(member);
		}
	}

	public void addMember(GridElement member) {
		this.entities.add(member);
		member.setGroup(this);
		if (Main.getHandlerForElement(member) != null) {
			((Component) member.getComponent()).removeMouseListener(Main.getHandlerForElement(member).getEntityListener(member));
			((Component) member.getComponent()).removeMouseMotionListener(Main.getHandlerForElement(member).getEntityListener(member));
		}
	}

	public void removeMemberListeners() {
		if (Main.getHandlerForElement(this) != null) {
			for (GridElement e : this.entities) {
				((Component) e.getComponent()).removeMouseListener(Main.getHandlerForElement(this).getEntityListener(e));
				((Component) e.getComponent()).removeMouseMotionListener(Main.getHandlerForElement(this).getEntityListener(e));
			}
		}
	}

	public Group getGroup() {
		return (Group) group;
	}

	protected boolean selected = false; // necessary woraround because selector doesn't have the correct selection state
	@Override
	public void setSelected(Boolean selected) {
		super.setSelected(selected);
		this.selected = selected;
	}
	@Override
	public void paintEntity(Graphics g) {
		if (selected && !this.isPartOfGroup()) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(java.awt.Color.green);
			g2.setStroke(Utils.getStroke(LineType.DASHED, 1));

			// Store non-transparent composite
			Composite old = g2.getComposite();

			// Create a weak transparency effect for the group-border
			float alphaFactorStrong = 0.8f;
			AlphaComposite alphaStrong = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaFactorStrong);
			g2.setComposite(alphaStrong);
			g2.drawRect(0, 0, this.getRectangle().width - 1, this.getRectangle().height - 1);

			// Create a strong transparency for the group background
			float alphaFactorLight = 0.05f;
			AlphaComposite alphaLight = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaFactorLight);
			g2.setComposite(alphaLight);
			g2.fillRect(0, 0, this.getRectangle().width - 1, this.getRectangle().height - 1);

			g2.setComposite(old);
			g2.setStroke(Utils.getStroke(LineType.SOLID, 1));
		}
	}
	
	public void adjustSize(boolean recursive) {

		if (entities.isEmpty()) return;

		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = 0;
		int maxY = 0;
		for (GridElement e : entities) {
			if (recursive && (e instanceof Group)) ((Group) e).adjustSize(true);
			maxX = Math.max(e.getRectangle().x + e.getRectangle().width, maxX);
			maxY = Math.max(e.getRectangle().y + e.getRectangle().height, maxY);
			minX = Math.min(e.getRectangle().x, minX);
			minY = Math.min(e.getRectangle().y, minY);
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
			GridElement clone = ElementFactory.createCopy(e);
			temp.addMember(clone);
		}
		temp.adjustSize(false);
		Main.getHandlerForElement(this).setHandlerAndInitListeners(temp);
		return temp;
	}

	@Override
	public void setLocationDifference(int diffx, int diffy) {
		super.setLocationDifference(diffx, diffy);
		for (GridElement e : this.entities)
			e.setLocationDifference(diffx, diffy);
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		return new HashSet<Direction>(); // deny size changes
	}
	
	/**
	 * the groups layer is always 1 level under the lowest contained layer
	 */
	@Override
	public Integer getLayer() {
		int minLayer = 0;
		for (GridElement el : entities) {
			minLayer = Math.min(minLayer, el.getLayer());
		}
		return minLayer-1;
	}
}
