package com.baselet.gwt.client;

import java.util.List;

import com.baselet.element.GridElement;
import com.baselet.element.Selector;
import com.google.gwt.storage.client.Storage;

/**
 * uses local storage of browser
 * local storage calculator: http://glynrob.com/webapp/lscalc/
 */
public class BrowserStorage {

	private static final String NO_STORAGE_ERROR = "The Browser doesn't support local storage";
	private static final String CACHED_DIAGRAM_ID = "CachedDiagram";
	private static final String CLIPBOARD = "Clipboard";
	
	private static Storage localStorage = Storage.getLocalStorageIfSupported();

	public static void setCachedDiagram(String diagramXml) {
		set(CACHED_DIAGRAM_ID, diagramXml);
	}
	
	public static String getCachedDiagram() {
		return get(CACHED_DIAGRAM_ID);
	}
	
	public static void setClipboard(List<GridElement> gridelements) {
		set(CLIPBOARD, OwnXMLParser.gridElementsToXml(gridelements));
	}
	
	public static List<GridElement> getClipboard(Selector selector) {
		return OwnXMLParser.xmlToGridElements(get(CLIPBOARD), selector);
	}
	
	private static String get(String id) {
		if (localStorage == null) throw new RuntimeException(NO_STORAGE_ERROR);
		return localStorage.getItem(id);
	}
	
	private static void set(String id, String value) {
		if (localStorage == null) throw new RuntimeException(NO_STORAGE_ERROR);
		localStorage.setItem(id, value);
	}
}
