package com.fuyouj.sword.database.snapshot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.fuyouj.sword.database.snapshot.exception.FailedToWriteSnapshot;
import com.fuyouj.sword.database.utils.Bytes;
import com.fuyouj.sword.scabard.Exceptions2;

import lombok.Getter;

class SnapshotEntry {
    static final int CRC_BYTE_LENGTH = 4;
    static final int DATA_LENGTH_BYTE_LENGTH = 4;

    static final short FIXED_BYTE_SIZE = CRC_BYTE_LENGTH + DATA_LENGTH_BYTE_LENGTH;

    /**
     * 数据的Crc校验码，用于验证数据是否有损坏，如果损坏则该条记录已经后续的所有记录均失效
     */
    private final int crc;
    private final int size;
    @Getter
    private final byte[] data;

    SnapshotEntry(final int crc, final int size, final byte[] data) {
        this.crc = crc;
        this.size = size;
        this.data = data;
    }

    boolean isValid() {
        return Bytes.crc32(data) == this.crc;
    }

    static SnapshotEntry read(final DataInputStream dataInput) throws IOException {
        final int crc = dataInput.readInt();
        final int size = dataInput.readInt();

        final byte[] data = new byte[size];
        dataInput.readFully(data);

        return new SnapshotEntry(crc, size, data);
    }

    static void write(final DataOutputStream writer, final byte[] data) {
        try {
            writer.writeInt(Bytes.crc32(data));
            writer.writeInt(data.length);
            writer.write(data);
        } catch (IOException e) {
            throw new FailedToWriteSnapshot(Exceptions2.extractMessage(e));
        }
    }
}
