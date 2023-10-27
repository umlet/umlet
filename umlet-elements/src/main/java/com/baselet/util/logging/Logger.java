package com.baselet.util.logging;


public interface Logger {
	void setLevel(LogLevel logLevel);
	boolean isTraceEnabled();
	LogLevel getLevel();
	void trace(String msg);
	void trace(String msg, Throwable throwable);
	void debug(String msg);
	void debug(String msg, Throwable throwable);
	void info(String msg);
	void info(String msg, Throwable throwable);
	void warn(String msg);
	void warn(String msg, Throwable throwable);
	void error(String msg);
	void error(String msg, Throwable throwable);
}
