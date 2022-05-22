package com.fuyouj.sword.database.id;

import static com.fuyouj.sword.database.id.SnowflakeIdWorker.DEFAULT_WORKER;

public class SnowflakeLongGenerator implements Generator<Long> {
    @Override
    public Long next(final Object currentId) {
        return DEFAULT_WORKER.nextId();
    }
}
