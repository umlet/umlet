package com.baselet.diagram.command;

import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.element.GridElement;
import com.baselet.element.sticking.StickingPolygon;
import com.umlet.element.Relation;
import com.umlet.element.relation.RelationLinePoint;


public class Resize extends Command {
	private static int current_id = 0;

	private int id;
	private int diffx, diffy, diffw, diffh;
	private Vector<RelationLinePoint> linepoints;
	private Vector<MoveLinePoint> move_commands;
	private GridElement entity;

	private int getDiffx() {
		return diffx * Main.getHandlerForElement(entity).getGridSize();
	}

	private int getDiffy() {
		return diffy * Main.getHandlerForElement(entity).getGridSize();
	}

	private int getDiffw() {
		return diffw * Main.getHandlerForElement(entity).getGridSize();
	}

	private int getDiffh() {
		return diffh * Main.getHandlerForElement(entity).getGridSize();
	}

	public Resize(GridElement entity, int diffx, int diffy, int diffw, int diffh) {
		this(entity, diffx, diffy, diffw, diffh, null);
	}

	// resize for merge
	private Resize(GridElement entity, int id, int diffx, int diffy, int diffw, int diffh,
			Vector<MoveLinePoint> move_commands, Vector<MoveLinePoint> move_commands2) {
		this.entity = entity;
		this.id = id;
		this.move_commands = move_commands;
		this.move_commands.addAll(move_commands2);
		this.diffx = diffx / Main.getHandlerForElement(entity).getGridSize();
		this.diffy = diffy / Main.getHandlerForElement(entity).getGridSize();
		this.diffw = diffw / Main.getHandlerForElement(entity).getGridSize();
		this.diffh = diffh / Main.getHandlerForElement(entity).getGridSize();
	}

	public Resize(GridElement entity, int diffx, int diffy, int diffw, int diffh, Resize first) {
		this.entity = entity;
		this.move_commands = new Vector<MoveLinePoint>();
		this.diffx = diffx / Main.getHandlerForElement(entity).getGridSize();
		this.diffy = diffy / Main.getHandlerForElement(entity).getGridSize();
		this.diffw = (diffw - diffx) / Main.getHandlerForElement(entity).getGridSize();
		this.diffh = (diffh - diffy) / Main.getHandlerForElement(entity).getGridSize();

		Rectangle entityRect = this.entity.getRectangle();
		StickingPolygon from = this.entity.generateStickingBorder(entityRect);

		// AB: FIXED: Use this.diffw/this.diffh instead of diffw/diffh as calculation base
		Rectangle newRect = new Rectangle(entityRect.x + diffx, entityRect.y + diffy, entityRect.width + getDiffw(), entityRect.height + getDiffh());
		StickingPolygon to = this.entity.generateStickingBorder(newRect);

		if (first != null) {
			this.id = first.id;
			this.linepoints = first.linepoints;
		}
		else {
			this.id = current_id;
			current_id++;
			this.linepoints = Utils.getStickingRelationLinePoints(Main.getHandlerForElement(this.entity), from);
		}

		PointDouble diff;
		Point p;
		Relation r;
		for (RelationLinePoint lp : this.linepoints) {
			r = lp.getRelation();
			p = r.getLinePoints().get(lp.getLinePointId());

			diff = from.getLine(lp.getStickingLineId()).diffToLine(to.getLine(lp.getStickingLineId()), p.x + r.getRectangle().x, p.y + r.getRectangle().y);

			DiagramHandler handler = Main.getHandlerForElement(entity);
			this.move_commands.add(new MoveLinePoint(lp.getRelation(), lp.getLinePointId(), handler.realignToGrid(diff.x), handler.realignToGrid(diff.y)));
		}

	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);

		entity.setLocationDifference(getDiffx(), getDiffy());
		entity.changeSize(getDiffw(), getDiffh());
		if (entity.isStickingBorderActive()) {
			for (MoveLinePoint c : this.move_commands) {
				c.execute(handler);
			}
		}
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		entity.setLocationDifference(-getDiffx(), -getDiffy());
		entity.changeSize(-getDiffw(), -getDiffh());
		for (MoveLinePoint c : this.move_commands)
			c.undo(handler);
		Main.getInstance().getDiagramHandler().getDrawPanel().updatePanelAndScrollbars();
	}

	@Override
	public boolean isMergeableTo(Command c) {
		if (!(c instanceof Resize)) return false;
		Resize r = (Resize) c;
		if (this.id == r.id) return true;
		return false;
	}

	@Override
	public Command mergeTo(Command c) {
		Resize tmp = (Resize) c;
		return new Resize(this.entity, Math.max(this.id, tmp.id), this.getDiffx() + tmp.getDiffx(), this.getDiffy() + tmp.getDiffy(),
				this.getDiffw() + tmp.getDiffw(), this.getDiffh() + tmp.getDiffh(), this.move_commands, tmp.move_commands);
	}
}
