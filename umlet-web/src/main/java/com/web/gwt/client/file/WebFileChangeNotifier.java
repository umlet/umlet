package com.web.gwt.client.file;

import com.baselet.element.interfaces.Diagram;
import com.baselet.gwt.client.file.FileChangeNotifier;

public class WebFileChangeNotifier implements FileChangeNotifier {
    @Override
    public void notifyFileChange(Diagram diagram) {
        // Web does not support this feature
    }
}
