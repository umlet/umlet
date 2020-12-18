package com.baselet.gwt.client.scripts;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface ScriptResource extends ClientBundle {
    @Source("blob-stream.js")
    TextResource blobStream();

    @Source("buffer@6.0.2.js")
    TextResource buffer();

    @Source("pdfkit.standalone.js")
    TextResource pdfKit();
}
