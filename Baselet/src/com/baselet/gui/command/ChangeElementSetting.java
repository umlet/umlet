package com.baselet.gui.command;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.baselet.control.HandlerElementMap;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.interfaces.GridElement;

public class ChangeElementSetting extends Command {

	private String key;
	private Map<GridElement, String> elementValueMap;
	private Map<GridElement, String> oldValue;

	public ChangeElementSetting(String key, String value, Collection<GridElement> element) {
		this(key, createSingleValueMap(value, element));
	}

	public ChangeElementSetting(String key, Map<GridElement, String> elementValueMap) {
		this.key = key;
		this.elementValueMap = elementValueMap;
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		oldValue = new HashMap<GridElement, String>();

		for (Entry<GridElement, String> entry : elementValueMap.entrySet()) {
			GridElement e = entry.getKey();
			oldValue.put(e, e.getSetting(key));
			e.setProperty(key, entry.getValue());
			if (handler.getDrawPanel().getSelector().isSelected(e)) {
				HandlerElementMap.getHandlerForElement(e).getDrawPanel().getSelector().updateSelectorInformation(); // update the property panel to display changed attributes
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

	private static Map<GridElement, String> createSingleValueMap(String value, Collection<GridElement> elements) {
		Map<GridElement, String> singleValueMap = new HashMap<GridElement, String>(1);
		for (GridElement e : elements) {
			singleValueMap.put(e, value);
		}
		return singleValueMap;
	}
}
