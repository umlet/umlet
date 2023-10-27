package com.baselet.util.logging;

import com.baselet.util.logging.impl.LoggerImpl;

public class LoggerFactory {
    public static Logger getLogger(Class clazz) {
        return new LoggerImpl(clazz.getName());
    }

    public Logger getLogger(Class clazz, LogLevel logLevel) {
        return new LoggerImpl(clazz.getName(), logLevel);
    }
}
