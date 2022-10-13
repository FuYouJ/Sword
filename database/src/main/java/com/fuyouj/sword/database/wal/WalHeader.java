package com.fuyouj.sword.database.wal;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuyouj.sword.database.utils.Bytes;
import com.fuyouj.sword.database.wal.exception.InvalidWalFileHeader;
import com.fuyouj.sword.database.wal.exception.UnableToAccessWalFile;
import com.fuyouj.sword.scabard.Exceptions2;

public class WalHeader {
    public static final int FIXED_BYTE_SIZE = 4 + 1 + 8;
    public static final byte[] CHANGE_LOG_MAGIC = "ZLOG".getBytes();
    public static final byte VERSION = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(WalHeader.class);

    private final long ts;

    public WalHeader(final long ts) {
        this.ts = ts;
    }

    public static WalHeader read(final File file, final DataInputStream reader) {
        try {
            byte[] magic = new byte[CHANGE_LOG_MAGIC.length];
            reader.readFully(magic);

            if (!Bytes.equal(magic, CHANGE_LOG_MAGIC)) {
                throw new InvalidWalFileHeader("magic value invalid");
            }

            if (reader.readByte() != VERSION) {
                throw new InvalidWalFileHeader("version invalid");
            }

            long entryIndex = reader.readLong();

            if (entryIndex <= 0) {
                throw new InvalidWalFileHeader("entryIndex invalid");
            }

            return new WalHeader(entryIndex);
        } catch (IOException e) {
            LOGGER.warn("Failed to read wal file [{}], SKIP; because of [{}]",
                    file.getName(),
                    Exceptions2.extractMessage(e));
            throw new UnableToAccessWalFile(file, Exceptions2.extractMessage(e));
        }
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(FIXED_BYTE_SIZE);

        buffer.put(CHANGE_LOG_MAGIC);
        buffer.put(VERSION);
        buffer.putLong(ts);

        return buffer.array();
    }
}
