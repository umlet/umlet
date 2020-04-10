package com.baselet.gwt.client.element;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.element.interfaces.GridElement;
import com.baselet.gwt.client.BaseletGWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.storage.client.Storage;

/**
 * uses local storage of browser
 * local storage calculator: http://glynrob.com/webapp/lscalc/
 */
public class BrowserStorage {

	private static final String CLIPBOARD = "Clipboard";
	private static final String SAVE_PREFIX = "s_";

	private static Storage localStorage;
	static Logger log = LoggerFactory.getLogger(BaseletGWT.class);

	public static boolean initLocalStorageAndCheckIfAvailable() {
		try {
			localStorage = Storage.getLocalStorageIfSupported();
			return localStorage != null;
		} catch (Exception e) {
			return false; // Firefox with the Cookie setting "ask everytime" will throw an exception here!
		}

	}

	public static boolean initStubLocalStorage() {
		localStorage = null;
		return true;
	}

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
		return null;
	}

	private static void remove(String id) {
	}

	private static Map<String, String> getWithPrefix(String prefix, boolean removePrefixFromKey) {
		Map<String, String> returnList = new HashMap<String, String>();
		/* for (int i = 0; i < localStorage.getLength(); i++) { String key = localStorage.key(i); if (key.startsWith(prefix)) { if (removePrefixFromKey) { key = key.substring(prefix.length()); } returnList.put(key, localStorage.getItem(key)); } } */
		return returnList;
	}

	private static void set(String id, String value) {

	}

	private static class KeyValuePairHolder extends JavaScriptObject {
		// Constructor needs to be protected and zero-arguments
		protected KeyValuePairHolder() {
		}

		// JSNI is used for setting/getting properties
		public final native String getId() /*-{
			return this.Id;
		}-*/;

		public final native String getValue() /*-{
			return this.Value;
		}-*/;

		public final native void setId(String id) /*-{
			this.Id = id;
		}-*/;

		public final native void setValue(String value) /*-{
			this.Value = value;
		}-*/;
	}

}
