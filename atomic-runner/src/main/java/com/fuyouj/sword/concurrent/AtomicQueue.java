package com.fuyouj.sword.concurrent;

import java.util.concurrent.ExecutorService;

import com.fuyouj.sword.concurrent.runner.AtomicResult;

/**
 * 原子性命令队列
 */
public interface AtomicQueue<K> {
    /**
     * 每个队列都有一个唯一Key
     *
     * @return queue key
     */
    K getKey();

    /**
     * 将命令推送进原子操作队列，并指定执行器；并等待执行结果
     *
     * @param executorService 执行器
     * @param cmd             被推送的命令
     */
    <T> AtomicResult<T> pushAndGet(ExecutorService executorService, AtomicCommand<K, T> cmd);
}
