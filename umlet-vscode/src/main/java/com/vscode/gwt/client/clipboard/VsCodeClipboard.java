package com.vscode.gwt.client.clipboard;

import com.baselet.control.basics.geom.Point;
import com.baselet.gwt.client.clipboard.ClipboardStorage;
import com.baselet.gwt.client.logging.CustomLogger;
import com.baselet.gwt.client.logging.CustomLoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class VsCodeClipboard extends ClipboardStorage {
    private static Map<String, String> clipboard;
    private static final CustomLogger log = CustomLoggerFactory.getLogger(VsCodeClipboard.class);

    private Point pasteTargetPosition;

    @Override
    public native void get() /*-{
        this.@com.vscode.gwt.client.clipboard.VsCodeClipboard::savePasteTargetPosition()();
        var that = this;
        $wnd.vscode.postMessage({
            command: 'requestPasteClipboard'
        })

        var response = function () {
            return new Promise(function (resolve) {
                $wnd.addEventListener('message', resolve, {once: true})
            })
        }
        response().then(function (fulfilled) {
            if (fulfilled.data.command === 'paste-response') {
                that.@com.vscode.gwt.client.clipboard.VsCodeClipboard::pasteExternal(Ljava/lang/String;)(fulfilled.data.text)
            }
        });
    }-*/;

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
    public native void set(String value) /*-{
        $wnd.vscode.postMessage({
            command: 'setClipboard',
            text: value
        });
    }-*/;

    private void setExternal() {
        commandInvoker.copySelectedElements(target);
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
        initListener();
        return true;
    }

    private void pasteExternal(String data) {
        commandInvoker.executePaste(target, data, pasteTargetPosition);
        pasteTargetPosition = null;
    }

    private void savePasteTargetPosition() {
        pasteTargetPosition = target.getLastContextMenuPosition();
    }

    private void cutExternal() {
        commandInvoker.cutSelectedElements(target);
    }

    private native void initListener() /*-{
        var that = this;
        $wnd.addEventListener('message', function (event) {
            var message = event.data;
            switch (message.command) {
                case 'copy':
                    that.@com.vscode.gwt.client.clipboard.VsCodeClipboard::setExternal()();
                    break;
                case 'paste':
                    that.@com.vscode.gwt.client.clipboard.VsCodeClipboard::pasteExternal(Ljava/lang/String;)(message.text);
                    break;
                case 'cut':
                    that.@com.vscode.gwt.client.clipboard.VsCodeClipboard::cutExternal()();
                    break;
            }
        });
    }-*/;
}
