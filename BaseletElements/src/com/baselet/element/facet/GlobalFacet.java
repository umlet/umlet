package com.baselet.element.facet;

/**
 * Global facets are parsed before any other ones, because they influence the whole diagram, even if they are located at the bottom
 * e.g. style=wordwrap may be located at the bottom but has an influence on every printed line, style=autoresize is even more important because it influences the size of the whole element
 */
public abstract class GlobalFacet extends Facet {

}
