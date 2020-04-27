package com.baselet.control.enums;

/**
 * these IDs should NEVER be changed, because they are stored in uxf files
 * To add a new element, add an Id here, implement the getId() method of the element and the creation method in ElementFactory
 */
public enum ElementId {
	UMLClass, UMLUseCase, UMLInterface, UMLActor, UMLState, UMLObject, UMLTimer, UMLSpecialState, UMLNote, UMLSyncBarHorizontal, UMLSyncBarVertical, UMLPackage, UMLFrame, UMLDeployment, UMLGeneric, UMLHierarchy, Relation, Text, PlotGrid, UMLSequenceAllInOne;

}