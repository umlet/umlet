package com.baselet.gwt.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baselet.element.GridElement;
import com.google.gwt.storage.client.Storage;

/**
 * uses local storage of browser
 * local storage calculator: http://glynrob.com/webapp/lscalc/
 */
public class BrowserStorage {

	private static final String NO_STORAGE_ERROR = "The Browser doesn't support local storage";
	private static final String CLIPBOARD = "Clipboard";
	private static final String SAVE_PREFIX = "s_";

	private static Storage localStorage = Storage.getLocalStorageIfSupported();

	public static void addSavedDiagram(String name, String diagramXml) {
		set(SAVE_PREFIX + name, diagramXml);
	}

	public static void removeSavedDiagram(String chosenName) {
		remove(SAVE_PREFIX + chosenName);
	}

	public static String getSavedDiagram(String name) {
		return get(SAVE_PREFIX + name);
	}

	public static Collection<String> getSavedDiagramKeys() {
		return getWithPrefix(SAVE_PREFIX, true).keySet();
	}

	public static void setClipboard(List<GridElement> gridelements) {
		set(CLIPBOARD, DiagramXmlParser.gridElementsToXml(gridelements));
	}

	public static List<GridElement> getClipboard() {
		return DiagramXmlParser.xmlToGridElements(get(CLIPBOARD));
	}

	private static String get(String id) {
		if (localStorage == null) {
			throw new RuntimeException(NO_STORAGE_ERROR);
		}
		return localStorage.getItem(id);
	}

	private static void remove(String id) {
		if (localStorage == null) {
			throw new RuntimeException(NO_STORAGE_ERROR);
		}
		localStorage.removeItem(id);
	}

	private static Map<String, String> getWithPrefix(String prefix, boolean removePrefixFromKey) {
		if (localStorage == null) {
			throw new RuntimeException(NO_STORAGE_ERROR);
		}
		Map<String, String> returnList = new HashMap<String, String>();
		for (int i = 0; i < localStorage.getLength(); i++) {
			String key = localStorage.key(i);
			if (key.startsWith(prefix)) {
				if (removePrefixFromKey) {
					key = key.substring(prefix.length());
				}
				returnList.put(key, localStorage.getItem(key));
			}
		}
		return returnList;
	}

	private static void set(String id, String value) {
		if (localStorage == null) {
			throw new RuntimeException(NO_STORAGE_ERROR);
		}
		localStorage.setItem(id, value);
	}
}
