package com.umlet.element.experimental;

import com.umlet.element.experimental.uml.Class;
import com.umlet.element.experimental.uml.UseCase;

public class ElementFactory {
	public static enum ElementId {
		UMLClass, UMLUseCase;
	}
	
	/**
	 * uses no reflection, to avoid complications with GWT
	 */
	public static NewGridElement create(String idString) {
		ElementId id = ElementId.valueOf(idString);
		if (id == Class.ID) return new Class();
		if (id == UseCase.ID) return new UseCase();
		throw new RuntimeException("Unknown class id: " + id);
	}
}
