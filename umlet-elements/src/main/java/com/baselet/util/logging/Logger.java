package com.baselet.util.logging;


public interface Logger {
	/**
	 * Set a new log level for the logger
	 * @param logLevel new level of the logger as com.baselet.util.logging.LogLevel
	 */
	void setLevel(LogLevel logLevel);

	/**
	 * Get the current log level of the logger
	 * @return level of the logger as com.baselet.util.logging.LogLevel
	 */
	LogLevel getLevel();

	/**
	 * Check whether trace logging is enabled
	 * @return boolean indicating if tracing is enabled
	 */
	boolean isTraceEnabled();

	/**
	 * Log a trace message
	 * @param msg message to log as string
	 */
	void trace(String msg);

	/**
	 * Log a trace message, including its throwable
	 * @param msg message to log as string
	 * @param throwable throwable to log
	 */
	void trace(String msg, Throwable throwable);

	/**
	 * Log a debug message
	 * @param msg message to log as string
	 */
	void debug(String msg);

	/**
	 * Log a debug message, including its throwable
	 * @param msg message to log as string
	 * @param throwable throwable to log
	 */
	void debug(String msg, Throwable throwable);

	/**
	 * Log an info message
	 * @param msg message to log as string
	 */
	void info(String msg);

	/**
	 * Log an info message, including its throwable
	 * @param msg message to log as string
	 * @param throwable throwable to log
	 */
	void info(String msg, Throwable throwable);

	/**
	 * Log a warn message
	 * @param msg message to log as string
	 */
	void warn(String msg);

	/**
	 * Log a warn message, including its throwable
	 * @param msg message to log as string
	 * @param throwable throwable to log
	 */
	void warn(String msg, Throwable throwable);

	/**
	 * Log an error message
	 * @param msg message to log as string
	 */
	void error(String msg);

	/**
	 * Log an error message, including its throwable
	 * @param msg message to log as string
	 * @param throwable throwable to log
	 */
	void error(String msg, Throwable throwable);
}
