package com.fuyouj.sword.scabard.command;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

public class LoggerCache {
    public static final ConcurrentHashMap<Class, Logger> CACHE = new ConcurrentHashMap<>();
}
