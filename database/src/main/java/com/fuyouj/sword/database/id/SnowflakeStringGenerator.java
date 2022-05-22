package com.fuyouj.sword.database.id;

import static com.fuyouj.sword.database.id.SnowflakeIdWorker.DEFAULT_WORKER;

public class SnowflakeStringGenerator implements Generator<String> {
    @Override
    public String next(final Object currentId) {
        return String.valueOf(DEFAULT_WORKER.nextId());
    }
}
