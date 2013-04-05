package com.baselet.element;

public interface Selector {

	public void select(GridElement ... elements);
	public void deselect(GridElement ... elements);
	public boolean isSelected(GridElement element);
}
