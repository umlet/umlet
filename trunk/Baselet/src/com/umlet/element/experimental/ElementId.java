package com.umlet.element.experimental;

import com.umlet.element.experimental.uml.Class;
import com.umlet.element.experimental.uml.UseCase;

public enum ElementId {
	UMLClass, UMLUseCase;
	
	public NewGridElement createAssociatedGridElement() {
		final NewGridElement returnObj;
		if (this == Class.ID) returnObj = new Class();
		else if (this == UseCase.ID) returnObj = new UseCase();
		else throw new RuntimeException("Unknown class id: " + this);
		return returnObj;
	}
}