package com.web.gwt.client.clipboard;

import com.baselet.gwt.client.clipboard.ClipboardStorage;
import com.google.gwt.storage.client.Storage;

import java.util.HashMap;
import java.util.Map;

/**
 * uses local storage of browser
 * local storage calculator: http://glynrob.com/webapp/lscalc/
 */
public class LocalStorageClipboard extends ClipboardStorage {
    private static Storage localStorage;

    @Override
    public void get() {
        commandInvoker.executePaste(target, localStorage.getItem(CLIPBOARD), target.getLastContextMenuPosition());
    }

    @Override
    public String getSaved(String name) {
        return localStorage.getItem(SAVE_PREFIX + name);
    }

    @Override
    public Map<String, String> getAllSaved(boolean removePrefixFromKey) {
        Map<String, String> returnList = new HashMap<String, String>();
        for (int i = 0; i < localStorage.getLength(); i++) {
            String key = localStorage.key(i);
            if (key.startsWith(SAVE_PREFIX)) {
                if (removePrefixFromKey) {
                    key = key.substring(SAVE_PREFIX.length());
                }
                returnList.put(key, localStorage.getItem(key));
            }
        }
        return returnList;
    }

    @Override
    public void set(String value) {
        save(CLIPBOARD, value);
    }

    @Override
    public void setSaved(String name, String value) {
        save(SAVE_PREFIX + name, value);
    }

    @Override
    public void remove(String id) {
        localStorage.removeItem(SAVE_PREFIX + id);
    }

    @Override
    public boolean init() {
        try {
            localStorage = Storage.getLocalStorageIfSupported();
            return localStorage != null;
        } catch (Exception e) {
            return false; // Firefox with the Cookie setting "ask everytime" will throw an exception here!
        }
    }

    /**
     * Saves a value with the corresponding prefix to the local storage
     *
     * @param id    the id of the value ("Clipboard" or "s_{name}")
     * @param value the value to save.
     */
    private void save(String id, String value) {
        localStorage.setItem(id, value);
    }
}
