package com.baselet.diagram.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.GridElement;


public class ChangeElementSetting extends Command {

	private String key;
	private String value;
	private Collection<GridElement> elements;
	private Map<GridElement, String> oldValue;

	public ChangeElementSetting(String key, String value, Collection<GridElement> elements) {
		this.key = key;
		this.value = value;
		this.elements = new ArrayList<GridElement>(elements); // make copy to make sure list cannot be modified from outside
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		oldValue = new HashMap<GridElement, String>();
		for (GridElement e : elements) {
			oldValue.put(e, e.getSetting(key));
			e.setProperty(key, value);
			if (handler.getDrawPanel().getSelector().isSelected(e)) {
				Main.getHandlerForElement(e).getDrawPanel().getSelector().updateSelectorInformation(); // update the property panel to display changed attributes
			}
		}
		handler.getDrawPanel().repaint();
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		for (Entry<GridElement, String> entry : oldValue.entrySet()) {
			entry.getKey().setProperty(key, entry.getValue());
		}
		handler.getDrawPanel().repaint();
	}
	
	@Override
	public boolean isMergeableTo(Command c) {
		if (!(c instanceof ChangeElementSetting)) return false;
		ChangeElementSetting other = (ChangeElementSetting) c;
		return equalsKeyAndValue(other);
	}
	
	@Override
	public Command mergeTo(Command c) {
		ChangeElementSetting other = (ChangeElementSetting) c;
		List<GridElement> allElements = new ArrayList<GridElement>(this.elements);
		allElements.addAll(other.elements);
		return new ChangeElementSetting(key, value, allElements);
	}
	
	private boolean equalsKeyAndValue(ChangeElementSetting other) {
		if (this == other) return true;
		if (other == null) return false;
		if (key == null) {
			if (other.key != null) return false;
		}
		else if (!key.equals(other.key)) return false;
		if (value == null) {
			if (other.value != null) return false;
		}
		else if (!value.equals(other.value)) return false;
		return true;
	
	}
	
	
}
