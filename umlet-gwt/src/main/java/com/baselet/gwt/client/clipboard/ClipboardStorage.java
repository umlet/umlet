package com.baselet.gwt.client.clipboard;

import com.baselet.command.CommandTarget;
import com.baselet.gwt.client.view.CommandInvoker;
import com.baselet.gwt.client.view.DrawPanel;

import java.util.Map;

public abstract class ClipboardStorage {
    protected String CLIPBOARD = "Clipboard";
    protected String SAVE_PREFIX = "s_";

    protected DrawPanel target;    // Target of clipboard actions
    protected CommandInvoker commandInvoker = CommandInvoker.getInstance();

    /**
     * Returns the value inside the "clipboard" of the local storage.
     *
     * @return the value inside the "clipboard" of the local storage.
     */
    public abstract void get();

    /**
     * Returns a saved value (prefix "s_") from the clipboard.
     * @param name the name of the diagram.
     * @return the saved value.
     */
    public abstract String getSaved(String name);

    /**
     * Returns all saved values inside the clipboard as Map.
     * @param removePrefixFromKey If true, it removes the "s_"-prefix from the key.
     * @return saved diagrams as Map.
     */
    public abstract Map<String, String> getAllSaved(boolean removePrefixFromKey);

    /**
     * Saves a value to the clipboard.
     *
     * @param value the value to be saved.
     */
    public abstract void set(String value);

    /**
     * Saves a value with prefix "s_" to the local storage.
     * @param name the name of the value.
     * @param value the value to save.
     */
    public abstract void setSaved(String name, String value);

    /**
     * Deletes the value with the given id ("s_{id}") from the clipboard.
     *
     * @param id the ID of the value.
     */
    public abstract void remove(String id);

    /**
     * Initializes the clipboard.
     */
    public abstract boolean init();

    public void updateTargetPanel(DrawPanel target) {
        this.target = target;
    }
}
