package com.fuyouj.sword.database.data.array;

public interface ArrayDataStreamHook<T> {
    void onDeleteByIndex(ArrayDataStream<T> dataStream, long index, T value);

    void onClose(ArrayDataStream<T> dataStream);

    void onValueAdded(ArrayDataStream<T> dataStream, long index, T data);

    void onValuesClear(ArrayDataStream<T> dataStream);
}
