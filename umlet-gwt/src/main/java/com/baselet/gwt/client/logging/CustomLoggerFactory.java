package com.baselet.gwt.client.logging;

import com.baselet.gwt.client.view.VersionChecker;

public class CustomLoggerFactory {
    public static CustomLogger getLogger(Class<?> clazz) {
        if (VersionChecker.isVsCodeVersion()) {
            return new VsCodeLogger();
        } else {
            return new GWTLogger(clazz);
        }
    }
}
