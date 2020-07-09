package com.web.gwt.client.file;

import com.baselet.gwt.client.file.FileChangeNotifier;

public class WebFileChangeNotifier implements FileChangeNotifier {
    @Override
    public void notifyFileChange(String diagramXMLState) {
        // Web does not support this feature
    }
}
