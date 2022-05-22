package com.fuyouj.sword.database.wal;

import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.thingworks.jarvis.concurrent.utils.Exceptions2;
import com.thingworks.jarvis.persistent.utils.Bytes;
import com.thingworks.jarvis.persistent.wal.exception.FailedToAppendWal;
import com.thingworks.jarvis.persistent.wal.exception.UnableToAccessWalFile;

/**
 * 表示一条预写日志记录
 * 8字节：entryIndex
 * 1字节：entryType
 * 4字节：crc校验码
 * 4字节：dataLength
 * n字节：data
 */
class WALEntry {
    static final int ENTRY_INDEX_BYTE_LENGTH = 8;
    static final int ENTRY_TYPE_BYTE_LENGTH = 1;
    static final int CRC_BYTE_LENGTH = 4;
    static final int DATA_LENGTH_BYTE_LENGTH = 4;

    static final short FIXED_BYTE_SIZE = ENTRY_INDEX_BYTE_LENGTH
            + ENTRY_TYPE_BYTE_LENGTH
            + CRC_BYTE_LENGTH
            + DATA_LENGTH_BYTE_LENGTH;
    /**
     * 预写日志的唯一自增序号
     */
    long entryIndex = -1;
    /**
     * 该日志的类型，最大可表示的类型有127种
     */
    private byte entryType;
    /**
     * 数据的Crc校验码，用于验证数据是否有损坏，如果损坏则该条记录已经后续的所有记录均失效
     */
    private Integer crc;
    /**
     *
     */
    private int size;
    private byte[] data;

    WALEntry(final long entryIndex,
             final byte entryType,
             final int crc,
             final int size,
             final byte[] data) {
        this.entryIndex = entryIndex;
        this.entryType = entryType;
        this.crc = crc;
        this.size = size;
        this.data = data;
    }

    byte[] getData() {
        return data;
    }

    long getEntryIndex() {
        return entryIndex;
    }

    EntryType getEntryType() {
        return EntryTypeFactory.check(entryType);
    }

    boolean isValid() {
        return Bytes.crc32(data) == this.crc;
    }

    static WALEntry read(final DataInput dataInput) {
        try {
            final long entryIndex = dataInput.readLong();
            final byte entryType = dataInput.readByte();
            final int crc = dataInput.readInt();
            final int size = dataInput.readInt();
            final byte[] data = new byte[size];
            dataInput.readFully(data);

            return new WALEntry(entryIndex, entryType, crc, size, data);
        } catch (IOException e) {
            throw new UnableToAccessWalFile(Exceptions2.extractMessage(e));
        }
    }

    byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(FIXED_BYTE_SIZE + data.length);

        buffer.putLong(this.entryIndex);
        buffer.put(entryType);
        buffer.putInt(Bytes.crc32(data));
        buffer.put(data);

        return buffer.array();
    }

    static void write(final DataOutputStream writer,
                      final long entryIndex,
                      final EntryType type,
                      final byte[] data) {
        try {
            writer.writeLong(entryIndex);
            writer.writeByte(type.getType());
            writer.writeInt(Bytes.crc32(data));
            writer.writeInt(data.length);
            writer.write(data);
        } catch (IOException e) {
            throw new FailedToAppendWal(Exceptions2.extractMessage(e));
        }
    }
}
