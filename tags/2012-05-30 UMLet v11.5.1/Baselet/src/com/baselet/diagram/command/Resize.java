package com.baselet.diagram.command;

import java.awt.Point;
import java.util.Vector;

import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.GridElement;
import com.baselet.element.StickingPolygon;
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
		return diffx * entity.getHandler().getGridSize();
	}

	private int getDiffy() {
		return diffy * entity.getHandler().getGridSize();
	}

	private int getDiffw() {
		return diffw * entity.getHandler().getGridSize();
	}

	private int getDiffh() {
		return diffh * entity.getHandler().getGridSize();
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
		this.diffx = diffx / entity.getHandler().getGridSize();
		this.diffy = diffy / entity.getHandler().getGridSize();
		this.diffw = diffw / entity.getHandler().getGridSize();
		this.diffh = diffh / entity.getHandler().getGridSize();
	}

	public Resize(GridElement entity, int diffx, int diffy, int diffw, int diffh, Resize first) {
		this.entity = entity;
		this.move_commands = new Vector<MoveLinePoint>();
		this.diffx = diffx / entity.getHandler().getGridSize();
		this.diffy = diffy / entity.getHandler().getGridSize();
		this.diffw = (diffw - diffx) / entity.getHandler().getGridSize();
		this.diffh = (diffh - diffy) / entity.getHandler().getGridSize();

		StickingPolygon from = this.entity.generateStickingBorder(this.entity.getLocation().x, this.entity.getLocation().y,
				this.entity.getSize().width, this.entity.getSize().height);

		// AB: FIXED: Use this.diffw/this.diffh instead of diffw/diffh as calculation base
		StickingPolygon to = this.entity.generateStickingBorder(this.entity.getLocation().x + diffx, this.entity.getLocation().y + diffy,
				this.entity.getSize().width + getDiffw(), this.entity.getSize().height + this.getDiffh());

		if (first != null) {
			this.id = first.id;
			this.linepoints = first.linepoints;
		}
		else {
			this.id = current_id;
			current_id++;
			this.linepoints = from.getStickingRelationLinePoints(this.entity.getHandler().getDrawPanel());
		}

		Point diff, p;
		Relation r;
		for (RelationLinePoint lp : this.linepoints) {
			r = lp.getRelation();
			p = r.getLinePoints().get(lp.getLinePointId());

			diff = from.getLine(lp.getStickingLineId()).diffToLine(to.getLine(lp.getStickingLineId()), p.x + r.getLocation().x, p.y + r.getLocation().y);

			DiagramHandler handler = Main.getInstance().getDiagramHandler();
			this.move_commands.add(new MoveLinePoint(lp.getRelation(), lp.getLinePointId(), handler.realignToGrid(diff.x), handler.realignToGrid(diff.y)));
		}

	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);

		entity.changeLocation(getDiffx(), getDiffy());
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
		entity.changeLocation(-getDiffx(), -getDiffy());
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
