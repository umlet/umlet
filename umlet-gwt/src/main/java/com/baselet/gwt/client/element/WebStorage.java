package com.baselet.gwt.client.element;

import java.util.Collection;
import java.util.List;

import com.baselet.gwt.client.clipboard.ClipboardStorage;
import com.baselet.gwt.client.clipboard.LocalStorageClipboard;
import com.baselet.gwt.client.clipboard.VsCodeClipboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.element.interfaces.GridElement;
import com.baselet.gwt.client.BaseletGWT;
import com.baselet.gwt.client.view.VersionChecker;

public class WebStorage {
    private static ClipboardStorage clipboardStorage;
    static Logger log = LoggerFactory.getLogger(BaseletGWT.class);

    public static boolean initLocalStorage() {
        if (VersionChecker.GetVersion() == VersionChecker.Version.VSCODE) {
            clipboardStorage = new VsCodeClipboard();
        } else {
            clipboardStorage = new LocalStorageClipboard();
        }
        return clipboardStorage.init();
    }

    public static void addSavedDiagram(String name, String diagramXml) {
        clipboardStorage.setSaved(name, diagramXml);
    }

    public static void removeSavedDiagram(String chosenName) {
        clipboardStorage.remove(chosenName);
    }

    public static String getSavedDiagram(String name) {
        return clipboardStorage.getSaved(name);
    }

    public static Collection<String> getSavedDiagramKeys() {
        return clipboardStorage.getAllSaved(true).keySet();
    }

    public static void setClipboard(List<GridElement> gridelements) {
        clipboardStorage.set(DiagramXmlParser.gridElementsToXml(gridelements));
    }

    public static List<GridElement> getClipboard() {
        return DiagramXmlParser.xmlToGridElements(clipboardStorage.get());
    }

}
