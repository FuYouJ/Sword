package com.fuyouj.sword.database.snapshot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuyouj.sword.database.snapshot.exception.FailedToWriteSnapshot;
import com.fuyouj.sword.database.snapshot.exception.InvalidSnapshotFileHeader;
import com.fuyouj.sword.database.snapshot.exception.UnableToAccessSnapshotFile;
import com.fuyouj.sword.database.utils.Bytes;
import com.fuyouj.sword.database.wal.exception.InvalidWalFileHeader;
import com.fuyouj.sword.scabard.Exceptions2;

public class SnapshotHeader {
    public static final int FIXED_BYTE_SIZE = 4 + 1 + 8;
    public static final byte[] SNAPSHOT_MAGIC = "SNAP".getBytes();
    public static final byte VERSION = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(SnapshotHeader.class);

    private final long ts;

    public SnapshotHeader(final long ts) {
        this.ts = ts;
    }

    public static SnapshotHeader read(final File file, final DataInputStream reader) {
        try {
            byte[] magic = new byte[SNAPSHOT_MAGIC.length];
            reader.readFully(magic);

            if (!Bytes.equal(magic, SNAPSHOT_MAGIC)) {
                throw new InvalidSnapshotFileHeader("magic value invalid");
            }

            if (reader.readByte() != VERSION) {
                throw new InvalidSnapshotFileHeader("version invalid");
            }

            long ts = reader.readLong();

            if (ts < 0) {
                throw new InvalidWalFileHeader("ts invalid");
            }

            return new SnapshotHeader(ts);
        } catch (IOException e) {
            LOGGER.warn("Failed to read snapshot file [{}], SKIP; because of [{}]",
                    file.getName(),
                    Exceptions2.extractMessage(e));
            throw new UnableToAccessSnapshotFile(file, Exceptions2.extractMessage(e));
        }
    }

    public static SnapshotHeader write(final File file, final DataOutputStream writer, final long createAt) {
        try {
            SnapshotHeader header = new SnapshotHeader(createAt);
            writer.write(header.toBytes());

            return header;
        } catch (IOException e) {
            throw new FailedToWriteSnapshot(String.format(
                    "Failed to append  [%s] because of [%s]",
                    file.getAbsolutePath(),
                    e.getMessage()
            ));
        }
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(FIXED_BYTE_SIZE);

        buffer.put(SNAPSHOT_MAGIC);
        buffer.put(VERSION);
        buffer.putLong(ts);

        return buffer.array();
    }
}
