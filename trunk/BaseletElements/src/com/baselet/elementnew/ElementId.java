package com.baselet.elementnew;

import com.baselet.elementnew.element.Text;
import com.baselet.elementnew.element.plot.PlotGrid;
import com.baselet.elementnew.element.uml.ActivityObject;
import com.baselet.elementnew.element.uml.Actor;
import com.baselet.elementnew.element.uml.Class;
import com.baselet.elementnew.element.uml.Deployment;
import com.baselet.elementnew.element.uml.Frame;
import com.baselet.elementnew.element.uml.Generic;
import com.baselet.elementnew.element.uml.Hierarchy;
import com.baselet.elementnew.element.uml.Interface;
import com.baselet.elementnew.element.uml.Note;
import com.baselet.elementnew.element.uml.Package;
import com.baselet.elementnew.element.uml.SpecialState;
import com.baselet.elementnew.element.uml.State;
import com.baselet.elementnew.element.uml.SyncBarHorizontal;
import com.baselet.elementnew.element.uml.SyncBarVertical;
import com.baselet.elementnew.element.uml.Timer;
import com.baselet.elementnew.element.uml.UseCase;
import com.baselet.elementnew.element.uml.relation.Relation;

/**
 * these IDs should NEVER be changed, because they are stored in uxf files
 */
public enum ElementId {
	UMLClass, UMLUseCase, UMLInterface, UMLActor, UMLState, UMLObject, UMLTimer, UMLSpecialState, UMLNote, UMLSyncBarHorizontal, UMLSyncBarVertical, UMLPackage, UMLFrame, UMLDeployment, UMLGeneric, UMLHierarchy, Relation, Text, PlotGrid;

	public NewGridElement createAssociatedGridElement() {
		switch (this) {
			case PlotGrid:
				return new PlotGrid();
			case Relation:
				return new Relation();
			case Text:
				return new Text();
			case UMLActor:
				return new Actor();
			case UMLClass:
				return new Class();
			case UMLDeployment:
				return new Deployment();
			case UMLFrame:
				return new Frame();
			case UMLGeneric:
				return new Generic();
			case UMLInterface:
				return new Interface();
			case UMLNote:
				return new Note();
			case UMLObject:
				return new ActivityObject();
			case UMLPackage:
				return new Package();
			case UMLSpecialState:
				return new SpecialState();
			case UMLState:
				return new State();
			case UMLSyncBarHorizontal:
				return new SyncBarHorizontal();
			case UMLSyncBarVertical:
				return new SyncBarVertical();
			case UMLTimer:
				return new Timer();
			case UMLUseCase:
				return new UseCase();
			case UMLHierarchy:
				return new Hierarchy();
			default:
				throw new RuntimeException("Unknown class id: " + this);
		}
	}
}