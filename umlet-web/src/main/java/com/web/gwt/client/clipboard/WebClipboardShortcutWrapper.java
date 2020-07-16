package com.web.gwt.client.clipboard;

import com.baselet.command.CommandTarget;
import com.baselet.gwt.client.clipboard.ClipboardShortcutWrapper;

public class WebClipboardShortcutWrapper extends ClipboardShortcutWrapper {
    @Override
    public void onCopy(CommandTarget target) {
        super.commandInvoker.copySelectedElements(target);
    }

    @Override
    public void onPaste() {
        super.commandInvoker.pasteElements();
    }

    @Override
    public void onCut(CommandTarget target) {
        super.commandInvoker.cutSelectedElements(target);
    }
}
