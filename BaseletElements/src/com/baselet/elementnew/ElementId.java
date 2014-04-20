package com.baselet.elementnew;

import com.baselet.elementnew.element.Text;
import com.baselet.elementnew.element.plot.PlotGrid;
import com.baselet.elementnew.element.uml.ActivityObject;
import com.baselet.elementnew.element.uml.Actor;
import com.baselet.elementnew.element.uml.Class;
import com.baselet.elementnew.element.uml.Deployment;
import com.baselet.elementnew.element.uml.Frame;
import com.baselet.elementnew.element.uml.Generic;
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
	UMLClass, UMLUseCase, UMLInterface, UMLActor, UMLState, UMLObject, UMLTimer, UMLSpecialState, UMLNote, UMLSyncBarHorizontal, UMLSyncBarVertical, UMLPackage, UMLFrame, UMLDeployment, UMLGeneric, Relation, Text, 
	PlotGrid /*standalone only (at the moment), therefore instantiated in ElementFactory and not here*/;
	
	public NewGridElement createAssociatedGridElement() {
		final NewGridElement returnObj;
		if (this == UMLClass) returnObj = new Class();
		else if (this == UMLUseCase) returnObj = new UseCase();
		else if (this == UMLInterface) returnObj = new Interface();
		else if (this == UMLActor) returnObj = new Actor();
		else if (this == UMLState) returnObj = new State();
		else if (this == UMLObject) returnObj = new ActivityObject();
		else if (this == UMLTimer) returnObj = new Timer();
		else if (this == UMLSpecialState) returnObj = new SpecialState();
		else if (this == UMLNote) returnObj = new Note();
		else if (this == UMLSyncBarHorizontal) returnObj = new SyncBarHorizontal();
		else if (this == UMLSyncBarVertical) returnObj = new SyncBarVertical();
		else if (this == UMLPackage) returnObj = new Package();
		else if (this == UMLFrame) returnObj = new Frame();
		else if (this == UMLDeployment) returnObj = new Deployment();
		else if (this == UMLGeneric) returnObj = new Generic();
		else if (this == Relation) returnObj = new Relation();
		else if (this == Text) returnObj = new Text();
		else if (this == PlotGrid) returnObj = new PlotGrid();
		else throw new RuntimeException("Unknown class id: " + this);
		return returnObj;
	}
}