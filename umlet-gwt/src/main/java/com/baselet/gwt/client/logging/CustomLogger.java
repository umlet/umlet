package com.baselet.gwt.client.logging;

public interface CustomLogger {
    void trace(String message);

    void debug(String message);

    void debug(String message, Throwable throwable);

    void info(String message);

    void error(String message);

    void error(String message, Throwable throwable);

    void init(Class<?> clazz);
}
