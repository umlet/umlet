package com.baselet.gwt.client.clipboard;

import java.util.HashMap;
import java.util.Map;

public class VsCodeClipboard implements ClipboardStorage {
    private Map<String, String> clipboard;

    @Override
    public String get() {
        return clipboard.get(CLIPBOARD);
    }

    @Override
    public String getSaved(String name) {
        return clipboard.get(SAVE_PREFIX + name);
    }

    @Override
    public Map<String, String> getAllSaved(boolean removePrefixFromKey) {
        Map<String, String> returnList = new HashMap<>();

        for (String key : clipboard.keySet()) {
            if (key.startsWith(SAVE_PREFIX)) {
                if (removePrefixFromKey) {
                    key = key.substring(SAVE_PREFIX.length());
                }
                returnList.put(key, clipboard.get(key));
            }
        }
        return returnList;
    }

    @Override
    public void set(String value) {
        clipboard.put(CLIPBOARD, value);
    }

    @Override
    public void setSaved(String name, String value) {
        clipboard.put(SAVE_PREFIX + name, value);
    }

    @Override
    public void remove(String id) {
        clipboard.remove(id);
    }

    @Override
    public boolean init() {
        clipboard = new HashMap<>();
        return true;
    }
}
