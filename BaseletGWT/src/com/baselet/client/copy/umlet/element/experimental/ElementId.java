package com.baselet.client.copy.umlet.element.experimental;

import com.baselet.client.copy.umlet.element.experimental.uml.UseCase;
import com.baselet.client.copy.umlet.element.experimental.uml.Class;

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