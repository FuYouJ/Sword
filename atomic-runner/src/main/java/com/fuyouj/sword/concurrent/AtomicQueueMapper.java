package com.fuyouj.sword.concurrent;

/**
 * 原子操作队列的归类器，根据给定的命令<b>Command</b>的<b>Key</b>归类到不同的FIFO的队列中；
 * 所有需要进行原子操作的命令都被串行化。
 */
public interface AtomicQueueMapper<K> {
    /**
     * 对命令进行归类
     *
     * @param atomicKey ，任意命令
     * @return SequenceQueue，返回被归类的队列
     */
    AtomicQueue<K> group(K atomicKey);
}
