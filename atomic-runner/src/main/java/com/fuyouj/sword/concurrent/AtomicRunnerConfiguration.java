package com.fuyouj.sword.concurrent;

public class AtomicRunnerConfiguration {
    private static final int TIMEOUT_SECONDS = 10;
    public static final AtomicRunnerConfiguration DEFAULT = new AtomicRunnerConfiguration(TIMEOUT_SECONDS);
    private final int commandTimeoutSeconds;

    public AtomicRunnerConfiguration(final int commandTimeoutSeconds) {
        this.commandTimeoutSeconds = commandTimeoutSeconds;
    }

    public int getCommandTimeoutSeconds() {
        return commandTimeoutSeconds;
    }
}
