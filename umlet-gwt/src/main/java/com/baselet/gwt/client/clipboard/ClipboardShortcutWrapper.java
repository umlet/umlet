package com.baselet.gwt.client.clipboard;

import com.baselet.command.CommandTarget;
import com.baselet.gwt.client.view.CommandInvoker;

public class ClipboardShortcutWrapper {
    protected CommandInvoker commandInvoker = CommandInvoker.getInstance();

    public void onCopy(CommandTarget target) {
        // Override if something shall be executed
    }

    public void onPaste() {
        // Override if something shall be executed
    }

    public void onCut(CommandTarget target) {
        // Override if something shall be executed
    }
}
