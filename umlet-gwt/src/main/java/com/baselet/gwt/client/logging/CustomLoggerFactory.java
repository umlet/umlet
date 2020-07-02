package com.baselet.gwt.client.logging;

import com.google.gwt.core.client.GWT;

public class CustomLoggerFactory {
    public static CustomLogger getLogger(Class<?> clazz) {
        CustomLogger logger = GWT.create(GWTLogger.class);
        logger.init(clazz);
        return logger;
    }
}
