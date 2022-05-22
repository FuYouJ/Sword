package com.fuyouj.sword.database.wal;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thingworks.jarvis.concurrent.utils.Exceptions2;
import com.thingworks.jarvis.persistent.utils.Bytes;
import com.thingworks.jarvis.persistent.wal.exception.InvalidWalFileHeader;
import com.thingworks.jarvis.persistent.wal.exception.UnableToAccessWalFile;

public class WalHeader {
    public static final int FIXED_BYTE_SIZE = 4 + 1 + 8;
    public static final byte[] CHANGE_LOG_MAGIC = "ZLOG".getBytes();
    public static final byte VERSION = 1;
    private static final Logger LOGGER = LoggerFactory.getLogger(WalHeader.class);
    private final long firstEntryIndex;

    public WalHeader(final long firstEntryIndex) {
        this.firstEntryIndex = firstEntryIndex;
    }

    public static WalHeader read(final File file, final DataInputStream reader) {

        try {
            byte[] magic = new byte[CHANGE_LOG_MAGIC.length];
            reader.readFully(magic);

            if (!Bytes.equal(magic, CHANGE_LOG_MAGIC)) {
                throw new InvalidWalFileHeader();
            }

            if (reader.readByte() != VERSION) {
                throw new InvalidWalFileHeader();
            }

            long entryIndex = reader.readLong();

            if (entryIndex <= 0) {
                throw new InvalidWalFileHeader();
            }

            return new WalHeader(entryIndex);
        } catch (IOException e) {
            LOGGER.warn("Failed to read wal file [{}], SKIP; because of [{}]",
                    file.getName(),
                    Exceptions2.extractMessage(e));
            throw new UnableToAccessWalFile(Exceptions2.extractMessage(e));
        }
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(FIXED_BYTE_SIZE);

        buffer.put(CHANGE_LOG_MAGIC);
        buffer.put(VERSION);
        buffer.putLong(firstEntryIndex);

        return buffer.array();
    }
}
