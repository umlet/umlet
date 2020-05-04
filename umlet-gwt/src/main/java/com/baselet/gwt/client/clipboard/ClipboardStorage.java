package com.baselet.gwt.client.clipboard;

import java.util.Map;

public interface ClipboardStorage {
    String CLIPBOARD = "Clipboard";
    String SAVE_PREFIX = "s_";

    /**
     * Returns the value inside the "clipboard" of the local storage.
     *
     * @return the value inside the "clipboard" of the local storage.
     */
    String get();

    /**
     * Returns a saved value (prefix "s_") from the clipboard.
     * @param name the name of the diagram.
     * @return the saved value.
     */
    String getSaved(String name);

    /**
     * Returns all saved values inside the clipboard as Map.
     * @param removePrefixFromKey If true, it removes the "s_"-prefix from the key.
     * @return saved diagrams as Map.
     */
    Map<String, String> getAllSaved(boolean removePrefixFromKey);

    /**
     * Saves a value to the clipboard.
     *
     * @param value the value to be saved.
     */
    void set(String value);

    /**
     * Saves a value with prefix "s_" to the local storage.
     * @param name the name of the value.
     * @param value the value to save.
     */
    void setSaved(String name, String value);

    /**
     * Deletes the value with the given id ("s_{id}") from the clipboard.
     *
     * @param id the ID of the value.
     */
    void remove(String id);

    /**
     * Initializes the clipboard.
     */
    boolean init();
}
