package com.fuyouj.sword.scabard.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Loggable {

    /**
     * @param message
     * @param arguments
     * @see org.slf4j.Logger#debug(String, Object...)
     */
    default void debug(final String message, Object... arguments) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(message, arguments);
        }
    }

    /**
     * @param message
     * @param throwable
     * @see org.slf4j.Logger#debug(String, Throwable)
     */
    default void debug(final String message, Throwable throwable) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(message, throwable);
        }
    }

    /**
     * @param message
     * @param arguments
     * @see org.slf4j.Logger#error(String, Object...)
     */
    default void error(final String message, Object... arguments) {
        if (getLogger().isErrorEnabled()) {
            getLogger().error(message, arguments);
        }
    }

    /**
     * @param message
     * @param throwable
     * @see org.slf4j.Logger#error(String, Throwable)
     */
    default void error(final String message, Throwable throwable) {
        if (getLogger().isErrorEnabled()) {
            getLogger().error(message, throwable);
        }
    }

    default Logger getLogger() {
        return LoggerCache.CACHE.computeIfAbsent(this.getClass(), LoggerFactory::getLogger);
    }

    /**
     * @param message
     * @param arguments
     * @see Logger#info(String, Object...)
     */
    default void info(final String message, Object... arguments) {
        if (getLogger().isInfoEnabled()) {
            getLogger().info(message, arguments);
        }
    }

    /**
     * @param message
     * @param arguments
     * @see Logger#warn(String, Object...)
     */
    default void warn(final String message, Object... arguments) {
        if (getLogger().isWarnEnabled()) {
            getLogger().warn(message, arguments);
        }
    }

    /**
     * @param message
     * @param throwable
     * @see Logger#warn(String, Throwable)
     */
    default void warn(final String message, Throwable throwable) {
        if (getLogger().isWarnEnabled()) {
            getLogger().warn(message, throwable);
        }
    }
}
