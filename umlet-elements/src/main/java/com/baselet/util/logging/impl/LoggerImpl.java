package com.baselet.util.logging.impl;

import com.baselet.util.logging.LogLevel;
import com.baselet.util.logging.Logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

public class LoggerImpl implements Logger {
    private final String className;
    private LogLevel logLevel;
    private final java.util.logging.Logger logger;

    public LoggerImpl(String className) {
        this.className = className;
        this.logLevel = LogLevel.INFO;
        logger = java.util.logging.Logger.getLogger(this.className);
        logger.setLevel(convertLogLevel(this.logLevel));
    }

    public LoggerImpl(String className, LogLevel logLevel) {
        this.className = className;
        this.logLevel = logLevel;
        logger = java.util.logging.Logger.getLogger(this.className);
        logger.setLevel(convertLogLevel(this.logLevel));
    }

    @Override
    public void setLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
        logger.setLevel(convertLogLevel(this.logLevel));
    }

    @Override
    public boolean isTraceEnabled() {
        return this.logLevel.equals(LogLevel.TRACE);
    }

    @Override
    public LogLevel getLevel() {
        return logLevel;
    }

    @Override
    public void trace(String msg) {
        logger.log(convertLogLevel(LogLevel.TRACE), msg);
    }

    @Override
    public void trace(String msg, Throwable throwable) {
        logger.log(convertLogLevel(LogLevel.TRACE), msg, throwable);
    }

    @Override
    public void debug(String msg) {
        logger.log(convertLogLevel(LogLevel.DEBUG), msg);
    }

    @Override
    public void debug(String msg, Throwable throwable) {
        logger.log(convertLogLevel(LogLevel.DEBUG), msg, throwable);
    }

    @Override
    public void info(String msg) {
        logger.log(convertLogLevel(LogLevel.INFO), msg);
    }

    @Override
    public void info(String msg, Throwable throwable) {
        logger.log(convertLogLevel(LogLevel.INFO), msg, throwable);
    }

    @Override
    public void warn(String msg) {
        logger.log(convertLogLevel(LogLevel.WARN), msg);
    }

    @Override
    public void warn(String msg, Throwable throwable) {
        logger.log(convertLogLevel(LogLevel.WARN), msg, throwable);
    }

    @Override
    public void error(String msg) {
        logger.log(convertLogLevel(LogLevel.ERROR), msg);
    }

    @Override
    public void error(String msg, Throwable throwable) {
        logger.log(convertLogLevel(LogLevel.ERROR), msg, throwable);
    }

    private Level convertLogLevel(LogLevel level) {
        switch (level) {
            case TRACE:
                return Level.FINER;
            case DEBUG:
                return Level.FINE;
            case INFO:
                return Level.INFO;
            case WARN:
                return Level.WARNING;
            case ERROR:
                return Level.SEVERE;
            default:
                return Level.INFO;
        }
    }
}
