// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.command;

import java.awt.Point;
import java.util.Vector;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.StickingPolygon;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Relation;
import com.umlet.element.base.relation.RelationLinePoint;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2001
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class Resize extends Command {
	private static int current_id = 0;

	private int id;
	private int diffx, diffy, diffw, diffh;
	private Vector<RelationLinePoint> linepoints;
	private Vector<MoveLinePoint> move_commands;
	private Entity entity;

	public Resize(Entity entity, int diffx, int diffy, int diffw, int diffh) {
		this(entity, diffx, diffy, diffw, diffh, null);
	}

	// resize for merge
	private Resize(Entity entity, int id, int diffx, int diffy, int diffw, int diffh,
			Vector<MoveLinePoint> move_commands, Vector<MoveLinePoint> move_commands2) {
		this.entity = entity;
		this.id = id;
		this.move_commands = move_commands;
		this.move_commands.addAll(move_commands2);
		this.diffx = diffx;
		this.diffy = diffy;
		this.diffw = diffw;
		this.diffh = diffh;
	}

	public Resize(Entity entity, int diffx, int diffy, int diffw, int diffh, Resize first) {
		this.entity = entity;
		this.move_commands = new Vector<MoveLinePoint>();
		this.diffx = diffx;
		this.diffy = diffy;
		this.diffw = diffw - diffx;
		this.diffh = diffh - diffy;

		StickingPolygon from = this.entity.generateStickingBorder(this.entity.getX(), this.entity.getY(),
				this.entity.getWidth() - 1, this.entity.getHeight() - 1);
		StickingPolygon to = this.entity.generateStickingBorder(this.entity.getX() + diffx, this.entity.getY() + diffy,
				this.entity.getWidth() - 1 + diffw, this.entity.getHeight() - 1 + diffh);

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
			diff = from.getLine(lp.getStickingLineId()).diffToLine(to.getLine(lp.getStickingLineId()), p.x + r.getX(), p.y + r.getY());
			this.move_commands.add(new MoveLinePoint(lp.getRelation(), lp.getLinePointId(), diff.x, diff.y));
		}

	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		entity.changeLocation(diffx, diffy);
		entity.changeSize(diffw, diffh);
		for (MoveLinePoint c : this.move_commands)
			c.execute(handler);
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		entity.changeLocation(-diffx, -diffy);
		entity.changeSize(-diffw, -diffh);
		for (MoveLinePoint c : this.move_commands)
			c.undo(handler);
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
		return new Resize(this.entity, Math.max(this.id, tmp.id), this.diffx + tmp.diffx, this.diffy + tmp.diffy,
				this.diffw + tmp.diffw, this.diffh + tmp.diffh, this.move_commands, tmp.move_commands);
	}
}
