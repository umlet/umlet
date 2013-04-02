package com.umlet.element.experimental;

import com.umlet.element.experimental.uml.Class;
import com.umlet.element.experimental.uml.Relation;
import com.umlet.element.experimental.uml.UseCase;

/**
 * these IDs should NEVER be changed, because they are stored in uxf files
 */
public enum ElementId {
	UMLClass, UMLUseCase, Relation;
	
	public NewGridElement createAssociatedGridElement() {
		final NewGridElement returnObj;
		if (this == UMLClass) returnObj = new Class();
		else if (this == UMLUseCase) returnObj = new UseCase();
		else if (this == Relation) returnObj = new Relation();
		else throw new RuntimeException("Unknown class id: " + this);
		return returnObj;
	}
}