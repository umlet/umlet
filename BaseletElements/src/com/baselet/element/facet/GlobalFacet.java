package com.baselet.element.facet;

/**
 * Global facets are parsed before any other ones, because they influence the whole diagram, even if they are located at the bottom
 * e.g. bg=red must be known before drawing the common content of an element; style=autoresize must be known as soon as possible to make the size-calculations
 */
public abstract class GlobalFacet extends Facet {

}
