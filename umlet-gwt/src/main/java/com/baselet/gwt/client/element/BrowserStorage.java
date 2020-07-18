package com.baselet.gwt.client.element;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.element.interfaces.GridElement;
import com.baselet.gwt.client.BaseletGWT;
import com.baselet.gwt.client.view.VersionChecker;
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

	public static boolean initLocalStorage() {
		if (VersionChecker.GetVersion() == VersionChecker.Version.VSCODE) {
			return initStubLocalStorage();
		}
		else {
			return initLocalStorageAndCheckIfAvailable();
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
		if (VersionChecker.GetVersion() == VersionChecker.Version.VSCODE) {
			return null;
		}
		else {
			return localStorage.getItem(id);
		}

	}

	private static void remove(String id) {
		if (VersionChecker.GetVersion() == VersionChecker.Version.VSCODE) {
		}
		else {
			localStorage.removeItem(id);
		}
	}

	private static Map<String, String> getWithPrefix(String prefix, boolean removePrefixFromKey) {
		if (VersionChecker.GetVersion() == VersionChecker.Version.VSCODE) {
			Map<String, String> returnList = new HashMap<String, String>();
			/* for (int i = 0; i < localStorage.getLength(); i++) { String key = localStorage.key(i); if (key.startsWith(prefix)) { if (removePrefixFromKey) { key = key.substring(prefix.length()); } returnList.put(key, localStorage.getItem(key)); } } */
			return returnList;
		}
		else {
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

	}

	private static void set(String id, String value) {
		if (VersionChecker.GetVersion() == VersionChecker.Version.VSCODE) {
		}
		else {
			localStorage.setItem(id, value);
		}
	}

}
